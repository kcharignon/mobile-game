package com.kcharignon.petitsjeux.games.mushrooms.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
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
import com.kcharignon.petitsjeux.core.ui.ExitArrow
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.consumeTaps
import com.kcharignon.petitsjeux.games.mushrooms.MushroomProgress
import com.kcharignon.petitsjeux.games.mushrooms.engine.Difficulty
import com.kcharignon.petitsjeux.games.mushrooms.engine.ForestLayout
import com.kcharignon.petitsjeux.games.mushrooms.engine.MushroomRound
import com.kcharignon.petitsjeux.games.mushrooms.engine.RoundEvent
import kotlin.math.PI
import kotlin.math.hypot
import kotlin.math.sin

/** Où en est la partie. */
private enum class Phase { Intro, Playing, Ending, Done }

/** Ce qui s'anime pour un champignon : quand il a été touché, éteint, ou touché à tort (rien de tout cela ne change les règles). */
private class Fx {
    var pickedAt = -10_000L
    var pickedColor = 0
    var puffAt = -10_000L
    var wobbleAt = -10_000L
    var floatText = ""
    var floatAt = -10_000L
}

/** Combien de temps (ms) de jeu on laisse les animations finir avant la fenêtre de fin. */
private const val ENDING_MS = 900L

/**
 * Une partie de Champis Magiques : la forêt, les champignons qui s'allument, le score. On touche **dès que le doigt se pose** (pas besoin de relever),
 * à plusieurs doigts si on veut. [onFinished] est appelé UNE fois, dès que la partie est finie (le joueur est sauvegardé tout de suite) ;
 * [progress] est ce que le joueur avait avant la partie (pour le record et le déblocage).
 */
@Composable
fun ShroomPlayScreen(
    difficulty: Difficulty,
    progress: MushroomProgress,
    onFinished: (score: Int, picked: Int, bestStreak: Int) -> Unit,
    onNextDifficulty: (Difficulty) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    /** Pour les tests : une graine pour que la partie soit toujours la même. */
    seed: Long? = null,
) {
    var attempt by remember(difficulty) { mutableIntStateOf(0) }
    BackHandler(onBack = onExit)
    key(difficulty, attempt) {
        Round(difficulty, progress, seed?.plus(attempt), skipIntro = attempt > 0, onFinished, onNextDifficulty, onMenu, onExit, onReplay = { attempt++ }, modifier)
    }
}

