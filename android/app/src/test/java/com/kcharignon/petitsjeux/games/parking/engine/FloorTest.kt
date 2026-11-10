package com.kcharignon.petitsjeux.games.parking.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Sens uniques, tapis roulants, téléporteurs et barrières. */
class FloorTest {
    private fun car(id: Int, col: Int, row: Int, len: Int, dir: Dir) = Car(id, col, row, len, dir, id % LevelGenerator.COLORS)

    // ---- sens unique

    @Test
    fun aOneWayCanOnlyBeCrossedInTheDirectionOfItsArrow() {
        val c = car(0, 0, 2, 2, Dir.RIGHT) // cases 0,2 et 1,2
        val with = Floor(oneWays = mapOf(Cell(3, 2) to Dir.RIGHT))
        val route = c.route(6, 5, Move.FORWARD, with)
        assertNull(route.stop)
        assertEquals(listOf(Cell(2, 2), Cell(3, 2), Cell(4, 2), Cell(5, 2)), route.cells)

        val against = Floor(oneWays = mapOf(Cell(3, 2) to Dir.LEFT))
        val stopped = c.route(6, 5, Move.FORWARD, against)
        assertEquals(Stop(Cell(3, 2), ObstacleKind.ONEWAY), stopped.stop)
        assertEquals(listOf(Cell(2, 2)), stopped.cells) // il avance jusqu'au panneau, sans le franchir
        assertNull(stopped.exit)

        val across = Floor(oneWays = mapOf(Cell(3, 2) to Dir.DOWN))
        assertEquals(ObstacleKind.ONEWAY, c.route(6, 5, Move.FORWARD, across).stop?.kind) // en travers : interdit aussi
    }

    @Test
    fun aOneWayIsDirectionalForReversingToo() {
        val c = car(0, 3, 2, 2, Dir.RIGHT) // recule vers la gauche
        val sign = Floor(oneWays = mapOf(Cell(1, 2) to Dir.RIGHT)) // flèche vers la droite : à contresens pour lui
        assertEquals(ObstacleKind.ONEWAY, c.route(6, 5, Move.BACKWARD, sign).stop?.kind)
        assertNull(c.route(6, 5, Move.FORWARD, sign).stop) // en avant il va vers la droite : il passe
    }

    @Test
    fun aOneWayBlocksLikeAnObstacleAndTheVehicleHonks() {
        val level = Level(101, 6, 3, listOf(car(0, 0, 1, 2, Dir.RIGHT)), oneWays = listOf(Arrow(Cell(3, 1), Dir.LEFT)))
        val game = ParkingGame(level)
        val result = game.tap(0, Move.FORWARD) as TapResult.Bumped
        assertEquals(Blocker.Obstacle(Cell(3, 1), ObstacleKind.ONEWAY), result.blocker)
        assertEquals(1, result.freeCells)
        assertEquals(1, game.bumps)
        game.finishMotion(0)
        // à reculons, il sort librement par la gauche
        assertTrue(game.tap(0, Move.BACKWARD) is TapResult.Exited)
    }

    @Test
    fun theSolverKnowsWhichWaysAOneWayCuts() {
        val level = Level(101, 6, 3, listOf(car(0, 0, 1, 2, Dir.RIGHT)), oneWays = listOf(Arrow(Cell(3, 1), Dir.LEFT)), walls = listOf(Wall(Cell(0, 1), Dir.LEFT)))
        // vers la droite : sens interdit ; vers la gauche : mur. Aucune sortie.
        assertFalse(Solver.canFinish(level))
        assertEquals(1, Solver.analyse(level).oneWayBlocks)
    }

    // ---- tapis roulant

    @Test
    fun aBeltPushesAVehicleOneCellSidewaysWithoutTurningIt() {
        val c = car(0, 0, 2, 2, Dir.RIGHT)
        val belt = Floor(belts = mapOf(Cell(3, 2) to Dir.DOWN))
        val route = c.route(6, 5, Move.FORWARD, belt)
        // tout droit jusqu'au tapis, poussé d'une case vers le bas, puis tout droit dans la nouvelle file
        assertEquals(listOf(Cell(2, 2), Cell(3, 2), Cell(3, 3), Cell(4, 3), Cell(5, 3)), route.cells)
        assertEquals(listOf(Entry.DRIVE, Entry.DRIVE, Entry.PUSH, Entry.DRIVE, Entry.DRIVE), route.entries)
        assertEquals(Dir.RIGHT, route.finalDirection) // il n'a pas tourné
        assertTrue(route.headings.all { it == Dir.RIGHT })
        assertEquals(Wall(Cell(5, 3), Dir.RIGHT), route.exit)
    }

