package com.kcharignon.petitsjeux.games.find.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.ui.ChunkyButton
import com.kcharignon.petitsjeux.core.ui.ExitArrow
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.Stars
import com.kcharignon.petitsjeux.core.ui.consumeTaps
import com.kcharignon.petitsjeux.games.find.FindProgress
import com.kcharignon.petitsjeux.games.find.engine.FindDifficulty
import com.kcharignon.petitsjeux.games.find.engine.FindLayout
import com.kcharignon.petitsjeux.games.find.engine.FindOutcome
import com.kcharignon.petitsjeux.games.find.engine.FindPlay
import com.kcharignon.petitsjeux.games.find.engine.FindRound
import com.kcharignon.petitsjeux.games.find.engine.FindSpot
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

/** Le temps (ms) entre le dernier objet trouvé (il saute de joie) et la fenêtre de bilan. */
const val END_DELAY_MS = 900L

/** Le plus gros qu'un objet peut être, en dp (pour ne pas avoir de géants quand il y en a peu). */
private const val MAX_OBJECT_DP = 140

/** La durée (ms) sur laquelle les objets apparaissent l'un après l'autre : toujours courte, même à 80 objets. */
private const val APPEAR_SPREAD_MS = 700L

/**
 * Une manche : les objets à trouver en haut, les autres éparpillés dessous. **On ne perd jamais** : un faux secoue la tête et pâlit (on risque une étoile),
 * il n'y a ni temps ni limite d'essais. La manche est tirée au hasard ; « Rejouer » en tire une nouvelle.
 * [onFinished] est appelé UNE fois, dès que le dernier objet est trouvé (le joueur est sauvegardé tout de suite), avec les étoiles, les erreurs
 * et le nombre d'objets trouvés ; [progress] est ce que le joueur avait avant la manche (pour le record et le déblocage).
 * La flèche bleue (et le retour d'Android) appelle [onExit] : l'accueil. Le bouton en grille de tuiles appelle [onMenu] : le choix de la difficulté.
 */
@Composable
fun FindPlayScreen(
    difficulty: FindDifficulty,
    progress: FindProgress,
    onFinished: (stars: Int, mistakes: Int, found: Int) -> Unit,
    onNextDifficulty: (FindDifficulty) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    /** Pour les tests : la graine de la première manche (les suivantes en sont déduites), pour qu'elles soient toujours les mêmes. */
    seed: Long? = null,
) {
    // la graine de la manche en cours survit à une rotation de l'écran
    var roundSeed by rememberSaveable(difficulty) { mutableStateOf(seed ?: Random.nextLong()) }
    BackHandler(onBack = onExit)
    key(difficulty, roundSeed) {
        Round(
            difficulty, progress, roundSeed, onFinished, onNextDifficulty, onMenu, onExit,
            onReplay = { roundSeed = if (seed != null) roundSeed + 1 else Random.nextLong() }, modifier,
        )
    }
}

