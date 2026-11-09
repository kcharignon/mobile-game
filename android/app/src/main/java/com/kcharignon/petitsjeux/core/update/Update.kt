package com.kcharignon.petitsjeux.core.update

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

/** La dernière version publiée, telle que la décrit `version.json` (publié avec chaque release : android/PUBLICATION.md). */
data class ReleaseInfo(val versionName: String, val versionCode: Long, val apkUrl: String, val sha256: String) {
    companion object {
        /** Seul ce dépôt GitHub peut fournir une mise à jour. */
        const val OFFICIAL_PREFIX = "https://github.com/kcharignon/mobile-game/releases/download/"

        /** Lève une exception si le texte n'est pas un `version.json` valide qui pointe vers [allowedPrefix]. */
        fun parse(json: String, allowedPrefix: String = OFFICIAL_PREFIX): ReleaseInfo {
            val o = JSONObject(json)
            val info = ReleaseInfo(o.getString("versionName"), o.getLong("versionCode"), o.getString("apkUrl"), o.getString("sha256").lowercase())
            require(info.versionCode > 0) { "numéro de version invalide" }
            require(info.apkUrl.startsWith(allowedPrefix)) { "adresse de téléchargement non autorisée : ${info.apkUrl}" }
            require(Regex("[0-9a-f]{64}").matches(info.sha256)) { "empreinte SHA-256 invalide" }
            return info
        }
    }
}

/** Où en est la mise à jour. */
sealed interface UpdateState {
    /** Rien n'a été demandé (ou la vérification discrète du lancement n'a rien trouvé). */
    data object Idle : UpdateState

    data object Checking : UpdateState

    data class UpToDate(val versionName: String) : UpdateState

    /** Une version plus récente existe. */
    data class Available(val info: ReleaseInfo) : UpdateState

    data class Downloading(val info: ReleaseInfo, val progress: Float) : UpdateState

    /** Android a été prié d'installer : la fenêtre de confirmation du système est (ou vient d'être) affichée. */
    data class ReadyToInstall(val info: ReleaseInfo, val apk: File) : UpdateState

    /** Android demande d'abord l'accord du joueur, une seule fois, pour installer depuis cette appli. */
    data class NeedsPermission(val info: ReleaseInfo, val apk: File) : UpdateState

    /** Échec : [info] est connue si l'échec a eu lieu pendant la mise à jour (on peut réessayer sans tout refaire). */
    data class Failed(val reason: Reason, val info: ReleaseInfo? = null) : UpdateState

    enum class Reason { NETWORK, DOWNLOAD }
}

/** Le réseau : demander la dernière version, télécharger l'APK (en vérifiant son empreinte). */
interface UpdateApi {
    suspend fun fetchLatest(): ReleaseInfo

    /** Télécharge [info] dans [into] ; lève une exception si le téléchargement échoue ou si l'empreinte ne correspond pas. */
    suspend fun download(info: ReleaseInfo, into: File, onProgress: (Float) -> Unit)
}

/** Android : peut-on installer, demander l'accord, lancer l'installation. */
interface ApkInstaller {
    fun canInstall(): Boolean

    /** Ouvre la page d'Android où le joueur autorise l'appli à installer des mises à jour. */
    fun requestPermission()

    /** Ouvre la fenêtre d'installation d'Android pour [apk]. */
    fun install(apk: File)
}

/**
 * La mise à jour de l'appli, pensée pour quelqu'un qui ne sait pas ce qu'est un APK : l'appli vérifie seule qu'une nouvelle version existe,
 * un gros bouton la télécharge, puis Android demande de confirmer l'installation. Elle ne se connecte à Internet que pour cela (GitHub),
 * et Android refuse toute mise à jour qui ne serait pas signée de la même clé : personne d'autre ne peut en pousser une.
 *
 * L'état est un état Compose : l'interface se met à jour toute seule.
 */
class UpdateController(
    private val api: UpdateApi,
    private val installer: ApkInstaller,
    /** Numéro de version de l'appli installée. */
    private val installedVersionCode: Long,
    private val workDir: File,
    private val scope: CoroutineScope,
) {
    var state: UpdateState by mutableStateOf(UpdateState.Idle)
        private set

    init {
        // les fichiers d'une mise à jour déjà installée (ou à moitié téléchargés) ne servent plus à rien
        runCatching {
            workDir.listFiles()?.forEach { file ->
                val code = Regex("petitsjeux-(\\d+)\\.apk").matchEntire(file.name)?.groupValues?.get(1)?.toLongOrNull()
                if (code == null || code <= installedVersionCode) file.delete()
            }
        }
    }

    private val busy: Boolean get() = state is UpdateState.Checking || state is UpdateState.Downloading

    /**
     * Cherche une version plus récente. [silent] : au lancement de l'appli, un échec ne dit rien (pas de réseau, ce n'est pas grave).
     */
    fun check(silent: Boolean) {
        if (busy) return
        state = UpdateState.Checking
        scope.launch {
            state = try {
                val info = api.fetchLatest()
                if (info.versionCode > installedVersionCode) UpdateState.Available(info) else UpdateState.UpToDate(info.versionName)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (silent) UpdateState.Idle else UpdateState.Failed(UpdateState.Reason.NETWORK)
            }
        }
    }

    /** Télécharge la nouvelle version, puis lance l'installation (ou demande d'abord l'accord d'Android). */
    fun update() {
        val info = when (val s = state) {
            is UpdateState.Available -> s.info
            is UpdateState.Failed -> s.info
            else -> null
        }
        if (info == null) {
            if (state is UpdateState.Failed) check(silent = false)
            return
        }
        state = UpdateState.Downloading(info, 0f)
        scope.launch {
            val apk = File(workDir, "petitsjeux-${info.versionCode}.apk")
            try {
                workDir.mkdirs()
                api.download(info, apk) { state = UpdateState.Downloading(info, it.coerceIn(0f, 1f)) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                apk.delete()
                state = UpdateState.Failed(UpdateState.Reason.DOWNLOAD, info)
                return@launch
            }
            proceed(info, apk)
        }
    }

    private fun proceed(info: ReleaseInfo, apk: File) {
        if (installer.canInstall()) {
            state = UpdateState.ReadyToInstall(info, apk)
            installer.install(apk)
        } else {
            state = UpdateState.NeedsPermission(info, apk)
        }
    }

    /** Le joueur a annulé la fenêtre d'installation d'Android : on la rouvre, sans retélécharger. */
    fun installAgain() {
        val s = state as? UpdateState.ReadyToInstall ?: return
        installer.install(s.apk)
    }

    /** Le joueur touche « Autoriser » : on ouvre la page d'Android qui demande son accord. */
    fun askPermission() {
        if (state is UpdateState.NeedsPermission) installer.requestPermission()
    }

    /** À appeler quand l'appli revient au premier plan : si le joueur vient d'accorder l'autorisation, l'installation continue seule. */
    fun resume() {
        val s = state as? UpdateState.NeedsPermission ?: return
        if (installer.canInstall()) proceed(s.info, s.apk)
    }
}
