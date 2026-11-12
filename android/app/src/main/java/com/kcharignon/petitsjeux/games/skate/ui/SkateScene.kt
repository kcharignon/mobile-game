package com.kcharignon.petitsjeux.games.skate.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import com.kcharignon.petitsjeux.core.ui.drawStar
import com.kcharignon.petitsjeux.games.skate.engine.Piece
import com.kcharignon.petitsjeux.games.skate.engine.PieceKind
import com.kcharignon.petitsjeux.games.skate.engine.SkateRun
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * La caméra de Super Skate : on regarde la rue de derrière le skateur, de plus haut. Un point à la distance [rel] devant le skateur (en mètres) et au travers [x] (unités de rue)
 * tombe à l'écran en `(sx(x, rel), y(rel))` ; [s] est l'échelle (1 au niveau du skateur, vers 0 à l'horizon) : c'est elle qui fait grossir les choses en approchant.
 */
internal class Cam(val width: Float, val height: Float) {
    val horizon = height * 0.27f
    val ground = height * 0.8f
    private val cameraBack = 7.5f

    /** Pixels par unité de rue au niveau du skateur. */
    val unit = width * 0.46f

    fun s(rel: Float): Float = cameraBack / (max(rel, -cameraBack * 0.6f) + cameraBack)
    fun y(rel: Float): Float = horizon + (ground - horizon) * s(rel)
    fun sx(x: Float, rel: Float): Float = width / 2f + x * unit * s(rel)
    fun u(rel: Float): Float = unit * s(rel)

    companion object {
        /** Jusqu'où on voit (m) : plus loin, c'est le brouillard de l'horizon. */
        const val VIEW = 75f
    }
}

private const val BAND = 4f
private const val DECOR_SPACING = 9f

private fun hash(a: Int, b: Int): Int = ((a * 73856093) xor (b * 19349663) xor 0x5bd1e995) and 0x7fffffff

/** Une chose à dessiner, avec sa distance : on dessine du plus loin au plus près. */
private class Item(val rel: Float, val draw: DrawScope.() -> Unit)

/**
 * Toute la scène d'une course : ciel, rue et bas-côtés qui défilent, décor, obstacles, rampes, arche d'arrivée et skateur, puis les effets du choc.
 * Ne dépend que de l'état de [run] (rien n'est retenu d'une image à l'autre) : la même course au même instant donne la même image.
 */
internal fun DrawScope.drawSkateScene(run: SkateRun, theme: SkateTheme, helmet: Color, steerHint: Boolean = false) {
    val cam = Cam(size.width, size.height)
    val dist = run.dist
    val time = run.time

    // la secousse du choc : tout l'écran tremble un instant
    // (un léger zoom l'accompagne : la secousse ne laisse jamais voir le bord de l'image)
    val shake = if (run.sinceHit < 0.45f) 1f - run.sinceHit / 0.45f else 0f
    val shakeAmp = shake * 9f * density
    withTransform({
        scale(1f + 0.06f * shake, 1f + 0.06f * shake, Offset(size.width / 2f, size.height / 2f))
        translate(sin(time * 80f) * shakeAmp, cos(time * 63f) * shakeAmp * 0.7f)
    }) {
        drawSky(cam, theme)
        drawGround(cam, theme, dist)

        val items = ArrayList<Item>(64)
        // le décor des deux côtés
        val firstDecor = floor((dist - 4f) / DECOR_SPACING).toInt()
        val lastDecor = floor((dist + Cam.VIEW) / DECOR_SPACING).toInt()
        for (k in firstDecor..lastDecor) for (side in intArrayOf(-1, 1)) {
            val h = hash(k, side + theme.name.length)
            val kind = theme.decor[h % theme.decor.size]
            val spread = (h / 7) % 5 * 0.12f
            val x = side * (if (kind == DecorKind.BUILDING) 2.15f + spread else 1.5f + spread)
            val rel = k * DECOR_SPACING + (h / 31) % 3 - dist
            if (rel < -3f) continue
            items += Item(rel) {
                val u = cam.u(rel)
                drawDecor(kind, h / 13, cam.sx(x, rel), cam.y(rel), u, theme.night)
            }
        }
        // les pièces de la rue
        for (piece in run.world.pieces) {
            val rel = piece.z - dist
            if (rel > Cam.VIEW) break
            val knock = run.knocked[piece.id]
            if (piece.kind == PieceKind.RAMP) {
                if (rel + PieceKind.RAMP.depth < -1.8f) continue
                items += Item(rel + PieceKind.RAMP.depth) { drawRamp(cam, piece, rel, theme) }
                continue
            }
            if (rel < -1.8f) continue
            items += Item(rel) { drawStreetPiece(cam, piece, rel, knock, time, theme) }
        }
        // l'arche d'arrivée
        val finishRel = run.world.length - dist
        if (finishRel in -1.8f..Cam.VIEW) items += Item(finishRel) { drawFinishArch(cam, finishRel) }
        // le skateur est au niveau 0
        items += Item(0f) { drawTheSkater(cam, run, helmet) }
        // tant que le doigt n'a pas glissé : deux flèches qui montrent le geste
        if (steerHint) items += Item(-0.01f) { drawSteerHint(cam, run) }

        items.sortByDescending { it.rel }
        for (item in items) item.draw(this)

        drawFog(cam, theme)
        drawImpactEffects(cam, run)
    }
    drawHitFlash(run)
}

// ------------------------------------------------------------------ le ciel et le sol

private fun DrawScope.drawSky(cam: Cam, theme: SkateTheme) {
    drawRect(Brush.verticalGradient(listOf(theme.skyTop, theme.skyBottom), startY = 0f, endY = cam.horizon), Offset.Zero, Size(size.width, cam.horizon + 1f))
    val w = size.width
    theme.sun?.let { sun ->
        val c = Offset(w * 0.74f, cam.horizon * 0.5f)
        drawCircle(sun.copy(alpha = 0.25f), w * 0.11f, c)
        drawCircle(sun, w * 0.06f, c)
    }
    if (theme.night) {
        // des étoiles, toujours les mêmes
        for (i in 0 until 26) {
            val h = hash(i, 17)
            val x = (h % 1000) / 1000f * w
            val y = ((h / 1000) % 1000) / 1000f * cam.horizon * 0.8f
            drawCircle(Color.White.copy(alpha = 0.5f + 0.4f * ((h / 3) % 3) / 2f), (0.8f + (h % 3) * 0.5f) * density, Offset(x, y))
        }
    } else {
        val cloud = Color.White.copy(alpha = 0.85f)
        for ((cx, cy, r) in listOf(Triple(0.2f, 0.28f, 0.05f), Triple(0.55f, 0.16f, 0.04f), Triple(0.9f, 0.4f, 0.045f))) {
            val c = Offset(w * cx, cam.horizon * cy * 2f)
            drawCircle(cloud, w * r, c)
            drawCircle(cloud, w * r * 0.8f, c + Offset(w * r * 1.1f, w * r * 0.15f))
            drawCircle(cloud, w * r * 0.7f, c + Offset(-w * r * 1.0f, w * r * 0.2f))
        }
    }
    // la silhouette lointaine : immeubles, collines ou mer
    when {
        theme.sea != null -> {
            drawRect(theme.sea, Offset(0f, cam.horizon - cam.height * 0.022f), Size(w, cam.height * 0.024f))
            drawRect(Color.White.copy(alpha = 0.35f), Offset(0f, cam.horizon - cam.height * 0.022f), Size(w, 2f * density))
        }
        theme.name == "Parc" -> for (i in 0 until 5) drawCircle(theme.far, w * (0.16f + (i % 3) * 0.05f), Offset(w * (i * 0.27f), cam.horizon + w * 0.1f))
        else -> {
            val n = 13
            for (i in 0 until n) {
                val h = hash(i, 3)
                val bw = w / n
                val bh = cam.height * (0.025f + (h % 5) * 0.011f)
                drawRect(theme.far, Offset(i * bw, cam.horizon - bh), Size(bw + 1f, bh + 1f))
                if (theme.night) for (r in 0 until 3) if ((h / (r + 2)) % 2 == 0) drawRect(Color(0xFFFFE08A).copy(alpha = 0.8f), Offset(i * bw + bw * 0.3f, cam.horizon - bh + 4f * density + r * 7f * density), Size(bw * 0.14f, 3f * density))
            }
        }
    }
}

private fun DrawScope.drawGround(cam: Cam, theme: SkateTheme, dist: Float) {
    val w = size.width
    drawRect(theme.side, Offset(0f, cam.horizon), Size(w, size.height - cam.horizon))
    val nearRel = -3f
    val farRel = Cam.VIEW
    val first = floor((dist + nearRel) / BAND).toInt()
    val last = floor((dist + farRel) / BAND).toInt()
    for (k in last downTo first) {
        val rel0 = max(k * BAND - dist, nearRel)
        val rel1 = min(k * BAND + BAND - dist, farRel)
        if (rel1 <= rel0) continue
        val y0 = cam.y(rel0)
        val y1 = cam.y(rel1)
        val alt = k % 2 == 0
        drawRect(if (alt) theme.sideAlt else theme.side, Offset(0f, y1), Size(w, y0 - y1 + 1.5f))
        // la chaussée
        val road = Path().apply {
            moveTo(cam.sx(-1f, rel0), y0)
            lineTo(cam.sx(1f, rel0), y0)
            lineTo(cam.sx(1f, rel1), y1)
            lineTo(cam.sx(-1f, rel1), y1)
            close()
        }
        drawPath(road, if (alt) theme.roadAlt else theme.road)
        // les tirets du milieu : deux mètres sur quatre
        if (alt) {
            val d0 = k * BAND - dist + 0.6f
            val d1 = d0 + 2f
            if (d1 > nearRel && d0 < farRel) {
                val a = max(d0, nearRel)
                val b = min(d1, farRel)
                quad(cam, -0.014f, 0.014f, a, b, theme.line.copy(alpha = 0.85f))
            }
        }
    }
    // les bordures de la rue : un trottoir clair de chaque côté, et une ligne continue
    quad(cam, -1.08f, -1f, nearRel, farRel, theme.curb)
    quad(cam, 1f, 1.08f, nearRel, farRel, theme.curb)
    quad(cam, -0.95f, -0.935f, nearRel, farRel, theme.line.copy(alpha = 0.6f))
    quad(cam, 0.935f, 0.95f, nearRel, farRel, theme.line.copy(alpha = 0.6f))
}

/** Un bandeau de la rue : de x0 à x1 (unités de rue), de la distance rel0 à rel1. */
private fun DrawScope.quad(cam: Cam, x0: Float, x1: Float, rel0: Float, rel1: Float, color: Color) {
    val p = Path().apply {
        moveTo(cam.sx(x0, rel0), cam.y(rel0))
        lineTo(cam.sx(x1, rel0), cam.y(rel0))
        lineTo(cam.sx(x1, rel1), cam.y(rel1))
        lineTo(cam.sx(x0, rel1), cam.y(rel1))
        close()
    }
    drawPath(p, color)
}

/** La brume de l'horizon : les choses lointaines s'y fondent. */
private fun DrawScope.drawFog(cam: Cam, theme: SkateTheme) {
    val top = cam.horizon - cam.height * 0.05f
    val bottom = cam.y(26f)
    val mid = (cam.horizon - top) / (bottom - top)
    drawRect(
        Brush.verticalGradient(0f to theme.fog.copy(alpha = 0f), mid to theme.fog.copy(alpha = 0.92f), 1f to theme.fog.copy(alpha = 0f), startY = top, endY = bottom),
        Offset(0f, top), Size(size.width, bottom - top),
    )
}

// ------------------------------------------------------------------ les pièces

private fun DrawScope.drawStreetPiece(cam: Cam, piece: Piece, rel: Float, knock: SkateRun.Knock?, time: Float, theme: SkateTheme) {
    val u = cam.u(rel)
    var x = piece.xAt(rel)
    var bx: Float
    val by = cam.y(rel)
    var lift = 0f
    var tilt = 0f
    var alpha = 1f
    if (knock != null) {
        // projeté sur le côté en tournant, avec un petit bond, puis il s'efface
        val t = knock.age
        x += knock.direction * t * 2.6f
        lift = 0.5f * sin(PI.toFloat() * min(t / 0.7f, 1f))
        tilt = knock.direction * min(t * 420f, 220f)
        alpha = (1f - t / 0.9f).coerceIn(0f, 1f)
    }
    // une fois dépassée, la chose s'efface avant de devenir énorme près de la caméra
    if (rel < -0.6f) alpha *= (1f - (-rel - 0.6f) / 1.2f).coerceIn(0f, 1f)
    if (alpha <= 0f) return
    bx = cam.sx(x, rel)
    val half = piece.kind.shadowHalfWidth()
    val bounds = Rect(bx - u * 1.2f, by - u * 2f, bx + u * 1.2f, by + u * 0.4f)
    withAlpha(alpha, bounds) {
        drawGroundShadow(bx, by, u, half, lift)
        rotate(tilt, Offset(bx, by - u * 0.3f)) {
            val y = by - lift * u
            if (piece.kind == PieceKind.PEDESTRIAN) {
                val walking = piece.crosses && rel < piece.walkStart && rel > piece.walkEnd
                val swing = if (walking) sin(time * 11f + piece.id) else 0f
                // à l'approche du skateur : la bouche s'arrondit
                val surprise = ((14f - rel) / 10f).coerceIn(0f, 1f)
                val bob = if (!walking) 1f + 0.012f * sin(time * 3f + piece.id) else 1f
                drawPedestrian(bx, y, u * bob, piece.variant, swing, if (knock != null) 1f else surprise)
            } else {
                drawPiece(piece.kind, piece.variant, bx, y, u, time, theme.night)
            }
        }
    }
}

private fun DrawScope.drawRamp(cam: Cam, piece: Piece, relNear: Float, theme: SkateTheme) {
    val relFar = relNear + PieceKind.RAMP.depth
    // dépassée, la rampe s'efface comme le reste avant de remplir l'écran
    val alpha = if (relFar < -0.6f) (1f - (-relFar - 0.6f) / 1.2f).coerceIn(0f, 1f) else 1f
    if (alpha <= 0f) return
    withAlpha(alpha, Rect(0f, 0f, size.width, size.height)) { drawRampBody(cam, piece, relNear, relFar) }
}

private fun DrawScope.drawRampBody(cam: Cam, piece: Piece, relNear: Float, relFar: Float) {
    val hw = piece.kind.width / 2f
    val h = SkateRun.RAMP_HEIGHT
    val yN = cam.y(relNear)
    val yFg = cam.y(relFar)
    val yF = yFg - h * cam.u(relFar)
    val xl0 = cam.sx(piece.x - hw, relNear)
    val xr0 = cam.sx(piece.x + hw, relNear)
    val xl1 = cam.sx(piece.x - hw, relFar)
    val xr1 = cam.sx(piece.x + hw, relFar)
    drawGroundShadow((xl0 + xr0) / 2f, yN, cam.u(relNear), hw * 1.05f, strength = 0.2f)
    val side = Color(0xFFD96A00)
    for ((x0, x1) in listOf(xl0 to xl1, xr0 to xr1)) {
        drawPath(Path().apply { moveTo(x0, yN); lineTo(x1, yFg); lineTo(x1, yF); close() }, side)
    }
    val top = Path().apply { moveTo(xl0, yN); lineTo(xr0, yN); lineTo(xr1, yF); lineTo(xl1, yF); close() }
    drawPath(top, Brush.verticalGradient(listOf(Color(0xFFFFB347), Color(0xFFFF8A1F)), startY = yF, endY = yN))
    // des chevrons jaunes qui montrent le chemin
    for (t in listOf(0.18f, 0.45f, 0.72f)) {
        fun point(tx: Float, tt: Float): Offset {
            val lx = lerp(xl0, xl1, tt)
            val rx = lerp(xr0, xr1, tt)
            return Offset(lerp(lx, rx, tx), lerp(yN, yF, tt))
        }
        val a = point(0.22f, t)
        val b = point(0.5f, t + 0.12f)
        val c = point(0.78f, t)
        val width = (xr0 - xl0) * lerp(1f, 0.6f, t) * 0.07f
        drawLine(Color(0xFFFFE066), a, b, strokeWidth = width, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        drawLine(Color(0xFFFFE066), c, b, strokeWidth = width, cap = androidx.compose.ui.graphics.StrokeCap.Round)
    }
    // la lèvre blanche en haut
    drawLine(Color.White.copy(alpha = 0.9f), Offset(xl1, yF), Offset(xr1, yF), strokeWidth = max(2f, cam.u(relFar) * 0.03f), cap = androidx.compose.ui.graphics.StrokeCap.Round)
}

private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t

/** L'arche d'arrivée : deux poteaux et une banderole à damier d'un bord à l'autre de la rue. */
private fun DrawScope.drawFinishArch(cam: Cam, rel: Float) {
    val u = cam.u(rel)
    val by = cam.y(rel)
    val left = cam.sx(-1.12f, rel)
    val right = cam.sx(1.12f, rel)
    val pole = Color(0xFFF4F1E4)
    val postW = max(3f, u * 0.07f)
    drawRect(pole, Offset(left - postW / 2, by - u * 1.55f), Size(postW, u * 1.55f))
    drawRect(pole, Offset(right - postW / 2, by - u * 1.55f), Size(postW, u * 1.55f))
    val bannerTop = by - u * 1.55f
    val bannerH = u * 0.3f
    val cols = 16
    val cw = (right - left) / cols
    for (c in 0 until cols) for (r in 0 until 2) {
        drawRect(if ((c + r) % 2 == 0) Color(0xFF2B2D42) else Color.White, Offset(left + c * cw, bannerTop + r * bannerH / 2f), Size(cw + 0.5f, bannerH / 2f + 0.5f))
    }
    drawRect(Color(0xFF2B2D42), Offset(left, bannerTop - max(1f, u * 0.015f)), Size(right - left, max(1f, u * 0.015f)))
    // la ligne d'arrivée au sol
    quad(cam, -1f, 1f, rel - 0.25f, rel + 0.25f, Color.White.copy(alpha = 0.9f))
}

// ------------------------------------------------------------------ le skateur

private fun DrawScope.drawTheSkater(cam: Cam, run: SkateRun, helmet: Color) {
    val u = cam.u(0f)
    val bx = cam.sx(run.x, 0f)
    val by = cam.y(0f)
    val height = run.height
    val lean = ((run.targetX - run.x) * 38f).coerceIn(-14f, 14f)
    val landing = if (run.sinceLand < 0.3f) sin(PI.toFloat() * run.sinceLand / 0.3f) else 0f
    // clignote après un choc : très pâle une fois sur deux (dix fois par seconde)
    val blink = if (run.hurt && ((run.hurtLeft * 10f).toInt() % 2 == 0)) 0.28f else 1f
    drawGroundShadow(bx, by, u, 0.17f, height)
    val bounds = Rect(bx - u, by - u * (1.2f + height), bx + u, by + u * 0.3f)
    withAlpha(blink, bounds) {
        drawSkater(bx, by - height * u, u, helmet, lean, run.airProgress, landing, run.time)
    }
}

// ------------------------------------------------------------------ les effets

/** Deux chevrons blancs qui s'écartent de part et d'autre du skateur : « glisse le doigt à gauche et à droite ». */
private fun DrawScope.drawSteerHint(cam: Cam, run: SkateRun) {
    val u = cam.u(0f)
    val y = cam.y(0f) - u * 0.34f
    val pulse = 0.5f + 0.5f * sin(run.time * 5f + 1f)
    val gap = u * (0.3f + 0.08f * pulse)
    val h = u * 0.1f
    for (side in intArrayOf(-1, 1)) {
        val x = cam.sx(run.x, 0f) + side * gap
        val path = Path().apply {
            moveTo(x, y - h)
            lineTo(x + side * h * 0.8f, y)
            lineTo(x, y + h)
        }
        val round = androidx.compose.ui.graphics.StrokeCap.Round
        drawPath(path, Color(0xFF2B2D42).copy(alpha = 0.55f), style = androidx.compose.ui.graphics.drawscope.Stroke(u * 0.075f, cap = round, join = androidx.compose.ui.graphics.StrokeJoin.Round))
        drawPath(path, Color.White.copy(alpha = 0.7f + 0.3f * pulse), style = androidx.compose.ui.graphics.drawscope.Stroke(u * 0.045f, cap = round, join = androidx.compose.ui.graphics.StrokeJoin.Round))
    }
}

private fun DrawScope.drawImpactEffects(cam: Cam, run: SkateRun) {
    val u = cam.u(0f)
    val bx = cam.sx(run.x, 0f)
    val by = cam.y(0f)
    val t = run.sinceHit
    if (t < 0.5f) {
        // l'éclat du choc : une étoile pointue jaune et orange qui grossit et s'efface
        val p = t / 0.5f
        val r = u * (0.22f + 0.4f * p)
        val center = Offset(bx, by - u * 0.32f)
        withAlpha(1f - p, Rect(center.x - r * 1.4f, center.y - r * 1.4f, center.x + r * 1.4f, center.y + r * 1.4f)) {
            burst(center, r, Color(0xFFFF8A1F), 11, 0.55f, 0f)
            burst(center, r * 0.68f, Color(0xFFFFE066), 11, 0.55f, 0.5f)
        }
        // l'étoile perdue s'envole
        val star = Offset(bx, by - u * 0.85f - u * 1.1f * p)
        withAlpha(1f - p * p, Rect(star.x - u * 0.3f, star.y - u * 0.3f, star.x + u * 0.3f, star.y + u * 0.3f)) {
            drawStar(star, u * 0.17f, on = true)
        }
    }
    // le saut : des étincelles qui tournent autour du skateur
    if (run.airborne) {
        val c = Offset(bx, by - (run.height + 0.32f) * u)
        for (k in 0 until 6) {
            val a = run.airProgress * 2f * PI.toFloat() * 2f + k * PI.toFloat() / 3f
            val r = u * (0.36f + 0.06f * sin(a * 3f))
            drawCircle(Color(0xFFFFE066), u * 0.028f, Offset(c.x + cos(a) * r, c.y + sin(a) * r * 0.8f))
            drawCircle(Color.White, u * 0.012f, Offset(c.x + cos(a) * r, c.y + sin(a) * r * 0.8f))
        }
    }
}

/** Une étoile à [spikes] pointes (l'éclat d'un choc). */
private fun DrawScope.burst(c: Offset, r: Float, color: Color, spikes: Int, inner: Float, phase: Float) {
    val path = Path()
    for (i in 0 until spikes * 2) {
        val a = (i + phase) * PI.toFloat() / spikes
        val rr = if (i % 2 == 0) r else r * inner
        val x = c.x + cos(a) * rr
        val y = c.y + sin(a) * rr
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color)
}

/** Les bords de l'écran rougissent un instant au choc. */
private fun DrawScope.drawHitFlash(run: SkateRun) {
    if (run.sinceHit >= 0.45f) return
    val a = 1f - run.sinceHit / 0.45f
    val radius = max(size.width, size.height) * 0.75f
    drawRect(Brush.radialGradient(0f to Color.Transparent, 0.5f to Color.Transparent, 1f to Color(0xFFFF3B30).copy(alpha = 0.42f * a), center = Offset(size.width / 2f, size.height * 0.55f), radius = radius))
}

/** Où se trouve le skateur à l'écran (pour poser les textes qui flottent au-dessus de lui). */
internal fun skaterScreenPosition(width: Float, height: Float, run: SkateRun): Offset {
    val cam = Cam(width, height)
    return Offset(cam.sx(run.x, 0f), cam.y(0f) - (run.height + 0.8f) * cam.unit)
}

internal fun skaterUnitPixels(width: Float, height: Float): Float = Cam(width, height).unit

/** Pour les écrans qui n'ont pas de course (le menu, la carte du jeu) : une rue immobile avec ce qu'on veut y poser, vue comme en course. */
internal fun DrawScope.drawStillStreet(theme: SkateTheme) {
    val cam = Cam(size.width, size.height)
    drawSky(cam, theme)
    drawGround(cam, theme, 0f)
    drawFog(cam, theme)
}


/** La couverture du jeu dans le menu : la rue vue de derrière avec un plot, un piéton, une rampe et le skateur. */
internal fun DrawScope.drawSkateCover(theme: SkateTheme, helmet: Color) {
    // une caméra plus basse que celle du jeu : on voit la scène de plus près
    val cam = Cam(size.width, size.height * 1.9f)
    translate(0f, -cam.height * 0.395f) {
        drawSky(cam, theme)
        drawGround(cam, theme, 0f)
        val cone = Piece(0, PieceKind.CONE, 16f, -0.55f, 0)
        val ped = Piece(1, PieceKind.PEDESTRIAN, 11f, 0.55f, 2)
        val ramp = Piece(2, PieceKind.RAMP, 5f, -0.05f, 0)
        val lamp = Piece(3, PieceKind.LAMP, 24f, 0.6f, 0)
        drawStreetPiece(cam, lamp, 24f, null, 0f, theme)
        drawStreetPiece(cam, cone, 16f, null, 0f, theme)
        drawStreetPiece(cam, ped, 11f, null, 0f, theme)
        drawFog(cam, theme)
        drawRamp(cam, ramp, 5f, theme)
        val u = cam.u(0f)
        val bx = cam.sx(-0.05f, 0f)
        val by = cam.y(0f)
        drawGroundShadow(bx, by, u, 0.17f)
        drawSkater(bx, by, u, helmet, 5f, 0f, 0f, 0f)
    }
}