@Composable
private fun Round(
    difficulty: FindDifficulty,
    progress: FindProgress,
    roundSeed: Long,
    onFinished: (Int, Int, Int) -> Unit,
    onNextDifficulty: (FindDifficulty) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
    onReplay: () -> Unit,
    modifier: Modifier,
) {
    val feedback = LocalFeedback.current
    val density = LocalDensity.current
    val finished by rememberUpdatedState(onFinished)
    val round = remember { FindRound.generate(difficulty, roundSeed) }
    val theme = remember { FindTheme.of(Math.floorMod(roundSeed, FindTheme.all.size.toLong()).toInt()) }
    val bestBefore = remember { progress.stars(difficulty) }
    val unlockedBefore = remember { FindDifficulty.entries.filter { progress.isUnlocked(it) }.toSet() }
    var play by remember { mutableStateOf(FindPlay(round)) }
    var touched by remember { mutableStateOf(false) }
    var lastTouch by remember { mutableIntStateOf(0) }
    var nudge by remember { mutableStateOf(false) }
    var showEnd by remember { mutableStateOf(false) }
    var area by remember { mutableStateOf(IntSize.Zero) }

    val objects = remember { round.ids.associateWith { FindObject.byId(it) ?: error("objet inconnu : $it") } }
    val spots = remember(area) {
        if (area.width == 0 || area.height == 0) emptyList()
        else FindLayout.arrange(
            round.ids, area.width.toFloat(), area.height.toFloat(), round.layoutSeed,
            maxSize = with(density) { MAX_OBJECT_DP.dp.toPx() }, rotate = difficulty.rotates, vary = difficulty.variesSizes,
        )
    }

    // l'aide : un objet à trouver se balance si l'enfant n'a rien touché depuis un moment
    LaunchedEffect(lastTouch, play.done) {
        nudge = false
        if (!play.done) {
            delay(difficulty.nudgeMs)
            nudge = true
        }
    }
    LaunchedEffect(play.done) {
        if (play.done) {
            delay(END_DELAY_MS)
            showEnd = true
        }
    }

    fun tapObject(id: String) {
        val t = play.tap(id)
        play = t.play
        if (t.outcome == FindOutcome.IGNORED) return
        touched = true
        lastTouch++
        when (t.outcome) {
            FindOutcome.WRONG -> feedback.puff()
            FindOutcome.FOUND -> if (t.play.done) {
                feedback.win()
                finished(t.play.stars, t.play.mistakes, round.targets.size)
            } else {
                feedback.pick(t.play.found.size - 1)
            }
            FindOutcome.IGNORED -> Unit
        }
    }

    Box(modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) { drawFindBackdrop(theme) }
        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 14.dp)) {
            // le haut : retour à l'accueil, difficulté et étoiles en jeu, choix de la difficulté, nouvelle manche
            Row(Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ExitArrow(onExit, diameter = 48.dp)
                Box(
                    Modifier.weight(1f).height(54.dp).background(Color.White.copy(alpha = 0.94f), RoundedCornerShape(27.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        PjText(difficulty.title, size = 19.sp, weight = FontWeight.Bold, color = Pj.Ink, maxLines = 1)
                        Stars(filled = play.stars, star = 16.dp, gap = 4.dp, raiseMiddle = true, modifier = Modifier.padding(top = 1.dp).testTag("potential-stars"))
                    }
                }
                RoundIconButton(IconKind.LEVELS, onClick = onMenu, modifier = Modifier.testTag("levels"), color = Pj.Slate, dark = Pj.SlateDark, diameter = 48.dp)
                RoundIconButton(IconKind.RESTART, onClick = onReplay, modifier = Modifier.testTag("restart"), diameter = 48.dp)
            }
            // le bandeau des objets à trouver : même place, même taille tout au long de la manche
            TargetsPanel(
                targets = round.targets.map { objects.getValue(it) }, found = play.found,
                hint = difficulty == FindDifficulty.EASY && !touched, Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally),
            )
            // la zone de jeu : tout l'espace restant
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .onSizeChanged { area = it }
                    .testTag("area"),
            ) {
                for ((i, spot) in spots.withIndex()) {
                    key(spot.id) {
                        Thing(
                            spot = spot, obj = objects.getValue(spot.id), index = i, count = spots.size,
                            wrong = spot.id in play.wrong, isTarget = spot.id in round.targets, foundThis = spot.id in play.found, done = play.done,
                            nudge = nudge && spot.id == play.missing.firstOrNull(),
                            onTap = { tapObject(spot.id) },
                        )
                    }
                }
            }
        }
        if (showEnd) {
            EndPanel(
                difficulty, round.targets.map { objects.getValue(it) }, play.stars, play.mistakes, bestBefore, unlockedBefore,
                onReplay, onMenu, onNextDifficulty,
            )
        }
    }
}

