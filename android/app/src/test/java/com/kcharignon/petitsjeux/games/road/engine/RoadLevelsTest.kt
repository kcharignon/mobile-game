package com.kcharignon.petitsjeux.games.road.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Les 48 niveaux : leur forme, leur déterminisme, et surtout la preuve (par le robot, puis en rejouant dans le vrai moteur) qu'on peut tous les traverser sans être touché. */
class RoadLevelsTest {
    private val all: List<RoadLevel> by lazy { (1..RoadWorlds.LEVEL_COUNT).map { RoadLevels.level(it) } }

    /** Ce qui identifie un niveau : lignes, circulation, graines. */
    private fun signature(l: RoadLevel): String = buildString {
        for (r in l.rows) {
            append(r.kind.name.first())
            when (val t = r.traffic) {
                is Stream -> append("[${t.direction},${"%.4f".format(t.speed)},${"%.4f".format(t.phase)},${t.slots.joinToString("/") { "${it.kind}:${it.color}:${"%.4f".format(it.gap)}" }}]")
                is TrainLine -> append("[${t.direction},${t.cycle},${t.warning},${"%.4f".format(t.offset)}]")
                null -> Unit
            }
            append(';')
        }
        append(l.grains.joinToString(",") { "${it.row}:${it.col}" })
    }

    @Test
    fun chunksSplitLanesInGroupsOfAtMostThree() {
        assertEquals(emptyList<Int>(), RoadLevels.chunks(0))
        assertEquals(listOf(1), RoadLevels.chunks(1))
        assertEquals(listOf(3), RoadLevels.chunks(3))
        assertEquals(listOf(2, 2), RoadLevels.chunks(4))
        assertEquals(listOf(3, 2), RoadLevels.chunks(5))
        assertEquals(listOf(3, 3), RoadLevels.chunks(6))
    }

    @Test
    fun worldsAndLaneCountsFollowTheFormula() {
        assertEquals(48, RoadWorlds.LEVEL_COUNT)
        assertEquals(2, RoadWorlds.lanes(1))
        assertEquals(2, RoadWorlds.lanes(3))
        assertEquals(3, RoadWorlds.lanes(4))
        assertEquals(3, RoadWorlds.lanes(7))
        assertEquals(10, RoadWorlds.lanes(48))
        var previous = 0
        for (n in 1..48) {
            assertTrue("les voies ne diminuent jamais ($n)", RoadWorlds.lanes(n) >= previous)
            previous = RoadWorlds.lanes(n)
        }
        assertEquals(0, RoadWorlds.of(1))
        assertEquals(0, RoadWorlds.of(6))
        assertEquals(1, RoadWorlds.of(7))
        assertEquals(7, RoadWorlds.of(48))
        assertEquals(8, RoadWorlds.names.size)
        assertEquals(8, RoadWorlds.news.size)
    }

    @Test
    fun theFirstLevelIsATwoWayStreet() {
        val l = all[0]
        assertEquals(2, l.lanes)
        val roads = l.rows.filter { it.kind == RowKind.ROAD }.map { it.traffic!!.direction }
        assertEquals("double sens : une voie dans chaque sens", listOf(+1, -1), roads)
        assertTrue("on commence doucement", l.rows.filter { it.kind == RowKind.ROAD }.all { it.traffic!!.speed < 2.2 })
        assertTrue(l.rows.none { it.kind == RowKind.RAIL })
    }

    @Test
    fun everyLevelHasTheRightShape() {
        for (l in all) {
            val n = l.number
            assertEquals("voies du niveau $n", RoadWorlds.lanes(n), l.lanes)
            assertEquals(RowKind.START, l.rows.first().kind)
            assertEquals(RowKind.FINISH, l.rows.last().kind)
            // jamais plus de 3 voies d'affilée sans refuge
            var run = 0
            for (r in l.rows) {
                run = if (r.kind == RowKind.ROAD) run + 1 else 0
                assertTrue("niveau $n : plus de 3 voies d'affilée", run <= 3)
            }
            // les voies du bas vont à droite, celles du haut à gauche
            val dirs = l.rows.filter { it.kind == RowKind.ROAD }.map { it.traffic!!.direction }
            assertEquals("niveau $n : un seul changement de sens", 1, dirs.zipWithNext().count { it.first != it.second })
            assertEquals(+1, dirs.first())
            assertEquals(-1, dirs.last())
            // un terre-plein là où le sens change, dès 4 voies
            if (l.lanes >= 4) {
                val lastGoingRight = l.rows.indexOfLast { it.kind == RowKind.ROAD && it.traffic!!.direction == +1 }
                assertEquals("niveau $n : refuge au milieu", RowKind.REFUGE, l.rows[lastGoingRight + 1].kind)
            }
            // les voies ferrées
            assertEquals("voies ferrées du niveau $n", RoadWorlds.rails(l.world), l.rows.count { it.kind == RowKind.RAIL })
            // une graine au moins, sur des voies, toutes à des cases différentes
            assertTrue("niveau $n : des graines", l.grains.isNotEmpty())
            assertEquals(l.grains.size, l.grains.map { it.row to it.col }.toSet().size)
        }
    }

