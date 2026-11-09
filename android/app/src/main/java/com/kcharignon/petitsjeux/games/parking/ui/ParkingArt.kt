package com.kcharignon.petitsjeux.games.parking.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.kcharignon.petitsjeux.core.ui.Pj

/**
 * Les dessins de Parking Malin : voitures, plateau, plots, panneau P. Mêmes proportions que les maquettes (tools/design/ds.js,
 * docs/design/00-planche-style.png). Taille de référence : [s] = côté d'une case, en pixels.
 */

private val Glass = Pj.Glass
private val Tire = Color(0xFF262938)
private val Headlight = Color(0xFFFFF6B8)
private val Taillight = Color(0xFFE8352B)

private fun darken(c: Color, t: Float) = lerp(c, Color.Black, t)
private fun lighten(c: Color, t: Float) = lerp(c, Color.White, t)

/**
 * Dessine une voiture de [len] cases (2 ou 3) centrée en [center], le nez tourné de [degrees] (0 = vers le haut).
 * [flash] (0 à 1) rougit la carrosserie (faute) ; [alpha] la fait disparaître (sortie).
 * La « tranche » sombre est décalée vers le bas de l'ÉCRAN, jamais avec la rotation : la lumière vient toujours d'en haut.
 */
fun DrawScope.drawCar(len: Int, color: Color, s: Float, center: Offset, degrees: Float, flash: Float = 0f, alpha: Float = 1f, shadow: Boolean = true) {
    if (alpha <= 0.01f) return
    val faded = alpha < 0.99f
    if (faded) {
        val half = s * (len + 1)
        drawIntoCanvas { it.saveLayer(Rect(center.x - half, center.y - half, center.x + half, center.y + half), Paint().apply { this.alpha = alpha }) }
    }
    val horizontal = degrees == 90f || degrees == 270f
    val w = if (horizontal) len * s else s
    val h = if (horizontal) s else len * s
    if (shadow) {
        drawOval(Color.Black.copy(alpha = 0.18f), Offset(center.x - (w / 2 - s * 0.02f), center.y + s * 0.2f - (h / 2 - s * 0.02f)), Size(w - s * 0.04f, h - s * 0.04f))
    }
    val body = lerp(color, Color(0xFFFF2D1F), 0.55f * flash)
    val width = s * 0.8f
    val length = s * len - s * 0.2f
    val depth = s * 0.11f
    translate(center.x, center.y) {
        translate(0f, depth) { rotate(degrees, Offset.Zero) { drawLowerLayer(body, width, length, s) } }
        rotate(degrees, Offset.Zero) { drawUpperLayer(len, body, width, length, s) }
    }
    if (faded) drawIntoCanvas { it.restore() }
}

private fun DrawScope.wheels(width: Float, length: Float, s: Float, dy: Float) {
    val wy = length / 2 - s * 0.34f
    for (x in floatArrayOf(-width / 2, width / 2)) for (y in floatArrayOf(-wy, wy)) {
        drawRoundRect(Tire, Offset(x - s * 0.07f, y + dy - s * 0.15f), Size(s * 0.14f, s * 0.3f), CornerRadius(s * 0.06f))
    }
}

private fun DrawScope.drawLowerLayer(body: Color, width: Float, length: Float, s: Float) {
    wheels(width, length, s, 0f)
    drawRoundRect(darken(body, 0.38f), Offset(-width / 2, -length / 2), Size(width, length), CornerRadius(s * 0.27f))
}

