package com.kcharignon.petitsjeux.games.plane.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Les dessins de Super Avion : tout est dessiné par le code, à plat, avec un fin contour d'encre, de face (les obstacles) ou de dos (l'avion).
 * Chaque dessin se pose sur un point `(cx, cy)` de l'écran, avec [u] pixels par « unité de ciel » à cette distance :
 * on dessine en unités de ciel, y vers le haut, l'origine au centre de la chose.
 */

internal val Ink = Color(0xFF2B2D42)
private val ShadowColor = Color(0xFF14172E)

/** Un pinceau qui dessine en unités de ciel (y vers le haut) autour d'un point. Avec `edge`, la forme est cernée d'un fin contour d'encre. */
internal class Pen(val s: DrawScope, val bx: Float, val by: Float, val u: Float) {
    private fun px(x: Float) = bx + x * u
    private fun py(y: Float) = by - y * u
    private val outline get() = (0.013f * u).coerceAtLeast(1f)

    fun rect(color: Color, x: Float, y: Float, w: Float, h: Float, r: Float = 0f, edge: Boolean = false) {
        s.drawRoundRect(color, Offset(px(x), py(y + h)), Size(w * u, h * u), CornerRadius(r * u))
        if (edge) s.drawRoundRect(Ink, Offset(px(x), py(y + h)), Size(w * u, h * u), CornerRadius(r * u), style = Stroke(outline))
    }

    fun circle(color: Color, cx: Float, cy: Float, r: Float, edge: Boolean = false) {
        s.drawCircle(color, r * u, Offset(px(cx), py(cy)))
        if (edge) s.drawCircle(Ink, r * u, Offset(px(cx), py(cy)), style = Stroke(outline))
    }

    fun oval(color: Color, cx: Float, cy: Float, rx: Float, ry: Float, edge: Boolean = false) {
        s.drawOval(color, Offset(px(cx - rx), py(cy + ry)), Size(rx * 2 * u, ry * 2 * u))
        if (edge) s.drawOval(Ink, Offset(px(cx - rx), py(cy + ry)), Size(rx * 2 * u, ry * 2 * u), style = Stroke(outline))
    }

    fun poly(color: Color, vararg p: Float, edge: Boolean = false) {
        val path = Path()
        for (i in 0 until p.size / 2) if (i == 0) path.moveTo(px(p[0]), py(p[1])) else path.lineTo(px(p[2 * i]), py(p[2 * i + 1]))
        path.close()
        s.drawPath(path, color)
        if (edge) s.drawPath(path, Ink, style = Stroke(outline, join = StrokeJoin.Round))
    }

    fun line(color: Color, x1: Float, y1: Float, x2: Float, y2: Float, w: Float) =
        s.drawLine(color, Offset(px(x1), py(y1)), Offset(px(x2), py(y2)), strokeWidth = (w * u).coerceAtLeast(1f), cap = StrokeCap.Round)

    /** Une ligne brisée épaisse (une aile d'oiseau, une corde). */
    fun polyline(color: Color, w: Float, vararg p: Float) {
        val path = Path()
        for (i in 0 until p.size / 2) if (i == 0) path.moveTo(px(p[0]), py(p[1])) else path.lineTo(px(p[2 * i]), py(p[2 * i + 1]))
        s.drawPath(path, color, style = Stroke((w * u).coerceAtLeast(1f), cap = StrokeCap.Round, join = StrokeJoin.Round))
    }

    /** Le haut d'un cercle de centre (cx, cy) : un demi-disque. */
    fun cap(color: Color, cx: Float, cy: Float, r: Float, edge: Boolean = false) {
        val path = Path().apply {
            moveTo(px(cx - r), py(cy))
            arcTo(Rect(px(cx - r), py(cy + r), px(cx + r), py(cy - r)), 180f, 180f, false)
            close()
        }
        s.drawPath(path, color)
        if (edge) s.drawPath(path, Ink, style = Stroke(outline, join = StrokeJoin.Round))
    }

    /** Un reflet blanc doux. */
    fun shine(cx: Float, cy: Float, rx: Float, ry: Float, alpha: Float = 0.45f) = oval(Color.White.copy(alpha = alpha), cx, cy, rx, ry)
}

/** Dessine [block] avec une transparence [alpha] (0 à 1) : les choses qui s'effacent, l'avion qui clignote. */
internal fun DrawScope.withAlpha(alpha: Float, bounds: Rect, block: DrawScope.() -> Unit) {
    if (alpha >= 0.999f) {
        block()
        return
    }
    drawIntoCanvas { canvas ->
        canvas.saveLayer(bounds, Paint().apply { this.alpha = alpha.coerceIn(0f, 1f) })
        block()
        canvas.restore()
    }
}

/** L'ombre ronde d'une chose du ciel sur le sol, très loin dessous : elle dit dans quelle voie on est. */
internal fun DrawScope.drawGroundShadow(bx: Float, by: Float, u: Float, halfWidth: Float, strength: Float = 0.22f) {
    drawOval(ShadowColor.copy(alpha = strength), Offset(bx - halfWidth * u, by - halfWidth * u * 0.26f), Size(halfWidth * 2f * u, halfWidth * u * 0.52f))
}

// ------------------------------------------------------------------ l'avion du joueur

