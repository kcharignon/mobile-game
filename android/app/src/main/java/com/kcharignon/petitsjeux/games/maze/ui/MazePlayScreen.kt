package com.kcharignon.petitsjeux.games.maze.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.ui.ChunkyButton
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Modal
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjIcon
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.ExitArrow
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.consumeTaps
import com.kcharignon.petitsjeux.games.maze.MazeProgress
import com.kcharignon.petitsjeux.games.maze.engine.Cell
import com.kcharignon.petitsjeux.games.maze.engine.MazeEvent
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevel
import com.kcharignon.petitsjeux.games.maze.engine.MazePlay
import com.kcharignon.petitsjeux.games.maze.engine.MazeScoring
import com.kcharignon.petitsjeux.games.maze.engine.MazeWorlds
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

/** Où en est le niveau : l'introduction (premier niveau du jeu ou d'un monde), le tracé, le poisson qui suit le chemin, puis le bilan. */
private enum class Phase { Intro, Playing, Swimming, Done }

private const val BURST_MS = 380L
private const val HINT_MS = 4200L

/** Un niveau a une introduction au tout premier niveau et au premier niveau de chaque monde. */
internal fun hasIntro(level: MazeLevel): Boolean = level.number == 1 || MazeWorlds.indexIn(level.number) == 0

/** La hauteur (0, grave, à 7, aigu) du « bloup » quand la trace gagne la case [c] : plus on est près de la sortie, plus c'est aigu. */
internal fun pitchOf(level: MazeLevel, c: Cell): Int {
    val total = level.distanceToExit(level.start).coerceAtLeast(1)
    return ((1f - level.distanceToExit(c).toFloat() / total).coerceIn(0f, 1f) * 7).roundToInt()
}

/**
 * Un niveau : le labyrinthe vu du dessus, entièrement visible. **Poser le doigt sur le poisson et tracer le chemin** jusqu'à l'anémone (voir [MazePlay]).
 * Pas de chronomètre, on ne perd jamais : les impasses ne font perdre que des étoiles. [onFinished] est appelé UNE fois, à l'arrivée (le joueur est sauvegardé tout de suite).
 */
@Composable
fun MazePlayScreen(
    level: MazeLevel,
    progress: MazeProgress,
    onFinished: (stars: Int, detours: Int) -> Unit,
    onNext: (Int) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var attempt by remember(level.number) { mutableIntStateOf(0) }
    BackHandler(onBack = onExit)
    key(level.number, attempt) {
        Run(level, progress, skipIntro = attempt > 0, onFinished, onNext, onMenu, onExit, onReplay = { attempt++ }, modifier)
    }
}

