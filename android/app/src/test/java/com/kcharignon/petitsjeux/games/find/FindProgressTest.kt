package com.kcharignon.petitsjeux.games.find

import com.kcharignon.petitsjeux.games.find.engine.FindDifficulty
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FindProgressTest {
    @Test
    fun aNewPlayerStartsOnTheFirstDifficultyAndTheGameOpensOnIt() {
        val p = FindProgress()
        assertEquals(0, p.totalStars)
        assertEquals(FindDifficulty.EASY, p.launchDifficulty)
        assertFalse(p.hasPlayed(FindDifficulty.EASY))
        assertTrue(p.isUnlocked(FindDifficulty.EASY))
        assertFalse(p.isUnlocked(FindDifficulty.MEDIUM))
    }

    @Test
    fun aRoundKeepsTheBestMarkAndAddsUpTheCounters() {
        var p = FindProgress().withResult(FindDifficulty.EASY, earnedStars = 2, mistakesNow = 1, foundNow = 1)
        assertEquals(2, p.stars(FindDifficulty.EASY))
        assertTrue(p.hasPlayed(FindDifficulty.EASY))
        p = p.withResult(FindDifficulty.EASY, earnedStars = 1, mistakesNow = 3, foundNow = 1)
        assertEquals("on garde la meilleure note", 2, p.stars(FindDifficulty.EASY))
        p = p.withResult(FindDifficulty.EASY, earnedStars = 3, mistakesNow = 0, foundNow = 1)
        assertEquals(3, p.stars(FindDifficulty.EASY))
        assertEquals(4, p.mistakes)
        assertEquals(3, p.found)
        assertEquals(3, p.played)
    }

    @Test
    fun theNextDifficultyOpensWithTwoStarsOnThePreviousOne() {
        var p = FindProgress().withResult(FindDifficulty.EASY, 1, 2, 1)
        assertFalse(p.isUnlocked(FindDifficulty.MEDIUM))
        p = p.withResult(FindDifficulty.EASY, 2, 1, 1)
        assertTrue(p.isUnlocked(FindDifficulty.MEDIUM))
        assertFalse(p.isUnlocked(FindDifficulty.HARD))
    }

    @Test
    fun theGameOpensOnTheHighestDifficultyAlreadyPlayedNotTheHighestUnlocked() {
        var p = FindProgress().withResult(FindDifficulty.EASY, 3, 0, 1)
        assertEquals("débloquée mais jamais jouée : on reste sur la première", FindDifficulty.EASY, p.launchDifficulty)
        p = p.withResult(FindDifficulty.MEDIUM, 1, 5, 2)
        assertEquals(FindDifficulty.MEDIUM, p.launchDifficulty)
        p = p.withResult(FindDifficulty.EASY, 3, 0, 1)
        assertEquals("rejouer une difficulté facile ne ramène pas en arrière", FindDifficulty.MEDIUM, p.launchDifficulty)
    }

    @Test
    fun totalStarsAddUpTheBestMarkOfEachDifficultyUpToFifteen() {
        var p = FindProgress()
        for (d in FindDifficulty.entries) p = p.withResult(d, 3, 0, d.targets)
        assertEquals(15, p.totalStars)
        assertEquals(FindDifficulty.MAX_STARS, p.totalStars)
        assertEquals(FindDifficulty.MASTER, p.launchDifficulty)
    }

    @Test
    fun theSaveRoundTripsAndToleratesGarbage() {
        val p = FindProgress().withResult(FindDifficulty.EASY, 3, 0, 1).withResult(FindDifficulty.HARD, 2, 4, 3)
        assertEquals(p, FindProgress.fromJson(JSONObject(p.toJson().toString())))
        assertEquals(FindProgress(), FindProgress.fromJson(null))
        assertEquals(FindProgress(), FindProgress.fromJson(JSONObject("{}")))
        val bad = FindProgress.fromJson(JSONObject("""{"best":{"0":9,"7":2,"x":3,"2":-1,"1":3},"found":-4,"mistakes":"?","played":5}"""))
        assertEquals(mapOf(1 to 3), bad.best)
        assertEquals(0, bad.found)
        assertEquals(0, bad.mistakes)
        assertEquals(5, bad.played)
    }

    @Test
    fun theOldHundredLevelSaveIsIgnoredWithoutBreakingAnything() {
        // la version 0.12.0 rangeait ses étoiles par niveau sous « stars » : elles ne doivent pas être prises pour des difficultés
        val old = FindProgress.fromJson(JSONObject("""{"stars":{"1":3,"2":3,"3":2,"4":1,"5":3},"mistakes":7,"played":9}"""))
        assertEquals(0, old.totalStars)
        assertEquals(FindDifficulty.EASY, old.launchDifficulty)
        assertEquals(7, old.mistakes)
        assertEquals(9, old.played)
    }
}
