package com.kcharignon.petitsjeux.games.plane.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.offset
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.ui.ChunkyButton
import com.kcharignon.petitsjeux.core.ui.ExitArrow
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Modal
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.consumeTaps
import com.kcharignon.petitsjeux.games.plane.PlaneProgress
import com.kcharignon.petitsjeux.games.plane.engine.PieceKind
import com.kcharignon.petitsjeux.games.plane.engine.PlaneDifficulty
import com.kcharignon.petitsjeux.games.plane.engine.PlaneEvent
import com.kcharignon.petitsjeux.games.plane.engine.PlaneRun
import com.kcharignon.petitsjeux.games.plane.engine.PlaneWorld
import kotlin.math.min
import kotlin.math.sin

/** Où en est le vol. */
private enum class Phase { Intro, Playing, Ending, Done }

/** Ce qui flotte au-dessus de l'avion un instant. */
private enum class Bubble { None, Ouch, Roll, RollAndStar }

/** On laisse l'avion finir de rouler sur la piste (et les animations finir) avant la fenêtre de fin. */
private const val ENDING_HOLD_SECONDS = 0.7f

/**
 * Un vol de Super Avion : le ciel qui défile, le doigt qui glisse, les étoiles. On pose le doigt n'importe où et on glisse : l'avion suit (1 pour 1).
 * [onFinished] est appelé UNE fois, dès que l'avion a touché la piste (le joueur est sauvegardé tout de suite) ; [progress] est ce que le joueur avait avant.
 */
@Composable
fun PlanePlayScreen(
    difficulty: PlaneDifficulty,
    progress: PlaneProgress,
    color: Color,
    onFinished: (stars: Int, score: Int, hits: Int, rolls: Int) -> Unit,
    onNextDifficulty: (PlaneDifficulty) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    /** Pour les tests : une graine pour que le ciel soit toujours le même. */
    seed: Long? = null,
    /** Pour les tests : un ciel tout fait (court, avec les obstacles voulus) à la place de celui tiré au hasard. */
    world: PlaneWorld? = null,
    /** Pour les tests : reçoit le vol en cours pour observer l'avion. */
    onRun: ((PlaneRun) -> Unit)? = null,
    /** Pour les tests : un vol déjà avancé, repris tel quel (sans introduction). Il ne sert qu'au premier vol : « Rejouer » en tire un nouveau. */
    startingRun: PlaneRun? = null,
) {
    var attempt by remember(difficulty) { mutableIntStateOf(0) }
    BackHandler(onBack = onExit)
    key(difficulty, attempt) {
        Race(difficulty, progress, color, seed?.plus(attempt), world, onRun, startingRun.takeIf { attempt == 0 }, skipIntro = attempt > 0, onFinished, onNextDifficulty, onMenu, onExit, onReplay = { attempt++ }, modifier)
    }
}