@Composable
private fun Run(
    level: MazeLevel,
    progress: MazeProgress,
    skipIntro: Boolean,
    onFinished: (Int, Int) -> Unit,
    onNext: (Int) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
    onReplay: () -> Unit,
    modifier: Modifier,
) {
    val feedback = LocalFeedback.current
    val finished by rememberUpdatedState(onFinished)
    val scene = remember(level.number) { MazeScene(level) }
    val play = remember(level.number) { MazePlay(level) }
    val density = LocalDensity.current
    var clock by remember { mutableLongStateOf(0L) }
    var phase by remember { mutableStateOf(if (skipIntro || !hasIntro(level)) Phase.Playing else Phase.Intro) }
    var trail by remember { mutableStateOf(listOf(level.start)) }
    var detours by remember { mutableIntStateOf(0) }
    var holding by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf<List<Cell>>(emptyList()) }
    var hintAt by remember { mutableLongStateOf(-100_000L) }
    var swimAt by remember { mutableLongStateOf(0L) }
    var swimMs by remember { mutableLongStateOf(1500L) }
    var layout by remember { mutableStateOf<MazeLayout?>(null) }
    val bursts = remember { ArrayList<Pair<Cell, Long>>() }

    fun sync() {
        trail = play.trail
        detours = play.detours
        holding = play.holding
    }

    fun handle(events: List<MazeEvent>, before: List<Cell>) {
        // un doigt rapide gagne plusieurs cases d'un coup : on n'entend que les trois dernières, pour que ce reste un « bloup, bloup, bloup »
        for (e in events.filterIsInstance<MazeEvent.Stepped>().takeLast(3)) feedback.bloop(pitchOf(level, e.cell))
        for (e in events) when (e) {
            is MazeEvent.Backed -> {
                feedback.burst()
                for (c in before.drop(play.trail.size)) bursts += c to clock
            }
            is MazeEvent.DeadEnd -> feedback.bonk()
            is MazeEvent.Arrived -> {
                phase = Phase.Swimming
                swimAt = clock
                swimMs = (900L + play.trail.size * 90L).coerceIn(1200L, 3000L)
                hint = emptyList()
                finished(play.stars, play.detours)
            }
            else -> Unit
        }
    }

    // l'horloge : elle ne sert qu'aux animations (bulles, anémone, poisson qui nage) et au chrono des petits effets ; jamais plus de 100 ms par image
    LaunchedEffect(Unit) {
        var last = -1L
        while (true) {
            withFrameMillis { t ->
                val dt = if (last >= 0) minOf(t - last, 100L) else 0L
                last = t
                clock += dt
                bursts.removeAll { clock - it.second > BURST_MS }
                if (phase == Phase.Swimming && clock - swimAt >= swimMs) {
                    phase = Phase.Done
                    feedback.win()
                }
            }
        }
    }

    fun giveHint() {
        if (phase != Phase.Playing) return
        hint = play.hint().cells
        hintAt = clock
        feedback.hint()
    }

    fun restart() {
        if (phase != Phase.Playing) return
        play.reset()
        hint = emptyList()
        bursts.clear()
        sync()
    }

    val onDown by rememberUpdatedState<(Offset) -> Boolean> { pos ->
        val l = layout
        if (phase != Phase.Playing || l == null) false
        else l.toCells(pos).let { (x, y) -> play.down(x, y) }.also { sync() }
    }
    val onMove by rememberUpdatedState<(Offset, Offset) -> Unit> { pos, last ->
        val l = layout
        if (phase == Phase.Playing && l != null) {
            val (x, y) = l.toCells(pos)
            val moved = hypot(pos.x - last.x, pos.y - last.y) / l.cell
            val before = play.trail
            val events = play.move(x, y, MazePlay.reachFor(moved.toDouble()))
            if (events.isNotEmpty()) {
                handle(events, before)
                sync()
            }
        }
    }
    val onUp by rememberUpdatedState {
        play.up()
        sync()
    }

    val potentialStars = MazeScoring.stars(detours)
    Box(modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) { drawSea(scene.theme, clock / 1000f) }
        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 14.dp)) {
            // le haut : retour, niveau et monde, recommencer
            Row(Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // (pendant l'introduction, la flèche est celle posée au-dessus du voile)
                if (phase == Phase.Intro) Box(Modifier.size(48.dp, 52.dp)) else ExitArrow(onExit, Modifier, 48.dp)
                Box(
                    Modifier.weight(1f).height(54.dp).background(Color.White.copy(alpha = 0.94f), RoundedCornerShape(27.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        PjText("Niveau ${level.number}", size = 20.sp, weight = FontWeight.Bold, color = Pj.Ink, maxLines = 1)
                        PjText(MazeWorlds.names[level.world], size = 13.sp, weight = FontWeight.Medium, color = Pj.InkSoft, maxLines = 1)
                    }
                }
                RoundIconButton(IconKind.LEVELS, onClick = onMenu, modifier = Modifier.testTag("levels"), color = Pj.Slate, dark = Pj.SlateDark, diameter = 48.dp)
                RoundIconButton(IconKind.RESTART, onClick = { restart() }, modifier = Modifier.testTag("restart"), diameter = 48.dp)
            }
            Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.Center) {
                Stars(filled = potentialStars, star = 22.dp, gap = 5.dp, raiseMiddle = true, modifier = Modifier.testTag("potential-stars"))
            }
            // le labyrinthe : tout l'espace restant, entièrement visible
            Box(Modifier.weight(1f).fillMaxWidth()) {
                Canvas(
                    Modifier
                        .fillMaxSize()
                        .testTag("maze")
                        .systemGestureExclusion()
                        .onSizeChanged { layout = MazeLayout(level, it.width.toFloat(), it.height.toFloat(), margin = with(density) { 6.dp.toPx() }) }
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val down = awaitFirstDown(requireUnconsumed = false)
                                    val grabbed = onDown(down.position)
                                    var last = down.position
                                    while (true) {
                                        val change = awaitPointerEvent().changes.firstOrNull { it.id == down.id } ?: break
                                        if (grabbed && change.pressed) {
                                            onMove(change.position, last)
                                            last = change.position
                                            change.consume()
                                        }
                                        if (!change.pressed) break
                                    }
                                    onUp()
                                }
                            }
                        },
                ) {
                    val l = layout ?: return@Canvas
                    val time = clock / 1000f
                    val points = trail.map { l.center(it) }
                    var fishAt: Offset? = null
                    var angle = 0f
                    var scale = 0.82f
                    var wiggle = sin(time * 3f) * 0.3f
                    when (phase) {
                        Phase.Swimming -> {
                            val (at, a) = positionAlong(points, (clock - swimAt) / swimMs.toFloat())
                            fishAt = at
                            angle = a
                            wiggle = sin(clock / 60f)
                        }
                        Phase.Done -> {
                            fishAt = Offset(l.center(level.exit).x, l.center(level.exit).y + l.cell * 0.02f)
                            scale = 0.8f
                            wiggle = sin(time * 4f) * 0.4f
                        }
                        else -> Unit
                    }
                    val fade = if (clock - hintAt < HINT_MS) (1f - ((clock - hintAt - (HINT_MS - 1200L)) / 1200f).coerceIn(0f, 1f)) else 0f
                    drawMazeBoard(
                        scene, l,
                        BoardState(
                            trail = trail,
                            gold = (phase == Phase.Swimming || phase == Phase.Done) && detours == 0,
                            hint = hint, hintAlpha = fade,
                            fishAt = fishAt, fishAngle = angle, fishScale = scale, fishWiggle = wiggle,
                            holding = holding,
                            bursts = bursts.map { (c, at) -> c to ((clock - at) / BURST_MS.toFloat()).coerceIn(0f, 1f) },
                            time = time,
                            showHead = phase == Phase.Playing || phase == Phase.Intro,
                        ),
                    )
                }
                // un nœud d'accessibilité (et pour les tests) : où en est la trace
                Box(
                    Modifier
                        .size(1.dp)
                        .testTag("trail")
                        .semantics {
                            contentDescription = "Trace"
                            stateDescription = "${trail.size} cases, tête ${trail.last().x},${trail.last().y}, $detours impasses" +
                                if (phase == Phase.Swimming || phase == Phase.Done) ", arrivé" else ""
                        },
                )
            }
            // le bas : les impasses et l'indice
            Row(
                Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 10.dp).alpha(if (phase == Phase.Swimming || phase == Phase.Done) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    Modifier.weight(0.9f).height(58.dp).background(Color.White.copy(alpha = 0.94f), RoundedCornerShape(29.dp)).padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    PjText("Impasses", size = 16.sp, weight = FontWeight.SemiBold, color = Pj.InkSoft, maxLines = 1)
                    PjText("$detours", Modifier.testTag("detours"), size = 26.sp, weight = FontWeight.Bold, color = Pj.Ink)
                }
                ChunkyButton(
                    "Indice", { giveHint() }, Modifier.weight(1f).testTag("hint"),
                    color = Pj.Orange, dark = Pj.OrangeDark, height = 54.dp, fontSize = 24.sp,
                    leading = { PjIcon(IconKind.BULB, size = 24.dp, tint = Color.White) },
                )
            }
        }
        when (phase) {
            Phase.Intro -> IntroCard(level, onStart = { phase = Phase.Playing }, onMenu = onMenu, onExit = onExit)
            Phase.Done -> EndPanel(level, play.stars, play.detours, progress, onReplay, onMenu, onNext)
            else -> Unit
        }
    }
}

