package com.kcharignon.petitsjeux.games.road.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Les véhicules (fonctions pures du temps) et la traversée du poussin : sauts, points, écrasements (qui ne font jamais perdre la partie). */
class CrossingTest {
    private val refuge = Row(RowKind.REFUGE)

    /** Une route où rien ne passe jamais pendant les tests (un seul véhicule à 50 cases, quasi immobile). */
    private fun emptyRoad() = Row(RowKind.ROAD, Stream(+1, 0.01, listOf(Slot(VehicleKind.CAR, 0, 100.0)), phase = 50.0))

    /** Une route où un véhicule est toujours sur le poussin : touche garantie. */
    private fun jam() = Row(RowKind.ROAD, Stream(+1, 1.0, listOf(Slot(VehicleKind.TRUCK, 0, 0.2)), phase = 0.0))

    private fun level(vararg rows: Row, grains: List<Grain> = emptyList()) = RoadLevel(1, listOf(Row(RowKind.START)) + rows.toList() + Row(RowKind.FINISH), grains)

    // ---------------------------------------------------------------- les véhicules

    @Test
    fun aStreamIsAPureFunctionOfTime() {
        val s = Stream(+1, 2.0, listOf(Slot(VehicleKind.CAR, 1, 8.0)), phase = 0.0) // période 10
        assertEquals(10.0, s.period, 1e-9)
        assertEquals(VehicleKind.CAR, s.hit(0.0, 1.0, 1.4))
        assertNull(s.hit(0.0, 2.5, 3.0))
        // une seconde plus tard, la voiture a avancé de 2 cases
        assertNull(s.hit(1.0, 1.0, 1.4))
        assertEquals(VehicleKind.CAR, s.hit(1.0, 2.5, 3.0))
        // et au bout d'une période elle est revenue au même endroit
        assertEquals(VehicleKind.CAR, s.hit(5.0, 1.0, 1.4))
    }

    @Test
    fun aStreamGoingLeftMovesLeft() {
        val s = Stream(-1, 2.0, listOf(Slot(VehicleKind.CAR, 1, 8.0)), phase = 0.0)
        assertNull(s.hit(1.0, 2.5, 3.0))
        assertEquals(VehicleKind.CAR, s.hit(1.0, 8.5, 9.0)) // 0 - 2 = -2 -> 8 (modulo 10)
    }

    @Test
    fun vehiclesLeaveOnOneSideAndComeBackOnTheOtherWithoutJumping() {
        val s = Stream(+1, 2.0, listOf(Slot(VehicleKind.CAR, 1, 8.0)), phase = 9.0) // bord gauche à 9 : dépasse à droite, revient à gauche
        assertEquals(VehicleKind.CAR, s.hit(0.0, 9.5, 9.8))
        assertEquals("la copie qui entre par la gauche", VehicleKind.CAR, s.hit(0.0, 0.0, 0.5))
        val bodies = s.bodies(0.0, 0.0, 14.0)
        assertEquals(2, bodies.size)
        assertTrue(bodies.any { it.left < 0.0 && it.left + it.length > 0.0 })
    }

    @Test
    fun collisionsAreLenientAlmostTouchingPasses() {
        val s = Stream(+1, 1.0, listOf(Slot(VehicleKind.CAR, 1, 8.0)), phase = 0.0) // la voiture occupe 0 à 2 (dessin) et 0,08 à 1,92 (collision)
        assertEquals("ça se touche vraiment", VehicleKind.CAR, s.hit(0.0, 1.8, 2.3))
        assertNull("ça se touche presque : ça passe", s.hit(0.0, 1.95, 2.4))
        assertNull(s.hit(0.0, -0.5, 0.05))
    }

    @Test
    fun theTrainWarnsThenCrossesThenLeaves() {
        val t = TrainLine(+1, cycle = 10.0, warning = 2.5, speed = 12.0, length = 14.0, offset = 0.0)
        assertTrue(t.lightsOn(0.1))
        assertNull("pas de train pendant l'avertissement", t.hit(2.0, 0.0, 14.0))
        // 0,5 s après l'avertissement : la tête du train est à -14 + 6 = -8, le train couvre -8 à 6
        assertEquals(VehicleKind.TRAIN, t.hit(3.0, 2.0, 2.4))
        assertNull(t.hit(3.0, 7.0, 7.5))
        assertEquals(1, t.bodies(3.0, 0.0, 14.0).size)
        assertTrue("les feux restent allumés tant qu'il passe", t.lightsOn(4.5))
        assertFalse(t.lightsOn(6.0))
        assertNull(t.hit(6.0, 0.0, 14.0))
        assertTrue("le cycle recommence", t.lightsOn(10.1))
    }

