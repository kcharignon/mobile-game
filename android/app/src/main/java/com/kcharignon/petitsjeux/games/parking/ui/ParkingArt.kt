package com.kcharignon.petitsjeux.games.parking.ui

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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.games.parking.engine.Car

/**
 * Les dessins de Parking Malin : plateau, plots, panneau P, et le tracé d'un véhicule. Les véhicules eux-mêmes (voitures, camions, bus) sont dans
 * VehicleArt.kt, GÉNÉRÉ par tools/design/make-vehicles.js depuis la même description que l'aperçu docs/design/vehicules.png.
 * Taille de référence : [s] = côté d'une case, en pixels.
 */

/** Éclaircit ([l]), assombrit ([d]) ou rend transparente ([a]) une couleur : le vocabulaire de VehicleArt.kt (généré). */
internal fun tone(base: Color, l: Float = 0f, d: Float = 0f, a: Float = 1f): Color {
    var c = base
    if (l != 0f) c = lerp(c, Color.White, l)
    if (d != 0f) c = lerp(c, Color.Black, d)
    return if (a < 1f) c.copy(alpha = c.alpha * a) else c
}

/**
 * Le modèle de véhicule d'une voiture du niveau : un des modèles de sa longueur (voitures 2 cases, camions 3, bus et poids lourds 4),
 * toujours le même pour une voiture donnée d'un niveau donné.
 */
fun vehicleModelFor(car: Car): VehicleModel {
    // le camion qui transporte une caisse est le pick-up (sa caisse se voit dans la benne) ; les autres camions de 3 cases : livraison ou fourgon
    if (car.carrier) return VehicleModel.PICKUP
    val options = VehicleModel.ofLength(car.len).filter { it != VehicleModel.PICKUP }
    return options[(car.id * 5 + car.color * 3) % options.size]
}

/**
 * Dessine un véhicule centré en [center], le nez tourné de [degrees] (0 = vers le haut). [s] = côté d'une case en pixels.
 * [flash] (0 à 1) rougit la carrosserie (faute) ; [alpha] la fait disparaître (sortie).
 * La « tranche » sombre est décalée vers le bas de l'ÉCRAN, jamais avec la rotation : la lumière vient toujours d'en haut.
 */
fun DrawScope.drawVehicle(
    model: VehicleModel,
    color: Color,
    s: Float,
    center: Offset,
    degrees: Float,
    flash: Float = 0f,
    alpha: Float = 1f,
    shadow: Boolean = true,
) {
    if (alpha <= 0.01f) return
    val faded = alpha < 0.99f
    if (faded) {
        val half = s * (model.len + 1)
        drawIntoCanvas { it.saveLayer(Rect(center.x - half, center.y - half, center.x + half, center.y + half), Paint().apply { this.alpha = alpha }) }
    }
    val body = color
    val sx = model.widthScale
    val length = (model.len - 0.2f) * s
    translate(center.x, center.y) {
        if (shadow) {
            // ombre portée plate : la silhouette, plus bas que la tranche, sombre et douce
            translate(0f, (VEHICLE_DEPTH + 0.07f) * s) {
                rotate(degrees, Offset.Zero) {
                    drawRoundRect(Color.Black.copy(alpha = 0.22f), Offset(-0.4f * s * sx, -length / 2), Size(0.8f * s * sx, length), CornerRadius(0.3f * s))
                }
            }
        }
        translate(0f, VEHICLE_DEPTH * s) { rotate(degrees, Offset.Zero) { scale(sx, 1f, Offset.Zero) { drawVehicleLower(model, body, s) } } }
        rotate(degrees, Offset.Zero) {
            scale(sx, 1f, Offset.Zero) {
                drawVehicleUpper(model, body, s)
                // faute : le véhicule entier rougit (superposition : sa couleur d'origine reste reconnaissable)
                if (flash > 0.01f) drawVehicleSilhouette(model, Color(0xFFFF3B30).copy(alpha = 0.5f * flash), s)
            }
        }
    }
    if (faded) drawIntoCanvas { it.restore() }
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
    val bowl = s * 0.31f
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
            BoardTheme(Color(0xFFA39A86), Color(0xFF7B735F), Color(0xFFB4AC99), Color(0xFFAAA28E)), // sable de chantier
        )

        fun of(world: Int) = worlds[world.coerceAtLeast(0) % worlds.size]
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

/** Une caisse en bois tombée sur la case centrée en ([cx], [cy]) : [appear] (0 à 1) la fait arriver de haut en rebondissant. */
fun DrawScope.drawCrate(cx: Float, cy: Float, s: Float, appear: Float) {
    if (appear <= 0.01f) return
    val scale = 1f + (1f - appear.coerceAtMost(1f)) * 0.5f
    val w = s * 0.72f * scale
    val lift = (1f - appear.coerceAtMost(1f)) * s * 0.5f
    val x = cx - w / 2
    val y = cy - w / 2 - lift
    val a = appear.coerceIn(0f, 1f)
    drawRoundRect(Color.Black.copy(alpha = 0.22f * a), Offset(cx - w / 2, cy - w / 2 + s * 0.1f), Size(w, w), CornerRadius(w * 0.14f))
    drawRoundRect(Color(0xFF8C6535).copy(alpha = a), Offset(x, y + s * 0.07f), Size(w, w), CornerRadius(w * 0.14f))
    drawRoundRect(Color(0xFFD9AE70).copy(alpha = a), Offset(x, y), Size(w, w), CornerRadius(w * 0.14f))
    drawRoundRect(Color(0xFF8C6535).copy(alpha = a), Offset(x, y), Size(w, w), CornerRadius(w * 0.14f), style = Stroke(s * 0.045f))
    // planches et sangles
    drawLine(Color(0xFF8C6535).copy(alpha = a), Offset(x + w * 0.1f, y + w * 0.5f), Offset(x + w * 0.9f, y + w * 0.5f), s * 0.04f, StrokeCap.Round)
    drawLine(Color(0xFF8C6535).copy(alpha = a), Offset(x + w * 0.5f, y + w * 0.1f), Offset(x + w * 0.5f, y + w * 0.9f), s * 0.04f, StrokeCap.Round)
    drawRoundRect(Color(0xFFF2D49B).copy(alpha = 0.6f * a), Offset(x + w * 0.1f, y + w * 0.08f), Size(w * 0.5f, w * 0.1f), CornerRadius(w * 0.05f))
}

/** Un chevron (V) qui montre le sens : [degrees] = rotation d'un chevron qui regarde vers le haut. */
fun DrawScope.drawChevron(center: Offset, degrees: Float, size: Float, alpha: Float) {
    if (alpha <= 0.02f) return
    rotate(degrees, center) {
        val path = Path().apply {
            moveTo(center.x - size, center.y + size * 0.45f)
            lineTo(center.x, center.y - size * 0.55f)
            lineTo(center.x + size, center.y + size * 0.45f)
        }
        drawPath(path, Color.White.copy(alpha = alpha), style = Stroke(size * 0.5f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}
