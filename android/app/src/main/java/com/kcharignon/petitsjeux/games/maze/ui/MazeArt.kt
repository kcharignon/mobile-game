package com.kcharignon.petitsjeux.games.maze.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import com.kcharignon.petitsjeux.games.maze.engine.Cell
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevel
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

// Le récif de Poisson Perdu, dessiné par le code (Canvas), aucune image. Les formes, les proportions et les couleurs sont celles des maquettes
// (docs/design/10-recif-planche-style.png, tools/design/recif.js) : si on change un dessin ici, on le change là-bas.

private fun Color.a(k: Float): Color = copy(alpha = alpha * k)

private val Orange = Color(0xFFFF8A1F)
private val Gold = Color(0xFFFFD54A)
private val GoldEdge = Color(0xFFC98A00)

// ---------------------------------------------------------------- les murs : coraux épais à bouts ronds, contour obligatoire

internal class WallSeg(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val horizontal: Boolean, val color: Color, val polyp: Boolean)
internal class WallBlob(val vx: Int, val vy: Int, val big: Boolean, val color: Color)

/** Les murs d'un niveau, calculés une fois : segments entre deux sommets de la grille, et têtes de corail aux jonctions. */
internal class WallShapes(val segs: List<WallSeg>, val blobs: List<WallBlob>)

internal fun wallShapes(level: MazeLevel, theme: MazeTheme): WallShapes {
    fun colorAt(mx: Double, my: Double): Color = theme.walls[(floor(mx / 2).toInt() + floor(my / 2).toInt() + 30) % 3]
    val segs = ArrayList<WallSeg>()
    // pour chaque sommet : nombre de segments, y en a-t-il d'horizontaux, de verticaux
    val verts = HashMap<Long, IntArray>()
    fun addVertex(x: Int, y: Int, horizontal: Boolean) {
        val e = verts.getOrPut(x * 1000L + y) { IntArray(3) }
        e[0]++
        e[if (horizontal) 1 else 2] = 1
    }
    for (r in 0..level.rows) for (x in 0 until level.cols) if (level.wallAbove(x, r)) {
        segs += WallSeg(x, r, x + 1, r, true, colorAt(x + 0.5, r.toDouble()), hash01(x * 10 + r, 1) > 0.62)
        addVertex(x, r, true)
        addVertex(x + 1, r, true)
    }
    for (r in 0 until level.rows) for (x in 0..level.cols) if (level.wallLeftOf(x, r)) {
        segs += WallSeg(x, r, x, r + 1, false, colorAt(x.toDouble(), r + 0.5), hash01(x * 10 + r, 2) > 0.62)
        addVertex(x, r, false)
        addVertex(x, r + 1, false)
    }
    val blobs = ArrayList<WallBlob>()
    for ((key, e) in verts) {
        val x = (key / 1000).toInt()
        val y = (key % 1000).toInt()
        val corner = e[0] == 2 && e[1] == 1 && e[2] == 1
        if (e[0] >= 3 || (corner && hash01(x, y) > 0.55)) blobs += WallBlob(x, y, e[0] >= 3, colorAt(x.toDouble(), y.toDouble()))
    }
    return WallShapes(segs, blobs.sortedWith(compareBy({ it.vy }, { it.vx })))
}

