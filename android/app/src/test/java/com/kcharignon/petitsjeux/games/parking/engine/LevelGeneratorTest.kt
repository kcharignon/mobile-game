package com.kcharignon.petitsjeux.games.parking.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class LevelGeneratorTest {
    private val all = (1..Worlds.LEVEL_COUNT).map { LevelGenerator.level(it) }
    private val analyses = all.map { Solver.analyse(it) }

    private fun group(g: Int) = all.filter { Worlds.groupOf(it.index) == g }

    @Test
    fun splitMix64MatchesTheReferenceVector() {
        val rng = LevelGenerator.SplitMix64(0L)
        assertEquals(0xE220A8397B1DCDAFUL.toLong(), rng.nextLong())
        assertEquals(0x6E789E6AA1B965F4UL.toLong(), rng.nextLong())
    }

    @Test
    fun thereAre240LevelsInFourGroupsOf60() {
        assertEquals(240, Worlds.LEVEL_COUNT)
        assertEquals(12, Worlds.COUNT)
        assertEquals(12, Worlds.names.size)
        assertEquals(listOf(0, 0, 1, 2, 3, 3), listOf(1, 60, 61, 121, 181, 999).map { Worlds.groupOf(it) })
    }

    @Test
    fun everyLevelIsSolvableAndTheHintsAlwaysWinWithoutAFault() {
        for ((i, level) in all.withIndex()) {
            assertTrue("le niveau ${level.index} est insoluble", analyses[i].solvable)
            val game = ParkingGame(level)
            var guard = 0
            while (!game.isWon) {
                assertFalse("niveau ${level.index} : impasse alors que l'on suit les indices", game.isStuck)
                val hint = game.hint() ?: error("niveau ${level.index} : aucun coup alors qu'il reste ${game.carsLeft} véhicules")
                assertTrue(game.tap(hint.car.id, hint.move) is TapResult.Exited)
                assertTrue(++guard <= level.cars.size)
            }
            assertEquals("niveau ${level.index} : aucune faute en suivant les indices", 0, game.bumps)
        }
    }

    @Test
    fun levelsFollowTheirParameters() {
        for (level in all) {
            val p = LevelGenerator.paramsFor(level.index)
            assertEquals(p.cols, level.cols)
            assertEquals(p.rows, level.rows)
            assertEquals("niveau ${level.index} : nombre de véhicules", p.cars, level.cars.size)
            assertEquals("niveau ${level.index} : nombre de plots", p.cones, level.cones.size)
            assertEquals("niveau ${level.index} : nombre de pick-up à caisse", p.carriers, level.cars.count { it.carrier })
            assertTrue(level.cars.all { it.color in 0 until LevelGenerator.COLORS })
        }
    }

    @Test
    fun neighbouringVehiclesHaveDifferentColours() {
        for (level in all) {
            if (level.cars.size > LevelGenerator.COLORS) continue
            for (a in level.cars) for (b in level.cars) {
                if (a.id >= b.id) continue
                val touching = a.cells.any { x -> b.cells.any { y -> abs(x.col - y.col) <= 1 && abs(x.row - y.row) <= 1 } }
                if (touching) assertTrue("niveau ${level.index} : véhicules ${a.id} et ${b.id} voisins de même couleur", a.color != b.color)
            }
        }
    }

    // ---- une mécanique de plus à chaque groupe de 60 niveaux

    @Test
    fun theFirstGroupHasOnlyVehiclesThatGoBothWays() {
        assertTrue(group(0).all { it.cones.isEmpty() && it.cars.none { c -> c.carrier } })
        assertTrue(group(0).all { it.cols <= 6 && it.rows <= 8 })
    }

    @Test
    fun theSecondGroupAddsRoadworksCones() {
        assertTrue("des plots à chaque niveau du groupe 2", group(1).all { it.cones.isNotEmpty() })
        assertTrue(group(1).none { level -> level.cars.any { it.carrier } })
        assertTrue("plus de plots vers la fin", group(1).last().cones.size > group(1).first().cones.size)
    }

    @Test
    fun theThirdGroupEnlargesTheCarPark() {
        assertTrue(group(2).any { it.cols >= 8 && it.rows >= 10 })
        assertTrue(group(2).first().cols * group(2).first().rows >= group(1).first().cols * group(1).first().rows)
        assertTrue(group(2).none { level -> level.cars.any { it.carrier } })
        assertTrue(group(2).all { it.cones.isNotEmpty() })
    }

    @Test
    fun theFourthGroupAddsPickupsThatDropACrate() {
        assertTrue("au moins un pick-up à caisse à chaque niveau du groupe 4", group(3).all { level -> level.cars.any { it.carrier } })
        assertTrue(all.filter { it.index < 181 }.none { level -> level.cars.any { it.carrier } })
        assertTrue(all.all { level -> level.cars.filter { it.carrier }.all { it.len == 3 } })
        assertTrue("jusqu'à 3 pick-up vers la fin", group(3).last().cars.count { it.carrier } >= 3)
    }

    @Test
    fun cratesAreARealTrapInTheLastLevels() {
        // un camion qui part du mauvais côté ferme un passage : l'indice ne le conseille jamais, mais un joueur peut s'y tromper
        val trapped = all.filter { it.index >= 190 }.count { analyses[it.index - 1].traps >= 1 }
        assertTrue("des niveaux piégés dans la fin du groupe 4 ($trapped)", trapped >= 8)
    }

    @Test
    fun busesAndHeavyTrucksAppearFromTheThirdWorldOnly() {
        assertTrue(all.take(40).none { level -> level.cars.any { it.len == 4 } })
        assertTrue(all.drop(40).count { level -> level.cars.any { it.len == 4 } } >= 150)
        assertTrue(all.all { level -> level.cars.all { it.len in 2..4 } })
    }

    @Test
    fun theFirstLevelsTeachReversing() {
        assertTrue("le niveau 1 est simple", all[0].cars.size <= 3)
        val teaching = (2..12).count { analyses[it - 1].backwardOnly >= 1 }
        assertTrue("des niveaux 2 à 12 obligent à reculer ($teaching)", teaching >= 9)
        assertTrue("le niveau 2 oblige à reculer", analyses[1].backwardOnly >= 1)
    }

    @Test
    fun levelsGetHarderAndBigger() {
        fun avgDepth(range: IntRange) = range.map { analyses[it - 1].depth }.average()
        assertTrue(avgDepth(181..240) > avgDepth(1..20) + 2)
        assertTrue(avgDepth(121..180) > avgDepth(1..60))
        assertTrue(all[239].cars.size > all[0].cars.size * 5)
        assertTrue(group(3).first().cols * group(3).first().rows > group(0).first().cols * group(0).first().rows)
    }

    @Test
    fun theFirstLevelsAreSmallAndNotTooCrowded() {
        for (level in all.take(20)) {
            assertTrue(level.cols <= 5 && level.rows <= 6)
            val cells = level.cars.sumOf { it.len } + level.cones.size
            assertTrue("niveau ${level.index} : trop dense", cells <= level.cols * level.rows * 0.8)
        }
    }

    @Test
    fun theGeneratorIsDeterministic() {
        for (index in listOf(1, 7, 33, 64, 100, 150, 190, 240, 241, 400)) {
            assertEquals(LevelGenerator.generate(index), LevelGenerator.generate(index))
        }
    }

    @Test
    fun levelsBeyondTheLastWorldStillWork() {
        val level = LevelGenerator.level(400)
        assertEquals(400, level.index)
        assertEquals(Worlds.COUNT - 1, level.world)
        assertTrue(Solver.canFinish(level))
    }

    // Empreinte (FNV-1a 64 bits) des niveaux : si un de ces tests casse, des niveaux ont changé sous les pieds des joueurs
    // (leurs étoiles sont rangées par numéro de niveau). Ne changer ces valeurs que volontairement, avec une nouvelle version.
    private fun digest(levels: List<Level>): Long {
        var hash = -0x340d631b7bdddcdbL
        for (level in levels) {
            val text = buildString {
                append(level.index).append(':').append(level.cols).append('x').append(level.rows)
                for (car in level.cars) {
                    append(';').append(car.col).append(',').append(car.row).append(',').append(car.len).append(',').append(car.dir.ordinal).append(',').append(car.color)
                    if (car.carrier) append('c')
                }
                for (cone in level.cones) append(";p").append(cone.col).append(',').append(cone.row)
            }
            for (ch in text) {
                hash = hash xor ch.code.toLong()
                hash *= 0x100000001b3L
            }
        }
        return hash
    }

    @Test
    fun theFirstGroupNeverChanges() {
        val hash = digest(all.take(60))
        assertEquals("empreinte des niveaux 1 à 60 : ${java.lang.Long.toHexString(hash)}", EXPECTED_DIGEST_GROUP_1, hash)
    }

    @Test
    fun levelsNeverChange() {
        val hash = digest(all)
        assertEquals("empreinte des niveaux : ${java.lang.Long.toHexString(hash)}", EXPECTED_DIGEST, hash)
    }

    private companion object {
        const val EXPECTED_DIGEST_GROUP_1 = 0x5d04d83bb6a67f9fL
        val EXPECTED_DIGEST = 0xa2a260b652b59751UL.toLong()
    }
}