    @Test
    fun aBeltAlongTheLaneChangesNothing() {
        val c = car(0, 0, 2, 2, Dir.RIGHT)
        val straight = c.route(6, 5, Move.FORWARD).cells
        assertEquals(straight, c.route(6, 5, Move.FORWARD, Floor(belts = mapOf(Cell(3, 2) to Dir.RIGHT))).cells)
        assertEquals(straight, c.route(6, 5, Move.FORWARD, Floor(belts = mapOf(Cell(3, 2) to Dir.LEFT))).cells)
    }

    @Test
    fun aBeltPushesTheSameWayWhenReversing() {
        val c = car(0, 3, 2, 2, Dir.RIGHT) // recule vers la gauche
        val route = c.route(6, 5, Move.BACKWARD, Floor(belts = mapOf(Cell(1, 2) to Dir.UP)))
        assertEquals(listOf(Cell(2, 2), Cell(1, 2), Cell(1, 1), Cell(0, 1)), route.cells)
        assertEquals(Dir.LEFT, route.finalDirection)
    }

    @Test
    fun aBeltCanPushAVehicleOffTheSideOfTheBoard() {
        val c = car(0, 0, 2, 2, Dir.RIGHT)
        val route = c.route(6, 5, Move.FORWARD, Floor(belts = mapOf(Cell(3, 4) to Dir.DOWN)))
        assertEquals(c.route(6, 5, Move.FORWARD).cells, route.cells) // la file passe en (3,2) : le tapis est ailleurs
        val low = car(1, 0, 4, 2, Dir.RIGHT) // sur la dernière ligne
        val off = low.route(6, 5, Move.FORWARD, Floor(belts = mapOf(Cell(3, 4) to Dir.DOWN)))
        assertEquals(listOf(Cell(2, 4), Cell(3, 4)), off.cells)
        assertEquals(Wall(Cell(3, 4), Dir.DOWN), off.exit) // poussé hors du plateau, par le côté
        assertEquals(Dir.DOWN, off.finalDirection)
        assertEquals(Dir.RIGHT, off.finalHeading)
    }

    @Test
    fun theCellAfterTheBeltPushCanBlockOrCountAsAnObstacle() {
        val level = Level(
            121, 6, 5, listOf(car(0, 0, 2, 2, Dir.RIGHT)),
            cones = listOf(Cell(4, 3)), belts = listOf(Arrow(Cell(3, 2), Dir.DOWN)),
        )
        val game = ParkingGame(level)
        val result = game.tap(0) as TapResult.Bumped
        assertEquals(Blocker.Obstacle(Cell(4, 3), ObstacleKind.CONE), result.blocker)
        assertEquals(3, result.freeCells) // (2,2), (3,2) le tapis, (3,3) la case où il est poussé
    }

    // ---- téléporteur

    @Test
    fun aVehicleEnteringAPortalComesOutOfTheOtherOne() {
        val c = car(0, 0, 2, 2, Dir.RIGHT)
        val floor = Floor(portals = mapOf(Cell(3, 2) to Cell(2, 0), Cell(2, 0) to Cell(3, 2)))
        val route = c.route(6, 5, Move.FORWARD, floor)
        // tout droit jusqu'au téléporteur en (3,2), il ressort en (2,0) et repart tout droit vers la droite
        assertEquals(listOf(Cell(2, 2), Cell(3, 2), Cell(2, 0), Cell(3, 0), Cell(4, 0), Cell(5, 0)), route.cells)
        assertEquals(listOf(Entry.DRIVE, Entry.DRIVE, Entry.JUMP, Entry.DRIVE, Entry.DRIVE, Entry.DRIVE), route.entries)
        assertEquals(Wall(Cell(5, 0), Dir.RIGHT), route.exit)
    }

    @Test
    fun aPortalWorksBothWaysAndNeverPingPongs() {
        val c = car(0, 4, 0, 2, Dir.RIGHT) // cases 4,0 et 5,0 ; recule vers la gauche
        val floor = Floor(portals = mapOf(Cell(3, 2) to Cell(2, 0), Cell(2, 0) to Cell(3, 2)))
        val route = c.route(6, 5, Move.BACKWARD, floor)
        // vers la gauche, (3,0), puis (2,0) : il ressort en (3,2) et repart vers la gauche
        assertEquals(listOf(Cell(3, 0), Cell(2, 0), Cell(3, 2), Cell(2, 2), Cell(1, 2), Cell(0, 2)), route.cells)
        assertEquals(Entry.JUMP, route.entries[2])
    }