@Composable
private fun Round(
    difficulty: Difficulty,
    progress: MushroomProgress,
    seed: Long?,
    skipIntro: Boolean,
    onFinished: (Int, Int, Int) -> Unit,
    onNextDifficulty: (Difficulty) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
    onReplay: () -> Unit,
    modifier: Modifier,
) {
    val feedback = LocalFeedback.current
    val density = LocalDensity.current
    val round = remember { MushroomRound(difficulty, seed = seed ?: System.nanoTime()) }
    val fx = remember { List(difficulty.mushrooms) { Fx() } }
    val litColors = remember { mutableStateMapOf<Int, Int>() }
    var clock by remember { mutableLongStateOf(0L) }
    var phase by remember { mutableStateOf(if (skipIntro) Phase.Playing else Phase.Intro) }
    var score by remember { mutableIntStateOf(0) }
    var remaining by remember { mutableIntStateOf(difficulty.targets) }
    var endAt by remember { mutableLongStateOf(0L) }
    val bestBefore = remember { progress.bestScore(difficulty) }
    val unlockedBefore = remember { Difficulty.entries.filter { progress.isUnlocked(it) }.toSet() }
    val textMeasurer = rememberTextMeasurer()
    val time = rememberForestTime()

    fun handle(event: RoundEvent, now: Long) {
        when (event) {
            is RoundEvent.Lit -> litColors[event.id] = event.color
            is RoundEvent.Picked -> {
                val f = fx[event.id]
                f.pickedAt = now
                f.pickedColor = litColors.remove(event.id) ?: 0
                f.floatText = "+${event.points}"
                f.floatAt = now
                // chaque champignon touché joue la note suivante de la mélodie ; le dernier de la partie joue un accord de do
                if (round.remaining == 0) feedback.finalPick() else feedback.pick(round.picked - 1)
            }
            is RoundEvent.Missed -> {
                litColors.remove(event.id)
                fx[event.id].puffAt = now
                feedback.puff()
            }
            is RoundEvent.DecoyGone -> {
                litColors.remove(event.id)
                fx[event.id].puffAt = now
            }
            is RoundEvent.DecoyTapped -> {
                litColors.remove(event.id)
                fx[event.id].wobbleAt = now
                feedback.bonk()
            }
            is RoundEvent.Nothing -> fx[event.id].wobbleAt = now
            RoundEvent.Finished -> {
                phase = Phase.Ending
                endAt = now
                onFinished(round.score, round.picked, round.bestStreak)
            }
        }
        score = round.score
        remaining = round.remaining
    }

    // l'horloge de la partie : elle n'avance que pendant qu'on joue, et jamais de plus de 100 ms par image (si l'appli est mise de côté, on ne perd rien)
    LaunchedEffect(phase) {
        if (phase != Phase.Playing && phase != Phase.Ending) return@LaunchedEffect
        var last = -1L
        while (true) {
            withFrameMillis { t ->
                if (last >= 0) clock += minOf(t - last, 100L)
                last = t
                for (e in round.update(clock)) handle(e, clock)
                if (phase == Phase.Ending && clock - endAt >= ENDING_MS) {
                    phase = Phase.Done
                    feedback.win()
                }
            }
        }
    }

    val playing by rememberUpdatedState(phase == Phase.Playing || phase == Phase.Ending)
    ForestBackdrop(
        modifier,
        overlay = {
            when (phase) {
                Phase.Intro -> IntroCard(difficulty, onStart = { phase = Phase.Playing }, onMenu = onMenu, onExit = onExit)
                Phase.Done -> EndCard(
                    difficulty, round.score, round.bestStreak, bestBefore, unlockedBefore,
                    onReplay = onReplay, onMenu = onMenu, onNext = onNextDifficulty,
                )
                else -> Unit
            }
        },
    ) {
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
                    Modifier.height(48.dp).background(Color.White.copy(alpha = 0.92f), CircleShape).padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    ShroomIcon(30.dp, ShroomColors[0])
                    PjText("$remaining", size = 24.sp, weight = FontWeight.Bold, color = Pj.Ink, modifier = Modifier.testTag("remaining"))
                }
                RoundIconButton(IconKind.LEVELS, onClick = onMenu, modifier = Modifier.testTag("levels"), color = Pj.Slate, dark = Pj.SlateDark, diameter = 48.dp)
            }
            if (difficulty.decoys) {
                Row(
                    Modifier.padding(top = 6.dp).align(Alignment.CenterHorizontally).background(Color.White.copy(alpha = 0.85f), CircleShape).padding(horizontal = 12.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShroomIcon(30.dp, ShroomColors[MushroomRound.FORBIDDEN_COLOR], forbidden = true)
                    PjText("Pas les violets !", size = 15.sp, weight = FontWeight.Bold, color = Color(0xFF6B2FB3))
                }
            }
            BoxWithConstraints(Modifier.weight(1f).fillMaxWidth().padding(top = 6.dp)) {
                val widthDp = maxWidth.value
                val heightDp = maxHeight.value
                val layout = remember(seed, widthDp, heightDp) {
                    ForestLayout.generate(difficulty.mushrooms, widthDp - 4f, heightDp - 4f, (seed ?: 0L) + difficulty.index * 7919L + 13L)
                }
                val dp = density.density
                // toucher : le champignon le plus proche du doigt, s'il est assez près (on est généreux avec les petits doigts)
                fun tapAt(position: Offset) {
                    if (!playing) return
                    var best = -1
                    var bestDistance = Float.MAX_VALUE
                    for (s in layout) {
                        val d = hypot(position.x - (s.x + 2f) * dp, position.y - (s.y + 2f) * dp)
                        if (d <= s.radius * dp * 1.15f && d < bestDistance) {
                            best = s.id
                            bestDistance = d
                        }
                    }
                    if (best >= 0) handle(round.tap(best, clock), clock)
                }
                val tap by rememberUpdatedState(::tapAt)
                Canvas(
                    Modifier
                        .fillMaxSize()
                        .testTag("forest")
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    for (c in event.changes) if (c.changedToDown()) tap(c.position)
                                }
                            }
                        },
                ) {
                    // les champignons du fond (en haut) d'abord
                    for (s in layout) {
                        val f = fx[s.id]
                        val cx = (s.x + 2f) * dp
                        val cy = (s.y + 2f) * dp
                        val r = s.radius * dp
                        val litColor = litColors[s.id]
                        val sinceWobble = (clock - f.wobbleAt).coerceAtLeast(0L)
                        val sincePick = (clock - f.pickedAt).coerceAtLeast(0L)
                        val litFor = litColor?.let { (clock - (round.litMushroom(s.id)?.litAt ?: clock)).coerceAtLeast(0L) } ?: 0L
                        val pulse = 0.5f + 0.5f * sin((time.value * 5f + s.id) * 2f * PI.toFloat() / 2f)
                        // écrasé quand on le touche (ou touché à tort), puis il rebondit
                        val squash = when {
                            sincePick < 260L -> sin(sincePick / 260f * PI.toFloat()) * 0.9f
                            sinceWobble < 300L -> sin(sinceWobble / 300f * PI.toFloat() * 2f) * 0.5f * (1f - sinceWobble / 300f)
                            else -> 0f
                        }
                        // un champignon allumé qui reste allumé longtemps grossit un peu en pulsant : on le voit mieux
                        val lift = if (litColor != null) 1f + 0.035f * pulse else 1f
                        drawMushroom(
                            cx, cy, r * lift, litColor?.let { ShroomColors[it] }, glow = pulse, squash = squash,
                            forbidden = litColor == MushroomRound.FORBIDDEN_COLOR, variant = s.id,
                        )
                        if (litColor != null && litFor < 220L) drawBurst(cx, cy - r * 0.3f, r * 0.7f, ShroomColors[litColor], litFor / 220f * 0.6f)
                    }
                    // étincelles, fumées et points qui montent : par-dessus tout
                    for (s in layout) {
                        val f = fx[s.id]
                        val cx = (s.x + 2f) * dp
                        val cy = (s.y + 2f) * dp
                        val r = s.radius * dp
                        val sincePick = clock - f.pickedAt
                        if (sincePick in 0..520L) drawBurst(cx, cy - r * 0.2f, r, ShroomColors[f.pickedColor.coerceIn(0, 5)], sincePick / 520f)
                        val sincePuff = clock - f.puffAt
                        if (sincePuff in 0..500L) drawPuff(cx, cy, r, sincePuff / 500f)
                        val sinceFloat = clock - f.floatAt
                        if (sinceFloat in 0..800L && f.floatText.isNotEmpty()) {
                            val p = sinceFloat / 800f
                            val style = TextStyle(
                                fontFamily = PjFont, fontWeight = FontWeight.Bold, fontSize = (r / dp * 0.62f).coerceIn(16f, 30f).sp,
                                color = Color.White.copy(alpha = 1f - p * p),
                                shadow = androidx.compose.ui.graphics.Shadow(Color(0xFF0E2A33).copy(alpha = 1f - p), Offset(0f, 3f), 6f),
                            )
                            val measured = textMeasurer.measure(f.floatText, style)
                            drawText(measured, topLeft = Offset(cx - measured.size.width / 2f, cy - r * 1.1f - p * r * 0.9f - measured.size.height / 2f))
                        }
                    }
                }
                // un nœud d'accessibilité par champignon (et pour les tests) : allumé ou éteint
                for (s in layout) {
                    val color = litColors[s.id]
                    Box(
                        Modifier
                            .offset((s.x + 2f - s.radius).dp, (s.y + 2f - s.radius).dp)
                            .size((s.radius * 2).dp)
                            .testTag("shroom-${s.id}")
                            .semantics {
                                contentDescription = "Champignon"
                                stateDescription = when (color) {
                                    null -> "éteint"
                                    MushroomRound.FORBIDDEN_COLOR -> "interdit"
                                    else -> "allumé"
                                }
                            },
                    )
                }
            }
        }
    }
}

