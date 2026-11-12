package com.kcharignon.petitsjeux.games.plane.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.math.ln

/** Le ciel tiré au hasard : des voies, des vagues d'un seul genre, un couloir libre, des obstacles qui se multiplient, des anneaux, et le même ciel pour la même graine. */
class PlaneWorldTest {
    private fun worlds(d: PlaneDifficulty, n: Int = 60) = (0L until n).map { PlaneWorld.generate(d, it) }

    /** Les rangées d'un ciel : les obstacles groupés par distance. */
    private fun rows(w: PlaneWorld) = w.pieces.filter { it.kind != PieceKind.RING }.groupBy { it.z }.toSortedMap()

    @Test
    fun theSameSeedGivesTheSameSkyAndOtherSeedsGiveOtherSkies() {
        for (d in PlaneDifficulty.entries) {
            val a = PlaneWorld.generate(d, 42)
            val b = PlaneWorld.generate(d, 42)
            assertEquals(a.pieces.map { Triple(it.kind, it.z, it.lane) }, b.pieces.map { Triple(it.kind, it.z, it.lane) })
            assertEquals(a.theme, b.theme)
            assertNotEquals(a.pieces.map { it.lane }, PlaneWorld.generate(d, 43).pieces.map { it.lane })
        }
    }

    @Test
    fun consecutiveSeedsGiveUnrelatedSkies() {
        for (d in PlaneDifficulty.entries) {
            val firsts = (0L until 40L).map { w -> PlaneWorld.generate(d, w).pieces.let { p -> p.take(3).map { it.lane to it.kind } } }.toSet()
            assertTrue("${d.title} : ${firsts.size} débuts différents sur 40", firsts.size >= 15)
        }
    }

    @Test
    fun everyObstacleSitsInTheMiddleOfALaneAndNeverStraddlesTwo() {
        for (d in PlaneDifficulty.entries) for (w in worlds(d, 30)) for (p in w.pieces) {
            assertTrue("voie ${p.lane}", p.lane in -PlaneWorld.MAX_LANE..PlaneWorld.MAX_LANE)
            assertEquals(p.lane * PlaneWorld.LANE_WIDTH, p.x, 1e-6f)
            if (p.kind.solid) {
                assertTrue("${p.kind} large de ${p.kind.width}", p.kind.width <= PlaneWorld.LANE_WIDTH * 0.75f)
                // le bord de l'obstacle reste dans sa voie
                assertTrue(abs(p.x) + p.kind.width / 2 <= PlaneWorld.MAX_LANE * PlaneWorld.LANE_WIDTH + PlaneWorld.LANE_WIDTH / 2)
                assertTrue(p.kind.width / 2 < PlaneWorld.LANE_WIDTH / 2)
            }
        }
    }

    @Test
    fun obstaclesOfARowAreAlignedAndOfASingleKind() {
        for (d in PlaneDifficulty.entries) for (w in worlds(d)) {
            for ((z, row) in rows(w)) {
                assertEquals("une rangée à $z m mélange des genres : ${row.map { it.kind }}", 1, row.map { it.kind }.toSet().size)
                assertEquals("deux obstacles dans la même voie", row.size, row.map { it.lane }.toSet().size)
            }
        }
    }

    @Test
    fun everyRowLeavesAtLeastOneFreeLane() {
        for (d in PlaneDifficulty.entries) for (w in worlds(d)) {
            for ((z, row) in rows(w)) {
                assertTrue("rangée pleine à $z m", row.size <= PlaneWorld.LANES - 1)
                // et la voie libre du couloir en fait partie
                val center = Math.round(w.pathAt(z) / PlaneWorld.LANE_WIDTH)
                assertTrue("le couloir est bouché à $z m", row.none { it.lane == center })
            }
        }
    }

    @Test
    fun theWavesAreMadeOfASingleKindAndTheKindChangesBetweenWaves() {
        // une vague = des rangées qui se suivent à intervalle régulier ; le genre ne change qu'entre deux vagues (jamais deux fois le même d'affilée)
        for (d in PlaneDifficulty.entries) {
            var changes = 0
            var sameInARow = 0
            for (w in worlds(d, 40)) {
                val kinds = rows(w).values.map { it.first().kind }
                for ((a, b) in kinds.zipWithNext()) if (a != b) changes++ else sameInARow++
            }
            assertTrue("${d.title} : $changes changements de genre", changes > 20)
            assertTrue("${d.title} : des vagues de plusieurs rangées ($sameInARow)", sameInARow > 20)
        }
    }

