package com.kcharignon.petitsjeux.games.find.engine

import com.kcharignon.petitsjeux.games.find.ui.FindObject
import kotlin.random.Random

/** Ce que la génération sait d'un objet : son identifiant, sa famille de couleur et sa sorte (les indices des énumérations `FindHue` et `FindKind`). */
class FindEntry(val id: String, val hue: Int, val kind: Int)

/** Les 120 objets du catalogue (`tools/find/catalog.js`, écrits dans `FindArt.kt`). */
object FindCatalog {
    val all: List<FindEntry> by lazy { FindObject.entries.map { FindEntry(it.id, it.hue.ordinal, it.kind.ordinal) } }
}

/**
 * Une manche de Cherche et Trouve, tirée au hasard : les [targets] à retrouver et les [others] (les faux), tous différents. Elle ne dépend que de
 * [difficulty] et de [seed] : la même graine redonne la même manche (donc les tests sont stables), et le jeu prend une graine nouvelle à chaque manche.
 */
data class FindRound(val difficulty: FindDifficulty, val targets: List<String>, val others: List<String>, val seed: Long) {
    /** Tous les objets de la manche, les cibles d'abord (l'ordre d'affichage est celui de la mise en page, pas celui-ci). */
    val ids: List<String> = targets + others

    /** La graine de la mise en page (places, inclinaisons, tailles), tirée de [seed]. */
    val layoutSeed: Int get() = (seed xor (seed ushr 32)).toInt()

    companion object {
        /**
         * Tire une manche. Les objets à trouver ont des couleurs **toutes différentes** (on les distingue d'un coup d'œil dans le bandeau). Les faux :
         * - Coup d'œil : d'une autre couleur et d'une autre sorte que les cibles, et de couleurs différentes entre eux (rien ne ressemble à rien) ;
         * - Bon œil : d'une autre couleur et d'une autre sorte que les cibles ;
         * - ensuite : n'importe quel autre objet, au hasard (de plus en plus nombreux, donc de plus en plus ressemblants).
         */
        fun generate(difficulty: FindDifficulty, seed: Long, catalog: List<FindEntry> = FindCatalog.all): FindRound {
            require(catalog.size >= difficulty.objects) { "${difficulty.objects} objets demandés, ${catalog.size} au catalogue" }
            val random = Random(seed)
            val shuffled = catalog.shuffled(random)
            val targets = ArrayList<FindEntry>()
            for (e in shuffled) {
                if (targets.size == difficulty.targets) break
                if (targets.none { it.hue == e.hue }) targets += e
            }
            // un catalogue minuscule (tests) : on complète sans exiger de couleurs différentes
            for (e in shuffled) {
                if (targets.size == difficulty.targets) break
                if (e !in targets) targets += e
            }
            val rest = shuffled.filter { it !in targets }
            val need = difficulty.objects - targets.size
            val others = when (difficulty) {
                FindDifficulty.EASY -> far(rest, need, targets, distinctHues = true)
                FindDifficulty.MEDIUM -> far(rest, need, targets, distinctHues = false)
                else -> rest.take(need)
            }
            return FindRound(difficulty, targets.map { it.id }, others.map { it.id }, seed)
        }

        /** [need] faux éloignés des cibles : on prend d'abord les plus éloignés, puis on relâche les exigences si le catalogue n'en offre pas assez. */
        private fun far(rest: List<FindEntry>, need: Int, targets: List<FindEntry>, distinctHues: Boolean): List<FindEntry> {
            val hues = targets.map { it.hue }.toSet()
            val kinds = targets.map { it.kind }.toSet()
            val chosen = ArrayList<FindEntry>()
            val passes = listOf<(FindEntry) -> Boolean>(
                { e -> e.hue !in hues && e.kind !in kinds && (!distinctHues || chosen.none { it.hue == e.hue }) },
                { e -> e.hue !in hues && e.kind !in kinds },
                { e -> e.hue !in hues },
                { _ -> true },
            )
            for (accept in passes) {
                for (e in rest) {
                    if (chosen.size == need) return chosen
                    if (e !in chosen && accept(e)) chosen += e
                }
            }
            return chosen
        }
    }
}