    @Test
    fun levelsGetHarderWithTheNumber() {
        // la vitesse moyenne des voies monte (de ×1 à ×1,8 environ), et la fenêtre de passage minimale baisse
        fun meanSpeed(l: RoadLevel) = l.rows.mapNotNull { (it.traffic as? Stream)?.speed }.average()
        assertTrue(meanSpeed(all[47]) > meanSpeed(all[0]) * 1.3)
        assertTrue(meanSpeed(all[41]) > meanSpeed(all[5]))
        assertTrue("des véhicules plus gros dans les derniers mondes",
            all.takeLast(12).flatMap { l -> l.rows.mapNotNull { (it.traffic as? Stream)?.slots }.flatten() }.any { it.kind == VehicleKind.BUS || it.kind == VehicleKind.TRUCK })
        assertTrue("pas de camion au monde 1", all.take(6).flatMap { l -> l.rows.mapNotNull { (it.traffic as? Stream)?.slots }.flatten() }.none { it.kind == VehicleKind.TRUCK || it.kind == VehicleKind.BUS })
    }

    @Test
    fun generationIsDeterministic() {
        for (n in listOf(1, 7, 25, 33, 48)) assertEquals(signature(RoadLevels.generate(n)), signature(RoadLevels.generate(n)))
        val signatures = all.map { signature(it) }
        assertEquals("tous les niveaux sont différents", signatures.size, signatures.toSet().size)
    }

    @Test
    fun everyLevelCanBeCrossedWithoutBeingHitAndTheRobotsPathReplaysInTheRealEngine() {
        for (l in all) {
            val planner = Planner(l)
            assertTrue("niveau ${l.number} : faisable avec marge", planner.feasible())
            val moves = planner.route(l.grains)
            assertNotNull("niveau ${l.number} : chemin passant par toutes les graines", moves)
            // on rejoue le chemin du robot dans le vrai moteur, avec l'horloge : aucune touche, toutes les graines, l'arrivée, le score maximum
            val c = Crossing(l)
            val events = ArrayList<CrossingEvent>()
            for (m in moves!!) events += c.hop(m.dir, m.tick.toLong() * Planner.TICK_MS)
            events += c.update(moves.last().tick.toLong() * Planner.TICK_MS + 1000)
            assertEquals("niveau ${l.number} : aucune touche", 0, c.hits)
            assertEquals("niveau ${l.number} : arrivé", ChickState.DONE, c.state)
            assertEquals("niveau ${l.number} : toutes les graines", l.grains.size, c.taken.size)
            assertEquals("niveau ${l.number} : score maximum", l.maxScore, c.score)
            assertEquals(3, l.stars(c.score))
            assertEquals(1, events.count { it is CrossingEvent.Arrived })
        }
    }

    @Test
    fun theRobotsPathIsNotTooSlow() {
        // une traversée parfaite dure moins d'une minute et demie, même au dernier niveau (22 lignes environ)
        for (l in listOf(all[0], all[23], all[47])) {
            val moves = Planner(l).route(l.grains)!!
            assertTrue("niveau ${l.number} : ${moves.last().tick} ticks", moves.last().tick < 450)
        }
    }

    @Test
    fun theWorldsBringTheirNewVehiclesInOrder() {
        fun kinds(world: Int): Set<VehicleKind> = (RoadWorlds.firstLevel(world) until RoadWorlds.firstLevel(world) + 6)
            .flatMap { n -> all[n - 1].rows.mapNotNull { (it.traffic as? Stream)?.slots }.flatten().map { it.kind } }.toSet()
        assertTrue(kinds(0).all { it == VehicleKind.CAR || it == VehicleKind.SCOOTER })
        assertTrue(kinds(1).all { it in setOf(VehicleKind.CAR, VehicleKind.SCOOTER, VehicleKind.VAN, VehicleKind.TRACTOR) })
        assertTrue(kinds(1).any { it == VehicleKind.VAN || it == VehicleKind.TRACTOR })
        val late = (3..7).flatMap { kinds(it) }.toSet()
        assertTrue(VehicleKind.AMBULANCE in late)
        assertTrue(VehicleKind.SPORT in late)
        assertTrue(VehicleKind.TAXI in late)
    }
}
