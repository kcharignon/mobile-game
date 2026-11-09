package com.kcharignon.petitsjeux.games.parking.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class LevelGeneratorTest {
    private val all get() = ALL
    private val analyses get() = ANALYSES

    private fun world(w: Int) = all.filter { Worlds.of(it.index) == w }

    @Test
    fun splitMix64MatchesTheReferenceVector() {
        val rng = LevelGenerator.SplitMix64(0L)
        assertEquals(0xE220A8397B1DCDAFUL.toLong(), rng.nextLong())
        assertEquals(0x6E789E6AA1B965F4UL.toLong(), rng.nextLong())
    }

    @Test
    fun thereAre180LevelsInNineWorldsOf20() {
        assertEquals(180, Worlds.LEVEL_COUNT)
        assertEquals(9, Worlds.COUNT)
        assertEquals(9, Worlds.names.size)
        assertEquals(listOf(0, 0, 1, 1, 2, 3, 4, 4, 5, 6, 7, 8, 8), listOf(1, 20, 21, 40, 41, 61, 81, 100, 101, 121, 141, 161, 999).map { Worlds.of(it) })
        assertEquals(listOf(1, 21, 41, 61, 81, 101, 121, 141, 161), (0 until Worlds.COUNT).map { Worlds.firstLevel(it) })
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
            // les 100 premiers niveaux sont figés ; plus loin, il peut manquer un véhicule ou deux quand le parking est très rempli
            if (level.index <= 100) assertEquals("niveau ${level.index} : nombre de véhicules", p.cars, level.cars.size)
            else assertTrue("niveau ${level.index} : ${level.cars.size} véhicules pour ${p.cars} voulus", level.cars.size in (p.cars - 2)..p.cars)
            assertEquals("niveau ${level.index} : nombre de plots", p.cones, level.cones.size)
            assertEquals("niveau ${level.index} : nombre de murs", p.walls, level.walls.size)
            assertEquals("niveau ${level.index} : nombre de pick-up à caisse", p.carriers, level.cars.count { it.carrier })
            assertEquals("niveau ${level.index} : nombre de taches d'huile", p.oils, level.oils.size)
            assertEquals("niveau ${level.index} : nombre de sens uniques", p.oneWays, level.oneWays.size)
            assertEquals("niveau ${level.index} : nombre de tapis roulants", p.belts, level.belts.size)
            assertEquals("niveau ${level.index} : nombre de téléporteurs", p.portals, level.portals.size)
            assertEquals("niveau ${level.index} : nombre de barrières", p.gates, level.gates.size)
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

    // ---- tous les 20 niveaux : le parking s'agrandit ET une nouvelle mécanique arrive

    @Test
    fun theCarParkGrowsEvery20Levels() {
        for (w in 0 until Worlds.COUNT) {
            val sizes = world(w).map { it.cols to it.rows }.toSet()
            assertEquals("le parking ne change pas à l'intérieur du monde ${w + 1}", 1, sizes.size)
        }
        val areas = (0 until Worlds.COUNT).map { all[it * 20].cols * all[it * 20].rows }
        assertTrue("le parking grandit à chaque monde : $areas", areas.zipWithNext().all { (a, b) -> b > a })
        assertEquals(LevelGenerator.sizes, (0 until Worlds.COUNT).map { all[it * 20].cols to all[it * 20].rows })
    }

    @Test
    fun theFirstWorldHasOnlyVehiclesThatGoBothWays() {
        assertTrue(world(0).all { it.cones.isEmpty() && it.walls.isEmpty() && it.oils.isEmpty() && it.cars.none { c -> c.carrier } })
    }

    @Test
    fun theSecondWorldAddsRoadworksCones() {
        assertTrue("des plots à chaque niveau du monde 2", world(1).all { it.cones.isNotEmpty() })
        assertTrue(world(1).all { it.walls.isEmpty() && it.oils.isEmpty() && it.cars.none { c -> c.carrier } })
    }

    @Test
    fun theThirdWorldAddsWallsOnTheBorderAndKeepsTheCones() {
        assertTrue("des murs à chaque niveau du monde 3", world(2).all { it.walls.isNotEmpty() })
        assertTrue("les plots restent", world(2).all { it.cones.isNotEmpty() })
        assertTrue(world(2).all { it.oils.isEmpty() && it.cars.none { c -> c.carrier } })
        // des murs sur le bord du parking
        assertTrue(world(2).all { level -> level.walls.all { w -> w.cell.col == 0 || w.cell.row == 0 || w.cell.col == level.cols - 1 || w.cell.row == level.rows - 1 } })
    }

    @Test
    fun theFourthWorldAddsPickupsThatDropACrateAndKeepsEverythingElse() {
        assertTrue("au moins un pick-up à caisse à chaque niveau du monde 4", world(3).all { level -> level.cars.any { it.carrier } })
        assertTrue("les plots et les murs restent", world(3).all { it.cones.isNotEmpty() && it.walls.isNotEmpty() })
        assertTrue(world(3).all { it.oils.isEmpty() })
        assertTrue(all.filter { it.index < 61 }.none { level -> level.cars.any { it.carrier } })
        assertTrue(all.all { level -> level.cars.filter { it.carrier }.all { it.len == 3 } })
        assertTrue("3 pick-up à la fin", all[99].cars.count { it.carrier } >= 3)
    }

    @Test
    fun theFifthWorldAddsOilSlicksAndKeepsEverythingElse() {
        assertTrue("des taches d'huile à chaque niveau du monde 5", world(4).all { it.oils.isNotEmpty() })
        assertTrue("tout le reste est toujours là", world(4).all { it.cones.isNotEmpty() && it.walls.isNotEmpty() && it.cars.any { c -> c.carrier } })
        assertTrue(all.filter { it.index < 81 }.none { it.oils.isNotEmpty() })
    }

    @Test
    fun theSixthWorldAddsOneWaySignsAndKeepsEverythingElse() {
        assertTrue("des sens uniques à chaque niveau du monde 6", world(5).all { it.oneWays.isNotEmpty() })
        assertTrue("tout le reste est toujours là", world(5).all { it.cones.isNotEmpty() && it.walls.isNotEmpty() && it.oils.isNotEmpty() && it.cars.any { c -> c.carrier } })
        assertTrue(world(5).all { it.belts.isEmpty() && it.portals.isEmpty() && it.gates.isEmpty() })
    }

    @Test
    fun theSeventhWorldAddsConveyorBelts() {
        assertTrue("des tapis à chaque niveau du monde 7", world(6).all { it.belts.isNotEmpty() })
        assertTrue("les sens uniques et l'huile restent", world(6).all { it.oneWays.isNotEmpty() && it.oils.isNotEmpty() })
        assertTrue(world(6).all { it.portals.isEmpty() && it.gates.isEmpty() })
    }

    @Test
    fun theEighthWorldAddsTeleporters() {
        assertTrue("des téléporteurs à chaque niveau du monde 8", world(7).all { it.portals.isNotEmpty() })
        assertTrue("les tapis roulants restent", world(7).all { it.belts.isNotEmpty() && it.oneWays.isNotEmpty() })
        assertTrue(world(7).all { it.gates.isEmpty() })
    }

    @Test
    fun theNinthWorldAddsGatesAndKeepsEverything() {
        assertTrue("des barrières à chaque niveau du monde 9", world(8).all { it.gates.isNotEmpty() })
        assertTrue("tout le reste est toujours là", world(8).all { it.portals.isNotEmpty() && it.belts.isNotEmpty() && it.oneWays.isNotEmpty() && it.oils.isNotEmpty() && it.walls.isNotEmpty() && it.cones.isNotEmpty() })
    }

    @Test
    fun everyMechanicArrivesAtItsFirstLevelAndNotBefore() {
        assertTrue(all.filter { it.index < 101 }.none { it.oneWays.isNotEmpty() })
        assertTrue(all.filter { it.index < 121 }.none { it.belts.isNotEmpty() })
        assertTrue(all.filter { it.index < 141 }.none { it.portals.isNotEmpty() })
        assertTrue(all.filter { it.index < 161 }.none { it.gates.isNotEmpty() })
        // chaque mécanique commence avec un seul élément, puis il y en a de plus en plus
        assertEquals(1, all[100].oneWays.size)
        assertEquals(1, all[120].belts.size)
        assertEquals(1, all[140].portals.size)
        assertEquals(1, all[160].gates.size)
        assertTrue("de plus en plus de sens uniques : ${all[179].oneWays.size}", all[179].oneWays.size >= 5)
        assertTrue("de plus en plus de tapis : ${all[179].belts.size}", all[179].belts.size >= 4)
        assertTrue("de plus en plus de téléporteurs : ${all[179].portals.size}", all[179].portals.size >= 3)
        assertTrue("de plus en plus de barrières : ${all[179].gates.size}", all[179].gates.size >= 3)
        for (counts in listOf(all.map { it.oneWays.size }, all.map { it.belts.size }, all.map { it.portals.size }, all.map { it.gates.size })) {
            assertTrue("ça ne redescend jamais : $counts", counts.zipWithNext().all { (a, b) -> b >= a })
        }
    }

    @Test
    fun conesWallsCratesAndOilRampUpGradually() {
        // 1 au début de leur mécanique (1 ou 2 murs au début), puis de plus en plus
        assertEquals(1, all[20].cones.size)
        assertEquals(1, all[40].walls.size)
        assertEquals(1, all[60].cars.count { it.carrier })
        assertEquals(1, all[80].oils.size)
        assertTrue("1 ou 2 murs au début", all.subList(40, 55).all { it.walls.size <= 2 })
        assertTrue("de plus en plus de plots", all[99].cones.size >= 4 && all[99].cones.size > all[20].cones.size)
        assertTrue("de plus en plus de murs : ${all[99].walls.size}", all[99].walls.size >= 5 && all[99].walls.size > all[40].walls.size)
        assertTrue("de plus en plus de taches : ${all[99].oils.size}", all[99].oils.size >= 4 && all[99].oils.size > all[80].oils.size)
        assertTrue("jusqu'à 3 pick-up", all[99].cars.count { it.carrier } >= 3)
        for (counts in listOf(all.map { it.cones.size }, all.map { it.walls.size }, all.map { it.oils.size }, all.map { l -> l.cars.count { it.carrier } })) {
            assertTrue("ça ne redescend jamais : $counts", counts.zipWithNext().all { (a, b) -> b >= a })
        }
    }

    @Test
    fun theFirstLevelsOfEachMechanicAreSimpler() {
        // la nouveauté arrive avec moins de véhicules que la fin du monde d'avant, puis la difficulté remonte
        for (start in listOf(21, 41, 61, 81, 101, 121, 141, 161)) {
            val before = all[start - 2].cars.size // dernier niveau du monde précédent
            val first = all[start - 1].cars.size
            assertTrue("niveau $start : $first véhicules contre $before au niveau d'avant", first < before)
            assertTrue("la difficulté remonte après l'apprentissage", all[start + 9].cars.size > first)
        }
        assertEquals(3, all[0].cars.size)
        assertEquals(0.55, LevelGenerator.introFactor(21), 0.0)
        assertEquals(0.75, LevelGenerator.introFactor(24), 0.0)
        assertEquals(0.9, LevelGenerator.introFactor(27), 0.0)
        assertEquals(1.0, LevelGenerator.introFactor(30), 0.0)
    }

    @Test
    fun everyMechanicReallyMatters() {
        val coneLevels = all.filter { it.cones.isNotEmpty() }
        assertTrue("des plots qui coupent une voie dans presque tous les niveaux", coneLevels.count { analyses[it.index - 1].coneBlocks >= 1 } >= coneLevels.size * 0.9)
        val wallLevels = all.filter { it.walls.isNotEmpty() }
        assertTrue("des murs qui murent une sortie utile dans presque tous les niveaux", wallLevels.count { analyses[it.index - 1].wallBlocks >= 1 } >= wallLevels.size * 0.9)
        val oilLevels = all.filter { it.oils.isNotEmpty() }
        assertTrue("des taches sur lesquelles un véhicule tourne dans presque tous les niveaux", oilLevels.count { analyses[it.index - 1].oilTurns >= 1 } >= oilLevels.size * 0.9)
        val oneWayLevels = all.filter { it.oneWays.isNotEmpty() }
        assertTrue("des sens uniques qui coupent un sens dans presque tous les niveaux", oneWayLevels.count { analyses[it.index - 1].oneWayBlocks >= 1 } >= oneWayLevels.size * 0.9)
        val beltLevels = all.filter { it.belts.isNotEmpty() }
        assertTrue("des tapis qui poussent un véhicule dans presque tous les niveaux", beltLevels.count { analyses[it.index - 1].beltPushes >= 1 } >= beltLevels.size * 0.9)
        val portalLevels = all.filter { it.portals.isNotEmpty() }
        assertTrue("des téléporteurs qu'un véhicule emprunte dans presque tous les niveaux", portalLevels.count { analyses[it.index - 1].portalJumps >= 1 } >= portalLevels.size * 0.9)
        val gateLevels = all.filter { it.gates.isNotEmpty() }
        assertTrue("des barrières sur la route d'un véhicule dans presque tous les niveaux", gateLevels.count { analyses[it.index - 1].gateBlocks >= 1 } >= gateLevels.size * 0.9)
    }

    @Test
    fun cratesAreARealTrapInTheLastLevels() {
        val trapped = all.filter { it.index >= 70 }.count { analyses[it.index - 1].traps >= 1 }
        assertTrue("des niveaux piégés à la fin ($trapped)", trapped >= 6)
    }

    @Test
    fun busesAndHeavyTrucksAppearFromTheThirdWorldOnly() {
        assertTrue(all.take(40).none { level -> level.cars.any { it.len == 4 } })
        assertTrue(all.drop(40).count { level -> level.cars.any { it.len == 4 } } >= 40)
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
        assertTrue(avgDepth(71..100) > avgDepth(1..30) + 2)
        assertTrue(avgDepth(41..70) > avgDepth(1..30))
        assertTrue(all[99].cars.size > all[0].cars.size * 3)
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
        for (index in listOf(1, 7, 33, 64, 81, 100, 101, 141, 161, 400)) {
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
                for (oil in level.oils) append(";h").append(oil.col).append(',').append(oil.row)
                for (a in level.oneWays) append(";o").append(a.cell.col).append(',').append(a.cell.row).append(',').append(a.dir.ordinal)
                for (a in level.belts) append(";b").append(a.cell.col).append(',').append(a.cell.row).append(',').append(a.dir.ordinal)
                for (p in level.portals) append(";t").append(p.a.col).append(',').append(p.a.row).append(',').append(p.b.col).append(',').append(p.b.row)
                for (g in level.gates) append(";g").append(g.cell.col).append(',').append(g.cell.row).append(',').append(if (g.startsOpen) 1 else 0)
            }
            for (ch in text) {
                hash = hash xor ch.code.toLong()
                hash *= 0x100000001b3L
            }
        }
        return hash
    }

    @Test
    fun theFirstFourWorldsNeverChange() {
        val hash = digest(all.take(80))
        assertEquals("empreinte des niveaux 1 à 80 : ${java.lang.Long.toHexString(hash)}", EXPECTED_DIGEST_80, hash)
    }

    @Test
    fun theFirstHundredLevelsNeverChange() {
        // les niveaux publiés dans la version 0.5.0 : les étoiles des joueurs y sont attachées
        val hash = digest(all.take(100))
        assertEquals("empreinte des niveaux 1 à 100 : ${java.lang.Long.toHexString(hash)}", EXPECTED_DIGEST_100, hash)
    }

    @Test
    fun levelsNeverChange() {
        val hash = digest(all)
        assertEquals("empreinte des niveaux : ${java.lang.Long.toHexString(hash)}", EXPECTED_DIGEST, hash)
    }

    private companion object {
        val ALL: List<Level> by lazy { (1..Worlds.LEVEL_COUNT).map { LevelGenerator.level(it) } }
        val ANALYSES: List<Analysis> by lazy { ALL.map { Solver.analyse(it) } }

        // les 100 premiers niveaux sont ceux de la version 0.5.0 : ils ne doivent jamais changer
        val EXPECTED_DIGEST_80 = -5748582831935537625L
        val EXPECTED_DIGEST_100 = 8486057663845578176L
        val EXPECTED_DIGEST = 2960005120528354096L
    }
}