    @Test
    fun aTrainGoingLeftEntersByTheRight() {
        val t = TrainLine(-1, cycle = 10.0, warning = 2.0, speed = 12.0, length = 14.0, offset = 0.0)
        assertEquals(VehicleKind.TRAIN, t.hit(2.5, 12.0, 12.4)) // gauche du train à 14 - 6 = 8, il couvre 8 à 22
        assertNull(t.hit(2.5, 3.0, 4.0))
    }

    // ---------------------------------------------------------------- la traversée

    @Test
    fun hoppingForwardScoresEachNewLineAndTheArrival() {
        val c = Crossing(level(refuge))
        val events = c.hop(HopDir.FORWARD, 0)
        assertEquals(listOf(CrossingEvent.Hopped(HopDir.FORWARD)), events)
        assertTrue("en vol", c.isHopping)
        assertEquals(0, c.score)
        assertEquals(listOf(CrossingEvent.NewRow(1, 10)), c.update(200))
        assertEquals(10, c.score)
        val end = c.hop(HopDir.FORWARD, 200)
        assertTrue(end.contains(CrossingEvent.Hopped(HopDir.FORWARD)))
        val arrival = c.update(400)
        assertTrue(arrival.contains(CrossingEvent.Arrived(50)))
        assertEquals(ChickState.DONE, c.state)
        assertEquals(10 + 10 + 50, c.score)
        assertEquals("une traversée sans touche ni graine : 3 étoiles ici", 3, c.level.stars(c.score))
        // une fois arrivé, plus rien ne bouge
        assertTrue(c.hop(HopDir.BACK, 500).isEmpty())
        assertEquals(2, c.row)
    }

    @Test
    fun aTapDuringAHopWaitsForTheLandingThenHops() {
        val c = Crossing(level(refuge, refuge))
        c.hop(HopDir.FORWARD, 0)
        c.hop(HopDir.FORWARD, 50) // pendant le saut : mis en attente
        assertEquals("pas de saut en double", 0, c.row)
        c.update(200)
        assertEquals(1, c.row)
        assertTrue("le saut en attente est parti à l'atterrissage", c.isHopping)
        c.update(400)
        assertEquals(2, c.row)
    }

    @Test
    fun theEdgesOfTheWorldAreWalls() {
        val c = Crossing(level(refuge))
        assertTrue("pas de ligne derrière le départ", c.hop(HopDir.BACK, 0).isEmpty())
        var now = 0L
        repeat(10) { c.hop(HopDir.LEFT, now); now += 200 }
        c.update(now + 200)
        assertEquals(0, c.col)
        repeat(20) { c.hop(HopDir.RIGHT, now); now += 200 }
        c.update(now + 200)
        assertEquals(Road.COLS - 1, c.col)
    }

    @Test
    fun goingBackAndForthScoresEachLineOnlyOnce() {
        val c = Crossing(level(refuge, refuge))
        c.hop(HopDir.FORWARD, 0)
        c.hop(HopDir.BACK, 200)
        c.hop(HopDir.FORWARD, 400)
        c.update(600)
        assertEquals(10, c.score)
        assertEquals(1, c.furthest)
    }

    @Test
    fun grainsAreTakenOnLandingAndOnlyOnce() {
        val c = Crossing(level(emptyRoad(), refuge, grains = listOf(Grain(0, 1, Road.START_COL))))
        val events = c.hop(HopDir.FORWARD, 0) + c.update(200)
        assertTrue(events.any { it is CrossingEvent.GrainTaken && it.taken == 1 })
        assertEquals(20, c.score)
        c.hop(HopDir.BACK, 200)
        c.hop(HopDir.FORWARD, 400)
        c.update(700)
        assertEquals("pas deux fois la même graine", 20, c.score)
        assertEquals(setOf(0), c.taken)
    }

