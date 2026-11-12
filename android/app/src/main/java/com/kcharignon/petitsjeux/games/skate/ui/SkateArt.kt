package com.kcharignon.petitsjeux.games.skate.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import com.kcharignon.petitsjeux.games.skate.engine.PieceKind
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Les dessins de Super Skate : tout est dessiné par le code, à plat, de face (ou de dos pour le skateur).
 * Chaque dessin se pose sur un point du sol `(baseX, baseY)` à l'écran, avec [u] pixels par « unité de rue » à cette distance :
 * on dessine en unités de rue, y vers le haut, l'origine aux pieds.
 */

private val Ink = Color(0xFF2B2D42)
private val ShadowColor = Color(0xFF14172E)

/** Un pinceau qui dessine en unités de rue (y vers le haut) à partir d'un point du sol. */
internal class Pen(val s: DrawScope, val bx: Float, val by: Float, val u: Float) {
    private fun px(x: Float) = bx + x * u
    private fun py(y: Float) = by - y * u

    /** Un rectangle arrondi dont (x, y) est le coin bas gauche. */
    fun rect(color: Color, x: Float, y: Float, w: Float, h: Float, r: Float = 0f) =
        s.drawRoundRect(color, Offset(px(x), py(y + h)), Size(w * u, h * u), CornerRadius(r * u))

    fun circle(color: Color, cx: Float, cy: Float, r: Float) = s.drawCircle(color, r * u, Offset(px(cx), py(cy)))

    fun oval(color: Color, cx: Float, cy: Float, rx: Float, ry: Float) =
        s.drawOval(color, Offset(px(cx - rx), py(cy + ry)), Size(rx * 2 * u, ry * 2 * u))

    fun poly(color: Color, vararg p: Float) {
        val path = Path()
        for (i in 0 until p.size / 2) if (i == 0) path.moveTo(px(p[0]), py(p[1])) else path.lineTo(px(p[2 * i]), py(p[2 * i + 1]))
        path.close()
        s.drawPath(path, color)
    }

    fun line(color: Color, x1: Float, y1: Float, x2: Float, y2: Float, w: Float) =
        s.drawLine(color, Offset(px(x1), py(y1)), Offset(px(x2), py(y2)), strokeWidth = (w * u).coerceAtLeast(1f), cap = StrokeCap.Round)

    /** Un demi-disque (le dessus d'une tête, d'un casque) : le haut du cercle de centre (cx, cy). */
    fun cap(color: Color, cx: Float, cy: Float, r: Float) {
        val path = Path().apply {
            moveTo(px(cx - r), py(cy))
            arcTo(Rect(px(cx - r), py(cy + r), px(cx + r), py(cy - r)), 180f, 180f, false)
            close()
        }
        s.drawPath(path, color)
    }
}

/** Dessine [block] avec une transparence [alpha] (0 à 1) : les choses qui s'effacent, le skateur qui clignote. */
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

/** L'ombre ronde sous une chose posée au sol, de demi-largeur [halfWidth] (unités de rue) ; plus on est haut, plus elle est petite et pâle. */
internal fun DrawScope.drawGroundShadow(bx: Float, by: Float, u: Float, halfWidth: Float, lift: Float = 0f, strength: Float = 0.28f) {
    val k = 1f / (1f + lift * 3f)
    drawOval(
        ShadowColor.copy(alpha = strength * k), Offset(bx - halfWidth * u * k, by - halfWidth * u * 0.28f * k), Size(halfWidth * 2f * u * k, halfWidth * u * 0.56f * k),
    )
}

// ------------------------------------------------------------------ les piétons

private class Look(val shirt: Color, val shirtDark: Color, val pants: Color, val hair: Color, val skin: Color, val accessory: Int)

private val Looks = listOf(
    Look(Color(0xFFFF5A4A), Color(0xFFD63E30), Color(0xFF3A4A7A), Color(0xFF3B2A20), Color(0xFFFFD2A6), 0),
    Look(Color(0xFF3E8BFF), Color(0xFF2B66D1), Color(0xFF4A4F5C), Color(0xFFF2C14E), Color(0xFFE8A97A), 1),
    Look(Color(0xFF4CD964), Color(0xFF2FA845), Color(0xFF8A5A3C), Color(0xFF1F1A1A), Color(0xFFB97A4F), 2),
    Look(Color(0xFFFFB020), Color(0xFFD68A00), Color(0xFF2B3A55), Color(0xFFC8502A), Color(0xFFFFD2A6), 3),
    Look(Color(0xFFB36BE8), Color(0xFF8A47C0), Color(0xFF3F4B3E), Color(0xFF8E8E9A), Color(0xFF8A5A3C), 4),
)

