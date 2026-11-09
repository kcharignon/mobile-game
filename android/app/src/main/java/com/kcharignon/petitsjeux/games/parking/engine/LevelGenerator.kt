package com.kcharignon.petitsjeux.games.parking.engine

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Générateur de niveaux **déterministe** : le niveau n est toujours le même (les étoiles des joueurs sont rangées par numéro de niveau),
 * et **toujours soluble** (vérifié par [Solver], murs, huile et caisses compris).
 *
 * 180 niveaux en 9 mondes de 20. **Tous les 20 niveaux, le parking s'agrandit ET une nouvelle mécanique arrive** ; les anciennes restent, elles se cumulent :
 *
 * | Niveaux | Parking | Mécanique ajoutée |
 * |---|---|---|
 * | 1-20 | 5x6 | chaque véhicule roule en marche avant ou arrière selon le sens où on le glisse |
 * | 21-40 | 6x7 | des plots de chantier qui bloquent des cases |
 * | 41-60 | 7x8 | des murs sur le bord du parking qui murent certaines sorties |
 * | 61-80 | 8x9 | des pick-up qui laissent tomber une caisse sur la case de leur queue, qui la bloque |
 * | 81-100 | 9x10 | des taches d'huile : le véhicule y fait un virage à 90°, à droite s'il avance, à gauche s'il recule |
 * | 101-120 | 10x11 | des sens uniques : on ne les franchit que dans le sens de la flèche |
 * | 121-140 | 10x12 | des tapis roulants : ils poussent d'une case de côté le véhicule qui les traverse |
 * | 141-160 | 11x12 | des téléporteurs : on entre par une case, on ressort par l'autre |
 * | 161-180 | 11x13 | des barrières qui s'ouvrent et se ferment à chaque véhicule qui sort |
 *
 * Plots, murs, pick-up, taches, sens uniques, tapis, téléporteurs et barrières : **1 au début de leur mécanique, puis de plus en plus**. Les premiers niveaux
 * de chaque monde sont plus simples (moins de véhicules) pour découvrir la nouveauté : 55 %, puis 75 %, puis 90 % du nombre normal.
 *
 * **Les 100 premiers niveaux n'ont jamais changé** depuis leur publication (les étoiles des joueurs y sont attachées) : rien de ce qui sert aux niveaux 1 à 100
 * ne doit changer, ni les formules, ni l'ordre des tirages au hasard (un test d'empreinte le vérifie).
 *
 * Il ne dépend ni de `kotlin.random` ni de la date : son propre générateur (SplitMix64) garantit les mêmes niveaux d'une version à l'autre.
 * Toute modification de cet algorithme change les niveaux : les tests d'empreinte le détectent exprès.
 */
object LevelGenerator {
    /** Nombre de couleurs de véhicule dans l'interface. */
    const val COLORS = 9

    private const val CANDIDATES = 120
    private val cache = ConcurrentHashMap<Int, Level>()

    /** Taille du parking (colonnes, lignes) de chaque monde : il s'agrandit tous les 20 niveaux. */
    val sizes = listOf(5 to 6, 6 to 7, 7 to 8, 8 to 9, 9 to 10, 10 to 11, 10 to 12, 11 to 12, 11 to 13)

    /**
     * Réglages d'un niveau. [longShare] : part de véhicules de 3 cases ou plus ; [busShare] : part de ceux-ci qui font 4 cases ;
     * [cones] : plots de chantier ; [walls] : sorties murées ; [carriers] : pick-up qui laissent tomber une caisse ; [oils] : taches d'huile ;
     * [oneWays] : sens uniques ; [belts] : tapis roulants ; [portals] : paires de téléporteurs ; [gates] : barrières.
     */
    data class Params(
        val cols: Int,
        val rows: Int,
        val cars: Int,
        val longShare: Double,
        val busShare: Double,
        val cones: Int,
        val walls: Int,
        val carriers: Int,
        val oils: Int,
        val oneWays: Int,
        val belts: Int,
        val portals: Int,
        val gates: Int,
        val wantedDepth: Int,
    ) {
        /** Cases du sol qui ne peuvent pas porter de véhicule. */
        val floorCells: Int get() = cones + oils + oneWays + belts + 2 * portals + gates
    }

