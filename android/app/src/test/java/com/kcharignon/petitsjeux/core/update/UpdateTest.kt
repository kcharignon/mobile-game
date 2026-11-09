package com.kcharignon.petitsjeux.core.update

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.net.InetAddress
import java.net.ServerSocket
import java.security.MessageDigest

class ReleaseInfoTest {
    private fun json(url: String = ReleaseInfo.OFFICIAL_PREFIX + "android-v0.3.0/petitsjeux.apk", sha: String = "A".repeat(64), code: Long = 3000) =
        """{"versionName":"0.3.0","versionCode":$code,"apkUrl":"$url","sha256":"$sha","minAndroid":26}"""

    @Test
    fun readsTheVersionJsonOfARelease() {
        val info = ReleaseInfo.parse(json())
        assertEquals("0.3.0", info.versionName)
        assertEquals(3000L, info.versionCode)
        assertEquals("a".repeat(64), info.sha256)
    }

    @Test
    fun refusesAnythingNotFromTheOfficialRepository() {
        assertTrue(runCatching { ReleaseInfo.parse(json(url = "https://exemple.com/petitsjeux.apk")) }.isFailure)
        assertTrue(runCatching { ReleaseInfo.parse(json(url = "http://github.com/kcharignon/mobile-game/releases/download/x.apk")) }.isFailure)
        assertTrue(runCatching { ReleaseInfo.parse(json(url = "https://github.com/autre/depot/releases/download/x.apk")) }.isFailure)
    }

    @Test
    fun refusesABadFingerprintOrVersion() {
        assertTrue(runCatching { ReleaseInfo.parse(json(sha = "xyz")) }.isFailure)
        assertTrue(runCatching { ReleaseInfo.parse(json(code = 0)) }.isFailure)
        assertTrue(runCatching { ReleaseInfo.parse("pas du json") }.isFailure)
    }
}

class UpdateControllerTest {
    @get:Rule
    val tmp = TemporaryFolder()

    private val work get() = File(tmp.root, "updates")

    private fun controller(api: FakeUpdateApi, installer: FakeInstaller = FakeInstaller(), installed: Long = 1) =
        UpdateController(api, installer, installed, work, CoroutineScope(Dispatchers.Unconfined))

    @Test
    fun aNewerVersionIsOffered() {
        val c = controller(FakeUpdateApi(latest = release(2)), installed = 1)
        c.check(silent = true)
        assertTrue(c.state is UpdateState.Available)
        assertEquals(2L, (c.state as UpdateState.Available).info.versionCode)
    }

    @Test
    fun theSameOrAnOlderVersionIsUpToDate() {
        for (installed in listOf(2L, 3L)) {
            val c = controller(FakeUpdateApi(latest = release(2, "0.2.0")), installed = installed)
            c.check(silent = false)
            assertEquals(UpdateState.UpToDate("0.2.0"), c.state)
        }
    }

    @Test
    fun aSilentCheckSaysNothingWhenThereIsNoNetwork() {
        val c = controller(FakeUpdateApi(failFetch = true))
        c.check(silent = true)
        assertEquals(UpdateState.Idle, c.state)
    }

    @Test
    fun aManualCheckTellsWhenThereIsNoNetwork() {
        val c = controller(FakeUpdateApi(failFetch = true))
        c.check(silent = false)
        assertEquals(UpdateState.Failed(UpdateState.Reason.NETWORK), c.state)
    }

    @Test
    fun updatingDownloadsThenOpensTheInstaller() {
        val installer = FakeInstaller(allowed = true)
        val c = controller(FakeUpdateApi(latest = release(2)), installer)
        c.check(silent = true)
        c.update()
        val ready = c.state as UpdateState.ReadyToInstall
        assertEquals(listOf(ready.apk), installer.installed)
        assertTrue(ready.apk.exists())
        assertEquals("petitsjeux-2.apk", ready.apk.name)
    }

    @Test
    fun withoutAndroidsConsentTheInstallWaitsForIt() {
        val installer = FakeInstaller(allowed = false)
        val c = controller(FakeUpdateApi(latest = release(2)), installer)
        c.check(silent = true)
        c.update()
        assertTrue(c.state is UpdateState.NeedsPermission)
        assertTrue(installer.installed.isEmpty())

        c.askPermission()
        assertEquals(1, installer.permissionRequests)

        // le joueur revient sans avoir autorisé : rien ne change
        c.resume()
        assertTrue(c.state is UpdateState.NeedsPermission)

        // il autorise, revient : l'installation continue toute seule
        installer.allowed = true
        c.resume()
        assertTrue(c.state is UpdateState.ReadyToInstall)
        assertEquals(1, installer.installed.size)
    }

    @Test
    fun aFailedDownloadCanBeRetriedWithoutStartingOver() {
        val api = FakeUpdateApi(latest = release(2), failDownload = true)
        val installer = FakeInstaller()
        val c = controller(api, installer)
        c.check(silent = true)
        c.update()
        assertEquals(UpdateState.Failed(UpdateState.Reason.DOWNLOAD, release(2)), c.state)
        assertEquals(1, api.fetches)

        api.failDownload = false
        c.update()
        assertTrue(c.state is UpdateState.ReadyToInstall)
        assertEquals(1, api.fetches) // pas de nouvelle recherche
        assertEquals(2, api.downloads)
    }

    @Test
    fun cancellingAndroidsWindowAllowsOpeningItAgain() {
        val installer = FakeInstaller()
        val c = controller(FakeUpdateApi(latest = release(2)), installer)
        c.check(silent = true)
        c.update()
        c.installAgain()
        assertEquals(2, installer.installed.size)
        assertEquals(installer.installed[0], installer.installed[1]) // même fichier : pas de nouveau téléchargement
    }