/**
 * Un piéton, de face, haut de 0,75 unité. [walk] : -1 à 1, le balancement des jambes et des bras (0 = à l'arrêt) ; [surprise] : 0 à 1, bouche et yeux ronds quand le skateur approche.
 */
internal fun DrawScope.drawPedestrian(bx: Float, by: Float, u: Float, variant: Int, walk: Float, surprise: Float) {
    val look = Looks[variant.mod(Looks.size)]
    val p = Pen(this, bx, by, u)
    val swing = walk * 0.055f
    // un sac à dos qui dépasse derrière
    if (look.accessory == 2) p.rect(Color(0xFF2B2D42), -0.125f, 0.28f, 0.25f, 0.24f, 0.05f)
    // les jambes
    for (side in listOf(-1f, 1f)) {
        val x0 = side * 0.04f
        val dx = side * swing
        p.poly(look.pants, x0 - 0.035f, 0.27f, x0 + 0.035f, 0.27f, x0 + 0.035f + dx, 0.03f, x0 - 0.035f + dx, 0.03f)
        p.rect(Color(0xFFF4F1E4), x0 - 0.045f + dx, 0f, 0.09f, 0.045f, 0.02f)
    }
    // les bras, derrière le corps
    for (side in listOf(-1f, 1f)) {
        val dx = -side * swing * 0.7f
        p.line(look.shirtDark, side * 0.115f, 0.5f, side * 0.14f + dx, 0.31f, 0.055f)
        p.circle(look.skin, side * 0.14f + dx, 0.285f, 0.028f)
    }
    // le corps
    p.rect(look.shirt, -0.115f, 0.24f, 0.23f, 0.3f, 0.07f)
    p.rect(look.shirtDark, -0.115f, 0.24f, 0.23f, 0.07f, 0.04f)
    // la tête
    p.circle(look.hair, 0f, 0.655f, 0.092f)
    p.circle(look.skin, 0f, 0.635f, 0.082f)
    when (look.accessory) {
        1 -> { // une casquette
            p.cap(look.shirt, 0f, 0.665f, 0.092f)
            p.rect(look.shirt, -0.11f, 0.655f, 0.22f, 0.02f, 0.01f)
        }
        3 -> { // une queue de cheval
            p.circle(look.hair, 0.1f, 0.62f, 0.035f)
            p.cap(look.hair, 0f, 0.655f, 0.09f)
        }
        else -> p.cap(look.hair, 0f, 0.655f, 0.09f)
    }
    if (look.accessory == 4) { // un téléphone dans la main
        p.rect(Color(0xFF2B2D42), 0.12f, 0.3f, 0.035f, 0.06f, 0.01f)
    }
    // le visage : des yeux, une bouche qui s'arrondit quand il voit arriver le skateur
    val eye = 0.013f + 0.005f * surprise
    p.circle(Ink, -0.033f, 0.642f, eye)
    p.circle(Ink, 0.033f, 0.642f, eye)
    if (surprise > 0.6f) {
        p.oval(Ink, 0f, 0.598f, 0.011f, 0.013f)
    } else { // un petit sourire
        p.line(Ink, -0.024f, 0.607f, 0f, 0.594f, 0.01f)
        p.line(Ink, 0f, 0.594f, 0.024f, 0.607f, 0.01f)
    }
}

// ------------------------------------------------------------------ le mobilier

/** Un lampadaire, de face, haut de 1,45 unité : un mât, une potence et une lanterne qui brille (plus la nuit). */
internal fun DrawScope.drawLamp(bx: Float, by: Float, u: Float, night: Boolean) {
    val p = Pen(this, bx, by, u)
    val pole = Color(0xFF4B5068)
    p.rect(Color(0xFF383C52), -0.055f, 0f, 0.11f, 0.07f, 0.03f)
    p.rect(pole, -0.018f, 0.05f, 0.036f, 1.28f, 0.015f)
    p.rect(pole, -0.018f, 1.28f, 0.17f, 0.032f, 0.015f)
    if (night) p.circle(Color(0xFFFFE9A0).copy(alpha = 0.28f), 0.15f, 1.22f, 0.2f)
    p.rect(Color(0xFF383C52), 0.085f, 1.275f, 0.13f, 0.03f, 0.01f)
    p.rect(Color(0xFFFFE066), 0.095f, 1.17f, 0.11f, 0.1f, 0.04f)
    p.rect(Color(0xFFFFF6B8), 0.11f, 1.2f, 0.04f, 0.05f, 0.02f)
}