/** Le bandeau des objets à trouver : une carte blanche toujours au même endroit. Un seul objet : grand, avec son nom ; plusieurs : une rangée de pastilles qui se cochent. */
@Composable
private fun TargetsPanel(targets: List<FindObject>, found: Set<String>, hint: Boolean, modifier: Modifier = Modifier) {
    val remaining = targets.count { it.id !in found }
    val description = "À retrouver : " + targets.joinToString(", ") { it.label + if (it.id in found) " (trouvé)" else "" }
    Panel(
        modifier.widthIn(max = 360.dp).fillMaxWidth().semantics(mergeDescendants = true) { contentDescription = description }.testTag("model"),
        corner = 30.dp, ledge = 6.dp, contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        if (targets.size == 1) SingleTarget(targets.first(), hint) else MultiTargets(targets, found, remaining)
    }
}

/** Un seul objet à trouver : grand, qui brille doucement, avec son nom. */
@Composable
private fun SingleTarget(obj: FindObject, hint: Boolean) {
    val pulse by rememberInfiniteTransition(label = "halo").animateFloat(
        initialValue = 0.9f, targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "halo",
    )
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Canvas(Modifier.size(96.dp)) {
            val r = size.minDimension / 2
            drawCircle(Brush.radialGradient(listOf(Pj.SunLight, Pj.SunLight.copy(alpha = 0f)), center, r * pulse), r * pulse, center)
            translate(size.width / 2, size.height / 2) { drawFindObject(obj, size.minDimension * 0.8f) }
        }
        Column(Modifier.padding(start = 12.dp).weight(1f, fill = false)) {
            PjText(
                if (hint) "Touche le même objet :" else "Trouve", Modifier.then(if (hint) Modifier.testTag("caption") else Modifier),
                size = 16.sp, weight = FontWeight.Medium, color = Pj.InkSoft, maxLines = 1,
            )
            PjText(obj.label, size = 26.sp, weight = FontWeight.Bold, color = Pj.Ink, maxLines = 2)
        }
    }
}

/** Plusieurs objets à trouver : une pastille par objet (un rond vert avec une coche quand il est trouvé) et combien il en reste. */
@Composable
private fun MultiTargets(targets: List<FindObject>, found: Set<String>, remaining: Int) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        BoxWithConstraints(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val gap = 8.dp
            val tile: Dp = minOf(66.dp, (maxWidth - gap * (targets.size - 1)) / targets.size)
            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                for (o in targets) TargetTile(o, o.id in found, tile)
            }
        }
        PjText(
            when {
                remaining == 0 -> "Tout est trouvé !"
                remaining == targets.size -> "Trouve-les tous !"
                else -> "Encore $remaining à trouver"
            },
            Modifier.padding(top = 2.dp).testTag("caption"), size = 16.sp, weight = FontWeight.SemiBold, color = Pj.InkSoft, maxLines = 1,
        )
    }
}

@Composable
private fun TargetTile(obj: FindObject, found: Boolean, size: Dp) {
    Box(
        Modifier
            .size(size)
            .testTag("tile-${obj.id}")
            .background(if (found) Color(0xFFD8F5DD) else Color(0xFFF1F3FA), RoundedCornerShape(18.dp)),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            translate(this.size.width / 2, this.size.height / 2) { drawFindObject(obj, this.size.minDimension * 0.82f) }
            if (found) drawCheckBadge(Offset(this.size.width * 0.76f, this.size.height * 0.76f), this.size.minDimension * 0.22f)
        }
    }
}