    /** Facteur de douceur des premiers niveaux d'un monde (la nouveauté arrive) : 3 niveaux à 55 %, 3 à 75 %, 3 à 90 %, puis 100 %. */
    fun introFactor(index: Int): Double = when (((index - 1) % Worlds.LEVELS_PER_WORLD) / 3) {
        0 -> 0.55
        1 -> 0.75
        2 -> 0.9
        else -> 1.0
    }

    /** Niveaux dont la formule d'origine ne bouge plus : les 100 premiers. */
    private const val CLASSIC = 100

    fun paramsFor(index: Int): Params {
        require(index >= 1) { "les niveaux commencent à 1" }
        val n = index.coerceAtMost(Worlds.LEVEL_COUNT)
        val world = (n - 1) / Worlds.LEVELS_PER_WORLD // 0 à 8 : un monde = un parking plus grand + une mécanique de plus
        val overall = (n - 1) / (CLASSIC - 1.0)
        val positionInWorld = ((n - 1) % Worlds.LEVELS_PER_WORLD) / (Worlds.LEVELS_PER_WORLD - 1.0)
        val (cols, rows) = sizes[world]
        val intro = introFactor(n)
        val late = (n - CLASSIC).coerceAtLeast(0) // niveaux après les 100 premiers

        // 1 au début de la mécanique, puis de plus en plus
        val cones = if (n >= 21) (1 + 4 * (n - 21) / 79.0).roundToInt() else 0
        val walls = if (n >= 41) (if (late == 0) (1 + 5 * (n - 41) / 59.0).roundToInt() else 6 + late / 20) else 0
        val carriers = if (n >= 61) (if (late == 0) (1 + 2 * (n - 61) / 39.0).roundToInt() else 3 + late / 40) else 0
        val oils = if (n >= 81) (if (late == 0) (1 + 4 * (n - 81) / 19.0).roundToInt() else 5 + late / 20) else 0
        val oneWays = if (n >= 101) (1 + 5 * (n - 101) / 79.0).roundToInt() else 0
        val belts = if (n >= 121) (1 + 4 * (n - 121) / 59.0).roundToInt() else 0
        val portals = if (n >= 141) (1 + 2 * (n - 141) / 39.0).roundToInt() else 0
        val gates = if (n >= 161) (1 + 3 * (n - 161) / 19.0).roundToInt() else 0
        val longShare = if (n <= 3) 0.0 else if (late == 0) 0.15 + 0.15 * overall else 0.30 + 0.05 * late / 80.0
        val busShare = if (world < 2) 0.0 else minOf(0.25 + 0.075 * (world - 2), 0.5)

        // Remplissage visé : monte avec la progression, et un peu à l'intérieur de chaque monde.
        val density = if (late == 0) 0.36 + 0.34 * overall + 0.10 * (positionInWorld - 0.5) else 0.70 + 0.05 * late / 80.0 + 0.10 * (positionInWorld - 0.5)
        val floorCells = cones + oils + oneWays + belts + 2 * portals + gates
        val area = cols * rows - floorCells
        val averageLength = 2.0 + longShare * 1.2
        val cars = maxOf(3, (intro * density * area / averageLength).roundToInt())
        val depthProgress = if (late == 0) overall else 1.0 + 0.4 * late / 80.0
        val wantedDepth = maxOf(2, ((2 + 7 * depthProgress + 1.5 * positionInWorld) * (0.6 + 0.4 * intro)).roundToInt())
        return Params(cols, rows, cars, longShare, busShare, cones, walls, carriers, oils, oneWays, belts, portals, gates, wantedDepth)
    }

    fun level(index: Int): Level = cache.getOrPut(index) { generate(index) }

