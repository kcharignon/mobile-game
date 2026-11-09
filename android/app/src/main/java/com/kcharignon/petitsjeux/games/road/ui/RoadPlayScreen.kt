package com.kcharignon.petitsjeux.games.road.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.ui.ChunkyButton
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Modal
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjFont
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.consumeTaps
import com.kcharignon.petitsjeux.games.road.RoadProgress
import com.kcharignon.petitsjeux.games.road.engine.ChickState
import com.kcharignon.petitsjeux.games.road.engine.Crossing
import com.kcharignon.petitsjeux.games.road.engine.CrossingEvent
import com.kcharignon.petitsjeux.games.road.engine.HopDir
import com.kcharignon.petitsjeux.games.road.engine.Road
import com.kcharignon.petitsjeux.games.road.engine.RoadLevel
import com.kcharignon.petitsjeux.games.road.engine.RoadWorlds
import com.kcharignon.petitsjeux.games.road.engine.TrainLine
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

/** Où en est la traversée. */
private enum class Phase { Intro, Playing, Ending, Done }

/** Un texte qui monte et s'efface (« +10 », « −15 »). [x] en cases, [row] en lignes. */
private class FloatText(val text: String, val x: Double, val row: Double, val color: Color, val at: Long)

/** Ce que l'écran affiche du moteur (le moteur n'est pas observable : on en garde une copie qui l'est). */
private data class Snap(val row: Int, val col: Int, val state: ChickState, val score: Int, val taken: Int, val hits: Int)

private const val ENDING_MS = 1100L

/** Où est le bas de l'écran (en lignes) : la caméra suit le poussin ; si la route est plus courte que l'écran, elle est centrée. */
private fun cameraTarget(chickRow: Float, visibleRows: Float, rows: Int): Float =
    if (rows <= visibleRows) (rows - visibleRows) / 2f else (chickRow - 1.5f).coerceIn(0f, rows - visibleRows)

/**
 * Une traversée : la route (la caméra suit le poussin), le score, les graines. **Toucher n'importe où = sauter d'une ligne vers l'avant** (dès que le doigt se pose) ;
 * la petite croix en bas à gauche fait aller de côté ou reculer. Se faire écraser fait perdre des points, jamais la partie.
 * [onFinished] est appelé UNE fois, à l'arrivée (le joueur est sauvegardé tout de suite) avec le score, les étoiles et le nombre d'écrasements.
 */
@Composable
fun RoadPlayScreen(
    level: RoadLevel,
    progress: RoadProgress,
    onFinished: (score: Int, stars: Int, hits: Int) -> Unit,
    onNext: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var attempt by remember(level.number) { mutableIntStateOf(0) }
    BackHandler(onBack = onMenu)
    key(level.number, attempt) {
        Run(level, progress, skipIntro = attempt > 0, onFinished, onNext, onMenu, onReplay = { attempt++ }, modifier)
    }
}

