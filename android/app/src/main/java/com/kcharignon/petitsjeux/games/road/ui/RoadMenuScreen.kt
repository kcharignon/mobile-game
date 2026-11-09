package com.kcharignon.petitsjeux.games.road.ui

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
import com.kcharignon.petitsjeux.core.ui.PjIcon
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.StarPill
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.withTapSound
import com.kcharignon.petitsjeux.games.road.RoadProgress
import com.kcharignon.petitsjeux.games.road.engine.RoadWorlds

/** Le choix du niveau (en paysage) : un monde par ligne, ses 6 niveaux côte à côte avec leurs étoiles ; les suivants sont verrouillés. */
@Composable
fun RoadMenuScreen(progress: RoadProgress, onPlay: (Int) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val current = progress.currentLevel
    BackHandler(onBack = onBack)
    val state = rememberLazyListState()
    LaunchedEffect(Unit) {
        // ouvre la liste sur le monde où le joueur en est
        val world = RoadWorlds.of(current)
        if (world > 0) state.scrollToItem(world)
    }
    Backdrop(modifier, top = Color(0xFF9ADBFF), mid = Color(0xFF7ED06A), bottom = Color(0xFF4FA14A)) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 6.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RoundIconButton(IconKind.BACK, onClick = onBack, diameter = 48.dp)
                PjText(
                    "Poussin Traverse", Modifier.weight(1f).padding(horizontal = 8.dp), size = 28.sp, weight = FontWeight.Bold, color = Color.White,
                    align = TextAlign.Center, outline = Pj.Ink, outlineWidth = 7.dp, maxLines = 1,
                )
                StarPill("${progress.totalStars}")
            }
            Panel(Modifier.padding(top = 8.dp).fillMaxWidth().weight(1f), corner = 30.dp, ledge = 8.dp) {
                LazyColumn(
                    state = state,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(30.dp)).testTag("worlds"),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    items((0 until RoadWorlds.COUNT).toList()) { world -> WorldRow(world, progress, current, onPlay) }
                }
            }
        }
    }
}

@Composable
private fun WorldRow(world: Int, progress: RoadProgress, current: Int, onPlay: (Int) -> Unit) {
    val first = RoadWorlds.firstLevel(world)
    val got = (first until first + RoadWorlds.LEVELS_PER_WORLD).sumOf { progress.starsOf(it) }
    Column(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            PjText("Monde ${world + 1} · ${RoadWorlds.names[world]}", Modifier.weight(1f), size = 17.sp, color = Pj.InkSoft, maxLines = 1)
            PjText("${RoadWorlds.lanes(first)} à ${RoadWorlds.lanes(first + RoadWorlds.LEVELS_PER_WORLD - 1)} voies", Modifier.padding(end = 14.dp), size = 14.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
            PjText("$got / ${RoadWorlds.LEVELS_PER_WORLD * 3}", size = 14.sp, weight = FontWeight.Medium, color = Pj.StarDark)
        }
        Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            for (n in first until first + RoadWorlds.LEVELS_PER_WORLD) {
                Box(Modifier.weight(1f)) { LevelCell(n, progress, current, onPlay) }
            }
        }
    }
}

@Composable
private fun LevelCell(level: Int, progress: RoadProgress, current: Int, onPlay: (Int) -> Unit) {
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
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, enabled = unlocked, onClick = withTapSound { onPlay(level) }),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth().height(54.dp)) {
            Box(Modifier.fillMaxWidth().height(48.dp).padding(top = 5.dp).background(edge, RoundedCornerShape(17.dp)))
            Box(Modifier.fillMaxWidth().height(48.dp).background(face, RoundedCornerShape(17.dp)), contentAlignment = Alignment.Center) {
                if (unlocked) PjText("$level", size = 26.sp, weight = FontWeight.Bold, color = Color.White)
                else PjIcon(IconKind.LOCK, size = 22.dp, tint = Pj.GrayText)
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
        if (unlocked) Stars(filled = progress.starsOf(level), star = 13.dp, gap = 1.dp, modifier = Modifier.padding(top = 2.dp))
        else Box(Modifier.size(1.dp, 16.dp))
    }
}
