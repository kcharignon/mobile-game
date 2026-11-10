package com.kcharignon.petitsjeux.games.parking.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.ui.Backdrop
import com.kcharignon.petitsjeux.core.ui.ChunkyButton
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Modal
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.consumeTaps
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjIcon
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.drawStar
import com.kcharignon.petitsjeux.games.parking.engine.Blocker
import com.kcharignon.petitsjeux.games.parking.engine.Car
import com.kcharignon.petitsjeux.games.parking.engine.Cell
import com.kcharignon.petitsjeux.games.parking.engine.EXIT_ACCELERATION
import com.kcharignon.petitsjeux.games.parking.engine.Hint
import com.kcharignon.petitsjeux.games.parking.engine.Level
import com.kcharignon.petitsjeux.games.parking.engine.Move
import com.kcharignon.petitsjeux.games.parking.engine.MotionSource
import com.kcharignon.petitsjeux.games.parking.engine.ParkingGame
import com.kcharignon.petitsjeux.games.parking.engine.Smash
import com.kcharignon.petitsjeux.games.parking.engine.TapResult
import com.kcharignon.petitsjeux.games.parking.engine.Worlds
import com.kcharignon.petitsjeux.games.parking.engine.starsForBumps
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/** Ce qui s'anime pour un véhicule : son avancement le long de son trajet (en cases, 0 = à sa place), sa rougeur de faute, son tremblement. */
class CarVisual {
    val progress = Animatable(0f)
    val flash = Animatable(0f)
    val shake = Animatable(0f)
    var gone by mutableStateOf(false)

    /** Le trajet dessiné du mouvement en cours (avec les virages sur l'huile), `null` tant que le véhicule n'a pas bougé. */
    var path: MotionPath? = null
        private set

    /** Vrai pendant que le véhicule fonce, rebondit et rougit : un autre geste ne compte pas (un enfant impatient ne perd pas deux étoiles d'un coup). */
    var bumping = false

    /** Appelé dès le geste (pas quand l'animation démarre) : deux gestes dans la même image ne comptent qu'une faute. */
    fun startBump() {
        bumping = true
    }

    /** Un nouveau mouvement commence : on retient son trajet. */
    fun begin(path: MotionPath) {
        this.path = path
    }

    /** Distance (en cases) à partir de laquelle le véhicule commence à s'effacer : son nez passe le bord du plateau. */
    var fadeFrom = Float.MAX_VALUE

    /** Le véhicule accélère le long de son trajet jusqu'au bord, passe le bord et disparaît. */
    suspend fun exit(routeLength: Int, len: Int) {
        val distance = routeLength + len + 1f
        fadeFrom = routeLength.toFloat()
        // Même accélération pour tous (la distance parcourue ne dépend que du temps écoulé) : un véhicule parti après un autre, sur la même file, ne le rattrape jamais.
        progress.animateTo(distance, tween(durationMillis = (1000 * sqrt(distance / EXIT_ACCELERATION)).toInt().coerceAtLeast(200), easing = { it * it }))
        gone = true
    }

    /** Faute : il fonce jusqu'à toucher l'obstacle, rougit, rebondit en arrière. */
    suspend fun bump(freeCells: Int, onHit: () -> Unit) {
        try {
            progress.animateTo(freeCells + 0.12f, tween(durationMillis = 90 + 80 * freeCells, easing = LinearEasing))
            onHit()
            flash.snapTo(1f)
            coroutineScope {
                launch { flash.animateTo(0f, tween(500)) }
                progress.animateTo(0f, spring(dampingRatio = 0.4f, stiffness = 380f))
            }
        } finally {
            bumping = false
        }
    }

    /** Le véhicule qui bloque tremble un peu. */
    suspend fun wobble() {
        shake.snapTo(0f)
        shake.animateTo(1f, tween(500, easing = LinearEasing))
        shake.snapTo(0f)
    }

    /** Alpha de la sortie : 1 sur le plateau, 0 une fois sorti. */
    fun alpha(len: Int): Float = (1f - ((progress.value - fadeFrom) / (len + 1f)).coerceIn(0f, 1f))
}

/** Une caisse qui vient de tomber : elle apparaît en rebondissant sur la case que la queue du camion occupait, puis éclate si un véhicule roule dessus. */
class CrateVisual {
    val appear = Animatable(0f)

