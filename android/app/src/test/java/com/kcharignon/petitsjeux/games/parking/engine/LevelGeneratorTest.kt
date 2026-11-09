package com.kcharignon.petitsjeux.games.parking.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DependenciesTest {
    @Test
    fun aPinwheelOfFourCarsIsUnsolvable() {
        // Chaque voiture attend la suivante : A -> B -> C -> D -> A.
        val cars = listOf(
            Car(0, 0, 0, 2, Dir.RIGHT, 0), // A : (0,0)-(1,0), voie (2,0),(3,0)
            Car(1, 3, 0, 2, Dir.DOWN, 1), // B : (3,0)-(3,1), voie (3,2),(3,3)
            Car(2, 2, 3, 2, Dir.LEFT, 2), // C : (2,3)-(3,3), voie (1,3),(0,3)
            Car(3, 0, 2, 2, Dir.UP, 3), // D : (0,2)-(0,3), voie (0,1),(0,0)
        )
        assertNull(Dependencies.of(cars, 4, 4))
    }

    @Test
    fun aChainHasOneWavePerLink() {
        // A attend B qui attend C ; C est libre.
        val cars = listOf(
            Car(0, 0, 0, 2, Dir.RIGHT, 0), // A : voie (2,0)...
            Car(1, 2, 0, 2, Dir.RIGHT, 1), // B : (2,0)-(3,0), voie (4,0)...
            Car(2, 4, 0, 2, Dir.RIGHT, 2), // C : (4,0)-(5,0), voie libre
        )
        val deps = Dependencies.of(cars, 6, 1)!!
        assertEquals(3, deps.depth)
        assertEquals(1, deps.freeAtStart)
        assertEquals(setOf(1, 2), deps.blockers.getValue(0))
        assertEquals(emptySet<Int>(), deps.blockers.getValue(2))
    }
}

class LevelGeneratorTest {
    private val all = (1..Worlds.LEVEL_COUNT).map { LevelGenerator.level(it) }

    @Test
    fun splitMix64MatchesTheReferenceVector() {
        val rng = LevelGenerator.SplitMix64(0L)
        assertEquals(0xE220A8397B1DCDAFUL.toLong(), rng.nextLong())
        assertEquals(0x6E789E6AA1B965F4UL.toLong(), rng.nextLong())
    }

    @Test
    fun everyLevelIsSolvable() {
        for (level in all) {
            assertNotNull("le niveau ${level.index} a un cycle d'attente", Dependencies.of(level))
            val game = ParkingGame(level)
            var guard = 0
            while (!game.isWon) {
                val hint = game.hint() ?: error("niveau ${level.index} : plus de voiture libre alors qu'il en reste ${game.carsLeft}")
                assertTrue(game.tap(hint.id) is TapResult.Exited)
                assertTrue(++guard <= level.cars.size)
            }
            assertEquals("niveau ${level.index} : aucune faute en suivant l'indice", 0, game.bumps)
        }
    }

    @Test
    fun levelsFollowTheirParameters() {
        for (level in all) {
            val p = LevelGenerator.paramsFor(level.index)
            assertEquals(p.cols, level.cols)
            assertEquals(p.rows, level.rows)
            assertEquals("niveau ${level.index} : nombre de voitures", p.cars, level.cars.size)
            assertTrue(level.cars.all { it.color in 0 until LevelGenerator.COLORS })
        }
    }

    @Test
    fun neighbouringCarsHaveDifferentColours() {
        for (level in all) {
            for (a in level.cars) for (b in level.cars) {
                if (a.id >= b.id) continue
                val touching = a.cells.any { x -> b.cells.any { y -> kotlin.math.abs(x.col - y.col) <= 1 && kotlin.math.abs(x.row - y.row) <= 1 } }
                if (touching && level.cars.size <= LevelGenerator.COLORS) {
                    assertTrue("niveau ${level.index} : voitures ${a.id} et ${b.id} voisines de même couleur", a.color != b.color)
                }
            }
        }
    }

    @Test
    fun theFirstLevelsAreEasyAndLaterOnesAreHarder() {
        fun avgDepth(range: IntRange) = range.map { Dependencies.of(all[it - 1])!!.depth }.average()
        assertTrue("le niveau 1 doit être simple", all[0].cars.size <= 3)
        assertTrue("le niveau 1 montre déjà qu'il y a un ordre", Dependencies.of(all[0])!!.depth >= 2)
        assertTrue(avgDepth(81..100) > avgDepth(1..20) + 3)
        assertTrue(avgDepth(41..60) > avgDepth(1..20))
        // Plus de voitures à mesure que l'on avance
        assertTrue(all[99].cars.size > all[0].cars.size * 5)
    }

    @Test
    fun theFirstLevelsAreSmallAndNotTooCrowded() {
        for (level in all.take(20)) {
            assertTrue(level.cols <= 5 && level.rows <= 6)
            val cells = level.cars.sumOf { it.len }
            assertTrue("niveau ${level.index} : trop dense", cells <= level.cols * level.rows * 0.75)
        }
    }

    @Test
    fun theGeneratorIsDeterministic() {
        for (index in listOf(1, 7, 33, 64, 100, 101, 250)) {
            assertEquals(LevelGenerator.generate(index), LevelGenerator.generate(index))
        }
    }

    @Test
    fun levelsBeyondTheLastWorldStillWork() {
        val level = LevelGenerator.level(150)
        assertEquals(150, level.index)
        assertEquals(Worlds.COUNT - 1, level.world)
        assertNotNull(Dependencies.of(level))
    }

    @Test
    fun levelsNeverChange() {
        // Empreinte de TOUS les niveaux (FNV-1a 64 bits) : si ce test casse, les niveaux ont changé sous les pieds des joueurs
        // (leurs étoiles sont rangées par numéro de niveau). Ne changer cette valeur que volontairement, avec une nouvelle version.
        var hash = -0x340d631b7bdddcdbL
        for (level in all) {
            val text = buildString {
                append(level.index).append(':').append(level.cols).append('x').append(level.rows)
                for (car in level.cars) append(';').append(car.col).append(',').append(car.row).append(',').append(car.len).append(',').append(car.dir.ordinal).append(',').append(car.color)
            }
            for (ch in text) {
                hash = hash xor ch.code.toLong()
                hash *= 0x100000001b3L
            }
        }
        assertEquals("empreinte des niveaux : ${java.lang.Long.toHexString(hash)}", EXPECTED_DIGEST, hash)
    }

    private companion object {
        const val EXPECTED_DIGEST = 0x175f619c0e518b83L
    }
}