@Composable
private fun Race(
    difficulty: PlaneDifficulty,
    progress: PlaneProgress,
    color: Color,
    seed: Long?,
    world: PlaneWorld?,
    onRun: ((PlaneRun) -> Unit)?,
    startingRun: PlaneRun?,
    skipIntro: Boolean,
    onFinished: (Int, Int, Int, Int) -> Unit,
    onNextDifficulty: (PlaneDifficulty) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
    onReplay: () -> Unit,
    modifier: Modifier,
) {
    val feedback = LocalFeedback.current
    val run = remember { (startingRun ?: PlaneRun(world ?: PlaneWorld.generate(difficulty, seed ?: System.nanoTime()))).also { onRun?.invoke(it) } }
    val theme = remember { PlaneThemes.of(run.world.theme) }
    var frame by remember { mutableLongStateOf(0L) }
    var phase by remember { mutableStateOf(if (skipIntro || startingRun != null) Phase.Playing else Phase.Intro) }
    var lives by remember { mutableIntStateOf(run.lives) }
    var score by remember { mutableIntStateOf(run.score) }
    var progressNow by remember { mutableFloatStateOf(run.progress) }
    var floating by remember { mutableStateOf(Bubble.None) }
    var floatingAt by remember { mutableFloatStateOf(-10f) }
    var endHold by remember { mutableFloatStateOf(0f) }
    // tant que le doigt n'a pas encore glissé, deux flèches montrent le geste
    var moved by remember { mutableStateOf(run.dist > 0f) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val bestBefore = remember { progress.bestScore(difficulty) }
    val unlockedBefore = remember { PlaneDifficulty.entries.filter { progress.isUnlocked(it) }.toSet() }

    fun handle(event: PlaneEvent) {
        when (event) {
            is PlaneEvent.Hit -> {
                feedback.crash()
                floating = Bubble.Ouch
                floatingAt = run.time
            }
            is PlaneEvent.Roll -> {
                feedback.roll()
                floating = if (event.starBack) Bubble.RollAndStar else Bubble.Roll
                floatingAt = run.time
            }
                        PlaneEvent.Finish -> {
                feedback.land()
                phase = Phase.Ending
                onFinished(run.stars, run.score, run.hits, run.rolls)
            }
            PlaneEvent.Done -> Unit
        }
    }

    // l'horloge du vol : elle n'avance que pendant qu'on joue, et jamais de plus de 50 ms par image (si l'appli est mise de côté, on ne perd rien)
    LaunchedEffect(phase) {
        if (phase != Phase.Playing && phase != Phase.Ending) return@LaunchedEffect
        var last = -1L
        while (true) {
            withFrameNanos { t ->
                val dt = if (last < 0) 0f else min((t - last) / 1_000_000_000f, 0.05f)
                last = t
                for (e in run.update(dt)) handle(e)
                lives = run.lives
                score = run.score
                progressNow = run.progress
                frame++
                if (phase == Phase.Ending && run.done) {
                    endHold += dt
                    if (endHold >= ENDING_HOLD_SECONDS) {
                        phase = Phase.Done
                        feedback.win()
                    }
                }
            }
        }
    }

    Box(modifier.fillMaxSize()) {
        // le ciel : un seul dessin, qui lit l'état du vol à chaque image ; le doigt qui glisse déplace l'avion
        Canvas(
            Modifier
                .fillMaxSize()
                .testTag("road")
                .onSizeChanged { canvasSize = it }
                .systemGestureExclusion()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            for (c in event.changes) {
                                if (c.pressed && c.previousPressed) {
                                    val dx = c.position.x - c.previousPosition.x
                                    // glisser de la largeur d'une unité de ciel au niveau de l'avion le déplace d'une unité : le doigt et l'avion vont ensemble
                                    if ((phase == Phase.Playing || phase == Phase.Ending) && canvasSize.width > 0) {
                                        run.steer(dx / planeUnitPixels(canvasSize.width.toFloat(), canvasSize.height.toFloat()))
                                        if (dx != 0f) moved = true
                                    }
                                    c.consume()
                                }
                            }
                        }
                    }
                },
        ) {
            frame // lire l'image courante : le dessin est refait à chaque image
            drawPlaneScene(run, theme, color, steerHint = phase != Phase.Intro && !moved && !run.autopilot && run.time < 10f)
        }

        // les textes qui flottent au-dessus de l'avion : « Aïe ! », « Tonneau ! »
        FloatingText(run, floating, floatingAt, canvasSize) { frame }

        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
            Column(Modifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // (pendant l'introduction, la flèche est celle posée au-dessus du voile)
                    if (phase == Phase.Intro) Box(Modifier.size(48.dp, 52.dp)) else ExitArrow(onExit, Modifier, 48.dp)
                    Row(
                        Modifier.weight(1f).height(48.dp).background(Color.White.copy(alpha = 0.92f), CircleShape).padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                    ) {
                        PjText("$score", size = 28.sp, weight = FontWeight.Bold, color = Pj.Ink, maxLines = 1, modifier = Modifier.testTag("score"))
                        PjText(" points", Modifier.padding(top = 5.dp), size = 14.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
                    }
                    Row(
                        Modifier
                            .height(48.dp)
                            .graphicsLayer {
                                // les étoiles tremblent quand on en perd une
                                frame
                                translationX = if (run.sinceHit < 0.5f) sin(run.sinceHit * 70f) * (1f - run.sinceHit / 0.5f) * 10.dp.toPx() else 0f
                            }
                            .background(Color.White.copy(alpha = 0.92f), CircleShape)
                            .padding(horizontal = 12.dp)
                            .testTag("lives")
                            .semantics {
                                contentDescription = "Étoiles"
                                stateDescription = "$lives"
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Stars(filled = lives, star = 24.dp, gap = 1.dp)
                    }
                    RoundIconButton(IconKind.LEVELS, onClick = onMenu, modifier = Modifier.testTag("levels"), color = Pj.Slate, dark = Pj.SlateDark, diameter = 48.dp)
                }
                ProgressTrack({ progressNow }, Modifier.padding(top = 10.dp, start = 62.dp, end = 62.dp))
            }
        }

        when (phase) {
            Phase.Intro -> IntroCard(difficulty, color, onStart = { phase = Phase.Playing }, onMenu = onMenu, onExit = onExit)
            Phase.Done -> EndCard(
                difficulty, run.stars, run.score, run.hits, run.rolls, bestBefore, unlockedBefore,
                onReplay = onReplay, onMenu = onMenu, onNext = onNextDifficulty,
            )
            else -> Unit
        }
    }
}