    /** 0 = entière, 1 = éclatée (un véhicule vient de rouler dessus). */
    val smash = Animatable(0f)
}

/** Pause entre la sortie du dernier véhicule et la fenêtre de victoire. */
const val WIN_DELAY_MS = 800L

/** La bulle « Pouet ! » : où (en pixels, dans la zone du plateau) et quand. */
private class Honk(val at: Offset, val token: Int)

/** Retient où est le plateau, sans provoquer de nouveau dessin (lu seulement quand on touche un véhicule). */
private class GeometryHolder {
    var value: BoardGeometry? = null
}

/** Le petit mot du haut de l'écran : ce que le niveau enseigne (seulement tant qu'on n'a rien bougé). */
fun levelCaption(index: Int): String? = when (index) {
    1 -> "Touche un véhicule : il avance tout droit !"
    2 -> "Glisse un véhicule vers l'arrière : il recule !"
    3 -> "Glisse-le dans le sens que tu veux : avant ou arrière."
    21 -> "Nouveau : un plus grand parking, et des plots de chantier qui bloquent le passage !"
    41 -> "Nouveau : un plus grand parking, et des murs qui ferment certaines sorties !"
    61 -> "Nouveau : un plus grand parking, et un pick-up qui laisse tomber une caisse derrière lui. Si un véhicule roule dessus, il la casse : c'est une faute !"
    81 -> "Nouveau : un plus grand parking, et des taches d'huile : on y tourne à droite en avançant, à gauche en reculant !"
    101 -> "Nouveau : un plus grand parking, et des sens uniques : on ne passe que dans le sens de la flèche !"
    121 -> "Nouveau : un plus grand parking, et des tapis roulants : ils te poussent d'une case sur le côté !"
    141 -> "Nouveau : un plus grand parking, et des téléporteurs : entre par un, ressors par l'autre de la même couleur !"
    161 -> "Nouveau : un plus grand parking, et des barrières : elles s'ouvrent ou se ferment chaque fois qu'un véhicule sort !"
    else -> null
}

/**
 * Un niveau en cours : barre du haut, plateau, barre du bas, puis la fenêtre de victoire.
 * [onFinished] est appelé UNE fois quand le dernier véhicule est sorti, avec le nombre de fautes.
 */
@Composable
fun PlayScreen(
    level: Level,
    hasNext: Boolean,
    onFinished: (bumps: Int) -> Unit,
    onNext: () -> Unit,
    onLevels: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var attempt by remember(level.index) { mutableIntStateOf(0) }
    BackHandler(onBack = onLevels)
    key(level.index, attempt) {
        PlayBoard(level, hasNext, onFinished, onNext, onLevels, onRestart = { attempt++ }, modifier)
    }
}

