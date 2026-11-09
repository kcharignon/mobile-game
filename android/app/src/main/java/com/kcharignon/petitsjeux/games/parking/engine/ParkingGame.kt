package com.kcharignon.petitsjeux.games.parking.engine

/** Ce qui bloque un véhicule : un autre véhicule, un plot de chantier ou une caisse tombée. */
sealed interface Blocker {
    data class Vehicle(val car: Car) : Blocker

    data class Obstacle(val cell: Cell, val kind: ObstacleKind) : Blocker
}

enum class ObstacleKind { CONE, CRATE }

/** Ce qui se passe quand on glisse (ou touche) un véhicule. */
sealed interface TapResult {
    /**
     * La voie est libre : le véhicule sort. [pathLength] = cases entre lui et le bord du plateau dans ce sens.
     * [dropped] : la case où un camion à caisse la laisse tomber en partant (celle que sa queue occupait), sinon `null`.
     */
    data class Exited(val car: Car, val move: Move, val pathLength: Int, val dropped: Cell? = null) : TapResult

    /** Quelque chose bloque : le véhicule avance de [freeCells] cases, klaxonne et revient. C'est une faute. */
    data class Bumped(val car: Car, val move: Move, val blocker: Blocker, val freeCells: Int) : TapResult

    /** Le véhicule n'est plus sur le plateau (déjà sorti) : rien ne se passe. */
    data object Ignored : TapResult
}

/** Un coup conseillé : ce véhicule, dans ce sens. */
data class Hint(val car: Car, val move: Move)

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

    fun isOnBoard(id: Int): Boolean = id in onBoard

    private fun fixedObstacles(): Set<Cell> = level.cones.toSet() + fallenCrates

    private fun owners(): Map<Cell, Blocker> {
        val owners = HashMap<Cell, Blocker>()
        for (cone in level.cones) owners[cone] = Blocker.Obstacle(cone, ObstacleKind.CONE)
        for (crate in fallenCrates) owners[crate] = Blocker.Obstacle(crate, ObstacleKind.CRATE)
        for (car in onBoard.values) for (cell in car.cells) owners[cell] = Blocker.Vehicle(car)
        return owners
    }

    /** Ce qui est le plus proche dans la voie de [car] dans ce sens, ou `null` si la voie est libre. */
    fun blockerOf(car: Car, move: Move = Move.FORWARD): Blocker? {
        val owners = owners()
        return car.path(level.cols, level.rows, move).firstNotNullOfOrNull { owners[it] }
    }

    fun canMove(car: Car, move: Move): Boolean = blockerOf(car, move) == null

    private fun freeCellsAhead(car: Car, move: Move): Int {
        val owners = owners()
        val path = car.path(level.cols, level.rows, move)
        val index = path.indexOfFirst { it in owners }
        return if (index < 0) path.size else index
    }

    /** Un véhicule est glissé (ou touché : un simple toucher = marche avant) dans ce sens. */
    fun tap(id: Int, move: Move = Move.FORWARD): TapResult {
        val car = onBoard[id] ?: return TapResult.Ignored
        val blocker = blockerOf(car, move)
        if (blocker != null) {
            bumps++
            return TapResult.Bumped(car, move, blocker, freeCellsAhead(car, move))
        }
        val path = car.path(level.cols, level.rows, move).size
        onBoard.remove(id)
        exited++
        val dropped = if (car.carrier) car.trail(move).also { fallenCrates += it } else null
        return TapResult.Exited(car, move, path, dropped)
    }

    /** Peut-on encore tout faire sortir depuis cet état ? Faux seulement si une caisse mal placée a fermé le dernier passage. */
    val isStuck: Boolean
        get() = onBoard.isNotEmpty() && !Solver.canFinish(level.cols, level.rows, fixedObstacles(), onBoard.values.toList())

    /**
     * Un coup qui mène à la solution : un véhicule libre, dans un sens où il peut sortir, qui en débloque le plus d'autres. Les coups qui
     * conduiraient à une impasse (un camion qui laisserait tomber sa caisse au mauvais endroit) ne sont jamais conseillés. `null` si le plateau
     * est vide ou si plus rien ne peut sortir.
     */
    fun hint(): Hint? {
        val remaining = onBoard.values.toList()
        val fixed = fixedObstacles()
        val candidates = remaining.flatMap { car -> Move.entries.filter { canMove(car, it) }.map { Hint(car, it) } }
            .filter { h ->
                val rest = remaining - h.car
                val crates = if (h.car.carrier) fixed + h.car.trail(h.move) else fixed
                Solver.canFinish(level.cols, level.rows, crates, rest)
            }
        if (candidates.isEmpty()) return null
        val dependants = candidates.associateWith { h ->
            val cells = h.car.cells.toSet()
            remaining.count { other ->
                other.id != h.car.id && Move.entries.any { m -> other.path(level.cols, level.rows, m).any { it in cells } }
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
