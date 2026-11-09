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
                game.finishMotion(hint.car.id)
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
            assertEquals("niveau ${level.index} : nombre de murs", p.walls, level.walls.size)
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

    // ---- le parking s'agrandit tous les 30 niveaux

    @Test
    fun theCarParkGrowsEvery30Levels() {
        for (block in 0 until 8) {
            val sizes = all.subList(block * 30, block * 30 + 30).map { it.cols to it.rows }.toSet()
            assertEquals("le parking ne change pas à l'intérieur de la tranche ${block + 1}", 1, sizes.size)
        }
        val areas = (0 until 8).map { all[it * 30].cols * all[it * 30].rows }
        assertTrue("le parking grandit à chaque tranche de 30 niveaux : $areas", areas.zipWithNext().all { (a, b) -> b > a })
        assertEquals(LevelGenerator.sizes, (0 until 8).map { all[it * 30].cols to all[it * 30].rows })
    }

    // ---- une mécanique de plus tous les 60 niveaux, qui se cumule avec les précédentes

    @Test
    fun theFirstGroupHasOnlyVehiclesThatGoBothWays() {
        assertTrue(group(0).all { it.cones.isEmpty() && it.walls.isEmpty() && it.cars.none { c -> c.carrier } })
    }

    @Test
    fun theSecondGroupAddsRoadworksCones() {
        assertTrue("des plots à chaque niveau du groupe 2", group(1).all { it.cones.isNotEmpty() })
        assertTrue(group(1).all { it.walls.isEmpty() && it.cars.none { c -> c.carrier } })
    }

    @Test
    fun theThirdGroupAddsWallsOnTheBorderAndKeepsTheCones() {
        assertTrue("des murs à chaque niveau du groupe 3", group(2).all { it.walls.isNotEmpty() })
        assertTrue("les plots restent", group(2).all { it.cones.isNotEmpty() })
        assertTrue(group(2).none { level -> level.cars.any { it.carrier } })
        // des murs sur le bord, qui murent bien des sorties
        assertTrue(group(2).all { level -> level.walls.all { w -> w.cell.col == 0 || w.cell.row == 0 || w.cell.col == level.cols - 1 || w.cell.row == level.rows - 1 } })
    }

    @Test
    fun theFourthGroupAddsPickupsThatDropACrateAndKeepsEverythingElse() {
        assertTrue("au moins un pick-up à caisse à chaque niveau du groupe 4", group(3).all { level -> level.cars.any { it.carrier } })
        assertTrue("les plots et les murs restent", group(3).all { it.cones.isNotEmpty() && it.walls.isNotEmpty() })
        assertTrue(all.filter { it.index < 181 }.none { level -> level.cars.any { it.carrier } })
        assertTrue(all.all { level -> level.cars.filter { it.carrier }.all { it.len == 3 } })
        assertTrue("3 pick-up à la fin", group(3).last().cars.count { it.carrier } >= 3)
    }

    @Test
    fun conesWallsAndCratesRampUpGradually() {
        // 1 ou 2 au début, puis de plus en plus
        assertTrue(all[60].cones.size == 1 && all[120].walls.size <= 2 && all[180].cars.count { it.carrier } == 1)
        assertTrue(all[119].cones.size > all[60].cones.size)
        assertTrue("jusqu'à 8 plots à la fin", all[239].cones.size >= 7)
        assertTrue("de plus en plus de murs : ${all[239].walls.size}", all[239].walls.size >= 7)
        assertTrue(all[150].walls.size > all[120].walls.size && all[239].walls.size > all[150].walls.size)
    }

    @Test
    fun theFirstLevelsOfEachMechanicAreSimpler() {
        // le nouvel élément arrive avec moins de véhicules que la fin du groupe d'avant, puis la difficulté remonte
        for (start in listOf(61, 121, 181)) {
            val before = all[start - 2].cars.size // dernier niveau du groupe précédent
            val first = all[start - 1].cars.size
            assertTrue("niveau $start : $first véhicules contre $before au niveau d'avant", first < before)
            assertTrue("la difficulté remonte après l'apprentissage", all[start + 11].cars.size > first)
        }
        assertEquals(3, all[0].cars.size)
        assertEquals(0.55, LevelGenerator.introFactor(61), 0.0)
        assertEquals(1.0, LevelGenerator.introFactor(70), 0.0)
    }

    @Test
    fun coneAndWallLevelsReallyUseTheirCones() {
        val coneLevels = all.filter { it.cones.isNotEmpty() }
        assertTrue("des plots qui coupent une voie dans presque tous les niveaux", coneLevels.count { analyses[it.index - 1].coneBlocks >= 1 } >= coneLevels.size * 0.9)
        val wallLevels = all.filter { it.walls.isNotEmpty() }
        assertTrue("des murs qui murent une sortie utile dans presque tous les niveaux", wallLevels.count { analyses[it.index - 1].wallBlocks >= 1 } >= wallLevels.size * 0.9)
    }

    @Test
    fun cratesAreARealTrapInTheLastLevels() {
        val trapped = all.filter { it.index >= 190 }.count { analyses[it.index - 1].traps >= 1 }
        assertTrue("des niveaux piégés dans la fin du groupe 4 ($trapped)", trapped >= 8)
    }

    @Test
    fun busesAndHeavyTrucksAppearFromTheThirdWorldOnly() {
        assertTrue(all.take(40).none { level -> level.cars.any { it.len == 4 } })
        assertTrue(all.drop(40).count { level -> level.cars.any { it.len == 4 } } >= 120)
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
    fun levelsGetHarderOverall() {
        fun avgDepth(range: IntRange) = range.map { analyses[it - 1].depth }.average()
        assertTrue(avgDepth(211..240) > avgDepth(1..30) + 2)
        assertTrue(avgDepth(121..150) > avgDepth(1..30))
        assertTrue(all[239].cars.size > all[0].cars.size * 6)
    }

    @Test
    fun theFirstLevelsAreNotTooCrowded() {
        for (level in all.take(30)) {
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
                for (wall in level.walls) append(";m").append(wall.cell.col).append(',').append(wall.cell.row).append(',').append(wall.side.ordinal)
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
        val EXPECTED_DIGEST_GROUP_1 = 0xa07cef107b3de258UL.toLong()
        val EXPECTED_DIGEST = 0x8ebf58048893ebacUL.toLong()
    }
}
