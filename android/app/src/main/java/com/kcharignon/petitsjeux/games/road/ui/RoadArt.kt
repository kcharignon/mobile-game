package com.kcharignon.petitsjeux.games.road.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.games.road.engine.Body
import com.kcharignon.petitsjeux.games.road.engine.Road
import com.kcharignon.petitsjeux.games.road.engine.RowKind
import com.kcharignon.petitsjeux.games.road.engine.VehicleKind
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

/** Les ambiances de la route (une par monde, voir `RoadWorlds.theme`) : uniquement des couleurs, les dessins sont les mêmes. */
class RoadTheme(
    val asphalt: Color,
    val asphaltDark: Color,
    val line: Color,
    val curb: Color,
    val pavement: Color,
    val pavementLine: Color,
    val grass: Color,
    val grassDark: Color,
    val gravel: Color,
    /** Le soir et la nuit : les véhicules allument leurs phares. */
    val headlights: Boolean,
) {
    companion object {
        val all = listOf(
            // 0 jour
            RoadTheme(Color(0xFF5B6070), Color(0xFF4E5362), Color(0xFFFFFFFF), Color(0xFFE9E2D0), Color(0xFFE8DFC8), Color(0xFFD3C9AE), Color(0xFF7ED06A), Color(0xFF5BB04C), Color(0xFFB9B2A4), false),
            // 1 campagne
            RoadTheme(Color(0xFF6A6D78), Color(0xFF5A5D68), Color(0xFFFFFFFF), Color(0xFFE4DDC8), Color(0xFFD9CFB1), Color(0xFFC4B995), Color(0xFF9BD65A), Color(0xFF74B33E), Color(0xFFC2B79A), false),
            // 2 ville
            RoadTheme(Color(0xFF4A4F5E), Color(0xFF3F4452), Color(0xFFF4F4F4), Color(0xFFCFCFD6), Color(0xFFC9CAD6), Color(0xFFB2B4C4), Color(0xFF6CBF7B), Color(0xFF4FA063), Color(0xFFA7A8B3), false),
            // 3 soir
            RoadTheme(Color(0xFF4B4660), Color(0xFF3F3A52), Color(0xFFFFE9C2), Color(0xFFC9B9B9), Color(0xFFBBA8A8), Color(0xFFA38F94), Color(0xFF6BA561), Color(0xFF4F8750), Color(0xFF9B8E8B), true),
            // 4 nuit
            RoadTheme(Color(0xFF2E3348), Color(0xFF262A3C), Color(0xFFE6EEFF), Color(0xFF8E92A8), Color(0xFF7F8399), Color(0xFF6A6E86), Color(0xFF3F7752), Color(0xFF2F5F44), Color(0xFF6E7083), true),
        )

        fun of(index: Int): RoadTheme = all[index.coerceIn(0, all.size - 1)]
    }
}

private fun Color.shade(factor: Float): Color = Color((red * factor).coerceIn(0f, 1f), (green * factor).coerceIn(0f, 1f), (blue * factor).coerceIn(0f, 1f), alpha)
private fun Color.mix(other: Color, t: Float): Color = Color(red + (other.red - red) * t, green + (other.green - green) * t, blue + (other.blue - blue) * t, alpha)

// ------------------------------------------------------------------------------------------------ les lignes

/** Les voisines d'une ligne de route, pour ses marquages : [above] / [below] = ce qui est au-dessus / au-dessous (`null` hors du niveau), et leur sens. */
class RowContext(val above: RowKind?, val aboveDirection: Int, val below: RowKind?, val belowDirection: Int, val direction: Int)

/**
 * Dessine la ligne de type [kind] dont le coin haut-gauche est (0, [top]) et de hauteur [cell] ; elle fait [width] de large ([Road.COLS] cases).
 * [seed] varie un peu les décors (buissons, fleurs) d'une ligne à l'autre ; [time] = secondes (feux qui clignotent).
 */
fun DrawScope.drawRow(kind: RowKind, theme: RoadTheme, top: Float, cell: Float, seed: Int, context: RowContext, lightsOn: Boolean, time: Float) {
    val w = cell * Road.COLS
    when (kind) {
        RowKind.ROAD -> drawAsphalt(theme, top, cell, w, context)
        RowKind.RAIL -> drawRails(theme, top, cell, w, lightsOn, time)
        RowKind.REFUGE -> drawGrass(theme, top, cell, w, seed)
        RowKind.START -> drawPavement(theme, top, cell, w, seed, finish = false)
        RowKind.FINISH -> drawPavement(theme, top, cell, w, seed, finish = true)
    }
}

