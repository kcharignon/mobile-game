package com.kcharignon.petitsjeux.games.find.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class FindLayoutTest {
    /** Des zones de jeu de téléphones (en pixels) : étroite, courante, haute, et une tablette. */
    private val areas = listOf(320f to 420f, 360f to 520f, 411f to 700f, 800f to 1100f, 360f to 330f)

    private fun ids(n: Int) = (1..n).map { "o$it" }

    private fun assertDisjointAndInside(spots: List<FindSpot>, w: Float, h: Float, label: String) {
        for (s in spots) {
            assertTrue("$label ${s.id} sort à gauche", s.cx - s.hitRadius >= -0.01f)
            assertTrue("$label ${s.id} sort à droite", s.cx + s.hitRadius <= w + 0.01f)
            assertTrue("$label ${s.id} sort en haut", s.cy - s.hitRadius >= -0.01f)
            assertTrue("$label ${s.id} sort en bas", s.cy + s.hitRadius <= h + 0.01f)
            assertTrue("$label ${s.id} : un objet dépasse sa zone de toucher", s.size <= s.hitRadius / FindLayout.HIT_FACTOR + 0.01f)
        }
        for (i in spots.indices) for (j in i + 1 until spots.size) {
            val a = spots[i]
            val b = spots[j]
            val apartX = abs(a.cx - b.cx) >= a.hitRadius + b.hitRadius - 0.01f
            val apartY = abs(a.cy - b.cy) >= a.hitRadius + b.hitRadius - 0.01f
            assertTrue("$label ${a.id} et ${b.id} : zones de toucher qui se chevauchent", apartX || apartY)
        }
    }

    @Test
    fun noTouchSquareEverOverlapsAnotherOrLeavesTheAreaForEveryDifficultyAndEverySeed() {
        for ((w, h) in areas) for (d in FindDifficulty.entries) for (seed in 0 until 25) {
            val spots = FindLayout.arrange(ids(d.objects), w, h, seed, rotate = d.rotates, vary = d.variesSizes)
            assertEquals(ids(d.objects), spots.map { it.id })
            assertDisjointAndInside(spots, w, h, "${d.name} seed $seed (${w.toInt()}x${h.toInt()})")
        }
    }

    @Test
    fun noTouchSquareOverlapsAnotherEvenWithTheMaximumSizeCap() {
        for ((w, h) in areas) for (d in FindDifficulty.entries) {
            assertDisjointAndInside(FindLayout.arrange(ids(d.objects), w, h, 3, maxSize = 90f, rotate = d.rotates, vary = d.variesSizes), w, h, "plafonné ${d.name}")
        }
    }

    @Test
    fun theFirstDifficultyKeepsEveryObjectUprightAndTheSameSize() {
        val spots = FindLayout.arrange(ids(5), 360f, 520f, 11)
        assertTrue(spots.all { it.angle == 0f })
        assertEquals(1, spots.map { it.size }.toSet().size)
    }

    @Test
    fun rotationTurnsObjectsInAllDirections() {
        val angles = FindLayout.arrange(ids(80), 360f, 520f, 5, rotate = true).map { it.angle }
        assertTrue(angles.all { it in 0f..360f })
        assertTrue("des inclinaisons de tous les côtés", angles.count { it in 90f..270f } >= 25)
        assertTrue(angles.count { it < 90f || it > 270f } >= 25)
        assertTrue("pas que des angles droits : ${angles.toSet().size} angles différents", angles.toSet().size >= 70)
    }

    @Test
    fun sizesVaryBetweenTheMinimumScaleAndTheMaximumSizeWhenAsked() {
        val spots = FindLayout.arrange(ids(40), 360f, 520f, 8, vary = true)
        val max = FindLayout.maxObjectSize(40, 360f, 520f)
        for (s in spots) assertTrue("${s.size} hors de ${max * FindLayout.MIN_SCALE}..$max", s.size in (max * FindLayout.MIN_SCALE - 0.01f)..(max + 0.01f))
        assertTrue("des tailles vraiment différentes", spots.maxOf { it.size } - spots.minOf { it.size } > max * 0.2f)
        val fixed = FindLayout.arrange(ids(40), 360f, 520f, 8, vary = false)
        assertEquals(1, fixed.map { it.size }.toSet().size)
    }

    @Test
    fun theTouchZoneIsTheSameForEveryObjectEvenTheSmallOnes() {
        val spots = FindLayout.arrange(ids(40), 360f, 520f, 8, vary = true)
        assertEquals(1, spots.map { it.hitRadius }.toSet().size)
        assertEquals(FindLayout.maxObjectSize(40, 360f, 520f) * FindLayout.HIT_FACTOR, spots.first().hitRadius, 0.01f)
    }

    @Test
    fun theSameSeedGivesTheSameLayoutAndAnotherSeedGivesAnother() {
        val a = FindLayout.arrange(ids(20), 360f, 520f, 9, rotate = true)
        assertEquals(a, FindLayout.arrange(ids(20), 360f, 520f, 9, rotate = true))
        assertNotEquals(a, FindLayout.arrange(ids(20), 360f, 520f, 10, rotate = true))
    }

    @Test
    fun aTargetIsNotAlwaysInTheSamePlace() {
        val cells = (0 until 60).map { seed ->
            val t = FindLayout.arrange(ids(10), 360f, 520f, seed).first()
            (t.cy / 520f * 3).toInt() * 10 + (t.cx / 360f * 3).toInt()
        }.toSet()
        assertTrue("la première cible doit être un peu partout : ${cells.size} zones", cells.size >= 6)
    }

    @Test
    fun objectsAreBigAndGetSmallerAsThereAreMoreOfThem() {
        val sizes = listOf(5, 10, 20, 40, 80).map { FindLayout.maxObjectSize(it, 360f, 520f) }
        for (i in 1 until sizes.size) assertTrue("${sizes[i]} > ${sizes[i - 1]}", sizes[i] < sizes[i - 1])
        assertTrue("80 objets doivent rester touchables (zone de toucher d'au moins 40 px de large sur 360 de large) : ${sizes.last()}", sizes.last() * FindLayout.OVERHANG >= 40f)
        assertTrue(sizes.first() > sizes.last() * 2)
    }

    @Test
    fun theGridFitsTheRightNumberOfCellsAndAvoidsNeedlesslyThinCells() {
        for (n in listOf(1, 2, 3, 5, 10, 20, 40, 80)) for ((w, h) in areas) {
            val (cols, rows) = FindLayout.grid(n, w, h)
            assertTrue(cols * rows >= n)
            assertTrue("${cols}x$rows pour $n", cols * rows < n + minOf(cols, rows) + 1 || n <= 3)
        }
        assertEquals(2 to 3, FindLayout.grid(5, 360f, 520f))
    }
}
