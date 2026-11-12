package com.kcharignon.petitsjeux.games.plane.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.random.Random

/** Les règles de Super Avion : un robot qui suit le couloir ne heurte jamais rien, un avion immobile ou fou finit toujours, le choc et le tonneau se vivent comme prévu, l'atterrissage aussi. */
class PlaneRunTest {
    private val dt = 1f / 60f

    /** Un ciel vide, sauf [pieces] ; sa longueur est [length]. */
    private fun sky(vararg pieces: Piece, length: Float = 300f, difficulty: PlaneDifficulty = PlaneDifficulty.EASY) =
        PlaneWorld(difficulty, 1, pieces.sortedBy { it.z }, listOf(PathPoint(0f, 0f)), 0, length)

    private fun playToTheEnd(run: PlaneRun, events: MutableList<PlaneEvent> = ArrayList()): PlaneRun {
        var guard = 0
        while (!run.done && guard++ < 200_000) events += run.update(dt)
        assertTrue("le vol finit", run.done)
        return run
    }

    /** Fait voler le robot du couloir jusqu'au bout : il vise le milieu du couloir 0,1 s devant lui (l'avion suit avec un léger retard). */
    private fun flyWithTheCorridor(world: PlaneWorld, lead: Float = 0.1f): PlaneRun {
        val run = PlaneRun(world)
        var guard = 0
        while (!run.done && guard++ < 60 * 400) {
            run.steerTo(world.pathAt(run.dist + lead * run.speed))
            run.update(dt)
        }
        assertTrue("le vol finit (${world.difficulty}, graine ${world.seed})", run.done)
        return run
    }

    @Test
    fun aRobotFollowingTheCorridorNeverHitsAnythingInAnyFlight() {
        for (d in PlaneDifficulty.entries) {
            for (seed in 0L until 120L) {
                val run = flyWithTheCorridor(PlaneWorld.generate(d, seed))
                assertEquals("choc en ${d.title}, graine $seed", 0, run.hits)
                assertEquals(3, run.stars)
                assertTrue(run.finished)
            }
        }
    }

    @Test
    fun theRobotAlsoWinsWhenItReactsSlowlyOrEarly() {
        // un humain suit le couloir avec du retard : le plafond de vitesse de côté de l'avion (80 %) et une avance de 0 ou 0,1 s suffisent
        // (se décaler trop tôt vers la voie suivante, avant d'avoir dépassé la rangée, fait frôler les obstacles de la voie voisine : on attend d'avoir passé la rangée)
        for (d in PlaneDifficulty.entries) {
            for (seed in 0L until 40L) {
                for (lead in listOf(0f, 0.1f)) {
                    val world = PlaneWorld.generate(d, seed)
                    val run = PlaneRun(world)
                    var cursor = 0f
                    var guard = 0
                    while (!run.done && guard++ < 60 * 400) {
                        val goal = world.pathAt(run.dist + lead * run.speed)
                        cursor += (goal - cursor).coerceIn(-0.8f * PlaneRun.LATERAL_MAX * dt, 0.8f * PlaneRun.LATERAL_MAX * dt)
                        run.steerTo(cursor)
                        run.update(dt)
                    }
                    assertEquals("choc en ${d.title}, graine $seed, avance $lead", 0, run.hits)
                }
            }
        }
    }

    @Test
    fun aMotionlessPlaneAlwaysLandsWithAtLeastOneStar() {
        for (d in PlaneDifficulty.entries) {
            for (seed in 0L until 30L) {
                val run = playToTheEnd(PlaneRun(PlaneWorld.generate(d, seed)))
                assertTrue(run.stars in 1..3)
                assertTrue("des chocs, mais jamais de game over", run.lives >= 0)
                assertTrue(run.finished)
            }
        }
    }

    @Test
    fun aFlailingPlaneAlwaysLandsToo() {
        for (d in PlaneDifficulty.entries) {
            val random = Random(d.index)
            for (seed in 0L until 20L) {
                val run = PlaneRun(PlaneWorld.generate(d, seed))
                var guard = 0
                while (!run.done && guard++ < 60 * 600) {
                    if (guard % 12 == 0) run.steerTo(random.nextFloat() * 2f - 1f)
                    run.update(dt)
                }
                assertTrue(run.done)
                assertTrue(run.stars in 1..3)
            }
        }
    }

