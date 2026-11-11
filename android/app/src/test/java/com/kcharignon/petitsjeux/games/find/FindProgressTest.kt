package com.kcharignon.petitsjeux.games.find

import com.kcharignon.petitsjeux.games.find.engine.FindWorlds
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FindProgressTest {
    @Test
    fun aNewPlayerStartsAtLevelOneAndTheGameOpensOnIt() {
        val p = FindProgress()
        assertEquals(0, p.totalStars)
        assertEquals(1, p.currentLevel)
        assertEquals(1, p.launchLevel)
        assertFalse(p.isDone(1))
        assertFalse(p.allDone)
    }

    @Test
    fun finishingALevelKeepsTheBestMarkAndMovesTheStartingPoint() {
        var p = FindProgress().withResult(1, earnedStars = 2, mistakesNow = 1)
        assertEquals(2, p.starsOf(1))
        assertEquals(2, p.currentLevel)
        assertEquals(1, p.mistakes)
        assertEquals(1, p.played)
        p = p.withResult(1, earnedStars = 1, mistakesNow = 3)
        assertEquals("on garde la meilleure note", 2, p.starsOf(1))
        p = p.withResult(1, earnedStars = 3, mistakesNow = 0)
        assertEquals(3, p.starsOf(1))
        assertEquals(3, p.played)
        assertEquals(4, p.mistakes)
        assertEquals(3, p.totalStars)
        assertEquals("rejouer ne fait pas reculer", 2, p.currentLevel)
    }

    @Test
    fun everyLevelCanBePlayedFromTheStartAndJumpingAheadMovesTheRestartingPoint() {
        var p = FindProgress()
        for (n in 1..10) p = p.withResult(n, 3, 0)
        assertEquals(11, p.currentLevel)
        p = p.withResult(40, 3, 0)
        assertEquals(41, p.currentLevel)
        assertEquals(41, p.launchLevel)
        assertFalse(p.isDone(15))
        p = p.withResult(3, 3, 0)
        assertEquals("rejouer un ancien niveau ne fait pas reculer", 41, p.currentLevel)
    }

    @Test
    fun finishingEverythingGivesThreeHundredStarsAndOpensTheGridInsteadOfALevel() {
        var p = FindProgress()
        for (n in 1..FindWorlds.LEVEL_COUNT) p = p.withResult(n, 3, 0)
        assertTrue(p.allDone)
        assertEquals(300, p.totalStars)
        assertEquals(FindWorlds.LEVEL_COUNT, p.currentLevel)
        assertNull(p.launchLevel)
    }

    @Test
    fun savingAndReadingBackGivesTheSameThing() {
        val p = FindProgress().withResult(3, 2, 1).withResult(12, 3, 0)
        assertEquals(p, FindProgress.fromJson(JSONObject(p.toJson().toString())))
    }

    @Test
    fun aBrokenSaveNeverCrashes() {
        assertEquals(FindProgress(), FindProgress.fromJson(null))
        val weird = JSONObject("""{"stars":{"1":3,"x":2,"2":9,"0":1,"101":2,"5":0},"mistakes":-4,"played":"a"}""")
        val p = FindProgress.fromJson(weird)
        assertEquals(mapOf(1 to 3), p.stars)
        assertEquals(0, p.mistakes)
        assertEquals(0, p.played)
    }
}
