package com.kcharignon.petitsjeux.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kcharignon.petitsjeux.core.save.Avatar

private val Dark = Color(0xFF2B2D42)

private fun background(avatar: Avatar): Color = when (avatar) {
    Avatar.CAT -> Color(0xFF9ED8FF)
    Avatar.DOG -> Color(0xFFFFD1A1)
    Avatar.BEAR -> Color(0xFFC8F0A2)
    Avatar.PANDA -> Color(0xFFFFB8D9)
    Avatar.RABBIT -> Color(0xFFD8C4FF)
    Avatar.FROG -> Color(0xFFFFF0A0)
    Avatar.FOX -> Color(0xFFB8F0E4)
    Avatar.LION -> Color(0xFFFFC4B8)
}

/** Le visage rond d'un animal, dessiné par le code (docs/design : `avatar()` de tools/design/ds.js). */
@Composable
fun AvatarFace(avatar: Avatar, size: Dp, modifier: Modifier = Modifier) {
    Canvas(modifier.size(size)) { drawAvatar(avatar, center, this.size.minDimension / 2f) }
}

/** Un animal à choisir ; cerclé de bleu quand il est choisi. */
@Composable
fun AvatarChoice(avatar: Avatar, selected: Boolean, onClick: () -> Unit, size: Dp = 64.dp, modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Box(
        modifier
            .size(size + 12.dp)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick),
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        if (selected) {
            Canvas(Modifier.size(size + 12.dp)) {
                drawCircle(Pj.Blue, radius = this.size.minDimension / 2f - 2.5.dp.toPx(), style = Stroke(5.dp.toPx()))
            }
        }
        AvatarFace(avatar, size)
    }
}

