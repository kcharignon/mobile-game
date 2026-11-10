package com.kcharignon.petitsjeux.games.maze.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.random.Random

/** La partie : la trace suit les couloirs, ne traverse jamais un mur, se rembobine, compte les impasses, et le doigt robot finit chacun des 100 niveaux. */
class MazePlayTest {
    private val levels: List<MazeLevel> by lazy { MazeLevels.parse(File("src/main/res/raw/maze_levels.json").readText()) }
    private fun level(n: Int) = levels[n - 1]

    /** Le centre d'une case, en cases. */
    private fun cx(c: Cell) = c.x + 0.5
    private fun cy(c: Cell) = c.y + 0.5

    private fun grab(p: MazePlay) = assertTrue(p.down(cx(p.head), cy(p.head)))

    /** Le doigt va au centre de la case [c]. */
    private fun fingerTo(p: MazePlay, c: Cell, reach: Int = MazePlay.DEFAULT_REACH) = p.move(cx(c), cy(c), reach)

    /** Une trace est valide si elle part du départ, sans repasser deux fois par la même case, sans jamais franchir un mur. */
    private fun assertValid(p: MazePlay) {
        val t = p.trail
        assertEquals(p.level.start, t.first())
        assertEquals("une case en double dans la trace", t.size, t.toSet().size)
        for (i in 1 until t.size) assertTrue("la trace traverse un mur entre ${t[i - 1]} et ${t[i]}", p.level.isOpen(t[i - 1], t[i]))
    }

    // ------------------------------------------------------------------ prendre la trace

    @Test
    fun theTrailStartsAtTheStartAndNeedsTheFingerOnItsHead() {
        val p = MazePlay(level(1))
        assertEquals(listOf(level(1).start), p.trail)
        // le doigt loin du poisson ne prend rien, et ne bouge rien
        assertFalse(p.down(0.2, 0.2))
        assertTrue(p.move(cx(level(1).start) - 1, cy(level(1).start) - 1).isEmpty())
        assertEquals(listOf(level(1).start), p.trail)
        // près du poisson, on le prend
        assertTrue(p.down(cx(level(1).start) + 0.9, cy(level(1).start)))
        assertTrue(p.holding)
    }

    @Test
    fun liftingTheFingerKeepsTheTrailAndTheHeadCanBeGrabbedAgain() {
        val l = level(1)
        val p = MazePlay(l)
        grab(p)
        fingerTo(p, l.solution[1])
        fingerTo(p, l.solution[2])
        p.up()
        assertFalse(p.holding)
        assertEquals(l.solution.take(3), p.trail)
        // le doigt qui bouge sans être posé ne fait rien
        assertTrue(p.move(cx(l.solution[3]), cy(l.solution[3])).isEmpty())
        // on reprend par la tête
        assertTrue(p.down(cx(l.solution[2]) + 0.4, cy(l.solution[2])))
        fingerTo(p, l.solution[3])
        assertEquals(l.solution.take(4), p.trail)
        // on ne reprend pas ailleurs (le départ est trop loin de la tête)
        p.up()
        assertFalse(p.down(cx(l.solution[0]), cy(l.solution[0])))
    }

    // ------------------------------------------------------------------ suivre les couloirs, jamais les murs

    @Test
    fun theTrailFollowsTheCorridorWhenTheFingerCutsACorner() {
        // un coin de la solution : le doigt va en diagonale, la trace passe par la case du coin
        val l = level(1)
        val corner = (1 until l.solution.size - 1).first { l.solution[it - 1].x != l.solution[it + 1].x && l.solution[it - 1].y != l.solution[it + 1].y }
        val p = MazePlay(l)
        grab(p)
        for (i in 1 until corner) fingerTo(p, l.solution[i])
        assertEquals(l.solution[corner - 1], p.head)
        val events = fingerTo(p, l.solution[corner + 1])
        assertEquals(l.solution.take(corner + 2), p.trail)
        assertEquals(2, events.count { it is MazeEvent.Stepped })
        assertValid(p)
    }