    @Test
    fun rowsInAWaveAreRegularlySpaced() {
        for (d in PlaneDifficulty.entries) for (w in worlds(d, 30)) {
            val zs = rows(w).keys.toList()
            // tous les écarts sont des multiples réguliers de l'écart du moment : jamais un écart minuscule (le temps de réagir)
            for ((a, b) in zs.zipWithNext()) {
                val time = (b - a) / d.speedAt(b)
                assertTrue("${d.title} : ${b - a} m ($time s) entre deux rangées", time >= 0.33f)
            }
        }
    }

    @Test
    fun theFreeCorridorNeverAsksMoreThanTheSafeLateralSpeed() {
        for (d in PlaneDifficulty.entries) for (w in worlds(d)) {
            for (i in 1 until w.path.size - 1) {
                val a = w.path[i - 1]
                val b = w.path[i]
                val time = (b.z - a.z) / d.speedAt(b.z)
                val needed = abs(b.x - a.x) / time
                assertTrue("${d.title} graine ${w.seed} : $needed u/s", needed <= PlaneWorld.LATERAL_SAFE + 1e-3f)
                // le couloir passe toujours par le milieu d'une voie
                val lane = b.x / PlaneWorld.LANE_WIDTH
                assertEquals(Math.round(lane).toFloat(), lane, 1e-4f)
            }
        }
    }

    @Test
    fun theLateralSpeedAskedIsWellBelowWhatThePlaneCanDo() {
        assertTrue(PlaneRun.LATERAL_MAX >= 1.5f * PlaneWorld.LATERAL_SAFE)
    }

    @Test
    fun obstaclesGetDenserAlongTheFlight() {
        for (d in PlaneDifficulty.entries) {
            var first = 0
            var last = 0
            for (w in worlds(d)) {
                val from = PlaneWorld.START_CLEAR
                val third = (w.length - PlaneWorld.END_CLEAR - from) / 3
                first += w.pieces.count { it.kind.solid && it.z < from + third }
                last += w.pieces.count { it.kind.solid && it.z > from + 2 * third }
            }
            assertTrue("${d.title} : $first obstacles au premier tiers, $last au dernier", last > first * 1.25)
        }
    }

    @Test
    fun harderDifficultiesAreFasterAndDenser() {
        val density = PlaneDifficulty.entries.map { d -> worlds(d, 40).sumOf { w -> w.pieces.count { it.kind.solid } } / worlds(d, 40).sumOf { it.length.toDouble() } * 100 }
        for (i in 1 until density.size) assertTrue("densités $density", density[i] > density[i - 1])
        val ds = PlaneDifficulty.entries
        for (i in 1 until ds.size) {
            assertTrue(ds[i].speedStart > ds[i - 1].speedStart && ds[i].speedEnd > ds[i - 1].speedEnd)
            assertTrue(ds[i].seconds > ds[i - 1].seconds)
            assertTrue(ds[i].gapStart <= ds[i - 1].gapStart && ds[i].gapEnd <= ds[i - 1].gapEnd)
        }
    }

    @Test
    fun theKindsOfObstacleAppearWithTheDifficulty() {
        fun kinds(d: PlaneDifficulty) = worlds(d).flatMap { w -> w.pieces.map { it.kind } }.toSet()
        assertTrue(PieceKind.PLANE !in kinds(PlaneDifficulty.EASY))
        for (d in PlaneDifficulty.entries.drop(1)) assertTrue(d.title, PieceKind.PLANE in kinds(d))
        for (d in PlaneDifficulty.entries) {
            val k = kinds(d)
            assertTrue(d.title, PieceKind.BIRDS in k && PieceKind.BALLOON in k && PieceKind.PARAGLIDER in k && PieceKind.RING in k)
        }
    }

