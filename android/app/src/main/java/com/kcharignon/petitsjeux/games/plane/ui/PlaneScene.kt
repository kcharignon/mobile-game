package com.kcharignon.petitsjeux.games.plane.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import com.kcharignon.petitsjeux.core.ui.drawStar
import com.kcharignon.petitsjeux.games.plane.engine.Piece
import com.kcharignon.petitsjeux.games.plane.engine.PieceKind
import com.kcharignon.petitsjeux.games.plane.engine.PlaneRun
import com.kcharignon.petitsjeux.games.plane.engine.PlaneWorld
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * La caméra de Super Avion : on regarde le ciel de derrière l'avion, un peu au-dessus. Une chose à la distance [rel] devant l'avion (en mètres) et au travers [x]
 * (unités de ciel) tombe à l'écran en `(sx(x, rel), yFlight(rel))` si elle vole à la hauteur de l'avion, ou en `(sx(x, rel), yTerrain(rel))` si elle est au sol,
 * très loin dessous. [s] est l'échelle (1 au niveau de l'avion, vers 0 à l'horizon) : c'est elle qui fait grossir les choses en approchant.
 * [altitude] (1 en croisière, 0 au toucher des roues) fait monter le sol vers l'avion à l'atterrissage.
 */
internal class Cam(val width: Float, val height: Float, altitude: Float = 1f) {
    val horizon = height * 0.30f
    val flight = height * 0.70f
    private val cameraBack = 24f

    /** Pixels par unité de ciel au niveau de l'avion. */
    val unit = width * 0.5f

    /** Où est le sol à la hauteur de l'avion : tout en bas de l'écran en croisière, juste sous les roues au toucher. */
    val terrain: Float = flight + 0.115f * unit + (height * 0.985f - flight - 0.115f * unit).coerceAtLeast(0f) * altitude

    fun s(rel: Float): Float = cameraBack / (max(rel, -cameraBack * 0.6f) + cameraBack)
    fun yFlight(rel: Float): Float = horizon + (flight - horizon) * s(rel)
    fun yTerrain(rel: Float): Float = horizon + (terrain - horizon) * s(rel)
    fun sx(x: Float, rel: Float): Float = width / 2f + x * unit * s(rel)
    fun u(rel: Float): Float = unit * s(rel)

    companion object {
        /** Jusqu'où on voit (m) : plus loin, c'est le brouillard de l'horizon. */
        const val VIEW = 150f
    }
}

private const val BAND = 8f
/** Les obstacles sont dessinés un peu plus grands que leur zone de choc : ils remplissent bien leur voie. */
private const val SPRITE_SCALE = 1.25f
private const val CLOUD_SPACING = 19f
private const val GROUND_SPACING = 14f

/** Les mètres de piste : elle commence 30 m avant l'endroit où l'avion se pose, et se prolonge de 170 m. */
private const val RUNWAY_BEFORE = 30f
private const val RUNWAY_AFTER = 170f

private fun hash(a: Int, b: Int): Int = ((a * 73856093) xor (b * 19349663) xor 0x5bd1e995) and 0x7fffffff

private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t

/** Une chose à dessiner, avec sa distance : on dessine du plus loin au plus près. */
private class Item(val rel: Float, val draw: DrawScope.() -> Unit)

/**
 * Toute la scène d'un vol : ciel, sol qui défile, voies, décor, obstacles, anneaux, aéroport, avion, puis les effets du choc.
 * Ne dépend que de l'état de [run] (rien n'est retenu d'une image à l'autre) : le même vol au même instant donne la même image.
 */