private fun DrawScope.drawUpperLayer(len: Int, body: Color, width: Float, length: Float, s: Float) {
    val depth = s * 0.11f
    wheels(width, length, s, -depth * 0.2f)
    drawRoundRect(
        Brush.linearGradient(
            0f to lighten(body, 0.28f), 0.55f to body, 1f to darken(body, 0.1f),
            start = Offset(-width / 2, -length / 2), end = Offset(-width / 2 + width * 0.35f, length / 2),
        ),
        Offset(-width / 2, -length / 2), Size(width, length), CornerRadius(s * 0.27f),
    )
    // capot brillant
    drawOval(Color.White.copy(alpha = 0.4f), Offset(-width * 0.38f, -length / 2 + s * 0.13f), Size(width * 0.4f, s * 0.14f))

    // toit
    val cabinLen = if (len == 2) length * 0.46f else length * 0.66f
    val cabinW = width * 0.74f
    val cabinY = if (len == 2) length * 0.06f else length * 0.04f
    val top = cabinY - cabinLen / 2
    drawRoundRect(lighten(body, 0.12f), Offset(-cabinW / 2, top), Size(cabinW, cabinLen), CornerRadius(s * 0.17f))
    drawRoundRect(darken(body, 0.14f), Offset(-cabinW / 2, top), Size(cabinW, cabinLen), CornerRadius(s * 0.17f), style = Stroke(s * 0.03f))
    // pare-brise
    drawPath(
        Path().apply {
            moveTo(-cabinW / 2 + s * 0.06f, top + s * 0.2f)
            quadraticTo(0f, top - s * 0.02f, cabinW / 2 - s * 0.06f, top + s * 0.2f)
            lineTo(cabinW / 2 - s * 0.1f, top + s * 0.42f)
            lineTo(-cabinW / 2 + s * 0.1f, top + s * 0.42f)
            close()
        },
        Glass,
    )
    // reflets sur le toit : une bande (voiture) ou deux (minibus)
    val y0 = top + s * 0.42f
    val y1 = cabinY + cabinLen / 2 - s * 0.26f
    val shine = lighten(body, 0.3f).copy(alpha = 0.5f)
    if (len == 3) {
        drawRoundRect(shine, Offset(-cabinW / 2 + s * 0.11f, y0 + (y1 - y0) * 0.12f), Size(cabinW - s * 0.22f, (y1 - y0) * 0.34f), CornerRadius(s * 0.05f))
        drawRoundRect(shine, Offset(-cabinW / 2 + s * 0.11f, y0 + (y1 - y0) * 0.54f), Size(cabinW - s * 0.22f, (y1 - y0) * 0.34f), CornerRadius(s * 0.05f))
    } else {
        drawRoundRect(shine, Offset(-cabinW / 2 + s * 0.14f, top + s * 0.52f), Size(cabinW - s * 0.28f, cabinLen - s * 0.52f - s * 0.32f), CornerRadius(s * 0.06f))
    }
    // lunette arrière
    drawRoundRect(Glass, Offset(-cabinW / 2 + s * 0.09f, cabinY + cabinLen / 2 - s * 0.26f), Size(cabinW - s * 0.18f, s * 0.14f), CornerRadius(s * 0.05f))
    // phares et feux arrière
    drawCircle(Headlight, s * 0.075f, Offset(-width * 0.3f, -length / 2 + s * 0.1f))
    drawCircle(Headlight, s * 0.075f, Offset(width * 0.3f, -length / 2 + s * 0.1f))
    drawRoundRect(Taillight, Offset(-width * 0.4f, length / 2 - s * 0.13f), Size(s * 0.17f, s * 0.07f), CornerRadius(s * 0.03f))
    drawRoundRect(Taillight, Offset(width * 0.4f - s * 0.17f, length / 2 - s * 0.13f), Size(s * 0.17f, s * 0.07f), CornerRadius(s * 0.03f))
}

/** Un plot orange et blanc dont le pied est centré en ([cx], [cy]). */
fun DrawScope.drawCone(cx: Float, cy: Float, s: Float) {
    val b = s * 0.34f
    drawOval(Color.Black.copy(alpha = 0.2f), Offset(cx - b * 0.95f, cy + b * 0.25f), Size(b * 1.9f, b * 0.8f))
    drawRoundRect(Color(0xFFE8641E), Offset(cx - b, cy + b * 0.35f), Size(b * 2f, b * 0.5f), CornerRadius(b * 0.2f))
    drawPath(
        Path().apply {
            moveTo(cx - b * 0.7f, cy + b * 0.4f)
            lineTo(cx - b * 0.18f, cy - b * 1.15f)
            quadraticTo(cx, cy - b * 1.3f, cx + b * 0.18f, cy - b * 1.15f)
            lineTo(cx + b * 0.7f, cy + b * 0.4f)
            close()
        },
        Color(0xFFFF8A3D),
    )
    drawPath(
        Path().apply {
            moveTo(cx - b * 0.46f, cy - b * 0.35f); lineTo(cx + b * 0.46f, cy - b * 0.35f)
            lineTo(cx + b * 0.56f, cy); lineTo(cx - b * 0.56f, cy); close()
        },
        Color.White,
    )
}

/** Le panneau « P » bleu sur son poteau : (cx, cy) = centre du panneau. */
fun DrawScope.drawParkingSign(cx: Float, cy: Float, s: Float) {
    drawRoundRect(Color(0xFF8E96AB), Offset(cx - s * 0.06f, cy), Size(s * 0.12f, s * 0.85f), CornerRadius(s * 0.04f))
    drawRoundRect(Color(0xFF2D6BD8), Offset(cx - s * 0.4f, cy - s * 0.6f), Size(s * 0.8f, s * 0.8f), CornerRadius(s * 0.2f))
    drawRoundRect(Color.White, Offset(cx - s * 0.34f, cy - s * 0.54f), Size(s * 0.68f, s * 0.68f), CornerRadius(s * 0.15f), style = Stroke(s * 0.04f))
    // la lettre P, dessinée à la main : une hampe et une panse
    val stem = s * 0.085f
    val x0 = cx - s * 0.13f
    val y0 = cy - s * 0.4f
    drawRoundRect(Color.White, Offset(x0, y0), Size(stem, s * 0.5f), CornerRadius(stem / 2))
    val bowl = s * 0.4f
    drawArc(Color.White, -90f, 180f, false, Offset(x0 + stem / 2 - bowl / 2, y0 + stem / 2), Size(bowl, bowl), style = Stroke(stem))
}

