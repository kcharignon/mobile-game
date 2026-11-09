package com.kcharignon.petitsjeux.games.parking.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
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
import androidx.compose.foundation.gestures.detectTapGestures
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
import com.kcharignon.petitsjeux.games.parking.engine.Car
import com.kcharignon.petitsjeux.games.parking.engine.Cell
import com.kcharignon.petitsjeux.games.parking.engine.Level
import com.kcharignon.petitsjeux.games.parking.engine.ParkingGame
import com.kcharignon.petitsjeux.games.parking.engine.TapResult
import com.kcharignon.petitsjeux.games.parking.engine.Worlds
import com.kcharignon.petitsjeux.games.parking.engine.starsForBumps
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/** Ce qui s'anime pour une voiture : sa position le long de sa voie (en cases), sa rougeur de faute, son tremblement. */
class CarVisual {
    val offset = Animatable(0f)
    val flash = Animatable(0f)
    val shake = Animatable(0f)
    var gone by mutableStateOf(false)

    /** Vrai pendant que la voiture fonce, rebondit et rougit : un autre toucher ne compte pas (un enfant impatient ne perd pas deux étoiles d'un coup). */
    var bumping = false

    /** Appelé dès le toucher (pas quand l'animation démarre) : deux touchers dans la même image ne comptent qu'une faute. */
    fun startBump() {
        bumping = true
    }

    /** Distance (en cases) à partir de laquelle la voiture commence à s'effacer : son nez passe le bord du plateau. */
    var fadeFrom = Float.MAX_VALUE

    /** La voiture accélère jusqu'au bord, passe le bord et disparaît. */
    suspend fun exit(pathLength: Int, len: Int) {
        val distance = pathLength + len + 1f
        fadeFrom = pathLength.toFloat()
        offset.animateTo(distance, tween(durationMillis = (260 + 70 * distance).toInt(), easing = FastOutLinearInEasing))
        gone = true
    }

    /** Faute : elle fonce jusqu'à toucher l'autre voiture, rougit, rebondit en arrière. */
    suspend fun bump(freeCells: Int, onHit: () -> Unit) {
        try {
            offset.animateTo(freeCells + 0.12f, tween(durationMillis = 90 + 80 * freeCells, easing = LinearEasing))
            onHit()
            flash.snapTo(1f)
            coroutineScope {
                launch { flash.animateTo(0f, tween(500)) }
                offset.animateTo(0f, spring(dampingRatio = 0.4f, stiffness = 380f))
            }
        } finally {
            bumping = false
        }
    }

    /** La voiture qui bloque tremble un peu. */
    suspend fun wobble() {
        shake.snapTo(0f)
        shake.animateTo(1f, tween(500, easing = LinearEasing))
        shake.snapTo(0f)
    }

    /** Alpha de la sortie : 1 sur le plateau, 0 une fois sortie. */
    fun alpha(len: Int): Float = (1f - ((offset.value - fadeFrom) / (len + 1f)).coerceIn(0f, 1f))
}

/** La bulle « Pouet ! » : où (en pixels, dans la zone du plateau) et quand. */
private class Honk(val at: Offset, val token: Int)

/** Retient où est le plateau, sans provoquer de nouveau dessin (lu seulement quand on touche une voiture). */
private class GeometryHolder {
    var value: BoardGeometry? = null
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
    val game = remember { ParkingGame(level) }
    val visuals = remember { level.cars.associate { it.id to CarVisual() } }
    var carsLeft by remember { mutableIntStateOf(game.carsLeft) }
    var bumps by remember { mutableIntStateOf(0) }
    var won by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var hintId by remember { mutableStateOf<Int?>(null) }
    var honk by remember { mutableStateOf<Honk?>(null) }
    var honkToken by remember { mutableIntStateOf(0) }
    val geometry = remember { GeometryHolder() }
    val scope = rememberCoroutineScope()
    val feedback = LocalFeedback.current
    val theme = remember(level.index) { BoardTheme.of(level.world) }

    LaunchedEffect(hintId) {
        if (hintId != null) {
            delay(4000)
            hintId = null
        }
    }
    LaunchedEffect(honk) {
        val shown = honk ?: return@LaunchedEffect
        delay(900)
        if (honk?.token == shown.token) honk = null
    }

    fun tapCar(car: Car) {
        if (won || visuals.getValue(car.id).bumping) return
        hintId = null
        when (val result = game.tap(car.id)) {
            TapResult.Ignored -> Unit
            is TapResult.Exited -> {
                carsLeft = game.carsLeft
                feedback.exit()
                // Le niveau est réussi (et sauvegardé) dès que la dernière voiture est partie : quitter pendant son animation ne perd rien.
                if (game.isWon && !finished) {
                    finished = true
                    onFinished(game.bumps)
                }
                scope.launch {
                    visuals.getValue(car.id).exit(result.pathLength, car.len)
                    // la fenêtre de victoire attend la fin de l'animation
                    if (game.isWon && !won) {
                        won = true
                        feedback.win()
                    }
                }
            }
            is TapResult.Bumped -> {
                bumps = game.bumps
                feedback.bump()
                visuals.getValue(car.id).startBump()
                scope.launch {
                    visuals.getValue(car.id).bump(result.freeCells) {
                        geometry.value?.let { g ->
                            val nose = noseCenter(g, car, result.freeCells + 0.12f)
                            honk = Honk(nose, ++honkToken)
                        }
                    }
                }
                scope.launch { visuals.getValue(result.blocker.id).wobble() }
            }
        }
    }

