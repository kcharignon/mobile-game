package com.kcharignon.petitsjeux.games.find.engine

/**
 * Les cinq difficultés de Cherche et Trouve. On choisit sa difficulté au début ; on **débloque la suivante** en réussissant la précédente
 * (2 étoiles). Chaque manche est tirée au hasard : mêmes règles, mais d'autres objets, d'autres places. On ne perd jamais : une manche finit toujours
 * quand tout est trouvé, avec au moins 1 étoile, sans temps limite.
 *
 * Les règles **s'additionnent** d'une difficulté à la suivante :
 * - [objects] : combien d'objets à l'écran (5, 10, 20, 40, 80) ;
 * - [targets] : combien d'objets à retrouver (1 à 5), montrés en haut ;
 * - [rotates] : à partir de la 2e difficulté, les objets sont posés dans tous les sens ;
 * - [variesSizes] : à partir de la 4e, les objets ont des tailles différentes ;
 * - [nudgeMs] : sans rien toucher pendant ce temps, un objet à trouver se balance (une aide discrète) ;
 * - [threeStarsMistakes] / [twoStarsMistakes] : le plus d'erreurs permis pour 3 étoiles, puis pour 2 (au-delà, 1 étoile).
 */
enum class FindDifficulty(
    val title: String,
    val blurb: String,
    val objects: Int,
    val targets: Int,
    val threeStarsMistakes: Int,
    val twoStarsMistakes: Int,
    val nudgeMs: Long,
) {
    EASY("Coup d'œil", "5 objets, 1 à trouver.", 5, 1, 0, 1, 8_000L),
    MEDIUM("Bon œil", "10 objets qui tournent, 2 à trouver.", 10, 2, 1, 3, 12_000L),
    HARD("Œil vif", "20 objets qui tournent, 3 à trouver.", 20, 3, 2, 5, 16_000L),
    EXPERT("Œil d'aigle", "40 objets qui tournent, de toutes tailles, 4 à trouver.", 40, 4, 3, 8, 20_000L),
    MASTER("Super détective", "80 objets minuscules, 5 à trouver !", 80, 5, 5, 12, 25_000L);

    val index: Int get() = ordinal

    /** La difficulté qu'il faut réussir pour débloquer celle-ci (`null` pour la première). */
    val previous: FindDifficulty? get() = entries.getOrNull(ordinal - 1)

    /** La difficulté d'après (`null` pour la dernière). */
    val next: FindDifficulty? get() = entries.getOrNull(ordinal + 1)

    /** Les objets sont posés dans tous les sens à partir de la 2e difficulté. */
    val rotates: Boolean get() = ordinal >= 1

    /** Les objets ont des tailles différentes à partir de la 4e difficulté. */
    val variesSizes: Boolean get() = ordinal >= 3

    /** Les étoiles (1 à 3) d'une manche avec [mistakes] erreurs : **on en gagne toujours au moins une**. */
    fun stars(mistakes: Int): Int = when {
        mistakes <= threeStarsMistakes -> 3
        mistakes <= twoStarsMistakes -> 2
        else -> 1
    }

    companion object {
        /** Étoiles qu'on peut gagner au plus dans tout le jeu : 3 par difficulté. */
        val MAX_STARS: Int get() = entries.size * 3

        fun of(index: Int): FindDifficulty = entries[index.coerceIn(0, entries.lastIndex)]
    }
}