/**
 * L'avion du joueur, de dos, large de 0,40 unité : ailes, empennage, dérive, verrière et un petit panache à la tuyère, aux couleurs de son animal.
 * [bank] : l'inclinaison en degrés (vers la droite si positif) ; [roll] : 0 à 1, un tonneau complet autour de l'axe de l'avion ; [gear] : 0 à 1, le train sorti (à l'approche).
 */
internal fun DrawScope.drawPlayerPlane(cx: Float, cy: Float, u: Float, color: Color, bank: Float, roll: Float, gear: Float, time: Float) {
    val dark = Color(color.red * 0.72f, color.green * 0.72f, color.blue * 0.72f)
    val light = Color(color.red + (1f - color.red) * 0.35f, color.green + (1f - color.green) * 0.35f, color.blue + (1f - color.blue) * 0.35f)
    val white = Color(0xFFF8FAFF)
    val shade = Color(0xFFD9E1F2)
    rotate(bank + roll * 360f, Offset(cx, cy)) {
        val p = Pen(this, cx, cy, u)
        // le train d'atterrissage, sous les ailes
        if (gear > 0.5f) for (side in listOf(-1f, 1f)) {
            p.line(Ink, side * 0.075f, -0.015f, side * 0.075f, -0.075f, 0.014f)
            p.circle(Color(0xFF3A3D52), side * 0.075f, -0.085f, 0.027f, edge = true)
            p.circle(Color(0xFFC9CEDB), side * 0.075f, -0.085f, 0.011f)
        }
        // les ailes : dessus clair, bout de l'aile aux couleurs de l'avion, feu de position rouge à gauche et vert à droite
        for (side in listOf(-1f, 1f)) {
            p.poly(white, side * 0.04f, -0.018f, side * 0.205f, 0.0f, side * 0.195f, 0.04f, side * 0.04f, 0.045f, edge = true)
            p.poly(shade, side * 0.04f, -0.018f, side * 0.205f, 0.0f, side * 0.2f, 0.012f, side * 0.04f, -0.002f)
            p.poly(color, side * 0.145f, -0.006f, side * 0.205f, 0.0f, side * 0.195f, 0.04f, side * 0.15f, 0.037f)
            p.circle(if (side < 0) Color(0xFFFF4B4B) else Color(0xFF4BE06B), side * 0.2f, 0.018f, 0.012f)
        }
        // l'empennage horizontal, plus près de nous
        for (side in listOf(-1f, 1f)) {
            p.poly(light, side * 0.03f, 0.0f, side * 0.115f, 0.012f, side * 0.108f, 0.04f, side * 0.03f, 0.036f, edge = true)
        }
        // le fuselage vu de l'arrière
        p.oval(color, 0f, 0.0f, 0.056f, 0.062f, edge = true)
        p.oval(dark, 0f, -0.026f, 0.05f, 0.03f)
        p.shine(-0.02f, 0.03f, 0.016f, 0.026f)
        p.circle(Color(0xFF3A3D52), 0f, -0.004f, 0.027f, edge = true)
        val flicker = 0.75f + 0.25f * sin(time * 40f)
        p.circle(Color(0xFFFFB020), 0f, -0.004f, 0.019f * flicker)
        p.circle(Color(0xFFFFF1A8), 0f, -0.004f, 0.01f * flicker)
        // la verrière et la dérive
        p.oval(Color(0xFF8FD3FF), 0f, 0.052f, 0.026f, 0.021f, edge = true)
        p.shine(-0.008f, 0.058f, 0.009f, 0.008f, 0.7f)
        p.poly(color, -0.016f, 0.05f, 0.016f, 0.05f, 0.009f, 0.15f, -0.009f, 0.15f, edge = true)
        p.poly(white, -0.012f, 0.1f, 0.012f, 0.1f, 0.009f, 0.15f, -0.009f, 0.15f)
        // un fin liseré blanc autour de la tuyère
        p.line(Color.White.copy(alpha = 0.55f), -0.04f, 0.0f, -0.04f, -0.03f, 0.008f)
    }
}

/** La fumée d'un choc : des ronds gris qui montent et grossissent derrière l'avion pendant une demi-seconde. */
internal fun DrawScope.drawSmoke(cx: Float, cy: Float, u: Float, age: Float) {
    if (age > 0.7f) return
    val p = age / 0.7f
    for (k in 0 until 5) {
        val a = k * 1.3f
        val x = cx + sin(a * 2f) * u * (0.05f + 0.1f * p)
        val y = cy + u * (0.02f + 0.18f * p) * (0.6f + 0.2f * k)
        val r = u * (0.035f + 0.05f * p + 0.008f * k)
        drawCircle(Color(0xFF4B4F66).copy(alpha = (0.55f * (1f - p)).coerceAtLeast(0f)), r, Offset(x, y))
    }
}

// ------------------------------------------------------------------ les obstacles

private fun mixColor(a: Color, b: Color, t: Float) = Color(a.red + (b.red - a.red) * t, a.green + (b.green - a.green) * t, a.blue + (b.blue - a.blue) * t)

