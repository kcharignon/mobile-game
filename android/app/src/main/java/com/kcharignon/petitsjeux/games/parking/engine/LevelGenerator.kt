package com.kcharignon.petitsjeux.games.parking.engine

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Générateur de niveaux **déterministe** : le niveau n est toujours le même (les étoiles des joueurs sont rangées par numéro de niveau),
 * et **toujours soluble** (vérifié par [Solver], murs et caisses compris).
 *
 * 240 niveaux. Ils **évoluent par tranches de 30** : le parking **s'agrandit tous les 30 niveaux** (la difficulté repart un peu plus bas dans le nouveau
 * parking, puis remonte tranche après tranche), et une **mécanique de plus s'ajoute tous les 60 niveaux** : les anciennes restent, elles se cumulent.
 *
 * | Niveaux | Mécanique ajoutée |
 * |---|---|
 * | 1-60 | chaque véhicule roule en marche avant ou arrière selon le sens où on le glisse |
 * | 61-120 | des plots de chantier qui bloquent des cases (1 au début, puis de plus en plus) |
 * | 121-180 | des murs sur le bord du parking qui murent certaines sorties (1 au début, puis de plus en plus) |
 * | 181-240 | des pick-up qui laissent tomber une caisse sur la case de leur queue, qui la bloque |
 *
 * Les premiers niveaux d'une mécanique sont plus simples (moins de véhicules) pour la découvrir : 55 %, puis 75 %, puis 90 % du nombre normal.
 *
 * Il ne dépend ni de `kotlin.random` ni de la date : son propre générateur (SplitMix64) garantit les mêmes niveaux d'une version à l'autre.
 * Toute modification de cet algorithme change les niveaux : les tests d'empreinte le détectent exprès.
 */
object LevelGenerator {
    /** Nombre de couleurs de véhicule dans l'interface. */
    const val COLORS = 9

    private const val CANDIDATES = 120
    private val cache = ConcurrentHashMap<Int, Level>()

    /** Taille du parking (colonnes, lignes) pour chaque tranche de 30 niveaux : il s'agrandit tous les 30 niveaux. */
    val sizes = listOf(5 to 6, 6 to 7, 6 to 8, 7 to 8, 7 to 9, 8 to 9, 8 to 10, 9 to 10)

    /**
     * Réglages d'un niveau. [longShare] : part de véhicules de 3 cases ou plus ; [busShare] : part de ceux-ci qui font 4 cases ;
     * [cones] : plots de chantier ; [walls] : sorties murées ; [carriers] : pick-up qui laissent tomber une caisse.
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
        val wantedDepth: Int,
    )

    /** Facteur de douceur des premiers niveaux d'une mécanique (et des tout premiers du jeu) : 3 niveaux à 55 %, 3 à 75 %, 3 à 90 %, puis 100 %. */
    fun introFactor(index: Int): Double = when (((index - 1) % Worlds.LEVELS_PER_GROUP) / 3) {
        0 -> 0.55
        1 -> 0.75
        2 -> 0.9
        else -> 1.0
    }

    fun paramsFor(index: Int): Params {
        require(index >= 1) { "les niveaux commencent à 1" }
        val n = index.coerceAtMost(Worlds.LEVEL_COUNT)
        val overall = (n - 1) / (Worlds.LEVEL_COUNT - 1.0)
        val block = (n - 1) / 30 // le parking s'agrandit tous les 30 niveaux
        val positionInBlock = ((n - 1) % 30) / 29.0
        val (cols, rows) = sizes[block]
        val intro = introFactor(n)

        // 1 au début de la mécanique, puis de plus en plus : 8 plots et 8 murs, 3 pick-up à la fin
        val cones = if (n >= 61) (1 + 7 * (n - 61) / 179.0).roundToInt() else 0
        val walls = if (n >= 121) (1 + 7 * (n - 121) / 119.0).roundToInt() else 0
        val carriers = if (n >= 181) (1 + 2 * (n - 181) / 59.0).roundToInt() else 0
        val longShare = if (n <= 3) 0.0 else 0.15 + 0.15 * overall
        val busShare = if (n <= 40) 0.0 else 0.25 + 0.15 * (n - 41) / (Worlds.LEVEL_COUNT - 41.0)

        // Remplissage visé : monte avec la progression, et remonte un peu à l'intérieur de chaque tranche de 30 niveaux (puis le parking s'agrandit : un palier).
        val density = 0.42 + 0.30 * overall + 0.08 * positionInBlock - 0.03
        val area = cols * rows - cones
        val averageLength = 2.0 + longShare * 1.2
        val cars = maxOf(3, (intro * density * area / averageLength).roundToInt())
        val wantedDepth = maxOf(2, ((2 + 8 * overall) * (0.6 + 0.4 * intro)).roundToInt())
        return Params(cols, rows, cars, longShare, busShare, cones, walls, carriers, wantedDepth)
    }

    fun level(index: Int): Level = cache.getOrPut(index) { generate(index) }

    internal fun generate(index: Int): Level {
        val params = paramsFor(index)
        var best: Level? = null
        var bestScore = Int.MAX_VALUE
        for (k in 0 until CANDIDATES) {
            val seed = index.toLong() * 1_000_003L + k
            val placed = place(SplitMix64(seed), params)
            val level = Level(index, params.cols, params.rows, placed.cars, placed.cones, placed.walls)
            val a = Solver.analyse(level)
            if (!a.solvable) continue
            // Le bon nombre de véhicules d'abord, puis la profondeur voulue, puis peu de véhicules libres au départ ; les plots et les murs doivent
            // servir à quelque chose ; les premiers niveaux obligent à reculer ; les derniers à bien choisir où tombe la caisse.
            var score = (params.cars - placed.cars.size) * 1000 + abs(a.depth - params.wantedDepth) * 10 + a.freeAtStart * 3
            if (index in 2..12 && a.backwardOnly == 0) score += 40
            if (params.cones > 0 && a.coneBlocks == 0) score += 25
            if (params.walls > 0 && a.wallBlocks == 0) score += 25
            if (params.carriers > 0 && index >= 190 && a.traps == 0) score += 40
            if (score < bestScore) {
                bestScore = score
                best = level
            }
        }
        return checkNotNull(best) { "aucun niveau généré pour $index" }
    }

    private class Placed(val cars: List<Car>, val cones: List<Cell>, val walls: List<Wall>)

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

        val maxLength = if (p.busShare > 0) 4 else 3
        val poses = ArrayList<Car>()
        for (dir in Dir.entries) for (len in 2..maxLength) {
            val rowsFit = p.rows - if (dir.horizontal) 0 else len - 1
            val colsFit = p.cols - if (dir.horizontal) len - 1 else 0
            for (row in 0 until rowsFit) for (col in 0 until colsFit) {
                val pose = Car(0, col, row, len, dir, 0)
                if (pose.cells.none { it in coneSet }) poses += pose
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
            if (!Solver.canFinish(p.cols, p.rows, cones, chosen + candidate, walls)) continue
            chosen += candidate
            used += candidate.cells
            when (candidate.len) {
                3 -> trucks++
                4 -> buses++
                else -> small++
            }
            if (carrier) carriers++
        }
        return Placed(paint(chosen, rng), cones, walls)
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
