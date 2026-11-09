package com.kcharignon.petitsjeux.games.parking.engine

import kotlin.math.floor

/** Ce qui bloque un véhicule : un autre véhicule (même s'il roule), un plot de chantier, une caisse tombée, un mur à la sortie ou un tour en rond sur l'huile. */
sealed interface Blocker {
    data class Vehicle(val car: Car) : Blocker

    data class Obstacle(val cell: Cell, val kind: ObstacleKind) : Blocker

    /** La sortie de la file est murée : on arrive jusqu'au bord, mais on ne passe pas. */
    data class Barrier(val wall: Wall) : Blocker
}

enum class ObstacleKind { CONE, CRATE, OIL }

/** Ce qui se passe quand on glisse (ou touche) un véhicule. */
sealed interface TapResult {
    /**
     * La voie est libre : le véhicule sort en suivant son [route] (cases traversées, avec les virages sur l'huile) puis continue dans le sens [finalDirection].
     * [dropped] : la case où un camion à caisse la laisse tomber en partant (celle que sa queue occupait), sinon `null`.
     */
    data class Exited(val car: Car, val move: Move, val route: List<Cell>, val finalDirection: Dir, val dropped: Cell? = null) : TapResult {
        /** Nombre de cases traversées avant le bord du plateau. */
        val pathLength: Int get() = route.size
    }

    /**
     * Quelque chose bloque : le véhicule avance de [freeCells] cases ([route] : ces cases, avec les virages) dans le sens [direction],
     * klaxonne et revient. C'est une faute.
     */
    data class Bumped(val car: Car, val move: Move, val blocker: Blocker, val freeCells: Int, val route: List<Cell>, val direction: Dir) : TapResult

    /** Le véhicule n'est plus sur le plateau (déjà sorti) ou est en plein mouvement : rien ne se passe. */
    data object Ignored : TapResult
}

/** Un coup conseillé : ce véhicule, dans ce sens. */
data class Hint(val car: Car, val move: Move)

/**
 * Où en est un véhicule qui roule, **tel qu'il est dessiné** : le chemin qu'il a parcouru depuis son départ, en cases le long de son trajet (0 au départ),
 * ou `null` si inconnu. C'est l'interface qui l'anime ; le moteur le lit pour que les véhicules en mouvement gardent leurs collisions.
 */
fun interface MotionSource {
    fun traveled(carId: Int): Float?
}

/** Étoiles d'un niveau selon le nombre de fautes : 0 = 3 étoiles, 1 ou 2 = 2 étoiles, 3 ou plus = 1 étoile. */
fun starsForBumps(bumps: Int): Int = when {
    bumps <= 0 -> 3
    bumps <= 2 -> 2
    else -> 1
}

/** Une partie de Parking Malin : l'état d'un niveau en cours. Règles pures, aucune dépendance Android. */
class ParkingGame(val level: Level) {
    private val onBoard = LinkedHashMap<Int, Car>().apply { level.cars.forEach { put(it.id, it) } }
    private val fallenCrates = LinkedHashSet<Cell>()

    /** Un véhicule en mouvement (il sort, ou il fonce sur un obstacle puis revient) : il barre son couloir tant que [finishMotion] n'a pas été appelé. */
    private class Motion(val car: Car, val move: Move, val cells: List<Cell>)

    private val motions = LinkedHashMap<Int, Motion>()

    /** Où sont dessinés les véhicules qui roulent (voir [MotionSource]). Sans source, un véhicule en mouvement barre tout son couloir. */
    var motionSource: MotionSource = MotionSource { null }

    /** Fautes commises (véhicules bloqués touchés). */
    var bumps: Int = 0
        private set

    /** Véhicules sortis. */
    var exited: Int = 0
        private set

    val carsLeft: Int get() = onBoard.size
    val isWon: Boolean get() = onBoard.isEmpty()
    val stars: Int get() = starsForBumps(bumps)

    /** Véhicules encore sur le plateau, dans l'ordre du niveau. */
    val cars: List<Car> get() = onBoard.values.toList()

    /** Les caisses tombées (obstacles pour de bon). */
    val crates: Set<Cell> get() = fallenCrates

    /** Y a-t-il encore des véhicules en mouvement ? */
    val isMoving: Boolean get() = motions.isNotEmpty()

    fun isOnBoard(id: Int): Boolean = id in onBoard

    /** L'interface appelle ceci quand l'animation d'un véhicule est terminée : son couloir est de nouveau libre. */
    fun finishMotion(id: Int) {
        motions.remove(id)
    }

    private fun fixedObstacles(): Set<Cell> = level.cones.toSet() + fallenCrates

    private fun inGrid(c: Cell) = c.col in 0 until level.cols && c.row in 0 until level.rows

    /** Le trajet de [car] dans ce sens, avec les virages des taches d'huile de ce niveau. */
    fun routeOf(car: Car, move: Move): Route = car.route(level.cols, level.rows, move, level.oils)

