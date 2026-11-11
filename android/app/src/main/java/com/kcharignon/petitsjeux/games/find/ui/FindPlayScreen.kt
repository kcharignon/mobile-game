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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.kcharignon.petitsjeux.games.find.engine.FindLayout
import com.kcharignon.petitsjeux.games.find.engine.FindLevel
import com.kcharignon.petitsjeux.games.find.engine.FindOutcome
import com.kcharignon.petitsjeux.games.find.engine.FindPlay
import com.kcharignon.petitsjeux.games.find.engine.FindSpot
import com.kcharignon.petitsjeux.games.find.engine.FindWorlds
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/** Sans rien toucher pendant ce temps (ms), le bon objet se balance doucement : une aide discrète, sans bouton ni pénalité. */
const val NUDGE_AFTER_MS = 8000L

/** Le temps (ms) entre le bon objet trouvé (il saute de joie) et la fenêtre de bilan. */
const val END_DELAY_MS = 900L

/** Le plus gros qu'un objet peut être, en dp (pour ne pas avoir de géants quand il y en a peu). */
private const val MAX_OBJECT_DP = 140

/**
 * Un niveau : le modèle en haut, les objets éparpillés dessous. **On ne perd jamais** : un mauvais objet secoue la tête et pâlit (une étoile de moins), il n'y a ni temps ni limite d'essais.
 * [onFinished] est appelé UNE fois, dès que le bon objet est touché (le joueur est sauvegardé tout de suite), avec les étoiles et le nombre d'erreurs.
 * La flèche bleue (et le retour d'Android) appelle [onExit] : l'accueil. Le bouton en grille de tuiles appelle [onLevels] : le choix du niveau.
 */
@Composable
fun FindPlayScreen(
    level: FindLevel,
    progress: FindProgress,
    onFinished: (stars: Int, mistakes: Int) -> Unit,
    onNext: (Int) -> Unit,
    onLevels: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var attempt by remember(level.number) { mutableIntStateOf(0) }
    BackHandler(onBack = onExit)
    key(level.number, attempt) {
        Round(level, progress, attempt, onFinished, onNext, onLevels, onExit, onReplay = { attempt++ }, modifier)
    }
}

@Composable
private fun Round(
    level: FindLevel,
    progress: FindProgress,
    attempt: Int,
    onFinished: (Int, Int) -> Unit,
    onNext: (Int) -> Unit,
    onLevels: () -> Unit,
    onExit: () -> Unit,
    onReplay: () -> Unit,
    modifier: Modifier,
) {
    val feedback = LocalFeedback.current
    val density = LocalDensity.current
    val finished by rememberUpdatedState(onFinished)
    val theme = remember(level.world) { FindTheme.of(level.world) }
    val bestBefore = remember { progress.starsOf(level.number) }
    var play by remember { mutableStateOf(FindPlay(level)) }
    var touched by remember { mutableStateOf(false) }
    var lastTouch by remember { mutableIntStateOf(0) }
    var nudge by remember { mutableStateOf(false) }
    var showEnd by remember { mutableStateOf(false) }
    var area by remember { mutableStateOf(IntSize.Zero) }

    val objects = remember(level) { level.ids.associateWith { FindObject.byId(it) ?: error("objet inconnu : $it") } }
    val spots = remember(area) {
        if (area.width == 0 || area.height == 0) emptyList()
        else FindLayout.arrange(level.ids, area.width.toFloat(), area.height.toFloat(), level.seed + attempt, maxSize = with(density) { MAX_OBJECT_DP.dp.toPx() })
    }

    // l'aide : le bon objet se balance si l'enfant n'a rien touché depuis un moment
    LaunchedEffect(lastTouch, play.found) {
        nudge = false
        if (!play.found) {
            delay(NUDGE_AFTER_MS)
            nudge = true
        }
    }
    LaunchedEffect(play.found) {
        if (play.found) {
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
            FindOutcome.FOUND -> {
                feedback.win()
                finished(t.play.stars, t.play.mistakes)
            }
            FindOutcome.IGNORED -> Unit
        }
    }

    Box(modifier.fillMaxSize()) {
        androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) { drawFindBackdrop(theme) }
        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 14.dp)) {
            // le haut : retour à l'accueil, niveau et monde, choix du niveau, recommencer
            Row(Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ExitArrow(onExit, diameter = 48.dp)
                Box(
                    Modifier.weight(1f).height(54.dp).background(Color.White.copy(alpha = 0.94f), RoundedCornerShape(27.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        PjText("Niveau ${level.number}", size = 20.sp, weight = FontWeight.Bold, color = Pj.Ink, maxLines = 1)
                        PjText(FindWorlds.names[level.world], size = 13.sp, weight = FontWeight.Medium, color = Pj.InkSoft, maxLines = 1)
                    }
                }
                RoundIconButton(IconKind.LEVELS, onClick = onLevels, modifier = Modifier.testTag("levels"), color = Pj.Slate, dark = Pj.SlateDark, diameter = 48.dp)
                RoundIconButton(IconKind.RESTART, onClick = onReplay, modifier = Modifier.testTag("restart"), diameter = 48.dp)
            }
            Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.Center) {
                Stars(filled = play.stars, star = 22.dp, gap = 5.dp, raiseMiddle = true, modifier = Modifier.testTag("potential-stars"))
            }
            // au niveau 1, tant qu'on n'a rien touché, la carte explique le jeu (même place, même taille : rien ne bouge quand elle change)
            ModelCard(objects.getValue(level.target), hint = level.number == 1 && !touched, Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally))
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
                            spot = spot, obj = objects.getValue(spot.id), index = i,
                            wrong = spot.id in play.wrong, isTarget = spot.id == level.target, found = play.found, nudge = nudge,
                            onTap = { tapObject(spot.id) },
                        )
                    }
                }
            }
        }
        if (showEnd) EndPanel(level, objects.getValue(level.target), play.stars, play.mistakes, bestBefore, onReplay, onLevels, onNext)
    }
}

