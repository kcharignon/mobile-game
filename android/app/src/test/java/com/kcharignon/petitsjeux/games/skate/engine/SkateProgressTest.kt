package com.kcharignon.petitsjeux.games.skate.engine

import com.kcharignon.petitsjeux.games.skate.SkateProgress
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SkateProgressTest {
    private val empty = SkateProgress()

    @Test
    fun aNewPlayerHasNothingAndOnlyTheFirstDifficultyIsOpen() {
        assertEquals(0, empty.totalStars)
        assertEquals(0, empty.topScore)
        assertEquals(SkateDifficulty.EASY, empty.launchDifficulty)
        assertTrue(empty.isUnlocked(SkateDifficulty.EASY))
        for (d in SkateDifficulty.entries.drop(1)) assertFalse(empty.isUnlocked(d))
    }

    @Test
    fun twoStarsOpenTheNextDifficulty() {
        val one = empty.withResult(SkateDifficulty.EASY, 1, 300, 5, 0)
        assertFalse(one.isUnlocked(SkateDifficulty.MEDIUM))
        val two = one.withResult(SkateDifficulty.EASY, 2, 280, 1, 1)
        assertTrue(two.isUnlocked(SkateDifficulty.MEDIUM))
        assertFalse(two.isUnlocked(SkateDifficulty.HARD))
    }

    @Test
    fun bestStarsAndScoreOnlyGoUp() {
        val p = empty.withResult(SkateDifficulty.EASY, 3, 900, 0, 2).withResult(SkateDifficulty.EASY, 1, 100, 6, 0)
        assertEquals(3, p.stars(SkateDifficulty.EASY))
        assertEquals(900, p.bestScore(SkateDifficulty.EASY))
        assertEquals(2, p.played)
        assertEquals(6, p.hits)
        assertEquals(2, p.jumps)
    }

    @Test
    fun launchesOnTheHighestDifficultyAlreadyPlayed() {
        val p = empty.withResult(SkateDifficulty.EASY, 3, 900, 0, 0).withResult(SkateDifficulty.MEDIUM, 1, 200, 4, 0)
        assertEquals(SkateDifficulty.MEDIUM, p.launchDifficulty)
        assertEquals(4, p.totalStars)
        assertEquals(900, p.topScore)
    }

    @Test
    fun savesAndReadsBack() {
        val p = empty.withResult(SkateDifficulty.EASY, 3, 900, 0, 2).withResult(SkateDifficulty.HARD, 2, 700, 1, 1)
        assertEquals(p, SkateProgress.fromJson(JSONObject(p.toJson().toString())))
    }

    @Test
    fun readingIsTolerant() {
        assertEquals(empty, SkateProgress.fromJson(null))
        assertEquals(empty, SkateProgress.fromJson(JSONObject()))
        val broken = JSONObject("""{"stars":{"0":9,"1":-2,"x":3,"7":2,"2":2},"best":{"0":-1,"1":50,"9":10},"hits":-4,"jumps":"a","played":3}""")
        val p = SkateProgress.fromJson(broken)
        assertEquals(mapOf(2 to 2), p.stars)
        assertEquals(mapOf(1 to 50), p.best)
        assertEquals(0, p.hits)
        assertEquals(0, p.jumps)
        assertEquals(3, p.played)
    }
}