/** La barre d'avancement : le chemin parcouru par l'avion, jusqu'à la piste de l'aéroport (à droite). */
@Composable
private fun ProgressTrack(progress: () -> Float, modifier: Modifier = Modifier) {
    val p = progress().coerceIn(0f, 1f)
    Box(modifier.fillMaxWidth().height(26.dp).testTag("progress"), contentAlignment = Alignment.CenterStart) {
        Box(Modifier.fillMaxWidth().height(10.dp).background(Color.White.copy(alpha = 0.55f), CircleShape))
        Box(Modifier.fillMaxWidth(p.coerceAtLeast(0.02f)).height(10.dp).background(Pj.Green, CircleShape))
        // le petit avion se tient au bout de la barre pleine
        Row(Modifier.fillMaxWidth(p.coerceAtLeast(0.06f)).height(26.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
            Canvas(Modifier.size(26.dp)) { drawTinyPlane() }
        }
        // et la piste de l'aéroport, tout au bout
        Canvas(Modifier.align(Alignment.CenterEnd).size(26.dp, 22.dp)) {
            drawRoundRect(Color(0xFF454A62), cornerRadius = androidx.compose.ui.geometry.CornerRadius(5.dp.toPx()))
            for (k in 0 until 3) drawRect(Color.White, Offset(size.width * (0.16f + k * 0.28f), size.height * 0.44f), androidx.compose.ui.geometry.Size(size.width * 0.14f, size.height * 0.12f))
            drawRect(Color.White.copy(alpha = 0.8f), Offset(0f, size.height * 0.1f), androidx.compose.ui.geometry.Size(size.width, 1.5.dp.toPx()))
            drawRect(Color.White.copy(alpha = 0.8f), Offset(0f, size.height * 0.9f - 1.5.dp.toPx()), androidx.compose.ui.geometry.Size(size.width, 1.5.dp.toPx()))
        }
    }
}

/** Un petit avion vu de dessus, qui va vers la droite, dans une pastille blanche. */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTinyPlane() {
    val w = size.width
    drawCircle(Color.White, w / 2f)
    val orange = Pj.Orange
    val dark = Pj.OrangeDark
    val c = Offset(w / 2f, w / 2f)
    // les ailes
    drawPath(androidx.compose.ui.graphics.Path().apply { moveTo(c.x + w * 0.04f, c.y); lineTo(c.x - w * 0.12f, c.y - w * 0.3f); lineTo(c.x - w * 0.02f, c.y - w * 0.3f); lineTo(c.x + w * 0.16f, c.y); close() }, dark)
    drawPath(androidx.compose.ui.graphics.Path().apply { moveTo(c.x + w * 0.04f, c.y); lineTo(c.x - w * 0.12f, c.y + w * 0.3f); lineTo(c.x - w * 0.02f, c.y + w * 0.3f); lineTo(c.x + w * 0.16f, c.y); close() }, dark)
    // la queue
    drawPath(androidx.compose.ui.graphics.Path().apply { moveTo(c.x - w * 0.3f, c.y); lineTo(c.x - w * 0.36f, c.y - w * 0.14f); lineTo(c.x - w * 0.26f, c.y - w * 0.14f); lineTo(c.x - w * 0.16f, c.y); close() }, dark)
    drawPath(androidx.compose.ui.graphics.Path().apply { moveTo(c.x - w * 0.3f, c.y); lineTo(c.x - w * 0.36f, c.y + w * 0.14f); lineTo(c.x - w * 0.26f, c.y + w * 0.14f); lineTo(c.x - w * 0.16f, c.y); close() }, dark)
    // le fuselage
    drawRoundRect(orange, Offset(c.x - w * 0.34f, c.y - w * 0.07f), androidx.compose.ui.geometry.Size(w * 0.68f, w * 0.14f), androidx.compose.ui.geometry.CornerRadius(w * 0.07f))
    drawCircle(Color(0xFF8FD3FF), w * 0.045f, Offset(c.x + w * 0.12f, c.y))
}

/** « Aïe ! » ou « Tonneau ! » qui monte et s'efface au-dessus de l'avion. Ne lit l'image courante que dans les phases de placement et de dessin (pas de recomposition par image). */
@Composable
private fun FloatingText(run: PlaneRun, kind: Bubble, startedAt: Float, size: IntSize, frame: () -> Long) {
    val (text, color, outline) = when (kind) {
        Bubble.Ouch -> Triple("Aïe !", Color.White, Color(0xFFD63E30))
        Bubble.Roll -> Triple("Tonneau ! +${PlaneRun.RING_POINTS}", Color.White, Color(0xFF2FA845))
        Bubble.RollAndStar -> Triple("Tonneau ! +${PlaneRun.RING_POINTS}\nUne étoile en plus !", Color.White, Color(0xFF2FA845))
        Bubble.None -> Triple("", Color.White, Color.Black)
    }
    if (text.isEmpty()) return
    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer {
                frame()
                val age = (run.time - startedAt) / 1.1f
                alpha = if (age in 0f..1f) (1f - age * age).coerceIn(0f, 1f) else 0f
            },
    ) {
        Box(
            Modifier
                .offset {
                    frame()
                    val at = if (size.width > 0) planeScreenPosition(size.width.toFloat(), size.height.toFloat(), run) else Offset.Zero
                    val rise = ((run.time - startedAt) / 1.1f).coerceIn(0f, 1f) * 46.dp.toPx()
                    IntOffset((at.x - size.width / 2f).toInt(), (at.y - rise - size.height / 2f).toInt())
                }
                .align(Alignment.Center),
        ) {
            PjText(text, size = 24.sp, weight = FontWeight.Bold, color = color, outline = outline, outlineWidth = 6.dp, align = TextAlign.Center)
        }
    }
}