@Composable
private fun IntroCard(level: MazeLevel, onStart: () -> Unit, onMenu: () -> Unit, onExit: () -> Unit) {
    val theme = MazeTheme.of(level.world)
    Modal(onDismiss = null, topStart = { ExitArrow(onExit, Modifier.align(Alignment.TopStart).padding(start = 14.dp, top = 6.dp), 48.dp) }) {
        Panel(
            Modifier.padding(horizontal = 24.dp).widthIn(max = 420.dp).fillMaxWidth().consumeTaps(),
            corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.size(width = 150.dp, height = 96.dp).background(theme.frameTop, RoundedCornerShape(24.dp)), contentAlignment = Alignment.Center) {
                    Canvas(Modifier.fillMaxSize()) {
                        val s = size.height * 0.62f
                        drawAnemone(Offset(size.width * 0.72f, size.height * 0.5f), s, 1.2f)
                        drawFish(Offset(size.width * 0.28f, size.height * 0.5f), s, 90f, 1f, 0.3f)
                    }
                }
                PjText("Niveau ${level.number}", size = 30.sp, weight = FontWeight.Bold, align = TextAlign.Center)
                PjText(MazeWorlds.names[level.world], size = 18.sp, weight = FontWeight.Medium, color = Pj.InkSoft, align = TextAlign.Center)
                if (MazeWorlds.indexIn(level.number) == 0 && level.world > 0) {
                    Box(Modifier.fillMaxWidth().background(Color(0xFFE5F7E8), RoundedCornerShape(18.dp)).padding(horizontal = 12.dp, vertical = 8.dp).testTag("world-news")) {
                        PjText("Nouveau : ${MazeWorlds.news[level.world]}", size = 16.sp, weight = FontWeight.Bold, color = Pj.GreenDark, align = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                }
                if (level.number == 1) {
                    Column(Modifier.padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        PjText("Pose ton doigt sur le poisson, puis trace le chemin jusqu'à sa maison !", Modifier.fillMaxWidth(), size = 16.sp, weight = FontWeight.Medium, color = Pj.Ink, align = TextAlign.Center)
                        PjText("Tu t'es trompé ? Repasse sur ton chemin pour reculer.", Modifier.fillMaxWidth(), size = 15.sp, weight = FontWeight.Medium, color = Pj.Ink, align = TextAlign.Center)
                        PjText("Pour 3 étoiles, ne te trompe pas de chemin.", Modifier.fillMaxWidth(), size = 15.sp, weight = FontWeight.Medium, color = Pj.Ink, align = TextAlign.Center)
                    }
                }
                ChunkyButton("C'est parti !", onStart, Modifier.padding(top = 10.dp).fillMaxWidth(), height = 56.dp, fontSize = 24.sp)
                ChunkyButton("Choisir un autre niveau", onMenu, Modifier.padding(top = 4.dp).fillMaxWidth(), color = Pj.Blue, dark = Pj.BlueDark, height = 40.dp, fontSize = 15.sp)
            }
        }
    }
}

