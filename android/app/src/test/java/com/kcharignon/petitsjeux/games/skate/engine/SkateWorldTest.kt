package com.kcharignon.petitsjeux.games.skate.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.math.ln

/** La rue tirée au hasard : toujours un couloir libre, des obstacles qui se multiplient, des rampes, et la même rue pour la même graine. */
class SkateWorldTest {
    private fun worlds(d: SkateDifficulty, n: Int = 60) = (0L until n).map { SkateWorld.generate(d, it) }

    @Test
    fun theSameSeedGivesTheSameStreetAndOtherSeedsGiveOtherStreets() {
        for (d in SkateDifficulty.entries) {
            val a = SkateWorld.generate(d, 42)
            val b = SkateWorld.generate(d, 42)
            assertEquals(a.pieces.map { Triple(it.kind, it.z, it.x) }, b.pieces.map { Triple(it.kind, it.z, it.x) })
            assertEquals(a.theme, b.theme)
            assertNotEquals(a.pieces.map { it.x }, SkateWorld.generate(d, 43).pieces.map { it.x })
        }
    }

    @Test
    fun consecutiveSeedsGiveUnrelatedStreets() {
        // les graines voisines (41, 42, 43…) ne donnent pas des rues qui se ressemblent
        for (d in SkateDifficulty.entries) {
            val firsts = (0L until 40L).map { SkateWorld.generate(d, it).pieces.first().x }.toSet()
            assertTrue("${d.title} : ${firsts.size} premiers obstacles différents sur 40", firsts.size >= 30)
        }
    }

    @Test
    fun piecesAreSortedAndInsideTheStreet() {
        for (d in SkateDifficulty.entries) for (w in worlds(d, 30)) {
            assertEquals(w.pieces.map { it.z }.sorted(), w.pieces.map { it.z })
            assertEquals(w.pieces.indices.toList(), w.pieces.map { it.id })
            for (p in w.pieces) {
                assertTrue("avant le départ ou après l'arrivée : ${p.z}", p.z >= SkateWorld.START_CLEAR - 0.35f * d.gapStart && p.z <= w.length - SkateWorld.END_CLEAR + 0.35f * d.gapStart + SkateWorld.RAMP_LENGTH)
                assertTrue(p.x - p.kind.width / 2 >= -1f && p.x + p.kind.width / 2 <= 1f)
            }
        }
    }

    @Test
    fun noPieceEverBlocksTheFreeCorridor() {
        for (d in SkateDifficulty.entries) for (w in worlds(d)) {
            for (p in w.pieces) {
                if (!p.kind.solid) continue
                val gap = abs(p.x - w.pathAt(p.z)) - p.kind.width / 2
                assertTrue("${d.title} graine ${w.seed} : ${p.kind} à ${p.z} m bouche le couloir (${gap})", gap >= SkateWorld.CORRIDOR_HALF + SkateWorld.CORRIDOR_CLEAR - 1e-4f)
            }
        }
    }

    @Test
    fun theCorridorNeverAsksMoreThanTheSafeLateralSpeed() {
        for (d in SkateDifficulty.entries) for (w in worlds(d)) {
            for (i in 1 until w.path.size - 1) {
                val a = w.path[i - 1]
                val b = w.path[i]
                val time = (b.z - a.z) / d.speedAt(b.z)
                val needed = abs(b.x - a.x) / time
                assertTrue("${d.title} graine ${w.seed} : ${needed} u/s", needed <= SkateWorld.LATERAL_SAFE * 0.5f + 1e-3f)
                assertTrue(abs(b.x) <= SkateWorld.CENTER_MAX + 1e-4f)
            }
        }
    }

    @Test
    fun theLateralSpeedAskedIsWellBelowWhatTheSkaterCanDo() {
        assertTrue(SkateRun.LATERAL_MAX >= 1.5f * SkateWorld.LATERAL_SAFE)
    }

    @Test
    fun obstaclesGetDenserAlongTheRun() {
        for (d in SkateDifficulty.entries) {
            // la zone où l'on pose des obstacles, coupée en trois : le dernier tiers est plus dense que le premier, malgré des rangées plus serrées qui en comptent autant
            var first = 0
            var last = 0
            for (w in worlds(d)) {
                val from = SkateWorld.START_CLEAR
                val third = (w.length - SkateWorld.END_CLEAR - from) / 3
                first += w.pieces.count { it.kind.solid && it.z < from + third }
                last += w.pieces.count { it.kind.solid && it.z > from + 2 * third }
            }
            assertTrue("${d.title} : $first obstacles au premier tiers, $last au dernier", last > first * 1.3)
        }
    }

