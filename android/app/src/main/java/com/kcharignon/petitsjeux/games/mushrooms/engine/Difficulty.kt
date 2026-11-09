package com.kcharignon.petitsjeux.games.mushrooms.engine

/**
 * Les difficultés de Champis Magiques. On choisit sa difficulté au début ; on **débloque la suivante** en réussissant la précédente
 * (2 étoiles : [twoStars] points). On ne perd jamais : une partie finit toujours, avec au moins 1 étoile.
 *
 * - [targets] : combien de champignons s'allument pendant une partie (les faux, interdits, ne comptent pas) ;
 * - [mushrooms] : combien de champignons dans la forêt ;
 * - [maxLit] : combien peuvent être allumés en même temps ;
 * - [litMs] : au bout de combien de temps un champignon allumé s'éteint tout seul (raté, sans punition) ; `null` = il reste allumé jusqu'à ce qu'on le touche ;
 * - [decoys] : des champignons de la couleur **interdite** (violette) s'allument aussi : il ne faut pas les toucher ;
 * - [gapMs] : le délai, au hasard, entre deux allumages.
 */
enum class Difficulty(
    val title: String,
    val blurb: String,
    val targets: Int,
    val mushrooms: Int,
    val maxLit: Int,
    val litMs: Long?,
    val decoys: Boolean,
    val gapMs: LongRange,
    val twoStars: Int,
    val threeStars: Int,
) {
    EASY("Petit champi", "Ils restent allumés jusqu'à ce que tu les touches.", 15, 12, 2, null, false, 900L..1500L, 700, 1000),
    MEDIUM("Champi rapide", "Plus de champignons, et plusieurs allumés en même temps.", 20, 16, 3, null, false, 700L..1200L, 1000, 1400),
    HARD("Champi filant", "Attention : ils s'éteignent si tu es trop lent.", 25, 18, 3, 3500L, false, 600L..1100L, 1300, 1800),
    EXPERT("Champi malin", "Ne touche pas les violets ! Et ils s'éteignent vite.", 30, 20, 4, 3000L, true, 500L..1000L, 1650, 2200);

    val index: Int get() = ordinal

    /** La difficulté qu'il faut réussir pour débloquer celle-ci (`null` pour la première). */
    val previous: Difficulty? get() = entries.getOrNull(ordinal - 1)

    /** Points maximum d'une partie (tous les champignons touchés tout de suite). */
    val maxScore: Int get() = targets * Scoring.MAX_POINTS

    /** Étoiles (1 à 3) pour un score : on en gagne toujours au moins une. */
    fun stars(score: Int): Int = when {
        score >= threeStars -> 3
        score >= twoStars -> 2
        else -> 1
    }

    companion object {
        fun of(index: Int): Difficulty = entries[index.coerceIn(0, entries.lastIndex)]
    }
}

/** Les points : plus on touche vite un champignon allumé, plus on en gagne. */
object Scoring {
    const val MIN_POINTS = 10
    const val MAX_POINTS = 100

    /** Le temps (ms) pour que les points « en plus » du minimum soient divisés par deux. */
    private const val HALF_LIFE_MS = 1200.0

    /** 100 points si on touche tout de suite, environ 60 après une seconde, 38 après deux, 19 après quatre, jamais moins de 10. */
    fun points(reactionMs: Long): Int {
        val extra = (MAX_POINTS - MIN_POINTS) * Math.pow(2.0, -reactionMs.coerceAtLeast(0L) / HALF_LIFE_MS)
        return MIN_POINTS + Math.round(extra).toInt()
    }
}