/** Un plot de chantier, haut de 0,3 unité. [variant] : orange, orange rouge, jaune. */
internal fun DrawScope.drawCone(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    val body = listOf(Color(0xFFFF8A1F), Color(0xFFFF5A3A), Color(0xFFFFC21F))[variant.mod(3)]
    p.rect(Color(0xFF3A3D52), -0.075f, 0f, 0.15f, 0.03f, 0.012f)
    p.poly(body, -0.058f, 0.03f, 0.058f, 0.03f, 0.016f, 0.3f, -0.016f, 0.3f)
    p.poly(Color.White, -0.043f, 0.1f, 0.043f, 0.1f, 0.034f, 0.15f, -0.034f, 0.15f)
    p.poly(Color.White, -0.03f, 0.19f, 0.03f, 0.19f, 0.024f, 0.225f, -0.024f, 0.225f)
}

/** Une poubelle, haute de 0,42 unité. [variant] : verte, bleue, jaune. */
internal fun DrawScope.drawBin(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    val body = listOf(Color(0xFF3FBF6A), Color(0xFF3E8BFF), Color(0xFFFFC21F))[variant.mod(3)]
    val dark = listOf(Color(0xFF2A9A52), Color(0xFF2B66D1), Color(0xFFD99A00))[variant.mod(3)]
    p.poly(dark, -0.075f, 0f, 0.075f, 0f, 0.085f, 0.36f, -0.085f, 0.36f)
    p.poly(body, -0.06f, 0.02f, 0.06f, 0.02f, 0.072f, 0.35f, -0.072f, 0.35f)
    p.rect(Color(0xFF2B2D42).copy(alpha = 0.25f), -0.05f, 0.1f, 0.1f, 0.012f, 0.005f)
    p.rect(Color(0xFF2B2D42).copy(alpha = 0.25f), -0.055f, 0.2f, 0.11f, 0.012f, 0.005f)
    p.rect(dark, -0.1f, 0.35f, 0.2f, 0.05f, 0.025f)
    p.rect(Color.White.copy(alpha = 0.5f), -0.04f, 0.385f, 0.08f, 0.012f, 0.006f)
}

/** Un banc, large de 0,52 et haut de 0,4 unité : le dossier à lattes et l'assise sur deux pieds. */
internal fun DrawScope.drawBench(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    val wood = listOf(Color(0xFFC98B54), Color(0xFF8FBF5A), Color(0xFF6FA8E8))[variant.mod(3)]
    val woodDark = listOf(Color(0xFFA96E3B), Color(0xFF6E9B3F), Color(0xFF4F86C4))[variant.mod(3)]
    val iron = Color(0xFF3A3D52)
    p.rect(iron, -0.23f, 0f, 0.035f, 0.2f, 0.01f)
    p.rect(iron, 0.195f, 0f, 0.035f, 0.2f, 0.01f)
    for (k in 0 until 3) p.rect(if (k % 2 == 0) wood else woodDark, -0.25f, 0.27f + k * 0.045f, 0.5f, 0.037f, 0.01f)
    p.rect(iron, -0.23f, 0.2f, 0.035f, 0.2f, 0.01f)
    p.rect(iron, 0.195f, 0.2f, 0.035f, 0.2f, 0.01f)
    p.rect(woodDark, -0.26f, 0.17f, 0.52f, 0.05f, 0.015f)
}

/** Une barrière de chantier, large de 0,64 et haute de 0,36 unité : deux planches rayées rouge et blanc, un feu orange. */
internal fun DrawScope.drawBarrier(bx: Float, by: Float, u: Float, time: Float) {
    val p = Pen(this, bx, by, u)
    val iron = Color(0xFF3A3D52)
    p.rect(iron, -0.27f, 0f, 0.035f, 0.3f, 0.01f)
    p.rect(iron, 0.235f, 0f, 0.035f, 0.3f, 0.01f)
    p.rect(iron, -0.3f, 0f, 0.1f, 0.025f, 0.01f)
    p.rect(iron, 0.2f, 0f, 0.1f, 0.025f, 0.01f)
    for ((k, y) in listOf(0.12f, 0.2f).withIndex()) {
        p.rect(Color.White, -0.32f, y, 0.64f, 0.065f, 0.012f)
        val stripes = 8
        for (i in 0 until stripes) {
            if ((i + k) % 2 == 0) continue
            val x0 = -0.32f + i * 0.64f / stripes
            p.poly(Color(0xFFE8362F), x0, y, x0 + 0.04f, y, x0 + 0.08f, y + 0.065f, x0 + 0.04f, y + 0.065f)
        }
    }
    val on = (time * 3f).toInt() % 2 == 0
    p.circle(if (on) Color(0xFFFFB020) else Color(0xFFB87800), 0f, 0.31f, 0.035f)
    p.circle(Color(0xFFFFB020).copy(alpha = if (on) 0.3f else 0f), 0f, 0.31f, 0.075f)
    p.rect(iron, -0.012f, 0.265f, 0.024f, 0.04f, 0.005f)
}