private fun introLines(d: Difficulty): List<String> = buildList {
    add("Appuie vite sur les champignons qui s'allument !")
    if (d.maxLit >= 2) add("Jusqu'à ${d.maxLit} champignons allumés en même temps !")
    add("Plus tu es rapide, plus tu gagnes de points.")
    if (d.litMs != null) add("Attention : ils s'éteignent si tu attends trop.")
    if (d.decoys) add("Ne touche pas les champignons violets !")
    if (d.litMs == null) add("Ils restent allumés tant que tu ne les as pas touchés.")
}

@Composable
private fun IntroCard(d: Difficulty, onStart: () -> Unit, onMenu: () -> Unit, onExit: () -> Unit) {
    Modal(onDismiss = null, topStart = { ExitArrow(onExit, Modifier.align(Alignment.TopStart).padding(start = 14.dp, top = 6.dp), 48.dp) }) {
        Panel(
            Modifier.padding(horizontal = 26.dp).fillMaxWidth().consumeTaps(),
            corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 22.dp, vertical = 22.dp),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    for (k in 0 until 3) ShroomIcon(54.dp, ShroomColors[(d.index + k) % 5])
                }
                PjText(d.title, Modifier.padding(top = 4.dp), size = 28.sp, weight = FontWeight.Bold, align = TextAlign.Center)
                Column(Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (line in introLines(d)) PjText(line, Modifier.fillMaxWidth(), size = 17.sp, weight = FontWeight.Medium, color = Pj.Ink, align = TextAlign.Center)
                }
                if (d.decoys) {
                    Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        ShroomIcon(46.dp, ShroomColors[MushroomRound.FORBIDDEN_COLOR], forbidden = true)
                    }
                }
                ChunkyButton("C'est parti !", onStart, Modifier.padding(top = 16.dp).fillMaxWidth(), height = 62.dp, fontSize = 26.sp)
                ChunkyButton("Changer de difficulté", onMenu, Modifier.padding(top = 10.dp).fillMaxWidth(), color = Pj.Blue, dark = Pj.BlueDark, height = 46.dp, fontSize = 17.sp)
            }
        }
    }
}