@Composable
private fun Run(
    level: RoadLevel,
    progress: RoadProgress,
    skipIntro: Boolean,
    onFinished: (Int, Int, Int) -> Unit,
    onNext: (Int) -> Unit,
    onMenu: () -> Unit,
    onReplay: () -> Unit,
    modifier: Modifier,
) {
    val feedback = LocalFeedback.current
    val finished by rememberUpdatedState(onFinished)
    val crossing = remember { Crossing(level) }
    val theme = remember(level.number) { RoadTheme.of(RoadWorlds.theme(level.world)) }
    var clock by remember { mutableLongStateOf(0L) }
    var phase by remember { mutableStateOf(if (skipIntro) Phase.Playing else Phase.Intro) }
    var snap by remember { mutableStateOf(Snap(0, Road.START_COL, ChickState.ALIVE, 0, 0, 0)) }
    var cam by remember { mutableFloatStateOf(0f) }
    var visibleRows by remember { mutableFloatStateOf(6f) }
    var endAt by remember { mutableLongStateOf(0L) }
    var showHint by remember { mutableStateOf(level.number == 1) }
    val grainAt = remember { HashMap<Int, Long>() }
    val texts = remember { ArrayList<FloatText>() }
    val railLights = remember { HashMap<Int, Boolean>() }
    val bestBefore = remember { progress.bestOf(level.number) }
    val textMeasurer = rememberTextMeasurer()

    fun sync() {
        val s = Snap(crossing.row, crossing.col, crossing.state, crossing.score, crossing.taken.size, crossing.hits)
        if (s != snap) snap = s
    }

    fun handle(e: CrossingEvent) {
        val pose = crossing.pose(clock)
        when (e) {
            is CrossingEvent.Hopped -> feedback.hop()
            is CrossingEvent.NewRow -> Unit
            is CrossingEvent.GrainTaken -> {
                grainAt[e.grain.id] = clock
                texts += FloatText("+${e.points}", e.grain.col + 0.5, e.grain.row.toDouble(), Color(0xFFFFE066), clock)
                // chaque graine ramassée joue la note suivante de la petite mélodie
                feedback.pick(e.taken - 1)
            }
            is CrossingEvent.Hit -> {
                texts += FloatText(if (e.lost > 0) "−${e.lost}" else "Aïe !", pose.x, pose.row + 0.2, Color(0xFFFF5A4A), clock)
                feedback.bump(e.by.horn)
            }
            CrossingEvent.Respawned -> Unit
            is CrossingEvent.Arrived -> {
                texts += FloatText("+${e.points}", pose.x, pose.row + 0.2, Color(0xFF4CD964), clock)
                phase = Phase.Ending
                endAt = clock
                finished(crossing.score, level.stars(crossing.score), crossing.hits)
            }
        }
    }

    // l'horloge de la traversée : elle n'avance que pendant qu'on joue, et jamais de plus de 100 ms par image (si l'appli est mise de côté, on ne perd rien)
    LaunchedEffect(phase) {
        if (phase != Phase.Playing && phase != Phase.Ending) return@LaunchedEffect
        var last = -1L
        while (true) {
            withFrameMillis { t ->
                val dt = if (last >= 0) minOf(t - last, 100L) else 0L
                last = t
                clock += dt
                for (e in crossing.update(clock)) handle(e)
                sync()
                val pose = crossing.pose(clock)
                val target = cameraTarget(pose.row.toFloat(), visibleRows, level.rows.size)
                cam += (target - cam) * minOf(1f, dt / 1000f * 8f)
                texts.removeAll { clock - it.at > 900L }
                if (showHint && clock >= 9000L) showHint = false
                // la cloche sonne quand les feux d'une voie ferrée proche se mettent à clignoter
                for ((i, row) in level.rows.withIndex()) {
                    val line = row.traffic as? TrainLine ?: continue
                    val on = line.lightsOn(clock / 1000.0)
                    val was = railLights.put(i, on) ?: false
                    if (on && !was && abs(i - pose.row) <= 5.0) feedback.bell()
                }
                if (phase == Phase.Ending && clock - endAt >= ENDING_MS) {
                    phase = Phase.Done
                    feedback.win()
                }
            }
        }
    }

    fun doHop(dir: HopDir) {
        if (phase != Phase.Playing) return
        for (e in crossing.hop(dir, clock)) handle(e)
        sync()
    }
    val hopForward by rememberUpdatedState { doHop(HopDir.FORWARD) }
    val hopLeft by rememberUpdatedState { doHop(HopDir.LEFT) }
    val hopRight by rememberUpdatedState { doHop(HopDir.RIGHT) }
    val hopBack by rememberUpdatedState { doHop(HopDir.BACK) }

    val shown = snap
    Box(modifier.fillMaxSize().background(theme.grass)) {
        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
            Canvas(
                Modifier
                    .fillMaxSize()
                    .testTag("road")
                    .onSizeChanged {
                        visibleRows = it.height.toFloat() / (it.width / Road.COLS.toFloat())
                        // avant le départ, la caméra se place tout de suite (une route courte est centrée)
                        if (phase == Phase.Intro) cam = cameraTarget(0f, visibleRows, level.rows.size)
                    },
            ) {
                val cell = size.width / Road.COLS
                val t = clock / 1000.0
                val seconds = clock / 1000f
                fun topOf(row: Double): Float = size.height - ((row - cam + 1) * cell).toFloat()
                val firstRow = (floor(cam).toInt() - 1).coerceAtLeast(0)
                val lastRow = (ceil(cam + size.height / cell).toInt() + 1).coerceAtMost(level.last)
                val rows = level.rows
                for (r in firstRow..lastRow) {
                    val row = rows[r]
                    val ctx = RowContext(
                        above = rows.getOrNull(r + 1)?.kind, aboveDirection = rows.getOrNull(r + 1)?.traffic?.direction ?: 0,
                        below = rows.getOrNull(r - 1)?.kind, belowDirection = rows.getOrNull(r - 1)?.traffic?.direction ?: 0,
                        direction = row.traffic?.direction ?: 0,
                    )
                    drawRow(row.kind, theme, topOf(r.toDouble()), cell, r, ctx, (row.traffic as? TrainLine)?.lightsOn(t) ?: false, seconds)
                }
                // les graines
                for (g in level.grains) {
                    if (g.row < firstRow || g.row > lastRow) continue
                    val c = Offset((g.col + 0.5f) * cell, topOf(g.row.toDouble()) + cell / 2)
                    if (g.id !in crossing.taken) drawGrain(c, cell * 0.2f, seconds + g.id)
                    else grainAt[g.id]?.let { drawSparkle(c, cell * 0.3f, (clock - it) / 450f) }
                }
                // le poussin, sous les véhicules : un véhicule qui le touche passe par-dessus
                val pose = crossing.pose(clock)
                val chickAt = Offset((pose.x * cell).toFloat(), topOf(pose.row) + cell / 2)
                val sinceSquash = clock - crossing.squashedAt
                val flat = if (crossing.state == ChickState.SQUASHED) minOf(1f, sinceSquash / 110f) else 0f
                val blinking = crossing.invulnerable(clock) && (clock / 110L) % 2L == 0L
                val celebrate = if (phase == Phase.Ending || phase == Phase.Done) ((clock - endAt) % 450L) / 450.0 else pose.hop
                if (flat <= 0f) drawChick(chickAt, cell * 0.32f, celebrate, 0f, seconds, alpha = if (blinking) 0.4f else 1f)
                if (phase == Phase.Ending) drawSparkle(chickAt, cell * 0.8f, ((clock - endAt) % 700L) / 700f, Color(0xFF4CD964))
                // les véhicules
                for (r in firstRow..lastRow) {
                    val traffic = rows[r].traffic ?: continue
                    val top = topOf(r.toDouble())
                    for (b in traffic.bodies(t, -6.0, Road.COLS + 6.0)) drawVehicle(b, theme, top, cell, seconds)
                }
                // aplati : la galette est dessinée par-dessus, pour qu'on voie bien ce qui s'est passé
                if (flat > 0f) drawChick(chickAt, cell * 0.32f, celebrate, flat, seconds)
                if (crossing.squashedAt >= 0) drawFeathers(chickAt, cell * 0.32f, sinceSquash / 800f)
                // les textes qui montent
                for (f in texts) {
                    val p = ((clock - f.at) / 900f).coerceIn(0f, 1f)
                    val style = TextStyle(
                        fontFamily = PjFont, fontWeight = FontWeight.Bold, fontSize = (cell / density * 0.5f).coerceIn(18f, 32f).sp,
                        color = f.color.copy(alpha = 1f - p * p),
                        shadow = Shadow(Color(0xFF0E2A33).copy(alpha = 1f - p), Offset(0f, 3f), 6f),
                    )
                    val measured = textMeasurer.measure(f.text, style)
                    drawText(measured, topLeft = Offset((f.x * cell).toFloat() - measured.size.width / 2f, topOf(f.row) - cell * 0.1f - p * cell * 0.9f - measured.size.height / 2f))
                }
            }
            // toucher n'importe où : sauter vers l'avant, dès que le doigt se pose
            Box(
                Modifier
                    .fillMaxSize()
                    .testTag("world")
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                for (c in event.changes) if (c.changedToDown()) hopForward()
                            }
                        }
                    },
            )
            // un nœud d'accessibilité (et pour les tests) : où est le poussin
            Box(
                Modifier
                    .size(1.dp)
                    .testTag("chick")
                    .semantics {
                        contentDescription = "Poussin"
                        stateDescription = "ligne ${shown.row} colonne ${shown.col} " + when (shown.state) {
                            ChickState.ALIVE -> "en route"
                            ChickState.SQUASHED -> "écrasé"
                            ChickState.DONE -> "arrivé"
                        }
                    },
            )
            // le haut : retour, niveau, score, graines
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                RoundIconButton(IconKind.BACK, onClick = onMenu, diameter = 46.dp)
                Pill(Modifier.weight(1f)) {
                    PjText("Niveau ${level.number}", size = 20.sp, weight = FontWeight.Bold, color = Pj.Ink, maxLines = 1)
                    PjText("  ·  ${RoadWorlds.names[level.world]}", size = 15.sp, weight = FontWeight.Medium, color = Pj.InkSoft, maxLines = 1)
                }
                Pill {
                    PjText("${snap.score}", size = 24.sp, weight = FontWeight.Bold, color = Pj.Ink, maxLines = 1, modifier = Modifier.testTag("score"))
                    PjText(" pts", Modifier.padding(top = 4.dp), size = 13.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
                }
                Pill {
                    Canvas(Modifier.size(24.dp)) { drawGrain(Offset(size.width / 2, size.height / 2), size.minDimension * 0.34f, 0f) }
                    PjText("${snap.taken}/${level.grains.size}", size = 20.sp, weight = FontWeight.Bold, color = Pj.Ink, maxLines = 1, modifier = Modifier.testTag("grains"))
                }
            }
            // la croix : de côté et en arrière
            Row(Modifier.align(Alignment.BottomStart).padding(start = 14.dp, bottom = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Bottom) {
                PadButton("pad-left", PadArrow.LEFT, "À gauche") { hopLeft() }
                PadButton("pad-back", PadArrow.DOWN, "En arrière") { hopBack() }
                PadButton("pad-right", PadArrow.RIGHT, "À droite") { hopRight() }
            }
            if (showHint && phase == Phase.Playing) {
                Pill(Modifier.align(Alignment.BottomEnd).padding(end = 18.dp, bottom = 18.dp)) {
                    PjText("Touche l'écran pour sauter !", size = 18.sp, weight = FontWeight.Bold, color = Pj.Ink)
                }
            }
        }
        when (phase) {
            Phase.Intro -> IntroCard(level, onStart = { phase = Phase.Playing }, onMenu = onMenu)
            Phase.Done -> EndCard(level, crossing.score, crossing.hits, crossing.taken.size, bestBefore, onReplay, onMenu, onNext)
            else -> Unit
        }
    }
}