/** Un rond vert à bord blanc avec une coche blanche : « trouvé ». */
private fun DrawScope.drawCheckBadge(center: Offset, r: Float) {
    drawCircle(Color.White, r * 1.18f, center)
    drawCircle(Pj.Green, r, center)
    val tick = Path().apply {
        moveTo(center.x - r * 0.5f, center.y + r * 0.02f)
        lineTo(center.x - r * 0.12f, center.y + r * 0.4f)
        lineTo(center.x + r * 0.52f, center.y - r * 0.38f)
    }
    drawPath(tick, Color.White, style = Stroke(r * 0.3f, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

/** Le balancement d'un objet à trouver quand on aide ; 0 le reste du temps (aucune animation tant que l'aide n'est pas là). */
@Composable
private fun wiggleAngle(active: Boolean): Float {
    if (!active) return 0f
    val a by rememberInfiniteTransition(label = "wiggle").animateFloat(
        initialValue = -9f, targetValue = 9f, animationSpec = infiniteRepeatable(tween(320), RepeatMode.Reverse), label = "wiggle",
    )
    return a
}

/**
 * Un objet de la zone de jeu : il apparaît en rebondissant, secoue la tête et pâlit si c'est un faux, saute de joie (avec un halo et une coche) si on l'a trouvé.
 * La zone de toucher est le cercle de la mise en page, toujours aussi large même pour un petit objet.
 */
@Composable
private fun Thing(
    spot: FindSpot, obj: FindObject, index: Int, count: Int,
    wrong: Boolean, isTarget: Boolean, foundThis: Boolean, done: Boolean, nudge: Boolean, onTap: () -> Unit,
) {
    val density = LocalDensity.current
    val r = spot.hitRadius
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * APPEAR_SPREAD_MS / count)
        shown = true
    }
    val appear by animateFloatAsState(if (shown) 1f else 0f, spring(dampingRatio = 0.5f, stiffness = 320f), label = "appear")
    val joy by animateFloatAsState(if (foundThis) 1f else 0f, spring(dampingRatio = 0.35f, stiffness = 300f), label = "joy")
    val fade by animateFloatAsState(if (wrong) 0.38f else if (done && !isTarget) 0.45f else 1f, tween(300), label = "fade")
    val shake = remember { Animatable(0f) }
    LaunchedEffect(wrong) {
        if (wrong) {
            repeat(3) {
                shake.animateTo(11f, tween(55))
                shake.animateTo(-11f, tween(55))
            }
            shake.animateTo(0f, tween(55))
        }
    }
    val wiggle = wiggleAngle(nudge && !foundThis)
    Box(
        Modifier
            .offset { IntOffset((spot.cx - r).roundToInt(), (spot.cy - r).roundToInt()) }
            .size(with(density) { (r * 2).toDp() })
            .testTag("obj-${obj.id}")
            .semantics {
                contentDescription = obj.label
                role = Role.Button
                stateDescription = if (wrong) "pas le bon" else if (foundThis) "trouvé" else "à toucher"
            }
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, enabled = !wrong && !foundThis && !done, onClick = onTap),
    ) {
        // le halo d'un objet trouvé, derrière lui, droit
        if (foundThis) {
            Canvas(Modifier.fillMaxSize().graphicsLayer { alpha = joy.coerceIn(0f, 1f) }) {
                drawCircle(Color.White.copy(alpha = 0.55f), spot.size * 0.64f, center)
                drawCircle(Pj.Green, spot.size * 0.64f, center, style = Stroke(maxOf(2.dp.toPx(), spot.size * 0.05f)))
            }
        }
        Canvas(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = appear.coerceIn(0f, 1f) * fade
                    val s = appear * (1f + 0.2f * joy)
                    scaleX = s
                    scaleY = s
                    rotationZ = spot.angle + shake.value + wiggle
                    translationY = -joy * spot.size * 0.06f
                },
        ) {
            val s = spot.size
            translate(size.width / 2, size.height / 2) { drawFindObject(obj, s) }
        }
        if (foundThis) {
            Canvas(Modifier.fillMaxSize().graphicsLayer { alpha = joy.coerceIn(0f, 1f) }) {
                drawCheckBadge(center + Offset(spot.size * 0.36f, -spot.size * 0.36f), maxOf(spot.size * 0.2f, 8.dp.toPx()))
            }
        }
    }
}