private fun introLines(d: PlaneDifficulty): List<String> = buildList {
    add("Glisse ton doigt à gauche et à droite pour piloter l'avion !")
    add("Évite les oiseaux, les avions, les montgolfières et les parapentes.")
    add("Un choc te fait perdre une étoile, mais tu ne perds jamais.")
    add("Passe dans un anneau lumineux : ton avion fait un tonneau, tu gagnes des points et une étoile !")
    add("À la fin, tu atterris à l'aéroport.")
}

@Composable
private fun IntroCard(d: PlaneDifficulty, color: Color, onStart: () -> Unit, onMenu: () -> Unit, onExit: () -> Unit) {
    Modal(onDismiss = null, topStart = { ExitArrow(onExit, Modifier.align(Alignment.TopStart).padding(start = 14.dp, top = 6.dp), 48.dp) }) {
        Panel(
            Modifier.padding(horizontal = 26.dp).fillMaxWidth().consumeTaps(),
            corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 22.dp, vertical = 22.dp),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    PlaneIcon(null, 56.dp, color)
                    PlaneIcon(PieceKind.BIRDS, 56.dp, color)
                    PlaneIcon(PieceKind.BALLOON, 56.dp, color)
                    PlaneIcon(PieceKind.PARAGLIDER, 56.dp, color)
                    PlaneIcon(PieceKind.RING, 56.dp, color)
                }
                PjText(d.title, Modifier.padding(top = 4.dp), size = 28.sp, weight = FontWeight.Bold, align = TextAlign.Center)
                Column(Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (line in introLines(d)) PjText(line, Modifier.fillMaxWidth(), size = 17.sp, weight = FontWeight.Medium, color = Pj.Ink, align = TextAlign.Center)
                }
                ChunkyButton("C'est parti !", onStart, Modifier.padding(top = 16.dp).fillMaxWidth(), height = 62.dp, fontSize = 26.sp)
                ChunkyButton("Changer de difficulté", onMenu, Modifier.padding(top = 10.dp).fillMaxWidth(), color = Pj.Blue, dark = Pj.BlueDark, height = 46.dp, fontSize = 17.sp)
            }
        }
    }
}