@Composable
private fun Pill(modifier: Modifier = Modifier, content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {
    Row(
        modifier.height(46.dp).background(Color.White.copy(alpha = 0.92f), CircleShape).padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, content = content,
    )
}

private enum class PadArrow { LEFT, RIGHT, DOWN }

/** Un gros bouton rond de la croix : il réagit dès que le doigt se pose (pas besoin de relever). */
@Composable
private fun PadButton(tag: String, arrow: PadArrow, label: String, onPress: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val press by rememberUpdatedState(onPress)
    Box(
        Modifier
            .size(62.dp)
            .testTag(tag)
            .semantics { contentDescription = label }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        for (c in event.changes) {
                            if (c.changedToDown()) {
                                pressed = true
                                c.consume()
                                press()
                            } else if (c.changedToUp()) {
                                pressed = false
                            }
                        }
                    }
                }
            },
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val c = Offset(size.width / 2, size.height / 2)
            drawCircle(Color.Black.copy(alpha = 0.18f), size.minDimension / 2, c.copy(y = c.y + 4.dp.toPx()))
            drawCircle(if (pressed) Color(0xFFFFE066) else Color.White.copy(alpha = 0.92f), size.minDimension / 2, c)
            val s = size.minDimension
            val path = Path().apply {
                when (arrow) {
                    PadArrow.LEFT -> { moveTo(s * 0.3f, s * 0.5f); lineTo(s * 0.66f, s * 0.28f); lineTo(s * 0.66f, s * 0.72f) }
                    PadArrow.RIGHT -> { moveTo(s * 0.7f, s * 0.5f); lineTo(s * 0.34f, s * 0.28f); lineTo(s * 0.34f, s * 0.72f) }
                    PadArrow.DOWN -> { moveTo(s * 0.5f, s * 0.7f); lineTo(s * 0.28f, s * 0.34f); lineTo(s * 0.72f, s * 0.34f) }
                }
                close()
            }
            drawPath(path, Pj.Ink)
        }
    }
}