    @Test
    fun aVehicleParkedOnTheWayOutOfAPortalBlocksIt() {
        val level = Level(
            141, 6, 5,
            listOf(car(0, 0, 2, 2, Dir.RIGHT), car(1, 3, 0, 2, Dir.DOWN)), // la voiture 1 occupe (3,0) et (3,1)
            portals = listOf(Portal(Cell(3, 2), Cell(2, 0))),
        )
        val game = ParkingGame(level)
        val result = game.tap(0, Move.FORWARD) as TapResult.Bumped
        assertTrue(result.blocker is Blocker.Vehicle && (result.blocker as Blocker.Vehicle).car.id == 1)
        assertEquals(3, result.freeCells) // (2,2), (3,2), (2,0) : la case suivante, (3,0), est prise
    }

    @Test
    fun theSolverFollowsAPortal() {
        // sans téléporteur la voiture 0 est coincée ; avec, elle sort par l'autre côté du parking
        val cars = listOf(car(0, 0, 1, 2, Dir.RIGHT), car(1, 3, 0, 3, Dir.DOWN))
        val walls = listOf(Wall(Cell(0, 1), Dir.LEFT))
        assertFalse(Solver.freeMoves(6, 3, emptyList(), cars, cars[0], walls).contains(Move.FORWARD))
        val portal = Floor(portals = mapOf(Cell(2, 1) to Cell(4, 0), Cell(4, 0) to Cell(2, 1)))
        assertTrue(Solver.freeMoves(6, 3, emptyList(), cars, cars[0], walls, portal).contains(Move.FORWARD))
        assertEquals(1, Solver.analyse(Level(141, 6, 3, cars, walls = walls, portals = listOf(Portal(Cell(2, 1), Cell(4, 0))))).portalJumps)
    }

    // ---- tout ensemble, et boucles

    @Test
    fun oilBeltsPortalsAndSignsCanBeChained() {
        val c = car(0, 0, 2, 2, Dir.RIGHT)
        val floor = Floor(
            belts = mapOf(Cell(2, 2) to Dir.UP), // poussé vers le haut en (2,1)
            oils = setOf(Cell(4, 1)), // puis tout droit jusqu'à l'huile en (4,1) : virage à droite, vers le bas
            portals = mapOf(Cell(4, 3) to Cell(7, 3), Cell(7, 3) to Cell(4, 3)), // (4,2), (4,3) : téléporté en (7,3)
        )
        val route = c.route(9, 6, Move.FORWARD, floor)
        assertEquals(
            listOf(Cell(2, 2), Cell(2, 1), Cell(3, 1), Cell(4, 1), Cell(4, 2), Cell(4, 3), Cell(7, 3), Cell(7, 4), Cell(7, 5)),
            route.cells,
        )
        assertEquals(Wall(Cell(7, 5), Dir.DOWN), route.exit)
        assertEquals(Dir.DOWN, route.finalDirection)
    }

    @Test
    fun aPortalThatSendsTheVehicleBackOnItsOwnPlaceIsNotARoute() {
        // il entre en (3,0), ressort en (0,0) et repartirait tout droit sur sa propre place : il se rentrerait dedans
        val c = car(0, 1, 0, 2, Dir.RIGHT) // 1,0 et 2,0
        val floor = Floor(portals = mapOf(Cell(3, 0) to Cell(0, 0), Cell(0, 0) to Cell(3, 0)))
        val route = c.route(6, 3, Move.FORWARD, floor)
        assertTrue(route.loops)
        assertNull(route.exit)
        assertEquals(listOf(Cell(3, 0), Cell(0, 0)), route.cells)
    }

    // ---- barrières de péage (entre deux cases)

    private val gate = Gate(Cell(3, 1), Cell(4, 1), startsOpen = false)

    private val gateLevel = Level(
        161, 8, 3,
        listOf(car(0, 0, 0, 2, Dir.RIGHT), car(1, 0, 2, 2, Dir.RIGHT), car(2, 0, 1, 2, Dir.RIGHT)), // 0 et 1 : libres ; 2 : passe par la barrière
        walls = listOf(Wall(Cell(0, 1), Dir.LEFT)), // le véhicule 2 ne peut pas sortir en marche arrière : il doit passer par la barrière
        gates = listOf(gate),
    )