/** Le dessin d'une pièce solide posée au sol (hors piéton, qui a ses propres réglages). */
internal fun DrawScope.drawPiece(kind: PieceKind, variant: Int, bx: Float, by: Float, u: Float, time: Float, night: Boolean) {
    when (kind) {
        PieceKind.LAMP -> drawLamp(bx, by, u, night)
        PieceKind.CONE -> drawCone(bx, by, u, variant)
        PieceKind.BIN -> drawBin(bx, by, u, variant)
        PieceKind.BENCH -> drawBench(bx, by, u, variant)
        PieceKind.BARRIER -> drawBarrier(bx, by, u, time)
        PieceKind.PEDESTRIAN, PieceKind.RAMP -> Unit
    }
}

/** La demi-largeur de l'ombre d'une pièce. */
internal fun PieceKind.shadowHalfWidth(): Float = when (this) {
    PieceKind.PEDESTRIAN -> 0.13f
    PieceKind.LAMP -> 0.08f
    PieceKind.CONE -> 0.09f
    PieceKind.BIN -> 0.11f
    PieceKind.BENCH -> 0.29f
    PieceKind.BARRIER -> 0.33f
    PieceKind.RAMP -> 0f
}

// ------------------------------------------------------------------ le skateur

/**
 * Le skateur, de dos (de face quand il se retourne en l'air) : planche, jambes, sweat, sac à dos et casque de la couleur de son animal.
 * [lean] : l'inclinaison en degrés (vers la droite si positif) ; [spin] : 0 à 1, un tour complet sur lui-même pendant le saut ; [squash] : l'écrasement à la réception.
 */
internal fun DrawScope.drawSkater(bx: Float, by: Float, u: Float, helmet: Color, lean: Float, spin: Float, squash: Float, time: Float) {
    val angle = spin * 2f * PI.toFloat()
    val turn = cos(angle)
    val front = turn < 0f
    val width = abs(turn).coerceAtLeast(0.4f)
    rotate(lean, Offset(bx, by)) {
        scale(width, 1f - 0.16f * squash, Offset(bx, by)) {
            val p = Pen(this, bx, by, u)
            val hoodie = Color(0xFF3E8BFF)
            val hoodieDark = Color(0xFF2B66D1)
            val jeans = Color(0xFF34406B)
            val skin = Color(0xFFFFD2A6)
            // la planche : un plateau orange, deux roues blanches, des essieux
            val roll = sin(time * 26f) * 0.004f
            p.rect(Color(0xFF3A3D52), -0.145f, 0.015f + roll, 0.04f, 0.026f, 0.01f)
            p.rect(Color(0xFF3A3D52), 0.105f, 0.015f - roll, 0.04f, 0.026f, 0.01f)
            p.circle(Color(0xFFF4F1E4), -0.125f, 0.032f + roll, 0.034f)
            p.circle(Color(0xFFF4F1E4), 0.125f, 0.032f - roll, 0.034f)
            p.circle(Color(0xFFC9C6B8), -0.125f, 0.032f + roll, 0.014f)
            p.circle(Color(0xFFC9C6B8), 0.125f, 0.032f - roll, 0.014f)
            p.rect(Color(0xFFFF8A1F), -0.18f, 0.05f, 0.36f, 0.04f, 0.02f)
            p.rect(Color(0xFFFFC21F), -0.15f, 0.07f, 0.3f, 0.012f, 0.006f)
            // les jambes et les baskets
            for (side in listOf(-1f, 1f)) {
                p.rect(jeans, side * 0.065f - 0.04f, 0.1f, 0.08f, 0.2f, 0.03f)
                p.rect(Color(0xFFF4F1E4), side * 0.065f - 0.05f, 0.085f, 0.1f, 0.05f, 0.025f)
            }
            // les bras écartés pour l'équilibre
            for (side in listOf(-1f, 1f)) {
                p.line(if (front) hoodie else hoodieDark, side * 0.1f, 0.5f, side * 0.2f, 0.4f, 0.06f)
                p.circle(skin, side * 0.2f, 0.385f, 0.03f)
            }
            if (!front) p.rect(Color(0xFFFF5A4A), -0.075f, 0.3f, 0.15f, 0.19f, 0.05f) // le sac à dos
            p.rect(hoodie, -0.105f, 0.27f, 0.21f, 0.27f, 0.06f)
            p.rect(hoodieDark, -0.105f, 0.27f, 0.21f, 0.06f, 0.04f)
            if (front) {
                p.rect(hoodieDark, -0.012f, 0.3f, 0.024f, 0.22f, 0.01f)
                p.rect(hoodieDark, -0.06f, 0.29f, 0.12f, 0.06f, 0.025f)
            }
            // la tête, le casque
            p.circle(skin, 0f, 0.6f, 0.078f)
            if (front) {
                p.circle(Ink, -0.03f, 0.605f, 0.012f)
                p.circle(Ink, 0.03f, 0.605f, 0.012f)
                p.oval(Ink, 0f, 0.572f, 0.022f, 0.011f)
                p.cap(helmet, 0f, 0.625f, 0.09f)
            } else {
                p.circle(helmet, 0f, 0.625f, 0.092f)
                p.rect(Color(0xFF14172E).copy(alpha = 0.18f), -0.012f, 0.6f, 0.024f, 0.115f, 0.01f)
                p.rect(skin, -0.04f, 0.53f, 0.08f, 0.04f, 0.02f)
            }
            p.rect(Color.White.copy(alpha = 0.35f), -0.05f, 0.665f, 0.06f, 0.014f, 0.007f)
        }
    }
}