/** Un oiseau de face : corps rond, tête, bec orange, deux ailes qui battent ([flap] de -1 à 1), bouts d'ailes plus sombres. */
private fun Pen.bird(x: Float, y: Float, size: Float, flap: Float, body: Color, wing: Color, tip: Color) {
    val k = size
    val lift = flap * 0.05f * k
    for (side in listOf(-1f, 1f)) {
        // une aile en deux morceaux : l'épaule puis le bout
        polyline(Ink, 0.034f * k, x + side * 0.012f * k, y + 0.012f * k, x + side * 0.045f * k, y + 0.024f * k + lift * 0.6f, x + side * 0.088f * k, y + 0.012f * k + lift)
        polyline(wing, 0.026f * k, x + side * 0.012f * k, y + 0.012f * k, x + side * 0.045f * k, y + 0.024f * k + lift * 0.6f, x + side * 0.086f * k, y + 0.012f * k + lift)
        polyline(tip, 0.022f * k, x + side * 0.066f * k, y + 0.019f * k + lift * 0.8f, x + side * 0.096f * k, y + 0.008f * k + lift * 1.05f)
    }
    oval(body, x, y - 0.004f * k, 0.024f * k, 0.028f * k, edge = true)
    circle(body, x, y + 0.03f * k, 0.018f * k)
    circle(Ink, x - 0.007f * k, y + 0.034f * k, 0.0035f * k)
    circle(Ink, x + 0.007f * k, y + 0.034f * k, 0.0035f * k)
    poly(Color(0xFFFF9A1F), x - 0.007f * k, y + 0.024f * k, x + 0.007f * k, y + 0.024f * k, x, y + 0.014f * k)
}

/** Un banc d'oiseaux en V, large de 0,28 : 5, 4 ou 3 oiseaux selon la variante ; la couleur change aussi ; ils battent des ailes chacun à son rythme. */
internal fun DrawScope.drawBirds(cx: Float, cy: Float, u: Float, variant: Int, time: Float, seed: Int) {
    val p = Pen(this, cx, cy, u)
    val body = listOf(Color(0xFFF8FAFF), Color(0xFFC98B54), Color(0xFF8FB4FF))[variant.mod(3)]
    val wing = listOf(Color(0xFFE3E8F5), Color(0xFFA96E3B), Color(0xFF6E96E8))[variant.mod(3)]
    val tip = listOf(Color(0xFF6B7088), Color(0xFF6B4A2E), Color(0xFF3F5FB0))[variant.mod(3)]
    val spots = when (variant.mod(3)) {
        0 -> listOf(0f to 0.07f, -0.085f to 0.02f, 0.085f to 0.02f, -0.17f to -0.03f, 0.17f to -0.03f)
        1 -> listOf(-0.05f to 0.05f, 0.05f to 0.05f, -0.1f to -0.03f, 0.1f to -0.03f)
        else -> listOf(0f to 0.06f, -0.1f to -0.02f, 0.1f to -0.02f)
    }
    val size = when (variant.mod(3)) {
        0 -> 0.86f
        1 -> 0.98f
        else -> 1.12f
    }
    for ((i, s) in spots.withIndex()) {
        p.bird(s.first, s.second, size, sin(time * 11f + i * 1.7f + seed), body, wing, tip)
    }
}

/**
 * Un avion qui vient de face, large de 0,30 : une hélice floue, un nez rond, une verrière, une aile haute, un train. Une variante par couleur
 * (rouge, jaune, vert).
 */
internal fun DrawScope.drawOncomingPlane(cx: Float, cy: Float, u: Float, variant: Int, time: Float) {
    val p = Pen(this, cx, cy, u)
    val body = listOf(Color(0xFFE8362F), Color(0xFFFFC21F), Color(0xFF3FBF4A))[variant.mod(3)]
    val dark = Color(body.red * 0.74f, body.green * 0.74f, body.blue * 0.74f)
    val white = Color(0xFFF8FAFF)
    // le train
    for (side in listOf(-1f, 1f)) {
        p.line(Ink, side * 0.05f, -0.03f, side * 0.06f, -0.075f, 0.012f)
        p.circle(Color(0xFF3A3D52), side * 0.06f, -0.085f, 0.022f, edge = true)
    }
    // le fuselage et le nez
    p.circle(body, 0f, 0f, 0.062f, edge = true)
    p.oval(dark, 0f, -0.035f, 0.052f, 0.026f)
    p.oval(white, 0f, -0.02f, 0.03f, 0.02f)
    p.circle(Color(0xFFC9CEDB), 0f, 0f, 0.022f, edge = true)
    p.circle(Color(0xFFF8FAFF), -0.006f, 0.006f, 0.008f)
    // la verrière
    p.cap(Color(0xFF8FD3FF), 0f, 0.035f, 0.036f, edge = true)
    p.shine(-0.012f, 0.056f, 0.01f, 0.007f, 0.75f)
    // l'aile haute, le bout de chaque aile de la couleur de l'avion
    p.rect(white, -0.15f, 0.052f, 0.3f, 0.028f, 0.012f, edge = true)
    p.rect(body, -0.15f, 0.052f, 0.06f, 0.028f, 0.012f)
    p.rect(body, 0.09f, 0.052f, 0.06f, 0.028f, 0.012f)
    p.line(Ink, -0.045f, 0.04f, -0.045f, 0.056f, 0.01f)
    p.line(Ink, 0.045f, 0.04f, 0.045f, 0.056f, 0.01f)
    // l'hélice : un disque flou et deux pales qui tournent
    p.circle(Color.White.copy(alpha = 0.22f), 0f, 0f, 0.105f)
    val a = time * 38f
    p.line(Color(0xFF2B2D42).copy(alpha = 0.35f), cos(a) * 0.1f, sin(a) * 0.1f, -cos(a) * 0.1f, -sin(a) * 0.1f, 0.014f)
}