    @Test
    fun formationsAppearWithTheDifficulty() {
        // un slalom : au moins 3 rangées de suite qui ne laissent qu'une voie libre, dont la place change d'une voie à chaque rangée
        fun slaloms(w: PlaneWorld): Int {
            val rs = rows(w).values.toList()
            var count = 0
            var i = 0
            while (i + 2 < rs.size) {
                val full = rs.subList(i, i + 3).all { it.size == PlaneWorld.LANES - 1 && it.map { p -> p.kind }.toSet().size == 1 }
                if (full) {
                    val frees = rs.subList(i, i + 3).map { row -> (-2..2).first { l -> row.none { it.lane == l } } }
                    if (frees[0] != frees[1] && frees[1] != frees[2]) count++
                }
                i++
            }
            return count
        }
        assertEquals(0, worlds(PlaneDifficulty.EASY).sumOf { slaloms(it) })
        for (d in listOf(PlaneDifficulty.HARD, PlaneDifficulty.EXPERT)) assertTrue(d.title, worlds(d).sumOf { slaloms(it) } > 20)
        // des murs (4 voies sur 5) dès la troisième difficulté seulement
        assertEquals(0, worlds(PlaneDifficulty.EASY).sumOf { w -> rows(w).values.count { it.size == 4 } })
        assertTrue(worlds(PlaneDifficulty.HARD).sumOf { w -> rows(w).values.count { it.size == 4 } } > 50)
    }

    @Test
    fun ringsAreAloneOnTheirRowInTheFreeLaneAndRegular() {
        for (d in PlaneDifficulty.entries) {
            val ringsPerFlight = worlds(d, 40).map { w -> w.pieces.filter { it.kind == PieceKind.RING } }
            val average = ringsPerFlight.sumOf { it.size } / 40.0
            val expected = (d.length - PlaneWorld.RING_END_CLEAR) / d.ringEvery
            assertTrue("${d.title} : $average anneaux par vol (attendu ~$expected)", average in expected * 0.5..expected * 1.3 && average >= 1.0)
            for (w in worlds(d, 20)) for (r in w.pieces.filter { it.kind == PieceKind.RING }) {
                assertTrue(r.z < d.length - PlaneWorld.RING_END_CLEAR)
                assertTrue("rien d'autre sur la rangée de l'anneau", w.pieces.none { it.z == r.z && it.kind != PieceKind.RING })
                assertEquals("l'anneau est dans la voie libre", r.x, w.pathAt(r.z), 1e-4f)
            }
        }
    }

    @Test
    fun theApproachToTheAirportIsClear() {
        for (d in PlaneDifficulty.entries) for (w in worlds(d, 30)) {
            val last = w.pieces.maxOf { it.z }
            assertTrue("${d.title} : dernier obstacle à $last m sur ${w.length}", last <= w.length - PlaneRun.APPROACH_METERS - 5f)
        }
    }

    @Test
    fun theLengthMatchesTheAnnouncedDuration() {
        for (d in PlaneDifficulty.entries) {
            // temps = intégrale de 1 / vitesse sur la longueur : v monte en ligne droite de speedStart à speedEnd
            val time = d.length / (d.speedEnd - d.speedStart) * ln(d.speedEnd / d.speedStart)
            assertEquals(d.seconds.toFloat(), time, 0.01f)
            assertEquals(d.speedStart, d.speedAt(0f), 0.0001f)
            assertEquals(d.speedEnd, d.speedAt(d.length), 0.0001f)
            assertEquals(d.speedEnd, d.speedAt(d.length * 3), 0.0001f)
        }
    }

    @Test
    fun themesVary() {
        val themes = worlds(PlaneDifficulty.EASY, 80).map { it.theme }.toSet()
        assertEquals((0 until PlaneWorld.THEMES).toSet(), themes)
    }

    @Test
    fun difficultyHelpers() {
        assertEquals(1, PlaneDifficulty.stars(0))
        assertEquals(1, PlaneDifficulty.stars(1))
        assertEquals(2, PlaneDifficulty.stars(2))
        assertEquals(3, PlaneDifficulty.stars(3))
        assertEquals(3, PlaneDifficulty.stars(9))
        assertEquals(PlaneDifficulty.EASY, PlaneDifficulty.of(-4))
        assertEquals(PlaneDifficulty.EXPERT, PlaneDifficulty.of(99))
        assertEquals(null, PlaneDifficulty.EASY.previous)
        assertEquals(PlaneDifficulty.EASY, PlaneDifficulty.MEDIUM.previous)
    }
}
