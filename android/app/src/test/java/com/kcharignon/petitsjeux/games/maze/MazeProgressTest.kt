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
        assertFalse(p.isDone(1))
        assertFalse(p.allDone)
    }

    @Test
    fun finishingALevelMovesTheStartingPointToTheNextOne() {
        val p = MazeProgress().withResult(1, 3, 0).withResult(2, 2, 1)
        assertEquals(3, p.currentLevel)
        assertTrue(p.isDone(1) && p.isDone(2))
        assertFalse(p.isDone(3))
        assertEquals(5, p.totalStars)
        assertEquals(1, p.detours)
        assertEquals(2, p.played)
    }


    @Test
    fun everyLevelCanBePlayedFromTheStartAndJumpingAheadMovesTheRestartingPoint() {
        var p = MazeProgress()
        for (n in 1..10) p = p.withResult(n, 3, 0)
        assertEquals(11, p.currentLevel)
        assertEquals("Niveau 11", MazeMiniGame.progressLabel(p.toJson()))
        // il trouve le jeu trop simple : il saute au niveau 40 sans avoir fini ceux d'avant, et le réussit
        p = p.withResult(40, 3, 0)
        assertEquals("on reprend au niveau le plus loin atteint, plus un", 41, p.currentLevel)
        assertEquals("Niveau 41", MazeMiniGame.progressLabel(p.toJson()))
        assertTrue(p.isDone(40))
        assertFalse("les niveaux laissés derrière restent à faire", p.isDone(15))
        assertFalse(p.allDone)
        // rejouer un niveau plus ancien, ou en réussir un autre en chemin, ne fait pas reculer le point de reprise
        p = p.withResult(3, 3, 0)
        assertEquals(41, p.currentLevel)
        p = p.withResult(12, 3, 0)
        assertEquals(41, p.currentLevel)
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