    internal fun generate(index: Int): Level {
        val params = paramsFor(index)
        var best: Level? = null
        var bestScore = Int.MAX_VALUE
        for (k in 0 until CANDIDATES) {
            val seed = index.toLong() * 1_000_003L + k
            val placed = place(SplitMix64(seed), params)
            val level = Level(index, params.cols, params.rows, placed.cars, placed.cones, placed.walls, placed.oils, placed.oneWays, placed.belts, placed.portals, placed.gates)
            val a = Solver.analyse(level)
            if (!a.solvable) continue
            // Le bon nombre de véhicules d'abord, puis la profondeur voulue, puis peu de véhicules libres au départ ; les plots et les murs doivent
            // servir à quelque chose ; les premiers niveaux obligent à reculer ; les derniers à bien choisir où tombe la caisse.
            var score = (params.cars - placed.cars.size) * 1000 + abs(a.depth - params.wantedDepth) * 10 + a.freeAtStart * 3
            if (index in 2..12 && a.backwardOnly == 0) score += 40
            if (params.cones > 0 && a.coneBlocks == 0) score += 25
            if (params.walls > 0 && a.wallBlocks == 0) score += 25
            if (params.oils > 0 && a.oilTurns == 0) score += 25
            if (params.oneWays > 0 && a.oneWayBlocks == 0) score += 25
            if (params.belts > 0 && a.beltPushes == 0) score += 25
            if (params.portals > 0 && a.portalJumps == 0) score += 25
            if (params.gates > 0 && a.gateBlocks == 0) score += 25
            if (params.carriers > 0 && index >= 70 && a.traps == 0) score += 40
            if (score < bestScore) {
                bestScore = score
                best = level
            }
        }
        return checkNotNull(best) { "aucun niveau généré pour $index" }
    }

    private class Placed(
        val cars: List<Car>,
        val cones: List<Cell>,
        val walls: List<Wall>,
        val oils: List<Cell>,
        val oneWays: List<Arrow>,
        val belts: List<Arrow>,
        val portals: List<Portal>,
        val gates: List<Gate>,
    )

    /** Pose les murs, les plots, puis des véhicules au hasard, dans un ordre mélangé, en refusant toute pose qui rendrait le niveau insoluble. */
    private fun place(rng: SplitMix64, p: Params): Placed {
        // les murs : des sorties du bord choisies au hasard
        val edges = ArrayList<Wall>()
        for (r in 0 until p.rows) for (c in 0 until p.cols) {
            if (r == 0) edges += Wall(Cell(c, r), Dir.UP)
            if (r == p.rows - 1) edges += Wall(Cell(c, r), Dir.DOWN)
            if (c == 0) edges += Wall(Cell(c, r), Dir.LEFT)
            if (c == p.cols - 1) edges += Wall(Cell(c, r), Dir.RIGHT)
        }
        rng.shuffle(edges)
        val walls = edges.take(p.walls)

        // les plots : des cases au hasard
        val allCells = ArrayList<Cell>()
        for (r in 0 until p.rows) for (c in 0 until p.cols) allCells += Cell(c, r)
        rng.shuffle(allCells)
        val cones = allCells.take(p.cones)
        val coneSet = cones.toSet()
        // les taches d'huile : d'autres cases au hasard (jamais sous un plot ni sous un véhicule : on les voit, et on roule dessus)
        val oils = allCells.drop(p.cones).take(p.oils)
        // sens uniques, tapis roulants, téléporteurs, barrières : encore d'autres cases (le tirage ne sert que s'il y en a : les anciens niveaux ne changent pas)
        var next = p.cones + p.oils
        val oneWays = allCells.drop(next).take(p.oneWays).map { Arrow(it, Dir.entries[rng.nextInt(4)]) }
        next += p.oneWays
        val belts = allCells.drop(next).take(p.belts).map { Arrow(it, Dir.entries[rng.nextInt(4)]) }
        next += p.belts
        val portals = (0 until p.portals).map { Portal(allCells[next + 2 * it], allCells[next + 2 * it + 1]) }
        next += 2 * p.portals
        val gates = allCells.drop(next).take(p.gates).map { Gate(it, rng.nextInt(2) == 0) }
        // le sol sans les barrières, pour les vérifications qui ne regardent pas l'ordre des sorties (les barrières, elles, sont vérifiées par l'analyse)
        val plainFloor = Floor(
            oils = oils.toSet(),
            oneWays = oneWays.associate { it.cell to it.dir },
            belts = belts.associate { it.cell to it.dir },
            portals = portals.flatMap { listOf(it.a to it.b, it.b to it.a) }.toMap(),
        )
        val blockedForCars = coneSet + oils.toSet() + oneWays.map { it.cell } + belts.map { it.cell } + portals.flatMap { listOf(it.a, it.b) } + gates.map { it.cell }

        val maxLength = if (p.busShare > 0) 4 else 3
        val poses = ArrayList<Car>()
        for (dir in Dir.entries) for (len in 2..maxLength) {
            val rowsFit = p.rows - if (dir.horizontal) 0 else len - 1
            val colsFit = p.cols - if (dir.horizontal) len - 1 else 0
            for (row in 0 until rowsFit) for (col in 0 until colsFit) {
                val pose = Car(0, col, row, len, dir, 0)
                if (pose.cells.none { it in blockedForCars }) poses += pose
            }
        }
        rng.shuffle(poses)

        val maxLong = (p.cars * p.longShare).roundToInt()
        val maxBuses = (maxLong * p.busShare).roundToInt()
        val maxTrucks = maxOf(maxLong - maxBuses, p.carriers)
        val smalls = p.cars - maxTrucks - maxBuses
        val chosen = ArrayList<Car>()
        val used = HashSet<Cell>()
        var trucks = 0
        var buses = 0
        var small = 0
        var carriers = 0
        for (pose in poses) {
            if (chosen.size >= p.cars) break
            when (pose.len) {
                3 -> if (trucks >= maxTrucks) continue
                4 -> if (buses >= maxBuses) continue
                else -> if (small >= smalls) continue
            }
            if (pose.cells.any { it in used }) continue
            val carrier = pose.len == 3 && carriers < p.carriers
            val candidate = pose.copy(id = chosen.size, carrier = carrier)
            if (!Solver.canFinish(p.cols, p.rows, cones, chosen + candidate, walls, plainFloor)) continue
            chosen += candidate
            used += candidate.cells
            when (candidate.len) {
                3 -> trucks++
                4 -> buses++
                else -> small++
            }
            if (carrier) carriers++
        }
        return Placed(paint(chosen, rng), cones, walls, oils, oneWays, belts, portals, gates)
    }

