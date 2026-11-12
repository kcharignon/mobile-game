package com.kcharignon.petitsjeux.games.skate.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.random.Random

/** Les règles de Super Skate : un robot qui suit le couloir ne heurte jamais rien, un skateur immobile ou fou finit toujours, et le choc se vit comme prévu. */
class SkateRunTest {
    private val dt = 1f / 60f

    /** Une rue sans rien, sauf [pieces]. */
    private fun street(vararg pieces: Piece, difficulty: SkateDifficulty = SkateDifficulty.EASY) =
        SkateWorld(difficulty, 1, pieces.sortedBy { it.z }, listOf(PathPoint(0f, 0f)), 0)

    private fun playToTheEnd(run: SkateRun, events: MutableList<SkateEvent> = ArrayList()): SkateRun {
        var guard = 0
        while (!run.done && guard++ < 200_000) events += run.update(dt)
        assertTrue("la course finit", run.done)
        return run
    }

    /** Fait rouler le robot du couloir jusqu'au bout : il vise le milieu du couloir 0,1 s devant lui (le skateur suit avec un léger retard). */
    private fun driveWithTheCorridor(world: SkateWorld): SkateRun {
        val run = SkateRun(world)
        var guard = 0
        while (!run.done && guard++ < 60 * 400) {
            run.steerTo(world.pathAt(run.dist + 0.1f * run.speed))
            run.update(dt)
        }
        assertTrue("la course finit (${world.difficulty}, graine ${world.seed})", run.done)
        return run
    }

    @Test
    fun aRobotFollowingTheCorridorNeverHitsAnythingInAnyRun() {
        for (d in SkateDifficulty.entries) {
            for (seed in 0L until 120L) {
                val run = driveWithTheCorridor(SkateWorld.generate(d, seed))
                assertEquals("choc en ${d.title}, graine $seed", 0, run.hits)
                assertEquals(3, run.stars)
                assertTrue(run.finished)
            }
        }
    }

    @Test
    fun theRobotAlsoWinsWhenItReactsSlowly() {
        // un skateur humain suit le couloir avec du retard : on ne lui demande que de rattraper à 80 % de la vitesse de côté permise
        for (d in SkateDifficulty.entries) {
            for (seed in 0L until 40L) {
                val world = SkateWorld.generate(d, seed)
                val run = SkateRun(world)
                var cursor = 0f
                var guard = 0
                while (!run.done && guard++ < 60 * 400) {
                    val goal = world.pathAt(run.dist + 0.1f * run.speed)
                    cursor += (goal - cursor).coerceIn(-0.8f * SkateRun.LATERAL_MAX * dt, 0.8f * SkateRun.LATERAL_MAX * dt)
                    run.steerTo(cursor)
                    run.update(dt)
                }
                assertEquals("choc en ${d.title}, graine $seed", 0, run.hits)
            }
        }
    }

    @Test
    fun aMotionlessSkaterAlwaysFinishesWithAtLeastOneStar() {
        for (d in SkateDifficulty.entries) {
            for (seed in 0L until 30L) {
                val run = playToTheEnd(SkateRun(SkateWorld.generate(d, seed)))
                assertTrue(run.stars in 1..3)
                assertTrue("des chocs, mais jamais de game over", run.lives >= 0)
            }
        }
    }

