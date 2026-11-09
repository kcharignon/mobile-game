package com.kcharignon.petitsjeux.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
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
    Avatar.UNICORN -> Color(0xFFBDEBFF)
    Avatar.TIGER -> Color(0xFFB5EFC0)
    Avatar.OWL -> Color(0xFFFFD9A8)
    Avatar.PENGUIN -> Color(0xFFB9C4FF)
}

/** Le visage rond d'un animal, dessiné par le code (les huit premiers sont aussi dans les maquettes : `avatar()` de tools/design/ds.js). */
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
            .testTag("avatar-${avatar.id}")
            .semantics { this.selected = selected; contentDescription = avatar.id }
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
    var overlay: (() -> Unit)? = null
    var noseColor = Dark
    var plainEyes = true
    var plainNose = true
    var plainMouth = true
    when (avatar) {
        Avatar.CAT -> {
            noseColor = Color(0xFFFF7FA0)
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
            noseColor = Color(0xFFFF7FA0)
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
        Avatar.UNICORN -> {
            faceFill = Color(0xFFFFFAFD)
            val pink = Color(0xFFFF9ED2)
            val lilac = Color(0xFFC9A8FF)
            val sky = Color(0xFF8FD3FF)
            earsOutside = {
                // la crinière, de chaque côté de la tête
                for (side in listOf(-1f, 1f)) {
                    circle(pink, side * 36f, -10f, 12f)
                    circle(lilac, side * 38f, 8f, 11f)
                    circle(sky, side * 33f, 24f, 10f)
                }
                for (side in listOf(-1f, 1f)) {
                    tri(faceFill, p(side * 28f, -16f), p(side * 27f, -42f), p(side * 9f, -27f))
                    tri(pink, p(side * 25f, -20f), p(side * 25f, -35f), p(side * 14f, -27f))
                }
            }
            snout = { oval(Color(0xFFFFE3EE), 0f, 12f, 14f, 10f) }
            noseColor = Color(0xFFFF7FA0)
            overlay = {
                // la corne dorée, rayée, et la mèche qui retombe devant
                val horn = Color(0xFFFFD34E)
                tri(horn, p(-9f, -24f), p(0f, -60f), p(9f, -24f))
                for (y in listOf(-33f, -42f, -50f)) {
                    val half = 9f * (y + 60f) / 36f
                    drawLine(Color(0xFFE8A200), p(-half, y), p(half, y - 3f), strokeWidth = 1.8f * u, cap = StrokeCap.Round)
                }
                circle(pink, -10f, -25f, 7.5f); circle(lilac, 10f, -25f, 7.5f); circle(sky, 0f, -22f, 5f)
                circle(Color(0x66FF7FA0), -25f, 10f, 5.5f); circle(Color(0x66FF7FA0), 25f, 10f, 5.5f)
            }
        }
        Avatar.TIGER -> {
            faceFill = Color(0xFFFFA23A)
            earsOutside = {
                circle(faceFill, -28f, -30f, 11.5f); circle(faceFill, 28f, -30f, 11.5f)
                circle(Color(0xFFFFE6C8), -28f, -30f, 5.5f); circle(Color(0xFFFFE6C8), 28f, -30f, 5.5f)
            }
            snout = { oval(Color(0xFFFFF4E0), 0f, 13f, 16f, 11f) }
            noseColor = Color(0xFFE0607E)
            overlay = {
                // les rayures : front et joues
                val stripe = Color(0xFF3A2A25)
                val w = 3.4f * u
                drawLine(stripe, p(0f, -31f), p(0f, -18f), w, StrokeCap.Round)
                drawLine(stripe, p(-8f, -29f), p(-7f, -21f), w, StrokeCap.Round)
                drawLine(stripe, p(8f, -29f), p(7f, -21f), w, StrokeCap.Round)
                for (side in listOf(-1f, 1f)) {
                    drawLine(stripe, p(side * 37f, 0f), p(side * 27f, 4f), w, StrokeCap.Round)
                    drawLine(stripe, p(side * 36f, 12f), p(side * 26f, 12f), w, StrokeCap.Round)
                    drawLine(stripe, p(side * 33f, 23f), p(side * 25f, 20f), w, StrokeCap.Round)
                }
            }
        }
        Avatar.OWL -> {
            faceFill = Color(0xFF9C7A5B)
            earsOutside = {
                // les aigrettes du hibou
                tri(Color(0xFF7A5A42), p(-36f, -4f), p(-34f, -44f), p(-10f, -28f))
                tri(Color(0xFF7A5A42), p(36f, -4f), p(34f, -44f), p(10f, -28f))
            }
            plainEyes = false; plainNose = false; plainMouth = false
            overlay = {
                // les deux disques clairs autour des yeux, les gros yeux ronds, le bec
                circle(Color(0xFFFFF0D0), -14f, -3f, 17f); circle(Color(0xFFFFF0D0), 14f, -3f, 17f)
                circle(Color(0xFFFFB800), -14f, -3f, 11f); circle(Color(0xFFFFB800), 14f, -3f, 11f)
                circle(Dark, -14f, -3f, 6.5f); circle(Dark, 14f, -3f, 6.5f)
                circle(Color.White, -11.5f, -6f, 2.2f); circle(Color.White, 16.5f, -6f, 2.2f)
                tri(Color(0xFFFF8F1F), p(-6.5f, 8f), p(6.5f, 8f), p(0f, 23f))
                for (x in listOf(-17f, -6f, 6f, 17f)) circle(Color(0xFFC9A37E), x, 29f - kotlin.math.abs(x) * 0.12f, 2.6f)
            }
        }
        Avatar.PENGUIN -> {
            faceFill = Color(0xFF353B5C)
            earsOutside = {}
            snout = {
                // le masque blanc : deux joues et un menton
                oval(Color.White, -11f, 6f, 15f, 19f); oval(Color.White, 11f, 6f, 15f, 19f); oval(Color.White, 0f, 17f, 20f, 13f)
            }
            plainNose = false; plainMouth = false
            overlay = {
                oval(Color(0xFFFF9F1C), 0f, 10f, 8f, 5f)
                oval(Color(0xFFE67E00), 0f, 15f, 5.6f, 3.2f)
                circle(Color(0x66FF7FA0), -24f, 11f, 4.5f); circle(Color(0x66FF7FA0), 24f, 11f, 4.5f)
            }
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
    overlay?.invoke()
    if (avatar == Avatar.FROG) {
        circle(Color.White, -22f, -26f, 9f); circle(Color.White, 22f, -26f, 9f)
        circle(Dark, -22f, -26f, 5f); circle(Dark, 22f, -26f, 5f)
    } else if (plainEyes) {
        circle(Dark, -15f, -4f, 5.5f); circle(Dark, 15f, -4f, 5.5f)
        circle(Color.White, -13f, -6f, 1.8f); circle(Color.White, 17f, -6f, 1.8f)
    }
    if (plainNose) oval(noseColor, 0f, 9f, 6f, 4.4f)
    val mouthY = if (avatar == Avatar.FROG) 14f else 19f
    val mouthW = if (avatar == Avatar.FROG) 17f else 8f
    if (plainMouth) drawPath(
        Path().apply {
            moveTo(p(-mouthW, mouthY).x, p(-mouthW, mouthY).y)
            quadraticTo(c.x, p(0f, mouthY + 9f).y, p(mouthW, mouthY).x, p(mouthW, mouthY).y)
        },
        Dark,
        style = Stroke(2.8f * u, cap = StrokeCap.Round),
    )
}