private val BalloonColors = listOf(
    Triple(Color(0xFFFF5A4A), Color(0xFFFFE066), Color(0xFFD63E30)),
    Triple(Color(0xFF3E8BFF), Color(0xFFF8FAFF), Color(0xFF2B66D1)),
    Triple(Color(0xFF3FBF4A), Color(0xFFFF8FB8), Color(0xFF2A9A3C)),
)

/** Une montgolfière, large de 0,26 et haute de 0,34 : l'enveloppe à bandes, la jupe, les cordes, la nacelle et la flamme du brûleur qui vacille. */
internal fun DrawScope.drawBalloon(cx: Float, cy: Float, u: Float, variant: Int, time: Float) {
    val p = Pen(this, cx, cy, u)
    val (a, b, c) = BalloonColors[variant.mod(3)]
    val sway = sin(time * 1.6f + variant) * 0.004f
    // les cordes et la nacelle
    p.line(Ink.copy(alpha = 0.8f), -0.055f, -0.07f + sway, -0.03f, -0.115f, 0.007f)
    p.line(Ink.copy(alpha = 0.8f), 0.055f, -0.07f + sway, 0.03f, -0.115f, 0.007f)
    p.rect(Color(0xFFC98B54), -0.04f, -0.16f, 0.08f, 0.048f, 0.014f, edge = true)
    p.rect(Color(0xFFA96E3B), -0.04f, -0.133f, 0.08f, 0.012f, 0.004f)
    // la flamme du brûleur
    val flame = 0.8f + 0.2f * sin(time * 17f)
    p.oval(Color(0xFFFF9A1F), 0f, -0.066f, 0.014f * flame, 0.026f * flame)
    p.oval(Color(0xFFFFEE88), 0f, -0.064f, 0.007f, 0.014f * flame)
    // l'enveloppe : un disque, des bandes, un reflet, la jupe sombre
    p.poly(c, -0.062f, -0.052f, 0.062f, -0.052f, 0.034f, -0.092f, -0.034f, -0.092f, edge = true)
    p.circle(a, 0f, 0.058f, 0.128f, edge = true)
    p.oval(b, 0f, 0.058f, 0.088f, 0.127f)
    p.oval(a, 0f, 0.058f, 0.044f, 0.126f)
    p.circle(Color.Transparent, 0f, 0.058f, 0.128f, edge = true)
    p.shine(-0.06f, 0.1f, 0.022f, 0.045f, 0.4f)
}

/**
 * Un parapentiste (variantes 0 et 1 : une aile à cellules colorées) ou un parachutiste (variante 2 : une coupole à bandes), large de 0,26 et haut de 0,3 :
 * des suspentes, un pilote suspendu qui salue de la main.
 */
internal fun DrawScope.drawParaglider(cx: Float, cy: Float, u: Float, variant: Int, time: Float) {
    val p = Pen(this, cx, cy, u)
    val colors = listOf(
        listOf(Color(0xFFFF5A4A), Color(0xFFFFE066)),
        listOf(Color(0xFF3E8BFF), Color(0xFFFF8FB8)),
        listOf(Color(0xFFB36BE8), Color(0xFFFFB020)),
    )[variant.mod(3)]
    val swing = sin(time * 2.1f + variant) * 0.01f
    val wave = sin(time * 6f)
    val round = variant.mod(3) == 2
    // les suspentes d'abord, derrière tout
    val anchors = if (round) listOf(-0.12f to 0.035f, -0.04f to 0.03f, 0.04f to 0.03f, 0.12f to 0.035f) else listOf(-0.12f to 0.052f, -0.045f to 0.078f, 0.045f to 0.078f, 0.12f to 0.052f)
    for ((x, y) in anchors) p.line(Ink.copy(alpha = 0.6f), x, y, swing, -0.045f, 0.006f)
    // le pilote : jambes, sangle, corps, bras qui salue, casque
    p.rect(Color(0xFF34406B), swing - 0.026f, -0.15f, 0.022f, 0.06f, 0.01f, edge = true)
    p.rect(Color(0xFF34406B), swing + 0.004f, -0.15f, 0.022f, 0.06f, 0.01f, edge = true)
    p.rect(colors[0], swing - 0.034f, -0.1f, 0.068f, 0.08f, 0.026f, edge = true)
    p.line(Color(0xFFFFD2A6), swing + 0.03f, -0.05f, swing + 0.075f, -0.015f + wave * 0.014f, 0.018f)
    p.line(colors[0], swing - 0.03f, -0.055f, swing - 0.07f, -0.085f, 0.018f)
    p.circle(Color(0xFFFFD2A6), swing, -0.03f, 0.03f, edge = true)
    p.cap(colors[1], swing, -0.025f, 0.034f, edge = true)
    p.circle(Ink, swing - 0.01f, -0.032f, 0.0045f)
    p.circle(Ink, swing + 0.01f, -0.032f, 0.0045f)
    if (round) {
        // la coupole : six fuseaux
        val n = 6
        for (i in 0 until n) {
            val a0 = PI.toFloat() - i * PI.toFloat() / n
            val a1 = PI.toFloat() - (i + 1) * PI.toFloat() / n
            p.poly(
                colors[i % 2],
                cos(a0) * 0.14f, 0.03f + sin(a0) * 0.12f, cos(a1) * 0.14f, 0.03f + sin(a1) * 0.12f, cos(a1) * 0.14f, 0.03f, cos(a0) * 0.14f, 0.03f, edge = true,
            )
        }
        p.shine(-0.05f, 0.12f, 0.025f, 0.012f, 0.4f)
    } else {
        // l'aile : une bande courbe à cellules qui alternent
        val top = listOf(-0.14f to 0.05f, -0.095f to 0.09f, -0.05f to 0.108f, 0f to 0.113f, 0.05f to 0.108f, 0.095f to 0.09f, 0.14f to 0.05f)
        val bottom = listOf(-0.14f to 0.037f, -0.095f to 0.063f, -0.05f to 0.078f, 0f to 0.082f, 0.05f to 0.078f, 0.095f to 0.063f, 0.14f to 0.037f)
        for (i in 0 until 6) {
            p.poly(colors[i % 2], top[i].first, top[i].second, top[i + 1].first, top[i + 1].second, bottom[i + 1].first, bottom[i + 1].second, bottom[i].first, bottom[i].second, edge = true)
        }
        p.shine(-0.03f, 0.1f, 0.03f, 0.006f, 0.45f)
    }
}

