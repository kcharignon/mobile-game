package com.kcharignon.petitsjeux.games.parking.engine

/**
 * Qui attend qui. Une voiture ne roule que droit devant elle et ne revient jamais une fois sortie : elle peut donc sortir
 * si et seulement si aucune voiture restante n'est dans sa voie. « A attend B » (B est dans la voie de A) : le niveau est soluble
 * si et seulement si ces attentes ne forment aucun cycle.
 */
class Dependencies private constructor(
    /** Pour chaque voiture, celles qui sont dans sa voie. */
    val blockers: Map<Int, Set<Int>>,
    /** Pour chaque voiture, le nombre de « vagues » à attendre avant qu'elle puisse sortir (0 = libre tout de suite). */
    val wave: Map<Int, Int>,
) {
    /** Nombre de vagues : la longueur de la plus longue chaîne d'attente. */
    val depth: Int get() = (wave.values.maxOrNull() ?: -1) + 1

    /** Voitures libres au départ. */
    val freeAtStart: Int get() = wave.count { it.value == 0 }

    companion object {
        /** `null` s'il y a un cycle (niveau insoluble). */
        fun of(cars: List<Car>, cols: Int, rows: Int): Dependencies? {
            val owner = HashMap<Cell, Int>()
            for (car in cars) for (cell in car.cells) owner[cell] = car.id
            val blockers = cars.associate { car ->
                car.id to car.path(cols, rows).mapNotNullTo(LinkedHashSet()) { owner[it] }
            }
            val wave = HashMap<Int, Int>()
            val left = cars.mapTo(HashSet()) { it.id }
            var round = 0
            while (left.isNotEmpty()) {
                val ready = left.filter { id -> blockers.getValue(id).none { it in left } }
                if (ready.isEmpty()) return null
                for (id in ready) wave[id] = round
                left.removeAll(ready.toSet())
                round++
            }
            return Dependencies(blockers, wave)
        }

        fun of(level: Level): Dependencies? = of(level.cars, level.cols, level.rows)
    }
}