// ------------------------------------------------------------------ le décor

private val Facades = listOf(
    Color(0xFFFFB3A0), Color(0xFFFFE08A), Color(0xFF9ED8FF), Color(0xFFB9E8A0), Color(0xFFE0B8F5), Color(0xFFFFC98A),
)

/** Un immeuble de face (haut de 2,4 à 3,6 unités, large de 1,3), de jour ou de nuit (fenêtres allumées). */
internal fun DrawScope.drawBuilding(bx: Float, by: Float, u: Float, variant: Int, night: Boolean) {
    val p = Pen(this, bx, by, u)
    val h = 2.4f + (variant % 4) * 0.4f
    val facade = if (night) Color(0xFF353A6B) else Facades[variant.mod(Facades.size)]
    val shade = if (night) Color(0xFF2A2E58) else facade.copy(alpha = 1f).let { Color(it.red * 0.86f, it.green * 0.86f, it.blue * 0.86f) }
    p.rect(shade, -0.65f, 0f, 1.3f, h, 0.04f)
    p.rect(facade, -0.62f, 0f, 1.24f, h - 0.04f, 0.04f)
    p.rect(shade, -0.7f, h - 0.04f, 1.4f, 0.1f, 0.03f)
    val cols = 3
    val rows = (h / 0.55f).toInt() - 1
    for (r in 0 until rows) for (c in 0 until cols) {
        val lit = night && ((r * 7 + c * 3 + variant) % 3 != 0)
        val glass = if (night) (if (lit) Color(0xFFFFE08A) else Color(0xFF1E2146)) else Color(0xFF7FB6E8)
        p.rect(glass, -0.5f + c * 0.35f, 0.62f + r * 0.5f, 0.24f, 0.32f, 0.03f)
    }
    p.rect(if (night) Color(0xFF1E2146) else Color(0xFF6B4A3A), -0.12f, 0f, 0.24f, 0.4f, 0.04f)
}

/** Un arbre rond, haut de 1,6 unité. */
internal fun DrawScope.drawTree(bx: Float, by: Float, u: Float, variant: Int, night: Boolean) {
    val p = Pen(this, bx, by, u)
    val leaf = if (night) Color(0xFF245A48) else listOf(Color(0xFF3FBF4A), Color(0xFF55CC4A), Color(0xFF2FA845))[variant.mod(3)]
    val leafDark = if (night) Color(0xFF1B4838) else Color(0xFF2A9A3C)
    p.rect(Color(0xFF8A5A3C), -0.05f, 0f, 0.1f, 0.7f, 0.03f)
    p.circle(leafDark, 0.02f, 1.0f, 0.5f)
    p.circle(leaf, -0.18f, 1.1f, 0.38f)
    p.circle(leaf, 0.2f, 1.12f, 0.36f)
    p.circle(leaf, 0f, 1.32f, 0.36f)
}

