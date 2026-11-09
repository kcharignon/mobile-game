package com.kcharignon.petitsjeux.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.platform.LocalDensity
import com.kcharignon.petitsjeux.R

/** Les couleurs de « Petits Jeux » (docs/design : planche de style). */
object Pj {
    val Sun = Color(0xFFFFC83D)
    val SunLight = Color(0xFFFFE07A)
    val SunDark = Color(0xFFF2A91B)
    val Ink = Color(0xFF2B2D42)
    val InkSoft = Color(0xFF5B6079)
    val Panel = Color(0xFFFFF8E6)
    val PanelEdge = Color(0xFFE8D5A6)

    val Green = Color(0xFF4CD964)
    val GreenDark = Color(0xFF2FA845)
    val Blue = Color(0xFF3E8BFF)
    val BlueDark = Color(0xFF2B66D1)
    val Orange = Color(0xFFFFB020)
    val OrangeDark = Color(0xFFD68A00)
    val Red = Color(0xFFFF5A4A)
    val RedDark = Color(0xFFCF3A2B)
    val Gray = Color(0xFFD5D9E6)
    val GrayDark = Color(0xFFB9BECE)
    val GrayText = Color(0xFF7B8196)
    val Slate = Color(0xFF8E96AB)
    val SlateDark = Color(0xFF6B7388)
    val Star = Color(0xFFFFC107)
    val StarDark = Color(0xFFE59A00)
    val Glass = Color(0xFF33425F)

    /** Palette des voitures, dans l'ordre des indices de couleur du moteur : vert, orange, cyan, bleu, rose, jaune, violet, beige, rouge. */
    val CarColors = listOf(
        Color(0xFF4DD35A),
        Color(0xFFFF7A3D),
        Color(0xFF35C6F2),
        Color(0xFF4A7DF0),
        Color(0xFFF0409A),
        Color(0xFFFFD93B),
        Color(0xFFA55BE8),
        Color(0xFFEBCB9C),
        Color(0xFFF2453D),
    )
}

/** Fredoka (variable, licence OFL : third_party/fredoka), arrondie et lisible. */
@OptIn(ExperimentalTextApi::class)
val PjFont = FontFamily(
    Font(R.font.fredoka, FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.fredoka, FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.fredoka, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.fredoka, FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(700))),
)

/**
 * Le texte de l'appli. Avec [outline], un liseré arrondi entoure les lettres (les gros titres blancs cernés d'encre).
 * La taille ne suit la taille de police du téléphone que jusqu'à 120 % : les écrans d'enfant ne doivent pas déborder.
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun PjText(
    text: String,
    modifier: Modifier = Modifier,
    size: TextUnit = 18.sp,
    weight: FontWeight = FontWeight.SemiBold,
    color: Color = Pj.Ink,
    align: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    outline: Color? = null,
    outlineWidth: Dp = 6.dp,
) {
    val style = TextStyle(fontFamily = PjFont, fontSize = size, fontWeight = weight, color = color, textAlign = align, lineHeight = size * 1.15f)
    if (outline == null) {
        BasicText(text, modifier, style = style, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
        return
    }
    val strokeWidthPx = with(LocalDensity.current) { outlineWidth.toPx() }
    Box(modifier) {
        BasicText(
            text,
            style = style.copy(color = outline, drawStyle = Stroke(width = strokeWidthPx, join = StrokeJoin.Round)),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
        BasicText(text, style = style, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
    }
}