/** Un anneau lumineux, de rayon 0,17 : un anneau d'or, un cœur de lumière, des étincelles qui tournent et un halo qui pulse. */
internal fun DrawScope.drawRing(cx: Float, cy: Float, u: Float, time: Float) {
    val pulse = 0.5f + 0.5f * sin(time * 5f)
    val r = u * 0.17f
    // le halo
    drawCircle(
        Brush.radialGradient(0f to Color(0xFFFFE066).copy(alpha = 0f), 0.6f to Color(0xFFFFE066).copy(alpha = 0.0f), 0.8f to Color(0xFFFFE066).copy(alpha = 0.32f + 0.12f * pulse), 1f to Color(0xFFFFE066).copy(alpha = 0f), center = Offset(cx, cy), radius = r * 1.75f),
        r * 1.75f, Offset(cx, cy),
    )
    // l'anneau : un trait épais d'orange, un d'or, un fin liseré blanc
    drawCircle(Color(0xFFFF8A1F), r, Offset(cx, cy), style = Stroke(u * 0.062f))
    drawCircle(Color(0xFFFFC21F), r, Offset(cx, cy), style = Stroke(u * 0.044f))
    drawCircle(Color(0xFFFFF3B0), r - u * 0.008f, Offset(cx, cy), style = Stroke(u * 0.014f))
    // des étincelles qui tournent autour
    for (k in 0 until 6) {
        val a = time * 2.4f + k * PI.toFloat() / 3f
        val rr = r * (1.28f + 0.1f * sin(time * 4f + k))
        val c = Offset(cx + cos(a) * rr, cy + sin(a) * rr)
        drawCircle(Color.White, u * 0.014f, c)
        drawCircle(Color(0xFFFFE066).copy(alpha = 0.7f), u * 0.026f, c)
    }
}

/** Le dessin d'un obstacle (jamais d'un anneau), centré sur `(cx, cy)`. */
internal fun DrawScope.drawObstacle(kind: com.kcharignon.petitsjeux.games.plane.engine.PieceKind, variant: Int, cx: Float, cy: Float, u: Float, time: Float, seed: Int) {
    when (kind) {
        com.kcharignon.petitsjeux.games.plane.engine.PieceKind.BIRDS -> drawBirds(cx, cy, u, variant, time, seed)
        com.kcharignon.petitsjeux.games.plane.engine.PieceKind.PLANE -> drawOncomingPlane(cx, cy, u, variant, time)
        com.kcharignon.petitsjeux.games.plane.engine.PieceKind.BALLOON -> drawBalloon(cx, cy, u, variant, time)
        com.kcharignon.petitsjeux.games.plane.engine.PieceKind.PARAGLIDER -> drawParaglider(cx, cy, u, variant, time)
        com.kcharignon.petitsjeux.games.plane.engine.PieceKind.RING -> drawRing(cx, cy, u, time)
    }
}

// ------------------------------------------------------------------ le décor du ciel

/** Un nuage de décor, large d'environ 0,6 unité : des boules blanches, un ventre plus sombre. */
internal fun DrawScope.drawCloud(cx: Float, cy: Float, u: Float, variant: Int, tint: Color) {
    val p = Pen(this, cx, cy, u)
    val shade = mixColor(tint, Color(0xFF9DB4D8), 0.45f)
    val w = 0.9f + 0.25f * (variant % 3)
    p.rect(shade, -0.3f * w, -0.07f, 0.6f * w, 0.1f, 0.05f)
    p.circle(shade, -0.18f * w, 0.0f, 0.1f)
    p.circle(shade, 0.2f * w, -0.005f, 0.09f)
    p.rect(tint, -0.29f * w, -0.04f, 0.58f * w, 0.1f, 0.05f)
    p.circle(tint, -0.15f * w, 0.07f, 0.12f)
    p.circle(tint, 0.05f * w, 0.11f, 0.15f)
    p.circle(tint, 0.2f * w, 0.05f, 0.11f)
}