private fun DrawScope.drawAsphalt(theme: RoadTheme, top: Float, cell: Float, w: Float, c: RowContext) {
    drawRect(theme.asphalt, Offset(0f, top), Size(w, cell))
    // quelques traces plus sombres : le bitume n'est pas lisse
    for (k in 0 until 9) {
        val x = ((k * 53 + c.direction * 17 + (top / cell).toInt() * 31) % 97) / 97f * w
        drawRoundRect(theme.asphaltDark.copy(alpha = 0.5f), Offset(x, top + cell * (0.2f + 0.6f * ((k * 37) % 5) / 4f)), Size(cell * 0.5f, cell * 0.035f), CornerRadius(cell * 0.02f))
    }
    val dash = PathEffect.dashPathEffect(floatArrayOf(cell * 0.45f, cell * 0.35f))
    val stroke = cell * 0.05f
    // le haut de la voie : pointillés entre deux voies de même sens, double ligne jaune entre deux sens, bord plein sinon
    when {
        c.above == RowKind.ROAD && c.aboveDirection == c.direction ->
            drawLine(theme.line.copy(alpha = 0.85f), Offset(0f, top), Offset(w, top), stroke, pathEffect = dash)
        c.above == RowKind.ROAD -> {
            val yellow = Color(0xFFFFD23F)
            drawLine(yellow, Offset(0f, top - cell * 0.035f), Offset(w, top - cell * 0.035f), stroke)
            drawLine(yellow, Offset(0f, top + cell * 0.035f), Offset(w, top + cell * 0.035f), stroke)
        }
        else -> drawLine(theme.line, Offset(0f, top + stroke / 2), Offset(w, top + stroke / 2), stroke)
    }
    if (c.below != RowKind.ROAD) drawLine(theme.line, Offset(0f, top + cell - stroke / 2), Offset(w, top + cell - stroke / 2), stroke)
}

private fun DrawScope.drawPavement(theme: RoadTheme, top: Float, cell: Float, w: Float, seed: Int, finish: Boolean) {
    drawRect(theme.pavement, Offset(0f, top), Size(w, cell))
    // dalles
    for (k in 0..Road.COLS) drawLine(theme.pavementLine, Offset(k * cell, top), Offset(k * cell, top + cell), cell * 0.02f)
    drawLine(theme.pavementLine, Offset(0f, top + cell / 2), Offset(w, top + cell / 2), cell * 0.02f)
    // quelques fleurs
    for (k in 0 until 4) {
        val col = (seed * 5 + k * 4 + 2) % Road.COLS
        val x = col * cell + cell * (0.25f + 0.5f * ((seed + k) % 3) / 2f)
        val y = top + cell * (0.25f + 0.5f * ((seed * 3 + k) % 2))
        drawFlower(Offset(x, y), cell * 0.1f, k + seed)
    }
    if (finish) {
        // une bande à damier le long de la route : l'arrivée
        val squares = Road.COLS * 2
        val s = w / squares
        for (i in 0 until squares) {
            drawRect(if (i % 2 == 0) Color(0xFF2B2D42) else Color.White, Offset(i * s, top + cell - s), Size(s, s))
            drawRect(if (i % 2 == 0) Color.White else Color(0xFF2B2D42), Offset(i * s, top + cell - 2 * s), Size(s, s))
        }
    } else {
        // un passage piéton devant le départ
        for (i in 0 until 12) drawRect(Color.White.copy(alpha = 0.9f), Offset(cell * (0.4f + i * 1.12f), top + cell - cell * 0.16f), Size(cell * 0.62f, cell * 0.16f))
    }
}

private fun DrawScope.drawGrass(theme: RoadTheme, top: Float, cell: Float, w: Float, seed: Int) {
    drawRect(theme.grass, Offset(0f, top), Size(w, cell))
    // les bordures du terre-plein
    drawRect(theme.curb, Offset(0f, top), Size(w, cell * 0.07f))
    drawRect(theme.curb, Offset(0f, top + cell * 0.93f), Size(w, cell * 0.07f))
    // touffes d'herbe
    for (k in 0 until 14) {
        val x = ((k * 7 + seed * 3) % 14 + 0.5f) * cell
        drawLine(theme.grassDark, Offset(x, top + cell * (0.3f + 0.4f * ((k * 5) % 3) / 2f)), Offset(x + cell * 0.07f, top + cell * (0.18f + 0.4f * ((k * 5) % 3) / 2f)), cell * 0.03f, StrokeCap.Round)
    }
    // buissons et fleurs
    for (k in 0 until 3) {
        val col = (seed * 3 + k * 5 + 1) % Road.COLS
        drawBush(Offset(col * cell + cell * 0.5f, top + cell * (0.45f + 0.1f * (k % 2))), cell * 0.27f, theme)
    }
    for (k in 0 until 5) {
        val col = (seed * 2 + k * 3) % Road.COLS
        drawFlower(Offset(col * cell + cell * (0.2f + 0.6f * ((k + seed) % 3) / 2f), top + cell * (0.3f + 0.4f * (k % 2))), cell * 0.07f, k + seed)
    }
}