    @Test
    fun theResultDoesNotDependOnTheFrameRate() {
        val world = PlaneWorld.generate(PlaneDifficulty.HARD, 7)
        fun play(frame: Float): PlaneRun {
            val run = PlaneRun(world)
            var guard = 0
            while (!run.done && guard++ < 100_000) {
                run.steerTo(world.pathAt(run.dist + 0.1f * run.speed))
                run.update(frame)
            }
            return run
        }
        val slow = play(1f / 20f)
        val fast = play(1f / 120f)
        assertEquals(fast.hits, slow.hits)
        assertEquals(fast.score, slow.score)
        assertTrue(abs(fast.time - slow.time) < 0.2f)
    }

    @Test
    fun aHitCostsAStarSlowsDownThenSpeedsUp() {
        val balloon = Piece(0, PieceKind.BALLOON, 60f, 0, 0)
        val run = PlaneRun(sky(balloon))
        var hit: PlaneEvent.Hit? = null
        var before = 0f
        var guard = 0
        while (hit == null && guard++ < 10_000) {
            before = run.speed
            hit = run.update(dt).filterIsInstance<PlaneEvent.Hit>().firstOrNull()
        }
        assertTrue("le choc a lieu", hit != null)
        assertEquals(2, run.lives)
        assertEquals(1, run.hits)
        assertTrue("la vitesse tombe d'un coup (${run.speed} contre $before)", run.speed < before * 0.45f)
        assertTrue(run.hurt)
        assertTrue(balloon.id in run.knocked)
        val slowest = run.speed
        // elle reste basse un instant puis remonte
        var t = 0f
        while (t < 0.2f) { run.update(dt); t += dt }
        assertTrue(abs(run.speed - slowest) < 0.05f)
        t = 0f
        while (t < 2.5f) { run.update(dt); t += dt }
        assertTrue("elle est remontée (${run.speed})", run.speed > slowest * 2.2f)
        assertFalse("plus protégé", run.hurt)
    }

    @Test
    fun aHitObstacleIsHitOnlyOnceAndEarnsNoPoints() {
        val run = playToTheEnd(PlaneRun(sky(Piece(0, PieceKind.BIRDS, 50f, 0, 0))))
        assertEquals(1, run.hits)
        assertEquals(2, run.lives)
        assertEquals("un obstacle heurté ne rapporte pas de points", 0, run.score)
    }

    @Test
    fun anObstacleDodgedEarnsPointsAndOnlyTheOwnLaneHurts() {
        // l'avion est dans la voie du milieu : les obstacles des voies voisines et des voies extérieures ne le touchent pas
        val pieces = (-2..2).filter { it != 0 }.mapIndexed { i, lane -> Piece(i, PieceKind.PLANE, 60f, lane, 0) }.toTypedArray()
        val run = playToTheEnd(PlaneRun(sky(*pieces)))
        assertEquals(0, run.hits)
        assertEquals(4 * PlaneRun.PASS_POINTS, run.score)
    }

    @Test
    fun aCrossingPlaneOnlyHurtsInTheLaneItEndsIn() {
        // l'avion arrive de quatre voies plus loin et se pose dans la voie 1 (x = 0,4) ; il traverse la voie du milieu pendant qu'il est encore loin, sans toucher personne
        val hunter = Piece(0, PieceKind.PLANE, 100f, 1, 0, drift = 4, span = 60f)
        val stay = PlaneRun(sky(hunter, length = 330f))
        stay.steerTo(0f)
        playToTheEnd(stay)
        assertEquals("au milieu, l'avion traversant ne touche pas", 0, stay.hits)

        val wait = PlaneRun(sky(Piece(0, PieceKind.PLANE, 100f, 1, 0, drift = 4, span = 60f), length = 330f))
        wait.steerTo(0.4f)
        playToTheEnd(wait)
        assertEquals("dans la voie d'arrivée, on le heurte", 1, wait.hits)
    }

    @Test
    fun aCrossingPlaneHurtsWhereItIsNotWhereItCameFrom() {
        // il vient de la droite (voie 2) et se pose dans la voie -2 : celui qui l'attend là d'où il vient (à droite) n'est pas touché, celui qui l'attend là où il va l'est
        fun fly(at: Float): PlaneRun {
            val run = PlaneRun(sky(Piece(0, PieceKind.PLANE, 100f, -2, 0, drift = -4, span = 60f), length = 330f))
            run.steerTo(at)
            return playToTheEnd(run)
        }
        assertEquals(PlaneWorld.LANE_WIDTH * 2, Piece(0, PieceKind.PLANE, 100f, -2, 0, drift = -4, span = 60f).xAt(60f), 1e-5f)
        assertEquals(0, fly(0.8f).hits)
        assertEquals(1, fly(-0.8f).hits)
    }