internal fun DrawScope.drawPlaneScene(run: PlaneRun, theme: PlaneTheme, color: Color, steerHint: Boolean = false) {
    val cam = Cam(size.width, size.height, run.altitude)
    val dist = run.dist
    val time = run.time
    val length = run.world.length

    // la secousse du choc : tout l'écran tremble un instant (un léger zoom l'accompagne : elle ne laisse jamais voir le bord de l'image)
    val shake = if (run.sinceHit < 0.45f) 1f - run.sinceHit / 0.45f else 0f
    val shakeAmp = shake * 9f * density
    withTransform({
        scale(1f + 0.06f * shake, 1f + 0.06f * shake, Offset(size.width / 2f, size.height / 2f))
        translate(sin(time * 80f) * shakeAmp, cos(time * 63f) * shakeAmp * 0.7f)
    }) {
        drawSky(cam, theme)
        drawTerrain(cam, theme, dist)
        drawAirfield(cam, theme, dist, length, time)
        drawLanes(cam, dist)

        val items = ArrayList<Item>(80)
        // des nuages de chaque côté, à toutes les hauteurs
        val firstCloud = floor((dist - 4f) / CLOUD_SPACING).toInt()
        val lastCloud = floor((dist + Cam.VIEW) / CLOUD_SPACING).toInt()
        for (k in firstCloud..lastCloud) for (side in intArrayOf(-1, 1)) {
            val h = hash(k, side + theme.name.length)
            val rel = k * CLOUD_SPACING + (h / 31) % 5 - dist
            if (rel < -2f) continue
            val x = side * (1.7f + (h % 100) / 100f * 2.2f)
            val lift = ((h / 7) % 100) / 100f * 1.3f - 0.5f
            items += Item(rel) {
                val u = cam.u(rel)
                drawCloud(cam.sx(x, rel), cam.yFlight(rel) - lift * u, u * 1.25f, h / 13, theme.cloud.copy(alpha = if (theme.night) 0.85f else 0.95f))
            }
        }
        // le décor du sol (jamais sur la piste)
        val firstGround = floor((dist - 4f) / GROUND_SPACING).toInt()
        val lastGround = floor((dist + Cam.VIEW) / GROUND_SPACING).toInt()
        for (k in firstGround..lastGround) for (n in 0 until 4) {
            val h = hash(k, n + 11)
            val z = k * GROUND_SPACING + (h / 17) % 9
            val rel = z - dist
            if (rel < -1.2f || z > length - RUNWAY_BEFORE - 70f) continue
            val x = ((h / 5) % 1000) / 1000f * 7.2f - 3.6f
            val kind = theme.ground[h % theme.ground.size]
            items += Item(rel) {
                drawGroundDecor(kind, h / 29, cam.sx(x, rel), cam.yTerrain(rel), cam.u(rel), time, theme.night)
            }
        }
        // les bâtiments de l'aéroport, de part et d'autre de la piste, après l'endroit où l'on se pose
        for ((offset, x, what) in listOf(Triple(18f, 1.55f, 0), Triple(52f, -1.7f, 1), Triple(88f, 1.6f, 1), Triple(8f, -0.75f, 2), Triple(60f, -1.15f, 3), Triple(96f, 1.05f, 3))) {
            val rel = length + offset - dist
            if (rel < -2f || rel > Cam.VIEW) continue
            items += Item(rel) {
                val bx = cam.sx(x, rel)
                val by = cam.yTerrain(rel)
                val u = cam.u(rel)
                when (what) {
                    0 -> drawControlTower(bx, by, u, time, theme.night)
                    1 -> drawHangar(bx, by, u, theme.night)
                    2 -> drawWindsock(bx, by, u, time)
                    else -> drawParkedPlane(bx, by, u, what + (x * 10).toInt())
                }
            }
        }
        // les obstacles et les anneaux
        for (piece in run.world.pieces) {
            val rel = piece.z - dist
            if (rel > Cam.VIEW) break
            if (rel < -1.8f) continue
            items += Item(rel) { drawPiece(cam, piece, rel, run.knocked[piece.id], time, theme) }
        }
        // l'avion est au niveau 0
        items += Item(0f) { drawThePlane(cam, run, color) }
        // tant que le doigt n'a pas glissé : deux flèches qui montrent le geste
        if (steerHint) items += Item(-0.01f) { drawSteerHint(cam, run) }

        items.sortByDescending { it.rel }
        for (item in items) item.draw(this)

        drawFog(cam, theme)
        drawEffects(cam, run)
    }
    drawHitFlash(run)
}

// ------------------------------------------------------------------ le ciel et le sol

