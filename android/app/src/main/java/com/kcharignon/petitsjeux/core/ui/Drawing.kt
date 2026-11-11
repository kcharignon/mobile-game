package com.kcharignon.petitsjeux.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Une étoile à 5 branches de rayon [r] centrée en [c]. Allumée : or avec une « marche » plus sombre dessous ; éteinte : grise. */
fun DrawScope.drawStar(c: Offset, r: Float, on: Boolean) {
    fun path(center: Offset): Path = Path().apply {
        for (i in 0 until 10) {
            val a = -PI / 2 + i * PI / 5
            val rr = if (i % 2 == 0) r else r * 0.48f
            val x = center.x + (rr * cos(a)).toFloat()
            val y = center.y + (rr * sin(a)).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    if (!on) {
        val p = path(c)
        drawPath(p, Color(0xFFD9DCE6))
        drawPath(p, Color(0xFFB8BDCC), style = Stroke(r * 0.14f, join = StrokeJoin.Round))
        return
    }
    val shadow = path(c.copy(y = c.y + r * 0.1f))
    drawPath(shadow, Pj.StarDark)
    drawPath(shadow, Pj.StarDark, style = Stroke(r * 0.2f, join = StrokeJoin.Round))
    val top = path(c)
    drawPath(top, Pj.Star)
    drawPath(top, Color(0xFFFFE066), style = Stroke(r * 0.12f, join = StrokeJoin.Round))
}

/** Une rangée d'étoiles ([filled] allumées sur [total]), celle du milieu un peu relevée avec [raiseMiddle]. */
@Composable
fun Stars(filled: Int, modifier: Modifier = Modifier, total: Int = 3, star: Dp = 16.dp, gap: Dp = 2.dp, raiseMiddle: Boolean = false) {
    Row(modifier, horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(gap), verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until total) {
            val lift = if (raiseMiddle && total == 3 && i == 1) star * 0.35f else 0.dp
            Canvas(Modifier.size(star + 2.dp, star + 2.dp + lift)) {
                drawStar(Offset(size.width / 2, size.height - size.width / 2 - 1.dp.toPx()), size.width / 2 - 1.dp.toPx(), on = i < filled)
            }
        }
    }
}

enum class IconKind { BACK, RESTART, BULB, GEAR, LOCK, PLUS, QUESTION, LEVELS }

/** Petites icônes dessinées par le code, sur une grille de 24 unités. [hole] = couleur du trou de l'engrenage. */
@Composable
fun PjIcon(kind: IconKind, modifier: Modifier = Modifier, size: Dp = 24.dp, tint: Color = Color.White, hole: Color = Pj.Blue) {
    Canvas(modifier.size(size)) {
        val u = this.size.minDimension / 24f
        fun o(x: Float, y: Float) = Offset(x * u, y * u)
        val stroke = Stroke(width = 3.4f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
        when (kind) {
            IconKind.BACK -> drawPath(Path().apply { moveTo(15 * u, 5 * u); lineTo(8 * u, 12 * u); lineTo(15 * u, 19 * u) }, tint, style = stroke)
            IconKind.RESTART -> {
                drawArc(tint, startAngle = -40f, sweepAngle = 290f, useCenter = false, topLeft = o(4f, 4f), size = Size(16 * u, 16 * u), style = Stroke(3.2f * u, cap = StrokeCap.Round))
                drawPath(Path().apply { moveTo(21.5f * u, 2.5f * u); lineTo(21.5f * u, 11 * u); lineTo(13 * u, 8.5f * u); close() }, tint)
            }
            IconKind.BULB -> {
                drawCircle(tint, 7f * u, o(12f, 9.5f))
                drawRoundRect(tint, o(8.5f, 15.5f), Size(7 * u, 5.5f * u), CornerRadius(2f * u))
            }
            IconKind.GEAR -> {
                for (i in 0 until 8) {
                    rotate(i * 45f, o(12f, 12f)) { drawRoundRect(tint, o(9.4f, 1.5f), Size(5.2f * u, 7f * u), CornerRadius(1.6f * u)) }
                }
                drawCircle(tint, 8f * u, o(12f, 12f))
                drawCircle(hole, 3.6f * u, o(12f, 12f))
            }
            IconKind.LOCK -> {
                drawPath(
                    Path().apply { moveTo(7 * u, 10 * u); lineTo(7 * u, 7 * u); arcTo(androidx.compose.ui.geometry.Rect(7 * u, 1.5f * u, 17 * u, 12.5f * u), 180f, 180f, false); lineTo(17 * u, 10 * u) },
                    tint, style = Stroke(3f * u, cap = StrokeCap.Round),
                )
                drawRoundRect(tint, o(4f, 10f), Size(16 * u, 12 * u), CornerRadius(3.5f * u))
            }
            IconKind.PLUS -> {
                drawRoundRect(tint, o(2f, 9.5f), Size(20 * u, 5 * u), CornerRadius(2.5f * u))
                drawRoundRect(tint, o(9.5f, 2f), Size(5 * u, 20 * u), CornerRadius(2.5f * u))
            }
            // la grille de tuiles : le choix du niveau
            IconKind.LEVELS -> for (col in 0..1) for (row in 0..1) {
                drawRoundRect(tint, o(3f + col * 10f, 3f + row * 10f), Size(8 * u, 8 * u), CornerRadius(2.4f * u))
            }
            IconKind.QUESTION -> {
                drawArc(tint, startAngle = 160f, sweepAngle = 230f, useCenter = false, topLeft = o(6.5f, 3f), size = Size(11 * u, 11 * u), style = Stroke(3.6f * u, cap = StrokeCap.Round))
                drawPath(Path().apply { moveTo(12 * u, 14 * u); lineTo(12 * u, 16 * u) }, tint, style = stroke)
                drawCircle(tint, 2f * u, o(12f, 20.5f))
            }
        }
    }
}

@Composable
fun Gap(w: Dp) = androidx.compose.foundation.layout.Spacer(Modifier.width(w))