/** Le bilan, en bas de l'écran : la scène et les objets trouvés restent visibles au-dessus. */
@Composable
private fun EndPanel(
    d: FindDifficulty, targets: List<FindObject>, stars: Int, mistakes: Int, bestBefore: Int, unlockedBefore: Set<FindDifficulty>,
    onReplay: () -> Unit, onMenu: () -> Unit, onNext: (FindDifficulty) -> Unit,
) {
    val feedback = LocalFeedback.current
    val next = d.next
    // la difficulté suivante vient de s'ouvrir avec cette manche
    val justUnlocked = next != null && stars >= 2 && next !in unlockedBefore
    var shown by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        for (i in 0 until stars) {
            delay(420)
            shown = i + 1
            feedback.star(i)
        }
    }
    val record = stars > bestBefore && bestBefore > 0
    Box(Modifier.fillMaxSize().background(Color(0xFF1B1D2E).copy(alpha = 0.28f)).testTag("end")) {
        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), contentAlignment = Alignment.BottomCenter) {
            Panel(
                Modifier.padding(horizontal = 24.dp).padding(bottom = 18.dp).widthIn(max = 460.dp).fillMaxWidth().consumeTaps(),
                corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            ) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    PjText(if (record) "Nouveau record !" else "Bravo !", size = 38.sp, weight = FontWeight.Bold, color = if (record) Pj.StarDark else Color(0xFFFF7A1A), align = TextAlign.Center)
                    Stars(filled = shown, star = 32.dp, gap = 6.dp, raiseMiddle = true, modifier = Modifier.padding(top = 4.dp).testTag("final-stars"))
                    PjText(
                        if (targets.size == 1) "Tu as trouvé ${targets.first().label} !" else "Tu as trouvé les ${targets.size} objets !",
                        Modifier.padding(top = 6.dp), size = 19.sp, weight = FontWeight.SemiBold, align = TextAlign.Center,
                    )
                    PjText(
                        if (mistakes == 0) "Sans aucune erreur !" else if (mistakes == 1) "1 erreur" else "$mistakes erreurs",
                        Modifier.testTag("final-mistakes"), size = 16.sp, weight = FontWeight.Medium, color = if (mistakes == 0) Pj.GreenDark else Pj.InkSoft, align = TextAlign.Center,
                    )
                    if (justUnlocked) {
                        Box(
                            Modifier.padding(top = 8.dp).fillMaxWidth().background(Color(0xFFE5F7E8), RoundedCornerShape(20.dp)).padding(horizontal = 12.dp, vertical = 8.dp).testTag("unlocked"),
                            contentAlignment = Alignment.Center,
                        ) {
                            PjText("Nouvelle difficulté débloquée : « ${next.title} » !", size = 16.sp, weight = FontWeight.Bold, color = Pj.GreenDark, align = TextAlign.Center)
                        }
                        ChunkyButton("Essayer « ${next.title} »", { onNext(next) }, Modifier.padding(top = 10.dp).fillMaxWidth(), height = 54.dp, fontSize = 21.sp)
                        Row(Modifier.padding(top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ChunkyButton("Rejouer", onReplay, Modifier.weight(1f), color = Pj.Orange, dark = Pj.OrangeDark, height = 46.dp, fontSize = 18.sp)
                            ChunkyButton("Difficultés", onMenu, Modifier.weight(1f), color = Pj.Slate, dark = Pj.SlateDark, height = 46.dp, fontSize = 18.sp)
                        }
                    } else {
                        ChunkyButton("Rejouer", onReplay, Modifier.padding(top = 10.dp).fillMaxWidth(), height = 54.dp, fontSize = 24.sp)
                        Row(Modifier.padding(top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (next != null && stars >= 2) ChunkyButton("Difficulté suivante", { onNext(next) }, Modifier.weight(1f), color = Pj.Orange, dark = Pj.OrangeDark, height = 46.dp, fontSize = 16.sp)
                            ChunkyButton("Difficultés", onMenu, Modifier.weight(1f), color = Pj.Slate, dark = Pj.SlateDark, height = 46.dp, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}