private fun DrawScope.drawSky(cam: Cam, theme: PlaneTheme) {
    val w = size.width
    drawRect(Brush.verticalGradient(listOf(theme.skyTop, theme.skyBottom), startY = 0f, endY = cam.horizon), Offset.Zero, Size(w, cam.horizon + 1f))
    theme.sun?.let { sun ->
        val c = Offset(w * 0.76f, cam.horizon * 0.62f)
        drawCircle(sun.copy(alpha = 0.22f), w * 0.15f, c)
        drawCircle(sun.copy(alpha = 0.4f), w * 0.1f, c)
        drawCircle(sun, w * 0.062f, c)
    }
    if (theme.night) {
        for (i in 0 until 30) {
            val h = hash(i, 17)
            val x = (h % 1000) / 1000f * w
            val y = ((h / 1000) % 1000) / 1000f * cam.horizon * 0.85f
            drawCircle(Color.White.copy(alpha = 0.5f + 0.4f * ((h / 3) % 3) / 2f), (0.8f + (h % 3) * 0.5f) * density, Offset(x, y))
        }
    } else {
        // de gros nuages lointains, immobiles
        val cloud = theme.cloud.copy(alpha = 0.8f)
        for ((cx, cy, r) in listOf(Triple(0.18f, 0.5f, 0.05f), Triple(0.52f, 0.28f, 0.04f), Triple(0.9f, 0.7f, 0.045f))) {
            val c = Offset(w * cx, cam.horizon * cy)
            drawCircle(cloud, w * r, c)
            drawCircle(cloud, w * r * 0.8f, c + Offset(w * r * 1.1f, w * r * 0.15f))
            drawCircle(cloud, w * r * 0.7f, c + Offset(-w * r * 1.0f, w * r * 0.2f))
        }
    }
    // la silhouette de l'horizon : collines, mer, plateaux ou ville
    when (theme.name) {
        "Campagne" -> for (i in 0 until 6) drawCircle(theme.far, w * (0.13f + (i % 3) * 0.04f), Offset(w * (i * 0.23f - 0.02f), cam.horizon + w * 0.1f))
        "Désert" -> for (i in 0 until 5) {
            val h = hash(i, 5)
            val bw = w * (0.16f + (h % 3) * 0.04f)
            val bh = cam.height * (0.02f + (h % 4) * 0.007f)
            val x = i * w * 0.24f
            drawPath(Path().apply { moveTo(x, cam.horizon + 1f); lineTo(x + bw * 0.15f, cam.horizon - bh); lineTo(x + bw * 0.85f, cam.horizon - bh); lineTo(x + bw, cam.horizon + 1f); close() }, theme.far)
        }
        "Nuit" -> {
            val n = 15
            for (i in 0 until n) {
                val h = hash(i, 3)
                val bw = w / n
                val bh = cam.height * (0.012f + (h % 5) * 0.008f)
                drawRect(theme.far, Offset(i * bw, cam.horizon - bh), Size(bw + 1f, bh + 1f))
                for (r in 0 until 3) if ((h / (r + 2)) % 2 == 0) drawRect(Color(0xFFFFE08A).copy(alpha = 0.8f), Offset(i * bw + bw * 0.3f, cam.horizon - bh + 3f * density + r * 5f * density), Size(bw * 0.14f, 2.5f * density))
            }
        }
        else -> drawRect(theme.far, Offset(0f, cam.horizon - cam.height * 0.004f), Size(w, cam.height * 0.006f))
    }
}

/** Le sol qui défile : des bandes qui alternent tous les 10 m pour qu'on voie la vitesse, des champs de tons voisins par-dessus. */
private fun DrawScope.drawTerrain(cam: Cam, theme: PlaneTheme, dist: Float) {
    val w = size.width
    drawRect(theme.terrain, Offset(0f, cam.horizon), Size(w, size.height - cam.horizon))
    val nearRel = -3f
    val farRel = Cam.VIEW
    val first = floor((dist + nearRel) / BAND).toInt()
    val last = floor((dist + farRel) / BAND).toInt()
    val cell = 1.1f
    for (k in last downTo first) {
        val rel0 = max(k * BAND - dist, nearRel)
        val rel1 = min(k * BAND + BAND - dist, farRel)
        if (rel1 <= rel0) continue
        val y0 = cam.yTerrain(rel0)
        val y1 = cam.yTerrain(rel1)
        drawRect(if (k % 2 == 0) theme.terrainAlt else theme.terrain, Offset(0f, y1), Size(w, y0 - y1 + 1.5f))
        // seules les cases qui tombent à l'écran : à cette distance l'écran montre de -1/s à +1/s unités
        val reach = 1.05f / cam.s(rel1.coerceAtLeast(-2f)) + cell
        for (j in floor(-reach / cell).toInt()..floor(reach / cell).toInt()) {
            val c = theme.patches[hash(k, j + 400) % theme.patches.size]
            val path = Path().apply {
                moveTo(cam.sx(j * cell, rel0), y0)
                lineTo(cam.sx((j + 1) * cell, rel0), y0)
                lineTo(cam.sx((j + 1) * cell, rel1), y1)
                lineTo(cam.sx(j * cell, rel1), y1)
                close()
            }
            drawPath(path, c)
        }
    }
}

