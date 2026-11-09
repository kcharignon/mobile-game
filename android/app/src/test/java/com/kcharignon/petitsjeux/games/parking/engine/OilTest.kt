package com.kcharignon.petitsjeux.games.parking.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** La tache d'huile : un virage à 90° à droite en avançant, à gauche en reculant. */
class OilTest {
    private fun car(id: Int, col: Int, row: Int, len: Int, dir: Dir) = Car(id, col, row, len, dir, id % LevelGenerator.COLORS)

    @Test
    fun quarterTurnsFollowTheDirectionOfTravel() {
        assertEquals(listOf(Dir.RIGHT, Dir.DOWN, Dir.LEFT, Dir.UP), listOf(Dir.UP, Dir.RIGHT, Dir.DOWN, Dir.LEFT).map { it.clockwise })
        assertEquals(listOf(Dir.LEFT, Dir.UP, Dir.RIGHT, Dir.DOWN), listOf(Dir.UP, Dir.RIGHT, Dir.DOWN, Dir.LEFT).map { it.counterClockwise })
    }

    @Test
    fun withoutOilTheRouteIsStraight() {
        val c = car(0, 0, 2, 2, Dir.RIGHT)
        val route = c.route(6, 5, Move.FORWARD)
        assertEquals(listOf(Cell(2, 2), Cell(3, 2), Cell(4, 2), Cell(5, 2)), route.cells)
        assertEquals(Dir.RIGHT, route.finalDirection)
        assertEquals(Wall(Cell(5, 2), Dir.RIGHT), route.exit)
        assertFalse(route.loops)
        assertEquals(c.path(6, 5, Move.FORWARD), route.cells)
    }

    @Test
    fun goingForwardTurnsRightOnOil() {
        val c = car(0, 0, 2, 2, Dir.RIGHT)
        val route = c.route(6, 5, Move.FORWARD, listOf(Cell(3, 2)))
        // tout droit jusqu'à la tache, virage à droite (vers le bas), puis jusqu'au bord
        assertEquals(listOf(Cell(2, 2), Cell(3, 2), Cell(3, 3), Cell(3, 4)), route.cells)
        assertEquals(Dir.DOWN, route.finalDirection)
        assertEquals(Wall(Cell(3, 4), Dir.DOWN), route.exit)
        assertEquals(listOf(Dir.RIGHT, Dir.DOWN, Dir.DOWN, Dir.DOWN), route.directions)
    }

    @Test
    fun goingBackwardTurnsLeftOnOil() {
        val c = car(0, 3, 2, 2, Dir.RIGHT) // 3,2 et 4,2 ; en marche arrière il roule vers la gauche
        val route = c.route(6, 5, Move.BACKWARD, listOf(Cell(1, 2)))
        assertEquals(listOf(Cell(2, 2), Cell(1, 2), Cell(1, 3), Cell(1, 4)), route.cells)
        assertEquals(Dir.DOWN, route.finalDirection) // vers la gauche, puis un quart de tour à gauche : vers le bas
    }

    @Test
    fun bothEndsOfALaneTurnTowardTheSameSideOfTheScreen() {
        // à droite en avançant, à gauche en reculant : à l'écran, les deux bouts d'une même file tournent du même côté
        val c = car(0, 2, 2, 2, Dir.RIGHT) // 2,2 et 3,2
        val oils = listOf(Cell(4, 2), Cell(1, 2))
        assertEquals(Dir.DOWN, c.route(6, 5, Move.FORWARD, oils).finalDirection)
        assertEquals(Dir.DOWN, c.route(6, 5, Move.BACKWARD, oils).finalDirection)
        val up = car(1, 2, 2, 2, Dir.UP) // 2,2 et 2,3
        val oilsUp = listOf(Cell(2, 1), Cell(2, 4))
        assertEquals(Dir.RIGHT, up.route(5, 6, Move.FORWARD, oilsUp).finalDirection) // monte, puis à droite
        assertEquals(Dir.RIGHT, up.route(5, 6, Move.BACKWARD, oilsUp).finalDirection) // descend, puis à gauche (= vers la droite de l'écran)
    }

    @Test
    fun theTurnCanBeBlockedByAnythingOnTheNewRoute() {
        val level = Level(
            1, 6, 5,
            listOf(car(0, 0, 2, 2, Dir.RIGHT)),
            cones = listOf(Cell(3, 4)),
            oils = listOf(Cell(3, 2)),
        )
        val game = ParkingGame(level)
        val blocker = game.blockerOf(level.cars[0], Move.FORWARD)
        assertEquals(Blocker.Obstacle(Cell(3, 4), ObstacleKind.CONE), blocker)
        val result = game.tap(0, Move.FORWARD) as TapResult.Bumped
        assertEquals(3, result.freeCells) // (2,2), (3,2) la tache, (3,3)
        assertEquals(Dir.DOWN, result.direction) // il a déjà tourné : il fonce vers le bas
        assertEquals(listOf(Cell(2, 2), Cell(3, 2), Cell(3, 3)), result.route)
        assertEquals(1, game.bumps)
    }

    @Test
    fun aVehicleLeavesThroughTheTurn() {
        val level = Level(1, 6, 5, listOf(car(0, 0, 2, 2, Dir.RIGHT)), oils = listOf(Cell(3, 2)))
        val game = ParkingGame(level)
        val result = game.tap(0, Move.FORWARD) as TapResult.Exited
        assertEquals(listOf(Cell(2, 2), Cell(3, 2), Cell(3, 3), Cell(3, 4)), result.route)
        assertEquals(Dir.DOWN, result.finalDirection)
        assertTrue(game.isWon)
        assertEquals(0, game.bumps)
    }