    @Test
    fun anAirplaneCannotSqueezeBetweenTwoFullLanes() {
        // entre deux voies pleines (au milieu, à 0,2 de chaque obstacle) il y a trop peu de place : on heurte
        val run = PlaneRun(sky(Piece(0, PieceKind.BIRDS, 60f, -1, 0), Piece(1, PieceKind.BIRDS, 60f, 0, 0)))
        run.steerTo(-0.2f)
        playToTheEnd(run)
        assertTrue(run.hits >= 1)
    }

    @Test
    fun hurtProtectionPreventsAChainOfHits() {
        val run = playToTheEnd(PlaneRun(sky(Piece(0, PieceKind.BALLOON, 50f, 0, 0), Piece(1, PieceKind.BALLOON, 50.9f, 0, 0), Piece(2, PieceKind.BALLOON, 51.8f, 0, 0))))
        assertEquals(1, run.hits)
    }

    @Test
    fun livesNeverGoBelowZeroAndTheFlightKeepsOneStar() {
        val run = playToTheEnd(PlaneRun(sky(*Array(8) { Piece(it, PieceKind.PLANE, 50f + it * 20f, 0, 0) }, length = 330f)))
        assertTrue(run.hits >= 6)
        assertEquals(0, run.lives)
        assertEquals(1, run.stars)
    }

    @Test
    fun aRingMakesTheAirplaneRollAndGivesAStarBack() {
        // une montgolfière qui coûte une étoile ; puis un anneau devant l'avion, qui la rend ; puis des oiseaux juste après l'anneau : pendant le tonneau on passe au travers
        val run = PlaneRun(sky(Piece(0, PieceKind.BALLOON, 46f, 0, 0), Piece(1, PieceKind.RING, 120f, 0, 0), Piece(2, PieceKind.BIRDS, 124f, 0, 0), length = 330f))
        val events = ArrayList<PlaneEvent>()
        var guard = 0
        var rolling = false
        while (!run.done && guard++ < 100_000) {
            events += run.update(dt)
            rolling = rolling || run.rolling
        }
        assertEquals(1, run.hits)
        assertEquals(1, events.count { it is PlaneEvent.Roll })
        assertTrue("une étoile rendue", (events.first { it is PlaneEvent.Roll } as PlaneEvent.Roll).starBack)
        assertEquals(3, run.lives)
        assertEquals(1, run.rolls)
        assertTrue("il a fait un tonneau", rolling)
        assertTrue("les points de l'anneau", run.score >= PlaneRun.RING_POINTS)
    }

    @Test
    fun theRollTurnsAFullCircleInOneSecond() {
        val run = PlaneRun(sky(Piece(0, PieceKind.RING, 50f, 0, 0)))
        var guard = 0
        while (!run.rolling && guard++ < 10_000) run.update(dt)
        assertTrue(run.rolling)
        val seen = ArrayList<Float>()
        while (run.rolling && guard++ < 20_000) {
            run.update(dt)
            seen += run.rollProgress
        }
        assertEquals("un tour dure ${PlaneRun.ROLL_SECONDS} s", PlaneRun.ROLL_SECONDS * 60f, seen.size.toFloat(), 3f)
        assertTrue(seen.zipWithNext().all { (a, b) -> b >= a || b == 0f })
        assertEquals(0f, run.rollProgress, 0.0001f)
    }

    @Test
    fun anAirplaneThatMissesTheRingDoesNothing() {
        val run = PlaneRun(sky(Piece(0, PieceKind.RING, 50f, 2, 0)))
        playToTheEnd(run)
        assertEquals(0, run.rolls)
        assertEquals(3, run.lives)
    }

    @Test
    fun theRingIsGenerousAroundItsCentre() {
        // à 0,2 du centre (à peine plus de la moitié d'une voie) on passe encore dedans ; à 0,3 non
        for ((off, expected) in listOf(0.2f to 1, 0.3f to 0)) {
            val run = PlaneRun(sky(Piece(0, PieceKind.RING, 50f, 0, 0)))
            run.steerTo(off)
            playToTheEnd(run)
            assertEquals("décalage $off", expected, run.rolls)
        }
    }

    @Test
    fun theStarsNeverGoAboveThree() {
        val run = playToTheEnd(PlaneRun(sky(Piece(0, PieceKind.RING, 50f, 0, 0))))
        assertEquals(3, run.lives)
        assertEquals(1, run.rolls)
    }

    @Test
    fun obstaclesAreFlownThroughDuringARoll() {
        val run = playToTheEnd(PlaneRun(sky(Piece(0, PieceKind.RING, 50f, 0, 0), Piece(1, PieceKind.BIRDS, 54f, 0, 0), Piece(2, PieceKind.PLANE, 57f, 0, 0))))
        assertEquals("on a traversé les oiseaux et l'avion en tonneau", 0, run.hits)
        assertNotEquals(0, run.rolls)
    }

