package com.kcharignon.petitsjeux.games.road

import com.kcharignon.petitsjeux.core.Games
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.Profile
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RoadProgressTest {
    @Test
    fun aNewPlayerStartsAtLevelOne() {
        val p = RoadProgress()
        assertEquals(0, p.totalStars)
        assertEquals(1, p.currentLevel)
        assertTrue(p.isUnlocked(1))
        assertFalse(p.isUnlocked(2))
        assertFalse(p.allDone)
    }

    @Test
    fun finishingALevelUnlocksTheNextAndKeepsTheBest() {
        val p = RoadProgress().withResult(1, earnedStars = 2, score = 70, hitsNow = 1)
        assertEquals(2, p.starsOf(1))
        assertEquals(70, p.bestOf(1))
        assertEquals(2, p.currentLevel)
        assertTrue(p.isUnlocked(2))
        assertFalse(p.isUnlocked(3))
        // un rejeu moins bon ne baisse rien ; un meilleur remonte
        val worse = p.withResult(1, earnedStars = 1, score = 40, hitsNow = 3)
        assertEquals(2, worse.starsOf(1))
        assertEquals(70, worse.bestOf(1))
        assertEquals(4, worse.hits)
        assertEquals(2, worse.crossings)
        val better = worse.withResult(1, earnedStars = 3, score = 120, hitsNow = 0)
        assertEquals(3, better.starsOf(1))
        assertEquals(120, better.bestOf(1))
        assertEquals(3, better.totalStars)
    }

    @Test
    fun starsAreAlwaysBetweenOneAndThree() {
        assertEquals(1, RoadProgress().withResult(1, 0, 10, 0).starsOf(1))
        assertEquals(3, RoadProgress().withResult(1, 9, 10, 0).starsOf(1))
        assertEquals(0, RoadProgress().withResult(1, 2, -50, 0).bestOf(1).coerceAtLeast(0))
    }

    @Test
    fun theSaveRoundTrips() {
        val p = RoadProgress().withResult(1, 3, 150, 0).withResult(2, 2, 100, 2).withResult(3, 1, 60, 5)
        assertEquals(p, RoadProgress.fromJson(JSONObject(p.toJson().toString())))
    }

    @Test
    fun aDamagedSaveNeverCrashes() {
        assertEquals(RoadProgress(), RoadProgress.fromJson(null))
        assertEquals(RoadProgress(), RoadProgress.fromJson(JSONObject()))
        val bad = JSONObject("""{"stars":{"1":5,"x":2,"2":0,"3":2,"999":3,"-1":3},"best":{"3":-4,"4":80,"y":3},"hits":-7,"crossings":"abc"}""")
        val p = RoadProgress.fromJson(bad)
        assertEquals(mapOf(3 to 2), p.stars)
        assertEquals(mapOf(4 to 80), p.best)
        assertEquals(0, p.hits)
        assertEquals(0, p.crossings)
    }

    @Test
    fun theGameJoinsThePlug() {
        val save = RoadProgress().withResult(1, 3, 150, 0).withResult(2, 2, 100, 0).toJson()
        assertEquals("road", RoadMiniGame.id)
        assertNotNull(Games.byId("road"))
        assertEquals("Nouveau !", RoadMiniGame.progressLabel(null))
        assertEquals("Niveau 3", RoadMiniGame.progressLabel(save))
        assertEquals(5, RoadMiniGame.stars(save))
        assertEquals(0, RoadMiniGame.stars(null))
        // les étoiles du jeu comptent pour le rang du joueur, avec celles des autres
        val player = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!.withGameSave("road", save)
        assertEquals(5, Games.totalStars(player))
    }
}