fun DrawScope.drawAvatar(avatar: Avatar, c: Offset, r: Float) {
    val u = r / 50f
    fun p(x: Float, y: Float) = Offset(c.x + x * u, c.y + y * u)
    fun circle(color: Color, x: Float, y: Float, rad: Float) = drawCircle(color, rad * u, p(x, y))
    fun oval(color: Color, x: Float, y: Float, rx: Float, ry: Float) =
        drawOval(color, Offset(c.x + (x - rx) * u, c.y + (y - ry) * u), Size(2 * rx * u, 2 * ry * u))
    fun tri(color: Color, a: Offset, b: Offset, d: Offset) =
        drawPath(Path().apply { moveTo(a.x, a.y); lineTo(b.x, b.y); lineTo(d.x, d.y); close() }, color)
    fun tilted(deg: Float, x: Float, y: Float, block: () -> Unit) = rotate(deg, p(x, y)) { block() }

    val bg = background(avatar)
    drawCircle(lerp(bg, Color.Black, 0.18f), r, c.copy(y = c.y + 3.dp.toPx()))
    drawCircle(bg, r, c)

    var faceFill = Color(0xFFFFB066)
    val earsOutside: () -> Unit
    var snout: (() -> Unit)? = null
    when (avatar) {
        Avatar.CAT -> {
            earsOutside = {
                tri(faceFill, p(-34f, -10f), p(-30f, -42f), p(-6f, -26f))
                tri(faceFill, p(34f, -10f), p(30f, -42f), p(6f, -26f))
                tri(Color(0xFFFF8FA8), p(-29f, -18f), p(-27f, -34f), p(-15f, -26f))
                tri(Color(0xFFFF8FA8), p(29f, -18f), p(27f, -34f), p(15f, -26f))
            }
            snout = { oval(Color(0xFFFFE3C4), 0f, 12f, 14f, 10f) }
        }
        Avatar.DOG -> {
            faceFill = Color(0xFFE0B07A)
            earsOutside = {
                tilted(12f, -36f, -6f) { oval(Color(0xFF8A5A34), -36f, -6f, 12f, 22f) }
                tilted(-12f, 36f, -6f) { oval(Color(0xFF8A5A34), 36f, -6f, 12f, 22f) }
            }
            snout = { oval(Color(0xFFFBE6C8), 0f, 12f, 17f, 13f) }
        }
        Avatar.BEAR -> {
            faceFill = Color(0xFFB5835A)
            earsOutside = {
                circle(faceFill, -28f, -30f, 12f); circle(faceFill, 28f, -30f, 12f)
                circle(Color(0xFFE5B98F), -28f, -30f, 6f); circle(Color(0xFFE5B98F), 28f, -30f, 6f)
            }
            snout = { oval(Color(0xFFEBCBA4), 0f, 12f, 16f, 12f) }
        }
        Avatar.PANDA -> {
            faceFill = Color.White
            earsOutside = { circle(Dark, -29f, -30f, 12f); circle(Dark, 29f, -30f, 12f) }
        }
        Avatar.RABBIT -> {
            faceFill = Color.White
            earsOutside = {
                oval(Color.White, -17f, -38f, 10f, 26f); oval(Color.White, 17f, -38f, 10f, 26f)
                oval(Color(0xFFFFB3C7), -17f, -38f, 5f, 18f); oval(Color(0xFFFFB3C7), 17f, -38f, 5f, 18f)
            }
        }
        Avatar.FROG -> {
            faceFill = Color(0xFF6FD36B)
            earsOutside = { circle(faceFill, -22f, -26f, 15f); circle(faceFill, 22f, -26f, 15f) }
        }
        Avatar.FOX -> {
            faceFill = Color(0xFFFF8A3D)
            earsOutside = {
                tri(faceFill, p(-36f, -8f), p(-32f, -44f), p(-8f, -24f))
                tri(faceFill, p(36f, -8f), p(32f, -44f), p(8f, -24f))
                tri(Dark, p(-31f, -18f), p(-29f, -35f), p(-16f, -26f))
                tri(Dark, p(31f, -18f), p(29f, -35f), p(16f, -26f))
            }
            snout = {
                drawPath(
                    Path().apply {
                        moveTo(p(-34f, 6f).x, p(-34f, 6f).y)
                        quadraticTo(p(-14f, 2f).x, p(-14f, 2f).y, p(0f, 20f).x, p(0f, 20f).y)
                        quadraticTo(p(14f, 2f).x, p(14f, 2f).y, p(34f, 6f).x, p(34f, 6f).y)
                        quadraticTo(p(24f, 34f).x, p(24f, 34f).y, p(0f, 38f).x, p(0f, 38f).y)
                        quadraticTo(p(-24f, 34f).x, p(-24f, 34f).y, p(-34f, 6f).x, p(-34f, 6f).y)
                        close()
                    },
                    Color(0xFFFFF3E0),
                )
            }
        }
        Avatar.LION -> {
            faceFill = Color(0xFFFFD27A)
            earsOutside = {
                circle(Color(0xFFE8902B), 0f, 2f, 46f)
                circle(faceFill, -26f, -30f, 9f); circle(faceFill, 26f, -30f, 9f)
            }
            snout = { oval(Color(0xFFFFF0CC), 0f, 13f, 14f, 10f) }
        }
    }
    earsOutside()
    val faceR = if (avatar == Avatar.LION) 33f else 38f
    oval(faceFill, 0f, 2f, faceR, faceR * 0.9f)
    snout?.invoke()
    if (avatar == Avatar.PANDA) {
        tilted(20f, -15f, -4f) { oval(Dark, -15f, -4f, 9f, 11f) }
        tilted(-20f, 15f, -4f) { oval(Dark, 15f, -4f, 9f, 11f) }
    }
    if (avatar == Avatar.FROG) {
        circle(Color.White, -22f, -26f, 9f); circle(Color.White, 22f, -26f, 9f)
        circle(Dark, -22f, -26f, 5f); circle(Dark, 22f, -26f, 5f)
    } else {
        circle(Dark, -15f, -4f, 5.5f); circle(Dark, 15f, -4f, 5.5f)
        circle(Color.White, -13f, -6f, 1.8f); circle(Color.White, 17f, -6f, 1.8f)
        oval(if (avatar == Avatar.CAT || avatar == Avatar.RABBIT) Color(0xFFFF7FA0) else Dark, 0f, 9f, 6f, 4.4f)
    }
    val mouthY = if (avatar == Avatar.FROG) 14f else 19f
    val mouthW = if (avatar == Avatar.FROG) 17f else 8f
    drawPath(
        Path().apply {
            moveTo(p(-mouthW, mouthY).x, p(-mouthW, mouthY).y)
            quadraticTo(c.x, p(0f, mouthY + 9f).y, p(mouthW, mouthY).x, p(mouthW, mouthY).y)
        },
        Dark,
        style = Stroke(2.8f * u, cap = StrokeCap.Round),
    )
}
