package com.kcharignon.petitsjeux.games.mushrooms.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjIcon
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.StarPill
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.withTapSound
import com.kcharignon.petitsjeux.games.mushrooms.MushroomProgress
import com.kcharignon.petitsjeux.games.mushrooms.engine.Difficulty

/** Le temps (en secondes) qui fait bouger les lucioles : 60 s en boucle. */
@Composable
fun rememberForestTime(): State<Float> =
    rememberInfiniteTransition(label = "forest").animateFloat(0f, 60f, infiniteRepeatable(tween(60_000, easing = LinearEasing), RepeatMode.Restart), label = "time")

/** Le fond de la forêt, plein écran, avec le contenu dans les zones sûres. [overlay] : les fenêtres (voile sombre) qui couvrent tout. */
@Composable
fun ForestBackdrop(
    modifier: Modifier = Modifier,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    val time = rememberForestTime()
    Box(modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) { drawForest(time.value) }
        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), content = content)
        overlay()
    }
}

/** Un petit champignon dessiné dans un carré de [size] (pour les cartes et les pastilles). */
@Composable
fun ShroomIcon(size: androidx.compose.ui.unit.Dp, color: Color?, modifier: Modifier = Modifier, forbidden: Boolean = false) {
    Canvas(modifier.size(size)) {
        val r = this.size.minDimension * 0.4f
        drawMushroom(this.size.width / 2, this.size.height * 0.48f, r, color, glow = 0.6f, forbidden = forbidden)
    }
}

/** Le choix de la difficulté : quatre cartes, la suivante ne s'ouvre qu'avec 2 étoiles sur la précédente. */
@Composable
fun ShroomMenuScreen(progress: MushroomProgress, onPlay: (Difficulty) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    BackHandler(onBack = onBack)
    ForestBackdrop(modifier) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RoundIconButton(IconKind.BACK, onClick = onBack, diameter = 52.dp)
                PjText(
                    "Champis Magiques", Modifier.weight(1f).padding(horizontal = 6.dp), size = 22.sp, weight = FontWeight.Bold, color = Color.White,
                    outline = Color(0xFF0E2A33), outlineWidth = 7.dp, align = TextAlign.Center, maxLines = 1,
                )
                StarPill("${progress.totalStars}")
            }
            PjText(
                "Choisis ta difficulté", Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 10.dp), size = 19.sp, weight = FontWeight.Medium,
                color = Color(0xFFE5F7E8), align = TextAlign.Center,
            )
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                for (d in Difficulty.entries) DifficultyCard(d, progress, onPlay = { onPlay(d) })
                Box(Modifier.size(2.dp))
            }
        }
    }
}

@Composable
private fun DifficultyCard(d: Difficulty, progress: MushroomProgress, onPlay: () -> Unit) {
    val unlocked = progress.isUnlocked(d)
    val stars = progress.stars(d)
    val accent = if (unlocked) ShroomColors[d.index % 5] else Color(0xFFB5BAC9)
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
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(Modifier.size(74.dp).background(Color(0xFF1F4F4B).copy(alpha = if (unlocked) 1f else 0.45f), androidx.compose.foundation.shape.CircleShape), contentAlignment = Alignment.Center) {
                    ShroomIcon(70.dp, if (unlocked) accent else null)
                }
                Column(Modifier.weight(1f)) {
                    PjText(d.title, size = 23.sp, weight = FontWeight.Bold, color = if (unlocked) Pj.Ink else Pj.GrayText, maxLines = 1)
                    if (unlocked) {
                        PjText(d.blurb, Modifier.padding(top = 1.dp), size = 13.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
                        Row(Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Stars(filled = stars, star = 20.dp)
                            if (progress.hasPlayed(d)) PjText("Record ${progress.bestScore(d)}", size = 14.sp, weight = FontWeight.Bold, color = Pj.StarDark)
                            else PjText("Nouveau !", size = 14.sp, weight = FontWeight.Bold, color = Pj.GreenDark)
                        }
                    } else {
                        val before = d.previous!!
                        PjText(
                            "Fais ${before.twoStars} points en « ${before.title} » (2 étoiles) pour débloquer.",
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
        val t = androidx.compose.ui.graphics.Path().apply {
            moveTo(size.width * 0.40f, size.height * 0.28f)
            lineTo(size.width * 0.74f, size.height * 0.5f)
            lineTo(size.width * 0.40f, size.height * 0.72f)
            close()
        }
        drawPath(t, Color.White)
    }
}