    @Test
    fun anotherVehicleOnTheNewRouteBlocksIt() {
        val level = Level(
            1, 6, 5,
            listOf(car(0, 0, 2, 2, Dir.RIGHT), car(1, 3, 3, 2, Dir.DOWN)), // le deuxième est sur la route après le virage
            oils = listOf(Cell(3, 2)),
        )
        val game = ParkingGame(level)
        val blocker = game.blockerOf(level.cars[0], Move.FORWARD)
        assertTrue(blocker is Blocker.Vehicle && blocker.car.id == 1)
        // le véhicule 0 peut quand même partir en marche arrière
        assertNull(game.blockerOf(level.cars[0], Move.BACKWARD))
    }

    @Test
    fun theWallAtTheEndOfTheTurnedRouteBlocksIt() {
        val level = Level(
            1, 6, 5,
            listOf(car(0, 0, 2, 2, Dir.RIGHT)),
            walls = listOf(Wall(Cell(3, 4), Dir.DOWN)),
            oils = listOf(Cell(3, 2)),
        )
        val game = ParkingGame(level)
        assertEquals(Blocker.Barrier(Wall(Cell(3, 4), Dir.DOWN)), game.blockerOf(level.cars[0], Move.FORWARD))
        // le mur est à la sortie après le virage : l'autre sens, lui, sort librement
        assertNull(game.blockerOf(level.cars[0], Move.BACKWARD))
    }

    @Test
    fun theSolverFollowsTheTurn() {
        // tout droit, le véhicule 0 est arrêté par le 1 ; avec la tache il tourne avant lui et passe
        val cars = listOf(car(0, 0, 2, 2, Dir.RIGHT), car(1, 4, 1, 3, Dir.DOWN))
        assertFalse(Solver.freeMoves(6, 5, emptyList(), cars, cars[0]).contains(Move.FORWARD))
        assertTrue(Solver.freeMoves(6, 5, emptyList(), cars, cars[0], floor = Floor(oils = setOf(Cell(3, 2)))).contains(Move.FORWARD))
        assertTrue(Solver.canFinish(Level(1, 6, 5, cars, oils = listOf(Cell(3, 2)))))
    }

    @Test
    fun aRouteThatBringsTheVehicleBackOnItselfIsBlocked() {
        // quatre taches en rectangle sur la route : le véhicule ferait le tour et reviendrait sur sa place
        val c = car(0, 2, 1, 2, Dir.RIGHT) // 2,1 et 3,1
        val oils = listOf(Cell(4, 1), Cell(4, 3), Cell(1, 3), Cell(1, 1))
        val route = c.route(6, 5, Move.FORWARD, oils)
        assertTrue(route.loops)
        assertNull(route.exit)
        val level = Level(1, 6, 5, listOf(c), oils = oils)
        val game = ParkingGame(level)
        val blocker = game.blockerOf(c, Move.FORWARD)
        assertTrue(blocker is Blocker.Obstacle && blocker.kind == ObstacleKind.OIL)
        // en arrière il tourne aussi en rond : ce niveau ne se finit pas, et le solveur le sait
        assertTrue(c.route(6, 5, Move.BACKWARD, oils).loops)
        assertFalse(Solver.canFinish(level))
    }

    @Test
    fun levelsRefuseOilUnderOtherThings() {
        val ok = runCatching { Level(1, 5, 5, listOf(car(0, 0, 0, 2, Dir.RIGHT)), cones = listOf(Cell(3, 3)), oils = listOf(Cell(2, 2))) }
        assertTrue(ok.isSuccess)
        assertTrue(runCatching { Level(1, 5, 5, listOf(car(0, 0, 0, 2, Dir.RIGHT)), oils = listOf(Cell(1, 0))) }.isFailure)
        assertTrue(runCatching { Level(1, 5, 5, listOf(car(0, 0, 0, 2, Dir.RIGHT)), cones = listOf(Cell(2, 2)), oils = listOf(Cell(2, 2))) }.isFailure)
        assertTrue(runCatching { Level(1, 5, 5, listOf(car(0, 0, 0, 2, Dir.RIGHT)), oils = listOf(Cell(7, 2))) }.isFailure)
    }

    @Test
    fun theHintUnderstandsTheTurn() {
        // 0 doit passer par la tache pour sortir (la marche arrière est bloquée par le mur du bord)
        val level = Level(
            1, 6, 5,
            listOf(car(0, 1, 2, 2, Dir.RIGHT)),
            walls = listOf(Wall(Cell(0, 2), Dir.LEFT), Wall(Cell(5, 2), Dir.RIGHT)),
            oils = listOf(Cell(3, 2)),
        )
        assertTrue(Solver.canFinish(level))
        val game = ParkingGame(level)
        val hint = game.hint()!!
        assertEquals(0, hint.car.id)
        assertEquals(Move.FORWARD, hint.move)
        assertTrue(game.tap(hint.car.id, hint.move) is TapResult.Exited)
    }

    @Test
    fun analysisCountsTheVehiclesThatTurn() {
        val level = Level(1, 6, 5, listOf(car(0, 0, 2, 2, Dir.RIGHT), car(1, 5, 0, 2, Dir.DOWN)), oils = listOf(Cell(3, 2)))
        val a = Solver.analyse(level)
        assertTrue(a.solvable)
        assertEquals(1, a.oilTurns)
    }
}