/** Un palmier, haut de 1,9 unité. */
internal fun DrawScope.drawPalm(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    val trunk = Color(0xFF9A6A3E)
    val lean = if (variant % 2 == 0) 0.1f else -0.1f
    p.line(trunk, 0f, 0f, lean, 1.45f, 0.1f)
    p.line(Color(0xFFB4824F), -0.01f, 0.2f, lean - 0.01f, 1.4f, 0.03f)
    val top = Offset(lean, 1.5f)
    for ((i, a) in listOf(-160f, -120f, -75f, -35f, 5f, 40f).withIndex()) {
        val rad = a * PI.toFloat() / 180f
        val len = 0.62f
        val x2 = top.x + cos(rad) * len
        val y2 = top.y + sin(rad) * len * 0.8f + 0.12f
        p.line(if (i % 2 == 0) Color(0xFF2FA845) else Color(0xFF3FBF4A), top.x, top.y, x2, y2, 0.11f)
    }
    p.circle(Color(0xFF6B4A2E), top.x - 0.04f, 1.44f, 0.04f)
    p.circle(Color(0xFF6B4A2E), top.x + 0.04f, 1.43f, 0.04f)
}

/** Un parasol de plage, haut de 1,1 unité. */
internal fun DrawScope.drawUmbrella(bx: Float, by: Float, u: Float, variant: Int) {
    val p = Pen(this, bx, by, u)
    val a = listOf(Color(0xFFFF5A4A), Color(0xFF3E8BFF), Color(0xFFFFC21F))[variant.mod(3)]
    p.line(Color(0xFF8A5A3C), 0f, 0f, 0f, 0.85f, 0.035f)
    p.cap(a, 0f, 0.85f, 0.45f)
    p.cap(Color.White, 0f, 0.85f, 0.28f)
    p.cap(a, 0f, 0.85f, 0.14f)
    p.rect(a, -0.45f, 0.84f, 0.9f, 0.02f, 0.01f)
}

/** Un buisson bas, haut de 0,45 unité. */
internal fun DrawScope.drawBush(bx: Float, by: Float, u: Float, variant: Int, night: Boolean) {
    val p = Pen(this, bx, by, u)
    val leaf = if (night) Color(0xFF245A48) else if (variant % 2 == 0) Color(0xFF3FBF4A) else Color(0xFF55CC4A)
    p.circle(leaf, -0.2f, 0.2f, 0.22f)
    p.circle(leaf, 0.2f, 0.2f, 0.22f)
    p.circle(if (night) Color(0xFF2C6B55) else Color(0xFF5ED86A), 0f, 0.26f, 0.26f)
    if (!night) for (k in 0 until 3) p.circle(listOf(Color(0xFFFF6FA8), Color(0xFFFFE066), Color.White)[(k + variant) % 3], -0.15f + k * 0.15f, 0.2f + (k % 2) * 0.1f, 0.025f)
}

/** Un lampadaire de décor (le long du trottoir) : plus mince et plus haut que ceux de la rue, avec une lanterne ronde. */
internal fun DrawScope.drawStreetLight(bx: Float, by: Float, u: Float, night: Boolean) {
    val p = Pen(this, bx, by, u)
    p.rect(Color(0xFF5B6079), -0.02f, 0f, 0.04f, 1.7f, 0.015f)
    if (night) p.circle(Color(0xFFFFE9A0).copy(alpha = 0.3f), 0f, 1.78f, 0.34f)
    p.circle(Color(0xFFFFE9A0), 0f, 1.78f, 0.1f)
    p.circle(Color.White.copy(alpha = 0.7f), -0.025f, 1.805f, 0.035f)
}

internal fun DrawScope.drawDecor(kind: DecorKind, variant: Int, bx: Float, by: Float, u: Float, night: Boolean) {
    when (kind) {
        DecorKind.BUILDING -> drawBuilding(bx, by, u, variant, night)
        DecorKind.TREE -> drawTree(bx, by, u, variant, night)
        DecorKind.PALM -> drawPalm(bx, by, u, variant)
        DecorKind.UMBRELLA -> drawUmbrella(bx, by, u, variant)
        DecorKind.BUSH -> drawBush(bx, by, u, variant, night)
        DecorKind.LAMP -> drawStreetLight(bx, by, u, night)
    }
}