    @Test
    fun aFlailingSkaterAlwaysFinishesToo() {
        for (d in SkateDifficulty.entries) {
            val random = Random(d.index)
            for (seed in 0L until 20L) {
                val run = SkateRun(SkateWorld.generate(d, seed))
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
        val world = SkateWorld.generate(SkateDifficulty.HARD, 7)
        fun play(frame: Float): SkateRun {
            val run = SkateRun(world)
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
        val cone = Piece(0, PieceKind.CONE, 60f, 0f, 0)
        val run = SkateRun(street(cone))
        var hit: SkateEvent.Hit? = null
        var before = 0f
        var guard = 0
        while (hit == null && guard++ < 10_000) {
            before = run.speed
            hit = run.update(dt).filterIsInstance<SkateEvent.Hit>().firstOrNull()
        }
        assertTrue("le choc a lieu", hit != null)
        assertEquals(2, run.lives)
        assertEquals(1, run.hits)
        assertTrue("la vitesse tombe d'un coup (${run.speed} contre $before)", run.speed < before * 0.45f)
        assertTrue(run.hurt)
        assertTrue(cone.id in run.knocked)
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
    fun aHitPedestrianIsHitOnlyOnceAndEarnsNoPoints() {
        val run = playToTheEnd(SkateRun(street(Piece(0, PieceKind.PEDESTRIAN, 50f, 0.05f, 0))))
        assertEquals(1, run.hits)
        assertEquals(2, run.lives)
        assertEquals("un piéton heurté ne rapporte pas de points", 0, run.score)
    }

    @Test
    fun anObstacleDodgedEarnsPoints() {
        val run = playToTheEnd(SkateRun(street(Piece(0, PieceKind.PEDESTRIAN, 50f, 0.7f, 0), Piece(1, PieceKind.LAMP, 80f, -0.7f, 0))))
        assertEquals(0, run.hits)
        assertEquals(2 * SkateRun.PASS_POINTS, run.score)
    }

    @Test
    fun hurtProtectionPreventsAChainOfHits() {
        val run = playToTheEnd(SkateRun(street(Piece(0, PieceKind.CONE, 50f, 0f, 0), Piece(1, PieceKind.CONE, 50.8f, 0f, 0), Piece(2, PieceKind.CONE, 51.6f, 0f, 0))))
        assertEquals(1, run.hits)
    }

    @Test
    fun livesNeverGoBelowZeroAndTheRunKeepsOneStar() {
        val run = playToTheEnd(SkateRun(street(*Array(8) { Piece(it, PieceKind.CONE, 50f + it * 14f, 0f, 0) })))
        assertTrue(run.hits >= 6)
        assertEquals(0, run.lives)
        assertEquals(1, run.stars)
    }

    @Test
    fun aRampMakesTheSkaterJumpAndGivesAStarBack() {
        // un plot, qui coûte une étoile ; puis une rampe, qui la rend ; puis un plot juste après la rampe (à 2,5 m de son bord) : en l'air on passe dessus
        val run = SkateRun(street(Piece(0, PieceKind.CONE, 46f, 0f, 0), Piece(1, PieceKind.RAMP, 80f, 0f, 0), Piece(2, PieceKind.CONE, 86f, 0f, 0)))
        val events = ArrayList<SkateEvent>()
        var guard = 0
        var maxHeight = 0f
        while (!run.done && guard++ < 100_000) {
            events += run.update(dt)
            maxHeight = maxOf(maxHeight, run.height)
        }
        assertEquals(1, run.hits)
        assertEquals(1, events.count { it is SkateEvent.Jump })
        assertEquals(1, events.count { it is SkateEvent.Land })
        assertTrue("une étoile rendue", (events.first { it is SkateEvent.Jump } as SkateEvent.Jump).starBack)
        assertEquals(3, run.lives)
        assertEquals(1, run.jumps)
        assertTrue("il est monté (${maxHeight})", maxHeight > 0.3f)
        assertTrue("les points de la rampe", run.score >= SkateRun.RAMP_POINTS)
        assertTrue("la rampe ne protège pas du plot d'avant, mais bien de celui d'après : un seul choc", run.hits == 1)
    }

    @Test
    fun aRampOffToTheSideDoesNothing() {
        val run = playToTheEnd(SkateRun(street(Piece(0, PieceKind.RAMP, 50f, 0.8f, 0))))
        assertEquals(0, run.jumps)
    }

    @Test
    fun theStarsNeverGoAboveThree() {
        val run = playToTheEnd(SkateRun(street(Piece(0, PieceKind.RAMP, 50f, 0f, 0))))
        assertEquals(3, run.lives)
        assertEquals(1, run.jumps)
    }

    @Test
    fun obstaclesAreFlownOverAfterARamp() {
        val run = playToTheEnd(SkateRun(street(Piece(0, PieceKind.RAMP, 50f, 0f, 0), Piece(1, PieceKind.CONE, 54.5f, 0f, 0), Piece(2, PieceKind.CONE, 56.5f, 0f, 0))))
        assertEquals("on a survolé les plots", 0, run.hits)
        assertNotEquals(0, run.jumps)
    }

    @Test
    fun theRunEndsAfterTheArchAndTheSkaterStops() {
        val run = SkateRun(SkateWorld.generate(SkateDifficulty.EASY, 3))
        val events = ArrayList<SkateEvent>()
        var guard = 0
        while (!run.done && guard++ < 100_000) {
            run.steerTo(run.world.pathAt(run.dist + 1f))
            events += run.update(dt)
        }
        assertEquals(1, events.count { it === SkateEvent.Finish })
        assertEquals(1, events.count { it === SkateEvent.Done })
        assertTrue(events.indexOf(SkateEvent.Finish) < events.indexOf(SkateEvent.Done))
        assertTrue(run.dist > run.world.length)
        assertEquals(0f, run.speed, 0.001f)
        assertTrue("l'arrêt se fait en quelques mètres (${run.dist - run.world.length})", run.dist - run.world.length < 40f)
    }

    @Test
    fun aCleanRunLastsAboutTheAnnouncedTime() {
        for (d in SkateDifficulty.entries) {
            val run = driveWithTheCorridor(SkateWorld.generate(d, 11))
            // + le démarrage depuis l'arrêt et le freinage final
            assertTrue("${d.title} : ${run.time} s pour ${d.seconds} s annoncées", run.time in d.seconds - 3f..d.seconds + 8f)
        }
    }

    @Test
    fun steeringFollowsTheFingerOneToOneWithoutLeavingTheStreet() {
        val run = SkateRun(street())
        run.steer(0.3f)
        run.steer(0.2f)
        assertEquals(0.5f, run.targetX, 0.0001f)
        for (i in 0 until 60) run.update(dt)
        assertEquals(0.5f, run.x, 0.01f)
        run.steer(5f)
        assertEquals(SkateRun.MAX_X, run.targetX, 0.0001f)
        run.steer(-9f)
        assertEquals(-SkateRun.MAX_X, run.targetX, 0.0001f)
        // jamais plus vite que LATERAL_MAX
        val start = run.x
        run.update(dt)
        assertTrue(abs(run.x - start) <= SkateRun.LATERAL_MAX * dt + 1e-4f)
    }

    @Test
    fun aCrossingPedestrianWalksAsAFunctionOfTheDistanceLeft() {
        val ped = Piece(0, PieceKind.PEDESTRIAN, 100f, 0.4f, 0, fromX = 1.3f, walkStart = 30f, walkEnd = 6f)
        assertEquals(1.3f, ped.xAt(45f), 0.0001f)
        assertEquals(0.4f, ped.xAt(6f), 0.0001f)
        assertEquals(0.4f, ped.xAt(0f), 0.0001f)
        assertEquals(0.85f, ped.xAt(18f), 0.0001f)
        assertTrue(ped.crosses)
        assertFalse(Piece(1, PieceKind.CONE, 10f, 0.4f, 0).crosses)
    }
}
