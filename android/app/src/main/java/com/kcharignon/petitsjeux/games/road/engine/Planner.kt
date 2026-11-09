package com.kcharignon.petitsjeux.games.road.engine

import java.util.BitSet
import kotlin.math.abs

/** Un saut du robot : au tick [tick] (de [Planner.TICK_MS] ms), dans la direction [dir]. */
class Move(val tick: Int, val dir: HopDir)

/**
 * Le **robot** qui prouve qu'un niveau se traverse sans jamais être touché, avec de la marge, et qui sait par où passer.
 *
 * Recherche en espace-temps : un état = (ligne, colonne, tick), un tick = 0,1 s, horizon 60 s. Actions : attendre un tick, ou sauter (2 ticks) d'une case.
 * Un état est **sûr** si aucun véhicule n'est à portée d'une zone **élargie** autour du poussin : la marge [reaction] (le temps de réaction d'un enfant) plus ce
 * que le véhicule parcourt en un tick (pour ne rien manquer entre deux ticks). Pendant un saut, il faut être sûr dans la ligne de départ **et** celle d'arrivée
 * (c'est plus prudent que le vrai moteur) : tout chemin du robot est donc aussi sans touche dans le vrai moteur.
 *
 * Il sert à trois choses : accepter un niveau ([feasible]), poser les graines le long d'un chemin sans touche ([placeGrains]), et rejouer ce chemin en test ([route]).
 */
class Planner(private val level: RoadLevel, reaction: Double = REACTION) {
    companion object {
        const val TICK_MS = 100
        const val HOP_TICKS = 2
        const val HORIZON = 600

        /** Marge de réaction (en cases) de chaque côté du poussin. */
        const val REACTION = 0.3
    }

    private val rows = level.rows.size
    private val cols = Road.COLS
    private val cells = rows * cols
    private val layers = HORIZON + HOP_TICKS + 1
    private val lastRow = level.last
    private val start = Road.START_COL

    /** `safe[t]` : les cases où le poussin peut se tenir au tick t. */
    private val safe: Array<BitSet> = Array(layers) { t ->
        val set = BitSet(cells)
        val seconds = t * TICK_MS / 1000.0
        for (r in 0 until rows) {
            val traffic = level.rows[r].traffic
            for (c in 0 until cols) {
                val ok = traffic == null || run {
                    val margin = reaction + traffic.speed * TICK_MS / 1000.0
                    val x = c + 0.5
                    traffic.hit(seconds, x - Road.CHICK_HALF - margin, x + Road.CHICK_HALF + margin) == null
                }
                if (ok) set.set(r * cols + c)
            }
        }
        set
    }

    private val startId = start // tick 0, ligne 0

    private fun neighbours(cell: Int): IntArray {
        val r = cell / cols
        val c = cell % cols
        val out = IntArray(4)
        var n = 0
        if (r < lastRow) out[n++] = cell + cols
        if (c > 0) out[n++] = cell - 1
        if (c < cols - 1) out[n++] = cell + 1
        if (r > 0) out[n++] = cell - cols
        return out.copyOf(n)
    }

    private fun hopOk(t: Int, from: Int, to: Int): Boolean =
        t + HOP_TICKS < layers && safe[t].get(to) && safe[t + 1].get(from) && safe[t + 1].get(to) && safe[t + 2].get(from) && safe[t + 2].get(to)

    /** Les états d'où l'on peut encore arriver (en sûreté) avant la fin de l'horizon. */
    private val good: Array<BitSet> = run {
        val g = Array(layers) { BitSet(cells) }
        for (t in layers - 1 downTo 0) {
            val here = safe[t]
            var cell = here.nextSetBit(0)
            while (cell >= 0) {
                val ok = when {
                    cell / cols == lastRow -> true
                    t + 1 < layers && safe[t + 1].get(cell) && g[t + 1].get(cell) -> true
                    else -> neighbours(cell).any { hopOk(t, cell, it) && g[t + HOP_TICKS].get(it) }
                }
                if (ok) g[t].set(cell)
                cell = here.nextSetBit(cell + 1)
            }
        }
        g
    }

    /** Le niveau a au moins une traversée sans touche, avec la marge de réaction. */
    fun feasible(): Boolean = good[0].get(startId)

    private class Closure(val reach: Array<BitSet>, val parent: IntArray?)

    /** Tout ce qu'on peut atteindre en partant des états [sources] (identifiant = tick · cases + case). */
    private fun closure(sources: List<Int>, withParents: Boolean): Closure {
        val reach = Array(layers) { BitSet(cells) }
        val parent = if (withParents) IntArray(layers * cells) { -2 } else null
        for (id in sources) {
            reach[id / cells].set(id % cells)
            parent?.set(id, -1)
        }
        for (t in 0 until layers - 1) {
            val here = reach[t]
            var cell = here.nextSetBit(0)
            while (cell >= 0) {
                val id = t * cells + cell
                if (cell / cols != lastRow) {
                    if (safe[t + 1].get(cell) && !reach[t + 1].get(cell)) {
                        reach[t + 1].set(cell)
                        parent?.set((t + 1) * cells + cell, id)
                    }
                    for (n in neighbours(cell)) {
                        if (hopOk(t, cell, n) && !reach[t + HOP_TICKS].get(n)) {
                            reach[t + HOP_TICKS].set(n)
                            parent?.set((t + HOP_TICKS) * cells + n, id)
                        }
                    }
                }
                cell = here.nextSetBit(cell + 1)
            }
        }
        return Closure(reach, parent)
    }

