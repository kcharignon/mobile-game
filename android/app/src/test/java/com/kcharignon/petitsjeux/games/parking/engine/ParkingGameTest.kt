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
        game.finishMotion(0) // l'animation du rebond est finie

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
        game.finishMotion(0)
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
        game.finishMotion(0) // le pick-up a fini de partir : sa caisse reste
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
        game.finishMotion(0)
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
                game.finishMotion(hint.car.id)
                assertTrue(++guard <= level.cars.size)
            }
            assertEquals(0, game.bumps)
            assertEquals(3, game.stars)
            assertNull(game.hint())
        }
    }

    // ---- les véhicules qui roulent gardent leurs collisions

    // 6 colonnes x 4 lignes : A (ligne 1, colonnes 0-1) sort vers la droite en traversant la colonne 3 ; B (colonne 3, lignes 2-3) monte et croise la file de A en (3,1).
    private val crossing = Level(
        1, 6, 4,
        listOf(car(0, 0, 1, 2, Dir.RIGHT), car(1, 3, 2, 2, Dir.UP)),
    )

    private fun crossingGame(offsets: MutableMap<Int, Float>) = ParkingGame(crossing).also { g -> g.motionSource = MotionSource { offsets[it] } }

    @Test
    fun aVehicleSentOntoARollingOneCollidesWithIt() {
        val game = crossingGame(mutableMapOf())
        assertTrue(game.tap(0) is TapResult.Exited) // A part : elle barre sa file jusqu'au bord
        val bump = game.tap(1) as TapResult.Bumped
        assertEquals(0, bump.freeCells)
        assertEquals(0, (bump.blocker as Blocker.Vehicle).car.id)
        assertEquals(1, game.bumps)
    }

    @Test
    fun aRollingVehicleFreesTheCellsItsTailHasPassed() {
        val offsets = mutableMapOf<Int, Float>()
        val game = crossingGame(offsets)
        game.tap(0)
        offsets[0] = 2f
        assertFalse(game.canMove(crossing.cars[1], Move.FORWARD)) // sa queue est en colonne 2 : (3,1) est encore devant elle
        offsets[0] = 3.8f
        assertFalse(game.canMove(crossing.cars[1], Move.FORWARD)) // sa queue est à 3,8 : elle recouvre encore la colonne 3
        offsets[0] = 3.95f
        assertTrue(game.canMove(crossing.cars[1], Move.FORWARD)) // sa queue a dépassé la colonne 3
        assertTrue(game.tap(1) is TapResult.Exited)
        assertEquals(0, game.bumps)
    }

    @Test
    fun aVehicleThatHasLeftTheBoardBlocksNothing() {
        val offsets = mutableMapOf<Int, Float>()
        val game = crossingGame(offsets)
        game.tap(0)
        offsets[0] = 9f
        assertTrue(game.canMove(crossing.cars[1], Move.FORWARD))
    }

    @Test
    fun finishingTheMotionFreesTheCorridor() {
        val game = crossingGame(mutableMapOf())
        game.tap(0)
        assertTrue(game.isMoving)
        game.finishMotion(0)
        assertFalse(game.isMoving)
        assertTrue(game.tap(1) is TapResult.Exited)
    }

    @Test
    fun aBackwardVehicleAlsoBarsItsCorridor() {
        val level = Level(1, 6, 4, listOf(car(0, 4, 1, 2, Dir.RIGHT), car(1, 2, 2, 2, Dir.UP)))
        val offsets = mutableMapOf<Int, Float>()
        val game = ParkingGame(level).also { it.motionSource = MotionSource { id -> offsets[id] } }
        assertTrue(game.tap(0, Move.BACKWARD) is TapResult.Exited) // recule vers la gauche, en traversant la colonne 2
        offsets[0] = -1f
        assertTrue(game.tap(1, Move.FORWARD) is TapResult.Bumped)
    }

    // ---- deux véhicules l'un derrière l'autre sur la même file

    @Test
    fun aVehicleFollowingAnExitingOneOnTheSameLaneDoesNotCollideWithIt() {
        val level = Level(1, 8, 1, listOf(car(0, 2, 0, 2, Dir.RIGHT), car(1, 0, 0, 2, Dir.RIGHT)))
        for (traveled in listOf(0f, 0.3f, 1f, 2.5f, 4f)) {
            val offsets = mutableMapOf<Int, Float>()
            val game = ParkingGame(level).also { it.motionSource = MotionSource { id -> offsets[id] } }
            assertTrue(game.tap(0) is TapResult.Exited)
            offsets[0] = traveled
            // B part juste derrière A, dans le même sens : pas de collision, A s'éloigne
            assertNull("A a parcouru $traveled : B n'est pas gêné", game.blockerOf(level.cars[1], Move.FORWARD))
            assertTrue(game.tap(1) is TapResult.Exited)
            assertEquals(0, game.bumps)
        }
    }

    @Test
    fun threeVehiclesInARowCanAllBeSentOneAfterTheOther() {
        val level = Level(1, 9, 1, listOf(car(0, 6, 0, 2, Dir.RIGHT), car(1, 3, 0, 3, Dir.RIGHT), car(2, 0, 0, 3, Dir.RIGHT)))
        val offsets = mutableMapOf<Int, Float>()
        val game = ParkingGame(level).also { it.motionSource = MotionSource { id -> offsets[id] } }
        assertTrue(game.tap(0) is TapResult.Exited)
        assertTrue(game.tap(1) is TapResult.Exited)
        assertTrue(game.tap(2) is TapResult.Exited)
        assertEquals(0, game.bumps)
        assertTrue(game.isWon)
    }

    @Test
    fun followingWorksInReverseAndOnColumnsToo() {
        // en marche arrière, vers la gauche
        val row = Level(1, 8, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.RIGHT)))
        val game = ParkingGame(row)
        assertTrue(game.tap(0, Move.BACKWARD) is TapResult.Exited)
        assertTrue(game.tap(1, Move.BACKWARD) is TapResult.Exited)
        assertEquals(0, game.bumps)
        // sur une colonne, vers le bas
        val column = Level(1, 1, 8, listOf(car(0, 0, 5, 2, Dir.DOWN), car(1, 0, 2, 2, Dir.DOWN)))
        val down = ParkingGame(column)
        assertTrue(down.tap(0) is TapResult.Exited)
        assertTrue(down.tap(1) is TapResult.Exited)
        assertEquals(0, down.bumps)
    }

    @Test
    fun aBouncingVehicleStillBlocksTheOneBehindIt() {
        // celui qui fonce sur un plot puis revient : il ne s'éloigne pas, il revient vers celui qui le suit
        val level = Level(1, 8, 1, listOf(car(0, 2, 0, 2, Dir.RIGHT), car(1, 0, 0, 2, Dir.RIGHT)), cones = listOf(Cell(6, 0)))
        val game = ParkingGame(level)
        assertTrue(game.tap(0) is TapResult.Bumped)
        val behind = game.tap(1) as TapResult.Bumped
        assertEquals(0, (behind.blocker as Blocker.Vehicle).car.id)
        assertEquals(2, game.bumps)
    }

    @Test
    fun anExitedVehicleNoLongerCollidesWithAnythingOnceItIsGone() {
        val level = Level(1, 8, 1, listOf(car(0, 2, 0, 2, Dir.RIGHT), car(1, 0, 0, 2, Dir.RIGHT)))
        val offsets = mutableMapOf<Int, Float>()
        val game = ParkingGame(level).also { it.motionSource = MotionSource { id -> offsets[id] } }
        game.tap(0)
        offsets[0] = 20f // il a quitté le plateau (même si son animation n'est pas finie)
        assertTrue(game.tap(1) is TapResult.Exited)
        assertEquals(0, game.bumps)
    }

    @Test
    fun aBumpingVehicleCanNotBeTappedAgainUntilItIsDone() {
        val level = Level(1, 5, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.RIGHT)))
        val game = ParkingGame(level)
        assertTrue(game.tap(0, Move.FORWARD) is TapResult.Bumped)
        assertEquals(TapResult.Ignored, game.tap(0, Move.FORWARD)) // il rebondit encore : le deuxième geste ne compte pas
        assertEquals(1, game.bumps)
        game.finishMotion(0)
        assertTrue(game.tap(0, Move.FORWARD) is TapResult.Bumped)
        assertEquals(2, game.bumps)
    }

    @Test
    fun aRollingVehicleDoesNotBlockItself() {
        val level = Level(1, 5, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT)))
        val game = ParkingGame(level)
        game.tap(0, Move.BACKWARD)
        assertEquals(null, game.blockerOf(level.cars[0], Move.FORWARD))
    }

    // ---- les murs sur le bord du parking

    @Test
    fun aWallBlocksTheExitItGuards() {
        val level = Level(121, 4, 1, listOf(car(0, 0, 0, 2, Dir.RIGHT)), walls = listOf(Wall(Cell(3, 0), Dir.RIGHT)))
        val game = ParkingGame(level)
        val bump = game.tap(0, Move.FORWARD) as TapResult.Bumped
        assertEquals(Blocker.Barrier(Wall(Cell(3, 0), Dir.RIGHT)), bump.blocker)
        assertEquals(2, bump.freeCells) // il roule jusqu'au mur, puis klaxonne
        game.finishMotion(0)
        assertTrue(game.tap(0, Move.BACKWARD) is TapResult.Exited) // l'autre bout de la file est ouvert
    }

    @Test
    fun aVehicleAlreadyAtTheBorderIsStoppedByTheWallOnItsSide() {
        val level = Level(121, 4, 1, listOf(car(0, 2, 0, 2, Dir.RIGHT)), walls = listOf(Wall(Cell(3, 0), Dir.RIGHT)))
        val bump = ParkingGame(level).tap(0, Move.FORWARD) as TapResult.Bumped
        assertEquals(0, bump.freeCells)
        assertTrue(bump.blocker is Blocker.Barrier)
    }

    @Test
    fun theExitEdgeIsTheEndOfTheLane() {
        val v = car(0, 1, 0, 3, Dir.DOWN)
        assertEquals(Wall(Cell(1, 4), Dir.DOWN), v.exitEdge(3, 5, Move.FORWARD))
        assertEquals(Wall(Cell(1, 0), Dir.UP), v.exitEdge(3, 5, Move.BACKWARD)) // déjà au bord du haut
    }

    @Test
    fun wallsMustBeOnTheBorderFacingOutAndUnique() {
        val c = car(0, 1, 1, 2, Dir.RIGHT)
        assertTrue(runCatching { Level(1, 4, 3, listOf(c), walls = listOf(Wall(Cell(1, 1), Dir.UP))) }.isFailure) // case pas sur le bord
        assertTrue(runCatching { Level(1, 4, 3, listOf(c), walls = listOf(Wall(Cell(0, 1), Dir.UP))) }.isFailure) // côté qui ne donne pas dehors
        assertTrue(runCatching { Level(1, 4, 3, listOf(c), walls = listOf(Wall(Cell(0, 1), Dir.LEFT), Wall(Cell(0, 1), Dir.LEFT))) }.isFailure)
        Level(1, 4, 3, listOf(c), walls = listOf(Wall(Cell(0, 1), Dir.LEFT), Wall(Cell(3, 1), Dir.RIGHT))) // ceux-là sont bons
    }

    @Test
    fun aLaneWalledAtBothEndsIsADeadlock() {
        val level = Level(121, 4, 1, listOf(car(0, 1, 0, 2, Dir.RIGHT)), walls = listOf(Wall(Cell(3, 0), Dir.RIGHT), Wall(Cell(0, 0), Dir.LEFT)))
        assertFalse(Solver.canFinish(level))
        assertEquals(1, Solver.analyse(level).wallBlocks)
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
