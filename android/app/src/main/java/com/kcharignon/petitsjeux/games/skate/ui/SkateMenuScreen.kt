package com.kcharignon.petitsjeux.games.skate.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
import com.kcharignon.petitsjeux.games.skate.SkateProgress
import com.kcharignon.petitsjeux.games.skate.engine.PieceKind
import com.kcharignon.petitsjeux.games.skate.engine.SkateDifficulty

/** La couleur du casque de chaque difficulté sur les cartes du menu (le skateur change de casque en montant). */
private val DifficultyHelmets = listOf(Color(0xFF3FBF4A), Color(0xFF3E8BFF), Color(0xFFFF8A1F), Color(0xFFE8362F))

/** Un petit dessin posé dans un carré de [size] : le skateur, un piéton, un lampadaire… dessinés comme dans le jeu. */
@Composable
fun SkateIcon(kind: PieceKind?, size: Dp, helmet: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.size(size)) {
        val bx = this.size.width / 2f
        val by = this.size.height * 0.94f
        when (kind) {
            null -> drawSkater(bx, by, this.size.height * 1.3f, helmet, 0f, 0f, 0f, 0f)
            PieceKind.PEDESTRIAN -> drawPedestrian(bx, by, this.size.height * 1.25f, helmet.hashCode().mod(5), 0f, 0f)
            PieceKind.LAMP -> drawLamp(bx - this.size.width * 0.12f, by, this.size.height * 0.62f, false)
            PieceKind.CONE -> drawCone(bx, by, this.size.height * 2.6f, 0)
            PieceKind.BIN -> drawBin(bx, by, this.size.height * 1.9f, 0)
            PieceKind.BENCH -> drawBench(bx, by, this.size.height * 1.6f, 0)
            PieceKind.BARRIER -> drawBarrier(bx, by, this.size.height * 1.5f, 0f)
            PieceKind.RAMP -> {
                val w = this.size.width
                val p = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.1f, by); lineTo(w * 0.9f, by); lineTo(w * 0.9f, by - w * 0.42f); close()
                }
                drawPath(p, Color(0xFFFF8A1F))
                drawLine(Color(0xFFFFE066), Offset(w * 0.3f, by - w * 0.04f), Offset(w * 0.8f, by - w * 0.28f), strokeWidth = w * 0.05f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            }
        }
    }
}

/** Le fond des écrans de choix : la rue de Super Skate, immobile, de jour. */
@Composable
fun SkateBackdrop(modifier: Modifier = Modifier, overlay: @Composable BoxScope.() -> Unit = {}, content: @Composable BoxScope.() -> Unit) {
    Box(modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) { drawStillStreet(SkateThemes.City) }
        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), content = content)
        overlay()
    }
}

/** Le choix de la difficulté : quatre cartes, la suivante ne s'ouvre qu'avec 2 étoiles sur la précédente. */
@Composable
fun SkateMenuScreen(progress: SkateProgress, helmet: Color, onPlay: (SkateDifficulty) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    BackHandler(onBack = onBack)
    SkateBackdrop(modifier) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RoundIconButton(IconKind.BACK, onClick = onBack, diameter = 52.dp)
                PjText(
                    "Super Skate", Modifier.weight(1f).padding(horizontal = 6.dp), size = 24.sp, weight = FontWeight.Bold, color = Color.White,
                    outline = Color(0xFF2B4A6B), outlineWidth = 7.dp, align = TextAlign.Center, maxLines = 1,
                )
                StarPill("${progress.totalStars}")
            }
            PjText(
                "Choisis ta difficulté", Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 10.dp), size = 19.sp, weight = FontWeight.Bold,
                color = Color.White, outline = Color(0xFF2B4A6B), outlineWidth = 5.dp, align = TextAlign.Center,
            )
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                for (d in SkateDifficulty.entries) DifficultyCard(d, progress, helmet, onPlay = { onPlay(d) })
                Box(Modifier.size(2.dp))
            }
        }
    }
}

@Composable
private fun DifficultyCard(d: SkateDifficulty, progress: SkateProgress, playerHelmet: Color, onPlay: () -> Unit) {
    val unlocked = progress.isUnlocked(d)
    val stars = progress.stars(d)
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
                Box(
                    Modifier.size(74.dp).background(if (unlocked) Color(0xFF8FD3FF) else Color(0xFFC9CEDB), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    SkateIcon(null, 66.dp, if (unlocked) DifficultyHelmets[d.index] else Color(0xFF9AA0B5))
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
                            "Finis « ${before.title} » avec 2 étoiles pour débloquer.",
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
