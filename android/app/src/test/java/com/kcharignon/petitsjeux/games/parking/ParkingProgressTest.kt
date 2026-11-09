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
        assertTrue(p.isUnlocked(1))
        assertFalse(p.isUnlocked(2))
        assertEquals(0, p.totalStars)
    }

    @Test
    fun finishingALevelUnlocksTheNextOne() {
        val p = ParkingProgress().withResult(1, 3, freed = 3, faults = 0).withResult(2, 1, freed = 4, faults = 5)
        assertEquals(3, p.currentLevel)
        assertTrue(p.isUnlocked(3))
        assertFalse(p.isUnlocked(4))
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
    fun finishingEverythingStopsAtTheLastLevel() {
        var p = ParkingProgress()
        for (level in 1..Worlds.LEVEL_COUNT) p = p.withResult(level, 3, 1, 0)
        assertTrue(p.allDone)
        assertEquals(Worlds.LEVEL_COUNT, p.currentLevel)
        assertTrue(p.isUnlocked(Worlds.LEVEL_COUNT))
        assertFalse(p.isUnlocked(Worlds.LEVEL_COUNT + 1))
        assertEquals(3 * Worlds.LEVEL_COUNT, p.totalStars)
    }
}
