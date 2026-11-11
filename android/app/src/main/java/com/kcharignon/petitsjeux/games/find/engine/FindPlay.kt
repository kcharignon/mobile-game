package com.kcharignon.petitsjeux.games.find.engine

/** Ce qui s'est passé quand l'enfant a touché un objet. */
enum class FindOutcome {
    /** Un des objets à trouver : il est trouvé (la manche est finie quand ils le sont tous, voir [FindPlay.done]). */
    FOUND,

    /** Un faux : il pâlit (on ne peut plus le toucher) et on risque une étoile. */
    WRONG,

    /** Rien : objet déjà pâle ou déjà trouvé, manche déjà finie, ou objet qui n'est pas dans la manche. */
    IGNORED,
}

/**
 * Une manche en cours, sans Android : une valeur qu'on remplace à chaque toucher ([tap]).
 * **On ne perd jamais** : un faux pâlit et ne peut plus être touché, donc après au plus `objects - targets` erreurs il ne reste que les bons.
 * Pas de temps, pas de limite d'essais.
 */
data class FindPlay(
    val round: FindRound,
    val wrong: Set<String> = emptySet(),
    val found: Set<String> = emptySet(),
    /** L'aide s'est déclenchée (un objet à trouver s'est balancé) : voir [withHelp]. */
    val helped: Boolean = false,
) {
    /** Les erreurs de cette manche (chaque faux touché compte une fois). */
    val mistakes: Int get() = wrong.size

    /** Les étoiles de cette manche (celles qu'on aura si on finit maintenant ; définitives quand [done]). */
    val stars: Int get() = round.difficulty.stars(mistakes)

    /**
     * Une manche **parfaite** : aucune erreur, et tout trouvé avant que l'aide se déclenche. C'est ce qui débloque la difficulté suivante
     * (les étoiles, elles, tolèrent quelques erreurs).
     */
    val flawless: Boolean get() = mistakes == 0 && !helped

    /** Combien d'objets restent à trouver. */
    val remaining: Int get() = round.targets.size - found.size

    /** Tout est trouvé : la manche est finie. */
    val done: Boolean get() = remaining == 0

    /** Les objets à trouver qui ne le sont pas encore, dans l'ordre du bandeau. */
    val missing: List<String> get() = round.targets.filter { it !in found }

    /** Cet objet peut-il encore être touché ? */
    fun canTap(id: String): Boolean = !done && id in round.ids && id !in wrong && id !in found

    /** L'aide vient de se déclencher : la manche n'est plus parfaite. Sans effet une fois la manche finie. */
    fun withHelp(): FindPlay = if (done || helped) this else copy(helped = true)

    class Tapped(val play: FindPlay, val outcome: FindOutcome)

    fun tap(id: String): Tapped = when {
        !canTap(id) -> Tapped(this, FindOutcome.IGNORED)
        id in round.targets -> Tapped(copy(found = found + id), FindOutcome.FOUND)
        else -> Tapped(copy(wrong = wrong + id), FindOutcome.WRONG)
    }
}
