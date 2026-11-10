package com.kcharignon.petitsjeux.games.maze.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File
import java.security.MessageDigest

/**
 * Le fichier de niveaux tel qu'il est livré (`res/raw/maze_levels.json`, fabriqué par `tools/mazes/make-mazes.js`) : ce test ne fait **pas confiance à l'outil**,
 * il relit le fichier et vérifie tout ce qui compte pour le joueur. Et il fige les niveaux publiés (empreinte de chaque monde).
 */
class MazeLevelsDataTest {
    private val levels: List<MazeLevel> by lazy { MazeLevels.parse(File("src/main/res/raw/maze_levels.json").readText()) }

    private val sizes = listOf(4 to 5, 5 to 7, 6 to 8, 7 to 9, 8 to 11, 9 to 12, 10 to 13, 10 to 14, 11 to 15, 11 to 16)
    private val solutionBands = listOf(8..10, 12..14, 15..18, 19..24, 27..33, 34..42, 44..52, 49..58, 55..66, 64..78)
    private val decisionBands = listOf(1..2, 2..3, 3..5, 5..7, 7..10, 10..13, 13..16, 15..18, 18..22, 21..25)
    private val centerExits = setOf(73, 76, 79)

    private fun inWorld(w: Int) = levels.filter { it.world == w }

    @Test
    fun hundredLevelsInTenWorldsOfTen() {
        assertEquals(MazeWorlds.LEVEL_COUNT, levels.size)
        assertEquals(100, levels.size)
        for ((i, l) in levels.withIndex()) {
            assertEquals(i + 1, l.number)
            assertEquals(MazeWorlds.of(l.number), l.world)
        }
        for (w in 0 until MazeWorlds.COUNT) assertEquals(MazeWorlds.LEVELS_PER_WORLD, inWorld(w).size)
        assertEquals(MazeWorlds.COUNT, MazeWorlds.names.size)
        assertEquals(MazeWorlds.COUNT, MazeWorlds.news.size)
    }

    @Test
    fun gridsGrowFromWorldToWorld() {
        for (l in levels) assertEquals("niveau ${l.number}", sizes[l.world], l.cols to l.rows)
        for (w in 1 until MazeWorlds.COUNT) assertTrue(sizes[w].first * sizes[w].second > sizes[w - 1].first * sizes[w - 1].second)
    }

    @Test
    fun everyLevelIsConnectedWithAnEntranceAtTheBottom() {
        for (l in levels) {
            // le fichier est relu par `parse`, qui rejette un labyrinthe non relié ; on revérifie la solution
            assertEquals(l.start, l.solution.first())
            assertEquals(l.exit, l.solution.last())
            assertTrue("niveau ${l.number} : solution trop courte", l.solution.size >= 5)
            for (i in 1 until l.solution.size) assertTrue(l.isOpen(l.solution[i - 1], l.solution[i]))
            assertEquals(l.rows - 1, l.start.y)
            assertFalse("niveau ${l.number} : entrée fermée", l.wallAbove(l.start.x, l.rows))
        }
    }

    @Test
    fun theExitIsAnOpeningOnTheTopBorderExceptInThreeCenterLevels() {
        for (l in levels) {
            if (l.number in centerExits) {
                assertFalse("niveau ${l.number} : sortie au centre attendue", l.exitOnBorder)
                assertTrue("niveau ${l.number} : sortie au centre mais sur le bord", l.exit.x in 1 until l.cols - 1 && l.exit.y in 1 until l.rows - 1)
            } else {
                assertTrue("niveau ${l.number} : la sortie doit s'ouvrir sur le bord du haut", l.exitOnBorder)
            }
        }
    }

    @Test
    fun worldsOneToFiveArePerfectAndLaterWorldsHaveLoops() {
        for (l in inWorld(0) + inWorld(1) + inWorld(2) + inWorld(3) + inWorld(4)) {
            assertEquals("niveau ${l.number} : un labyrinthe parfait a une case de plus que de passages", l.cols * l.rows - 1, l.passages)
        }
        for (w in 5 until MazeWorlds.COUNT) {
            val ls = inWorld(w)
            for (l in ls) assertTrue("niveau ${l.number}", l.passages >= l.cols * l.rows - 1)
            val withLoops = ls.count { it.passages > it.cols * it.rows - 1 }
            assertTrue("monde ${w + 1} : au moins 8 niveaux sur 10 ont une boucle (a $withLoops)", withLoops >= 8)
        }
    }

    @Test
    fun solutionLengthAndDecisionsStayInTheBandOfTheirWorld() {
        for (l in levels) {
            val sol = solutionBands[l.world]
            val dec = decisionBands[l.world]
            assertTrue("niveau ${l.number} : solution de ${l.solution.size} cases, bande $sol", l.solution.size >= sol.first * 3 / 4 && l.solution.size <= sol.last * 5 / 4)
            assertTrue("niveau ${l.number} : ${l.decisions} embranchements, bande $dec", l.decisions >= dec.first - 3 && l.decisions <= dec.last + 3)
        }
    }