@Composable
private fun EndCard(
    d: PlaneDifficulty,
    stars: Int,
    score: Int,
    hits: Int,
    rolls: Int,
    bestBefore: Int,
    unlockedBefore: Set<PlaneDifficulty>,
    onReplay: () -> Unit,
    onMenu: () -> Unit,
    onNext: (PlaneDifficulty) -> Unit,
) {
    val feedback = LocalFeedback.current
    val record = score > bestBefore && bestBefore > 0
    val next = PlaneDifficulty.entries.getOrNull(d.index + 1)
    // la difficulté suivante vient de s'ouvrir avec ce vol
    val unlocked = next != null && stars >= PlaneDifficulty.UNLOCK_STARS && next !in unlockedBefore
    // la difficulté suivante est proposée dès qu'elle est ouverte : à l'instant (2 étoiles) ou depuis un vol précédent
    val canClimb = next != null && (stars >= PlaneDifficulty.UNLOCK_STARS || next in unlockedBefore)
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
            Modifier.padding(horizontal = 26.dp).fillMaxWidth().consumeTaps(),
            corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 22.dp, vertical = 22.dp),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                PjText(if (record) "Nouveau record !" else "Bravo !", size = 30.sp, weight = FontWeight.Bold, color = if (record) Pj.StarDark else Pj.Ink, align = TextAlign.Center)
                PjText("$score", Modifier.padding(top = 2.dp).testTag("final-score"), size = 60.sp, weight = FontWeight.Bold, color = Pj.GreenDark)
                PjText("points", size = 17.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
                Stars(filled = shown, star = 46.dp, gap = 6.dp, raiseMiddle = true, modifier = Modifier.padding(top = 10.dp).testTag("final-stars"))
                PjText(
                    when {
                        hits == 0 && rolls > 0 -> "Aucun choc et $rolls ${if (rolls > 1) "tonneaux" else "tonneau"} : atterrissage parfait !"
                        hits == 0 -> "Aucun choc : atterrissage parfait !"
                        rolls > 0 -> "Tu as fait $rolls ${if (rolls > 1) "tonneaux" else "tonneau"} !"
                        else -> "Passe dans les anneaux : ils rendent des étoiles !"
                    },
                    Modifier.padding(top = 8.dp), size = 15.sp, weight = FontWeight.Medium, color = Pj.InkSoft, align = TextAlign.Center,
                )
                if (unlocked) {
                    Box(
                        Modifier.padding(top = 12.dp).fillMaxWidth().background(Color(0xFFE5F7E8), RoundedCornerShape(20.dp)).padding(horizontal = 14.dp, vertical = 10.dp).testTag("unlocked"),
                        contentAlignment = Alignment.Center,
                    ) {
                        PjText("Nouvelle difficulté débloquée : « ${next.title.replace(' ', ' ')} » !", size = 17.sp, weight = FontWeight.Bold, color = Pj.GreenDark, align = TextAlign.Center)
                    }
                }
                // le bouton vert rejoue toujours la même difficulté ; l'orange monte d'une difficulté quand elle est ouverte
                ChunkyButton("Rejouer", onReplay, Modifier.padding(top = 14.dp).fillMaxWidth(), height = 58.dp, fontSize = 24.sp)
                Row(Modifier.padding(top = 10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (canClimb) ChunkyButton("Difficulté suivante", { onNext(next) }, Modifier.weight(1f), color = Pj.Orange, dark = Pj.OrangeDark, height = 50.dp, fontSize = 16.sp)
                    ChunkyButton("Difficultés", onMenu, Modifier.weight(1f), color = Pj.Slate, dark = Pj.SlateDark, height = 50.dp, fontSize = 19.sp)
                }
            }
        }
    }
}

