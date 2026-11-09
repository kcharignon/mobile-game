package com.kcharignon.petitsjeux.core.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.ui.ChunkyButton
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.update.UpdateController
import com.kcharignon.petitsjeux.core.update.UpdateState
import kotlin.math.roundToInt

/**
 * Sur l'accueil : n'apparaît que lorsqu'il y a quelque chose à faire (une nouvelle version, un téléchargement, une confirmation à donner).
 * Un seul geste pour la personne : toucher le gros bouton vert.
 */
@Composable
fun UpdateBanner(controller: UpdateController?, modifier: Modifier = Modifier) {
    if (controller == null) return
    val state = controller.state
    if (state is UpdateState.Idle || state is UpdateState.Checking || state is UpdateState.UpToDate) return
    // un échec de recherche (sans réseau) ne se dit que dans les réglages ; la bannière ne parle d'un échec que pendant une mise à jour
    if (state is UpdateState.Failed && state.info == null) return
    Panel(
        modifier.fillMaxWidth(),
        fill = Color(0xFFEAF9EC), edge = Color(0xFFB9E4C0),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
    ) {
        UpdateContent(controller, version = null)
    }
}

/** Dans les réglages : la même chose, plus « Chercher une mise à jour » et le numéro de la version installée. */
@Composable
fun UpdateSection(controller: UpdateController?, version: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (controller != null) UpdateContent(controller, version)
        PjText("Version $version", Modifier.padding(top = 6.dp), size = 13.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
    }
}

@Composable
private fun UpdateContent(controller: UpdateController, version: String?) {
    val inSettings = version != null
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        when (val state = controller.state) {
            UpdateState.Idle, is UpdateState.Failed -> {
                if (state is UpdateState.Failed) {
                    Message(if (state.reason == UpdateState.Reason.NETWORK) "Pas de connexion. Réessaie plus tard." else "Le téléchargement a échoué.", error = true)
                }
                if (inSettings || state is UpdateState.Failed) {
                    ChunkyButton(
                        if (state is UpdateState.Failed) "Réessayer" else "Chercher une mise à jour", { if (state is UpdateState.Failed) controller.update() else controller.check(silent = false) },
                        Modifier.fillMaxWidth(), color = Pj.Blue, dark = Pj.BlueDark, height = 50.dp, fontSize = 19.sp,
                    )
                }
            }
            UpdateState.Checking -> if (inSettings) Message("Je cherche une nouvelle version…")
            is UpdateState.UpToDate -> if (inSettings) Message("C'est la dernière version, bravo !")
            is UpdateState.Available -> {
                ChunkyButton("Nouvelle version ! Mettre à jour", controller::update, Modifier.fillMaxWidth(), height = 56.dp, fontSize = 19.sp)
                Message("Version ${state.info.versionName} : gratuite et rapide. Tes joueurs sont gardés.")
            }
            is UpdateState.Downloading -> {
                Message("Je télécharge la nouvelle version… ${(state.progress * 100).roundToInt()} %")
                Box14(state.progress)
            }
            is UpdateState.ReadyToInstall -> {
                Message("Touche « Installer » dans la fenêtre d'Android.")
                ChunkyButton("Installer", controller::installAgain, Modifier.fillMaxWidth(), color = Pj.Blue, dark = Pj.BlueDark, height = 50.dp, fontSize = 20.sp)
            }
            is UpdateState.NeedsPermission -> {
                Message("Android demande ton accord, une seule fois. Active « Autoriser » puis reviens ici.")
                ChunkyButton("Autoriser", controller::askPermission, Modifier.fillMaxWidth(), color = Pj.Orange, dark = Pj.OrangeDark, height = 50.dp, fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun Message(text: String, error: Boolean = false) {
    PjText(text, Modifier.fillMaxWidth(), size = 15.sp, weight = FontWeight.Medium, color = if (error) Pj.RedDark else Pj.Ink, align = TextAlign.Center)
}

@Composable
private fun Box14(progress: Float) {
    androidx.compose.foundation.layout.Box(Modifier.fillMaxWidth().height(14.dp).background(Color(0xFFD7EFDB), CircleShape)) {
        androidx.compose.foundation.layout.Box(Modifier.fillMaxWidth(progress.coerceIn(0.03f, 1f)).height(14.dp).background(Pj.Green, CircleShape))
    }
}
