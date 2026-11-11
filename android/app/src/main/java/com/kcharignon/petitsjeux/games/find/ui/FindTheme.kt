package com.kcharignon.petitsjeux.games.find.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/** Le motif discret d'un décor (des formes claires et transparentes : jamais un des objets). */
enum class FindMotif { STARS, DOTS, WAVES, CHECKS, STRIPES, BUBBLES, CLOUDS }

/**
 * Un décor : un dégradé de couleurs **moyennes** (ni très claires ni très sombres : les objets blancs comme les objets à contour encre
 * y restent lisibles, voir `FindThemeTest`) et un motif léger. Dix décors : chaque manche en tire un au hasard.
 */
class FindTheme(val top: Color, val bottom: Color, val motif: FindMotif, val accent: Color) {
    companion object {
        val all = listOf(
            FindTheme(Color(0xFFC7B8FF), Color(0xFFA992F5), FindMotif.STARS, Color(0xFF7C5CDB)), // lilas
            FindTheme(Color(0xFFB0E39B), Color(0xFF8FD27A), FindMotif.DOTS, Color(0xFF4E9E3D)), // vert prairie
            FindTheme(Color(0xFF86DCEB), Color(0xFF5CC3DC), FindMotif.WAVES, Color(0xFF2A93B0)), // turquoise
            FindTheme(Color(0xFFFFC2A3), Color(0xFFFFA98A), FindMotif.CHECKS, Color(0xFFE0714A)), // pêche
            FindTheme(Color(0xFFF3D08A), Color(0xFFE8B860), FindMotif.STRIPES, Color(0xFFC08A2C)), // blé
            FindTheme(Color(0xFF8FD1B0), Color(0xFF62B993), FindMotif.BUBBLES, Color(0xFF2F8D68)), // vert menthe
            FindTheme(Color(0xFF9DB6E8), Color(0xFF7C98D8), FindMotif.CHECKS, Color(0xFF4D6FC0)), // bleu ardoise
            FindTheme(Color(0xFFA6D8FF), Color(0xFF7FC0F7), FindMotif.CLOUDS, Color(0xFF4A98DD)), // bleu ciel
            FindTheme(Color(0xFFD9B99A), Color(0xFFC29C78), FindMotif.STRIPES, Color(0xFF93683F)), // bois clair
            FindTheme(Color(0xFFFFB3D1), Color(0xFFF58FBA), FindMotif.STARS, Color(0xFFD65A92)), // rose bonbon
        )

        fun of(index: Int): FindTheme = all[index.coerceIn(0, all.lastIndex)]
    }
}

private fun DrawScope.star(c: Offset, r: Float, color: Color) {
    val p = Path()
    for (i in 0 until 10) {
        val a = -PI / 2 + i * PI / 5
        val rad = if (i % 2 == 0) r else r * 0.45f
        val x = c.x + (cos(a) * rad).toFloat()
        val y = c.y + (sin(a) * rad).toFloat()
        if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
    }
    p.close()
    drawPath(p, color)
}

/** Le décor d'une manche : dégradé et motif léger, sur toute la zone de dessin. */
fun DrawScope.drawFindBackdrop(theme: FindTheme) {
    drawRect(Brush.verticalGradient(listOf(theme.top, theme.bottom)))
    val soft = Color.White.copy(alpha = 0.22f)
    val faint = Color.White.copy(alpha = 0.13f)
    val d = density
    val w = size.width
    val h = size.height
    when (theme.motif) {
        FindMotif.STARS -> {
            val r = Random(11)
            repeat(16) { star(Offset(r.nextFloat() * w, r.nextFloat() * h), (6 + r.nextFloat() * 8) * d, soft) }
        }
        FindMotif.DOTS -> {
            val step = 46f * d
            var row = 0
            var y = step / 2
            while (y < h + step) {
                var x = if (row % 2 == 0) step / 2 else step
                while (x < w + step) {
                    drawCircle(soft, 6f * d, Offset(x, y))
                    x += step
                }
                y += step
                row++
            }
        }
        FindMotif.WAVES -> {
            for (band in 0 until 5) {
                val y0 = h * (0.12f + band * 0.2f)
                val p = Path().apply {
                    moveTo(0f, y0)
                    var x = 0f
                    while (x <= w) {
                        lineTo(x, y0 + sin(x / (34f * d) + band) * 7f * d)
                        x += 8f * d
                    }
                }
                drawPath(p, soft, style = Stroke(3f * d))
            }
        }
        FindMotif.CHECKS -> {
            val s = 54f * d
            var row = 0
            var y = 0f
            while (y < h) {
                var x = if (row % 2 == 0) 0f else s
                while (x < w) {
                    drawRect(faint, Offset(x, y), Size(s, s))
                    x += s * 2
                }
                y += s
                row++
            }
        }
        FindMotif.STRIPES -> {
            val step = 44f * d
            var x = -h
            while (x < w) {
                val p = Path().apply {
                    moveTo(x, h); lineTo(x + step * 0.5f, h); lineTo(x + step * 0.5f + h, 0f); lineTo(x + h, 0f); close()
                }
                drawPath(p, faint)
                x += step
            }
        }
        FindMotif.BUBBLES -> {
            val r = Random(23)
            repeat(14) { drawCircle(soft, (8 + r.nextFloat() * 22) * d, Offset(r.nextFloat() * w, r.nextFloat() * h), style = Stroke(2.5f * d)) }
        }
        FindMotif.CLOUDS -> {
            val r = Random(5)
            repeat(5) {
                val cx = r.nextFloat() * w
                val cy = h * (0.08f + 0.8f * r.nextFloat())
                val u = (14 + r.nextFloat() * 10) * d
                drawRoundRect(soft, Offset(cx - u * 2.2f, cy), Size(u * 4.4f, u * 1.3f), CornerRadius(u * 0.65f))
                drawCircle(soft, u * 1.0f, Offset(cx - u * 0.5f, cy))
                drawCircle(soft, u * 1.3f, Offset(cx + u * 0.6f, cy - u * 0.15f))
            }
        }
    }
}