/** Le bilan, en bas de l'écran : le labyrinthe et la trace (dorée si parfaite) restent visibles au-dessus, c'est la récompense du jeu. */
@Composable
private fun EndPanel(level: MazeLevel, stars: Int, detours: Int, progress: MazeProgress, onReplay: () -> Unit, onMenu: () -> Unit, onNext: (Int) -> Unit) {
    val feedback = LocalFeedback.current
    val next = level.number + 1
    val hasNext = next <= MazeWorlds.LEVEL_COUNT
    var shown by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        for (i in 0 until stars) {
            kotlinx.coroutines.delay(420)
            shown = i + 1
            feedback.star(i)
        }
    }
    val record = stars > progress.starsOf(level.number) && progress.starsOf(level.number) > 0
    Box(Modifier.fillMaxSize().background(Color(0xFF1B1D2E).copy(alpha = 0.28f))) {
        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), contentAlignment = Alignment.BottomCenter) {
            Panel(
                Modifier.padding(horizontal = 24.dp).padding(bottom = 18.dp).widthIn(max = 460.dp).fillMaxWidth().consumeTaps(),
                corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 24.dp, vertical = 18.dp),
            ) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    PjText(if (record) "Nouveau record !" else "Bravo !", size = 40.sp, weight = FontWeight.Bold, color = if (record) Pj.StarDark else Color(0xFFFF7A1A), align = TextAlign.Center)
                    Stars(filled = shown, star = 34.dp, gap = 6.dp, raiseMiddle = true, modifier = Modifier.padding(top = 4.dp).testTag("final-stars"))
                    PjText("Niveau ${level.number} réussi", Modifier.padding(top = 6.dp), size = 20.sp, weight = FontWeight.SemiBold, align = TextAlign.Center)
                    PjText(
                        if (detours == 0) "Chemin parfait !" else if (detours == 1) "1 impasse" else "$detours impasses",
                        Modifier.testTag("final-detours"), size = 16.sp, weight = FontWeight.Medium, color = if (detours == 0) Pj.GreenDark else Pj.InkSoft, align = TextAlign.Center,
                    )
                    // le bouton vert passe au niveau suivant ; au dernier niveau, rien au-dessus : il rejoue
                    if (hasNext) ChunkyButton("Niveau suivant", { onNext(next) }, Modifier.padding(top = 10.dp).fillMaxWidth(), height = 54.dp, fontSize = 24.sp)
                    else ChunkyButton("Rejouer", onReplay, Modifier.padding(top = 10.dp).fillMaxWidth(), height = 54.dp, fontSize = 24.sp)
                    Row(Modifier.padding(top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (hasNext) ChunkyButton("Rejouer", onReplay, Modifier.weight(1f), color = Pj.Blue, dark = Pj.BlueDark, height = 44.dp, fontSize = 18.sp)
                        ChunkyButton("Niveaux", onMenu, Modifier.weight(1f), color = Pj.Slate, dark = Pj.SlateDark, height = 44.dp, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
