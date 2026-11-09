package com.kcharignon.petitsjeux.core.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.composed
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.LocalFeedback

/** Le fond jaune soleil de toute l'appli, avec de grands cercles clairs ; le contenu reste dans les zones sûres (encoche, barres système). */
@Composable
fun Backdrop(
    modifier: Modifier = Modifier,
    top: Color = Pj.SunLight,
    mid: Color = Pj.Sun,
    bottom: Color = Pj.SunDark,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(0f to top, 0.35f to mid, 1f to bottom))
            .drawBehind {
                val white = Color.White.copy(alpha = 0.1f)
                drawCircle(white, 70.dp.toPx(), Offset(40.dp.toPx(), size.height * 0.14f))
                drawCircle(white, 46.dp.toPx(), Offset(size.width - 40.dp.toPx(), size.height * 0.11f))
                drawCircle(white, 90.dp.toPx(), Offset(size.width - 70.dp.toPx(), size.height * 0.36f))
                drawCircle(white, 60.dp.toPx(), Offset(30.dp.toPx(), size.height * 0.62f))
                drawCircle(white, 80.dp.toPx(), Offset(size.width - 30.dp.toPx(), size.height * 0.76f))
                drawCircle(white, 50.dp.toPx(), Offset(80.dp.toPx(), size.height * 0.94f))
            },
    ) {
        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), content = content)
        // les fenêtres (voile sombre) couvrent tout l'écran, barres système comprises
        overlay()
    }
}

/** Un panneau crème à « marche » (ombre pleine en dessous). */
@Composable
fun Panel(
    modifier: Modifier = Modifier,
    corner: Dp = 30.dp,
    fill: Color = Pj.Panel,
    edge: Color = Pj.PanelEdge,
    ledge: Dp = 6.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier
            .padding(bottom = ledge)
            .drawBehind {
                drawRoundRect(edge, Offset(0f, ledge.toPx()), size, CornerRadius(corner.toPx()))
            }
            .background(fill, RoundedCornerShape(corner))
            .padding(contentPadding),
        content = content,
    )
}

/** Un `onClick` qui joue d'abord le petit « pop » des boutons. */
@Composable
fun withTapSound(onClick: () -> Unit): () -> Unit {
    val feedback = LocalFeedback.current
    return { feedback.tap(); onClick() }
}

/** Gros bouton épais à reflet ; il s'enfonce quand on appuie. */
@Composable
fun ChunkyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Pj.Green,
    dark: Color = Pj.GreenDark,
    height: Dp = 60.dp,
    fontSize: TextUnit = 24.sp,
    enabled: Boolean = true,
    leading: (@Composable () -> Unit)? = null,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val click = withTapSound(onClick)
    val ledge = (height * 0.09f).coerceAtLeast(5.dp)
    val face = if (enabled) color else Pj.Gray
    val edge = if (enabled) dark else Pj.GrayDark
    Box(
        modifier
            .height(height + ledge)
            .clickable(interactionSource = source, indication = null, enabled = enabled, onClick = click),
    ) {
        Box(Modifier.fillMaxWidth().height(height).offset(y = ledge).background(edge, CircleShape))
        Box(
            Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = if (pressed) ledge else 0.dp)
                .background(face, CircleShape)
                .drawBehind { gloss(size) },
            contentAlignment = Alignment.Center,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                leading?.invoke()
                PjText(text, size = fontSize, weight = FontWeight.Bold, color = if (enabled) Color.White else Pj.GrayText)
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.gloss(size: Size) {
    drawRoundRect(
        Color.White.copy(alpha = 0.28f),
        topLeft = Offset(size.height * 0.25f, size.height * 0.1f),
        size = Size(size.width - size.height * 0.5f, size.height * 0.22f),
        cornerRadius = CornerRadius(size.height * 0.11f),
    )
}

/** Bouton rond (retour, recommencer, réglages) portant une icône. */
@Composable
fun RoundIconButton(
    kind: IconKind,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Pj.Blue,
    dark: Color = Pj.BlueDark,
    diameter: Dp = 48.dp,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val click = withTapSound(onClick)
    val ledge = 4.dp
    Box(modifier.size(diameter, diameter + ledge).clickable(interactionSource = source, indication = null, onClick = click)) {
        Box(Modifier.size(diameter).offset(y = ledge).background(dark, CircleShape))
        Box(
            Modifier
                .size(diameter)
                .offset(y = if (pressed) ledge else 0.dp)
                .background(color, CircleShape)
                .drawBehind {
                    drawOval(Color.White.copy(alpha = 0.3f), Offset(size.width * 0.3f, size.height * 0.12f), Size(size.width * 0.4f, size.height * 0.2f))
                },
            contentAlignment = Alignment.Center,
        ) {
            PjIcon(kind, size = diameter * 0.5f, hole = color)
        }
    }
}

/** Pastille blanche « ★ 74 » ou « ★ Niv. 27 ». */
@Composable
fun StarPill(label: String, modifier: Modifier = Modifier, textColor: Color = Pj.StarDark) {
    Row(
        modifier.background(Color.White.copy(alpha = 0.92f), CircleShape).padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Canvas(Modifier.size(24.dp)) { drawStar(Offset(size.width / 2, size.height / 2), 11.dp.toPx(), on = true) }
        PjText(label, size = 20.sp, weight = FontWeight.Bold, color = textColor)
    }
}

/** Un interrupteur épais (sons, vibrations). */
@Composable
fun PjSwitch(checked: Boolean, onChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val track = if (checked) Pj.Green else Pj.GrayDark
    Box(
        modifier
            .size(64.dp, 36.dp)
            .background(track, CircleShape)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onChange(!checked) },
    ) {
        Box(
            Modifier
                .padding(4.dp)
                .size(28.dp)
                .offset(x = if (checked) 28.dp else 0.dp)
                .background(Color.White, CircleShape),
        )
    }
}

/** Absorbe les touches : à mettre sur une fenêtre pour qu'un toucher dans la fenêtre ne ferme pas le voile qui est derrière. */
fun Modifier.consumeTaps(): Modifier = composed {
    clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = {})
}

/**
 * Une fenêtre posée sur l'écran (réglages, nouveau joueur, victoire) : un voile sombre puis [content], dans les zones sûres (et au-dessus du clavier).
 * Faite dans l'écran, sans fenêtre système, pour rester simple (et photographiable en test). Retour ou toucher du voile = [onDismiss] (s'il y en a un).
 */
@Composable
fun Modal(onDismiss: (() -> Unit)?, modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    if (onDismiss != null) BackHandler(onBack = onDismiss)
    Box(
        modifier
            .fillMaxSize()
            .background(Color(0xFF1B1D2E).copy(alpha = 0.5f))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = { onDismiss?.invoke() }),
    ) {
        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), contentAlignment = Alignment.Center, content = content)
    }
}