// ------------------------------------------------------------------ le décor du sol

private val Roofs = listOf(Color(0xFFE8562F), Color(0xFF3E8BFF), Color(0xFF8A5A3C))

/** Un arbre rond vu de loin, posé sur le sol en `(bx, by)`, haut de 0,35 unité. */
internal fun DrawScope.drawGroundTree(bx: Float, by: Float, u: Float, variant: Int, night: Boolean) {
    val p = Pen(this, bx, by, u)
    val leaf = if (night) Color(0xFF1F5A48) else listOf(Color(0xFF3FBF4A), Color(0xFF55CC4A), Color(0xFF2FA845))[variant.mod(3)]
    p.oval(Color(0xFF14172E).copy(alpha = 0.18f), 0f, 0f, 0.09f, 0.025f)
    p.rect(Color(0xFF8A5A3C), -0.012f, 0f, 0.024f, 0.1f)
    p.circle(if (night) Color(0xFF173F34) else Color(0xFF2A9A3C), 0.01f, 0.2f, 0.11f)
    p.circle(leaf, -0.03f, 0.22f, 0.09f)
    p.circle(leaf, 0.04f, 0.23f, 0.08f)
    p.circle(leaf, 0f, 0.27f, 0.08f)
}

/** Une maison à toit rouge (ou bleu, ou brun), haute de 0,3 unité, avec ses fenêtres (allumées la nuit). */
internal fun DrawScope.drawGroundHouse(bx: Float, by: Float, u: Float, variant: Int, night: Boolean) {
    val p = Pen(this, bx, by, u)
    val wall = if (night) Color(0xFF3A3F70) else listOf(Color(0xFFFFF1D0), Color(0xFFFFD7B0), Color(0xFFE9F0FF))[variant.mod(3)]
    val roof = if (night) Color(0xFF2A2E58) else Roofs[variant.mod(3)]
    p.oval(Color(0xFF14172E).copy(alpha = 0.18f), 0.02f, 0f, 0.15f, 0.03f)
    p.rect(wall, -0.11f, 0f, 0.22f, 0.15f, 0.01f, edge = true)
    p.poly(roof, -0.135f, 0.15f, 0.135f, 0.15f, 0.0f, 0.27f, edge = true)
    val glass = if (night) Color(0xFFFFE08A) else Color(0xFF8FD3FF)
    p.rect(glass, -0.08f, 0.07f, 0.05f, 0.05f, 0.005f)
    p.rect(glass, 0.03f, 0.07f, 0.05f, 0.05f, 0.005f)
    p.rect(if (night) Color(0xFF1E2146) else Color(0xFF6B4A3A), -0.02f, 0f, 0.04f, 0.07f, 0.005f)
}

/** Un moulin à vent, haut de 0,5 unité : une tour, quatre pales qui tournent. */
internal fun DrawScope.drawWindmill(bx: Float, by: Float, u: Float, time: Float, night: Boolean) {
    val p = Pen(this, bx, by, u)
    p.oval(Color(0xFF14172E).copy(alpha = 0.18f), 0f, 0f, 0.12f, 0.03f)
    p.poly(if (night) Color(0xFF3A3F70) else Color(0xFFFFF1D0), -0.07f, 0f, 0.07f, 0f, 0.04f, 0.3f, -0.04f, 0.3f, edge = true)
    p.poly(if (night) Color(0xFF2A2E58) else Color(0xFFE8562F), -0.055f, 0.3f, 0.055f, 0.3f, 0f, 0.36f, edge = true)
    val a = time * 1.3f
    for (k in 0 until 4) {
        val t = a + k * PI.toFloat() / 2f
        p.line(if (night) Color(0xFFB8BCE0) else Color.White, 0f, 0.31f, cos(t) * 0.2f, 0.31f + sin(t) * 0.2f, 0.035f)
    }
    p.circle(Ink, 0f, 0.31f, 0.018f)
}

/** Un voilier sur la mer, haut de 0,32 unité : une coque, deux voiles. */
internal fun DrawScope.drawBoat(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    p.oval(Color(0xFF14172E).copy(alpha = 0.15f), 0f, -0.01f, 0.14f, 0.025f)
    p.poly(Color(0xFFF8FAFF), -0.01f, 0.04f, -0.01f, 0.28f, -0.11f, 0.05f, edge = true)
    p.poly(Roofs[variant.mod(3)], 0.01f, 0.04f, 0.01f, 0.22f, 0.09f, 0.05f, edge = true)
    p.poly(Color(0xFF8A5A3C), -0.14f, 0.045f, 0.14f, 0.045f, 0.1f, 0f, -0.1f, 0f, edge = true)
    p.oval(Color.White.copy(alpha = 0.6f), 0f, -0.015f, 0.18f, 0.012f)
}