@Composable
private fun PlayBoard(
    level: Level,
    hasNext: Boolean,
    onFinished: (Int) -> Unit,
    onNext: () -> Unit,
    onLevels: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier,
) {
    val visuals = remember { level.cars.associate { it.id to CarVisual() } }
    val game = remember {
        ParkingGame(level).also { g ->
            // le moteur lit où sont dessinés les véhicules qui roulent : un véhicule envoyé sur l'un d'eux le percute toujours
            g.motionSource = MotionSource { id -> visuals[id]?.progress?.value }
        }
    }
    val crates = remember { mutableStateMapOf<Cell, CrateVisual>() }
    var carsLeft by remember { mutableIntStateOf(game.carsLeft) }
    var bumps by remember { mutableIntStateOf(0) }
    var won by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf<Hint?>(null) }
    var honk by remember { mutableStateOf<Honk?>(null) }
    var honkToken by remember { mutableIntStateOf(0) }
    var touched by remember { mutableStateOf(false) }
    val geometry = remember { GeometryHolder() }
    val scope = rememberCoroutineScope()
    val feedback = LocalFeedback.current
    val theme = remember(level.index) { BoardTheme.of(level.world) }

    LaunchedEffect(hint) {
        if (hint != null) {
            delay(4000)
            hint = null
        }
    }
    LaunchedEffect(honk) {
        val shown = honk ?: return@LaunchedEffect
        delay(900)
        if (honk?.token == shown.token) honk = null
    }

    /** Un véhicule roule sur des caisses : chacune éclate quand il arrive sur sa case (c'est une faute, déjà comptée par le moteur). */
    fun smashCrates(smashed: List<Smash>) {
        for (s in smashed) {
            val crate = crates[s.cell] ?: continue
            scope.launch {
                delay((sqrt((s.index + 0.5f) / EXIT_ACCELERATION) * 1000f).toLong())
                feedback.bonk()
                crate.smash.animateTo(1f, tween(280))
                if (crates[s.cell] === crate) crates.remove(s.cell)
            }
        }
    }

    fun act(car: Car, move: Move) {
        if (won || visuals.getValue(car.id).bumping) return
        hint = null
        touched = true
        when (val result = game.tap(car.id, move)) {
            TapResult.Ignored -> Unit
            is TapResult.Exited -> {
                carsLeft = game.carsLeft
                feedback.exit()
                bumps = game.bumps
                smashCrates(result.smashed)
                // Le niveau est réussi (et sauvegardé) dès que le dernier véhicule est parti : quitter pendant son animation ne perd rien.
                if (game.isWon && !finished) {
                    finished = true
                    onFinished(game.bumps)
                }
                result.dropped?.let { cell ->
                    // la caisse tombe sur la case que la queue du camion occupait ; le prochain véhicule qui y passe la casse (une faute)
                    val crate = CrateVisual()
                    crates[cell] = crate
                    scope.launch { crate.appear.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = 260f)) }
                }
                visuals.getValue(car.id).begin(MotionPath(car, move, result.trip))
                scope.launch {
                    visuals.getValue(car.id).exit(result.route.size, car.len)
                    game.finishMotion(car.id)
                    if (game.isWon && !won) {
                        // un court instant pour savourer le plateau vide, puis la fenêtre de victoire
                        delay(WIN_DELAY_MS)
                        won = true
                        feedback.win()
                    }
                }
            }
            is TapResult.Bumped -> {
                bumps = game.bumps
                feedback.bump(car.len)
                smashCrates(result.smashed)
                val path = MotionPath(car, move, result.trip)
                visuals.getValue(car.id).begin(path)
                visuals.getValue(car.id).startBump()
                scope.launch {
                    visuals.getValue(car.id).bump(result.freeCells) {
                        geometry.value?.let { g ->
                            honk = Honk(noseCenter(g, path, result.freeCells + 0.12f), ++honkToken)
                        }
                    }
                    game.finishMotion(car.id)
                }
                (result.blocker as? Blocker.Vehicle)?.let { scope.launch { visuals.getValue(it.car.id).wobble() } }
            }
        }
    }

    Backdrop(
        modifier,
        overlay = {
            if (won) {
                WinCard(level, game.stars, level.cars.size, game.bumps, hasNext, onNext, onReplay = onRestart, onLevels)
            }
        },
    ) {
        Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RoundIconButton(IconKind.BACK, onClick = onLevels, diameter = 48.dp)
                Box(
                    Modifier.weight(1f).padding(horizontal = 14.dp).height(46.dp).background(Color.White.copy(alpha = 0.92f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    PjText("Niveau ${level.index}", size = 24.sp, weight = FontWeight.Bold)
                }
                RoundIconButton(IconKind.RESTART, onClick = onRestart, diameter = 48.dp)
            }
            Box(Modifier.fillMaxWidth().padding(top = 6.dp), contentAlignment = Alignment.Center) {
                Stars(filled = starsForBumps(bumps), star = 28.dp, gap = 4.dp, raiseMiddle = true)
            }
            val caption = levelCaption(level.index)
            if (caption != null && !touched) {
                PjText(
                    caption,
                    Modifier.fillMaxWidth().padding(top = 2.dp),
                    size = 15.sp, weight = FontWeight.Medium, color = Pj.Ink, align = TextAlign.Center,
                )
            }

            BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
                val density = LocalDensity.current
                val widthPx = with(density) { maxWidth.toPx() }
                val heightPx = with(density) { maxHeight.toPx() }
                val g = remember(level, widthPx, heightPx) { BoardGeometry.fit(level.cols, level.rows, widthPx, heightPx) }
                geometry.value = g
                val hintPhase by rememberInfiniteTransition(label = "hint").animateFloat(
                    0f, 1f, infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart), label = "dash",
                )
                // les tapis roulants défilent et les téléporteurs tournent
                val floorPhase by rememberInfiniteTransition(label = "floor").animateFloat(
                    0f, 1f, infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart), label = "belt",
                )

                Canvas(
                    Modifier.fillMaxSize().testTag("board").pointerInput(level, g) {
                        // Un simple toucher fait avancer ; un glissement le long du véhicule le fait avancer ou reculer selon son sens.
                        val threshold = 14.dp.toPx()
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            val cell = g.cellAt(down.position)
                            val car = cell?.let { (c, r) -> game.cars.firstOrNull { Cell(c, r) in it.cells } } ?: return@awaitEachGesture
                            var decided = false
                            while (true) {
                                val change = awaitPointerEvent().changes.firstOrNull { it.id == down.id } ?: break
                                val delta = change.position - down.position
                                val along = delta.x * car.dir.dc + delta.y * car.dir.dr
                                val across = abs(delta.x * car.dir.dr) + abs(delta.y * car.dir.dc)
                                if (!decided && maxOf(abs(along), across) > threshold) {
                                    decided = true
                                    // un glissement en travers du véhicule ne fait rien
                                    if (abs(along) >= across) act(car, if (along > 0) Move.FORWARD else Move.BACKWARD)
                                    change.consume()
                                }
                                if (!change.pressed) {
                                    if (!decided) act(car, Move.FORWARD)
                                    break
                                }
                            }
                        }
                    },
                ) {
                    drawBoard(g, theme)
                    drawBoardDecor(g)
                    // murs sur le bord : ils murent certaines sorties
                    for (wall in level.walls) drawWall(g, wall)
                    // plots de chantier : fixes
                    for (cone in level.cones) {
                        val tl = g.cellTopLeft(cone.col, cone.row)
                        drawCone(tl.x + g.s / 2, tl.y + g.s * 0.58f, g.s * 1.05f)
                    }
                    // taches d'huile : au sol, les véhicules roulent dessus et y font un virage
                    for (oil in level.oils) {
                        val tl = g.cellTopLeft(oil.col, oil.row)
                        drawOil(tl.x + g.s / 2, tl.y + g.s / 2, g.s)
                    }
                    // sens uniques, tapis roulants, téléporteurs : au sol, sous les véhicules
                    for (arrow in level.oneWays) {
                        val tl = g.cellTopLeft(arrow.cell.col, arrow.cell.row)
                        drawOneWay(tl.x + g.s / 2, tl.y + g.s / 2, g.s, arrow.dir)
                    }
                    for (belt in level.belts) {
                        val tl = g.cellTopLeft(belt.cell.col, belt.cell.row)
                        drawBelt(tl.x + g.s / 2, tl.y + g.s / 2, g.s, belt.dir, floorPhase)
                    }
                    for ((k, portal) in level.portals.withIndex()) for (end in listOf(portal.a, portal.b)) {
                        val tl = g.cellTopLeft(end.col, end.row)
                        drawPortal(tl.x + g.s / 2, tl.y + g.s / 2, g.s, PortalColors[k % PortalColors.size], floorPhase)
                    }
                    // barrières : elles changent à chaque véhicule qui sort (carsLeft change à ce moment-là)
                    if (carsLeft >= 0) for (gate in level.gates) {
                        val tl = g.cellTopLeft(gate.cell.col, gate.cell.row)
                        drawGate(tl.x + g.s / 2, tl.y + g.s / 2, g.s, game.isOpen(gate))
                    }
                    // caisses tombées : sous les véhicules (le camion qui part passe par-dessus)
                    for ((cell, crate) in crates) {
                        val tl = g.cellTopLeft(cell.col, cell.row)
                        drawCrate(tl.x + g.s / 2, tl.y + g.s / 2, g.s, crate.appear.value, crate.smash.value)
                    }
                    // les véhicules qui roulent passent par-dessus les autres
                    val order = level.cars.filterNot { visuals.getValue(it.id).gone }.sortedBy { if (abs(visuals.getValue(it.id).progress.value) > 0.01f) 1 else 0 }
                    for (car in order) {
                        val v = visuals.getValue(car.id)
                        val pose = carPose(g, car, v)
                        drawVehicle(
                            model = vehicleModelFor(car),
                            color = Pj.CarColors[car.color % Pj.CarColors.size],
                            s = g.s,
                            center = pose.first,
                            degrees = pose.second,
                            flash = v.flash.value,
                            alpha = v.alpha(car.len) * pose.third,
                        )
                    }
                    hint?.let { h ->
                        val car = h.car
                        if (!visuals.getValue(car.id).gone) {
                            val horizontal = car.dir.horizontal
                            val tl = g.cellTopLeft(car.col, car.row)
                            val w = (if (horizontal) car.len else 1) * g.s
                            val hh = (if (horizontal) 1 else car.len) * g.s
                            val dash = 10.dp.toPx()
                            drawRoundRect(
                                Color.White, Offset(tl.x - 3.dp.toPx(), tl.y - 3.dp.toPx()), Size(w + 6.dp.toPx(), hh + 6.dp.toPx()), CornerRadius(g.s * 0.45f),
                                style = Stroke(5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash * 0.7f), hintPhase * dash * 1.7f)),
                            )
                            // deux chevrons qui filent dans le bon sens : c'est de ce côté-là qu'il faut le glisser
                            val d = car.directionOf(h.move)
                            val lead = car.lead(h.move)
                            val base = g.cellTopLeft(lead.col, lead.row)
                            for (k in 0..1) {
                                val travel = (hintPhase + k * 0.5f) % 1f
                                val centre = Offset(
                                    base.x + g.s / 2 + d.dc * (0.9f + travel * 0.9f) * g.s,
                                    base.y + g.s / 2 + d.dr * (0.9f + travel * 0.9f) * g.s,
                                )
                                drawChevron(centre, d.degrees, g.s * 0.28f, 1f - travel)
                            }
                        }
                    }
                }
                honk?.let { h ->
                    Box(
                        Modifier
                            .offset { IntOffset((h.at.x - 42.dp.toPx()).toInt(), (h.at.y - 58.dp.toPx()).toInt()) }
                            .rotate(-8f)
                            .background(Color.White, RoundedCornerShape(50))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                    ) { PjText("Pouet !", size = 20.sp, weight = FontWeight.Bold, color = Pj.Red) }
                }
            }

            Row(Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    Modifier.height(58.dp).background(Color.White.copy(alpha = 0.92f), CircleShape).padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Canvas(Modifier.size(44.dp, 26.dp)) {
                        drawVehicle(VehicleModel.SEDAN, Pj.CarColors[4], 20.dp.toPx(), center, 90f, shadow = false)
                    }
                    PjText("$carsLeft", size = 28.sp, weight = FontWeight.Bold)
                }
                ChunkyButton(
                    "Indice", onClick = { if (!won) { hint = game.hint(); if (hint != null) feedback.hint() } },
                    Modifier.weight(1f), color = Pj.Orange, dark = Pj.OrangeDark, height = 58.dp, fontSize = 24.sp,
                    leading = { PjIcon(IconKind.BULB, size = 24.dp) },
                )
            }
        }
    }
}

