package com.kcharignon.petitsjeux.core.update

import java.io.File

fun release(code: Long, name: String = "$code.0.0", sha: String = "a".repeat(64)) =
    ReleaseInfo(name, code, ReleaseInfo.OFFICIAL_PREFIX + "android-v$name/petitsjeux.apk", sha)

class FakeUpdateApi(
    var latest: ReleaseInfo = release(2),
    var failFetch: Boolean = false,
    var failDownload: Boolean = false,
    /** Si vrai, le téléchargement s'arrête à 45 % et attend (pour photographier l'état « téléchargement »). */
    var holdDownload: Boolean = false,
) : UpdateApi {
    var fetches = 0
    var downloads = 0

    override suspend fun fetchLatest(): ReleaseInfo {
        fetches++
        if (failFetch) error("pas de réseau")
        return latest
    }

    override suspend fun download(info: ReleaseInfo, into: File, onProgress: (Float) -> Unit) {
        downloads++
        onProgress(0.45f)
        if (holdDownload) kotlinx.coroutines.awaitCancellation()
        if (failDownload) error("coupé")
        into.parentFile?.mkdirs()
        into.writeText("faux apk")
        onProgress(1f)
    }
}

class FakeInstaller(var allowed: Boolean = true) : ApkInstaller {
    val installed = mutableListOf<File>()
    var permissionRequests = 0

    override fun canInstall() = allowed

    override fun requestPermission() {
        permissionRequests++
    }

    override fun install(apk: File) {
        installed += apk
    }
}
