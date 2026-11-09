package com.kcharignon.petitsjeux.core.update

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.core.content.pm.PackageInfoCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/** La version installée : (nom « 0.3.0 », numéro de version). */
fun installedVersion(context: Context): Pair<String, Long> = runCatching {
    val info = context.packageManager.getPackageInfo(context.packageName, 0)
    (info.versionName ?: "?") to PackageInfoCompat.getLongVersionCode(info)
}.getOrDefault("?" to 0L)

/**
 * Le réseau, vers GitHub seulement : `version.json` de la release « android-latest » (adresse fixe), puis l'APK qu'il annonce.
 * Tout passe en HTTPS ; l'APK n'est gardé que si son empreinte SHA-256 est celle de `version.json`.
 */
class GitHubUpdateApi(
    private val latestUrl: String = "https://github.com/kcharignon/mobile-game/releases/download/android-latest/version.json",
    private val allowedPrefix: String = ReleaseInfo.OFFICIAL_PREFIX,
) : UpdateApi {
    private fun open(url: String): HttpURLConnection {
        require(url.startsWith("https://") || url.startsWith(allowedPrefix)) { "connexion non chiffrée refusée" }
        return (URL(url).openConnection() as HttpURLConnection).apply {
            connectTimeout = 15_000
            readTimeout = 30_000
            instanceFollowRedirects = true
            setRequestProperty("User-Agent", "PetitsJeux")
        }
    }

    override suspend fun fetchLatest(): ReleaseInfo = withContext(Dispatchers.IO) {
        val connection = open(latestUrl)
        try {
            check(connection.responseCode == 200) { "réponse ${connection.responseCode}" }
            ReleaseInfo.parse(connection.inputStream.bufferedReader().use { it.readText() }, allowedPrefix)
        } finally {
            connection.disconnect()
        }
    }

    override suspend fun download(info: ReleaseInfo, into: File, onProgress: (Float) -> Unit) = withContext(Dispatchers.IO) {
        val temp = File(into.parentFile, into.name + ".part")
        val connection = open(info.apkUrl)
        try {
            check(connection.responseCode == 200) { "réponse ${connection.responseCode}" }
            val total = connection.contentLengthLong
            val digest = MessageDigest.getInstance("SHA-256")
            var done = 0L
            connection.inputStream.use { input ->
                temp.outputStream().use { out ->
                    val buffer = ByteArray(64 * 1024)
                    while (true) {
                        val n = input.read(buffer)
                        if (n < 0) break
                        out.write(buffer, 0, n)
                        digest.update(buffer, 0, n)
                        done += n
                        if (total > 0) onProgress(done.toFloat() / total)
                    }
                }
            }
            val sha = digest.digest().joinToString("") { "%02x".format(it) }
            check(sha == info.sha256) { "empreinte inattendue : le fichier est refusé" }
            check(temp.renameTo(into)) { "fichier introuvable après téléchargement" }
            onProgress(1f)
        } catch (e: Exception) {
            temp.delete()
            throw e
        } finally {
            connection.disconnect()
        }
    }
}

/** L'installation par Android : la fenêtre « Installer » du système, via un fichier partagé le temps de l'installation. */
class AndroidApkInstaller(private val context: Context) : ApkInstaller {
    override fun canInstall(): Boolean = context.packageManager.canRequestPackageInstalls()

    override fun requestPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:${context.packageName}")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    override fun install(apk: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.updates", apk)
        val intent = Intent(Intent.ACTION_VIEW)
            .setDataAndType(uri, "application/vnd.android.package-archive")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