/**
 * Où et comment est dessiné un véhicule : (centre en pixels, rotation en degrés). À l'arrêt : à sa place. En mouvement : sur son trajet (qui peut tourner sur l'huile),
 * avec un petit tremblement en travers quand un autre véhicule le percute.
 */
private fun carPose(g: BoardGeometry, car: Car, v: CarVisual): Triple<Offset, Float, Float> {
    val path = v.path
    val horizontal = car.dir.horizontal
    val w = (if (horizontal) car.len else 1) * g.s
    val h = (if (horizontal) 1 else car.len) * g.s
    val rest = g.cellTopLeft(car.col, car.row).let { Offset(it.x + w / 2, it.y + h / 2) }
    val shaking = sin(v.shake.value * 3f * 2f * PI.toFloat()) * (1f - v.shake.value) * 0.07f * g.s
    if (path == null) {
        // le tremblement se fait en travers de la voie
        return Triple(Offset(rest.x + if (horizontal) 0f else shaking, rest.y + if (horizontal) shaking else 0f), car.dir.degrees, 1f)
    }
    val pose = path.at(v.progress.value.coerceAtLeast(-0.2f))
    val heading = path.headingDegrees(pose.travelDegrees)
    val rad = Math.toRadians(heading.toDouble())
    // en travers du nez : (cos, sin) du cap
    val lateral = Offset(cos(rad).toFloat() * shaking, sin(rad).toFloat() * shaking)
    return Triple(Offset(g.origin.x + pose.col * g.s + lateral.x, g.origin.y + pose.row * g.s + lateral.y), heading, pose.alpha)
}