internal fun DrawScope.drawWalls(shapes: WallShapes, layout: MazeLayout, theme: MazeTheme) {
    val s = layout.cell
    val t = s * 0.26f
    val rim = 1.8f * density
    val ledge = max(3f * density, s * 0.07f)
    fun p(vx: Int, vy: Int, dy: Float = 0f) = Offset(layout.originX + vx * s, layout.originY + vy * s + dy)
    fun line(sg: WallSeg, width: Float, color: Color, dy: Float = 0f) =
        drawLine(color, p(sg.x1, sg.y1, dy), p(sg.x2, sg.y2, dy), width, StrokeCap.Round)
    for (sg in shapes.segs) line(sg, t + 2 * rim, lerp(theme.wallEdge(sg.color), Color.Black, 0.3f), ledge)
    for (b in shapes.blobs) drawCircle(lerp(theme.wallEdge(b.color), Color.Black, 0.3f), t * (if (b.big) 0.74f else 0.68f) + rim, p(b.vx, b.vy, ledge))
    for (sg in shapes.segs) line(sg, t + 2 * rim, theme.wallEdge(sg.color))
    for (b in shapes.blobs) drawCircle(theme.wallEdge(b.color), t * (if (b.big) 0.74f else 0.68f) + rim, p(b.vx, b.vy))
    for (sg in shapes.segs) line(sg, t, sg.color)
    for (b in shapes.blobs) drawCircle(b.color, t * (if (b.big) 0.74f else 0.68f), p(b.vx, b.vy))
    // reflets, et quelques points de polypes
    val off = t * 0.16f
    val ins = t * 0.3f
    for (sg in shapes.segs) {
        val hl = lerp(sg.color, Color.White, 0.5f).a(0.6f)
        val a = p(sg.x1, sg.y1)
        val b = p(sg.x2, sg.y2)
        if (sg.horizontal) drawLine(hl, Offset(a.x + ins, a.y - off), Offset(b.x - ins, b.y - off), t * 0.26f, StrokeCap.Round)
        else drawLine(hl, Offset(a.x - off, a.y + ins), Offset(b.x - off, b.y - ins), t * 0.26f, StrokeCap.Round)
        if (sg.polyp && s >= 30f) drawCircle(lerp(sg.color, Color.White, 0.75f).a(0.85f), t * 0.1f, Offset((a.x + b.x) / 2 + if (sg.horizontal) 0f else -off, (a.y + b.y) / 2))
    }
    for (b in shapes.blobs) {
        val r = t * (if (b.big) 0.74f else 0.68f)
        val c = p(b.vx, b.vy)
        drawCircle(lerp(b.color, Color.White, 0.55f).a(0.7f), r * 0.3f, Offset(c.x - r * 0.28f, c.y - r * 0.3f))
    }
}

// ---------------------------------------------------------------- le sol, la bouée, les objets du fond des impasses

internal fun DrawScope.drawFloor(level: MazeLevel, layout: MazeLayout, theme: MazeTheme) {
    val s = layout.cell
    drawRect(theme.floor, Offset(layout.originX, layout.originY), Size(level.cols * s, level.rows * s))
    val alt = lerp(theme.floor, Color.White, 0.35f).a(if (luminance(theme.floor) < 0.2) 0.14f else 0.55f)
    for (y in 0 until level.rows) for (x in 0 until level.cols) {
        if ((x + y) % 2 == 1) drawRoundRect(alt, Offset(layout.originX + x * s + 1f, layout.originY + y * s + 1f), Size(s - 2f, s - 2f), CornerRadius(s * 0.12f))
        if (s >= 22f * density) for (i in 0 until 2) {
            val hx = hash01(x * 7 + i, y * 13 + 5)
            val hy = hash01(x * 11 + i, y * 17 + 9)
            if (hx > 0.45) drawCircle(theme.sand.a(0.8f), max(0.9f * density, s * 0.03f), Offset(layout.originX + (x + 0.15f + 0.7f * hx.toFloat()) * s, layout.originY + (y + 0.15f + 0.7f * hy.toFloat()) * s))
        }
    }
}

/** La lumière qui sort par l'ouverture de la sortie, quand elle est dans le bord du haut. */
internal fun DrawScope.drawExitLight(level: MazeLevel, layout: MazeLayout) {
    if (!level.exitOnBorder) return
    val s = layout.cell
    val ex = layout.center(level.exit).x
    val top = layout.originY
    val path = Path().apply {
        moveTo(ex - s * 0.32f, top); lineTo(ex + s * 0.32f, top); lineTo(ex + s * 0.7f, top - s); lineTo(ex - s * 0.7f, top - s); close()
    }
    drawPath(path, Brush.verticalGradient(listOf(Color(0xFFFFF2A8).a(0f), Color(0xFFFFF2A8).a(0.65f)), startY = top - s, endY = top))
}

internal fun DrawScope.drawBuoy(c: Offset, s: Float) {
    val r = 0.17f * s
    val w = 0.1f * s
    drawOval(Color.Black.a(0.18f), Offset(c.x - r * 1.2f, c.y + r + w * 0.9f - r * 0.38f), Size(r * 2.4f, r * 0.76f))
    drawCircle(Ink.a(0.45f), r, c, style = Stroke(w + 3f * density))
    drawCircle(Color.White, r, c, style = Stroke(w))
    // huit secteurs, un sur deux en rouge
    for (i in 0 until 8 step 2) drawArc(Color(0xFFE8362F), i * 45f, 45f, false, Offset(c.x - r, c.y - r), Size(2 * r, 2 * r), style = Stroke(w))
}