/** Un bandeau posé sur le sol : de x0 à x1 (unités de ciel), de la distance rel0 à rel1. */
private fun DrawScope.groundQuad(cam: Cam, x0: Float, x1: Float, rel0: Float, rel1: Float, color: Color) {
    if (rel1 < -3f || rel0 > Cam.VIEW) return
    val a = max(rel0, -3f)
    val b = min(rel1, Cam.VIEW)
    val p = Path().apply {
        moveTo(cam.sx(x0, a), cam.yTerrain(a))
        lineTo(cam.sx(x1, a), cam.yTerrain(a))
        lineTo(cam.sx(x1, b), cam.yTerrain(b))
        lineTo(cam.sx(x0, b), cam.yTerrain(b))
        close()
    }
    drawPath(p, color)
}

/** La piste de l'aéroport (bitume, bandes blanches, seuil, marques de toucher, feux de bord) et les feux d'approche qui y mènent. */
private fun DrawScope.drawAirfield(cam: Cam, theme: PlaneTheme, dist: Float, length: Float, time: Float) {
    val start = length - RUNWAY_BEFORE - dist
    val end = length + RUNWAY_AFTER - dist
    if (start > Cam.VIEW + 140f) return
    val asphalt = if (theme.night) Color(0xFF2B2E4A) else Color(0xFF454A62)
    val paint = Color(0xFFF4F1E4)
    val half = 0.46f
    // les feux d'approche : une file de points sur l'axe de la piste, qui clignotent l'un après l'autre
    var z = -(RUNWAY_BEFORE + 84f)
    var i = 0
    while (z < -RUNWAY_BEFORE) {
        val rel = length + z - dist
        if (rel > -2f && rel < Cam.VIEW) {
            val on = ((time * 6f).toInt() + i) % 6 == 0
            val u = cam.u(rel)
            val c = Offset(cam.sx(0f, rel), cam.yTerrain(rel))
            drawCircle(Color(0xFFFFE9A0).copy(alpha = if (on) 0.5f else 0.18f), u * 0.05f, c)
            drawCircle(if (on) Color.White else Color(0xFFFFD36B), u * 0.02f, c)
            // et les deux petites barres de travers tous les 24 m
            if (i % 4 == 0) {
                for (side in intArrayOf(-1, 1)) drawCircle(Color(0xFFFFB0A0).copy(alpha = 0.9f), u * 0.014f, Offset(cam.sx(side * 0.1f, rel), cam.yTerrain(rel)))
            }
        }
        z += 6f
        i++
    }
    // le tablier de la piste et ses lignes
    groundQuad(cam, -half - 0.05f, half + 0.05f, start, end, asphalt.copy(alpha = 1f))
    groundQuad(cam, -half, -half + 0.022f, start, end, paint.copy(alpha = 0.9f))
    groundQuad(cam, half - 0.022f, half, start, end, paint.copy(alpha = 0.9f))
    // le seuil : huit barres
    for (b in 0 until 8) {
        val x0 = -half + 0.07f + b * (2f * (half - 0.07f) / 8f)
        groundQuad(cam, x0, x0 + 0.06f, start + 1f, start + 7f, paint)
    }
    // l'axe : des tirets de 8 m tous les 16 m
    var d = RUNWAY_BEFORE + 12f
    while (d < RUNWAY_BEFORE + RUNWAY_AFTER - 6f) {
        groundQuad(cam, -0.012f, 0.012f, length - RUNWAY_BEFORE + (d - RUNWAY_BEFORE) - dist, length - RUNWAY_BEFORE + (d - RUNWAY_BEFORE) - dist + 8f, paint.copy(alpha = 0.9f))
        d += 16f
    }
    // les marques de toucher : deux barres de chaque côté, là où les roues se posent
    for (side in intArrayOf(-1, 1)) for (m in 0 until 2) {
        val x0 = side * (0.16f + m * 0.09f)
        val a = length - dist + 4f
        groundQuad(cam, min(x0, x0 + 0.06f), max(x0, x0 + 0.06f), a, a + 12f, paint)
    }
    // les feux de bord de piste
    var l = 0f
    while (l < RUNWAY_BEFORE + RUNWAY_AFTER) {
        val rel = length - RUNWAY_BEFORE + l - dist
        if (rel > -2f && rel < Cam.VIEW) {
            val u = cam.u(rel)
            for (side in intArrayOf(-1, 1)) {
                val c = Offset(cam.sx(side * (half + 0.06f), rel), cam.yTerrain(rel))
                drawCircle(Color(0xFFFFE066).copy(alpha = if (theme.night) 0.5f else 0.25f), u * 0.045f, c)
                drawCircle(Color(0xFFFFF3B0), u * 0.014f, c)
            }
        }
        l += 14f
    }
}

