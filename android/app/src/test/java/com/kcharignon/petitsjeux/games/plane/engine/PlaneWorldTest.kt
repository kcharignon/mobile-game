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

    /** Toutes les rangées, anneaux compris (un anneau est seul sur sa rangée : il laisse tout le reste libre). */
    private fun allRows(w: PlaneWorld) = w.pieces.groupBy { it.z }.toSortedMap().values.toList()

    private fun freeLanes(row: List<Piece>) = (-PlaneWorld.MAX_LANE..PlaneWorld.MAX_LANE).filter { l -> row.none { it.lane == l } }

    private fun hasTwoFreeLanesSideBySide(row: List<Piece>) = freeLanes(row).let { free -> free.any { it + 1 in free } }

    private fun narrowRows(w: PlaneWorld) = allRows(w).mapIndexedNotNull { i, row -> i.takeIf { freeLanes(row).size == 1 } }

    @Test
    fun theFirstTwoDifficultiesAlwaysKeepTwoFreeLanesSideBySide() {
        for (d in listOf(PlaneDifficulty.EASY, PlaneDifficulty.MEDIUM)) for (w in worlds(d, 120)) for ((i, row) in allRows(w).withIndex()) {
            assertTrue("${d.title} graine ${w.seed} : la rangée $i (à ${row.first().z} m) n'a pas deux voies libres côte à côte", hasTwoFreeLanesSideBySide(row))
        }
        // et il y a moins d'obstacles par rangée : 2 au plus au premier niveau, 3 au deuxième
        for (w in worlds(PlaneDifficulty.EASY, 60)) for (row in rows(w).values) assertTrue(row.size <= 2)
        for (w in worlds(PlaneDifficulty.MEDIUM, 60)) for (row in rows(w).values) assertTrue(row.size <= 3)
    }

    @Test
    fun onTheThirdDifficultyASingleFreeLaneOnlyComesAsAnIsolatedWall() {
        var walls = 0
        for (w in worlds(PlaneDifficulty.HARD, 120)) {
            val all = allRows(w)
            val narrow = narrowRows(w)
            walls += narrow.size
            for (i in narrow) {
                // un mur est seul sur sa rangée (une vague d'une rangée) : ni avant ni après, une seule voie libre
                if (i > 0) assertTrue("graine ${w.seed} : deux rangées étroites d'affilée", hasTwoFreeLanesSideBySide(all[i - 1]))
                if (i + 1 < all.size) assertTrue("graine ${w.seed} : deux rangées étroites d'affilée", hasTwoFreeLanesSideBySide(all[i + 1]))
            }
            // et deux murs sont toujours séparés d'au moins trois rangées larges
            for ((a, b) in narrow.zipWithNext()) assertTrue("graine ${w.seed} : des murs trop proches (rangées $a et $b)", b - a > PlaneWorld.SOLO_SPACING)
        }
        assertTrue("$walls murs isolés sur 120 vols : il doit en rester", walls > 30)
    }

    @Test
    fun onlyTheLastDifficultyKeepsASingleFreeLaneForSeveralRowsInARow() {
        fun streaks(d: PlaneDifficulty, minimum: Int) = worlds(d, 120).sumOf { w ->
            val narrow = narrowRows(w).toSet()
            allRows(w).indices.count { i -> (0 until minimum).all { (i + it) in narrow } }
        }
        for (d in PlaneDifficulty.entries.dropLast(1)) assertEquals(d.title, 0, streaks(d, 2))
        assertTrue("des passages étroits d'affilée à la dernière difficulté", streaks(PlaneDifficulty.EXPERT, 3) > 30)
    }

    @Test
    fun formationsAppearWithTheDifficulty() {
        // un slalom : au moins 3 rangées de suite du même genre et de la même taille, dont le couloir change d'une voie à chaque rangée
        fun slaloms(w: PlaneWorld, size: Int): Int {
            val rs = rows(w).entries.toList()
            var count = 0
            for (i in 0 until rs.size - 2) {
                val three = rs.subList(i, i + 3)
                val full = three.all { it.value.size == size && it.value.map { p -> p.kind }.toSet().size == 1 } && three.map { it.value.first().kind }.toSet().size == 1
                if (full) {
                    val centers = three.map { Math.round(w.pathAt(it.key) / PlaneWorld.LANE_WIDTH) }
                    if (centers[0] != centers[1] && centers[1] != centers[2]) count++
                }
            }
            return count
        }
        // des slaloms à deux voies libres dès la deuxième difficulté (trois obstacles par rangée) ; à une seule voie libre (quatre obstacles) à la dernière seulement
        assertEquals(0, worlds(PlaneDifficulty.EASY).sumOf { slaloms(it, 3) })
        for (d in listOf(PlaneDifficulty.MEDIUM, PlaneDifficulty.HARD)) assertTrue(d.title, worlds(d, 120).sumOf { slaloms(it, 3) } > 10)
        for (d in PlaneDifficulty.entries.dropLast(1)) assertEquals(d.title, 0, worlds(d, 120).sumOf { slaloms(it, 4) })
        assertTrue(worlds(PlaneDifficulty.EXPERT, 120).sumOf { slaloms(it, 4) } > 20)
        // des murs (4 voies sur 5) dès la troisième difficulté seulement
        for (d in listOf(PlaneDifficulty.EASY, PlaneDifficulty.MEDIUM)) assertEquals(d.title, 0, worlds(d).sumOf { w -> rows(w).values.count { it.size == 4 } })
        assertTrue(worlds(PlaneDifficulty.HARD, 120).sumOf { w -> rows(w).values.count { it.size == 4 } } > 20)
    }

    @Test
    fun wavesAreShortAndLeaveRoomToBreathe() {
        // une vague de plus de quatre rangées du même genre n'existe pas ; et les slaloms ne s'enchaînent pas sans une vague éparpillée entre eux
        for (d in PlaneDifficulty.entries) for (w in worlds(d, 60)) {
            var run = 1
            val kinds = rows(w).values.map { it.first().kind }
            for ((a, b) in kinds.zipWithNext()) {
                run = if (a == b) run + 1 else 1
                assertTrue("${d.title} graine ${w.seed} : $run rangées du même genre d'affilée", run <= 5)
            }
        }
        // le plus souvent, une vague n'a qu'une ou deux rangées : au moins un tiers des rangées sont suivies d'une rangée d'un autre genre
        for (d in PlaneDifficulty.entries) {
            var changes = 0
            var total = 0
            for (w in worlds(d, 60)) rows(w).values.map { it.first().kind }.zipWithNext().forEach { (a, b) -> total++; if (a != b) changes++ }
            assertTrue("${d.title} : $changes changements de genre sur $total rangées", changes * 100 >= total * 35)
        }
    }

    @Test
    fun planesCrossTheSkyOnTheThirdAndFourthDifficultiesOnly() {
        for (d in listOf(PlaneDifficulty.EASY, PlaneDifficulty.MEDIUM)) for (w in worlds(d, 120)) assertTrue(d.title, w.pieces.none { it.crossing })
        for (d in listOf(PlaneDifficulty.HARD, PlaneDifficulty.EXPERT)) {
            val crossing = worlds(d, 120).flatMap { w -> w.pieces.filter { it.crossing } }
            assertTrue("${d.title} : ${crossing.size} avions qui traversent", crossing.size > 40)
            for (p in crossing) {
                assertEquals(PieceKind.PLANE, p.kind)
                assertTrue("traversée de ${p.drift} voies", abs(p.drift) in PlaneWorld.CROSS_MIN_LANES..PlaneWorld.CROSS_MIN_LANES + 1)
                assertTrue(p.span > 0f)
            }
            // dans les deux sens
            assertTrue(crossing.any { it.drift > 0 } && crossing.any { it.drift < 0 })
        }
    }

    @Test
    fun aCrossingWaveCrossesTogetherAndNeverMixesWithPlanesThatStayPut() {
        for (d in listOf(PlaneDifficulty.HARD, PlaneDifficulty.EXPERT)) for (w in worlds(d, 120)) {
            for ((z, row) in rows(w)) {
                if (row.any { it.crossing }) {
                    assertTrue("${d.title} : tous les avions d'une rangée traversent", row.all { it.crossing })
                    assertEquals("même sens et même durée dans la rangée à $z m", 1, row.map { it.drift to it.span }.toSet().size)
                }
            }
        }
    }

    @Test
    fun aCrossingPlaneGlidesToItsLaneAndIsInItWhenYouPassIt() {
        val plane = Piece(0, PieceKind.PLANE, 100f, 1, 0, drift = 4, span = 60f)
        assertEquals(plane.x, plane.xAt(0f), 1e-6f)
        assertEquals(plane.x, plane.xAt(-3f), 1e-6f)
        // loin, il est 4 voies plus loin sur le côté d'où il vient
        assertEquals(plane.x - 4 * PlaneWorld.LANE_WIDTH, plane.xAt(60f), 1e-5f)
        assertEquals(plane.x - 4 * PlaneWorld.LANE_WIDTH, plane.xAt(500f), 1e-5f)
        // à mi-chemin, à mi-chemin
        assertEquals(plane.x - 2 * PlaneWorld.LANE_WIDTH, plane.xAt(30f), 1e-5f)
        // il avance toujours dans le même sens, et se pose en douceur (presque plus de mouvement de côté tout près, comme tout au bout)
        var last = plane.xAt(60f)
        for (rel in 59 downTo 0) {
            val x = plane.xAt(rel.toFloat())
            assertTrue("recule à $rel m", x >= last - 1e-6f)
            last = x
        }
        assertTrue(abs(plane.xAt(0.5f) - plane.x) < 0.01f)
        assertTrue(abs(plane.xAt(59.5f) - plane.xAt(60f)) < 0.01f)
        // il penche du côté où il va, le plus au milieu de la traversée
        assertEquals(1f, plane.bankAt(30f), 1e-5f)
        assertEquals(0f, plane.bankAt(0f), 1e-6f)
        assertEquals(0f, plane.bankAt(80f), 1e-6f)
        assertEquals(-1f, Piece(1, PieceKind.PLANE, 100f, 0, 0, drift = -3, span = 40f).bankAt(20f), 1e-5f)
        // un obstacle ordinaire ne bouge jamais de côté
        val bird = Piece(2, PieceKind.BIRDS, 100f, -2, 0)
        assertEquals(bird.x, bird.xAt(40f), 1e-6f)
        assertEquals(0f, bird.bankAt(40f), 1e-6f)
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