/** Le bout du nez après avoir parcouru [cells] cases de son trajet : là où la bulle « Pouet ! » se pose. */
private fun noseCenter(g: BoardGeometry, path: MotionPath, cells: Float): Offset {
    val pose = path.noseAt(cells)
    return Offset(g.origin.x + pose.col * g.s, g.origin.y + pose.row * g.s)
}

/** La fenêtre de victoire : confettis, étoiles qui s'allument une à une, puis les trois boutons. */
@Composable
fun WinCard(
    level: Level,
    stars: Int,
    cars: Int,
    bumps: Int,
    hasNext: Boolean,
    onNext: () -> Unit,
    onReplay: () -> Unit,
    onLevels: () -> Unit,
) {
    var shown by remember { mutableIntStateOf(0) }
    val feedback = LocalFeedback.current
    LaunchedEffect(stars) {
        repeat(stars) {
            delay(320)
            shown = it + 1
            feedback.star(it)
        }
    }
    Modal(onDismiss = null) {
        Confetti(Modifier.fillMaxSize())
        Panel(
            Modifier.padding(horizontal = 36.dp).fillMaxWidth().consumeTaps(),
            corner = 38.dp, ledge = 8.dp,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 28.dp, vertical = 24.dp),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                PjText("Bravo !", size = 46.sp, weight = FontWeight.Bold, color = Color(0xFFFF7A1A))
                Row(Modifier.padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.Bottom) {
                    for (i in 0 until 3) {
                        val big = i == 1
                        val box = if (big) 84.dp else 64.dp
                        val on = i < stars && i < shown
                        val scale = androidx.compose.animation.core.animateFloatAsState(
                            if (on) 1f else if (i < stars) 0.4f else 1f,
                            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "star",
                        ).value
                        Canvas(Modifier.size(box).scale(scale).offset(y = if (big) (-10).dp else 0.dp)) {
                            drawStar(center, this.size.minDimension / 2 - 3.dp.toPx(), on = on)
                        }
                    }
                }
                PjText("Niveau ${level.index} réussi", Modifier.padding(top = 4.dp), size = 22.sp)
                PjText(
                    "$cars véhicules · " + if (bumps == 0) "0 faute" else if (bumps == 1) "1 faute" else "$bumps fautes",
                    Modifier.padding(top = 2.dp), size = 16.sp, weight = FontWeight.Medium, color = Pj.InkSoft,
                )
                if (hasNext) {
                    ChunkyButton("Niveau suivant", onNext, Modifier.padding(top = 18.dp).fillMaxWidth(), height = 62.dp, fontSize = 26.sp)
                } else {
                    PjText(
                        "Tu as réussi les ${Worlds.LEVEL_COUNT} niveaux !", Modifier.padding(top = 14.dp),
                        size = 18.sp, weight = FontWeight.Bold, color = Pj.GreenDark, align = TextAlign.Center,
                    )
                }
                Row(Modifier.padding(top = 14.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    ChunkyButton("Rejouer", onReplay, Modifier.weight(1f), color = Pj.Blue, dark = Pj.BlueDark, height = 46.dp, fontSize = 18.sp)
                    ChunkyButton("Niveaux", onLevels, Modifier.weight(1f), color = Pj.Slate, dark = Pj.SlateDark, height = 46.dp, fontSize = 18.sp)
                }
            }
        }
    }
}

/** Des confettis qui tombent en tournant. */
@Composable
private fun Confetti(modifier: Modifier = Modifier) {
    val t by rememberInfiniteTransition(label = "confetti").animateFloat(0f, 1f, infiniteRepeatable(tween(4200, easing = LinearEasing)), label = "t")
    val palette = listOf(Pj.Red, Pj.Blue, Pj.Green, Color(0xFFFFD93B), Color(0xFFF0409A), Color(0xFFA55BE8))
    Canvas(modifier) {
        for (i in 0 until 46) {
            val x = (i * 97 % 100) / 100f * size.width
            val speed = 0.6f + (i * 37 % 40) / 100f
            val y = (((i * 53 % 100) / 100f + t * speed) % 1f) * (size.height + 40.dp.toPx()) - 20.dp.toPx()
            rotate(((i * 37) % 180) + t * 360f * (if (i % 2 == 0) 1 else -1), Offset(x, y)) {
                drawRoundRect(palette[i % palette.size], Offset(x, y), Size(9.dp.toPx(), 15.dp.toPx()), CornerRadius(3.dp.toPx()))
            }
        }
    }
}
