package com.kcharignon.petitsjeux.games.parking.engine

/** Ce qui se passe quand on touche une voiture. */
sealed interface TapResult {
    /** La voie est libre : la voiture sort. [pathLength] = cases entre son nez et le bord du plateau. */
    data class Exited(val car: Car, val pathLength: Int) : TapResult

    /** Une voiture bloque : la voiture avance de [freeCells] cases, klaxonne et recule. C'est une faute. */
    data class Bumped(val car: Car, val blocker: Car, val freeCells: Int) : TapResult

    /** La voiture n'est plus sur le plateau (déjà sortie) : rien ne se passe. */
    data object Ignored : TapResult
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

    /** Fautes commises (voitures bloquées touchées). */
    var bumps: Int = 0
        private set

    /** Voitures sorties. */
    var exited: Int = 0
        private set

    val carsLeft: Int get() = onBoard.size
    val isWon: Boolean get() = onBoard.isEmpty()
    val stars: Int get() = starsForBumps(bumps)

    /** Voitures encore sur le plateau, dans l'ordre du niveau. */
    val cars: List<Car> get() = onBoard.values.toList()

    fun isOnBoard(id: Int): Boolean = id in onBoard

    private fun owners(): Map<Cell, Car> {
        val owners = HashMap<Cell, Car>()
        for (car in onBoard.values) for (cell in car.cells) owners[cell] = car
        return owners
    }

    /** La voiture la plus proche dans la voie de [car], ou `null` si la voie est libre. */
    fun blockerOf(car: Car): Car? {
        val owners = owners()
        return car.path(level.cols, level.rows).firstNotNullOfOrNull { owners[it] }
    }

    /** Cases vides entre le nez de [car] et la voiture qui la bloque (ou le bord si rien ne la bloque). */
    private fun freeCellsAhead(car: Car): Int {
        val owners = owners()
        val path = car.path(level.cols, level.rows)
        val index = path.indexOfFirst { it in owners }
        return if (index < 0) path.size else index
    }

    fun tap(id: Int): TapResult {
        val car = onBoard[id] ?: return TapResult.Ignored
        val blocker = blockerOf(car)
        if (blocker != null) {
            bumps++
            return TapResult.Bumped(car, blocker, freeCellsAhead(car))
        }
        val path = car.path(level.cols, level.rows).size
        onBoard.remove(id)
        exited++
        return TapResult.Exited(car, path)
    }

    /**
     * Une voiture qui peut sortir tout de suite et qui en débloque le plus d'autres (la plus utile), ou `null` si le plateau est vide.
     * Faire sortir une voiture libre ne gêne jamais personne : n'importe quelle voiture libre mène à la solution.
     */
    fun hint(): Car? {
        val free = onBoard.values.filter { blockerOf(it) == null }
        if (free.isEmpty()) return null
        val dependants = free.associateWith { candidate ->
            val cells = candidate.cells.toSet()
            onBoard.values.count { other -> other.id != candidate.id && other.path(level.cols, level.rows).any { it in cells } }
        }
        return free.maxWith(compareBy<Car> { dependants.getValue(it) }.thenBy { -it.id })
    }
}