/** Les voies du ciel : de fins tirets blancs, à la hauteur de l'avion, qui séparent les cinq voies. Les obstacles sont toujours au milieu d'une voie. */
private fun DrawScope.drawLanes(cam: Cam, dist: Float) {
    val w = PlaneWorld.LANE_WIDTH
    val spacing = 12f
    val first = floor((dist - 3f) / spacing).toInt()
    val last = floor((dist + Cam.VIEW) / spacing).toInt()
    for (k in first..last) {
        val rel0 = max(k * spacing - dist, -1.5f)
        val rel1 = k * spacing - dist + 4f
        if (rel1 <= rel0 || rel0 > Cam.VIEW) continue
        val y0 = cam.yFlight(rel0)
        val y1 = cam.yFlight(rel1)
        val width = max(1.5f, cam.u(rel0) * 0.018f)
        for (b in -2..3) {
            val x = (b - 0.5f) * w
            val edge = b == -2 || b == 3
            drawLine(Color.White.copy(alpha = if (edge) 0.38f else 0.28f), Offset(cam.sx(x, rel0), y0), Offset(cam.sx(x, rel1), y1), strokeWidth = width, cap = StrokeCap.Round)
        }
    }
}

/** La brume de l'horizon : les choses lointaines s'y fondent. */
private fun DrawScope.drawFog(cam: Cam, theme: PlaneTheme) {
    val top = cam.horizon - cam.height * 0.05f
    val bottom = cam.yFlight(36f)
    val mid = (cam.horizon - top) / (bottom - top)
    drawRect(
        Brush.verticalGradient(0f to theme.fog.copy(alpha = 0f), mid to theme.fog.copy(alpha = 0.93f), 1f to theme.fog.copy(alpha = 0f), startY = top, endY = bottom),
        Offset(0f, top), Size(size.width, bottom - top),
    )
}

// ------------------------------------------------------------------ les pièces

private fun DrawScope.drawPiece(cam: Cam, piece: Piece, rel: Float, knock: PlaneRun.Knock?, time: Float, theme: PlaneTheme) {
    val u = cam.u(rel)
    var x = piece.x
    val by = cam.yFlight(rel)
    var drop = 0f
    var tilt = 0f
    var alpha = 1f
    if (knock != null) {
        // projeté sur le côté en tournant et en tombant, puis il s'efface
        val t = knock.age
        x += knock.direction * t * 2.2f
        drop = t * t * 1.6f
        tilt = knock.direction * min(t * 380f, 200f)
        alpha = (1f - t / 0.9f).coerceIn(0f, 1f)
    }
    // une fois dépassée, la chose s'efface avant de devenir énorme près de la caméra
    if (rel < -0.6f) alpha *= (1f - (-rel - 0.6f) / 1.2f).coerceIn(0f, 1f)
    if (alpha <= 0f) return
    val bx = cam.sx(x, rel)
    val cy = by + drop * u
    val bounds = Rect(bx - u * 1.2f, cy - u * 1.2f, bx + u * 1.2f, cy + u * 1.2f)
    withAlpha(alpha, bounds) {
        if (piece.kind == PieceKind.RING) {
            // l'anneau grandit un peu et brille plus fort quand on le traverse
            val near = ((-rel) / 0.6f).coerceIn(0f, 1f)
            drawRing(bx, by, u * (1f + 0.5f * near), time)
        } else {
            // l'ombre sur le sol, très loin dessous : elle montre la voie
            drawGroundShadow(bx, cam.yTerrain(rel), u, piece.kind.width * 0.55f)
            rotate(tilt, Offset(bx, cy)) { drawObstacle(piece.kind, piece.variant, bx, cy, u * SPRITE_SCALE, time, piece.id) }
        }
    }
}