private fun DrawScope.drawRails(theme: RoadTheme, top: Float, cell: Float, w: Float, lightsOn: Boolean, time: Float) {
    drawRect(theme.gravel, Offset(0f, top), Size(w, cell))
    for (k in 0 until Road.COLS * 3) drawRect(Color(0xFF6B4F3A), Offset(k * cell / 3f + cell * 0.04f, top + cell * 0.14f), Size(cell * 0.12f, cell * 0.72f))
    drawLine(Color(0xFFCDD1DB), Offset(0f, top + cell * 0.3f), Offset(w, top + cell * 0.3f), cell * 0.08f)
    drawLine(Color(0xFFCDD1DB), Offset(0f, top + cell * 0.7f), Offset(w, top + cell * 0.7f), cell * 0.08f)
    // feux de chaque côté, qui clignotent en alternance quand un train arrive
    val blink = if (lightsOn) (time * 3f).toInt() % 2 == 0 else null
    for ((i, x) in listOf(cell * 0.4f, w - cell * 0.4f).withIndex()) {
        drawLine(Color(0xFF8B8F9E), Offset(x, top + cell * 0.55f), Offset(x, top + cell * 0.95f), cell * 0.05f)
        val on = blink?.let { it == (i == 0) } ?: false
        drawCircle(if (on) Color(0xFFFF3B30) else Color(0xFF5A1E1A), cell * 0.1f, Offset(x, top + cell * 0.45f))
        if (on) drawCircle(Color(0xFFFF3B30).copy(alpha = 0.3f), cell * 0.2f, Offset(x, top + cell * 0.45f))
    }
}

fun DrawScope.drawFlower(at: Offset, r: Float, variant: Int) {
    val petal = listOf(Color(0xFFFFFFFF), Color(0xFFFF8FB1), Color(0xFFFFE066), Color(0xFFB69CFF))[Math.floorMod(variant, 4)]
    for (k in 0 until 5) {
        val a = k * 2 * PI / 5
        drawCircle(petal, r * 0.55f, Offset(at.x + (cos(a) * r * 0.6).toFloat(), at.y + (sin(a) * r * 0.6).toFloat()))
    }
    drawCircle(Color(0xFFFFB020), r * 0.4f, at)
}

fun DrawScope.drawBush(at: Offset, r: Float, theme: RoadTheme) {
    drawOval(Color.Black.copy(alpha = 0.15f), Offset(at.x - r * 1.1f, at.y + r * 0.5f), Size(r * 2.2f, r * 0.7f))
    drawCircle(theme.grassDark.shade(0.85f), r, Offset(at.x, at.y))
    drawCircle(theme.grassDark, r * 0.8f, Offset(at.x - r * 0.55f, at.y + r * 0.2f))
    drawCircle(theme.grassDark, r * 0.8f, Offset(at.x + r * 0.55f, at.y + r * 0.2f))
    drawCircle(theme.grass.copy(alpha = 0.8f), r * 0.45f, Offset(at.x - r * 0.2f, at.y - r * 0.25f))
}

// ------------------------------------------------------------------------------------------------ les véhicules

/** La largeur de la carrosserie, en cases (une ligne fait 1 case de haut). */
private fun bodyWidth(kind: VehicleKind): Float = when (kind) {
    VehicleKind.SCOOTER -> 0.3f
    VehicleKind.SPORT -> 0.6f
    VehicleKind.CAR, VehicleKind.TAXI -> 0.66f
    VehicleKind.TRACTOR -> 0.58f
    VehicleKind.TRAIN -> 0.8f
    else -> 0.74f
}

/**
 * Dessine un véhicule vu du dessus. [body].left est son bord gauche en cases (la route commence à x = 0), [top] le haut de sa ligne, [cell] la taille d'une case.
 * On le dessine toujours « vers la droite » puis on le retourne s'il va à gauche. [time] = secondes (gyrophare de l'ambulance, phares).
 */
fun DrawScope.drawVehicle(body: Body, theme: RoadTheme, top: Float, cell: Float, time: Float) {
    val len = (body.length * cell).toFloat()
    val cx = ((body.left + body.length / 2) * cell).toFloat()
    val cy = top + cell / 2
    val color = Pj.CarColors[Math.floorMod(body.color, Pj.CarColors.size)]
    withTransform({
        translate(cx, cy)
        if (body.direction < 0) scale(-1f, 1f, Offset.Zero)
    }) {
        val half = len / 2
        val w = bodyWidth(body.kind) * cell
        // ombre
        drawRoundRect(Color.Black.copy(alpha = 0.22f), Offset(-half + cell * 0.04f, -w / 2 + cell * 0.07f), Size(len, w), CornerRadius(w * 0.35f))
        // phares : un cône de lumière devant le véhicule, le soir et la nuit
        if (theme.headlights && body.kind != VehicleKind.TRAIN) {
            val beam = Path().apply {
                moveTo(half, -w * 0.35f); lineTo(half + cell * 1.7f, -w * 0.95f); lineTo(half + cell * 1.7f, w * 0.95f); lineTo(half, w * 0.35f); close()
            }
            drawPath(beam, Color(0xFFFFD866).copy(alpha = 0.2f))
        }
        when (body.kind) {
            VehicleKind.CAR -> drawCar(len, w, color, cell, roof = color.shade(0.88f))
            VehicleKind.TAXI -> {
                drawCar(len, w, Color(0xFFFFD41F), cell, roof = Color(0xFFF0BE00))
                drawRoundRect(Color.White, Offset(-len * 0.1f, -w * 0.14f), Size(len * 0.2f, w * 0.28f), CornerRadius(cell * 0.03f))
                drawRect(Color(0xFF2B2D42), Offset(-len * 0.08f, -w * 0.05f), Size(len * 0.16f, w * 0.1f))
            }
            VehicleKind.SPORT -> {
                drawCar(len, w, color, cell, roof = color.shade(0.8f), cabinLength = 0.34f)
                drawRect(Color.White.copy(alpha = 0.8f), Offset(-half + len * 0.08f, -w * 0.12f), Size(len * 0.84f, w * 0.09f))
                drawRect(Color.White.copy(alpha = 0.8f), Offset(-half + len * 0.08f, w * 0.03f), Size(len * 0.84f, w * 0.09f))
            }
            VehicleKind.SCOOTER -> drawScooter(len, w, color, cell)
            VehicleKind.VAN -> drawVan(len, w, color, cell)
            VehicleKind.TRACTOR -> drawTractor(len, w, Color(0xFF4CAF50), cell)
            VehicleKind.AMBULANCE -> drawAmbulance(len, w, cell, time)
            VehicleKind.TRUCK -> drawTruck(len, w, color, cell)
            VehicleKind.BUS -> drawBus(len, w, color, cell)
            VehicleKind.TRAIN -> drawTrain(len, w, cell)
        }
    }
}