    @Test
    fun harderDifficultiesHaveMoreObstaclesPerHundredMetersAndGoFaster() {
        val density = SkateDifficulty.entries.map { d -> worlds(d, 40).sumOf { w -> w.pieces.count { it.kind.solid } } / worlds(d, 40).sumOf { it.length.toDouble() } * 100 }
        for (i in 1 until density.size) assertTrue("densités $density", density[i] > density[i - 1])
        val ds = SkateDifficulty.entries
        for (i in 1 until ds.size) {
            assertTrue(ds[i].speedStart > ds[i - 1].speedStart && ds[i].speedEnd > ds[i - 1].speedEnd)
            assertTrue(ds[i].seconds > ds[i - 1].seconds)
            assertTrue(ds[i].gapStart <= ds[i - 1].gapStart && ds[i].gapEnd <= ds[i - 1].gapEnd)
        }
    }

    @Test
    fun theKindsOfObstacleAppearWithTheDifficulty() {
        fun kinds(d: SkateDifficulty) = worlds(d).flatMap { w -> w.pieces.map { it.kind } }.toSet()
        assertTrue(PieceKind.BENCH !in kinds(SkateDifficulty.EASY) && PieceKind.BARRIER !in kinds(SkateDifficulty.EASY))
        assertTrue(PieceKind.BENCH in kinds(SkateDifficulty.MEDIUM) && PieceKind.BARRIER !in kinds(SkateDifficulty.MEDIUM))
        assertTrue(PieceKind.BARRIER in kinds(SkateDifficulty.HARD))
        for (d in SkateDifficulty.entries) {
            val k = kinds(d)
            assertTrue(PieceKind.PEDESTRIAN in k && PieceKind.LAMP in k && PieceKind.CONE in k && PieceKind.BIN in k && PieceKind.RAMP in k)
        }
    }

    @Test
    fun pedestriansCrossFromTheMediumDifficultyOn() {
        assertEquals(0, worlds(SkateDifficulty.EASY).sumOf { w -> w.pieces.count { it.crosses } })
        for (d in listOf(SkateDifficulty.MEDIUM, SkateDifficulty.HARD, SkateDifficulty.EXPERT)) {
            assertTrue(d.title, worlds(d).sumOf { w -> w.pieces.count { it.crosses } } > 20)
        }
        for (d in SkateDifficulty.entries) for (w in worlds(d, 20)) for (p in w.pieces.filter { it.crosses }) {
            assertEquals(PieceKind.PEDESTRIAN, p.kind)
            assertTrue("il part du trottoir", abs(p.fromX) > 1.2f)
            assertTrue("il a fini de marcher bien avant d'arriver", p.walkEnd >= 5f && p.walkStart > p.walkEnd + 15f)
        }
    }

    @Test
    fun rampsAreRegularAndNeverTheLastThing() {
        for (d in SkateDifficulty.entries) {
            val ramps = worlds(d, 40).map { w -> w.pieces.filter { it.kind == PieceKind.RAMP } }
            val average = ramps.sumOf { it.size } / 40.0
            val expected = (d.length - SkateWorld.RAMP_END_CLEAR) / d.rampEvery
            assertTrue("${d.title} : $average rampes par course (attendu ~$expected)", average in expected * 0.5..expected * 1.3 && average >= 1.0)
            for (r in ramps.flatten()) assertTrue(r.z < d.length - SkateWorld.RAMP_END_CLEAR)
        }
    }

    @Test
    fun theLengthMatchesTheAnnouncedDuration() {
        for (d in SkateDifficulty.entries) {
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
        val themes = worlds(SkateDifficulty.EASY, 80).map { it.theme }.toSet()
        assertEquals((0 until SkateWorld.THEMES).toSet(), themes)
    }

    @Test
    fun difficultyHelpers() {
        assertEquals(1, SkateDifficulty.stars(0))
        assertEquals(1, SkateDifficulty.stars(1))
        assertEquals(2, SkateDifficulty.stars(2))
        assertEquals(3, SkateDifficulty.stars(3))
        assertEquals(3, SkateDifficulty.stars(9))
        assertEquals(SkateDifficulty.EASY, SkateDifficulty.of(-4))
        assertEquals(SkateDifficulty.EXPERT, SkateDifficulty.of(99))
        assertEquals(null, SkateDifficulty.EASY.previous)
        assertEquals(SkateDifficulty.EASY, SkateDifficulty.MEDIUM.previous)
    }
}
