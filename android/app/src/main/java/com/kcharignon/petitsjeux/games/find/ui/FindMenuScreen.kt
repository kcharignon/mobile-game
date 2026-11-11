package com.kcharignon.petitsjeux.games.find.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.ui.Backdrop
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjIcon
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.StarPill
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.withTapSound
import com.kcharignon.petitsjeux.games.find.FindProgress
import com.kcharignon.petitsjeux.games.find.engine.FindDifficulty

/** Le choix de la difficulté : cinq cartes, la suivante ne s'ouvre qu'avec une manche parfaite de la précédente (sans erreur, finie avant l'aide). */
@Composable
fun FindMenuScreen(progress: FindProgress, onPlay: (FindDifficulty) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    BackHandler(onBack = onBack)
    Backdrop(modifier) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 6.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RoundIconButton(IconKind.BACK, onClick = onBack, diameter = 48.dp)
                PjText(
                    "Cherche et Trouve", Modifier.weight(1f).padding(horizontal = 8.dp), size = 24.sp, weight = FontWeight.Bold, color = Color.White,
                    align = TextAlign.Center, outline = Pj.Ink, outlineWidth = 6.dp, maxLines = 2,
                )
                StarPill("${progress.totalStars}")
            }
            PjText(
                "Choisis ta difficulté", Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp), size = 19.sp, weight = FontWeight.Medium,
                color = Color.White, outline = Pj.Ink, outlineWidth = 4.dp, align = TextAlign.Center,
            )
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).testTag("difficulties"), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (d in FindDifficulty.entries) DifficultyCard(d, progress, onPlay = { onPlay(d) })
                Box(Modifier.size(2.dp))
            }
        }
    }
}

@Composable
private fun DifficultyCard(d: FindDifficulty, progress: FindProgress, onPlay: () -> Unit) {
    val unlocked = progress.isUnlocked(d)
    val stars = progress.stars(d)
    val theme = FindTheme.of(d.index * 2)
    val click = withTapSound(onPlay)
    Box(
        Modifier
            .fillMaxWidth()
            .testTag("difficulty-${d.index}")
            .clickable(enabled = unlocked, interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = click),
    ) {
        Panel(
            Modifier.fillMaxWidth(), corner = 30.dp, ledge = 8.dp,
            fill = if (unlocked) Pj.Panel else Color(0xFFE6E8F0), edge = if (unlocked) Pj.PanelEdge else Pj.GrayDark,
        ) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                // le nombre d'objets de la difficulté, sur les couleurs d'un des décors du jeu
                Box(
                    Modifier.size(72.dp).clip(CircleShape).background(if (unlocked) Brush.verticalGradient(listOf(theme.top, theme.bottom)) else Brush.verticalGradient(listOf(Color(0xFFCDD1DE), Color(0xFFB5BAC9)))),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        PjText("${d.objects}", size = 30.sp, weight = FontWeight.Bold, color = Color.White, outline = if (unlocked) theme.accent else Pj.GrayText, outlineWidth = 5.dp, maxLines = 1)
                        PjText("objets", size = 12.sp, weight = FontWeight.SemiBold, color = Color.White, outline = if (unlocked) theme.accent else Pj.GrayText, outlineWidth = 3.dp, maxLines = 1)
                    }
                }
                Column(Modifier.weight(1f)) {
                    PjText(d.title, size = 23.sp, weight = FontWeight.Bold, color = if (unlocked) Pj.Ink else Pj.GrayText, maxLines = 1)
                    if (unlocked) {
                        PjText(d.blurb, Modifier.padding(top = 1.dp), size = 13.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
                        Row(Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Stars(filled = stars, star = 20.dp)
                            if (!progress.hasPlayed(d)) PjText("Nouveau !", size = 14.sp, weight = FontWeight.Bold, color = Pj.GreenDark)
                        }
                    } else {
                        PjText(
                            "Réussis «\u00A0${d.previous!!.title}\u00A0» sans erreur ni aide pour débloquer.",
                            Modifier.padding(top = 2.dp), size = 13.sp, weight = FontWeight.Medium, color = Pj.GrayText,
                        )
                    }
                }
                if (unlocked) PlayBadge() else PjIcon(IconKind.LOCK, size = 28.dp, tint = Pj.GrayText)
            }
        }
    }
}

/** Un rond vert avec un triangle blanc : « jouer ». */
@Composable
private fun PlayBadge() {
    Canvas(Modifier.size(44.dp)) {
        drawCircle(Pj.GreenDark, size.minDimension / 2, Offset(size.width / 2, size.height / 2 + 3.dp.toPx()))
        drawCircle(Pj.Green, size.minDimension / 2, Offset(size.width / 2, size.height / 2))
        val t = Path().apply {
            moveTo(size.width * 0.40f, size.height * 0.28f)
            lineTo(size.width * 0.74f, size.height * 0.5f)
            lineTo(size.width * 0.40f, size.height * 0.72f)
            close()
        }
        drawPath(t, Color.White)
    }
}
