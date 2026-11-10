package com.kcharignon.petitsjeux.games.parking

import com.kcharignon.petitsjeux.games.parking.engine.Worlds
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ParkingProgressTest {
    @Test
    fun aNewPlayerStartsAtLevelOne() {
        val p = ParkingProgress()
        assertEquals(1, p.currentLevel)
        assertFalse(p.isDone(1))
        assertEquals(0, p.totalStars)
    }

    @Test
    fun finishingALevelMovesTheStartingPointToTheNextOne() {
        val p = ParkingProgress().withResult(1, 3, freed = 3, faults = 0).withResult(2, 1, freed = 4, faults = 5)
        assertEquals(3, p.currentLevel)
        assertTrue(p.isDone(1) && p.isDone(2))
        assertFalse(p.isDone(3))
        assertEquals(4, p.totalStars)
        assertEquals(7, p.carsFreed)
        assertEquals(5, p.bumps)
        assertEquals(2, p.played)
    }

    @Test
    fun theBestMarkIsKept() {
        val p = ParkingProgress().withResult(1, 3, 3, 0).withResult(1, 1, 3, 4)
        assertEquals(3, p.starsOf(1))
        assertEquals(2, p.played)
        val q = ParkingProgress().withResult(1, 1, 3, 4).withResult(1, 2, 3, 1)
        assertEquals(2, q.starsOf(1))
    }

    @Test
    fun starsAreClampedToOneToThree() {
        assertEquals(1, ParkingProgress().withResult(1, 0, 1, 0).starsOf(1))
        assertEquals(3, ParkingProgress().withResult(1, 9, 1, 0).starsOf(1))
    }

    @Test
    fun jsonRoundTrips() {
        val p = ParkingProgress().withResult(1, 3, 3, 0).withResult(2, 2, 4, 2)
        assertEquals(p, ParkingProgress.fromJson(JSONObject(p.toJson().toString())))
    }

    @Test
    fun readingIsForgiving() {
        assertEquals(ParkingProgress(), ParkingProgress.fromJson(null))
        assertEquals(ParkingProgress(), ParkingProgress.fromJson(JSONObject("{}")))
        val odd = JSONObject("""{"stars":{"1":3,"x":2,"2":9,"-4":2,"3":0},"carsFreed":-5,"bumps":"a","played":4}""")
        val p = ParkingProgress.fromJson(odd)
        assertEquals(mapOf(1 to 3), p.stars)
        assertEquals(0, p.carsFreed)
        assertEquals(0, p.bumps)
        assertEquals(4, p.played)
    }


    @Test
    fun everyLevelCanBePlayedFromTheStartAndJumpingAheadMovesTheRestartingPoint() {
        var p = ParkingProgress()
        for (n in 1..10) p = p.withResult(n, 3, 1, 0)
        assertEquals(11, p.currentLevel)
        assertEquals("Niveau 11", ParkingMiniGame.progressLabel(p.toJson()))
        // il trouve le jeu trop simple : il saute au niveau 40 sans avoir fini ceux d'avant, et le réussit
        p = p.withResult(40, 3, 1, 0)
        assertEquals("on reprend au niveau le plus loin atteint, plus un", 41, p.currentLevel)
        assertEquals("Niveau 41", ParkingMiniGame.progressLabel(p.toJson()))
        assertTrue(p.isDone(40))
        assertFalse("les niveaux laissés derrière restent à faire", p.isDone(15))
        assertFalse(p.allDone)
        // rejouer un niveau plus ancien, ou en réussir un autre en chemin, ne fait pas reculer le point de reprise
        p = p.withResult(3, 3, 1, 0)
        assertEquals(41, p.currentLevel)
        p = p.withResult(12, 3, 1, 0)
        assertEquals(41, p.currentLevel)
    }

    @Test
    fun finishingEverythingStopsAtTheLastLevel() {
        var p = ParkingProgress()
        for (level in 1..Worlds.LEVEL_COUNT) p = p.withResult(level, 3, 1, 0)
        assertTrue(p.allDone)
        assertEquals(Worlds.LEVEL_COUNT, p.currentLevel)
        assertTrue(p.isDone(Worlds.LEVEL_COUNT))
        assertFalse(p.isDone(Worlds.LEVEL_COUNT + 1))
        assertEquals(3 * Worlds.LEVEL_COUNT, p.totalStars)
    }

    @Test
    fun openingTheGameLaunchesTheNextLevelToDoAndTheGridOnlyWhenEverythingIsDone() {
        assertEquals("un nouveau joueur arrive direct sur le niveau 1", 1, ParkingProgress().launchLevel)
        var p = ParkingProgress()
        for (n in 1..10) p = p.withResult(n, 3, 1, 0)
        assertEquals(11, p.launchLevel)
        // après un saut en avant, on reprend au plus loin atteint
        p = p.withResult(40, 3, 1, 0)
        assertEquals(41, p.launchLevel)
        for (level in 1..Worlds.LEVEL_COUNT) p = p.withResult(level, 3, 1, 0)
        assertEquals("tout est réussi : plus de suivant, on montre la grille", null, p.launchLevel)
    }
}