@Composable
private fun EndCard(
    d: Difficulty,
    score: Int,
    bestStreak: Int,
    bestBefore: Int,
    unlockedBefore: Set<Difficulty>,
    onReplay: () -> Unit,
    onMenu: () -> Unit,
    onNext: (Difficulty) -> Unit,
) {
    val feedback = LocalFeedback.current
    val stars = d.stars(score)
    val record = score > bestBefore && bestBefore > 0
    val next = Difficulty.entries.getOrNull(d.index + 1)
    // la difficulté suivante vient de s'ouvrir avec cette partie
    val unlocked = next != null && stars >= 2 && next !in unlockedBefore
    // la difficulté suivante est proposée dès qu'elle est ouverte : à l'instant (2 étoiles) ou depuis une partie précédente
    val canClimb = next != null && (stars >= 2 || next in unlockedBefore)
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
                    if (bestStreak >= 3) "Plus longue série : $bestStreak champignons !" else "Tu peux faire encore mieux : essaie d'être plus rapide !",
                    Modifier.padding(top = 8.dp), size = 15.sp, weight = FontWeight.Medium, color = Pj.InkSoft, align = TextAlign.Center,
                )
                if (unlocked) {
                    Box(
                        Modifier.padding(top = 12.dp).fillMaxWidth().background(Color(0xFFE5F7E8), RoundedCornerShape(20.dp)).padding(horizontal = 14.dp, vertical = 10.dp).testTag("unlocked"),
                        contentAlignment = Alignment.Center,
                    ) {
                        PjText("Nouvelle difficulté débloquée : « ${next.title.replace(' ', ' ')} » !", size = 17.sp, weight = FontWeight.Bold, color = Pj.GreenDark, align = TextAlign.Center)
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