/** Des vaguelettes blanches : quelques petits arcs. */
internal fun DrawScope.drawWaves(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    val c = Color.White.copy(alpha = 0.55f)
    for (k in 0 until 3) {
        val x = (k - 1) * 0.13f + ((variant + k) % 3) * 0.02f
        p.polyline(c, 0.014f, x - 0.045f, 0.0f + 0.012f * (k % 2), x, 0.018f + 0.012f * (k % 2), x + 0.045f, 0.0f + 0.012f * (k % 2))
    }
}

/** Une petite île de sable avec deux palmiers, haute de 0,3 unité. */
internal fun DrawScope.drawIsland(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    p.oval(Color(0xFF7FD9F2).copy(alpha = 0.6f), 0f, 0f, 0.3f, 0.06f)
    p.oval(Color(0xFFF1D79A), 0f, 0.01f, 0.22f, 0.05f)
    p.oval(Color(0xFF7ACB5B), 0f, 0.04f, 0.14f, 0.035f)
    for ((x, h) in listOf(-0.05f to 0.2f, 0.05f to 0.16f)) {
        p.line(Color(0xFF9A6A3E), x, 0.03f, x + 0.012f, 0.03f + h, 0.022f)
        for (a in listOf(-150f, -110f, -70f, -30f)) {
            val r = a * PI.toFloat() / 180f
            p.line(Color(0xFF2FA845), x + 0.012f, 0.03f + h, x + 0.012f + cos(r) * 0.08f, 0.03f + h + sin(r) * 0.045f + 0.02f, 0.03f)
        }
    }
}

/** Un cactus, haut de 0,25 unité. */
internal fun DrawScope.drawCactus(bx: Float, by: Float, u: Float) {
    val p = Pen(this, bx, by, u)
    p.oval(Color(0xFF14172E).copy(alpha = 0.16f), 0f, 0f, 0.1f, 0.025f)
    val g = Color(0xFF3FA24A)
    p.rect(g, -0.025f, 0f, 0.05f, 0.24f, 0.025f, edge = true)
    p.rect(g, -0.08f, 0.09f, 0.04f, 0.1f, 0.02f, edge = true)
    p.rect(g, -0.08f, 0.09f, 0.07f, 0.035f, 0.015f)
    p.rect(g, 0.04f, 0.06f, 0.04f, 0.09f, 0.02f, edge = true)
    p.rect(g, -0.001f, 0.06f, 0.08f, 0.035f, 0.015f)
}

/** Un plateau rocheux orange (mesa), haut de 0,3 unité. */
internal fun DrawScope.drawMesa(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    val a = Color(0xFFD9825B)
    val b = Color(0xFFB8643F)
    val w = 0.2f + 0.04f * (variant % 3)
    p.oval(Color(0xFF14172E).copy(alpha = 0.18f), 0.04f, 0f, w + 0.1f, 0.04f)
    p.poly(b, -w, 0f, w, 0f, w * 0.7f, 0.26f, -w * 0.7f, 0.26f, edge = true)
    p.poly(a, -w, 0f, -w * 0.2f, 0f, -w * 0.15f, 0.26f, -w * 0.7f, 0.26f)
    p.rect(Color(0xFFF0A27A), -w * 0.72f, 0.24f, w * 1.44f, 0.04f, 0.015f)
}

/** Un immeuble de ville vu de nuit (ou de jour), haut de 0,3 à 0,6 unité, aux fenêtres allumées. */
internal fun DrawScope.drawTowerBlock(bx: Float, by: Float, u: Float, variant: Int, night: Boolean) {
    val p = Pen(this, bx, by, u)
    val h = 0.3f + 0.1f * (variant % 4)
    val wall = if (night) Color(0xFF2C3166) else Color(0xFFB9C3E0)
    p.oval(Color(0xFF14172E).copy(alpha = 0.2f), 0.02f, 0f, 0.12f, 0.03f)
    p.rect(wall, -0.07f, 0f, 0.14f, h, 0.01f, edge = true)
    val rows = (h / 0.07f).toInt() - 1
    for (r in 0 until rows) for (c in 0 until 2) {
        val lit = (r * 5 + c * 3 + variant) % 3 != 0
        p.rect(if (night && lit) Color(0xFFFFE08A) else if (night) Color(0xFF191D40) else Color(0xFF8FD3FF), -0.045f + c * 0.055f, 0.03f + r * 0.065f, 0.035f, 0.04f, 0.004f)
    }
    if (variant % 2 == 0) p.line(Ink, 0f, h, 0f, h + 0.07f, 0.008f)
}

/** Des lumières de ville : un petit semis de points jaunes et rouges au sol, la nuit. */
internal fun DrawScope.drawLights(bx: Float, by: Float, u: Float, variant: Int, time: Float) {
    val p = Pen(this, bx, by, u)
    for (k in 0 until 7) {
        val x = ((k * 37 + variant * 11) % 21 - 10) * 0.014f
        val y = ((k * 53 + variant * 7) % 9) * 0.01f
        val on = (k + (time * 1.5f).toInt() * (k % 2)) % 5 != 0
        val c = if (k % 4 == 0) Color(0xFFFF6B5A) else Color(0xFFFFD36B)
        p.circle(c.copy(alpha = 0.25f), x, y, 0.028f)
        p.circle(if (on) c else c.copy(alpha = 0.35f), x, y, 0.011f)
    }
}

