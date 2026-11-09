package com.kcharignon.petitsjeux.games.mushrooms.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.lerp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Les dessins de Champis Magiques : une forêt du soir, bleu-vert profond, où les champignons allumés brillent. Tout est dessiné par le code.
 * Taille de référence : [r] = rayon du chapeau d'un champignon, en pixels.
 */

/** Les couleurs des champignons allumés (jaune, orange, rose, cyan, vert) et la couleur interdite (violet), dans l'ordre des couleurs du moteur. */
val ShroomColors = listOf(
    Color(0xFFFFD93B),
    Color(0xFFFF9A2E),
    Color(0xFFFF6FA8),
    Color(0xFF39D0F0),
    Color(0xFF6BE05A),
    Color(0xFF8A3FD6),
)

private val CapOff = Color(0xFFF6F3EC)
private val CapOffShade = Color(0xFFD9D3C4)
private val StemLight = Color(0xFFFBF1DA)
private val StemShade = Color(0xFFE3D2AE)

/** Les couleurs de la forêt : ciel du soir, arbres lointains, arbres proches, sol. */
object Forest {
    val SkyTop = Color(0xFF16394F)
    val SkyBottom = Color(0xFF1F5A53)
    val FarTrees = Color(0xFF1B4A4A)
    val NearTrees = Color(0xFF143C3F)
    val Ground = Color(0xFF2B6A45)
    val GroundLight = Color(0xFF3A8352)
    val Moss = Color(0xFF4A9A5C)
}

private fun DrawScope.drawPine(x: Float, baseY: Float, h: Float, color: Color) {
    val w = h * 0.5f
    val trunk = Color(0xFF10303A)
    drawRoundRect(trunk, Offset(x - w * 0.07f, baseY - h * 0.16f), Size(w * 0.14f, h * 0.18f), CornerRadius(w * 0.03f))
    val tiers = 4
    for (k in 0 until tiers) {
        val top = baseY - h + k * h * 0.17f
        val bottom = baseY - h * 0.12f - (tiers - 1 - k) * h * 0.17f + h * 0.17f
        val half = w * (0.28f + 0.2f * k / (tiers - 1))
        val p = Path().apply {
            moveTo(x, top)
            lineTo(x + half, bottom)
            lineTo(x - half, bottom)
            close()
        }
        drawPath(p, color)
    }
}

