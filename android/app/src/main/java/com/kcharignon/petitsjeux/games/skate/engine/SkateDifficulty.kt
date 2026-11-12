package com.kcharignon.petitsjeux.games.skate.engine

import kotlin.math.ln

/**
 * Les difficultés de Super Skate. On choisit sa difficulté au début ; on **débloque la suivante** en finissant la précédente avec 2 étoiles.
 * On ne perd jamais : une course finit toujours, avec au moins 1 étoile.
 *
 * Dans une course la difficulté **monte toute seule** de la ligne de départ à l'arche d'arrivée : la vitesse passe de [speedStart] à [speedEnd] (mètres par seconde),
 * l'écart entre deux rangées d'obstacles de [gapStart] à [gapEnd] (mètres) et le nombre d'obstacles par rangée de [perRowStart] à [perRowEnd].
 *
 * - [seconds] : la durée d'une course sans le moindre choc (la longueur de la rue en découle, voir [length]) ;
 * - [tier] : la variété des obstacles : 0 = piétons, lampadaires, plots (et poubelles) ; 1 = + bancs et piétons qui traversent ; 2 = + barrières de chantier ; 3 = + beaucoup de piétons qui traversent ;
 * - [rampEvery] : une rampe tous les tant de mètres, en moyenne.
 */
enum class SkateDifficulty(
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
    val rampEvery: Float,
) {
    EASY("Petite balade", "Doucement, avec peu d'obstacles.", 50, 5.0f, 8.0f, 17f, 12f, 1.0f, 2.0f, 0, 110f),
    MEDIUM("Bon rythme", "Ça roule plus vite. Des piétons traversent !", 65, 6.5f, 11.0f, 14f, 9f, 1.5f, 3.0f, 1, 130f),
    HARD("Grande vitesse", "Plus de monde, plus d'obstacles, plus vite !", 80, 8.0f, 14.0f, 12f, 7.5f, 2.0f, 3.5f, 2, 150f),
    EXPERT("Skate de fou", "La rue est bondée et tu files à toute allure.", 95, 9.0f, 17.0f, 10f, 6.5f, 2.5f, 4.5f, 3, 170f);

    val index: Int get() = ordinal

    /** La difficulté qu'il faut réussir (2 étoiles) pour débloquer celle-ci (`null` pour la première). */
    val previous: SkateDifficulty? get() = entries.getOrNull(ordinal - 1)

    /** La longueur de la rue, en mètres : ce que l'on parcourt en [seconds] secondes quand la vitesse monte de [speedStart] à [speedEnd] **le long de la rue**. */
    val length: Float get() = seconds * (speedEnd - speedStart) / ln(speedEnd / speedStart)

    /** La vitesse (m/s) à la distance [dist] du départ, quand tout roule : elle monte en ligne droite jusqu'à l'arrivée. */
    fun speedAt(dist: Float): Float = lerp(speedStart, speedEnd, (dist / length).coerceIn(0f, 1f))

    companion object {
        fun of(index: Int): SkateDifficulty = entries[index.coerceIn(0, entries.lastIndex)]

        /** Les étoiles de départ, et le plus qu'on puisse en avoir. */
        const val MAX_STARS = 3

        /** Étoiles d'une course finie avec [lives] étoiles restantes : toujours au moins une. */
        fun stars(lives: Int): Int = lives.coerceIn(1, MAX_STARS)

        /** Étoiles qu'il faut pour ouvrir la difficulté suivante. */
        const val UNLOCK_STARS = 2
    }
}

internal fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