/** Le modèle à retrouver : une carte blanche, toujours au même endroit, avec son objet qui brille doucement et son nom. */
@Composable
private fun ModelCard(obj: FindObject, hint: Boolean, modifier: Modifier = Modifier) {
    val pulse by rememberInfiniteTransition(label = "halo").animateFloat(
        initialValue = 0.9f, targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "halo",
    )
    Panel(
        modifier.widthIn(max = 360.dp).fillMaxWidth().semantics(mergeDescendants = true) { contentDescription = "Modèle à retrouver : ${obj.label}" }.testTag("model"),
        corner = 30.dp, ledge = 6.dp, contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Canvas(Modifier.size(104.dp)) {
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
}

/** Le balancement du bon objet quand on l'aide ; 0 le reste du temps (aucune animation tant que l'aide n'est pas là). */
@Composable
private fun wiggleAngle(active: Boolean): Float {
    if (!active) return 0f
    val a by rememberInfiniteTransition(label = "wiggle").animateFloat(
        initialValue = -9f, targetValue = 9f, animationSpec = infiniteRepeatable(tween(320), RepeatMode.Reverse), label = "wiggle",
    )
    return a
}

/** Un objet de la zone de jeu : il apparaît en rebondissant, secoue la tête et pâlit si ce n'est pas le bon, saute de joie si c'est lui. */
@Composable
private fun Thing(spot: FindSpot, obj: FindObject, index: Int, wrong: Boolean, isTarget: Boolean, found: Boolean, nudge: Boolean, onTap: () -> Unit) {
    val density = LocalDensity.current
    val r = spot.hitRadius
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 45L)
        shown = true
    }
    val appear by animateFloatAsState(if (shown) 1f else 0f, spring(dampingRatio = 0.5f, stiffness = 320f), label = "appear")
    val joy by animateFloatAsState(if (found && isTarget) 1f else 0f, spring(dampingRatio = 0.35f, stiffness = 300f), label = "joy")
    val fade by animateFloatAsState(if (wrong) 0.38f else if (found && !isTarget) 0.45f else 1f, tween(300), label = "fade")
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
    val wiggle = wiggleAngle(nudge && isTarget && !found)
    Box(
        Modifier
            .offset { IntOffset((spot.cx - r).roundToInt(), (spot.cy - r).roundToInt()) }
            .size(with(density) { (r * 2).toDp() })
            .testTag("obj-${obj.id}")
            .semantics {
                contentDescription = obj.label
                role = Role.Button
                stateDescription = if (wrong) "pas le bon" else "à toucher"
            }
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, enabled = !wrong && !found, onClick = onTap),
    ) {
        Canvas(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = appear.coerceIn(0f, 1f) * fade
                    val s = appear * (1f + 0.28f * joy)
                    scaleX = s
                    scaleY = s
                    rotationZ = spot.angle + shake.value + wiggle
                    translationY = -joy * spot.size * 0.1f
                },
        ) {
            val s = spot.size
            drawOval(Color(0xFF1B1D2E).copy(alpha = 0.16f), Offset(size.width / 2 - s * 0.34f, size.height / 2 + s * 0.4f), Size(s * 0.68f, s * 0.13f))
            translate(size.width / 2, size.height / 2) { drawFindObject(obj, s) }
        }
    }
}

/** Le bilan, en bas de l'écran : la scène et l'objet trouvé restent visibles au-dessus. */
@Composable
private fun EndPanel(
    level: FindLevel, obj: FindObject, stars: Int, mistakes: Int, bestBefore: Int,
    onReplay: () -> Unit, onLevels: () -> Unit, onNext: (Int) -> Unit,
) {
    val feedback = LocalFeedback.current
    val next = level.number + 1
    val hasNext = next <= FindWorlds.LEVEL_COUNT
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
                corner = 36.dp, ledge = 8.dp, contentPadding = PaddingValues(horizontal = 24.dp, vertical = 18.dp),
            ) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    PjText(if (record) "Nouveau record !" else "Bravo !", size = 40.sp, weight = FontWeight.Bold, color = if (record) Pj.StarDark else Color(0xFFFF7A1A), align = TextAlign.Center)
                    Stars(filled = shown, star = 34.dp, gap = 6.dp, raiseMiddle = true, modifier = Modifier.padding(top = 4.dp).testTag("final-stars"))
                    PjText("Tu as trouvé ${obj.label} !", Modifier.padding(top = 6.dp), size = 20.sp, weight = FontWeight.SemiBold, align = TextAlign.Center)
                    PjText(
                        if (mistakes == 0) "Du premier coup !" else if (mistakes == 1) "1 erreur" else "$mistakes erreurs",
                        Modifier.testTag("final-mistakes"), size = 16.sp, weight = FontWeight.Medium, color = if (mistakes == 0) Pj.GreenDark else Pj.InkSoft, align = TextAlign.Center,
                    )
                    if (hasNext) ChunkyButton("Niveau suivant", { onNext(next) }, Modifier.padding(top = 10.dp).fillMaxWidth(), height = 54.dp, fontSize = 24.sp)
                    Row(Modifier.padding(top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ChunkyButton("Rejouer", onReplay, Modifier.weight(1f), color = Pj.Blue, dark = Pj.BlueDark, height = 44.dp, fontSize = 18.sp)
                        ChunkyButton("Niveaux", onLevels, Modifier.weight(1f), color = Pj.Slate, dark = Pj.SlateDark, height = 44.dp, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