private fun DrawScope.wheels(x: Float, w: Float, cell: Float, big: Boolean = false) {
    val r = cell * (if (big) 0.17f else 0.12f)
    val wheelLength = r * 2.2f
    // une roue de chaque côté de la carrosserie, à moitié dehors
    for (sign in listOf(-1f, 1f)) {
        drawRoundRect(Color(0xFF1E2030), Offset(x - wheelLength / 2, sign * (w / 2) - r / 2), Size(wheelLength, r), CornerRadius(r * 0.4f))
    }
}

private fun DrawScope.lights(half: Float, w: Float, cell: Float) {
    for (sign in listOf(-1f, 1f)) {
        drawOval(Color(0xFFFFF0A0), Offset(half - cell * 0.1f, sign * w * 0.3f - cell * 0.045f), Size(cell * 0.09f, cell * 0.09f))
        drawOval(Color(0xFFE23A2E), Offset(-half + cell * 0.01f, sign * w * 0.3f - cell * 0.04f), Size(cell * 0.05f, cell * 0.08f))
    }
}

private fun DrawScope.drawCar(len: Float, w: Float, color: Color, cell: Float, roof: Color, cabinLength: Float = 0.42f) {
    val half = len / 2
    wheels(-half * 0.55f, w, cell)
    wheels(half * 0.55f, w, cell)
    drawRoundRect(color, Offset(-half, -w / 2), Size(len, w), CornerRadius(w * 0.38f))
    drawRoundRect(color.shade(0.78f), Offset(-half, -w / 2), Size(len, w), CornerRadius(w * 0.38f), style = Stroke(cell * 0.03f))
    // pare-brise, toit et lunette arrière
    val cabinStart = -len * 0.12f
    drawRoundRect(Color(0xFF9CD3F5), Offset(cabinStart - len * 0.02f, -w * 0.4f), Size(len * (cabinLength + 0.1f), w * 0.8f), CornerRadius(w * 0.22f))
    drawRoundRect(roof, Offset(cabinStart + len * 0.06f, -w * 0.4f), Size(len * (cabinLength - 0.06f), w * 0.8f), CornerRadius(w * 0.18f))
    lights(half, w, cell)
}

private fun DrawScope.drawScooter(len: Float, w: Float, color: Color, cell: Float) {
    val half = len / 2
    drawRoundRect(Color(0xFF1E2030), Offset(-half, -cell * 0.05f), Size(cell * 0.28f, cell * 0.1f), CornerRadius(cell * 0.04f))
    drawRoundRect(Color(0xFF1E2030), Offset(half - cell * 0.28f, -cell * 0.05f), Size(cell * 0.28f, cell * 0.1f), CornerRadius(cell * 0.04f))
    drawRoundRect(color, Offset(-half + cell * 0.1f, -w / 2), Size(len - cell * 0.2f, w), CornerRadius(w * 0.5f))
    // le guidon, puis le conducteur (épaules et casque vus du dessus)
    drawLine(Color(0xFF1E2030), Offset(half - cell * 0.22f, -cell * 0.26f), Offset(half - cell * 0.22f, cell * 0.26f), cell * 0.05f, StrokeCap.Round)
    drawOval(Color(0xFF3E8BFF), Offset(-cell * 0.14f, -cell * 0.24f), Size(cell * 0.3f, cell * 0.48f))
    drawCircle(Color(0xFFFFC83D), cell * 0.14f, Offset(-cell * 0.0f, 0f))
    drawCircle(Color.White.copy(alpha = 0.5f), cell * 0.045f, Offset(cell * 0.03f, -cell * 0.05f))
}

private fun DrawScope.drawVan(len: Float, w: Float, color: Color, cell: Float) {
    val half = len / 2
    wheels(-half * 0.55f, w, cell)
    wheels(half * 0.5f, w, cell)
    drawRoundRect(color, Offset(-half, -w / 2), Size(len, w), CornerRadius(w * 0.2f))
    drawRoundRect(color.shade(0.78f), Offset(-half, -w / 2), Size(len, w), CornerRadius(w * 0.2f), style = Stroke(cell * 0.03f))
    // toit de la caisse puis cabine
    drawRoundRect(color.mix(Color.White, 0.35f), Offset(-half + cell * 0.07f, -w * 0.4f), Size(len * 0.62f, w * 0.8f), CornerRadius(w * 0.1f))
    drawLine(color.shade(0.78f), Offset(-half + len * 0.33f, -w * 0.4f), Offset(-half + len * 0.33f, w * 0.4f), cell * 0.025f)
    drawRoundRect(Color(0xFF9CD3F5), Offset(half - len * 0.3f, -w * 0.4f), Size(len * 0.24f, w * 0.8f), CornerRadius(w * 0.14f))
    lights(half, w, cell)
}