    @Test
    fun beingRunOverCostsPointsAndSendsTheChickBackToTheLastRefugeButNeverEndsTheGame() {
        val c = Crossing(level(refuge, refuge, refuge, jam()))
        var now = 0L
        repeat(3) { c.hop(HopDir.FORWARD, now); now += 200 }
        c.update(now)
        assertEquals(30, c.score)
        assertEquals(3, c.checkpoint)
        // dans la route : touché en plein saut
        val events = c.hop(HopDir.FORWARD, now) + c.update(now + 300)
        val hit = events.filterIsInstance<CrossingEvent.Hit>().single()
        assertEquals(15, hit.lost)
        assertEquals(VehicleKind.TRUCK, hit.by)
        assertEquals(15, c.score)
        assertEquals(1, c.hits)
        assertEquals(ChickState.SQUASHED, c.state)
        // aplati : les sauts sont ignorés
        assertTrue(c.hop(HopDir.FORWARD, now + 400).isEmpty())
        // 900 ms après : retour au refuge, clignotant, et la partie continue
        val back = c.update(now + 100 + Road.SQUASH_MS + 40)
        assertTrue(back.contains(CrossingEvent.Respawned))
        assertEquals(ChickState.ALIVE, c.state)
        assertEquals(3, c.row)
        assertTrue(c.invulnerable(now + 100 + Road.SQUASH_MS + 40))
        assertEquals("rien de perdu à part les points", 15, c.score)
    }

    @Test
    fun theScoreNeverGoesBelowZero() {
        val c = Crossing(level(jam()))
        c.hop(HopDir.FORWARD, 0)
        val events = c.update(300)
        assertEquals(0, events.filterIsInstance<CrossingEvent.Hit>().single().lost)
        assertEquals(0, c.score)
    }

    @Test
    fun afterBeingRunOverTheChickCannotBeHitForAWhileThenCan() {
        val c = Crossing(level(refuge, jam()))
        c.hop(HopDir.FORWARD, 0)
        c.hop(HopDir.FORWARD, 200)
        c.update(500)
        assertEquals(1, c.hits)
        val respawn = 200 + 100 + Road.SQUASH_MS // l'instant du retour (environ)
        c.update(respawn + 100)
        assertEquals(ChickState.ALIVE, c.state)
        val now = respawn + 100
        c.hop(HopDir.FORWARD, now) // retour dans la route pendant l'invulnérabilité
        c.update(now + 600)
        assertEquals("invulnérable : pas de nouvelle touche", 1, c.hits)
        c.update(now + Road.INVULNERABLE_MS + 400)
        assertEquals("plus invulnérable : touché", 2, c.hits)
    }

    @Test
    fun theChickIsCountedInTheStartingLineForTheFirstHalfOfTheHopThenInTheNextOne() {
        // la ligne 1 est un embouteillage, la ligne 0 est sûre : touché seulement à partir de la mi-saut
        val c = Crossing(level(jam()))
        c.hop(HopDir.FORWARD, 0)
        assertTrue(c.update(80).none { it is CrossingEvent.Hit })
        assertTrue(c.update(140).any { it is CrossingEvent.Hit })
    }

    @Test
    fun theClockOnlyMovesForward() {
        val c = Crossing(level(refuge))
        c.hop(HopDir.FORWARD, 100)
        assertTrue(c.update(50).isEmpty())
        assertEquals(1, c.update(300).count { it is CrossingEvent.NewRow })
    }

    @Test
    fun poseFollowsTheHop() {
        val c = Crossing(level(refuge))
        assertEquals(ChickPose(Road.START_COL + 0.5, 0.0, -1.0), c.pose(0))
        c.hop(HopDir.FORWARD, 0)
        val mid = c.pose(100)
        assertEquals(0.5, mid.row, 1e-9)
        assertEquals(0.5, mid.hop, 1e-9)
        c.hop(HopDir.RIGHT, 100)
        assertNotNull(c.pose(150))
    }

    @Test
    fun starsComeFromThePointsAndAreAlwaysAtLeastOne() {
        val l = level(emptyRoad(), emptyRoad(), grains = listOf(Grain(0, 1, 3), Grain(1, 2, 4)))
        assertEquals(10 * 3 + 20 + 50, l.maxScore)
        assertEquals(3, l.stars(l.maxScore))
        assertEquals(3, l.stars(l.maxScore * 8 / 10))
        assertEquals(2, l.stars(l.maxScore * 8 / 10 - 1))
        assertEquals(2, l.stars(l.maxScore / 2))
        assertEquals(1, l.stars(l.maxScore / 2 - 1))
        assertEquals(1, l.stars(0))
    }
}
