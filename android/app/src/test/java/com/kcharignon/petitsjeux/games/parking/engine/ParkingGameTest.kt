package com.kcharignon.petitsjeux.games.parking.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParkingGameTest {
    // Le niveau d'exemple des maquettes (docs/design/niveau-exemple.json), 6 colonnes x 8 lignes. A..M = 0..12.
    private fun sample(): Level {
        fun car(id: Int, col: Int, row: Int, len: Int, dir: Dir) = Car(id, col, row, len, dir, color = id % LevelGenerator.COLORS)
        return Level(
            index = 27,
            cols = 6,
            rows = 8,
            cars = listOf(
                car(0, 0, 0, 2, Dir.RIGHT), // A
                car(1, 3, 0, 3, Dir.UP), // B
                car(2, 4, 1, 2, Dir.RIGHT), // C
                car(3, 2, 1, 2, Dir.DOWN), // D
                car(4, 0, 3, 3, Dir.LEFT), // E
                car(5, 5, 2, 2, Dir.DOWN), // F
                car(6, 3, 3, 2, Dir.RIGHT), // G
                car(7, 1, 4, 2, Dir.UP), // H
                car(8, 2, 5, 2, Dir.LEFT), // I
                car(9, 4, 4, 3, Dir.DOWN), // J
                car(10, 0, 7, 2, Dir.RIGHT), // K
                car(11, 2, 6, 2, Dir.RIGHT), // L
                car(12, 0, 5, 2, Dir.UP), // M
            ),
        )
    }

    private val A = 0
    private val B = 1
    private val C = 2
    private val G = 6
    private val F = 5
    private val H = 7

    @Test
    fun headAndPathFollowTheNose() {
        val right = Car(0, 1, 2, 2, Dir.RIGHT, 0)
        assertEquals(Cell(2, 2), right.head)
        assertEquals(listOf(Cell(3, 2), Cell(4, 2)), right.path(cols = 5, rows = 4))

        val left = Car(0, 1, 2, 2, Dir.LEFT, 0)
        assertEquals(Cell(1, 2), left.head)
        assertEquals(listOf(Cell(0, 2)), left.path(5, 4))

        val up = Car(0, 3, 1, 3, Dir.UP, 0)
        assertEquals(Cell(3, 1), up.head)
        assertEquals(listOf(Cell(3, 0)), up.path(5, 6))

        val down = Car(0, 3, 1, 3, Dir.DOWN, 0)
        assertEquals(Cell(3, 3), down.head)
        assertEquals(listOf(Cell(3, 4), Cell(3, 5)), down.path(5, 6))
    }

    @Test
    fun aCarAtTheEdgeFacingOutHasAnEmptyPath() {
        assertTrue(Car(0, 0, 0, 2, Dir.UP, 0).path(4, 4).isEmpty())
        assertTrue(Car(0, 2, 1, 2, Dir.RIGHT, 0).path(4, 4).isEmpty())
    }

    @Test
    fun levelRefusesOverlappingOrOutOfBoundsCars() {
        val a = Car(0, 0, 0, 2, Dir.RIGHT, 0)
        val overlapping = Car(1, 1, 0, 2, Dir.DOWN, 0)
        assertTrue(runCatching { Level(1, 3, 3, listOf(a, overlapping)) }.isFailure)
        val outside = Car(1, 2, 0, 2, Dir.RIGHT, 0)
        assertTrue(runCatching { Level(1, 3, 3, listOf(outside)) }.isFailure)
    }

    @Test
    fun aFreeCarExitsAndLeavesTheBoard() {
        val game = ParkingGame(sample())
        val result = game.tap(B)
        assertTrue(result is TapResult.Exited)
        assertEquals(0, (result as TapResult.Exited).pathLength)
        assertFalse(game.isOnBoard(B))
        assertEquals(12, game.carsLeft)
        assertEquals(0, game.bumps)
        assertEquals(1, game.exited)
    }

    @Test
    fun aBlockedCarBumpsAndStaysAndCostsAFault() {
        val game = ParkingGame(sample())
        // G (nez à droite, en (3,3)-(4,3)) est bloquée par F en (5,3) : aucune case libre devant.
        val result = game.tap(G)
        assertTrue(result is TapResult.Bumped)
        result as TapResult.Bumped
        assertEquals(F, result.blocker.id)
        assertEquals(0, result.freeCells)
        assertTrue(game.isOnBoard(G))
        assertEquals(1, game.bumps)
        assertEquals(13, game.carsLeft)
    }

    @Test
    fun aBumpedCarFirstDrivesUpToTheBlocker() {
        // H monte en colonne 1 depuis (1,4) : (1,3) est occupée par E -> aucune case libre ; A est plus loin.
        val game = ParkingGame(sample())
        val bump = game.tap(H) as TapResult.Bumped
        assertEquals(4, bump.blocker.id)
        assertEquals(0, bump.freeCells)

        // Une voiture avec 2 cases vides devant elle avant la voiture qui bloque : la 0 roule vers la droite, la 1 occupe (4,0)-(5,0).
        val level = Level(1, 6, 1, listOf(Car(0, 0, 0, 2, Dir.RIGHT, 0), Car(1, 4, 0, 2, Dir.LEFT, 1)))
        val result = ParkingGame(level).tap(0) as TapResult.Bumped
        assertEquals(2, result.freeCells)
        assertEquals(1, result.blocker.id)
    }

    @Test
    fun anExitedCarCanNotBeTappedAgain() {
        val game = ParkingGame(sample())
        game.tap(B)
        assertEquals(TapResult.Ignored, game.tap(B))
        assertEquals(0, game.bumps)
    }

    @Test
    fun removingACarFreesTheCarsWaitingForIt() {
        val game = ParkingGame(sample())
        assertTrue(game.tap(A) is TapResult.Bumped) // A attend B (B en (3,0))
        assertTrue(game.tap(B) is TapResult.Exited)
        assertTrue(game.tap(A) is TapResult.Exited)
        assertEquals(1, game.bumps)
    }

    @Test
    fun theHintIsAFreeCarAndFollowingItAlwaysWins() {
        val game = ParkingGame(sample())
        var guard = 0
        while (!game.isWon) {
            val hint = game.hint()
            assertNotNull(hint)
            assertNull("l'indice doit être une voiture libre", game.blockerOf(hint!!))
            assertTrue(game.tap(hint.id) is TapResult.Exited)
            assertTrue(++guard <= 13)
        }
        assertEquals(0, game.bumps)
        assertEquals(3, game.stars)
        assertNull(game.hint())
    }

    @Test
    fun hintPrefersTheCarThatUnblocksTheMost() {
        val game = ParkingGame(sample())
        // B (minibus en haut) bloque A ; C, F, E, J, K sont libres aussi. B et E débloquent le plus : le plus petit numéro l'emporte.
        val hint = game.hint()!!
        assertTrue(hint.id == B || hint.id == 4)
    }

    @Test
    fun starsDependOnBumps() {
        assertEquals(3, starsForBumps(0))
        assertEquals(2, starsForBumps(1))
        assertEquals(2, starsForBumps(2))
        assertEquals(1, starsForBumps(3))
        assertEquals(1, starsForBumps(40))
    }
}
