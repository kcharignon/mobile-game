package com.kcharignon.petitsjeux.games.parking.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParkingGameTest {
    private fun car(id: Int, col: Int, row: Int, len: Int, dir: Dir, carrier: Boolean = false) = Car(id, col, row, len, dir, id % LevelGenerator.COLORS, carrier)

    @Test
    fun leadAndPathFollowTheDirectionOfTheMove() {
        val right = car(0, 1, 2, 2, Dir.RIGHT)
        assertEquals(Cell(2, 2), right.lead(Move.FORWARD))
        assertEquals(Cell(1, 2), right.lead(Move.BACKWARD))
        assertEquals(listOf(Cell(3, 2), Cell(4, 2)), right.path(5, 4, Move.FORWARD))
        assertEquals(listOf(Cell(0, 2)), right.path(5, 4, Move.BACKWARD))

        val up = car(0, 3, 1, 3, Dir.UP)
        assertEquals(Cell(3, 1), up.head)
        assertEquals(listOf(Cell(3, 0)), up.path(5, 6, Move.FORWARD))
        assertEquals(listOf(Cell(3, 4), Cell(3, 5)), up.path(5, 6, Move.BACKWARD))
    }

    @Test
    fun theTrailIsTheCellBehindTheVehicleInTheDirectionItMoves() {
        val down = car(0, 1, 1, 3, Dir.DOWN)
        assertEquals(Cell(1, 1), down.trail(Move.FORWARD)) // roule vers le bas : sa queue est en haut
        assertEquals(Cell(1, 3), down.trail(Move.BACKWARD)) // recule vers le haut : l'arrière du mouvement est en bas
    }

    @Test
    fun aVehicleBlockedAheadCanLeaveBackwards() {
        val level = Level(1, 5, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.RIGHT)))
        val game = ParkingGame(level)
        val ahead = game.tap(0, Move.FORWARD)
        assertTrue(ahead is TapResult.Bumped)
        ahead as TapResult.Bumped
        assertEquals(1, ahead.freeCells)
        assertEquals(1, (ahead.blocker as Blocker.Vehicle).car.id)
        assertEquals(1, game.bumps)

        val behind = game.tap(0, Move.BACKWARD) as TapResult.Exited
        assertEquals(Move.BACKWARD, behind.move)
        assertEquals(0, behind.pathLength)
        assertFalse(game.isOnBoard(0))
    }

    @Test
    fun aSimpleTapMeansForward() {
        val level = Level(1, 5, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT)))
        val result = ParkingGame(level).tap(0) as TapResult.Exited
        assertEquals(Move.FORWARD, result.move)
        assertEquals(3, result.pathLength)
    }

    @Test
    fun aConeBlocksTheRoadForGood() {
        val level = Level(61, 5, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT)), cones = listOf(Cell(3, 0)))
        val game = ParkingGame(level)
        val bump = game.tap(0, Move.FORWARD) as TapResult.Bumped
        assertEquals(Blocker.Obstacle(Cell(3, 0), ObstacleKind.CONE), bump.blocker)
        assertEquals(1, bump.freeCells)
        assertTrue(game.tap(0, Move.BACKWARD) is TapResult.Exited)
    }

    @Test
    fun aVehicleBoxedInByConesOnBothSidesIsUnsolvable() {
        val level = Level(1, 5, 1, listOf(car(0, 1, 0, 2, Dir.RIGHT)), cones = listOf(Cell(0, 0), Cell(3, 0)))
        assertFalse(Solver.canFinish(level))
        assertFalse(Solver.analyse(level).solvable)
    }

    @Test
    fun levelsRefuseOverlapsOutOfBoundsAndMisplacedCrates() {
        val a = car(0, 0, 0, 2, Dir.RIGHT)
        assertTrue(runCatching { Level(1, 3, 3, listOf(a, car(1, 1, 0, 2, Dir.DOWN))) }.isFailure)
        assertTrue(runCatching { Level(1, 3, 3, listOf(car(1, 2, 0, 2, Dir.RIGHT))) }.isFailure)
        assertTrue(runCatching { Level(1, 3, 3, listOf(a), cones = listOf(Cell(1, 0))) }.isFailure)
        assertTrue(runCatching { Level(1, 3, 3, listOf(a), cones = listOf(Cell(5, 5))) }.isFailure)
        assertTrue(runCatching { car(2, 0, 1, 2, Dir.RIGHT, carrier = true) }.isFailure) // seul un camion de 3 cases porte une caisse
    }

    @Test
    fun anExitedVehicleCanNotBeTappedAgain() {
        val game = ParkingGame(Level(1, 4, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT))))
        game.tap(0)
        assertEquals(TapResult.Ignored, game.tap(0))
        assertEquals(0, game.bumps)
        assertTrue(game.isWon)
    }

    // ---- les camions qui laissent tomber une caisse

    @Test
    fun aPickupDropsItsCrateOnTheCellItsTailOccupied() {
        val level = Level(181, 5, 3, listOf(car(0, 0, 0, 3, Dir.RIGHT, carrier = true)))
        val forward = ParkingGame(level).tap(0, Move.FORWARD) as TapResult.Exited
        assertEquals(Cell(0, 0), forward.dropped) // roule vers la droite : la queue était à gauche
        val backward = ParkingGame(level).tap(0, Move.BACKWARD) as TapResult.Exited
        assertEquals(Cell(2, 0), backward.dropped) // recule vers la gauche : l'arrière du mouvement est à droite
    }

    @Test
    fun theSameMoveAlwaysDropsTheCrateOnTheSameCell() {
        val level = Level(181, 5, 5, listOf(car(0, 1, 1, 3, Dir.DOWN, carrier = true)))
        repeat(3) { assertEquals(Cell(1, 1), (ParkingGame(level).tap(0, Move.FORWARD) as TapResult.Exited).dropped) }
    }

    @Test
    fun otherVehiclesDropNothing() {
        val level = Level(1, 5, 3, listOf(car(0, 0, 0, 3, Dir.RIGHT)))
        val result = ParkingGame(level).tap(0) as TapResult.Exited
        assertNull(result.dropped)
    }

    @Test
    fun aFallenCrateBlocksTheCellForGood() {
        // le pick-up part vers la droite et laisse sa caisse en (0,0) ; le véhicule vertical de la colonne 0 la rencontre en montant
        val level = Level(
            181, 5, 3,
            listOf(car(0, 0, 0, 3, Dir.RIGHT, carrier = true), car(1, 0, 1, 2, Dir.UP)),
        )
        val game = ParkingGame(level)
        assertTrue(game.tap(0, Move.FORWARD) is TapResult.Exited)
        assertEquals(setOf(Cell(0, 0)), game.crates)
        val bump = game.tap(1, Move.FORWARD) as TapResult.Bumped
        assertEquals(Blocker.Obstacle(Cell(0, 0), ObstacleKind.CRATE), bump.blocker)
        assertEquals(0, bump.freeCells)
    }

    // Un piège : 3 colonnes x 4 lignes, un plot en (0,3). Le véhicule vertical W (colonne 0, lignes 1-2) ne peut sortir que par le haut,
    // où se trouve le pick-up. Si le pick-up part vers la DROITE, sa caisse tombe en (0,0) et ferme le haut : plus personne ne sort.
    // S'il part vers la GAUCHE, sa caisse tombe en (2,0) : tout va bien.
    private val trap = Level(
        181, 3, 4,
        listOf(car(0, 0, 0, 3, Dir.RIGHT, carrier = true), car(1, 0, 1, 2, Dir.UP)),
        cones = listOf(Cell(0, 3)),
    )

    @Test
    fun theSolverSeesTheTrap() {
        val a = Solver.analyse(trap)
        assertTrue(a.solvable)
        assertEquals(1, a.traps)
    }

    @Test
    fun theHintNeverLeadsIntoTheTrap() {
        val hint = ParkingGame(trap).hint()!!
        assertEquals(0, hint.car.id)
        assertEquals(Move.BACKWARD, hint.move)
    }

    @Test
    fun walkingIntoTheTrapIsDetectedAsStuck() {
        val game = ParkingGame(trap)
        assertFalse(game.isStuck)
        game.tap(0, Move.FORWARD)
        assertTrue(game.isStuck)
        assertNull(game.hint())
    }

    @Test
    fun theRightWayThroughTheTrapWins() {
        val game = ParkingGame(trap)
        game.tap(0, Move.BACKWARD)
        assertFalse(game.isStuck)
        assertTrue(game.tap(1, Move.FORWARD) is TapResult.Exited)
        assertTrue(game.isWon)
        assertEquals(0, game.bumps)
    }

    @Test
    fun followingTheHintsAlwaysWinsWithoutAFault() {
        for (level in listOf(trap, Level(1, 5, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.RIGHT))))) {
            val game = ParkingGame(level)
            var guard = 0
            while (!game.isWon) {
                val hint = game.hint()!!
                assertTrue(game.tap(hint.car.id, hint.move) is TapResult.Exited)
                assertTrue(++guard <= level.cars.size)
            }
            assertEquals(0, game.bumps)
            assertEquals(3, game.stars)
            assertNull(game.hint())
        }
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