    @Test
    fun difficultyNeverDropsFromAWorldToTheNext() {
        var previousDecisions = -1.0
        var previousLength = -1.0
        for (w in 0 until MazeWorlds.COUNT) {
            val ls = inWorld(w)
            val decisions = ls.sumOf { it.decisions }.toDouble() / ls.size
            val length = ls.sumOf { it.solution.size }.toDouble() / ls.size
            assertTrue("le monde ${w + 1} est plus simple que le précédent ($decisions embranchements contre $previousDecisions)", decisions >= previousDecisions)
            assertTrue("le monde ${w + 1} a une solution plus courte que le précédent ($length contre $previousLength)", length >= previousLength)
            previousDecisions = decisions
            previousLength = length
        }
        // et dans un monde, la fin est plus difficile que le début
        for (w in 0 until MazeWorlds.COUNT) {
            val ls = inWorld(w)
            val early = ls.take(3).sumOf { it.decisions }
            val late = ls.takeLast(3).sumOf { it.decisions }
            assertTrue("monde ${w + 1} : les 3 derniers niveaux ($late) ne sont pas plus durs que les 3 premiers ($early)", late >= early)
        }
    }

    @Test
    fun noSolutionIsMostlyOneStraightLine() {
        for (l in levels) {
            var run = 1
            var longest = 1
            for (i in 1 until l.solution.size - 1) {
                val a = l.solution[i - 1]
                val b = l.solution[i]
                val c = l.solution[i + 1]
                if (b.x - a.x != c.x - b.x || b.y - a.y != c.y - b.y) run = 1 else { run++; longest = maxOf(longest, run) }
            }
            assertTrue("niveau ${l.number} : une ligne droite de $longest cases sur ${l.solution.size}", longest.toDouble() / l.solution.size <= 0.75)
        }
    }

    @Test
    fun noLevelIsTwiceInTheFileNorTheMirrorOfAnother() {
        val seen = HashMap<String, Int>()
        for (l in levels) {
            val key = l.grid.joinToString("/") + "|" + l.start + "|" + l.exit
            assertEquals("niveau ${l.number} est le même que ${seen[key]}", null, seen.put(key, l.number))
        }
        // le miroir gauche-droite : sans les symboles de bord, on compare les ensembles de passages
        val passages = HashMap<String, Int>()
        for (l in levels) {
            val opens = HashSet<String>()
            val mirrored = HashSet<String>()
            for (y in 0 until l.rows) for (x in 0 until l.cols) {
                val c = Cell(x, y)
                for (n in l.neighbors(c)) if (n.x > x || n.y > y) {
                    opens += "${c.x},${c.y}-${n.x},${n.y}"
                    mirrored += listOf(Cell(l.cols - 1 - c.x, c.y), Cell(l.cols - 1 - n.x, n.y)).sortedWith(compareBy({ it.y }, { it.x })).joinToString("-") { "${it.x},${it.y}" }
                }
            }
            val a = "${l.cols}x${l.rows}:${opens.sorted()}"
            val b = "${l.cols}x${l.rows}:${mirrored.sorted()}"
            assertEquals("niveau ${l.number} ressemble à ${passages[a]}", null, passages[a])
            assertEquals("niveau ${l.number} est le miroir de ${passages[b]}", null, passages[b])
            passages[a] = l.number
        }
    }

    @Test
    fun cellsStayBigEnoughOnA360dpPhone() {
        for (l in levels) {
            // largeur 360 dp, 16 dp de marge de chaque côté
            val cell = (360 - 32).toDouble() / l.cols
            assertTrue("niveau ${l.number} : cases de ${"%.1f".format(cell)} dp", cell >= 28.0)
            // et la hauteur : 640 dp moins le bandeau du haut et les boutons du bas
            assertTrue("niveau ${l.number} : ${l.rows} lignes ne tiennent pas", minOf(cell, 450.0 / l.rows) >= 28.0)
        }
    }

