package com.kcharignon.petitsjeux.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Tant que ce composable est à l'écran, l'appli reste **en paysage** (dans les deux sens, pour les gauchers, même si la rotation automatique est coupée) ;
 * quand il disparaît, l'orientation d'avant est remise (le portrait du reste de l'appli).
 *
 * Le manifeste déclare `configChanges="orientation|screenSize|…"` : tourner l'écran ne recrée pas l'activité, donc une partie en cours n'est pas perdue.
 */
@Composable
fun LockLandscape() {
    val context = LocalContext.current
    DisposableEffect(context) {
        val activity = context.findActivity()
        val before = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        onDispose {
            if (activity != null && before != null) activity.requestedOrientation = before
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
