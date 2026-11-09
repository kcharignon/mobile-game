package com.kcharignon.petitsjeux.games.parking.ui

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.kcharignon.petitsjeux.games.parking.ParkingProgress
import com.kcharignon.petitsjeux.games.parking.engine.Worlds

private sealed interface GridEntry {
    data class Header(val world: Int) : GridEntry
    data class Item(val level: Int) : GridEntry
}

private val entries: List<GridEntry> = buildList {
    for (w in 0 until Worlds.COUNT) {
        add(GridEntry.Header(w))
        for (n in Worlds.firstLevel(w) until Worlds.firstLevel(w) + Worlds.LEVELS_PER_WORLD) add(GridEntry.Item(n))
    }
}

/** Le choix du niveau : les niveaux par mondes de 20, avec leurs étoiles ; les suivants sont verrouillés. */
@Composable
fun LevelSelectScreen(progress: ParkingProgress, onPlay: (Int) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val current = progress.currentLevel
    androidx.activity.compose.BackHandler(onBack = onBack)
    val state = rememberLazyGridState()
    LaunchedEffect(Unit) {
        // ouvre la liste sur le monde où le joueur en est (en-tête visible), ou un peu au-dessus de son niveau s'il est tout en bas du monde
        val index = entries.indexOfFirst { it is GridEntry.Item && it.level == current }
        val header = entries.indexOfFirst { it is GridEntry.Header && it.world == Worlds.of(current) }
        val target = maxOf(header, index - 8)
        if (target > 0) state.scrollToItem(target)
    }
    Backdrop(modifier) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RoundIconButton(IconKind.BACK, onClick = onBack, diameter = 52.dp)
                PjText(
                    "Parking Malin", Modifier.weight(1f).padding(horizontal = 8.dp), size = 30.sp, weight = FontWeight.Bold, color = Color.White,
                    align = androidx.compose.ui.text.style.TextAlign.Center, outline = Pj.Ink, outlineWidth = 7.dp,
                )
                StarPill("${progress.totalStars}")
            }
            Panel(Modifier.padding(top = 14.dp).fillMaxWidth().weight(1f), corner = 34.dp, ledge = 8.dp) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    state = state,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(34.dp)),
                    // pas de marge en haut ni en bas : les lignes qui défilent sont coupées net au bord du panneau (la marge est dans les en-têtes)
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(entries, span = { if (it is GridEntry.Header) GridItemSpan(maxLineSpan) else GridItemSpan(1) }) { entry ->
                        when (entry) {
                            is GridEntry.Header -> WorldHeader(entry.world, progress)
                            is GridEntry.Item -> LevelCell(entry.level, progress, current, onPlay)
                        }
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) { Box(Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun WorldHeader(world: Int, progress: ParkingProgress) {
    val first = Worlds.firstLevel(world)
    val got = (first until first + Worlds.LEVELS_PER_WORLD).sumOf { progress.starsOf(it) }
    Row(Modifier.fillMaxWidth().padding(top = if (world == 0) 16.dp else 26.dp, bottom = 2.dp), verticalAlignment = Alignment.CenterVertically) {
        PjText("Monde ${world + 1} · ${Worlds.names[world]}", Modifier.weight(1f), size = 17.sp, color = Pj.InkSoft, maxLines = 1)
        PjText("$got / ${Worlds.LEVELS_PER_WORLD * 3}", size = 14.sp, weight = FontWeight.Medium, color = Pj.StarDark)
    }
}

@Composable
private fun LevelCell(level: Int, progress: ParkingProgress, current: Int, onPlay: (Int) -> Unit) {
    val unlocked = progress.isUnlocked(level)
    val isCurrent = level == current && !progress.allDone
    val face = when {
        !unlocked -> Pj.Gray
        isCurrent -> Pj.Orange
        else -> Pj.Green
    }
    val edge = when {
        !unlocked -> Pj.GrayDark
        isCurrent -> Pj.OrangeDark
        else -> Pj.GreenDark
    }
    Column(
        Modifier
            .fillMaxWidth()
            .testTag("level-$level")
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, enabled = unlocked) { onPlay(level) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth().height(60.dp)) {
            Box(Modifier.fillMaxWidth().height(54.dp).padding(top = 5.dp).background(edge, RoundedCornerShape(19.dp)))
            Box(Modifier.fillMaxWidth().height(54.dp).background(face, RoundedCornerShape(19.dp)), contentAlignment = Alignment.Center) {
                if (unlocked) PjText("$level", size = 28.sp, weight = FontWeight.Bold, color = Color.White)
                else PjIcon(IconKind.LOCK, size = 24.dp, tint = Pj.GrayText)
            }
            if (isCurrent) {
                Canvas(Modifier.fillMaxSize()) {
                    drawRoundRect(
                        Color.White, Offset(-4.dp.toPx(), -4.dp.toPx()), Size(size.width + 8.dp.toPx(), 62.dp.toPx()), CornerRadius(22.dp.toPx()),
                        style = Stroke(3.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 6.dp.toPx()))),
                    )
                }
            }
        }
        if (unlocked) Stars(filled = progress.starsOf(level), star = 13.dp, gap = 1.dp, modifier = Modifier.padding(top = 2.dp))
        else Box(Modifier.size(1.dp, 16.dp))
    }
}