    @Test
    fun nothingHappensWithoutANewVersion() {
        val api = FakeUpdateApi()
        val c = controller(api)
        c.update()
        c.installAgain()
        c.askPermission()
        c.resume()
        assertEquals(UpdateState.Idle, c.state)
        assertEquals(0, api.downloads)
    }

    @Test
    fun oldDownloadsAreCleanedUpAtStart() {
        work.mkdirs()
        val old = File(work, "petitsjeux-1.apk").apply { writeText("x") }
        val partial = File(work, "petitsjeux-9.apk.part").apply { writeText("x") }
        val newer = File(work, "petitsjeux-5.apk").apply { writeText("x") }
        controller(FakeUpdateApi(), installed = 2)
        assertFalse(old.exists())
        assertFalse(partial.exists())
        assertTrue(newer.exists())
    }
}

/** Un mini serveur HTTP local (une requête à la fois) : chemin -> (code, en-têtes, corps). */
private class TinyServer(private val routes: Map<String, () -> Triple<Int, Map<String, String>, ByteArray>>) : AutoCloseable {
    private val socket = ServerSocket(0, 20, InetAddress.getByName("127.0.0.1"))
    val base = "http://127.0.0.1:${socket.localPort}/"

    init {
        Thread {
            while (!socket.isClosed) {
                try {
                    socket.accept().use { client ->
                        val reader = client.getInputStream().bufferedReader()
                        val path = reader.readLine()?.split(" ")?.getOrNull(1) ?: return@use
                        while (reader.readLine()?.isNotEmpty() == true) { /* en-têtes ignorés */ }
                        val (code, headers, body) = routes[path]?.invoke() ?: Triple(404, emptyMap(), ByteArray(0))
                        val out = client.getOutputStream()
                        val head = buildString {
                            append("HTTP/1.1 $code X\r\nContent-Length: ${body.size}\r\nConnection: close\r\n")
                            headers.forEach { (k, v) -> append("$k: $v\r\n") }
                            append("\r\n")
                        }
                        out.write(head.toByteArray()); out.write(body); out.flush()
                    }
                } catch (e: Exception) {
                    // serveur arrêté : on sort
                }
            }
        }.apply { isDaemon = true }.start()
    }

    override fun close() = socket.close()
}

class GitHubUpdateApiTest {
    @get:Rule
    val tmp = TemporaryFolder()

    private val apk = ByteArray(300_000) { (it * 31 % 251).toByte() }
    private fun sha(b: ByteArray) = MessageDigest.getInstance("SHA-256").digest(b).joinToString("") { "%02x".format(it) }

    private fun serve(block: (base: String, api: GitHubUpdateApi) -> Unit) {
        lateinit var base: String
        val ok = { Triple(200, emptyMap<String, String>(), apk) }
        TinyServer(
            mapOf(
                "/latest/version.json" to { Triple(200, emptyMap(), """{"versionName":"0.9.0","versionCode":9000,"apkUrl":"${base}download/real.apk","sha256":"${sha(apk)}"}""".toByteArray()) },
                "/download/redirect.apk" to { Triple(302, mapOf("Location" to base + "download/real.apk"), ByteArray(0)) },
                "/download/real.apk" to ok,
            ),
        ).use { server ->
            base = server.base
            block(base, GitHubUpdateApi(latestUrl = base + "latest/version.json", allowedPrefix = base))
        }
    }

    @Test
    fun readsTheLatestVersion() = serve { base, api ->
        val info = runBlocking { api.fetchLatest() }
        assertEquals(9000L, info.versionCode)
        assertEquals(base + "download/real.apk", info.apkUrl)
    }

    @Test
    fun downloadsFollowsRedirectsAndKeepsOnlyAFileWithTheRightFingerprint() = serve { base, api ->
        val target = File(tmp.root, "out/petitsjeux-9000.apk")
        target.parentFile.mkdirs()
        val progress = mutableListOf<Float>()
        runBlocking { api.download(release(9000, "0.9.0", sha(apk)).copy(apkUrl = base + "download/redirect.apk"), target) { progress += it } }
        assertEquals(apk.toList(), target.readBytes().toList())
        assertEquals(1f, progress.last(), 0f)
        assertTrue(progress.zipWithNext().all { (a, b) -> b >= a })
        assertEquals(listOf("petitsjeux-9000.apk"), target.parentFile.list()!!.toList()) // pas de fichier .part qui traîne
    }

    @Test
    fun aFileWithTheWrongFingerprintIsRefusedAndNothingIsKept() = serve { base, api ->
        val target = File(tmp.root, "out/petitsjeux-9000.apk")
        target.parentFile.mkdirs()
        val bad = release(9000, "0.9.0", "0".repeat(64)).copy(apkUrl = base + "download/real.apk")
        assertTrue(runCatching { runBlocking { api.download(bad, target) {} } }.isFailure)
        assertFalse(target.exists())
        assertEquals(emptyList<String>(), target.parentFile.list()!!.toList())
    }

    @Test
    fun aMissingFileIsAFailure() = serve { base, api ->
        val target = File(tmp.root, "out/x.apk")
        target.parentFile.mkdirs()
        assertTrue(runCatching { runBlocking { api.download(release(1).copy(apkUrl = base + "download/missing.apk"), target) {} } }.isFailure)
        assertFalse(target.exists())
    }

    @Test
    fun refusesAnUnencryptedAddressOutsideTheTestServer() {
        val api = GitHubUpdateApi(latestUrl = "http://exemple.com/version.json")
        assertTrue(runCatching { runBlocking { api.fetchLatest() } }.isFailure)
    }
}