/** Couleurs du plateau pour chaque monde : dessus, tranche, case claire, case foncée. */
class BoardTheme(val top: Color, val side: Color, val tileA: Color, val tileB: Color) {
    companion object {
        val worlds = listOf(
            BoardTheme(Color(0xFF7C879B), Color(0xFF5B667B), Color(0xFF8E99AD), Color(0xFF8590A5)), // asphalte bleu-gris
            BoardTheme(Color(0xFF9A8B7C), Color(0xFF75685A), Color(0xFFAB9D8E), Color(0xFFA29383)), // asphalte chaud
            BoardTheme(Color(0xFF6F9A86), Color(0xFF4F7766), Color(0xFF82AD99), Color(0xFF79A490)), // vert
            BoardTheme(Color(0xFF8A7FA8), Color(0xFF665D86), Color(0xFF9C92BB), Color(0xFF9389B1)), // violet
            BoardTheme(Color(0xFF50586B), Color(0xFF353B4B), Color(0xFF626B80), Color(0xFF596277)), // sombre
        )

        fun of(world: Int) = worlds[world.coerceIn(0, worlds.lastIndex)]
    }
}

/** Où est le plateau dans la zone de dessin : une case = [s] pixels, la case (0,0) a son coin haut gauche en [origin]. */
class BoardGeometry(val cols: Int, val rows: Int, val s: Float, val origin: Offset) {
    val pad: Float get() = s * 0.19f
    val slab: Rect get() = Rect(origin.x - pad, origin.y - pad, origin.x + cols * s + pad, origin.y + rows * s + pad)

    fun cellTopLeft(col: Int, row: Int) = Offset(origin.x + col * s, origin.y + row * s)

    /** La case sous un point, ou `null` hors du plateau. */
    fun cellAt(p: Offset): Pair<Int, Int>? {
        val c = ((p.x - origin.x) / s).let { if (it < 0f) -1 else it.toInt() }
        val r = ((p.y - origin.y) / s).let { if (it < 0f) -1 else it.toInt() }
        return if (c in 0 until cols && r in 0 until rows) c to r else null
    }

    companion object {
        /** Le plus grand plateau qui tient dans [width] x [height], avec la place du décor (plots, panneau) en haut et en bas. */
        fun fit(cols: Int, rows: Int, width: Float, height: Float): BoardGeometry {
            val s = minOf(width / (cols + 0.4f), height / (rows + 0.4f + 1.5f))
            val boardW = cols * s
            val boardH = rows * s
            val origin = Offset((width - boardW) / 2f, (height - boardH) / 2f)
            return BoardGeometry(cols, rows, s, origin)
        }
    }
}

/** L'asphalte : tranche, dessus, damier discret, petits traits de parking en haut et en bas. */
fun DrawScope.drawBoard(g: BoardGeometry, theme: BoardTheme) {
    val slab = g.slab
    val radius = CornerRadius(g.s * 0.58f)
    drawRoundRect(theme.side, Offset(slab.left, slab.top + g.s * 0.27f), slab.size, radius)
    drawRoundRect(theme.top, slab.topLeft, slab.size, radius)
    for (r in 0 until g.rows) for (c in 0 until g.cols) {
        val tl = g.cellTopLeft(c, r)
        drawRoundRect((if ((r + c) % 2 == 1) theme.tileA else theme.tileB).copy(alpha = 0.55f), Offset(tl.x + 1f, tl.y + 1f), Size(g.s - 2f, g.s - 2f), CornerRadius(g.s * 0.14f))
    }
    val line = Color(0xFFF4F6FA).copy(alpha = 0.85f)
    for (c in 1 until g.cols) {
        val x = g.origin.x + c * g.s - 1.5f
        drawRoundRect(line, Offset(x, slab.top + 2f), Size(3f, g.pad * 0.9f), CornerRadius(1.5f))
        drawRoundRect(line, Offset(x, g.origin.y + g.rows * g.s + 1f), Size(3f, g.pad * 0.9f), CornerRadius(1.5f))
    }
}

/** Plots et panneau autour du plateau (décor fixe : jamais dans la voie d'une voiture). */
fun DrawScope.drawBoardDecor(g: BoardGeometry) {
    val slab = g.slab
    drawParkingSign(slab.left + g.s * 0.5f, slab.top - g.s * 0.95f, g.s * 0.62f)
    drawCone(slab.right - g.s * 0.2f, slab.top - g.s * 0.5f, g.s * 0.5f)
    drawCone(slab.left + g.s * 0.1f, slab.bottom + g.s * 0.62f, g.s * 0.5f)
    drawCone(slab.right - g.s * 0.1f, slab.bottom + g.s * 0.62f, g.s * 0.5f)
}