    @Test
    fun aFingerAcrossAWallDoesNothingAndTheWallIsSignalledOnlyOnce() {
        val l = level(1)
        val p = MazePlay(l)
        grab(p)
        val head = p.head
        val blocked = listOf(Cell(head.x, head.y - 1), Cell(head.x - 1, head.y), Cell(head.x + 1, head.y)).first { l.inside(it) && l.wallBetween(head, it) }
        val first = fingerTo(p, blocked)
        assertEquals(listOf<MazeEvent>(MazeEvent.Blocked(head, blocked)), first)
        assertEquals(listOf(head), p.trail)
        assertTrue("le même mur ne se signale qu'une fois par appui", fingerTo(p, blocked).isEmpty())
        // la partie continue normalement
        fingerTo(p, l.solution[1])
        assertEquals(l.solution.take(2), p.trail)
    }

    @Test
    fun aFingerOnTheOtherSideOfAThinWallDoesNotSendTheTrailAroundIt() {
        // deux cases voisines séparées par un mur, mais reliées par un tour de 3 pas ou plus : la trace n'y va pas
        for (l in levels.take(60)) for (along in listOf(0, 3, 6)) {
            val p = MazePlay(l)
            grab(p)
            for (i in 1..along) fingerTo(p, l.solution[i])
            val head = p.head
            for (target in listOf(Cell(head.x - 1, head.y), Cell(head.x + 1, head.y), Cell(head.x, head.y - 1), Cell(head.x, head.y + 1))) {
                if (!l.inside(target) || !l.wallBetween(head, target) || target in p.trail) continue
                val before = p.trail
                p.move(cx(target), cy(target), reach = 8)
                assertEquals("niveau ${l.number} : la trace a fait le tour d'un mur", before, p.trail)
            }
        }
    }

    @Test
    fun aFastFingerGainsSeveralCellsAtOnceButASlowOneGainsAtMostThree() {
        // un niveau dont la solution a cinq cases d'affilée en ligne droite
        fun straightRun(l: MazeLevel): Int? = (0 until l.solution.size - 5).firstOrNull { i -> l.solution[i + 5].manhattan(l.solution[i]) == 5 && (1..5).all { l.solution[i + it].x == l.solution[i].x || l.solution[i + it].y == l.solution[i].y } }
        val l = levels.first { straightRun(it) != null }
        val i = straightRun(l)!!
        val slow = MazePlay(l)
        val fast = MazePlay(l)
        for (p in listOf(slow, fast)) {
            grab(p)
            for (k in 1..i) fingerTo(p, l.solution[k])
        }
        fingerTo(slow, l.solution[i + 5], reach = MazePlay.DEFAULT_REACH)
        assertEquals("un doigt lent ne gagne pas 5 cases d'un coup", i + 1, slow.trail.size)
        fingerTo(fast, l.solution[i + 5], reach = 8)
        assertEquals(i + 6, fast.trail.size)
    }

    @Test
    fun reachGrowsWithTheSpeedOfTheFinger() {
        assertEquals(3, MazePlay.reachFor(0.0))
        assertEquals(3, MazePlay.reachFor(0.9))
        assertEquals(5, MazePlay.reachFor(3.0))
        assertEquals(8, MazePlay.reachFor(40.0))
    }

    @Test
    fun theFingerOutsideTheGridIsIgnored() {
        val l = level(1)
        val p = MazePlay(l)
        grab(p)
        assertTrue(p.move(-3.0, 2.0).isEmpty())
        assertTrue(p.move(cx(p.head), l.rows + 4.0).isEmpty())
        assertEquals(1, p.trail.size)
    }

    // ------------------------------------------------------------------ reculer