private fun DrawScope.drawTractor(len: Float, w: Float, color: Color, cell: Float) {
    val half = len / 2
    // grosses roues arrière, petites roues avant
    for (sign in listOf(-1f, 1f)) {
        drawRoundRect(Color(0xFF1E2030), Offset(-half + len * 0.1f, sign * w * 0.5f - cell * 0.1f), Size(cell * 0.62f, cell * 0.2f), CornerRadius(cell * 0.08f))
        drawRoundRect(Color(0xFF1E2030), Offset(half - len * 0.22f, sign * w * 0.42f - cell * 0.06f), Size(cell * 0.32f, cell * 0.12f), CornerRadius(cell * 0.05f))
    }
    // capot étroit, cabine
    drawRoundRect(color, Offset(-half + len * 0.1f, -w * 0.42f), Size(len * 0.88f, w * 0.84f), CornerRadius(w * 0.3f))
    drawRoundRect(color.shade(0.75f), Offset(-half + len * 0.1f, -w * 0.42f), Size(len * 0.88f, w * 0.84f), CornerRadius(w * 0.3f), style = Stroke(cell * 0.03f))
    drawRoundRect(Color(0xFF9CD3F5), Offset(-half + len * 0.2f, -w * 0.32f), Size(len * 0.34f, w * 0.64f), CornerRadius(w * 0.15f))
    drawRoundRect(Color(0xFFF3F6FA), Offset(-half + len * 0.24f, -w * 0.32f), Size(len * 0.26f, w * 0.64f), CornerRadius(w * 0.12f))
    drawCircle(Color(0xFF1E2030), cell * 0.04f, Offset(half - len * 0.22f, -w * 0.15f))
    lights(half, w * 0.9f, cell)
}

private fun DrawScope.drawAmbulance(len: Float, w: Float, cell: Float, time: Float) {
    val half = len / 2
    wheels(-half * 0.6f, w, cell)
    wheels(half * 0.55f, w, cell)
    drawRoundRect(Color.White, Offset(-half, -w / 2), Size(len, w), CornerRadius(w * 0.25f))
    drawRoundRect(Color(0xFFC9CFDA), Offset(-half, -w / 2), Size(len, w), CornerRadius(w * 0.25f), style = Stroke(cell * 0.03f))
    // bande rouge, croix rouge
    drawRect(Color(0xFFE8362F), Offset(-half, -w * 0.5f), Size(len, w * 0.1f))
    drawRect(Color(0xFFE8362F), Offset(-half, w * 0.4f), Size(len, w * 0.1f))
    val cross = Offset(-half + len * 0.3f, 0f)
    drawRect(Color(0xFFE8362F), Offset(cross.x - cell * 0.2f, -cell * 0.07f), Size(cell * 0.4f, cell * 0.14f))
    drawRect(Color(0xFFE8362F), Offset(cross.x - cell * 0.07f, -cell * 0.2f), Size(cell * 0.14f, cell * 0.4f))
    drawRoundRect(Color(0xFF9CD3F5), Offset(half - len * 0.26f, -w * 0.36f), Size(len * 0.2f, w * 0.72f), CornerRadius(w * 0.14f))
    // gyrophare : rouge et bleu en alternance
    val red = (time * 5f).toInt() % 2 == 0
    drawCircle((if (red) Color(0xFFFF3B30) else Color(0xFF3E8BFF)).copy(alpha = 0.35f), cell * 0.3f, Offset(half - len * 0.38f, 0f))
    drawRect(if (red) Color(0xFFFF3B30) else Color(0xFF3E8BFF), Offset(half - len * 0.42f, -w * 0.3f), Size(cell * 0.1f, w * 0.6f))
    drawRect(if (red) Color(0xFF3E8BFF) else Color(0xFFFF3B30), Offset(half - len * 0.42f + cell * 0.1f, -w * 0.3f), Size(cell * 0.1f, w * 0.6f))
    lights(half, w, cell)
}

