package com.kcharignon.petitsjeux.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Mettre l'appli à jour : l'appli ne se connecte JAMAIS elle-même (aucune permission réseau). Le bouton des réglages ouvre simplement
 * le téléchargement de la dernière version dans le navigateur du téléphone ; on ouvre ensuite le fichier pour l'installer par-dessus
 * (les joueurs et leurs étoiles sont conservés : même clé de signature, même identifiant).
 */
object AppUpdate {
    /** Adresse fixe de la dernière version (release « android-latest », voir android/PUBLICATION.md). */
    const val DOWNLOAD_URL = "https://github.com/kcharignon/mobile-game/releases/download/android-latest/petitsjeux.apk"

    fun intent(): Intent = Intent(Intent.ACTION_VIEW, Uri.parse(DOWNLOAD_URL)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    /** Ouvre le téléchargement dans le navigateur ; `false` s'il n'y a aucun navigateur sur le téléphone. */
    fun open(context: Context): Boolean = try {
        context.startActivity(intent())
        true
    } catch (e: ActivityNotFoundException) {
        false
    }

    /** Numéro de la version installée (« 0.2.0 »), ou « ? » s'il est introuvable. */
    fun installedVersion(context: Context): String =
        runCatching { context.packageManager.getPackageInfo(context.packageName, 0).versionName }.getOrNull() ?: "?"
}
