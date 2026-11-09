package com.kcharignon.petitsjeux.games.parking.engine

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Générateur de niveaux **déterministe** : le niveau n est toujours le même (les étoiles des joueurs sont rangées par numéro de niveau),
 * et **toujours soluble** (vérifié par [Solver], caisses comprises).
 *
 * 240 niveaux en 4 groupes de 60 ; à chaque groupe, une nouvelle mécanique :
 * 1. 1 à 60 : voitures, camions, bus ; chaque véhicule roule en **marche avant ou arrière** selon le sens où on le glisse ;
 * 2. 61 à 120 : des **plots de chantier** qui bloquent des cases ;
 * 3. 121 à 180 : un **parking agrandi** (jusqu'à 8 x 10) ;
 * 4. 181 à 240 : des **pick-up qui laissent tomber une caisse** sur la case de leur queue, qui la bloque.
 *
 * Il ne dépend ni de `kotlin.random` ni de la date : son propre générateur (SplitMix64) garantit les mêmes niveaux d'une version à l'autre.
 * Toute modification de cet algorithme change les niveaux : les tests d'empreinte le détectent exprès.
 */
object LevelGenerator {
    /** Nombre de couleurs de véhicule dans l'interface. */
    const val COLORS = 9

    private const val CANDIDATES = 120
    private val cache = ConcurrentHashMap<Int, Level>()

    /**
     * Réglages d'un niveau. [longShare] : part de véhicules de 3 cases ou plus ; [busShare] : part de ceux-ci qui font 4 cases ;
     * [cones] : plots de chantier ; [carriers] : pick-up qui laissent tomber une caisse.
     */
    data class Params(
        val cols: Int,
        val rows: Int,
        val cars: Int,
        val longShare: Double,
        val busShare: Double,
        val cones: Int,
        val carriers: Int,
        val wantedDepth: Int,
    )

    // Par groupe : grille, nombre de véhicules, de plots et de caisses au premier puis au dernier niveau du groupe (montée en continu).
    private class GroupSpec(
        val cols0: Int, val rows0: Int, val cars0: Int, val cols1: Int, val rows1: Int, val cars1: Int,
        val cones0: Int = 0, val cones1: Int = 0, val carriers0: Int = 0, val carriers1: Int = 0,
    )

    private val groups = listOf(
        GroupSpec(4, 5, 3, 6, 8, 16),
        GroupSpec(6, 7, 12, 7, 9, 20, cones0 = 1, cones1 = 5),
        GroupSpec(7, 9, 18, 8, 10, 24, cones0 = 3, cones1 = 7),
        GroupSpec(8, 10, 20, 8, 10, 25, cones0 = 4, cones1 = 8, carriers0 = 1, carriers1 = 3),
    )

    private fun lerp(a: Int, b: Int, t: Double) = (a + (b - a) * t).roundToInt()

    fun paramsFor(index: Int): Params {
        require(index >= 1) { "les niveaux commencent à 1" }
        val capped = index.coerceAtMost(Worlds.LEVEL_COUNT)
        val g = groups[Worlds.groupOf(capped)]
        val t = (capped - Worlds.groupOf(capped) * Worlds.LEVELS_PER_GROUP - 1) / (Worlds.LEVELS_PER_GROUP - 1.0)
        val overall = (capped - 1) / (Worlds.LEVEL_COUNT - 1.0)
        return Params(
            cols = lerp(g.cols0, g.cols1, t),
            rows = lerp(g.rows0, g.rows1, t),
            cars = lerp(g.cars0, g.cars1, t),
            longShare = if (capped <= 3) 0.0 else 0.15 + 0.15 * overall,
            busShare = if (capped <= 40) 0.0 else 0.25 + 0.15 * (capped - 41) / (Worlds.LEVEL_COUNT - 41.0),
            cones = lerp(g.cones0, g.cones1, t),
            carriers = lerp(g.carriers0, g.carriers1, t),
            wantedDepth = (2 + 8 * overall).roundToInt(),
        )
    }

    fun level(index: Int): Level = cache.getOrPut(index) { generate(index) }

    internal fun generate(index: Int): Level {
        val params = paramsFor(index)
        var best: Level? = null
        var bestScore = Int.MAX_VALUE
        for (k in 0 until CANDIDATES) {
            val seed = index.toLong() * 1_000_003L + k
            val (cars, cones) = place(SplitMix64(seed), params)
            val level = Level(index, params.cols, params.rows, cars, cones)
            val a = Solver.analyse(level)
            if (!a.solvable) continue // jamais pour les niveaux sans caisse : place() ne pose que des véhicules qui gardent le niveau soluble
            // Le bon nombre de véhicules d'abord, puis la profondeur voulue, puis peu de véhicules libres au départ ;
            // les premiers niveaux doivent obliger à reculer (c'est ce qu'ils enseignent), les derniers à bien choisir où tombe la caisse.
            var score = (params.cars - cars.size) * 1000 + abs(a.depth - params.wantedDepth) * 10 + a.freeAtStart * 3
            if (index in 2..12 && a.backwardOnly == 0) score += 40
            if (params.carriers > 0 && index >= 190 && a.traps == 0) score += 40
            if (score < bestScore) {
                bestScore = score
                best = level
            }
        }
        return checkNotNull(best) { "aucun niveau généré pour $index" }
    }

    /** Pose des plots, puis des véhicules au hasard, dans un ordre mélangé, en refusant toute pose qui rendrait le niveau insoluble. */
    private fun place(rng: SplitMix64, p: Params): Pair<List<Car>, List<Cell>> {
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
            if (!Solver.canFinish(p.cols, p.rows, cones, chosen + candidate)) continue
            chosen += candidate
            used += candidate.cells
            when (candidate.len) {
                3 -> trucks++
                4 -> buses++
                else -> small++
            }
            if (carrier) carriers++
        }
        return paint(chosen, rng) to cones
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