private fun DrawScope.drawTruck(len: Float, w: Float, color: Color, cell: Float) {
    val half = len / 2
    val cab = len * 0.26f
    wheels(-half + len * 0.12f, w, cell, big = true)
    wheels(-half + len * 0.3f, w, cell, big = true)
    wheels(half - cab * 0.6f, w, cell, big = true)
    // remorque : une grande caisse blanche à bande de couleur
    drawRoundRect(Color(0xFFF1F3F8), Offset(-half, -w / 2), Size(len - cab - cell * 0.06f, w), CornerRadius(w * 0.1f))
    drawRoundRect(Color(0xFFC9CFDA), Offset(-half, -w / 2), Size(len - cab - cell * 0.06f, w), CornerRadius(w * 0.1f), style = Stroke(cell * 0.03f))
    drawRect(color, Offset(-half + cell * 0.05f, -w * 0.12f), Size(len - cab - cell * 0.16f, w * 0.24f))
    for (k in 1..3) drawLine(Color(0xFFD9DEE8), Offset(-half + (len - cab) * k / 4f, -w * 0.45f), Offset(-half + (len - cab) * k / 4f, w * 0.45f), cell * 0.02f)
    // cabine
    drawRoundRect(color, Offset(half - cab, -w * 0.46f), Size(cab, w * 0.92f), CornerRadius(w * 0.22f))
    drawRoundRect(color.shade(0.75f), Offset(half - cab, -w * 0.46f), Size(cab, w * 0.92f), CornerRadius(w * 0.22f), style = Stroke(cell * 0.03f))
    drawRoundRect(Color(0xFF9CD3F5), Offset(half - cab * 0.5f, -w * 0.36f), Size(cab * 0.36f, w * 0.72f), CornerRadius(w * 0.12f))
    lights(half, w * 0.92f, cell)
}

private fun DrawScope.drawBus(len: Float, w: Float, color: Color, cell: Float) {
    val half = len / 2
    wheels(-half * 0.6f, w, cell, big = true)
    wheels(half * 0.45f, w, cell, big = true)
    drawRoundRect(color, Offset(-half, -w / 2), Size(len, w), CornerRadius(w * 0.2f))
    drawRoundRect(color.shade(0.75f), Offset(-half, -w / 2), Size(len, w), CornerRadius(w * 0.2f), style = Stroke(cell * 0.03f))
    // toit clair avec des bouches d'aération, fenêtres le long des côtés, pare-brise
    drawRoundRect(color.mix(Color.White, 0.45f), Offset(-half + cell * 0.1f, -w * 0.34f), Size(len - cell * 0.4f, w * 0.68f), CornerRadius(w * 0.1f))
    for (k in 0 until 3) drawRoundRect(color.mix(Color.White, 0.75f), Offset(-half + len * (0.2f + k * 0.22f), -w * 0.16f), Size(len * 0.1f, w * 0.32f), CornerRadius(cell * 0.03f))
    for (k in 0 until 7) {
        val x = -half + len * 0.06f + k * len * 0.115f
        drawRect(Color(0xFF9CD3F5), Offset(x, -w * 0.47f), Size(len * 0.07f, w * 0.1f))
        drawRect(Color(0xFF9CD3F5), Offset(x, w * 0.37f), Size(len * 0.07f, w * 0.1f))
    }
    drawRoundRect(Color(0xFF9CD3F5), Offset(half - len * 0.08f, -w * 0.4f), Size(len * 0.05f, w * 0.8f), CornerRadius(w * 0.1f))
    lights(half, w, cell)
}

private fun DrawScope.drawTrain(len: Float, w: Float, cell: Float) {
    val half = len / 2
    // la locomotive est devant (à droite), puis trois wagons
    val loco = cell * 3.4f
    val wagon = (len - loco - 3 * cell * 0.15f) / 3f
    var x = -half
    val colors = listOf(Color(0xFF2D5FE0), Color(0xFF2D5FE0), Color(0xFF2D5FE0))
    for (i in 0 until 3) {
        drawRoundRect(Color(0xFFF1F3F8), Offset(x, -w / 2), Size(wagon, w), CornerRadius(w * 0.2f))
        drawRoundRect(colors[i], Offset(x, -w / 2), Size(wagon, w), CornerRadius(w * 0.2f), style = Stroke(cell * 0.05f))
        drawRect(colors[i].copy(alpha = 0.5f), Offset(x + wagon * 0.1f, -w * 0.15f), Size(wagon * 0.8f, w * 0.3f))
        for (k in 0 until 4) drawRect(Color(0xFF9CD3F5), Offset(x + wagon * (0.1f + k * 0.22f), -w * 0.4f), Size(wagon * 0.14f, w * 0.1f))
        drawLine(Color(0xFF2B2D42), Offset(x + wagon, 0f), Offset(x + wagon + cell * 0.15f, 0f), cell * 0.05f)
        x += wagon + cell * 0.15f
    }
    drawRoundRect(Color(0xFFE8362F), Offset(x, -w / 2), Size(loco, w), CornerRadius(w * 0.35f))
    drawRoundRect(Color(0xFFB02A24), Offset(x, -w / 2), Size(loco, w), CornerRadius(w * 0.35f), style = Stroke(cell * 0.05f))
    drawRect(Color(0xFFFFD23F), Offset(x + loco * 0.1f, -w * 0.14f), Size(loco * 0.8f, w * 0.28f))
    drawRoundRect(Color(0xFF9CD3F5), Offset(x + loco * 0.7f, -w * 0.34f), Size(loco * 0.18f, w * 0.68f), CornerRadius(w * 0.1f))
    drawCircle(Color(0xFFFFF0A0), cell * 0.1f, Offset(half - cell * 0.08f, -w * 0.22f))
    drawCircle(Color(0xFFFFF0A0), cell * 0.1f, Offset(half - cell * 0.08f, w * 0.22f))
}