    private fun cellOf(row: Int, col: Int) = row * cols + col

    /** Les ticks où l'état (case) est atteignable ET permet encore d'arriver. */
    private fun usable(reach: Array<BitSet>, cell: Int): List<Int> = (0 until layers).filter { reach[it].get(cell) && good[it].get(cell) }

    /**
     * Pose des graines, une par ligne de route choisie au hasard (probabilité [chance] par ligne), **le long d'un chemin sans touche** : on passe de graine en graine
     * dans l'ordre des lignes, et l'on peut toujours finir ensuite. Elles se ramassent donc toutes d'un seul trajet, sans se faire écraser.
     * Renvoie une liste vide si le niveau n'est pas faisable.
     */
    fun placeGrains(rng: Rng, chance: Double): List<Grain> {
        if (!feasible()) return emptyList()
        var reach = closure(listOf(startId), false).reach
        val out = ArrayList<Grain>()
        var previous = start
        // les lignes qui portent une graine : au hasard, mais au moins une (la première voie si le hasard n'en a choisi aucune)
        val roads = (1 until lastRow).filter { level.rows[it].kind == RowKind.ROAD }
        val chosen = roads.filter { rng.nextDouble() <= chance }.ifEmpty { listOfNotNull(roads.firstOrNull()) }
        for (r in chosen) {
            val candidates = (0 until cols).filter { c -> usable(reach, cellOf(r, c)).isNotEmpty() }
            if (candidates.isEmpty()) continue
            val wanted = (previous + rng.nextInt(7) - 3).coerceIn(0, cols - 1)
            val c = candidates.minByOrNull { abs(it - wanted) }!!
            val cell = cellOf(r, c)
            reach = closure(usable(reach, cell).map { it * cells + cell }, false).reach
            out += Grain(out.size, r, c)
            previous = c
        }
        return out
    }

    /**
     * Un trajet sans touche qui passe par toutes les graines de [grains] (dans l'ordre des lignes) puis arrive, ou `null`.
     * Les sauts sont des [Move] à rejouer aux ticks indiqués (le tick t est à `t · TICK_MS` ms).
     */
    fun route(grains: List<Grain>): List<Move>? {
        if (!feasible()) return null
        val stages = ArrayList<Closure>()
        var sources = listOf(startId)
        val ordered = grains.sortedWith(compareBy({ it.row }, { it.id }))
        val chain = ArrayList<Int>()
        for (g in ordered) {
            val cl = closure(sources, true)
            val cell = cellOf(g.row, g.col)
            val seeds = usable(cl.reach, cell).map { it * cells + cell }
            if (seeds.isEmpty()) return null
            stages += cl
            sources = seeds
        }
        val finalClosure = closure(sources, true)
        stages += finalClosure
        // l'arrivée la plus tôt possible
        var end = -1
        search@ for (t in 0 until layers) {
            for (c in 0 until cols) if (finalClosure.reach[t].get(cellOf(lastRow, c))) {
                end = t * cells + cellOf(lastRow, c)
                break@search
            }
        }
        if (end < 0) return null
        // on remonte étage par étage : l'état de départ d'un étage est un état atteint dans l'étage d'avant
        var id = end
        for (k in stages.indices.reversed()) {
            val parents = stages[k].parent!!
            val part = ArrayList<Int>()
            var cur = id
            while (true) {
                part += cur
                val p = parents[cur]
                if (p < 0) break
                cur = p
            }
            // `cur` est la source de l'étage ; part est à l'envers (arrivée -> source)
            chain.addAll(0, part.asReversed().let { if (chain.isNotEmpty()) it.dropLast(1) else it })
            id = cur
        }
        // `chain` est dans l'ordre du temps ; on le relit en sauts
        val moves = ArrayList<Move>()
        for (i in 1 until chain.size) {
            val a = chain[i - 1]
            val b = chain[i]
            val cellA = a % cells
            val cellB = b % cells
            if (cellA == cellB) continue
            val dir = when (cellB - cellA) {
                cols -> HopDir.FORWARD
                -cols -> HopDir.BACK
                1 -> HopDir.RIGHT
                else -> HopDir.LEFT
            }
            moves += Move(a / cells, dir)
        }
        return moves
    }
}

/** Un petit générateur pseudo-aléatoire (SplitMix64) : la même suite pour la même graine, sur tous les téléphones, pour toujours. */
class Rng(seed: Long) {
    private var state = seed

    fun nextLong(): Long {
        state += -0x61c8864680b583ebL
        var z = state
        z = (z xor (z ushr 30)) * -0x40a7b892e31b1a47L
        z = (z xor (z ushr 27)) * -0x6b2fb644ecceee15L
        return z xor (z ushr 31)
    }

    /** Entier de 0 (inclus) à [bound] (exclu). */
    fun nextInt(bound: Int): Int = ((nextLong() ushr 33) % bound).toInt()

    /** Nombre de 0 (inclus) à 1 (exclu). */
    fun nextDouble(): Double = (nextLong() ushr 11).toDouble() / (1L shl 53).toDouble()
}
