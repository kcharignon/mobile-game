package com.kcharignon.petitsjeux.games.road.engine

import java.util.concurrent.ConcurrentHashMap

/**
 * Les 48 niveaux de « Poussin Traverse », **déterministes** : le niveau n est toujours le même sur tous les téléphones (les étoiles sont rangées par numéro),
 * et **toujours faisable** : chaque niveau est accepté seulement si le [Planner] trouve une traversée sans aucune touche, avec une marge de réaction.
 *
 * - **Voies** : 2 + monde + (indice dans le monde) / 3. Les voies du bas vont vers la droite, celles du haut vers la gauche (double sens).
 *   Un terre-plein au milieu dès 4 voies, et jamais plus de 3 voies d'affilée sans refuge.
 * - **Véhicules** : chaque monde en ajoute (scooters et voitures, puis camionnettes et tracteurs, camions et bus, taxis et ambulances, voitures de sport) ; les
 *   voies sont lentes, normales, lourdes ou rapides, jamais mélangées dans une même voie.
 * - **Difficulté** : de niveau 1 à 48, la fenêtre de passage entre deux véhicules descend de 3 s à 1,1 s et la vitesse monte de 1,6 à 2,9 cases/s (avant le facteur de la voie).
 * - **Voies ferrées** : une avant l'arrivée à partir du monde 6, une de plus au milieu au monde 8.
 * - **Graines** : posées par le robot le long d'un chemin sans touche ([Planner.placeGrains]).
 */
object RoadLevels {
    private const val MAX_ATTEMPTS = 80
    private val cache = ConcurrentHashMap<Int, RoadLevel>()

    /** Le niveau [n] s'il est déjà calculé, sinon `null` (pour ne pas bloquer l'écran). */
    fun cached(n: Int): RoadLevel? = cache[n]

    /** Le niveau [n] (1 à 48), calculé une fois puis gardé. */
    fun level(n: Int): RoadLevel {
        require(n in 1..RoadWorlds.LEVEL_COUNT) { "niveau $n" }
        return cache.getOrPut(n) { generate(n) }
    }

    private enum class LaneClass(val speedFactor: Double) { SLOW(0.7), NORMAL(1.0), HEAVY(0.85), FAST(1.45) }

    private fun difficulty(n: Int): Double = (n - 1) / (RoadWorlds.LEVEL_COUNT - 1.0)

    internal fun generate(n: Int): RoadLevel {
        for (attempt in 0 until MAX_ATTEMPTS) {
            val rng = Rng(n * 1_000_003L + attempt * 7919L + 17L)
            val rows = buildRows(n, rng)
            val bare = RoadLevel(n, rows, emptyList())
            val planner = Planner(bare)
            if (!planner.feasible()) continue
            val grains = planner.placeGrains(rng, chance = 0.7)
            return RoadLevel(n, rows, grains)
        }
        error("niveau $n : aucune traversée sans touche trouvée en $MAX_ATTEMPTS essais")
    }

    /** Découpe [n] voies en groupes d'au plus 3, de tailles les plus égales possible. */
    internal fun chunks(n: Int): List<Int> {
        if (n <= 0) return emptyList()
        val k = (n + 2) / 3
        return List(k) { n / k + if (it < n % k) 1 else 0 }
    }

    private class Plan(val kind: RowKind, val direction: Int)

    /** L'ordre des lignes, du départ à l'arrivée. */
    private fun layout(lanes: Int, world: Int): List<Plan> {
        val a = (lanes + 1) / 2
        val b = lanes - a
        val rails = RoadWorlds.rails(world)
        val out = ArrayList<Plan>()
        out += Plan(RowKind.START, 0)
        for ((i, size) in chunks(a).withIndex()) {
            if (i > 0) out += Plan(RowKind.REFUGE, 0)
            repeat(size) { out += Plan(RowKind.ROAD, +1) }
        }
        if (lanes >= 4) out += Plan(RowKind.REFUGE, 0)
        if (rails >= 2) {
            if (lanes < 4) out += Plan(RowKind.REFUGE, 0)
            out += Plan(RowKind.RAIL, +1)
            out += Plan(RowKind.REFUGE, 0)
        }
        for ((i, size) in chunks(b).withIndex()) {
            if (i > 0) out += Plan(RowKind.REFUGE, 0)
            repeat(size) { out += Plan(RowKind.ROAD, -1) }
        }
        if (rails >= 1) {
            out += Plan(RowKind.REFUGE, 0)
            out += Plan(RowKind.RAIL, if (rails >= 2) -1 else +1)
        }
        out += Plan(RowKind.FINISH, 0)
        return out
    }