// ------------------------------------------------------------------------------------------------ le poussin et les graines

/**
 * Le poussin vu du dessus, centré en [at], de rayon [r], le bec vers le haut. [hop] : avancement du saut (0 à 1, ou -1 s'il ne saute pas) : il grossit un peu
 * en haut du saut et son ombre rétrécit. [flat] (0 à 1) : à quel point il est aplati (écrasé) ; [time] en secondes (battement des ailes, étoiles qui tournent).
 */
fun DrawScope.drawChick(at: Offset, r: Float, hop: Double, flat: Float, time: Float, alpha: Float = 1f) {
    val lift = if (hop >= 0) sin(PI * hop).toFloat() else 0f
    val body = Color(0xFFFFD23F).copy(alpha = alpha)
    val bodyDark = Color(0xFFF2A91B).copy(alpha = alpha)
    val beak = Color(0xFFFF8A1F).copy(alpha = alpha)
    // ombre au sol
    drawOval(Color.Black.copy(alpha = (0.25f - 0.1f * lift) * alpha), Offset(at.x - r * (1.0f - 0.2f * lift) * (1f + flat * 0.6f), at.y + r * 0.35f), Size(r * 2f * (1f - 0.2f * lift) * (1f + flat * 0.6f), r * 0.8f))
    if (flat > 0.02f) {
        // aplati : une galette jaune, les ailes écartées, des étoiles qui tournent au-dessus
        val sx = 1f + 0.8f * flat
        val sy = 1f - 0.72f * flat
        withTransform({ translate(at.x, at.y); scale(sx, sy, Offset.Zero) }) {
            drawCircle(bodyDark, r * 1.02f, Offset.Zero)
            drawCircle(body, r, Offset(0f, -r * 0.04f))
            drawOval(bodyDark, Offset(-r * 1.55f, -r * 0.3f), Size(r * 0.9f, r * 0.6f))
            drawOval(bodyDark, Offset(r * 0.65f, -r * 0.3f), Size(r * 0.9f, r * 0.6f))
        }
        // croix à la place des yeux
        val ex = r * 0.35f * sx
        for (side in listOf(-1f, 1f)) {
            val c = Offset(at.x + side * ex, at.y - r * 0.2f * sy)
            drawLine(Color(0xFF2B2D42).copy(alpha = alpha), Offset(c.x - r * 0.1f, c.y - r * 0.1f), Offset(c.x + r * 0.1f, c.y + r * 0.1f), r * 0.07f, StrokeCap.Round)
            drawLine(Color(0xFF2B2D42).copy(alpha = alpha), Offset(c.x - r * 0.1f, c.y + r * 0.1f), Offset(c.x + r * 0.1f, c.y - r * 0.1f), r * 0.07f, StrokeCap.Round)
        }
        for (k in 0 until 3) {
            val a = time * 5f + k * 2f * PI.toFloat() / 3f
            val sc = Offset(at.x + cos(a) * r * 1.1f * sx, at.y - r * 0.55f + sin(a) * r * 0.25f)
            drawStarSmall(sc, r * 0.22f)
        }
        return
    }
    val s = 1f + 0.25f * lift
    val flap = if (hop >= 0) 0.55f else 0.1f * sin(time * 9f)
    withTransform({ translate(at.x, at.y - r * 0.25f * lift); scale(s, s, Offset.Zero) }) {
        // ailes
        for (side in listOf(-1f, 1f)) {
            withTransform({ rotate(side * (20f + 40f * flap), Offset(side * r * 0.75f, 0f)) }) {
                drawOval(bodyDark, Offset(side * r * 0.75f - r * 0.3f, -r * 0.2f), Size(r * 0.6f, r * 0.95f))
            }
        }
        // corps et tête
        drawCircle(bodyDark, r * 1.03f, Offset(0f, r * 0.05f))
        drawCircle(body, r, Offset(0f, 0f))
        drawCircle(Color.White.copy(alpha = 0.3f * alpha), r * 0.3f, Offset(-r * 0.35f, r * 0.15f))
        // petite houppe, bec, yeux
        drawCircle(bodyDark, r * 0.12f, Offset(0f, r * 0.55f))
        val nose = Path().apply { moveTo(-r * 0.22f, -r * 0.82f); lineTo(r * 0.22f, -r * 0.82f); lineTo(0f, -r * 1.2f); close() }
        drawPath(nose, beak)
        for (side in listOf(-1f, 1f)) {
            drawCircle(Color.White.copy(alpha = alpha), r * 0.2f, Offset(side * r * 0.36f, -r * 0.42f))
            drawCircle(Color(0xFF2B2D42).copy(alpha = alpha), r * 0.11f, Offset(side * r * 0.36f, -r * 0.45f))
        }
        // pattes
        for (side in listOf(-1f, 1f)) drawOval(beak, Offset(side * r * 0.4f - r * 0.12f, -r * 1.02f), Size(r * 0.24f, r * 0.3f))
    }
}

private fun DrawScope.drawStarSmall(c: Offset, r: Float) {
    val p = Path()
    for (i in 0 until 10) {
        val a = -PI / 2 + i * PI / 5
        val rr = if (i % 2 == 0) r else r * 0.45f
        val x = c.x + (rr * cos(a)).toFloat()
        val y = c.y + (rr * sin(a)).toFloat()
        if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
    }
    p.close()
    drawPath(p, Color(0xFFFFE066))
    drawPath(p, Color(0xFFE59A00), style = Stroke(r * 0.2f))
}