internal fun DrawScope.drawGroundDecor(kind: GroundKind, variant: Int, bx: Float, by: Float, u: Float, time: Float, night: Boolean) {
    when (kind) {
        GroundKind.TREE -> drawGroundTree(bx, by, u, variant, night)
        GroundKind.HOUSE -> drawGroundHouse(bx, by, u, variant, night)
        GroundKind.WINDMILL -> drawWindmill(bx, by, u, time, night)
        GroundKind.BOAT -> drawBoat(bx, by, u, variant)
        GroundKind.ISLAND -> drawIsland(bx, by, u, variant)
        GroundKind.WAVES -> drawWaves(bx, by, u, variant)
        GroundKind.CACTUS -> drawCactus(bx, by, u)
        GroundKind.MESA -> drawMesa(bx, by, u, variant)
        GroundKind.TOWER_BLOCK -> drawTowerBlock(bx, by, u, variant, night)
        GroundKind.LIGHTS -> drawLights(bx, by, u, variant, time)
    }
}

// ------------------------------------------------------------------ l'aéroport

/** La tour de contrôle, haute de 0,8 unité : un fût, une cabine vitrée, un phare qui tourne. */
internal fun DrawScope.drawControlTower(bx: Float, by: Float, u: Float, time: Float, night: Boolean) {
    val p = Pen(this, bx, by, u)
    p.oval(Color(0xFF14172E).copy(alpha = 0.22f), 0.03f, 0f, 0.2f, 0.045f)
    p.poly(Color(0xFFE9EDF7), -0.07f, 0f, 0.07f, 0f, 0.045f, 0.5f, -0.045f, 0.5f, edge = true)
    p.poly(Color(0xFFC9D0E4), 0.0f, 0f, 0.07f, 0f, 0.045f, 0.5f, 0.0f, 0.5f)
    p.poly(Color(0xFF3E4A7A), -0.13f, 0.5f, 0.13f, 0.5f, 0.1f, 0.54f, -0.1f, 0.54f, edge = true)
    p.rect(if (night) Color(0xFFFFE08A) else Color(0xFF8FD3FF), -0.1f, 0.54f, 0.2f, 0.08f, 0.01f, edge = true)
    p.shine(-0.05f, 0.6f, 0.03f, 0.012f, 0.6f)
    p.poly(Color(0xFFE8362F), -0.115f, 0.62f, 0.115f, 0.62f, 0.0f, 0.7f, edge = true)
    p.line(Ink, 0f, 0.7f, 0f, 0.78f, 0.01f)
    val on = (time * 2f).toInt() % 2 == 0
    p.circle(if (on) Color(0xFFFF4B4B) else Color(0xFF8A2A2A), 0f, 0.785f, 0.02f)
}

/** Un hangar à toit arrondi, large de 0,6 et haut de 0,28 unité. */
internal fun DrawScope.drawHangar(bx: Float, by: Float, u: Float, night: Boolean) {
    val p = Pen(this, bx, by, u)
    p.oval(Color(0xFF14172E).copy(alpha = 0.22f), 0.03f, 0f, 0.38f, 0.05f)
    p.rect(Color(0xFFDDE3F2), -0.3f, 0f, 0.6f, 0.18f, 0.01f, edge = true)
    p.cap(Color(0xFF8E9AC0), 0f, 0.17f, 0.3f, edge = true)
    p.rect(Color(0xFF3E4A7A), -0.18f, 0f, 0.36f, 0.17f, 0.01f)
    p.rect(if (night) Color(0xFFFFE08A) else Color(0xFF5A6896), -0.17f, 0.005f, 0.34f, 0.15f, 0.008f)
    for (k in 1..3) p.line(Color(0xFF3E4A7A), -0.18f + k * 0.09f, 0.005f, -0.18f + k * 0.09f, 0.155f, 0.007f)
}

/** Une manche à air, haute de 0,35 unité, qui flotte au vent. */
internal fun DrawScope.drawWindsock(bx: Float, by: Float, u: Float, time: Float) {
    val p = Pen(this, bx, by, u)
    p.rect(Color(0xFFE9EDF7), -0.01f, 0f, 0.02f, 0.3f, 0.01f)
    val wob = sin(time * 3f) * 0.012f
    for (k in 0 until 4) {
        val x0 = 0.01f + k * 0.045f
        p.poly(if (k % 2 == 0) Color(0xFFFF8A1F) else Color.White, x0, 0.3f - k * 0.008f + wob * k, x0 + 0.045f, 0.292f - k * 0.008f + wob * (k + 1), x0 + 0.045f, 0.262f - k * 0.008f + wob * (k + 1), x0, 0.27f - k * 0.008f + wob * k)
    }
}

/** Un petit avion garé, de face, large de 0,2 : celui des autres, au repos. */
internal fun DrawScope.drawParkedPlane(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    p.oval(Color(0xFF14172E).copy(alpha = 0.22f), 0f, 0f, 0.14f, 0.03f)
    drawOncomingPlaneAt(bx, by - 0.1f * u, u * 0.8f, variant)
}

private fun DrawScope.drawOncomingPlaneAt(cx: Float, cy: Float, u: Float, variant: Int) = drawOncomingPlane(cx, cy, u, variant, 0f)

