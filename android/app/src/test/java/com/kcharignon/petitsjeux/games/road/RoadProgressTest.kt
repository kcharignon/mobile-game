package com.kcharignon.petitsjeux.games.road

import com.kcharignon.petitsjeux.core.Games
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.road.engine.RoadWorlds
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RoadProgressTest {

    @Test
    fun everyLevelCanBePlayedFromTheStartAndJumpingAheadMovesTheRestartingPoint() {
        var p = RoadProgress()
        for (n in 1..5) p = p.withResult(n, 3, 100, 0)
        assertEquals(6, p.currentLevel)
        assertEquals("Niveau 6", RoadMiniGame.progressLabel(p.toJson()))
        // il trouve le jeu trop simple : il saute au niveau 30 sans avoir fini ceux d'avant, et le réussit
        p = p.withResult(30, 3, 100, 0)
        assertEquals("on reprend au niveau le plus loin atteint, plus un", 31, p.currentLevel)
        assertEquals("Niveau 31", RoadMiniGame.progressLabel(p.toJson()))
        assertTrue(p.isDone(30))
        assertFalse("les niveaux laissés derrière restent à faire", p.isDone(10))
        assertFalse(p.allDone)
        // rejouer un niveau plus ancien, ou en réussir un autre en chemin, ne fait pas reculer le point de reprise
        p = p.withResult(3, 3, 100, 0)
        assertEquals(31, p.currentLevel)
        p = p.withResult(7, 3, 100, 0)
        assertEquals(31, p.currentLevel)
    }

    @Test
    fun aNewPlayerStartsAtLevelOne() {
        val p = RoadProgress()
        assertEquals(0, p.totalStars)
        assertEquals(1, p.currentLevel)
        assertFalse(p.isDone(1))
        assertFalse(p.allDone)
    }

    @Test
    fun finishingALevelMovesTheStartingPointAndKeepsTheBest() {
        val p = RoadProgress().withResult(1, earnedStars = 2, score = 70, hitsNow = 1)
        assertEquals(2, p.starsOf(1))
        assertEquals(70, p.bestOf(1))
        assertEquals(2, p.currentLevel)
        assertTrue(p.isDone(1))
        assertFalse(p.isDone(2))
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

    @Test
    fun openingTheGameLaunchesTheNextLevelToDoAndTheGridOnlyWhenEverythingIsDone() {
        assertEquals("un nouveau joueur arrive direct sur le niveau 1", 1, RoadProgress().launchLevel)
        var p = RoadProgress()
        for (n in 1..5) p = p.withResult(n, 3, 100, 0)
        assertEquals(6, p.launchLevel)
        p = p.withResult(30, 3, 100, 0)
        assertEquals(31, p.launchLevel)
        for (n in 1..RoadWorlds.LEVEL_COUNT) p = p.withResult(n, 3, 100, 0)
        assertEquals("tout est réussi : plus de suivant, on montre la grille", null, p.launchLevel)
    }
}
