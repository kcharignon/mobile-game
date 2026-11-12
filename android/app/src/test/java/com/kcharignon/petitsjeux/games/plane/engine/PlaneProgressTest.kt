package com.kcharignon.petitsjeux.games.plane.engine

import com.kcharignon.petitsjeux.games.plane.PlaneProgress
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaneProgressTest {
    private val empty = PlaneProgress()

    @Test
    fun aNewPlayerHasNothingAndOnlyTheFirstDifficultyIsOpen() {
        assertEquals(0, empty.totalStars)
        assertEquals(0, empty.topScore)
        assertEquals(PlaneDifficulty.EASY, empty.launchDifficulty)
        assertTrue(empty.isUnlocked(PlaneDifficulty.EASY))
        for (d in PlaneDifficulty.entries.drop(1)) assertFalse(empty.isUnlocked(d))
    }

    @Test
    fun twoStarsOpenTheNextDifficulty() {
        val one = empty.withResult(PlaneDifficulty.EASY, 1, 300, 5, 0)
        assertFalse(one.isUnlocked(PlaneDifficulty.MEDIUM))
        val two = one.withResult(PlaneDifficulty.EASY, 2, 280, 1, 1)
        assertTrue(two.isUnlocked(PlaneDifficulty.MEDIUM))
        assertFalse(two.isUnlocked(PlaneDifficulty.HARD))
    }

    @Test
    fun bestStarsAndScoreOnlyGoUp() {
        val p = empty.withResult(PlaneDifficulty.EASY, 3, 900, 0, 2).withResult(PlaneDifficulty.EASY, 1, 100, 6, 0)
        assertEquals(3, p.stars(PlaneDifficulty.EASY))
        assertEquals(900, p.bestScore(PlaneDifficulty.EASY))
        assertEquals(2, p.played)
        assertEquals(6, p.hits)
        assertEquals(2, p.rolls)
    }

    @Test
    fun launchesOnTheHighestDifficultyAlreadyPlayed() {
        val p = empty.withResult(PlaneDifficulty.EASY, 3, 900, 0, 0).withResult(PlaneDifficulty.MEDIUM, 1, 200, 4, 0)
        assertEquals(PlaneDifficulty.MEDIUM, p.launchDifficulty)
        assertEquals(4, p.totalStars)
        assertEquals(900, p.topScore)
    }

    @Test
    fun savesAndReadsBack() {
        val p = empty.withResult(PlaneDifficulty.EASY, 3, 900, 0, 2).withResult(PlaneDifficulty.HARD, 2, 700, 1, 1)
        assertEquals(p, PlaneProgress.fromJson(JSONObject(p.toJson().toString())))
    }

    @Test
    fun readingIsTolerant() {
        assertEquals(empty, PlaneProgress.fromJson(null))
        assertEquals(empty, PlaneProgress.fromJson(JSONObject()))
        val broken = JSONObject("""{"stars":{"0":9,"1":-2,"x":3,"7":2,"2":2},"best":{"0":-1,"1":50,"9":10},"hits":-4,"rolls":"a","played":3}""")
        val p = PlaneProgress.fromJson(broken)
        assertEquals(mapOf(2 to 2), p.stars)
        assertEquals(mapOf(1 to 50), p.best)
        assertEquals(0, p.hits)
        assertEquals(0, p.rolls)
        assertEquals(3, p.played)
    }

    @Test
    fun theFirstVersionOfTheGameStoredItsRampsUnderTheKeyJumps() {
        // le skateur de la version 0.15.0 : mêmes étoiles, mêmes scores, et les « rampes » sous la clé jumps
        val old = PlaneProgress.fromJson(JSONObject("""{"stars":{"0":3,"1":2},"best":{"0":640,"1":520},"hits":4,"jumps":7,"played":5}"""))
        assertEquals(5, old.totalStars)
        assertEquals(640, old.topScore)
        assertEquals(7, old.rolls)
        assertEquals(PlaneDifficulty.MEDIUM, old.launchDifficulty)
        assertTrue(old.isUnlocked(PlaneDifficulty.HARD))
        // et une fois sauvegardé, c'est la clé rolls
        assertEquals(7, old.toJson().getInt("rolls"))
        assertFalse(old.toJson().has("jumps"))
    }
}
