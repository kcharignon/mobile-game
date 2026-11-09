package com.kcharignon.petitsjeux.games.mushrooms

import com.kcharignon.petitsjeux.games.mushrooms.engine.Difficulty
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MushroomProgressTest {
    @Test
    fun aNewPlayerHasOnlyTheFirstDifficulty() {
        val p = MushroomProgress()
        assertTrue(p.isUnlocked(Difficulty.EASY))
        assertFalse(p.isUnlocked(Difficulty.MEDIUM))
        assertFalse(p.isUnlocked(Difficulty.HARD))
        assertFalse(p.isUnlocked(Difficulty.EXPERT))
        assertEquals(0, p.totalStars)
        assertEquals(0, p.stars(Difficulty.EASY))
        assertFalse(p.hasPlayed(Difficulty.EASY))
    }

    @Test
    fun theNextDifficultyOpensAtTwoStarsOnThePreviousOne() {
        var p = MushroomProgress().withResult(Difficulty.EASY, Difficulty.EASY.twoStars - 1, 15, 4)
        assertEquals(1, p.stars(Difficulty.EASY)) // fini, une étoile : on ne perd jamais
        assertFalse(p.isUnlocked(Difficulty.MEDIUM))
        p = p.withResult(Difficulty.EASY, Difficulty.EASY.twoStars, 15, 6)
        assertEquals(2, p.stars(Difficulty.EASY))
        assertTrue(p.isUnlocked(Difficulty.MEDIUM))
        assertFalse(p.isUnlocked(Difficulty.HARD))
        p = p.withResult(Difficulty.MEDIUM, Difficulty.MEDIUM.threeStars, 20, 9)
        assertTrue(p.isUnlocked(Difficulty.HARD))
        assertEquals(5, p.totalStars)
    }

    @Test
    fun theBestScoreIsKeptAndTheCountersAdd() {
        var p = MushroomProgress().withResult(Difficulty.EASY, 900, 15, 5)
        p = p.withResult(Difficulty.EASY, 600, 14, 3)
        assertEquals(900, p.bestScore(Difficulty.EASY))
        assertEquals(29, p.picked)
        assertEquals(2, p.played)
        assertEquals(5, p.bestStreak)
        assertEquals(900, p.topScore)
        p = p.withResult(Difficulty.EASY, 1200, 15, 2)
        assertEquals(1200, p.bestScore(Difficulty.EASY))
    }

    @Test
    fun theSaveSurvivesAJsonRoundTrip() {
        val p = MushroomProgress().withResult(Difficulty.EASY, 1000, 15, 7).withResult(Difficulty.MEDIUM, 1100, 20, 8)
        assertEquals(p, MushroomProgress.fromJson(JSONObject(p.toJson().toString())))
    }

    @Test
    fun aDamagedSaveNeverCrashes() {
        assertEquals(MushroomProgress(), MushroomProgress.fromJson(null))
        assertEquals(MushroomProgress(), MushroomProgress.fromJson(JSONObject()))
        val weird = JSONObject("""{"best":{"x":5,"99":10,"0":-3,"1":800},"picked":-4,"played":"beaucoup","bestStreak":2.5}""")
        val p = MushroomProgress.fromJson(weird)
        assertEquals(mapOf(1 to 800), p.best)
        assertEquals(0, p.picked)
        assertEquals(0, p.played)
        assertEquals(2, p.bestStreak)
    }

    @Test
    fun theMiniGameTellsTheHubWhereThePlayerIs() {
        assertEquals("Nouveau !", MushroomMiniGame.progressLabel(null))
        val save = MushroomProgress().withResult(Difficulty.EASY, 840, 15, 3).toJson()
        assertEquals("Record 840", MushroomMiniGame.progressLabel(save))
        assertEquals(2, MushroomMiniGame.stars(save)) // 840 points : 2 étoiles sur « Petit champi »
        assertEquals(0, MushroomMiniGame.stars(null))
        assertEquals("mushrooms", MushroomMiniGame.id)
    }
}