// ------------------------------------------------------------------ l'avion

private fun DrawScope.drawThePlane(cam: Cam, run: PlaneRun, color: Color) {
    val u = cam.u(0f)
    val bx = cam.sx(run.x, 0f)
    val bob = if (run.altitude > 0.3f) sin(run.time * 2.3f) * 0.012f * u else 0f
    val cy = cam.flight + bob
    val bank = ((run.targetX - run.x) * 42f).coerceIn(-24f, 24f)
    val roll = run.rollProgress.let { it * it * (3f - 2f * it) }
    val gear = if (run.altitude < 0.45f) 1f else 0f
    // clignote après un choc : très pâle une fois sur deux (dix fois par seconde)
    val blink = if (run.hurt && ((run.hurtLeft * 10f).toInt() % 2 == 0)) 0.28f else 1f
    // l'ombre de l'avion sur le sol, qui monte à sa rencontre quand il descend
    drawGroundShadow(bx, cam.yTerrain(0f), u, 0.2f, strength = 0.3f)
    val bounds = Rect(bx - u, cy - u, bx + u, cy + u)
    withAlpha(blink, bounds) { drawPlayerPlane(bx, cy, u, color, bank, roll, gear, run.time) }
    if (run.sinceHit < 0.7f) drawSmoke(bx, cy + u * 0.02f, u, run.sinceHit)
    // les roues touchent la piste : un peu de fumée de gomme
    if (run.sinceTouchdown < 0.9f) {
        val p = run.sinceTouchdown / 0.9f
        for (side in intArrayOf(-1, 1)) for (k in 0 until 3) {
            val c = Offset(bx + side * u * (0.075f + 0.05f * p * (k + 1)), cy + u * (0.11f - 0.03f * p * k))
            drawCircle(Color.White.copy(alpha = (0.5f * (1f - p)).coerceAtLeast(0f)), u * (0.025f + 0.04f * p), c)
        }
    }
}

