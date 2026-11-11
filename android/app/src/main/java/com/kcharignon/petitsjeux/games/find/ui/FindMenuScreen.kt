package com.kcharignon.petitsjeux.games.find.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.ui.Backdrop
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.StarPill
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.withTapSound
import com.kcharignon.petitsjeux.games.find.FindProgress
import com.kcharignon.petitsjeux.games.find.engine.FindWorlds

/** Le choix du niveau : un monde par bloc (ses 10 niveaux sur deux rangées de 5, avec leurs étoiles). Tous sont jouables ; ceux qu'on n'a pas encore réussis sont gris. */
@Composable
fun FindMenuScreen(progress: FindProgress, onPlay: (Int) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val current = progress.currentLevel
    BackHandler(onBack = onBack)
    val state = rememberLazyListState()
    LaunchedEffect(Unit) {
        // ouvre la liste sur le monde où le joueur en est
        val world = FindWorlds.of(current)
        if (world > 0) state.scrollToItem(world)
    }
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
            Panel(Modifier.padding(top = 10.dp).fillMaxWidth().weight(1f), corner = 30.dp, ledge = 8.dp) {
                LazyColumn(
                    state = state,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(30.dp)).testTag("worlds"),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    items((0 until FindWorlds.COUNT).toList()) { world -> WorldBlock(world, progress, current, onPlay) }
                }
            }
        }
    }
}

@Composable
private fun WorldBlock(world: Int, progress: FindProgress, current: Int, onPlay: (Int) -> Unit) {
    val first = FindWorlds.firstLevel(world)
    val theme = FindTheme.of(world)
    // les mondes se montrent toujours en couleurs (on peut y sauter) ; leurs étoiles apparaissent dès qu'on y a joué ou qu'on y est arrivé
    val reached = first <= current
    val got = (first until first + FindWorlds.LEVELS_PER_WORLD).sumOf { progress.starsOf(it) }
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            PjText("Monde ${world + 1} · ${FindWorlds.names[world]}", Modifier.weight(1f), size = 16.sp, color = Pj.InkSoft, maxLines = 1)
            // les trois couleurs du décor du monde
            Row(Modifier.padding(end = 10.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (c in listOf(theme.top, theme.bottom, theme.accent)) Box(Modifier.size(11.dp).background(c, CircleShape))
            }
            if (reached || got > 0) PjText("$got / ${FindWorlds.LEVELS_PER_WORLD * 3}", size = 14.sp, weight = FontWeight.Medium, color = Pj.StarDark)
        }
        for (row in 0 until 2) {
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                for (col in 0 until 5) {
                    val n = first + row * 5 + col
                    Box(Modifier.weight(1f)) { LevelCell(n, progress, current, onPlay) }
                }
            }
        }
    }
}

@Composable
private fun LevelCell(level: Int, progress: FindProgress, current: Int, onPlay: (Int) -> Unit) {
    val done = progress.isDone(level)
    val isCurrent = level == current && !done
    val face = when {
        isCurrent -> Pj.Orange
        done -> Pj.Green
        else -> Pj.Gray
    }
    val edge = when {
        isCurrent -> Pj.OrangeDark
        done -> Pj.GreenDark
        else -> Pj.GrayDark
    }
    Column(
        Modifier
            .fillMaxWidth()
            .testTag("level-$level")
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = withTapSound { onPlay(level) }),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth().height(54.dp)) {
            Box(Modifier.fillMaxWidth().height(48.dp).padding(top = 5.dp).background(edge, RoundedCornerShape(17.dp)))
            Box(Modifier.fillMaxWidth().height(48.dp).background(face, RoundedCornerShape(17.dp)), contentAlignment = Alignment.Center) {
                PjText("$level", size = 24.sp, weight = FontWeight.Bold, color = if (done || isCurrent) Color.White else Pj.GrayText)
            }
            if (isCurrent) {
                Canvas(Modifier.fillMaxSize()) {
                    drawRoundRect(
                        Color.White, Offset(-4.dp.toPx(), -4.dp.toPx()), Size(size.width + 8.dp.toPx(), 56.dp.toPx()), CornerRadius(20.dp.toPx()),
                        style = Stroke(3.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 6.dp.toPx()))),
                    )
                }
            }
        }
        if (done || isCurrent) Stars(filled = progress.starsOf(level), star = 12.dp, gap = 1.dp, modifier = Modifier.padding(top = 2.dp))
        else Box(Modifier.size(1.dp, 16.dp))
    }
}