    @Test
    fun goingBackOverTheTrailRewindsIt() {
        val l = level(2)
        val p = MazePlay(l)
        grab(p)
        for (i in 1..4) fingerTo(p, l.solution[i])
        assertEquals(l.solution.take(5), p.trail)
        // un pas en arrière
        val one = fingerTo(p, l.solution[3])
        assertEquals(listOf<MazeEvent>(MazeEvent.Backed(1)), one)
        assertEquals(l.solution.take(4), p.trail)
        // d'un coup, jusqu'au départ
        val all = fingerTo(p, l.solution[0], reach = 8)
        assertEquals(listOf<MazeEvent>(MazeEvent.Backed(3)), all)
        assertEquals(listOf(l.start), p.trail)
        assertEquals(0, p.detours)
        // et on repart
        fingerTo(p, l.solution[1])
        assertEquals(l.solution.take(2), p.trail)
    }

    @Test
    fun aFingerThatBrushesAnAdjacentCorridorOfTheTrailDoesNotEraseIt() {
        // des cases de la trace qui sont voisines à vol d'oiseau mais loin le long de la trace : un doigt qui les frôle n'efface rien
        for (l in levels.drop(40).take(30)) {
            val p = MazePlay(l)
            grab(p)
            for (c in l.solution.drop(1).take(24)) fingerTo(p, c, reach = 8)
            val t = p.trail
            for (back in 5 until t.size) {
                val target = t[t.size - 1 - back]
                if (target.manhattan(p.head) == back) continue // en ligne droite derrière la tête : c'est un vrai retour
                val before = p.trail
                p.move(cx(target), cy(target), reach = 8)
                assertEquals("niveau ${l.number} : la trace s'est effacée en frôlant $target", before, p.trail)
            }
        }
    }

    // ------------------------------------------------------------------ impasses et étoiles

    /** La première case de la solution où un couloir part vers une impasse : (case sur la solution, case de l'impasse). */
    private fun branch(l: MazeLevel): Pair<Int, Cell> {
        for (i in 0 until l.solution.size - 1) {
            val wrong = l.neighbors(l.solution[i]).firstOrNull { it != l.solution[i + 1] && (i == 0 || it != l.solution[i - 1]) }
            if (wrong != null && l.distanceToExit(wrong) > l.distanceToExit(l.solution[i])) return i to wrong
        }
        error("pas d'embranchement dans le niveau ${l.number}")
    }

    @Test
    fun enteringADeadEndCountsOneDetourNoMatterHowDeepAndGoingDeeperCountsNothingMore() {
        val l = level(5)
        val (at, wrong) = branch(l)
        val p = MazePlay(l)
        grab(p)
        for (i in 1..at) fingerTo(p, l.solution[i])
        assertEquals(0, p.detours)
        val events = fingerTo(p, wrong)
        assertEquals(1, p.detours)
        assertTrue(events.any { it is MazeEvent.Detour })
        assertEquals(MazeScoring.stars(1), p.stars)
        // on avance dans l'impasse tant qu'on peut : toujours une seule impasse
        var deeper = p.head
        while (true) {
            val next = l.neighbors(deeper).firstOrNull { it !in p.trail } ?: break
            fingerTo(p, next)
            deeper = next
        }
        assertEquals(1, p.detours)
    }

    @Test
    fun reachingTheBottomOfADeadEndIsSignalledAndBacktrackingReenteringCountsAgain() {
        val l = level(5)
        val (at, wrong) = branch(l)
        val p = MazePlay(l)
        grab(p)
        for (i in 1..at) fingerTo(p, l.solution[i])
        val events = ArrayList<MazeEvent>()
        events += fingerTo(p, wrong)
        var here = wrong
        while (true) {
            val next = l.neighbors(here).firstOrNull { it !in p.trail } ?: break
            events += fingerTo(p, next)
            here = next
        }
        assertEquals("on a atteint le fond de l'impasse", 1, events.count { it is MazeEvent.DeadEnd })
        assertEquals(here, (events.last { it is MazeEvent.DeadEnd } as MazeEvent.DeadEnd).cell)
        // on recule jusqu'à l'embranchement, puis on reprend la bonne voie : une seule impasse
        for (c in p.trail.reversed().drop(1)) {
            fingerTo(p, c, reach = 8)
            if (c == l.solution[at]) break
        }
        assertEquals(l.solution[at], p.head)
        assertEquals(1, p.detours)
        // se ré-engager dans l'impasse après être revenu à l'embranchement : une impasse de plus
        fingerTo(p, wrong)
        assertEquals(2, p.detours)
    }

