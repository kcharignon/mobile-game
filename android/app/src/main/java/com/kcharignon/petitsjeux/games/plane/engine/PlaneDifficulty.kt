package com.kcharignon.petitsjeux.games.plane.engine

import kotlin.math.ln

/**
 * Les difficultés de Super Avion. On choisit sa difficulté au début ; on **débloque la suivante** en atterrissant avec 2 étoiles sur la précédente.
 * On ne perd jamais : un vol finit toujours par un atterrissage, avec au moins 1 étoile.
 *
 * Dans un vol la difficulté **monte toute seule** du décollage à l'aéroport : la vitesse passe de [speedStart] à [speedEnd] (mètres par seconde),
 * l'écart entre deux rangées d'obstacles de [gapStart] à [gapEnd] (mètres) et le nombre d'obstacles par rangée de [perRowStart] à [perRowEnd]
 * (sur 5 voies : il en reste toujours au moins une de libre).
 *
 * - [seconds] : la durée d'un vol sans le moindre choc (la longueur du trajet en découle, voir [length]) ;
 * - [tier] : la variété. 0 = oiseaux, montgolfières et parapentes, en rangées éparses ; 1 = + avions, et de petits slaloms ;
 *   2 = + murs d'obstacles et slaloms plus longs ; 3 = encore plus de slaloms et de murs ;
 * - [ringEvery] : un anneau lumineux tous les tant de mètres, en moyenne.
 */
enum class PlaneDifficulty(
    val title: String,
    val blurb: String,
    val seconds: Int,
    val speedStart: Float,
    val speedEnd: Float,
    val gapStart: Float,
    val gapEnd: Float,
    val perRowStart: Float,
    val perRowEnd: Float,
    val tier: Int,
    val ringEvery: Float,
) {
    EASY("Petit vol", "Doucement, avec peu d'obstacles.", 50, 8f, 12f, 22f, 16f, 1.0f, 2.0f, 0, 130f),
    MEDIUM("Vol rapide", "Ça va plus vite, et des avions arrivent !", 65, 10f, 16f, 20f, 13f, 1.5f, 3.0f, 1, 150f),
    HARD("Haute voltige", "Plus de monde, avec des slaloms à faire !", 80, 12f, 20f, 18f, 11f, 2.0f, 3.5f, 2, 170f),
    EXPERT("Pilote de chasse", "Le ciel est bondé et tu files à toute allure.", 95, 14f, 24f, 16f, 9f, 2.5f, 4.0f, 3, 190f);

    val index: Int get() = ordinal

    /** La difficulté qu'il faut réussir (2 étoiles) pour débloquer celle-ci (`null` pour la première). */
    val previous: PlaneDifficulty? get() = entries.getOrNull(ordinal - 1)

    /** La longueur du trajet, en mètres : ce que l'on parcourt en [seconds] secondes quand la vitesse monte de [speedStart] à [speedEnd] **le long du trajet**. */
    val length: Float get() = seconds * (speedEnd - speedStart) / ln(speedEnd / speedStart)

    /** La vitesse (m/s) à la distance [dist] du départ, quand tout roule : elle monte en ligne droite jusqu'à l'aéroport. */
    fun speedAt(dist: Float): Float = lerp(speedStart, speedEnd, (dist / length).coerceIn(0f, 1f))

    companion object {
        fun of(index: Int): PlaneDifficulty = entries[index.coerceIn(0, entries.lastIndex)]

        /** Les étoiles de départ, et le plus qu'on puisse en avoir. */
        const val MAX_STARS = 3

        /** Étoiles d'un vol fini avec [lives] étoiles restantes : toujours au moins une. */
        fun stars(lives: Int): Int = lives.coerceIn(1, MAX_STARS)

        /** Étoiles qu'il faut pour ouvrir la difficulté suivante. */
        const val UNLOCK_STARS = 2
    }
}

internal fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
