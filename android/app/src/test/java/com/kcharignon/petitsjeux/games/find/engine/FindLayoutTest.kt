package com.kcharignon.petitsjeux.games.find.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.math.abs
import kotlin.math.hypot

class FindLayoutTest {
    private val levels: List<FindLevel> by lazy { FindLevels.parse(File("src/main/res/raw/find_levels.json").readText()) }

    /** Des zones de jeu de téléphones (en pixels) : étroite, courante, haute, et une tablette. */
    private val areas = listOf(320f to 420f, 360f to 520f, 411f to 700f, 800f to 1100f, 360f to 330f)

    private fun assertDisjointAndInside(spots: List<FindSpot>, w: Float, h: Float, label: String) {
        for (s in spots) {
            assertTrue("$label ${s.id} sort à gauche", s.cx - s.reach >= -0.01f)
            assertTrue("$label ${s.id} sort à droite", s.cx + s.reach <= w + 0.01f)
            assertTrue("$label ${s.id} sort en haut", s.cy - s.reach >= -0.01f)
            assertTrue("$label ${s.id} sort en bas", s.cy + s.reach <= h + 0.01f)
        }
        for (i in spots.indices) for (j in i + 1 until spots.size) {
            val a = spots[i]
            val b = spots[j]
            val apartX = abs(a.cx - b.cx) >= a.reach + b.reach - 0.01f
            val apartY = abs(a.cy - b.cy) >= a.reach + b.reach - 0.01f
            assertTrue("$label ${a.id} et ${b.id} se touchent", apartX || apartY)
            assertTrue("$label zones de toucher qui se chevauchent", hypot(a.cx - b.cx, a.cy - b.cy) >= a.hitRadius + b.hitRadius - 0.01f)
        }
    }

    @Test
    fun noObjectEverTouchesAnotherOrLeavesTheAreaInAnyLevel() {
        for ((w, h) in areas) for (l in levels) {
            val spots = FindLayout.arrange(l.ids, w, h, l.seed)
            assertEquals(l.ids, spots.map { it.id })
            assertDisjointAndInside(spots, w, h, "niveau ${l.number} (${w.toInt()}x${h.toInt()})")
        }
    }

    @Test
    fun noObjectTouchesAnotherEvenWithTheMaximumSizeCap() {
        for ((w, h) in areas) for (l in levels) assertDisjointAndInside(FindLayout.arrange(l.ids, w, h, l.seed, maxSize = 90f), w, h, "plafonné ${l.number}")
    }

    @Test
    fun theSameSeedGivesTheSameLayoutAndAnotherSeedGivesAnother() {
        val l = levels[40]
        val a = FindLayout.arrange(l.ids, 360f, 520f, l.seed)
        assertEquals(a, FindLayout.arrange(l.ids, 360f, 520f, l.seed))
        assertNotEquals(a, FindLayout.arrange(l.ids, 360f, 520f, l.seed + 1))
    }

    @Test
    fun theModelIsNotAlwaysInTheSamePlace() {
        val firstCells = levels.map { l ->
            val spots = FindLayout.arrange(l.ids, 360f, 520f, l.seed)
            val t = spots.first { it.id == l.target }
            (t.cy / 520f * 3).toInt() * 10 + (t.cx / 360f * 3).toInt()
        }.toSet()
        assertTrue("le modèle doit être un peu partout : ${firstCells.size} zones", firstCells.size >= 6)
    }

    @Test
    fun objectsAreBigAndGetSmallerAsThereAreMoreOfThem() {
        val sizes = (5..14).map { n -> FindLayout.arrange((1..n).map { "o$it" }, 360f, 520f, 1).first().size }
        for (i in 1 until sizes.size) assertTrue("${i + 5} objets : ${sizes[i]} > ${sizes[i - 1]}", sizes[i] <= sizes[i - 1] + 0.01f)
        assertTrue("14 objets doivent rester assez gros pour un doigt (> 70 px sur 360 de large) : ${sizes.last()}", sizes.last() > 70f)
        assertTrue(sizes.first() > sizes.last())
    }

    @Test
    fun theGridFitsTheRightNumberOfCellsAndAvoidsNeedlesslyThinCells() {
        for (n in 1..14) for ((w, h) in areas) {
            val (cols, rows) = FindLayout.grid(n, w, h)
            assertTrue(cols * rows >= n)
            assertTrue("${cols}x$rows pour $n", cols * rows < n + minOf(cols, rows) + 1 || n <= 3)
        }
        assertEquals(2 to 3, FindLayout.grid(5, 360f, 520f))
        assertEquals(3 to 5, FindLayout.grid(14, 360f, 520f))
    }
}