/** Deux chevrons blancs qui s'écartent de part et d'autre de l'avion : « glisse le doigt à gauche et à droite ». */
private fun DrawScope.drawSteerHint(cam: Cam, run: PlaneRun) {
    val u = cam.u(0f)
    val y = cam.flight
    val pulse = 0.5f + 0.5f * sin(run.time * 5f + 1f)
    val gap = u * (0.34f + 0.08f * pulse)
    val h = u * 0.1f
    for (side in intArrayOf(-1, 1)) {
        val x = cam.sx(run.x, 0f) + side * gap
        val path = Path().apply {
            moveTo(x, y - h)
            lineTo(x + side * h * 0.8f, y)
            lineTo(x, y + h)
        }
        drawPath(path, Color(0xFF2B2D42).copy(alpha = 0.55f), style = Stroke(u * 0.075f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(path, Color.White.copy(alpha = 0.7f + 0.3f * pulse), style = Stroke(u * 0.045f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

// ------------------------------------------------------------------ les effets

private fun DrawScope.drawEffects(cam: Cam, run: PlaneRun) {
    val u = cam.u(0f)
    val bx = cam.sx(run.x, 0f)
    val cy = cam.flight
    val t = run.sinceHit
    if (t < 0.5f) {
        // l'éclat du choc : une étoile pointue jaune et orange qui grossit et s'efface
        val p = t / 0.5f
        val r = u * (0.22f + 0.4f * p)
        val center = Offset(bx, cy)
        withAlpha(1f - p, Rect(center.x - r * 1.4f, center.y - r * 1.4f, center.x + r * 1.4f, center.y + r * 1.4f)) {
            burst(center, r, Color(0xFFFF8A1F), 11, 0.55f, 0f)
            burst(center, r * 0.68f, Color(0xFFFFE066), 11, 0.55f, 0.5f)
        }
        // l'étoile perdue s'envole
        val star = Offset(bx, cy - u * 0.4f - u * 1.1f * p)
        withAlpha(1f - p * p, Rect(star.x - u * 0.3f, star.y - u * 0.3f, star.x + u * 0.3f, star.y + u * 0.3f)) {
            drawStar(star, u * 0.17f, on = true)
        }
    }
    // le tonneau : un cercle de lumière qui s'élargit et des étincelles qui tournent autour de l'avion
    run.rollTime?.let { rt ->
        val p = (rt / 0.55f).coerceIn(0f, 1f)
        if (p < 1f) drawCircle(Color(0xFFFFE066).copy(alpha = 0.6f * (1f - p)), u * (0.25f + 0.9f * p), Offset(bx, cy), style = Stroke(u * 0.04f))
        for (k in 0 until 8) {
            val a = run.rollProgress * 2f * PI.toFloat() * 2f + k * PI.toFloat() / 4f
            val r = u * (0.32f + 0.05f * sin(a * 3f))
            val c = Offset(bx + cos(a) * r, cy + sin(a) * r * 0.8f)
            drawCircle(Color(0xFFFFE066), u * 0.028f, c)
            drawCircle(Color.White, u * 0.012f, c)
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
private fun DrawScope.drawHitFlash(run: PlaneRun) {
    if (run.sinceHit >= 0.45f) return
    val a = 1f - run.sinceHit / 0.45f
    val radius = max(size.width, size.height) * 0.75f
    drawRect(Brush.radialGradient(0f to Color.Transparent, 0.5f to Color.Transparent, 1f to Color(0xFFFF3B30).copy(alpha = 0.42f * a), center = Offset(size.width / 2f, size.height * 0.55f), radius = radius))
}

/** Où se trouve l'avion à l'écran (pour poser les textes qui flottent au-dessus de lui). */
internal fun planeScreenPosition(width: Float, height: Float, run: PlaneRun): Offset {
    val cam = Cam(width, height, run.altitude)
    return Offset(cam.sx(run.x, 0f), cam.flight - 0.5f * cam.unit)
}

internal fun planeUnitPixels(width: Float, height: Float): Float = Cam(width, height).unit

/** Un fond de ciel sans vol, pour le menu : le ciel, le sol et la brume, immobiles. */
internal fun DrawScope.drawStillSky(theme: PlaneTheme) {
    val cam = Cam(size.width, size.height)
    drawSky(cam, theme)
    drawTerrain(cam, theme, 0f)
    drawFog(cam, theme)
}

/** La couverture du jeu dans le menu : l'avion, un anneau lumineux devant lui, une montgolfière, un banc d'oiseaux et un nuage, au-dessus de la campagne. */
internal fun DrawScope.drawPlaneCover(theme: PlaneTheme, color: Color) {
    val w = size.width
    val h = size.height
    drawRect(Brush.verticalGradient(listOf(theme.skyTop, theme.skyBottom), startY = 0f, endY = h * 0.72f), Offset.Zero, size)
    theme.sun?.let { drawCircle(it.copy(alpha = 0.35f), h * 0.3f, Offset(w * 0.84f, h * 0.2f)); drawCircle(it, h * 0.17f, Offset(w * 0.84f, h * 0.2f)) }
    // le sol, en bas : deux bandes de champs
    drawRect(theme.terrain, Offset(0f, h * 0.8f), Size(w, h * 0.2f))
    for (j in 0 until 9) drawRect(theme.patches[j % theme.patches.size], Offset(j * w / 8f - w * 0.02f, h * 0.84f), Size(w / 8f + 1f, h * 0.16f))
    drawRect(theme.terrainAlt, Offset(0f, h * 0.8f), Size(w, h * 0.04f))
    drawGroundWindmillAndTree(w, h, theme)
    // un nuage de chaque côté
    drawCloud(w * 0.16f, h * 0.3f, h * 0.7f, 0, Color.White.copy(alpha = 0.95f))
    drawCloud(w * 0.7f, h * 0.17f, h * 0.55f, 1, Color.White.copy(alpha = 0.9f))
    // les obstacles, de loin en près, et l'avion
    drawBirds(w * 0.78f, h * 0.45f, h * 1.05f, 0, 0.4f, 1)
    drawBalloon(w * 0.1f, h * 0.55f, h * 1.25f, 0, 0.7f)
    drawRing(w * 0.62f, h * 0.5f, h * 1.2f, 0.3f)
    drawPlayerPlane(w * 0.5f, h * 0.58f, h * 1.55f, color, 8f, 0f, 0f, 0.2f)
}

private fun DrawScope.drawGroundWindmillAndTree(w: Float, h: Float, theme: PlaneTheme) {
    drawGroundTree(w * 0.2f, h * 0.9f, h * 0.9f, 0, false)
    drawGroundHouse(w * 0.34f, h * 0.93f, h * 0.9f, 0, false)
    drawWindmill(w * 0.9f, h * 0.9f, h * 0.9f, 0.4f, false)
}