    @Test
    fun starsFollowTheDetoursAndAWinNeverEarnsLessThanOne() {
        assertEquals(3, MazeScoring.stars(0))
        assertEquals(2, MazeScoring.stars(1))
        assertEquals(2, MazeScoring.stars(2))
        assertEquals(1, MazeScoring.stars(3))
        assertEquals(1, MazeScoring.stars(40))
    }

    // ------------------------------------------------------------------ le doigt robot finit les 100 niveaux

    @Test
    fun aRobotFingerFollowingTheSolutionWithNoiseFinishesEveryLevelWithThreeStars() {
        val noise = Random(7)
        for (l in levels) {
            val p = MazePlay(l)
            assertTrue(p.down(cx(l.start) + (noise.nextDouble() - 0.5) * 0.6, cy(l.start) + (noise.nextDouble() - 0.5) * 0.6))
            var last = l.start
            val events = ArrayList<MazeEvent>()
            for (c in l.solution.drop(1)) {
                // le doigt tremble de ±0,3 case autour du centre de la case
                events += p.move(cx(c) + (noise.nextDouble() - 0.5) * 0.6, cy(c) + (noise.nextDouble() - 0.5) * 0.6)
                assertValid(p)
                last = c
            }
            assertTrue("niveau ${l.number} : pas fini", p.finished)
            assertEquals(l.exit, last)
            assertEquals("niveau ${l.number} : des impasses en suivant la solution", 0, p.detours)
            assertEquals(3, p.stars)
            assertEquals(l.solution, p.trail)
            assertEquals(1, events.count { it === MazeEvent.Arrived })
            assertTrue("arrivé : le doigt est lâché", !p.holding)
        }
    }

    @Test
    fun nothingMovesAfterTheArrival() {
        val l = level(1)
        val p = MazePlay(l)
        grab(p)
        for (c in l.solution.drop(1)) fingerTo(p, c)
        assertTrue(p.finished)
        assertFalse(p.down(cx(l.exit), cy(l.exit)))
        assertTrue(p.move(cx(l.start), cy(l.start), reach = 8).isEmpty())
        assertEquals(l.solution, p.trail)
    }

    @Test
    fun resetStartsTheLevelOver() {
        val l = level(5)
        val (at, wrong) = branch(l)
        val p = MazePlay(l)
        grab(p)
        for (i in 1..at) fingerTo(p, l.solution[i])
        fingerTo(p, wrong)
        assertEquals(1, p.detours)
        p.reset()
        assertEquals(listOf(l.start), p.trail)
        assertEquals(0, p.detours)
        assertFalse(p.finished)
        assertFalse(p.holding)
        assertEquals(3, p.stars)
    }

    // ------------------------------------------------------------------ tir au hasard : la trace est toujours valide

    @Test
    fun whateverTheFingerDoesTheTrailStaysAValidPathAndNeverCrossesAWall() {
        val rnd = Random(2024)
        for (l in levels.filter { it.number % 4 == 1 }) {
            val p = MazePlay(l)
            var detourEvents = 0
            var previousDetours = 0
            repeat(1500) { step ->
                when (rnd.nextInt(10)) {
                    0 -> p.up()
                    1 -> p.down(rnd.nextDouble(-1.0, l.cols + 1.0), rnd.nextDouble(-1.0, l.rows + 1.0))
                    2 -> p.down(cx(p.head) + rnd.nextDouble(-1.0, 1.0), cy(p.head) + rnd.nextDouble(-1.0, 1.0))
                    else -> {
                        // le doigt part de la tête et dérive : tantôt près, tantôt n'importe où
                        val near = rnd.nextBoolean()
                        val x = if (near) cx(p.head) + rnd.nextDouble(-2.5, 2.5) else rnd.nextDouble(-1.0, l.cols + 1.0)
                        val y = if (near) cy(p.head) + rnd.nextDouble(-2.5, 2.5) else rnd.nextDouble(-1.0, l.rows + 1.0)
                        detourEvents += p.move(x, y, reach = rnd.nextInt(1, 10)).count { it is MazeEvent.Detour }
                    }
                }
                assertValid(p)
                assertTrue("les impasses ne baissent jamais", p.detours >= previousDetours)
                previousDetours = p.detours
                assertEquals("niveau ${l.number} pas $step : les impasses comptées ne sont pas celles signalées", detourEvents, p.detours)
                assertEquals(p.head, p.trail.last())
                if (p.finished) assertEquals(l.exit, p.head)
                if (p.finished && step % 7 == 0) p.reset().also { detourEvents = 0; previousDetours = 0 }
            }
        }
    }

