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
 *   2 = + quelques murs isolés (une seule voie libre, une rangée, de temps en temps) et des avions qui traversent le ciel ;
 *   3 = + des murs et des slaloms où il ne reste qu'une voie libre, plusieurs rangées d'affilée ;
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
    MEDIUM("Vol rapide", "Ça va plus vite, et des avions arrivent !", 65, 10f, 16f, 20f, 13f, 1.3f, 2.6f, 1, 150f),
    HARD("Haute voltige", "Des avions traversent le ciel, avec des slaloms !", 80, 12f, 20f, 18f, 11f, 1.6f, 3.0f, 2, 170f),
    EXPERT("Pilote de chasse", "Le ciel est bondé, avec des passages étroits !", 95, 14f, 24f, 16f, 9f, 2.0f, 3.8f, 3, 190f);

    val index: Int get() = ordinal

    /**
     * Le plus d'obstacles dans une rangée (sur 5 voies). Avant la dernière difficulté il reste toujours **deux voies libres côte à côte**
     * (sauf pour un mur isolé, voir [soloWalls]) ; à la dernière, une seule voie libre suffit.
     */
    val maxPerRow: Int get() = when (tier) {
        0 -> 2
        1, 2 -> 3
        else -> 4
    }

    /** Des murs isolés (une seule voie libre, une seule rangée, jamais deux de suite) dès la troisième difficulté. */
    val soloWalls: Boolean get() = tier >= 2

    /** Des murs et des slaloms où il ne reste qu'une voie libre, plusieurs rangées d'affilée : seulement à la dernière difficulté. */
    val narrowStreaks: Boolean get() = tier >= 3

    /** Des avions qui traversent le ciel de côté en arrivant : à partir de la troisième difficulté. */
    val crossingPlanes: Boolean get() = tier >= 2

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