/** Une graine dorée (trois grains) qui se balance un peu ; [bob] = secondes décalées pour que toutes ne bougent pas pareil. */
fun DrawScope.drawGrain(at: Offset, r: Float, bob: Float) {
    val dy = sin(bob * 4f) * r * 0.12f
    val c = Offset(at.x, at.y + dy)
    drawOval(Color.Black.copy(alpha = 0.18f), Offset(c.x - r * 0.9f, c.y + r * 0.55f), Size(r * 1.8f, r * 0.5f))
    for ((dx, dyy, a) in listOf(Triple(-0.45f, 0.1f, -25f), Triple(0.45f, 0.1f, 25f), Triple(0f, -0.3f, 0f))) {
        withTransform({ translate(c.x + dx * r, c.y + dyy * r); rotate(a, Offset.Zero) }) {
            drawOval(Color(0xFFE59A00), Offset(-r * 0.32f, -r * 0.5f), Size(r * 0.64f, r * 1.0f))
            drawOval(Color(0xFFFFC83D), Offset(-r * 0.26f, -r * 0.46f), Size(r * 0.52f, r * 0.86f))
            drawOval(Color.White.copy(alpha = 0.7f), Offset(-r * 0.12f, -r * 0.34f), Size(r * 0.14f, r * 0.3f))
        }
    }
}

/** Les étincelles d'une graine ramassée ou d'un arrivée, [p] de 0 à 1. */
fun DrawScope.drawSparkle(at: Offset, r: Float, p: Float, color: Color = Color(0xFFFFE066)) {
    if (p <= 0f || p >= 1f) return
    for (k in 0 until 8) {
        val a = k * 2 * PI / 8
        val d = r * (0.5f + 1.5f * p)
        drawCircle(color.copy(alpha = 1f - p), r * 0.16f * (1f - p * 0.5f), Offset(at.x + (cos(a) * d).toFloat(), at.y + (sin(a) * d).toFloat()))
    }
}

/** Quelques plumes qui s'envolent quand le poussin est écrasé, [p] de 0 à 1. */
fun DrawScope.drawFeathers(at: Offset, r: Float, p: Float) {
    if (p <= 0f || p >= 1f) return
    for (k in 0 until 6) {
        val a = -PI / 2 + (k - 2.5) * 0.45
        val d = r * (0.6f + 2.2f * p)
        val c = Offset(at.x + (cos(a) * d).toFloat() * 1.3f, at.y + (sin(a) * d).toFloat() - r * 0.3f * p)
        withTransform({ rotate((k * 50f + p * 200f), c) }) {
            drawOval(Color.White.copy(alpha = (1f - p) * 0.95f), Offset(c.x - r * 0.1f, c.y - r * 0.22f), Size(r * 0.2f, r * 0.44f))
        }
    }
}

/** Une petite scène de route pour la carte du jeu dans le menu : deux voies, une voiture, un camion, le poussin sur le trottoir. */
fun DrawScope.drawRoadCard(time: Float) {
    val cell = size.height / 4.2f
    val theme = RoadTheme.of(0)
    fun wrap(x: Float) = x - floor(x / 20f) * 20f
    val rows = listOf(RowKind.START, RowKind.ROAD, RowKind.ROAD, RowKind.FINISH)
    // décalage pour centrer la scène en hauteur
    val total = rows.size * cell
    val y0 = (size.height - total) / 2f + cell * 0.1f
    val xs = (size.width - Road.COLS * cell) / 2f
    withTransform({ translate(xs, 0f) }) {
        for ((i, kind) in rows.withIndex().reversed()) {
            val top = y0 + (rows.size - 1 - i) * cell
            val below = rows.getOrNull(i - 1)
            val above = rows.getOrNull(i + 1)
            drawRow(kind, theme, top, cell, i, RowContext(above, -1, below, +1, if (i == 1) +1 else -1), false, time)
        }
        val topRoad2 = y0 + 1 * cell
        val topRoad1 = y0 + 2 * cell
        drawVehicle(Body(VehicleKind.TRUCK, 3, (wrap(14f - time * 1.5f) - 6f).toDouble(), VehicleKind.TRUCK.length, -1), theme, topRoad2, cell, time)
        drawVehicle(Body(VehicleKind.CAR, 4, (wrap(8f + time * 1.5f) - 6f).toDouble(), VehicleKind.CAR.length, +1), theme, topRoad1, cell, time)
        drawVehicle(Body(VehicleKind.SCOOTER, 5, (wrap(2f + time * 1.5f) - 6f).toDouble(), VehicleKind.SCOOTER.length, +1), theme, topRoad1, cell, time)
        drawGrain(Offset(Road.COLS * cell * 0.62f, y0 + 2.5f * cell), cell * 0.2f, time)
        drawChick(Offset(Road.COLS * cell * 0.5f, y0 + 3.5f * cell - cell * 0.1f), cell * 0.32f, -1.0, 0f, time)
    }
}