    @Test
    fun aGateChangesWithEveryVehicleThatLeavesTheBoard() {
        assertFalse(gate.isOpen(0))
        assertTrue(gate.isOpen(1))
        assertFalse(gate.isOpen(2))
        assertTrue(Gate(Cell(0, 0), Cell(1, 0), startsOpen = true).isOpen(0))
        val game = ParkingGame(gateLevel)
        assertFalse(game.isOpen(gate))
        game.tap(0)
        assertTrue(game.isOpen(gate))
        game.tap(1)
        assertFalse(game.isOpen(gate))
        assertEquals(2, game.exited)
        assertEquals(0, game.bumps)
    }

    @Test
    fun aGateSitsBetweenTwoCellsAndOnlyStopsWhoCrossesIt() {
        assertTrue(gate.separates(Cell(3, 1), Cell(4, 1)))
        assertTrue(gate.separates(Cell(4, 1), Cell(3, 1)))
        assertFalse(gate.separates(Cell(3, 0), Cell(4, 0)))
        assertFalse(gate.separates(Cell(3, 1), Cell(3, 2)))
        assertTrue(gate.vertical)
        assertFalse(Gate(Cell(2, 2), Cell(2, 3), true).vertical)
        // la ligne 1 est barrée entre les colonnes 3 et 4 : les lignes 0 et 2 ne le sont pas, et la case de chaque côté reste libre
        val game = ParkingGame(gateLevel)
        assertTrue(game.canMove(gateLevel.cars[0], Move.FORWARD))
        assertTrue(game.canMove(gateLevel.cars[1], Move.FORWARD))
        assertFalse(game.canMove(gateLevel.cars[2], Move.FORWARD))
    }

    @Test
    fun aLoweredGateStopsTheVehicleRightBeforeIt() {
        val game = ParkingGame(gateLevel)
        val result = game.tap(2) as TapResult.Bumped
        assertEquals(Blocker.ClosedGate(gate), result.blocker)
        assertEquals(2, result.freeCells) // (2,1) et (3,1), puis la barrière
        assertEquals(1, game.bumps)
    }

    @Test
    fun aRaisedGateLetsEveryoneThrough() {
        val game = ParkingGame(gateLevel)
        game.tap(0) // un autre véhicule sort : la barrière se lève
        game.finishMotion(0)
        assertTrue(game.tap(2) is TapResult.Exited)
        assertEquals(0, game.bumps)
    }

    @Test
    fun honkingInFrontOfALoweredGateRaisesItSoYouAreNeverStuck() {
        val game = ParkingGame(gateLevel)
        game.tap(0)
        game.finishMotion(0)
        game.tap(1) // mauvais ordre : la barrière est de nouveau baissée et le véhicule 2 reste seul
        game.finishMotion(1)
        assertFalse(game.isOpen(gate))
        assertTrue("sans faute, il n'y a plus de solution", game.faultAhead)
        // l'indice montre le véhicule bloqué : il klaxonne (une faute) et la barrière se lève
        val hint = game.hint()!!
        assertEquals(2, hint.car.id)
        val bump = game.tap(2, hint.move) as TapResult.Bumped
        assertEquals(Blocker.ClosedGate(gate), bump.blocker)
        game.finishMotion(2)
        assertTrue(game.isOpen(gate))
        assertEquals(1, game.bumps)
        assertTrue(game.tap(2) is TapResult.Exited)
        assertTrue(game.isWon)
        assertEquals(2, game.stars)
    }

    @Test
    fun aVehicleThatJumpsThroughATeleporterDoesNotCrossTheGatesBetweenItsTwoPlaces() {
        // le téléporteur de (1,1) ressort en (6,1) : la barrière baissée entre (3,1) et (4,1) est sautée
        val level = Level(
            161, 8, 3,
            listOf(car(0, 0, 1, 1 + 1, Dir.RIGHT)),
            portals = listOf(Portal(Cell(2, 1), Cell(6, 1))),
            gates = listOf(gate),
        )
        val route = level.cars[0].route(8, 3, Move.FORWARD, level.floor)
        assertTrue(Entry.JUMP in route.entries)
        assertTrue(ParkingGame(level).canMove(level.cars[0], Move.FORWARD))
    }

