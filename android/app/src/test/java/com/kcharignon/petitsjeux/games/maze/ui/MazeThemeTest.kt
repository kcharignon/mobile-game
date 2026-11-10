package com.kcharignon.petitsjeux.games.maze.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevel
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevels
import com.kcharignon.petitsjeux.games.maze.engine.MazeWorlds
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class MazeThemeTest {
    private val levels: List<MazeLevel> by lazy { MazeLevels.parse(File("src/main/res/raw/maze_levels.json").readText()) }

    @Test
    fun thereIsOneThemePerWorld() {
        assertEquals(MazeWorlds.COUNT, MazeTheme.all.size)
        for (w in 0 until MazeWorlds.COUNT) assertEquals(MazeWorlds.names[w], MazeTheme.of(w).name)
        assertEquals(3, MazeTheme.all.map { it.walls.size }.distinct().single())
        // les dix décors sont bien différents les uns des autres
        assertEquals(10, MazeTheme.all.map { it.floor }.distinct().size)
        assertEquals(10, MazeTheme.all.map { it.motif }.distinct().size)
        assertEquals(MazeTheme.of(9), MazeTheme.of(40))
    }

    /** Le contour d'un mur doit contraster au moins 3:1 avec le sol, dans chaque monde et pour chaque couleur de corail : la lisibilité d'un labyrinthe ne tient pas à la couleur seule. */
    @Test
    fun everyWallOutlineContrastsAtLeastThreeToOneWithTheFloor() {
        for (t in MazeTheme.all) for (wall in t.walls) {
            val ratio = contrast(t.floor, t.wallEdge(wall))
            assertTrue("${t.name} : mur $wall, contraste ${"%.2f".format(ratio)}:1", ratio >= 3.0)
        }
    }

    @Test
    fun theTraceStandsOutFromTheFloorInEveryWorld() {
        for (t in MazeTheme.all) {
            val ratio = contrast(t.floor, t.trailEdge)
            assertTrue("${t.name} : trace, contraste ${"%.2f".format(ratio)}:1", ratio >= 3.0)
        }
    }

    @Test
    fun theOnlyDarkFloorIsTheAbyssesAndItsOutlineIsLight() {
        assertEquals(listOf("Les abysses"), MazeTheme.all.filter { it.darkFloor }.map { it.name })
        for (t in MazeTheme.all) assertEquals(t.name, luminance(t.floor) < 0.2, t.darkFloor)
        val abysses = MazeTheme.of(8)
        for (w in abysses.walls) assertTrue(luminance(abysses.wallEdge(w)) > luminance(w))
    }

    @Test
    fun contrastIsSymmetricAndBounded() {
        assertEquals(21.0, contrast(Color.Black, Color.White), 0.01)
        assertEquals(1.0, contrast(Color.White, Color.White), 0.001)
        assertEquals(contrast(Color.Red, Color.Blue), contrast(Color.Blue, Color.Red), 1e-9)
    }

    @Test
    fun theLayoutCentersTheBoardAndConvertsPixelsToCells() {
        val level = levels[13]
        val layout = MazeLayout(level, 360f, 480f, margin = 10f)
        assertTrue(layout.cell > 0f)
        assertTrue("le plateau tient en largeur", layout.originX >= 10f - 0.01f && layout.originX + level.cols * layout.cell <= 360f - 10f + 0.01f)
        assertTrue("le plateau tient en hauteur, bouée comprise", layout.originY + (level.rows + 0.8f) * layout.cell <= 480f + 0.01f)
        // le centre d'une case, converti en cases, retombe au milieu de cette case
        for (c in listOf(level.start, level.exit, level.solution[5])) {
            val (x, y) = layout.toCells(layout.center(c))
            assertEquals(c.x + 0.5, x, 1e-4)
            assertEquals(c.y + 0.5, y, 1e-4)
            assertEquals(c, layout.cellAt(layout.center(c)))
        }
        assertEquals(level.start, layout.cellAt(Offset(layout.center(level.start).x + layout.cell * 0.4f, layout.center(level.start).y - layout.cell * 0.4f)))
    }

    @Test
    fun cellsStayAtLeast26dpOnAPhoneOf360By640() {
        // largeur 360 dp ; hauteur : 640 moins la barre d'état, le bandeau, les étoiles et la barre du bas (environ 150 dp), 6 dp de marge
        for (l in levels) {
            val layout = MazeLayout(l, 360f - 28f, 490f, margin = 6f)
            assertTrue("niveau ${l.number} : cases de ${layout.cell} dp", layout.cell >= 26.5f)
        }
    }

    @Test
    fun theDecorIsNeverOnTheSolution() {
        for (l in levels) {
            val onSolution = l.solution.toSet()
            for (m in motifSpots(l)) assertFalse("niveau ${l.number} : un décor sur la solution en ${m.cell}", m.cell in onSolution)
            for (m in motifSpots(l)) assertTrue("niveau ${l.number} : un décor ailleurs qu'au fond d'une impasse", m.cell in l.deadEnds)
        }
    }

    @Test
    fun hashIsDeterministicAndBetweenZeroAndOne() {
        for (a in 0..30) for (b in 0..30) {
            val h = hash01(a, b)
            assertTrue(h in 0.0..1.0)
            assertEquals(h, hash01(a, b), 0.0)
        }
        assertTrue((0..200).map { hash01(it, 7) }.distinct().size > 150)
    }

    @Test
    fun wallShapesDrawEveryWallOnceAndHeadsOnlyAtJunctions() {
        for (l in levels.filter { it.number % 9 == 1 }) {
            val shapes = wallShapes(l, MazeTheme.of(l.world))
            var expected = 0
            for (y in 0..l.rows) for (x in 0 until l.cols) if (l.wallAbove(x, y)) expected++
            for (y in 0 until l.rows) for (x in 0..l.cols) if (l.wallLeftOf(x, y)) expected++
            assertEquals("niveau ${l.number}", expected, shapes.segs.size)
            assertTrue(shapes.blobs.size < shapes.segs.size)
            for (s in shapes.segs) assertTrue(s.color in MazeTheme.of(l.world).walls)
        }
    }

    @Test
    fun theFishFollowsTheTraceAtConstantSpeed() {
        val pts = listOf(Offset(0f, 100f), Offset(0f, 0f), Offset(100f, 0f))
        val (start, startAngle) = positionAlong(pts, 0f)
        assertEquals(pts[0], start)
        assertEquals(0f, startAngle, 0.01f) // vers le haut
        val (middle, _) = positionAlong(pts, 0.5f)
        assertEquals(Offset(0f, 0f), middle)
        val (late, lateAngle) = positionAlong(pts, 0.75f)
        assertEquals(50f, late.x, 0.01f)
        assertEquals(90f, lateAngle, 0.01f) // vers la droite
        assertEquals(pts.last(), positionAlong(pts, 1f).first)
        assertEquals(pts.last(), positionAlong(pts, 5f).first)
    }

    @Test
    fun pitchGrowsAsTheTraceGetsCloserToTheExit() {
        for (l in levels.filter { it.number % 7 == 0 }) {
            val pitches = l.solution.map { pitchOf(l, it) }
            assertEquals(0, pitches.first())
            assertEquals(7, pitches.last())
            assertEquals("niveau ${l.number} : le bloup ne baisse jamais sur le bon chemin", pitches.sorted(), pitches)
        }
    }

    @Test
    fun onlyTheFirstLevelOfTheGameAndOfEachWorldHaveAnIntroduction() {
        val withIntro = levels.filter { hasIntro(it) }.map { it.number }
        assertEquals(listOf(1, 11, 21, 31, 41, 51, 61, 71, 81, 91), withIntro)
    }
}