    private fun buildRows(n: Int, rng: Rng): List<Row> {
        val world = RoadWorlds.of(n)
        var railCount = 0
        return layout(RoadWorlds.lanes(n), world).map { plan ->
            when (plan.kind) {
                RowKind.ROAD -> Row(RowKind.ROAD, stream(n, world, plan.direction, rng))
                RowKind.RAIL -> Row(RowKind.RAIL, trainLine(n, plan.direction, railCount++, rng))
                else -> Row(plan.kind)
            }
        }
    }

    private fun laneClass(world: Int, rng: Rng): LaneClass {
        val options = ArrayList<Pair<LaneClass, Int>>()
        options += LaneClass.NORMAL to 6
        options += LaneClass.SLOW to 3
        if (world >= 2) options += LaneClass.HEAVY to 3
        if (world >= 3) options += LaneClass.FAST to 2
        var pick = rng.nextInt(options.sumOf { it.second })
        for ((c, w) in options) {
            if (pick < w) return c
            pick -= w
        }
        return LaneClass.NORMAL
    }

    private fun pool(c: LaneClass, world: Int): List<VehicleKind> = when (c) {
        LaneClass.SLOW -> buildList {
            add(VehicleKind.SCOOTER)
            add(VehicleKind.SCOOTER)
            if (world >= 1) {
                add(VehicleKind.TRACTOR)
                add(VehicleKind.VAN)
            }
        }
        LaneClass.NORMAL -> buildList {
            add(VehicleKind.CAR)
            add(VehicleKind.CAR)
            add(VehicleKind.CAR)
            if (world >= 1) add(VehicleKind.VAN)
            if (world >= 3) add(VehicleKind.TAXI)
        }
        LaneClass.HEAVY -> listOf(VehicleKind.TRUCK, VehicleKind.TRUCK, VehicleKind.BUS)
        LaneClass.FAST -> buildList {
            add(VehicleKind.AMBULANCE)
            if (world >= 4) {
                add(VehicleKind.SPORT)
                add(VehicleKind.SPORT)
            }
        }
    }

    private fun stream(n: Int, world: Int, direction: Int, rng: Rng): Stream {
        val d = difficulty(n)
        val cls = laneClass(world, rng)
        val kinds = pool(cls, world)
        val speed = (1.6 + 1.3 * d) * cls.speedFactor * (0.92 + 0.16 * rng.nextDouble())
        val window = 3.0 - 1.9 * d
        val baseGap = speed * window + 0.5
        val slots = ArrayList<Slot>()
        var period = 0.0
        while (slots.size < 2 || period < Road.COLS + 6) {
            val kind = kinds[rng.nextInt(kinds.size)]
            val gap = baseGap * (1.0 + 0.7 * rng.nextDouble())
            slots += Slot(kind, rng.nextInt(9), gap)
            period += kind.length + gap
        }
        return Stream(direction, speed, slots, rng.nextDouble() * period)
    }

    private fun trainLine(n: Int, direction: Int, index: Int, rng: Rng): TrainLine {
        val world = RoadWorlds.of(n)
        val cycle = 10.0 - (world - 5) * 1.0 // 10 s au monde 6, 9 s, 8 s
        val warning = 2.6 - (world - 5) * 0.2
        val offset = rng.nextDouble() * cycle + index * 3.1
        return TrainLine(direction, cycle, warning, speed = 12.0, length = 14.0, offset = offset)
    }
}