    @Test
    fun aGateOnTheSideOfABeltPushStopsTheVehicleBeingPushed() {
        // le tapis en (2,1) pousse vers le bas : la barrière entre (2,1) et (2,2) barre ce côté
        val level = Level(
            161, 6, 4,
            listOf(car(0, 0, 1, 2, Dir.RIGHT)),
            belts = listOf(Arrow(Cell(2, 1), Dir.DOWN)),
            gates = listOf(Gate(Cell(2, 1), Cell(2, 2), startsOpen = false)),
        )
        val result = ParkingGame(level).tap(0) as TapResult.Bumped
        assertEquals(Blocker.ClosedGate(level.gates[0]), result.blocker)
        assertEquals(1, result.freeCells) // le tapis (2,1) est atteint, la poussée est arrêtée
    }

    @Test
    fun theSolverKnowsTheGatesParityMatters() {
        assertTrue(Solver.canFinish(gateLevel)) // 0, puis 2 (barrière levée), puis 1
        assertEquals(1, Solver.analyse(gateLevel).gateBlocks)
        // seul : la barrière ne se lèvera jamais sans faute
        val alone = Level(161, 8, 3, listOf(car(0, 0, 1, 2, Dir.RIGHT)), walls = listOf(Wall(Cell(0, 1), Dir.LEFT)), gates = listOf(gate))
        assertFalse(Solver.canFinish(alone))
        assertFalse(Solver.analyse(alone).solvable)
    }

    @Test
    fun theHintNeverLeadsIntoAGateTrap() {
        val game = ParkingGame(gateLevel)
        game.tap(0)
        game.finishMotion(0)
        // il reste 1 (libre) et 2 (barrière levée maintenant) : si 1 sortait d'abord, la barrière serait baissée pour toujours
        val hint = game.hint()!!
        assertEquals(2, hint.car.id)
        assertFalse(game.faultAhead)
    }

    @Test
    fun followingTheHintsWinsEvenWithGates() {
        val game = ParkingGame(gateLevel)
        var guard = 0
        while (!game.isWon) {
            val hint = game.hint() ?: error("aucun coup")
            assertTrue(game.tap(hint.car.id, hint.move) is TapResult.Exited)
            game.finishMotion(hint.car.id)
            assertTrue(++guard <= 3)
        }
        assertEquals(0, game.bumps)
    }

    @Test
    fun levelsRefuseFloorThingsOnTopOfEachOther() {
        val cars = listOf(car(0, 0, 0, 2, Dir.RIGHT))
        assertTrue(runCatching { Level(1, 5, 5, cars, belts = listOf(Arrow(Cell(1, 0), Dir.UP))) }.isFailure) // sous la voiture
        assertTrue(runCatching { Level(1, 5, 5, cars, oneWays = listOf(Arrow(Cell(3, 3), Dir.UP)), belts = listOf(Arrow(Cell(3, 3), Dir.DOWN))) }.isFailure)
        assertTrue(runCatching { Level(1, 5, 5, cars, portals = listOf(Portal(Cell(3, 3), Cell(3, 3)))) }.isFailure)
        // une barrière est entre deux cases voisines de la grille, jamais au milieu d'un véhicule garé
        assertTrue(runCatching { Level(1, 5, 5, cars, gates = listOf(Gate(Cell(4, 3), Cell(5, 3), true))) }.isFailure) // hors de la grille
        assertTrue(runCatching { Level(1, 5, 5, cars, gates = listOf(Gate(Cell(0, 0), Cell(1, 0), true))) }.isFailure) // couperait le véhicule en deux
        assertTrue(runCatching { Gate(Cell(0, 0), Cell(2, 0), true) }.isFailure) // pas voisines
        assertTrue(runCatching { Level(1, 5, 5, cars, gates = listOf(Gate(Cell(3, 3), Cell(4, 3), true), Gate(Cell(4, 3), Cell(3, 3), false))) }.isFailure) // deux fois la même
        assertTrue(runCatching { Level(1, 5, 5, cars, portals = listOf(Portal(Cell(3, 3), Cell(4, 4))), gates = listOf(Gate(Cell(2, 2), Cell(2, 3), true))) }.isSuccess)
        // une barrière peut longer un véhicule garé, ou être entre deux véhicules
        assertTrue(runCatching { Level(1, 5, 5, cars, gates = listOf(Gate(Cell(1, 0), Cell(2, 0), true), Gate(Cell(0, 0), Cell(0, 1), true))) }.isSuccess)
    }
}