private fun introLines(level: RoadLevel): List<String> = buildList {
    add("Touche l'écran pour sauter d'une ligne. Les flèches font aller de côté ou reculer.")
    add("Attends que la route soit libre, ramasse les graines et arrive de l'autre côté !")
    add("Si un véhicule t'écrase, tu perds des points mais tu continues.")
}

@Composable
private fun IntroCard(level: RoadLevel, onStart: () -> Unit, onMenu: () -> Unit) {
    Modal(onDismiss = null) {
        Panel(
            Modifier.padding(horizontal = 20.dp).widthIn(max = 680.dp).fillMaxWidth().consumeTaps(),
            corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 24.dp, vertical = 18.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(22.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(0.8f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(Modifier.size(96.dp)) { drawChick(Offset(size.width / 2, size.height / 2), size.minDimension * 0.34f, -1.0, 0f, 0f) }
                    PjText("Niveau ${level.number}", size = 30.sp, weight = FontWeight.Bold, align = TextAlign.Center)
                    PjText(RoadWorlds.names[level.world], size = 18.sp, weight = FontWeight.Medium, color = Pj.InkSoft, align = TextAlign.Center)
                    PjText("${level.lanes} voies à traverser", Modifier.padding(top = 2.dp), size = 15.sp, weight = FontWeight.Medium, color = Pj.InkSoft, align = TextAlign.Center)
                }
                Column(Modifier.weight(1.4f), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (RoadWorlds.indexIn(level.number) == 0) {
                        Box(Modifier.fillMaxWidth().background(Color(0xFFE5F7E8), RoundedCornerShape(18.dp)).padding(horizontal = 12.dp, vertical = 8.dp).testTag("world-news")) {
                            PjText("Nouveau : ${RoadWorlds.news[level.world]}", size = 16.sp, weight = FontWeight.Bold, color = Pj.GreenDark, align = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (line in introLines(level)) PjText(line, Modifier.fillMaxWidth(), size = 15.sp, weight = FontWeight.Medium, color = Pj.Ink, align = TextAlign.Center)
                    }
                    ChunkyButton("C'est parti !", onStart, Modifier.padding(top = 12.dp).fillMaxWidth(), height = 54.dp, fontSize = 24.sp)
                    ChunkyButton("Choisir un autre niveau", onMenu, Modifier.padding(top = 8.dp).fillMaxWidth(), color = Pj.Blue, dark = Pj.BlueDark, height = 40.dp, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
private fun EndCard(
    level: RoadLevel,
    score: Int,
    hits: Int,
    taken: Int,
    bestBefore: Int,
    onReplay: () -> Unit,
    onMenu: () -> Unit,
    onNext: (Int) -> Unit,
) {
    val feedback = LocalFeedback.current
    val stars = level.stars(score)
    val record = score > bestBefore && bestBefore > 0
    val next = level.number + 1
    val hasNext = next <= RoadWorlds.LEVEL_COUNT
    var shown by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        for (i in 0 until stars) {
            kotlinx.coroutines.delay(420)
            shown = i + 1
            feedback.star(i)
        }
    }
    Modal(onDismiss = null) {
        Panel(
            Modifier.padding(horizontal = 20.dp).widthIn(max = 680.dp).fillMaxWidth().consumeTaps(),
            corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(22.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    PjText(if (record) "Nouveau record !" else "Bravo !", size = 28.sp, weight = FontWeight.Bold, color = if (record) Pj.StarDark else Pj.Ink, align = TextAlign.Center)
                    PjText("$score", Modifier.testTag("final-score"), size = 56.sp, weight = FontWeight.Bold, color = Pj.GreenDark)
                    PjText("points", size = 16.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
                    Stars(filled = shown, star = 42.dp, gap = 6.dp, raiseMiddle = true, modifier = Modifier.padding(top = 6.dp).testTag("final-stars"))
                }
                Column(Modifier.weight(1.2f), horizontalAlignment = Alignment.CenterHorizontally) {
                    PjText("Graines : $taken / ${level.grains.size}", size = 17.sp, weight = FontWeight.Bold, color = Pj.Ink)
                    PjText(
                        if (hits == 0) "Jamais écrasé !" else if (hits == 1) "Écrasé 1 fois" else "Écrasé $hits fois",
                        Modifier.testTag("final-hits"), size = 17.sp, weight = FontWeight.Medium, color = if (hits == 0) Pj.GreenDark else Pj.InkSoft,
                    )
                    PjText(
                        if (stars == 3) "Parfait !" else "Ramasse toutes les graines sans te faire écraser pour 3 étoiles.",
                        Modifier.padding(top = 4.dp), size = 14.sp, weight = FontWeight.Medium, color = Pj.InkSoft, align = TextAlign.Center,
                    )
                    if (hasNext) ChunkyButton("Niveau suivant", { onNext(next) }, Modifier.padding(top = 10.dp).fillMaxWidth(), height = 52.dp, fontSize = 22.sp)
                    Row(Modifier.padding(top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ChunkyButton("Rejouer", onReplay, Modifier.weight(1f), color = Pj.Orange, dark = Pj.OrangeDark, height = 44.dp, fontSize = 18.sp)
                        ChunkyButton("Menu", onMenu, Modifier.weight(1f), color = Pj.Blue, dark = Pj.BlueDark, height = 44.dp, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