    Backdrop(
        modifier,
        overlay = { if (won) WinCard(level, game.stars, level.cars.size, game.bumps, hasNext, onNext, onReplay = onRestart, onLevels) },
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
            if (level.index <= 2 && carsLeft == level.cars.size) {
                PjText(
                    "Touche une voiture libre : elle roule tout droit !",
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

                Canvas(
                    Modifier.fillMaxSize().testTag("board").pointerInput(level, g) {
                        detectTapGestures { p ->
                            val cell = g.cellAt(p) ?: return@detectTapGestures
                            game.cars.firstOrNull { Cell(cell.first, cell.second) in it.cells }?.let(::tapCar)
                        }
                    },
                ) {
                    drawBoard(g, theme)
                    drawBoardDecor(g)
                    // les voitures qui roulent passent par-dessus les autres
                    val order = level.cars.filterNot { visuals.getValue(it.id).gone }.sortedBy { if (visuals.getValue(it.id).offset.value > 0.01f) 1 else 0 }
                    for (car in order) {
                        val v = visuals.getValue(car.id)
                        drawCar(
                            len = car.len,
                            color = Pj.CarColors[car.color % Pj.CarColors.size],
                            s = g.s,
                            center = carCenter(g, car, v),
                            degrees = car.dir.degrees,
                            flash = v.flash.value,
                            alpha = v.alpha(car.len),
                        )
                    }
                    hintId?.let { id ->
                        val car = level.cars.firstOrNull { it.id == id }
                        if (car != null && !visuals.getValue(id).gone) {
                            val horizontal = car.dir.horizontal
                            val tl = g.cellTopLeft(car.col, car.row)
                            val w = (if (horizontal) car.len else 1) * g.s
                            val h = (if (horizontal) 1 else car.len) * g.s
                            val dash = 10.dp.toPx()
                            drawRoundRect(
                                Color.White, Offset(tl.x - 3.dp.toPx(), tl.y - 3.dp.toPx()), Size(w + 6.dp.toPx(), h + 6.dp.toPx()), CornerRadius(g.s * 0.45f),
                                style = Stroke(5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash * 0.7f), hintPhase * dash * 1.7f)),
                            )
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
                        drawCar(2, Pj.CarColors[4], 20.dp.toPx(), center, 90f, shadow = false)
                    }
                    PjText("$carsLeft", size = 28.sp, weight = FontWeight.Bold)
                }
                ChunkyButton(
                    "Indice", onClick = { if (!won) hintId = game.hint()?.id },
                    Modifier.weight(1f), color = Pj.Orange, dark = Pj.OrangeDark, height = 58.dp, fontSize = 24.sp,
                    leading = { PjIcon(IconKind.BULB, size = 24.dp) },
                )
            }
        }

    }
}

/** Le centre d'une voiture à l'écran : sa case de départ, avancée de `visual.offset` cases le long de sa voie, avec le tremblement. */
private fun carCenter(g: BoardGeometry, car: Car, v: CarVisual): Offset {
    val horizontal = car.dir.horizontal
    val tl = g.cellTopLeft(car.col, car.row)
    val w = (if (horizontal) car.len else 1) * g.s
    val h = (if (horizontal) 1 else car.len) * g.s
    val along = v.offset.value * g.s
    val wobble = sin(v.shake.value * 3f * 2f * PI.toFloat()) * (1f - v.shake.value) * 0.07f * g.s
    // le tremblement se fait en travers de la voie
    val dx = car.dir.dc * along + (if (horizontal) 0f else wobble)
    val dy = car.dir.dr * along + (if (horizontal) wobble else 0f)
    return Offset(tl.x + w / 2 + dx, tl.y + h / 2 + dy)
}

/** Le nez d'une voiture après avoir avancé de [cells] cases : là où la bulle « Pouet ! » se pose. */
private fun noseCenter(g: BoardGeometry, car: Car, cells: Float): Offset {
    val head = car.head
    val c = g.cellTopLeft(head.col, head.row)
    return Offset(c.x + g.s / 2 + car.dir.dc * (cells + 0.4f) * g.s, c.y + g.s / 2 + car.dir.dr * (cells + 0.4f) * g.s)
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
    LaunchedEffect(stars) {
        repeat(stars) {
            delay(320)
            shown = it + 1
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
                    "$cars voitures · " + if (bumps == 0) "0 faute" else if (bumps == 1) "1 faute" else "$bumps fautes",
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