    /** Les niveaux 1 à 100 sont publiés (0.9.0) : les étoiles des joueurs y sont attachées, ils ne changent plus. Pour en ajouter, on complète le fichier, jamais on ne le modifie. */
    @Test
    fun publishedLevelsNeverChange() {
        val expected = listOf(
            "f2d3162dcc5b836f0dbc8f4690daa97c2656ad92888fd12f6fc18079a639a831",
            "8387cd2e3fe24bb49413d16ced057bc552e2bfc0b34ad760425d9ff9a6fbd657",
            "ae81cf0e22f05ffd2f33c5bd72f773964e7d96a2a998dec251cf99514f75b530",
            "83558a601e589c92725becef83ec31983619dfe35b5e81b94cdb730d5a403ecf",
            "57ebb831c97f2b7df52978fb8b5ee97eaec4d22f517f6e1506109a218211eb42",
            "cceef65a3557186925a58a54ffa7bf75391e6e02fd1a60c36ff598a057b63907",
            "086f09a1f9abc44e004b752ad5196c8471429651c8ac19e3baaa575e489c1c9d",
            "82bc3116ee90801ecc2ad3f8f4b453411c32d55ccef1e5fb8810588bd4cf0ec8",
            "e2e6a0e1268545f4a48f84d2dce8b3170e131eeb719641eeefc27435af9da628",
            "d66f538f8773cc1d1dbfbd66d325d035fe70d60a859b68daaacdaa92047ca164",
        )
        for (w in 0 until MazeWorlds.COUNT) {
            val text = inWorld(w).joinToString("\n") { "${it.number}|${it.start.x},${it.start.y}|${it.exit.x},${it.exit.y}|${it.grid.joinToString("/")}" }
            val digest = MessageDigest.getInstance("SHA-256").digest(text.toByteArray()).joinToString("") { "%02x".format(it) }
            assertEquals("le monde ${w + 1} (niveaux ${MazeWorlds.firstLevel(w)} à ${MazeWorlds.firstLevel(w) + 9}) a changé : des étoiles de joueurs y sont attachées", expected[w], digest)
        }
    }

    // ------------------------------------------------------------------ l'analyse du fichier rejette ce qui est incohérent

    private val good = listOf(
        "+--+  +--+--+",
        "|     |     |",
        "+  +  +  +  +",
        "|  |  |  |  |",
        "+  +  +  +--+",
        "|  |  |  |  |",
        "+  +  +  +  +",
        "|  |  |  |  |",
        "+  +--+  +  +",
        "|           |",
        "+--+--+--+  +",
    )

    private fun level(grid: List<String>, start: Cell = Cell(3, 4), exit: Cell = Cell(1, 0)) = MazeLevels.level(1, 0, 4, 5, start, exit, grid)

    private fun rejects(why: String, block: () -> Unit) {
        try {
            block()
            fail("aurait dû être rejeté : $why")
        } catch (_: IllegalArgumentException) {
            // attendu
        }
    }

    @Test
    fun theParserAcceptsAWellFormedLevel() {
        val l = level(good)
        assertEquals(9, l.solution.size)
        assertEquals(1, l.decisions)
        assertTrue(l.exitOnBorder)
    }

    @Test
    fun theParserRejectsABadGrid() {
        rejects("ligne manquante") { level(good.dropLast(1)) }
        rejects("ligne trop courte") { level(good.mapIndexed { i, s -> if (i == 3) s.dropLast(1) else s }) }
        rejects("caractère inconnu") { level(good.mapIndexed { i, s -> if (i == 2) s.replaceFirst("  +", "x +") else s }) }
        rejects("coin absent") { level(good.mapIndexed { i, s -> if (i == 0) s.replaceFirst('+', '-') else s }) }
        rejects("case non vide") { level(good.mapIndexed { i, s -> if (i == 1) s.replaceFirst("|  ", "| x") else s }) }
        rejects("bord gauche ouvert") { level(good.mapIndexed { i, s -> if (i == 3) " " + s.drop(1) else s }) }
    }

    @Test
    fun theParserRejectsBadStartsAndExits() {
        rejects("départ hors de la grille") { level(good, start = Cell(9, 4)) }
        rejects("départ pas en bas") { level(good, start = Cell(3, 2)) }
        rejects("sortie hors de la grille") { level(good, exit = Cell(1, -1)) }
        rejects("départ = sortie") { level(good, exit = Cell(3, 4)) }
        rejects("entrée fermée") { level(good.mapIndexed { i, s -> if (i == 10) "+--+--+--+--+" else s }) }
        rejects("deux ouvertures dans le bord du haut") { level(good.mapIndexed { i, s -> if (i == 0) "+--+  +  +--+" else s }) }
    }

    @Test
    fun theParserRejectsADisconnectedMaze() {
        // la case (3, 0), en haut à droite, entourée de murs : un îlot que le poisson ne peut jamais atteindre
        val islands = good.mapIndexed { i, s ->
            when (i) {
                1 -> "|     |  |  |"
                2 -> "+  +  +  +--+"
                else -> s
            }
        }
        rejects("îlot") { level(islands) }
    }

    @Test
    fun theFileRejectsLevelsOutOfOrder() {
        val text = File("src/main/res/raw/maze_levels.json").readText().replaceFirst("\"n\": 2,", "\"n\": 3,")
        rejects("numéros qui ne se suivent pas") { MazeLevels.parse(text) }
        rejects("format inconnu") { MazeLevels.parse(File("src/main/res/raw/maze_levels.json").readText().replaceFirst("\"format\": 1", "\"format\": 2")) }
    }
}