    // ------------------------------------------------------------------ l'indice

    @Test
    fun onTheRightWayTheHintShowsTheNextCellsOfTheShortestWay() {
        val l = level(14)
        val p = MazePlay(l)
        grab(p)
        val h0 = p.hint()
        assertFalse(h0.back)
        assertEquals(l.solution.drop(1).take(MazePlay.HINT_CELLS), h0.cells)
        for (i in 1..3) fingerTo(p, l.solution[i])
        val h = p.hint(3)
        assertFalse(h.back)
        assertEquals(l.solution.drop(4).take(3), h.cells)
        // le dernier indice s'arrête à la sortie
        for (i in 4 until l.solution.size - 2) fingerTo(p, l.solution[i])
        assertEquals(2, p.hint().cells.size)
        assertEquals(l.exit, p.hint().cells.last())
    }

    @Test
    fun inADeadEndTheHintShowsTheWayBackToTheFork() {
        val l = level(5)
        val (at, wrong) = branch(l)
        val p = MazePlay(l)
        grab(p)
        for (i in 1..at) fingerTo(p, l.solution[i])
        fingerTo(p, wrong)
        val here = p.head
        val h = p.hint()
        assertTrue(h.back)
        assertEquals("le retour passe par l'embranchement", l.solution[at], h.cells.last())
        assertEquals("le retour commence par la case juste derrière la tête", p.trail[p.trail.size - 2], h.cells.first())
        // suivre l'indice du retour ramène sur le bon chemin sans nouvelle impasse
        for (c in h.cells) fingerTo(p, c, reach = 8)
        assertEquals(l.solution[at], p.head)
        assertEquals(1, p.detours)
        assertFalse(p.hint().back)
        assertTrue(here != p.head)
    }

    @Test
    fun aWandererWhoEntersDeadEndsThenFollowsTheHintsAlwaysFinishesWithoutAnyNewDetour() {
        val rnd = Random(99)
        for (l in levels) {
            val p = MazePlay(l)
            grab(p)
            // il erre : à chaque embranchement il choisit au hasard, jusqu'à 60 pas
            var steps = 0
            while (steps++ < 60 && !p.finished) {
                val options = l.neighbors(p.head).filter { it !in p.trail }
                if (options.isEmpty()) break
                fingerTo(p, options[rnd.nextInt(options.size)])
            }
            val detoursBefore = p.detours
            // puis il ne suit plus que les indices
            var guard = 0
            while (!p.finished && guard++ < 400) {
                val hint = p.hint(1)
                assertTrue("niveau ${l.number} : un indice vide alors que ce n'est pas fini", hint.cells.isNotEmpty())
                fingerTo(p, hint.cells.first(), reach = 8)
                assertValid(p)
            }
            assertTrue("niveau ${l.number} : les indices ne mènent pas à la sortie", p.finished)
            assertEquals("niveau ${l.number} : suivre les indices ne coûte aucune impasse", detoursBefore, p.detours)
        }
    }

    @Test
    fun theHintOnAFinishedTraceIsEmpty() {
        val l = level(1)
        val p = MazePlay(l)
        grab(p)
        for (c in l.solution.drop(1)) fingerTo(p, c)
        assertEquals(emptyList<Cell>(), p.hint().cells)
    }
}
