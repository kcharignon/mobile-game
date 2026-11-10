package com.kcharignon.petitsjeux.games.maze

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MazeProgressTest {
    @Test
    fun aNewPlayerStartsAtLevelOneWithNothingDone() {
        val p = MazeProgress()
        assertEquals(0, p.totalStars)
        assertEquals(0, p.levelsDone)
        assertEquals(1, p.currentLevel)
        assertTrue(p.isUnlocked(1))
        assertFalse(p.isUnlocked(2))
        assertFalse(p.allDone)
    }

    @Test
    fun aLevelUnlocksWhenThePreviousOneIsDone() {
        val p = MazeProgress().withResult(1, 3, 0).withResult(2, 2, 1)
        assertEquals(3, p.currentLevel)
        assertTrue(p.isUnlocked(3))
        assertFalse(p.isUnlocked(4))
        assertEquals(5, p.totalStars)
        assertEquals(1, p.detours)
        assertEquals(2, p.played)
    }

    @Test
    fun aReplayKeepsTheBestNoteButCountsTheGame() {
        val p = MazeProgress().withResult(1, 3, 0).withResult(1, 1, 5)
        assertEquals(3, p.starsOf(1))
        assertEquals(1, p.levelsDone)
        assertEquals(5, p.detours)
        assertEquals(2, p.played)
        assertEquals(3, MazeProgress().withResult(1, 1, 4).withResult(1, 3, 0).starsOf(1))
    }

    @Test
    fun aWinAlwaysEarnsBetweenOneAndThreeStars() {
        assertEquals(1, MazeProgress().withResult(1, 0, 0).starsOf(1))
        assertEquals(3, MazeProgress().withResult(1, 9, 0).starsOf(1))
    }

    @Test
    fun savingAndLoadingGivesTheSameProgress() {
        val p = MazeProgress().withResult(1, 3, 0).withResult(2, 2, 1).withResult(3, 1, 4).withResult(2, 3, 0)
        assertEquals(p, MazeProgress.fromJson(JSONObject(p.toJson().toString())))
    }

    @Test
    fun aMissingOrBrokenSaveIsZeroNeverAnError() {
        assertEquals(MazeProgress(), MazeProgress.fromJson(null))
        assertEquals(MazeProgress(), MazeProgress.fromJson(JSONObject()))
        val broken = JSONObject("""{"stars":{"abc":3,"1":9,"2":0,"101":2,"0":3,"3":2},"detours":-4,"played":"x"}""")
        val p = MazeProgress.fromJson(broken)
        assertEquals(mapOf(3 to 2), p.stars)
        assertEquals(0, p.detours)
        assertEquals(0, p.played)
    }

    @Test
    fun finishingAllHundredLevelsGivesThreeHundredStars() {
        var p = MazeProgress()
        for (n in 1..100) p = p.withResult(n, 3, 0)
        assertTrue(p.allDone)
        assertEquals(300, p.totalStars)
        assertEquals(100, p.currentLevel)
    }
}
