package com.kcharignon.petitsjeux.games.mushrooms

import com.kcharignon.petitsjeux.games.mushrooms.engine.Difficulty
import com.kcharignon.petitsjeux.games.mushrooms.engine.ForestLayout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.hypot

class ForestLayoutTest {
    private val screens = listOf(360f to 520f, 390f to 600f, 320f to 420f, 412f to 700f)

    @Test
    fun everyMushroomIsInsideTheForestAndNeverOnAnother() {
        for (d in Difficulty.entries) for ((w, h) in screens) for (seed in 1L..5L) {
            val spots = ForestLayout.generate(d.mushrooms, w, h, seed)
            assertEquals(d.mushrooms, spots.size)
            assertEquals((0 until d.mushrooms).toList(), spots.map { it.id })
            for (s in spots) {
                assertTrue("${d.title} $w x $h : dehors ($s)", s.x - s.radius >= -0.01f && s.x + s.radius <= w + 0.01f && s.y - s.radius >= -0.01f && s.y + s.radius <= h + 0.01f)
                assertTrue("rayon trop petit : ${s.radius}", s.radius >= ForestLayout.MIN_RADIUS - 0.01f)
            }
            for (a in spots) for (b in spots) {
                if (a.id >= b.id) continue
                assertTrue("${d.title} $w x $h graine $seed : ${a.id} et ${b.id} se touchent", hypot(a.x - b.x, a.y - b.y) >= a.radius + b.radius + ForestLayout.GAP - 0.01f)
            }
        }
    }

    @Test
    fun mushroomsHaveDifferentSizesAndBigEnoughToTouch() {
        for (d in Difficulty.entries) {
            val spots = ForestLayout.generate(d.mushrooms, 360f, 520f, 4L)
            val radii = spots.map { it.radius }
            assertTrue("${d.title} : des tailles variées (${radii.min()} à ${radii.max()})", radii.max() / radii.min() >= 1.3f)
            assertTrue("chapeau de ${radii.min() * 2} dp : trop petit pour un doigt d'enfant", radii.min() * 2 >= 52f)
        }
    }

    @Test
    fun theForestOnlyDependsOnItsArguments() {
        assertEquals(ForestLayout.generate(16, 360f, 520f, 9L), ForestLayout.generate(16, 360f, 520f, 9L))
        assertTrue(ForestLayout.generate(16, 360f, 520f, 9L) != ForestLayout.generate(16, 360f, 520f, 10L))
    }

    @Test
    fun aTinyForestStillGetsAllItsMushrooms() {
        val spots = ForestLayout.generate(20, 200f, 260f, 3L)
        assertEquals(20, spots.size)
        assertTrue(spots.all { it.x in 0f..200f && it.y in 0f..260f })
    }
}