class SolverTest {
    private fun car(id: Int, col: Int, row: Int, len: Int, dir: Dir) = Car(id, col, row, len, dir, 0)

    @Test
    fun twoVehiclesFacingEachOtherBetweenConesAreADeadlock() {
        // 6 x 1 : plot, A, B, plot... A ne peut ni avancer (B) ni reculer (plot) ; B non plus
        val level = Level(1, 6, 1, listOf(car(0, 1, 0, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.RIGHT)), cones = listOf(Cell(0, 0), Cell(5, 0)))
        assertFalse(Solver.canFinish(level))
    }

    @Test
    fun theAnalysisCountsWavesFreeVehiclesAndBackwardOnlyOnes() {
        // A est bloquée devant par B mais peut reculer librement : libres au départ = A (arrière) et B (avant), dont 1 seule « en arrière seulement »
        val level = Level(1, 6, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.RIGHT)))
        val a = Solver.analyse(level)
        assertTrue(a.solvable)
        assertEquals(2, a.freeAtStart)
        assertEquals(1, a.backwardOnly)
        assertEquals(1, a.depth)
        assertEquals(0, a.traps)
    }

    @Test
    fun waitingForAnotherVehicleAddsAWave() {
        // 6 x 1 avec un plot en (0,0) : A (1-2) est coincée à l'arrière par le plot et devant par B (3-4) ; B sort en avant ; puis A
        val level = Level(1, 6, 1, listOf(car(0, 1, 0, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.RIGHT)), cones = listOf(Cell(0, 0)))
        val a = Solver.analyse(level)
        assertTrue(a.solvable)
        assertEquals(2, a.depth)
        assertEquals(1, a.freeAtStart)
    }

    @Test
    fun freeMovesListsTheOpenDirections() {
        val cars = listOf(car(0, 0, 0, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.RIGHT))
        assertEquals(listOf(Move.BACKWARD), Solver.freeMoves(6, 1, emptyList(), cars, cars[0]))
        assertEquals(listOf(Move.FORWARD), Solver.freeMoves(6, 1, emptyList(), cars, cars[1]))
    }
}