    /**
     * Les cases barrées par un véhicule en mouvement : de la case où sa queue se trouve **maintenant** jusqu'au bout de sa course (le bord du plateau
     * s'il sort ; le point de choc s'il rebondit), en suivant son trajet (virages compris). Dès que sa queue a dépassé une case, elle est libre.
     */
    private fun corridor(m: Motion): List<Cell> {
        val traveled = (motionSource.traveled(m.car.id) ?: 0f).coerceAtLeast(0f)
        val from = floor(traveled + 0.1f).toInt()
        return (m.car.cellsTrailToLead(m.move) + m.cells).drop(from).filter(::inGrid)
    }

    private fun owners(except: Int?): Map<Cell, Blocker> {
        val owners = HashMap<Cell, Blocker>()
        for (cone in level.cones) owners[cone] = Blocker.Obstacle(cone, ObstacleKind.CONE)
        for (crate in fallenCrates) owners[crate] = Blocker.Obstacle(crate, ObstacleKind.CRATE)
        // un véhicule immobile occupe ses cases ; un véhicule en mouvement barre son couloir
        for (car in onBoard.values) if (car.id !in motions) for (cell in car.cells) owners[cell] = Blocker.Vehicle(car)
        for (m in motions.values) if (m.car.id != except) for (cell in corridor(m)) owners[cell] = Blocker.Vehicle(m.car)
        return owners
    }

    /** Ce qui est le plus proche sur le trajet de [car] dans ce sens (ou le mur à la sortie), ou `null` si la voie est libre. */
    fun blockerOf(car: Car, move: Move = Move.FORWARD): Blocker? {
        val owners = owners(except = car.id)
        val route = routeOf(car, move)
        route.cells.firstNotNullOfOrNull { owners[it] }?.let { return it }
        if (route.loops) return Blocker.Obstacle(route.cells.last(), ObstacleKind.OIL)
        return route.exit?.takeIf { it in level.walls }?.let { Blocker.Barrier(it) }
    }

    fun canMove(car: Car, move: Move): Boolean = blockerOf(car, move) == null

    private fun freeCellsAhead(car: Car, move: Move): Int {
        val owners = owners(except = car.id)
        val cells = routeOf(car, move).cells
        val index = cells.indexOfFirst { it in owners }
        return if (index < 0) cells.size else index
    }

    /** Un véhicule est glissé (ou touché : un simple toucher = marche avant) dans ce sens. */
    fun tap(id: Int, move: Move = Move.FORWARD): TapResult {
        if (id in motions) return TapResult.Ignored // déjà en mouvement (il rebondit encore) : un deuxième geste ne compte pas
        val car = onBoard[id] ?: return TapResult.Ignored
        val route = routeOf(car, move)
        val blocker = blockerOf(car, move)
        if (blocker != null) {
            bumps++
            val free = freeCellsAhead(car, move)
            val cells = route.cells.take(free)
            motions[id] = Motion(car, move, cells)
            val direction = if (free == 0) car.directionOf(move) else route.directions[free - 1]
            return TapResult.Bumped(car, move, blocker, free, cells, direction)
        }
        onBoard.remove(id)
        exited++
        motions[id] = Motion(car, move, route.cells)
        val dropped = if (car.carrier) car.trail(move).also { fallenCrates += it } else null
        return TapResult.Exited(car, move, route.cells, route.finalDirection, dropped)
    }

    /** Peut-on encore tout faire sortir depuis cet état ? Faux seulement si une caisse mal placée a fermé le dernier passage. */
    val isStuck: Boolean
        get() = onBoard.isNotEmpty() && !Solver.canFinish(level.cols, level.rows, fixedObstacles(), onBoard.values.toList(), level.walls, level.oils)

    /**
     * Un coup qui mène à la solution : un véhicule libre **maintenant**, dans un sens où il peut sortir, qui en débloque le plus d'autres.
     * Les coups qui conduiraient à une impasse (un camion qui laisserait tomber sa caisse au mauvais endroit) ne sont jamais conseillés.
     * `null` si le plateau est vide ou si rien n'est libre pour l'instant (un véhicule qui roule encore barre peut-être le passage).
     */
    fun hint(): Hint? {
        val remaining = onBoard.values.toList()
        val fixed = fixedObstacles()
        val candidates = remaining.flatMap { car -> Move.entries.filter { canMove(car, it) }.map { Hint(car, it) } }
            .filter { h ->
                val rest = remaining - h.car
                val crates = if (h.car.carrier) fixed + h.car.trail(h.move) else fixed
                Solver.canFinish(level.cols, level.rows, crates, rest, level.walls, level.oils)
            }
        if (candidates.isEmpty()) return null
        val dependants = candidates.associateWith { h ->
            val cells = h.car.cells.toSet()
            remaining.count { other ->
                other.id != h.car.id && Move.entries.any { m -> routeOf(other, m).cells.any { it in cells } }
            }
        }
        // d'abord ceux qui débloquent le plus ; à égalité, les non-camions (rien ne tombe), puis le plus petit numéro, puis la marche avant
        return candidates.maxWith(
            compareBy<Hint> { dependants.getValue(it) }
                .thenBy { if (it.car.carrier) 0 else 1 }
                .thenBy { -it.car.id }
                .thenBy { if (it.move == Move.FORWARD) 1 else 0 },
        )
    }
}
