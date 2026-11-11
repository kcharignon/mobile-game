package com.kcharignon.petitsjeux.games.find.ui

import androidx.compose.ui.graphics.Color
import com.kcharignon.petitsjeux.core.ui.Pj
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/** Les dix décors doivent faire ressortir **tous** les objets : ni trop clairs (les objets blancs s'y perdraient) ni trop sombres (le contour encre s'y perdrait). */
class FindThemeTest {
    private fun luminance(c: Color): Double {
        fun channel(v: Float): Double = v.toDouble().let { if (it <= 0.03928) it / 12.92 else ((it + 0.055) / 1.055).pow(2.4) }
        return 0.2126 * channel(c.red) + 0.7152 * channel(c.green) + 0.0722 * channel(c.blue)
    }

    private fun ratio(a: Color, b: Color): Double = (max(luminance(a), luminance(b)) + 0.05) / (min(luminance(a), luminance(b)) + 0.05)

    @Test
    fun tenDistinctBackdrops() {
        assertEquals(10, FindTheme.all.size)
        assertEquals(10, FindTheme.all.map { it.top }.toSet().size)
        assertEquals(FindTheme.all.last(), FindTheme.of(99))
        assertEquals(FindTheme.all.first(), FindTheme.of(-1))
    }

    @Test
    fun whiteObjectsStandOutFromEveryBackdrop() {
        for ((w, t) in FindTheme.all.withIndex()) for (c in listOf(t.top, t.bottom)) {
            assertTrue("décor ${w + 1} : fond trop clair pour un objet blanc (${ratio(c, Color.White)})", ratio(c, Color.White) >= 1.4)
        }
    }

    @Test
    fun theInkOutlineOfEveryObjectStandsOutFromEveryBackdrop() {
        for ((w, t) in FindTheme.all.withIndex()) for (c in listOf(t.top, t.bottom)) {
            assertTrue("décor ${w + 1} : fond trop sombre pour le contour encre (${ratio(c, Pj.Ink)})", ratio(c, Pj.Ink) >= 3.0)
        }
    }

    @Test
    fun theMotifColoursAreDarkerThanTheBackdropSoTheMenuDotsAreVisible() {
        for ((w, t) in FindTheme.all.withIndex()) assertTrue("décor ${w + 1}", luminance(t.accent) < luminance(t.bottom))
    }
}