    @Test
    fun theAirplaneLandsOnTheRunwayAndStops() {
        val run = PlaneRun(PlaneWorld.generate(PlaneDifficulty.EASY, 3))
        val events = ArrayList<PlaneEvent>()
        var guard = 0
        while (!run.done && guard++ < 100_000) {
            run.steerTo(run.world.pathAt(run.dist + 1f))
            events += run.update(dt)
        }
        assertEquals(1, events.count { it === PlaneEvent.Finish })
        assertEquals(1, events.count { it === PlaneEvent.Done })
        assertTrue(events.indexOf(PlaneEvent.Finish) < events.indexOf(PlaneEvent.Done))
        assertTrue(run.dist > run.world.length)
        assertEquals(0f, run.speed, 0.001f)
        assertTrue("l'arrêt se fait en quelques mètres (${run.dist - run.world.length})", run.dist - run.world.length < 60f)
    }

    @Test
    fun theApproachDescendsSmoothlyToTheRunwayWithNoVerticalSpeedAtTouchdown() {
        val run = PlaneRun(sky(length = 400f))
        val altitudes = ArrayList<Float>()
        var guard = 0
        while (!run.finished && guard++ < 100_000) {
            run.update(dt)
            altitudes += run.altitude
        }
        assertEquals(1f, altitudes.first(), 0.0001f)
        assertEquals(0f, run.altitude, 0.0001f)
        // elle ne remonte jamais et ne baisse qu'à l'approche
        assertTrue(altitudes.zipWithNext().all { (a, b) -> b <= a + 1e-6f })
        val approachStart = altitudes.indexOfFirst { it < 0.9999f }
        assertTrue(approachStart > 0)
        // les derniers instants : presque plus de variation (descente douce, comme une assiette)
        val last = altitudes.takeLast(4)
        assertTrue("fin de descente douce : $last", last.first() - last.last() < 0.003f)
    }

    @Test
    fun theAutopilotCentresTheAirplaneOnTheRunwayAndIgnoresTheFinger() {
        val run = PlaneRun(sky(length = 400f))
        run.steerTo(0.7f)
        var guard = 0
        while (!run.autopilot && guard++ < 100_000) run.update(dt)
        assertTrue(run.autopilot)
        assertTrue(abs(run.x) > 0.3f)
        for (i in 0 until 30) run.update(dt)
        val before = run.targetX
        assertTrue("le pilote automatique ramène vers la piste ($before)", before < 0.7f)
        run.steer(0.5f)
        assertEquals("le doigt ne change plus rien", before, run.targetX, 0.0001f)
        while (!run.finished && guard++ < 200_000) run.update(dt)
        assertTrue("centré sur la piste au toucher (${run.x})", abs(run.x) < 0.05f)
    }

    @Test
    fun aCleanFlightLastsAboutTheAnnouncedTime() {
        for (d in PlaneDifficulty.entries) {
            val run = flyWithTheCorridor(PlaneWorld.generate(d, 11))
            // + le démarrage depuis l'arrêt et le roulage final
            assertTrue("${d.title} : ${run.time} s pour ${d.seconds} s annoncées", run.time in d.seconds - 3f..d.seconds + 9f)
        }
    }

    @Test
    fun steeringFollowsTheFingerOneToOneWithoutLeavingTheSky() {
        val run = PlaneRun(sky())
        run.steer(0.3f)
        run.steer(0.2f)
        assertEquals(0.5f, run.targetX, 0.0001f)
        for (i in 0 until 60) run.update(dt)
        assertEquals(0.5f, run.x, 0.01f)
        run.steer(5f)
        assertEquals(PlaneRun.MAX_X, run.targetX, 0.0001f)
        run.steer(-9f)
        assertEquals(-PlaneRun.MAX_X, run.targetX, 0.0001f)
        // jamais plus vite que LATERAL_MAX
        val start = run.x
        run.update(dt)
        assertTrue(abs(run.x - start) <= PlaneRun.LATERAL_MAX * dt + 1e-4f)
    }

    @Test
    fun theFingerIsPreciseAndNotSnappedToLanes() {
        // le contrôle reste libre : un dixième d'unité de glissé donne un dixième d'unité de déplacement, entre deux voies aussi
        val run = PlaneRun(sky())
        run.steer(0.13f)
        for (i in 0 until 60) run.update(dt)
        assertEquals(0.13f, run.x, 0.005f)
        assertNotEquals(0f, run.x % PlaneWorld.LANE_WIDTH)
    }
}