    /** Une couleur par véhicule, différente de celles des véhicules voisins. */
    private fun paint(cars: List<Car>, rng: SplitMix64): List<Car> {
        val painted = ArrayList<Car>(cars.size)
        for (car in cars) {
            val neighbours = painted.filter { other -> near(car, other) }.mapTo(HashSet()) { it.color }
            val allowed = (0 until COLORS).filter { it !in neighbours }.ifEmpty { (0 until COLORS).toList() }
            painted += car.copy(color = allowed[rng.nextInt(allowed.size)])
        }
        return painted
    }

    private fun near(a: Car, b: Car): Boolean =
        a.cells.any { x -> b.cells.any { y -> abs(x.col - y.col) <= 1 && abs(x.row - y.row) <= 1 } }

    /** SplitMix64 : petit générateur pseudo-aléatoire dont la suite est fixée pour toujours. */
    class SplitMix64(seed: Long) {
        private var state = seed

        fun nextLong(): Long {
            state += GOLDEN
            var z = state
            z = (z xor (z ushr 30)) * MIX1
            z = (z xor (z ushr 27)) * MIX2
            return z xor (z ushr 31)
        }

        /** Entier de 0 (inclus) à [bound] (exclu). */
        fun nextInt(bound: Int): Int = ((nextLong() ushr 33) % bound).toInt()

        fun <T> shuffle(list: MutableList<T>) {
            for (i in list.size - 1 downTo 1) {
                val j = nextInt(i + 1)
                val tmp = list[i]
                list[i] = list[j]
                list[j] = tmp
            }
        }

        private companion object {
            val GOLDEN = 0x9E3779B97F4A7C15UL.toLong()
            val MIX1 = 0xBF58476D1CE4E5B9UL.toLong()
            val MIX2 = 0x94D049BB133111EBUL.toLong()
        }
    }
}
