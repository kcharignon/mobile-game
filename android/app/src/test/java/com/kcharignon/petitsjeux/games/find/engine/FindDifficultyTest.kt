package com.kcharignon.petitsjeux.games.find.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FindDifficultyTest {
    @Test
    fun objectsAndTargetsGrowAsAskedByTheIssue() {
        assertEquals(listOf(6, 12, 20, 40, 84), FindDifficulty.entries.map { it.objects })
        assertEquals(listOf(1, 2, 3, 4, 5), FindDifficulty.entries.map { it.targets })
    }

    @Test
    fun theNewRulesAddUp() {
        // la rotation arrive à la 2e difficulté, les tailles variables à la 4e, et elles restent ensuite
        assertEquals(listOf(false, true, true, true, true), FindDifficulty.entries.map { it.rotates })
        assertEquals(listOf(false, false, false, true, true), FindDifficulty.entries.map { it.variesSizes })
    }

    @Test
    fun theDifficultiesAreChainedAndNamed() {
        assertNull(FindDifficulty.EASY.previous)
        assertEquals(FindDifficulty.EASY, FindDifficulty.MEDIUM.previous)
        assertEquals(FindDifficulty.MASTER, FindDifficulty.EXPERT.next)
        assertNull(FindDifficulty.MASTER.next)
        assertEquals(5, FindDifficulty.entries.map { it.title }.toSet().size)
        assertEquals(15, FindDifficulty.MAX_STARS)
        assertEquals(FindDifficulty.EASY, FindDifficulty.of(-3))
        assertEquals(FindDifficulty.MASTER, FindDifficulty.of(40))
        for ((i, d) in FindDifficulty.entries.withIndex()) assertEquals(i, d.index)
    }

    @Test
    fun youNeverLoseAndAlwaysGetAtLeastOneStar() {
        for (d in FindDifficulty.entries) {
            assertEquals(3, d.stars(0))
            assertEquals(1, d.stars(d.objects))
            // moins d'étoiles à mesure qu'on se trompe, jamais moins d'une
            val stars = (0..d.objects).map { d.stars(it) }
            assertEquals(stars.sortedDescending(), stars)
            assertTrue(stars.all { it in 1..3 })
        }
    }

    @Test
    fun theFirstDifficultyKeepsThePreviousRuleAndTheOthersForgiveMoreMistakesAsObjectsGrow() {
        assertEquals(listOf(3, 2, 1, 1), (0..3).map { FindDifficulty.EASY.stars(it) })
        val threeStars = FindDifficulty.entries.map { it.threeStarsMistakes }
        assertEquals(threeStars.sorted(), threeStars)
        for (d in FindDifficulty.entries) {
            assertTrue(d.threeStarsMistakes < d.twoStarsMistakes)
            // on peut toujours avoir 2 étoiles sans tout toucher au hasard
            assertTrue(d.twoStarsMistakes < d.objects - d.targets)
        }
    }

    @Test
    fun theHintComesLaterWhenThereAreMoreObjects() {
        val delays = FindDifficulty.entries.map { it.nudgeMs }
        assertEquals(delays.sorted(), delays)
        assertFalse(delays.first() < 5_000L)
    }
}
