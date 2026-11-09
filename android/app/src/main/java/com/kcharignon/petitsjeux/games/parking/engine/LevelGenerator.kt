package com.kcharignon.petitsjeux.games.parking.engine

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Générateur de niveaux **déterministe** : le niveau n est toujours le même (les étoiles des joueurs sont rangées par numéro de niveau),
 * et **toujours soluble** (une voiture n'est posée que si les attentes entre voitures restent sans cycle : voir [Dependencies]).
 *
 * Il ne dépend ni de `kotlin.random` ni de la date : son propre générateur (SplitMix64) garantit les mêmes niveaux d'une version à l'autre.
 * Toute modification de cet algorithme change les niveaux : le test `LevelGeneratorTest.levelsNeverChange` le détecte exprès.
 */
object LevelGenerator {
    /** Nombre de couleurs de voiture dans l'interface. */
    const val COLORS = 9

    private const val CANDIDATES = 40
    private val cache = ConcurrentHashMap<Int, Level>()

    /** Réglages d'un niveau. */
    /** [longShare] : part de véhicules de 3 cases ou plus ; [busShare] : part de ceux-ci qui font 4 cases (bus, poids lourds : à partir du monde 3). */
    data class Params(val cols: Int, val rows: Int, val cars: Int, val longShare: Double, val busShare: Double, val wantedDepth: Int)

    // Par monde : grille et nombre de voitures au début puis à la fin (la grille grandit à mi-monde, le nombre de voitures monte en continu).
    private class WorldSpec(val cols0: Int, val rows0: Int, val cars0: Int, val cols1: Int, val rows1: Int, val cars1: Int)

    private val specs = listOf(
        WorldSpec(4, 5, 3, 5, 6, 8),
        WorldSpec(5, 6, 8, 6, 7, 12),
        WorldSpec(6, 7, 12, 6, 8, 16),
        WorldSpec(6, 8, 15, 7, 9, 20),
        WorldSpec(7, 9, 18, 7, 9, 24),
    )

    fun paramsFor(index: Int): Params {
        require(index >= 1) { "les niveaux commencent à 1" }
        val capped = index.coerceAtMost(Worlds.LEVEL_COUNT)
        val world = Worlds.of(capped)
        val spec = specs[world]
        val t = (capped - Worlds.firstLevel(world)) / (Worlds.LEVELS_PER_WORLD - 1.0)
        val overall = (capped - 1) / (Worlds.LEVEL_COUNT - 1.0)
        val late = t >= 0.5
        return Params(
            cols = if (late) spec.cols1 else spec.cols0,
            rows = if (late) spec.rows1 else spec.rows0,
            cars = (spec.cars0 + (spec.cars1 - spec.cars0) * t).roundToInt(),
            longShare = if (capped <= 3) 0.0 else 0.15 + 0.15 * overall,
            busShare = if (capped <= 2 * Worlds.LEVELS_PER_WORLD) 0.0 else 0.25 + 0.15 * (capped - 2 * Worlds.LEVELS_PER_WORLD - 1) / (Worlds.LEVEL_COUNT - 2 * Worlds.LEVELS_PER_WORLD - 1.0),
            wantedDepth = (2 + 9 * overall).roundToInt(),
        )
    }

    fun level(index: Int): Level = cache.getOrPut(index) { generate(index) }

    internal fun generate(index: Int): Level {
        val params = paramsFor(index)
        var best: Level? = null
        var bestScore = Int.MAX_VALUE
        for (k in 0 until CANDIDATES) {
            val seed = index.toLong() * 1_000_003L + k
            val cars = place(SplitMix64(seed), params)
            val deps = Dependencies.of(cars, params.cols, params.rows) ?: continue // jamais : place() ne crée pas de cycle
            // Le bon nombre de voitures d'abord, puis la profondeur d'attente voulue, puis peu de voitures libres au départ.
            val score = (params.cars - cars.size) * 1000 + abs(deps.depth - params.wantedDepth) * 10 + deps.freeAtStart
            if (score < bestScore) {
                bestScore = score
                best = Level(index, params.cols, params.rows, cars)
            }
        }
        return checkNotNull(best) { "aucun niveau généré pour $index" }
    }

    /** Pose des voitures au hasard, dans un ordre mélangé, en refusant toute pose qui crée un cycle d'attente. */
    private fun place(rng: SplitMix64, p: Params): List<Car> {
        val poses = ArrayList<Car>()
        val maxLength = if (p.busShare > 0) 4 else 3
        for (dir in Dir.entries) for (len in 2..maxLength) {
            val rowsFit = p.rows - if (dir.horizontal) 0 else len - 1
            val colsFit = p.cols - if (dir.horizontal) len - 1 else 0
            for (row in 0 until rowsFit) for (col in 0 until colsFit) poses += Car(0, col, row, len, dir, 0)
        }
        rng.shuffle(poses)

        val maxLong = (p.cars * p.longShare).roundToInt()
        val maxBuses = (maxLong * p.busShare).roundToInt()
        val maxTrucks = maxLong - maxBuses
        val chosen = ArrayList<Car>()
        val used = HashSet<Cell>()
        var trucks = 0
        var buses = 0
        for (pose in poses) {
            if (chosen.size >= p.cars) break
            if (pose.len == 3 && trucks >= maxTrucks) continue
            if (pose.len == 4 && buses >= maxBuses) continue
            if (pose.len == 2 && chosen.size - trucks - buses >= p.cars - maxLong) continue
            if (pose.cells.any { it in used }) continue
            val candidate = pose.copy(id = chosen.size)
            if (Dependencies.of(chosen + candidate, p.cols, p.rows) == null) continue
            chosen += candidate
            used += candidate.cells
            if (candidate.len == 3) trucks++ else if (candidate.len == 4) buses++
        }
        return paint(chosen, rng)
    }

    /** Une couleur par voiture, différente de celles des voitures voisines. */
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
