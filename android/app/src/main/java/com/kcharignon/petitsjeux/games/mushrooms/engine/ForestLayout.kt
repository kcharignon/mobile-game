package com.kcharignon.petitsjeux.games.mushrooms.engine

import kotlin.math.PI
import kotlin.math.hypot
import kotlin.math.sqrt
import kotlin.random.Random

/** Un champignon de la forêt : son centre ([x], [y]) et le rayon de son chapeau ([radius]), dans l'unité de la zone de jeu (des dp). */
data class ShroomSpot(val id: Int, val x: Float, val y: Float, val radius: Float)

/**
 * Où poussent les champignons : [count] champignons de **tailles différentes**, jamais l'un sur l'autre, entièrement dans la zone [width] x [height].
 * Le résultat ne dépend que des arguments (même graine, même forêt).
 */
object ForestLayout {
    /** Part de la zone qu'occupent les champignons (les cercles de rayon [ShroomSpot.radius]). */
    private const val FILL = 0.42f

    /** Espace minimum entre deux champignons. */
    const val GAP = 6f

    /** Plus petit rayon (un chapeau de 52 dp de large se touche encore bien du doigt). */
    const val MIN_RADIUS = 26f

    fun generate(count: Int, width: Float, height: Float, seed: Long): List<ShroomSpot> {
        require(count >= 1 && width > 0f && height > 0f)
        val random = Random(seed)
        val average = maxOf(MIN_RADIUS * 1.05f, sqrt(width * height * FILL / (count * PI.toFloat())))
        // On essaie plusieurs fois : si une forêt est trop serrée pour que tout le monde ait sa place, on recommence avec des champignons un peu plus petits
        // (jamais sous le rayon minimum).
        var scale = 1f
        for (attempt in 0 until 40) {
            // des rayons de 0,72 à 1,28 fois la moyenne, les plus grands d'abord (ils se placent plus facilement)
            val radii = List(count) { average * scale * (0.72f + 0.56f * random.nextFloat()) }.sortedDescending().map { maxOf(it, MIN_RADIUS) }
            val placed = ArrayList<ShroomSpot>()
            for ((k, r) in radii.withIndex()) {
                var spot: ShroomSpot? = null
                var tries = 0
                while (spot == null && tries++ < 400) {
                    val x = r + random.nextFloat() * (width - 2 * r).coerceAtLeast(0f)
                    val y = r + random.nextFloat() * (height - 2 * r).coerceAtLeast(0f)
                    if (placed.none { hypot(it.x - x, it.y - y) < it.radius + r + GAP }) spot = ShroomSpot(k, x, y, r)
                }
                placed += spot ?: break
            }
            if (placed.size == count) return finish(placed)
            if (attempt % 5 == 4) scale *= 0.94f
        }
        // dernier recours : une zone vraiment trop petite, on pose chacun sur la place la plus éloignée des autres (ils peuvent se toucher)
        val placed = ArrayList<ShroomSpot>()
        for (k in 0 until count) placed += farthest(placed, width, height, MIN_RADIUS, k)
        return finish(placed)
    }

    /** Les ids suivent l'ordre de haut en bas (pour que l'interface dessine d'abord les champignons du fond). */
    private fun finish(placed: List<ShroomSpot>): List<ShroomSpot> =
        placed.sortedWith(compareBy({ it.y }, { it.x })).mapIndexed { i, s -> s.copy(id = i) }

    private fun farthest(placed: List<ShroomSpot>, width: Float, height: Float, r: Float, id: Int): ShroomSpot {
        var best = ShroomSpot(id, width / 2, height / 2, r)
        var bestDistance = -1f
        var y = r
        while (y <= height - r) {
            var x = r
            while (x <= width - r) {
                val d = placed.minOfOrNull { hypot(it.x - x, it.y - y) - it.radius } ?: Float.MAX_VALUE
                if (d > bestDistance) {
                    bestDistance = d
                    best = ShroomSpot(id, x, y, r)
                }
                x += r / 2
            }
            y += r / 2
        }
        return best
    }
}
