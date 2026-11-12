package com.kcharignon.petitsjeux.games.plane.ui

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
import com.kcharignon.petitsjeux.games.plane.PlaneProgress
import com.kcharignon.petitsjeux.games.plane.engine.PieceKind
import com.kcharignon.petitsjeux.games.plane.engine.PlaneDifficulty

/** La couleur de l'avion de chaque difficulté sur les cartes du menu (l'avion change de peinture en montant). */
private val DifficultyColors = listOf(Color(0xFF3FBF4A), Color(0xFF2D6FEF), Color(0xFFFF7A1F), Color(0xFFE8362F))

/** Un petit dessin posé dans un carré de [size] : l'avion (`null`) ou un obstacle, dessinés comme dans le jeu. */
@Composable
fun PlaneIcon(kind: PieceKind?, size: Dp, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.size(size)) {
        val c = Offset(this.size.width / 2f, this.size.height / 2f)
        val h = this.size.height
        when (kind) {
            null -> drawPlayerPlane(c.x, c.y + h * 0.02f, h * 2.3f, color, 0f, 0f, 0f, 0f)
            PieceKind.BIRDS -> drawBirds(c.x, c.y, h * 1.6f, 0, 0.3f, 1)
            PieceKind.PLANE -> drawOncomingPlane(c.x, c.y, h * 2.2f, 0, 0f)
            PieceKind.BALLOON -> drawBalloon(c.x, c.y, h * 2.3f, 0, 0f)
            PieceKind.PARAGLIDER -> drawParaglider(c.x, c.y, h * 2.3f, 0, 0f)
            PieceKind.RING -> drawRing(c.x, c.y, h * 2.2f, 0.2f)
        }
    }
}

/** Le fond des écrans de choix : le ciel de Super Avion, immobile, de jour. */
@Composable
fun PlaneBackdrop(modifier: Modifier = Modifier, overlay: @Composable BoxScope.() -> Unit = {}, content: @Composable BoxScope.() -> Unit) {
    Box(modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) { drawStillSky(PlaneThemes.Country) }
        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), content = content)
        overlay()
    }
}

/** Le choix de la difficulté : quatre cartes, la suivante ne s'ouvre qu'avec 2 étoiles sur la précédente. */
@Composable
fun PlaneMenuScreen(progress: PlaneProgress, color: Color, onPlay: (PlaneDifficulty) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    BackHandler(onBack = onBack)
    PlaneBackdrop(modifier) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RoundIconButton(IconKind.BACK, onClick = onBack, diameter = 52.dp)
                PjText(
                    "Super Avion", Modifier.weight(1f).padding(horizontal = 6.dp), size = 24.sp, weight = FontWeight.Bold, color = Color.White,
                    outline = Color(0xFF2B4A6B), outlineWidth = 7.dp, align = TextAlign.Center, maxLines = 1,
                )
                StarPill("${progress.totalStars}")
            }
            PjText(
                "Choisis ta difficulté", Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 10.dp), size = 19.sp, weight = FontWeight.Bold,
                color = Color.White, outline = Color(0xFF2B4A6B), outlineWidth = 5.dp, align = TextAlign.Center,
            )
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                for (d in PlaneDifficulty.entries) DifficultyCard(d, progress, color, onPlay = { onPlay(d) })
                Box(Modifier.size(2.dp))
            }
        }
    }
}

@Composable
private fun DifficultyCard(d: PlaneDifficulty, progress: PlaneProgress, playerColor: Color, onPlay: () -> Unit) {
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
                    PlaneIcon(null, 66.dp, if (unlocked) DifficultyColors[d.index] else Color(0xFF9AA0B5))
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
                            "Atterris avec 2 étoiles en « ${before.title} » pour débloquer.",
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