/** Le fond de la forêt : ciel du soir, deux rangées de sapins, sol de mousse, lucioles (qui bougent avec [time] en secondes). */
fun DrawScope.drawForest(time: Float) {
    val w = size.width
    val h = size.height
    drawRect(Brush.verticalGradient(0f to Forest.SkyTop, 0.55f to Forest.SkyBottom, 1f to Forest.Ground))
    // la lune, très douce
    drawCircle(Color(0xFFFFF3C4).copy(alpha = 0.10f), w * 0.22f, Offset(w * 0.8f, h * 0.1f))
    drawCircle(Color(0xFFFFF3C4).copy(alpha = 0.18f), w * 0.09f, Offset(w * 0.8f, h * 0.1f))
    // sapins lointains puis proches
    val far = listOf(0.04f to 0.30f, 0.17f to 0.36f, 0.31f to 0.28f, 0.46f to 0.34f, 0.60f to 0.29f, 0.74f to 0.37f, 0.88f to 0.31f, 1.0f to 0.27f)
    for ((fx, fh) in far) drawPine(w * fx, h * 0.52f, h * fh, Forest.FarTrees)
    val near = listOf(-0.02f to 0.42f, 0.14f to 0.34f, 0.84f to 0.36f, 1.03f to 0.44f)
    for ((fx, fh) in near) drawPine(w * fx, h * 0.58f, h * fh, Forest.NearTrees)
    // le sol
    val ground = Path().apply {
        moveTo(0f, h * 0.55f)
        cubicTo(w * 0.3f, h * 0.50f, w * 0.7f, h * 0.60f, w, h * 0.54f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(ground, Brush.verticalGradient(0f to Forest.GroundLight, 1f to Forest.Ground, startY = h * 0.5f, endY = h))
    // touffes d'herbe
    val grass = Forest.Moss.copy(alpha = 0.55f)
    for (k in 0 until 22) {
        val gx = w * ((k * 0.137f + 0.05f) % 1f)
        val gy = h * (0.6f + 0.38f * ((k * 0.311f + 0.1f) % 1f))
        for (b in -1..1) drawLine(grass, Offset(gx, gy), Offset(gx + b * w * 0.012f, gy - h * 0.014f), strokeWidth = w * 0.006f, cap = StrokeCap.Round)
    }
    // lucioles : de petits points dorés qui flottent
    for (k in 0 until 14) {
        val phase = time * (0.15f + 0.03f * (k % 5)) + k * 1.7f
        val fx = w * (0.06f + 0.88f * ((k * 0.173f) % 1f)) + cos(phase * 2f * PI.toFloat()) * w * 0.02f
        val fy = h * (0.12f + 0.80f * ((k * 0.291f + 0.2f) % 1f)) + sin(phase * 2f * PI.toFloat()) * h * 0.012f
        val blink = 0.35f + 0.65f * (0.5f + 0.5f * sin((time * 1.4f + k) * 2f * PI.toFloat() / 2f))
        drawCircle(Color(0xFFFFE97A).copy(alpha = 0.16f * blink), w * 0.016f, Offset(fx, fy))
        drawCircle(Color(0xFFFFF6BF).copy(alpha = 0.85f * blink), w * 0.0045f, Offset(fx, fy))
    }
}

/**
 * Un champignon de rayon de chapeau [r], centré en ([cx], [cy]) (le centre du cercle qui l'entoure).
 *
 * - [capColor] `null` : éteint, un chapeau blanc ; sinon allumé de cette couleur, avec un halo qui pulse ([glow] de 0 à 1) ;
 * - [forbidden] : la couleur interdite (violet, taches sombres) ;
 * - [squash] de 0 à 1 : écrasé un instant quand on le touche, puis il rebondit ;
 * - [variant] : change un peu la place des taches d'un champignon à l'autre.
 */
fun DrawScope.drawMushroom(cx: Float, cy: Float, r: Float, capColor: Color?, glow: Float = 0f, squash: Float = 0f, forbidden: Boolean = false, variant: Int = 0) {
    val lit = capColor != null
    val groundY = cy + r * 0.98f
    // ombre au sol
    drawOval(Color.Black.copy(alpha = 0.28f), Offset(cx - r * 0.78f, groundY - r * 0.12f), Size(r * 1.56f, r * 0.3f))
    // halo
    if (lit) {
        val g = 0.6f + 0.4f * glow
        drawCircle(
            Brush.radialGradient(listOf(capColor!!.copy(alpha = 0.8f * g), capColor.copy(alpha = 0.26f * g), Color.Transparent), Offset(cx, cy), r * 2.1f),
            r * 2.1f, Offset(cx, cy),
        )
    }
    val sy = 1f - 0.2f * squash
    val sx = 1f + 0.1f * squash
    scale(sx, sy, pivot = Offset(cx, groundY)) {
        val rim = cy + r * 0.1f
        // pied
        val stem = Path().apply {
            moveTo(cx - r * 0.3f, rim - r * 0.05f)
            cubicTo(cx - r * 0.24f, rim + r * 0.4f, cx - r * 0.42f, groundY - r * 0.25f, cx - r * 0.4f, groundY - r * 0.04f)
            quadraticTo(cx, groundY + r * 0.1f, cx + r * 0.4f, groundY - r * 0.04f)
            cubicTo(cx + r * 0.42f, groundY - r * 0.25f, cx + r * 0.24f, rim + r * 0.4f, cx + r * 0.3f, rim - r * 0.05f)
            close()
        }
        drawPath(stem, Brush.horizontalGradient(listOf(StemLight, StemLight, StemShade), cx - r * 0.4f, cx + r * 0.4f))
        // dessous du chapeau (les lamelles)
        drawOval(Color(0xFFE9DCC0), Offset(cx - r * 0.94f, rim - r * 0.17f), Size(r * 1.88f, r * 0.36f))
        // chapeau
        val cap = Path().apply {
            moveTo(cx - r, rim)
            cubicTo(cx - r * 1.02f, cy - r * 1.18f, cx + r * 1.02f, cy - r * 1.18f, cx + r, rim)
            quadraticTo(cx, rim + r * 0.3f, cx - r, rim)
            close()
        }
        val base = capColor ?: CapOff
        val shade = if (lit) lerp(base, Color.Black, 0.28f) else CapOffShade
        drawPath(cap, Brush.verticalGradient(0f to lerp(base, Color.White, if (lit) 0.18f else 0f), 0.62f to base, 1f to shade, startY = cy - r, endY = rim + r * 0.3f))
        clipPath(cap) {
            // taches
            val spot = if (forbidden) Color(0xFF3A1766).copy(alpha = 0.9f) else if (lit) Color.White.copy(alpha = 0.85f) else Color(0xFFDAD4C6)
            val v = variant % 3
            val spots = listOf(
                Triple(-0.45f, -0.35f + 0.05f * v, 0.2f), Triple(0.15f + 0.05f * v, -0.62f, 0.17f), Triple(0.5f, -0.2f - 0.05f * v, 0.19f),
                Triple(-0.1f, -0.15f, 0.13f), Triple(-0.7f + 0.1f * v, 0.0f, 0.1f),
            )
            for ((sxk, syk, srk) in spots) drawCircle(spot, r * srk, Offset(cx + r * sxk, cy + r * syk))
            // reflet
            drawOval(Color.White.copy(alpha = if (lit) 0.42f else 0.55f), Offset(cx - r * 0.72f, cy - r * 0.86f), Size(r * 0.5f, r * 0.24f))
        }
        drawPath(cap, shade.copy(alpha = 0.6f), style = Stroke(r * 0.04f))
        if (forbidden) {
            // un trait en travers : « interdit »
            drawLine(Color(0xFFFF4D4D), Offset(cx - r * 0.55f, cy - r * 0.5f), Offset(cx + r * 0.55f, cy + r * 0.25f), strokeWidth = r * 0.12f, cap = StrokeCap.Round)
        }
    }
}

/** Des étincelles qui jaillissent autour de ([cx], [cy]) : [progress] de 0 (au début) à 1 (disparues). */
fun DrawScope.drawBurst(cx: Float, cy: Float, r: Float, color: Color, progress: Float) {
    if (progress <= 0f || progress >= 1f) return
    val fade = 1f - progress
    for (k in 0 until 10) {
        val a = k * 2f * PI.toFloat() / 10f + (k % 2) * 0.15f
        val d = r * (0.9f + 1.3f * progress) * (if (k % 2 == 0) 1f else 0.78f)
        val px = cx + cos(a) * d
        val py = cy + sin(a) * d
        val size = r * (if (k % 2 == 0) 0.14f else 0.09f) * (0.5f + fade)
        drawCircle(color.copy(alpha = fade), size, Offset(px, py))
        drawCircle(Color.White.copy(alpha = 0.9f * fade), size * 0.45f, Offset(px, py))
    }
    drawCircle(color.copy(alpha = 0.7f * fade), r * (1f + 0.9f * progress), Offset(cx, cy), style = Stroke(r * 0.1f * fade))
}

/** Une petite nuée de fumée quand un champignon s'éteint tout seul : [progress] de 0 à 1. */
fun DrawScope.drawPuff(cx: Float, cy: Float, r: Float, progress: Float) {
    if (progress <= 0f || progress >= 1f) return
    val fade = 1f - progress
    for (k in 0 until 4) {
        val a = -PI.toFloat() / 2 + (k - 1.5f) * 0.6f
        drawCircle(Color.White.copy(alpha = 0.45f * fade), r * (0.16f + 0.1f * progress), Offset(cx + cos(a) * r * 0.6f * progress * 1.6f, cy - r * 0.3f + sin(a) * r * 0.9f * progress))
    }
}