internal class MotifSpot(val cell: Cell, val dx: Float, val dy: Float, val k: Float)

/** Où poser les petits objets du décor : au fond de certaines impasses seulement (jamais sur la solution : ils la révéleraient). */
internal fun motifSpots(level: MazeLevel): List<MotifSpot> = level.deadEnds.mapIndexedNotNull { i, c ->
    if (hash01(c.x * 31 + 7, c.y * 17 + 3) < 0.28) null
    else MotifSpot(c, ((hash01(c.x, c.y) - 0.5) * 0.12).toFloat(), ((hash01(c.y, c.x) - 0.5) * 0.12).toFloat(), hash01(c.x + i, c.y).toFloat())
}

internal fun DrawScope.drawMotif(kind: MazeMotif, c: Offset, s: Float, k: Float, alpha: Float = 1f) {
    val line = max(1f * density, 0.035f * s)
    fun fill(color: Color) = color.a(alpha)
    when (kind) {
        MazeMotif.SHELL -> shell(c, s, alpha, line)
        MazeMotif.STARFISH -> {
            val path = Path()
            for (i in 0 until 10) {
                val a = -PI / 2 + i * PI / 5 + 0.2 * (k - 0.5)
                val r = (if (i % 2 == 1) 0.09f else 0.23f) * s
                val x = c.x + cos(a).toFloat() * r
                val y = c.y + sin(a).toFloat() * r
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, fill(Color(0xFFFF9A4D)))
            drawPath(path, fill(Color(0xFFC9591A)), style = Stroke(line * 1.1f, join = StrokeJoin.Round))
            for (i in 0 until 3) drawCircle(fill(Color(0xFFFFE0B8)), 0.018f * s, Offset(c.x + (i - 1) * 0.06f * s, c.y + (if (i % 2 == 1) 0.03f else -0.02f) * s))
        }
        MazeMotif.SEAWEED -> {
            val cols = listOf(Color(0xFF2F9E5B), Color(0xFF52B96E), Color(0xFF1F8A55))
            for ((i, dx) in listOf(-0.1f, 0f, 0.1f).withIndex()) {
                val p = Path().apply {
                    moveTo(c.x + dx * s, c.y + 0.22f * s)
                    quadraticTo(c.x + (dx - 0.12f + i * 0.05f) * s, c.y, c.x + (dx + 0.04f) * s, c.y - (0.2f + 0.05f * i) * s)
                }
                drawPath(p, fill(cols[i]), style = Stroke(0.075f * s, cap = StrokeCap.Round))
            }
        }
        MazeMotif.PLANK -> rotate(-24f + 40f * k, c) {
            drawRoundRect(fill(Color(0xFFB07A4B)), Offset(c.x - 0.24f * s, c.y - 0.08f * s), Size(0.48f * s, 0.16f * s), CornerRadius(0.04f * s))
            drawRoundRect(fill(Color(0xFF5E3B1F)), Offset(c.x - 0.24f * s, c.y - 0.08f * s), Size(0.48f * s, 0.16f * s), CornerRadius(0.04f * s), style = Stroke(line))
            drawLine(fill(Color(0xFF5E3B1F)).a(0.5f), Offset(c.x - 0.2f * s, c.y), Offset(c.x + 0.2f * s, c.y), line * 0.6f)
            drawCircle(fill(Color(0xFF5E3B1F)), 0.02f * s, Offset(c.x - 0.18f * s, c.y))
            drawCircle(fill(Color(0xFF5E3B1F)), 0.02f * s, Offset(c.x + 0.18f * s, c.y))
        }
        MazeMotif.CRYSTAL -> for ((spot, col) in listOf(
            Triple(-0.1f, 0.02f, 0.12f) to Color(0xFF8E6CF2), Triple(0.08f, -0.02f, 0.15f) to Color(0xFF4FB6E8), Triple(0f, 0.08f, 0.09f) to Color(0xFFC77DFF),
        )) {
            val (d, e, hh) = spot
            val p = Path().apply {
                moveTo(c.x + d * s, c.y + (e - hh * 1.3f) * s); lineTo(c.x + (d + hh * 0.55f) * s, c.y + e * s)
                lineTo(c.x + d * s, c.y + (e + hh * 0.8f) * s); lineTo(c.x + (d - hh * 0.55f) * s, c.y + e * s); close()
            }
            drawPath(p, fill(col))
            drawPath(p, fill(lerp(col, Color.Black, 0.35f)), style = Stroke(line, join = StrokeJoin.Round))
            drawLine(Color.White.a(0.6f * alpha), Offset(c.x + d * s, c.y + (e - hh * 1.3f) * s), Offset(c.x + (d - hh * 0.2f) * s, c.y + e * s), line * 0.7f)
        }
        MazeMotif.ICE -> rotate(-10f + 20f * k, c) {
            drawRoundRect(fill(Color(0xFFEAF8FF)), Offset(c.x - 0.17f * s, c.y - 0.17f * s), Size(0.34f * s, 0.34f * s), CornerRadius(0.07f * s))
            drawRoundRect(fill(Color(0xFF6FA9CF)), Offset(c.x - 0.17f * s, c.y - 0.17f * s), Size(0.34f * s, 0.34f * s), CornerRadius(0.07f * s), style = Stroke(line * 1.1f))
            drawLine(Color.White.a(alpha), Offset(c.x - 0.09f * s, c.y - 0.08f * s), Offset(c.x - 0.02f * s, c.y - 0.12f * s), 0.04f * s, StrokeCap.Round)
        }
        MazeMotif.ROCK -> {
            drawOval(fill(Color(0xFF4A3B47)), Offset(c.x - 0.2f * s, c.y - 0.16f * s), Size(0.4f * s, 0.32f * s))
            drawOval(fill(Ink), Offset(c.x - 0.2f * s, c.y - 0.16f * s), Size(0.4f * s, 0.32f * s), style = Stroke(line))
            val crack = Path().apply {
                moveTo(c.x - 0.1f * s, c.y - 0.02f * s); lineTo(c.x - 0.02f * s, c.y + 0.03f * s); lineTo(c.x + 0.05f * s, c.y - 0.05f * s); lineTo(c.x + 0.12f * s, c.y + 0.02f * s)
            }
            drawPath(crack, fill(Color(0xFFFF7A3D)), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
        MazeMotif.ANEMONE -> {
            for (i in 0 until 9) {
                val a = i * 2 * PI / 9
                drawLine(fill(if (i % 2 == 1) Color(0xFFFF9B6B) else Color(0xFFFF6FA0)), c, Offset(c.x + cos(a).toFloat() * 0.2f * s, c.y + sin(a).toFloat() * 0.2f * s), 0.07f * s, StrokeCap.Round)
            }
            drawCircle(fill(Color(0xFFC23A78)), 0.08f * s, c)
        }
        MazeMotif.JELLY -> {
            drawCircle(Brush.radialGradient(listOf(Color(0xFF9BF0FF).a(0.8f * alpha), Color(0xFF9BF0FF).a(0f)), c, 0.34f * s), 0.34f * s, c)
            for (dx in listOf(-0.1f, -0.035f, 0.035f, 0.1f)) {
                val p = Path().apply {
                    moveTo(c.x + dx * s, c.y + 0.02f * s)
                    quadraticTo(c.x + (dx + 0.04f) * s, c.y + 0.09f * s, c.x + dx * s, c.y + 0.16f * s)
                    quadraticTo(c.x + (dx - 0.04f) * s, c.y + 0.22f * s, c.x + dx * s, c.y + 0.26f * s)
                }
                drawPath(p, fill(Color(0xFFC9F6FF)).a(0.9f), style = Stroke(0.03f * s, cap = StrokeCap.Round))
            }
            val bell = Path().apply {
                moveTo(c.x - 0.15f * s, c.y + 0.03f * s)
                arcTo(Rect(c.x - 0.15f * s, c.y - 0.12f * s, c.x + 0.15f * s, c.y + 0.18f * s), 180f, 180f, false)
                close()
            }
            drawPath(bell, fill(Color(0xFF7FE8FF)))
            drawPath(bell, fill(Color(0xFFE8FFFF)), style = Stroke(line, join = StrokeJoin.Round))
        }
        MazeMotif.PEARL -> {
            shell(Offset(c.x, c.y + 0.03f * s), s, alpha, line)
            drawCircle(fill(Color.White), 0.075f * s, Offset(c.x, c.y - 0.05f * s))
            drawCircle(fill(Color(0xFFBFA5D9)), 0.075f * s, Offset(c.x, c.y - 0.05f * s), style = Stroke(max(0.8f * density, 0.025f * s)))
            drawCircle(fill(Color.White), 0.02f * s, Offset(c.x - 0.025f * s, c.y - 0.075f * s))
        }
    }
}

private fun DrawScope.shell(c: Offset, s: Float, alpha: Float, line: Float) {
    val p = Path().apply {
        moveTo(c.x - 0.2f * s, c.y + 0.16f * s)
        quadraticTo(c.x - 0.27f * s, c.y - 0.12f * s, c.x - 0.12f * s, c.y - 0.2f * s)
        quadraticTo(c.x, c.y - 0.28f * s, c.x + 0.12f * s, c.y - 0.2f * s)
        quadraticTo(c.x + 0.27f * s, c.y - 0.12f * s, c.x + 0.2f * s, c.y + 0.16f * s)
        close()
    }
    drawPath(p, Color(0xFFFFD6E0).a(alpha))
    drawPath(p, Color(0xFFD9577F).a(alpha), style = Stroke(line, join = StrokeJoin.Round))
    for (dx in listOf(-0.12f, 0f, 0.12f)) drawLine(Color(0xFFD9577F).a(0.7f * alpha), Offset(c.x + dx * 0.4f * s, c.y + 0.13f * s), Offset(c.x + dx * 1.5f * s, c.y - 0.16f * s), line * 0.7f, StrokeCap.Round)
    drawRoundRect(Color(0xFFD9577F).a(alpha), Offset(c.x - 0.07f * s, c.y + 0.13f * s), Size(0.14f * s, 0.07f * s), CornerRadius(0.03f * s))
}

// ---------------------------------------------------------------- le poisson-clown (vu du dessus, le nez vers le haut) et son anémone

/**
 * Le poisson, centré en [c], de longueur environ `s` ; [angle] en degrés (0 = nez vers le haut, 90 = vers la droite) ; [wiggle] (de -1 à 1) bat la queue et les nageoires.
 */
internal fun DrawScope.drawFish(c: Offset, s: Float, angle: Float, scale: Float = 1f, wiggle: Float = 0f, shadow: Boolean = true) {
    val u = s * scale
    val line = max(1.2f * density, 0.04f * u)
    val fin = Orange.let { lerp(it, Color.White, 0.12f) }
    translate(c.x, c.y) {
        rotate(angle, Offset.Zero) {
            if (shadow) drawOval(Color.Black.a(0.16f), Offset(-0.29f * u, (0.08f - 0.5f) * u), Size(0.58f * u, 1.0f * u))
            // la queue
            rotate(wiggle * 12f, Offset(0f, 0.3f * u)) {
                val tail = Path().apply {
                    moveTo(0f, 0.3f * u)
                    cubicTo(-0.06f * u, 0.4f * u, -0.25f * u, 0.5f * u, -0.23f * u, 0.68f * u)
                    quadraticTo(0f, 0.6f * u, 0.23f * u, 0.68f * u)
                    cubicTo(0.25f * u, 0.5f * u, 0.06f * u, 0.4f * u, 0f, 0.3f * u)
                    close()
                }
                drawPath(tail, lerp(Orange, Color.White, 0.1f))
                drawPath(tail, Ink, style = Stroke(line, join = StrokeJoin.Round))
            }
            // les nageoires
            for (sx in listOf(-1f, 1f)) {
                val pivot = Offset(0.3f * u * sx, -0.07f * u)
                rotate(32f * sx + wiggle * 10f * sx, pivot) {
                    drawOval(fin, Offset(pivot.x - 0.1f * u, pivot.y - 0.18f * u), Size(0.2f * u, 0.36f * u))
                    drawOval(Ink, Offset(pivot.x - 0.1f * u, pivot.y - 0.18f * u), Size(0.2f * u, 0.36f * u), style = Stroke(line))
                }
            }
            // le corps, ses trois bandes blanches cernées d'encre, son contour
            val body = Path().apply { addOval(Rect(-0.25f * u, (-0.02f - 0.43f) * u, 0.25f * u, (-0.02f + 0.43f) * u)) }
            drawPath(body, Brush.radialGradient(listOf(lerp(Orange, Color.White, 0.38f), Orange, lerp(Orange, Color.Black, 0.12f)), Offset(-0.1f * u, -0.2f * u), 0.7f * u))
            clipPath(body) {
                for ((center, th) in listOf(-0.17f to 0.1f, 0.1f to 0.13f, 0.33f to 0.07f)) {
                    drawRect(Color.White, Offset(-0.3f * u, (center - th / 2) * u), Size(0.6f * u, th * u))
                    drawLine(Ink, Offset(-0.3f * u, (center - th / 2) * u), Offset(0.3f * u, (center - th / 2) * u), max(1f * density, 0.03f * u))
                    drawLine(Ink, Offset(-0.3f * u, (center + th / 2) * u), Offset(0.3f * u, (center + th / 2) * u), max(1f * density, 0.03f * u))
                }
            }
            drawPath(body, Ink, style = Stroke(line, join = StrokeJoin.Round))
            drawRoundRect(lerp(Orange, Color.Black, 0.1f).a(0.55f), Offset(-0.032f * u, -0.14f * u), Size(0.064f * u, 0.42f * u), CornerRadius(0.032f * u))
            // les yeux
            for (sx in listOf(-1f, 1f)) {
                drawCircle(Color.White, 0.064f * u, Offset(0.135f * u * sx, -0.31f * u))
                drawCircle(Ink, 0.064f * u, Offset(0.135f * u * sx, -0.31f * u), style = Stroke(max(1f * density, 0.025f * u)))
                drawCircle(Ink, 0.034f * u, Offset(0.14f * u * sx, -0.325f * u))
                drawCircle(Color.White, 0.011f * u, Offset(0.128f * u * sx, -0.337f * u))
            }
        }
    }
}

/** L'anémone : la maison du poisson, et la sortie. Elle brille et ondule ([time] en secondes). */
internal fun DrawScope.drawAnemone(c: Offset, s: Float, time: Float, glow: Boolean = true) {
    val u = s
    if (glow) {
        val pulse = 1f + 0.05f * sin(time * 2.2f)
        drawCircle(
            Brush.radialGradient(
                0f to Color(0xFFFFF2A8).a(0.95f), 0.5f to Color(0xFFFFE07A).a(0.55f), 1f to Color(0xFFFFE07A).a(0f),
                center = c, radius = u * 1.0f * pulse,
            ),
            u * pulse, c,
        )
    }
    fun ring(n: Int, r1: Float, r2: Float, w: Float, off: Double, cols: List<Color>, tip: Color) {
        for (i in 0 until n) {
            val a = off + i * 2 * PI / n
            val len = r2 * (0.9f + 0.12f * hash01(i, n).toFloat()) * (1f + 0.04f * sin(time * 2.4f + i))
            val p1 = Offset(c.x + cos(a).toFloat() * r1 * u, c.y + sin(a).toFloat() * r1 * u)
            val p2 = Offset(c.x + cos(a).toFloat() * len * u, c.y + sin(a).toFloat() * len * u)
            drawLine(Color(0xFF7A2450).a(0.55f), p1, p2, (w + 0.05f) * u, StrokeCap.Round)
            drawLine(cols[i % cols.size], p1, p2, w * u, StrokeCap.Round)
            drawCircle(tip, w * 0.5f * u, p2)
        }
    }
    ring(14, 0.1f, 0.42f, 0.11f, 0.0, listOf(Color(0xFFFF6FA0), Color(0xFFFF9B6B)), Color(0xFFFFF2A8))
    ring(10, 0.06f, 0.3f, 0.1f, PI / 10, listOf(Color(0xFFFF8FB5), Color(0xFFFFB383)), Color.White)
    drawCircle(Color(0xFFC23A78), 0.15f * u, c)
    drawCircle(Color(0xFF7A2450).a(0.6f), 0.15f * u, c, style = Stroke(0.03f * u))
    drawOval(Color(0xFF8E1F57), Offset(c.x - 0.07f * u, c.y + 0.01f * u - 0.05f * u), Size(0.14f * u, 0.1f * u))
    drawOval(Color.White.a(0.5f), Offset(c.x - 0.1f * u, c.y - 0.07f * u - 0.025f * u), Size(0.1f * u, 0.05f * u))
    for ((a, r, k) in listOf(Triple(-0.9f, 0.62f, 0.1f), Triple(0.5f, 0.66f, 0.08f), Triple(2.4f, 0.6f, 0.09f), Triple(3.7f, 0.64f, 0.07f))) {
        val tw = 0.75f + 0.25f * sin(time * 3f + a * 5f)
        drawSparkle(Offset(c.x + cos(a) * r * u, c.y + sin(a) * r * u), k * u * tw, Color.White)
    }
}

internal fun DrawScope.drawSparkle(c: Offset, r: Float, color: Color, alpha: Float = 0.95f) {
    val k = r * 0.28f
    val p = Path().apply {
        moveTo(c.x, c.y - r)
        quadraticTo(c.x + k, c.y - k, c.x + r, c.y)
        quadraticTo(c.x + k, c.y + k, c.x, c.y + r)
        quadraticTo(c.x - k, c.y + k, c.x - r, c.y)
        quadraticTo(c.x - k, c.y - k, c.x, c.y - r)
        close()
    }
    drawPath(p, color.a(alpha))
}

// ---------------------------------------------------------------- la trace : un ruban d'eau jalonné de bulles

/** Un chemin qui arrondit ses coins (rayon [r]). */
internal fun roundedPath(pts: List<Offset>, r: Float): Path {
    val path = Path()
    if (pts.isEmpty()) return path
    path.moveTo(pts[0].x, pts[0].y)
    for (i in 1 until pts.size - 1) {
        val p = pts[i - 1]
        val c = pts[i]
        val n = pts[i + 1]
        val d1 = Offset(c.x - p.x, c.y - p.y)
        val d2 = Offset(n.x - c.x, n.y - c.y)
        val l1 = hypot(d1.x, d1.y)
        val l2 = hypot(d2.x, d2.y)
        if (abs(d1.x * d2.y - d1.y * d2.x) < 1e-3f) continue
        val rr = min(r, min(l1 / 2, l2 / 2))
        path.lineTo(c.x - d1.x / l1 * rr, c.y - d1.y / l1 * rr)
        path.quadraticTo(c.x, c.y, c.x + d2.x / l2 * rr, c.y + d2.y / l2 * rr)
    }
    path.lineTo(pts.last().x, pts.last().y)
    return path
}

/** Des points régulièrement espacés de [step] le long de la ligne brisée (mesurés depuis son début : les bulles ne bougent pas quand la trace s'allonge). */
internal fun along(pts: List<Offset>, step: Float): List<Offset> {
    val out = ArrayList<Offset>()
    var carry = step * 0.5f
    for (i in 1 until pts.size) {
        val a = pts[i - 1]
        val b = pts[i]
        val len = hypot(b.x - a.x, b.y - a.y)
        if (len < 1e-3f) continue
        var d = carry
        while (d <= len) {
            out += Offset(a.x + (b.x - a.x) * d / len, a.y + (b.y - a.y) * d / len)
            d += step
        }
        carry = d - len
    }
    return out
}

/** Le ruban : contour, cœur, reflet, bulles. [gold] : la trace parfaite (aucune impasse), dorée avec des étincelles. */
internal fun DrawScope.drawRibbon(pts: List<Offset>, s: Float, theme: MazeTheme, gold: Boolean, time: Float) {
    if (pts.size < 2) return
    val tw = s * 0.34f
    val edge = if (gold) GoldEdge else theme.trailEdge
    val core = if (gold) Gold else theme.trailCore
    val path = roundedPath(pts, s * 0.3f)
    val round = Stroke(tw + 4f * density, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawPath(path, edge.a(0.95f), style = round)
    drawPath(path, core, style = Stroke(tw, cap = StrokeCap.Round, join = StrokeJoin.Round))
    translate(-tw * 0.1f, -tw * 0.1f) { drawPath(path, Color.White.a(0.45f), style = Stroke(tw * 0.3f, cap = StrokeCap.Round, join = StrokeJoin.Round)) }
    for ((i, b) in along(pts, s * 0.4f).withIndex()) {
        val off = (hash01(i, 3) - 0.5).toFloat() * tw * 0.55f
        val r = max(1.4f * density, tw * (0.1f + 0.13f * hash01(i, 5).toFloat()))
        val bob = sin(time * 2.2f + i * 1.7f) * s * 0.015f
        val c = Offset(b.x + off * 0.6f, b.y + off * 0.4f + bob)
        drawCircle(Color.White.a(0.55f), r, c)
        drawCircle(Color.White, r, c, style = Stroke(max(0.8f * density, r * 0.22f)))
        drawCircle(Color.White, r * 0.28f, Offset(c.x - r * 0.3f, c.y - r * 0.3f))
    }
    if (gold) for ((i, b) in along(pts, s * 1.1f).withIndex()) {
        val tw2 = 0.6f + 0.4f * sin(time * 4f + i * 2f)
        drawSparkle(Offset(b.x + (hash01(i, 8) - 0.5).toFloat() * tw, b.y + (hash01(i, 9) - 0.5).toFloat() * tw), tw * 0.42f * tw2, Color.White)
    }
}

/** Une bulle qui éclate (la trace a reculé) : un anneau qui grossit et s'efface, [progress] de 0 à 1. */
internal fun DrawScope.drawBurst(c: Offset, s: Float, progress: Float) {
    val p = progress.coerceIn(0f, 1f)
    drawCircle(Color.White.a(0.9f * (1f - p)), s * (0.12f + 0.3f * p), c, style = Stroke(max(1.5f * density, s * 0.04f)))
    for (i in 0 until 5) {
        val a = i * 2 * PI / 5 + 0.4
        drawCircle(Color.White.a(0.8f * (1f - p)), s * 0.04f * (1f - p * 0.5f), Offset(c.x + cos(a).toFloat() * s * (0.15f + 0.4f * p), c.y + sin(a).toFloat() * s * (0.15f + 0.4f * p)))
    }
}

/** La tête de la trace : une grosse bulle claire, plus grande (avec un anneau décalé) quand le doigt la tient. */
internal fun DrawScope.drawHead(c: Offset, s: Float, theme: MazeTheme, holding: Boolean) {
    val r = s * (if (holding) 0.36f else 0.24f)
    drawCircle(Color.White.a(0.92f), r, c)
    drawCircle(theme.trailEdge, r, c, style = Stroke(3f * density))
    drawCircle(Color.White, r * 0.3f, Offset(c.x - r * 0.35f, c.y - r * 0.35f))
    if (holding) {
        drawCircle(Color.White.a(0.28f), s * 0.46f, Offset(c.x + s * 0.18f, c.y + s * 0.34f))
        drawCircle(Color.White.a(0.85f), s * 0.46f, Offset(c.x + s * 0.18f, c.y + s * 0.34f), style = Stroke(3f * density))
    }
}

/** Les pastilles de l'indice : de plus en plus petites, jaunes, bien distinctes du ruban bleu. */
internal fun DrawScope.drawHint(cells: List<Offset>, s: Float, alpha: Float) {
    for ((i, c) in cells.withIndex()) {
        val r = s * (0.17f - i * 0.012f)
        drawCircle(Color(0xFFFFC83D).a(0.25f * alpha), r * 1.9f, c)
        drawCircle(Color(0xFFFFC83D).a(alpha), r, c)
        drawCircle(Color.White.a(alpha), r, c, style = Stroke(max(1.5f * density, s * 0.04f)))
    }
}

// ---------------------------------------------------------------- le fond marin d'un écran de jeu

/** Le fond marin aux couleurs du monde : dégradé, rayons de lumière qui s'effacent en descendant, bulles qui montent doucement. */
internal fun DrawScope.drawSea(theme: MazeTheme, time: Float) {
    val w = size.width
    val h = size.height
    drawRect(Brush.verticalGradient(listOf(theme.frameTop, theme.frameBottom)))
    for ((x, rw) in listOf(40f to 90f, 170f to 60f, 280f to 110f)) {
        val k = w / 390f
        val p = Path().apply { moveTo(x * k, 0f); lineTo((x + rw) * k, 0f); lineTo((x + rw * 2.2f) * k, h); lineTo((x - rw * 0.8f) * k, h); close() }
        drawPath(p, Brush.verticalGradient(listOf(Color.White.a(0.14f), Color.White.a(0f)), 0f, h))
    }
    for (i in 0 until 22) {
        val x = (hash01(i, 1) * w).toFloat() + sin(time * 0.8f + i) * 6f * density
        val speed = (12f + 18f * hash01(i, 4).toFloat()) * density
        val y = (((hash01(i, 2) * h).toFloat() - time * speed) % h + h) % h
        val r = (2f + 7f * hash01(i, 3).toFloat()) * density
        drawCircle(Color.White.a(0.1f), r, Offset(x, y))
        drawCircle(Color.White.a(0.35f), r, Offset(x, y), style = Stroke(1.4f * density))
        drawCircle(Color.White.a(0.5f), r * 0.25f, Offset(x - r * 0.3f, y - r * 0.3f))
    }
}

/** L'angle (en degrés, 0 = vers le haut, 90 = vers la droite) de la direction de [from] vers [to]. */
internal fun angleBetween(from: Offset, to: Offset): Float = (atan2((to.x - from.x).toDouble(), -(to.y - from.y).toDouble()) * 180.0 / PI).toFloat()
