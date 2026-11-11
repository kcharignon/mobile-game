package com.kcharignon.petitsjeux.games.find.engine

/** Les étoiles d'un niveau : 3 sans erreur, 2 avec une erreur, 1 avec deux erreurs ou plus. **On en gagne toujours au moins une.** */
object FindScoring {
    fun stars(mistakes: Int): Int = (3 - mistakes).coerceIn(1, 3)
}

/** Ce qui s'est passé quand l'enfant a touché un objet. */
enum class FindOutcome {
    /** Le bon objet : le niveau est réussi. */
    FOUND,

    /** Un mauvais objet : il pâlit (on ne peut plus le toucher) et une étoile de moins. */
    WRONG,

    /** Rien : objet déjà pâle, niveau déjà réussi, ou objet qui n'est pas dans le niveau. */
    IGNORED,
}

/**
 * Un niveau en cours, sans Android : une valeur qu'on remplace à chaque toucher ([tap]).
 * **On ne peut pas perdre** : un mauvais objet pâlit et ne peut plus être touché, donc après au plus `objectCount - 1` erreurs il ne reste que le bon.
 * Pas de temps, pas de limite d'essais.
 */
data class FindPlay(val level: FindLevel, val wrong: Set<String> = emptySet(), val found: Boolean = false) {
    /** Les erreurs de ce niveau (chaque mauvais objet touché compte une fois). */
    val mistakes: Int get() = wrong.size

    /** Les étoiles de ce niveau (celles qu'on aura si on trouve maintenant ; définitives quand [found]). */
    val stars: Int get() = FindScoring.stars(mistakes)

    /** Cet objet peut-il encore être touché ? */
    fun canTap(id: String): Boolean = !found && id in level.ids && id !in wrong

    class Tapped(val play: FindPlay, val outcome: FindOutcome)

    fun tap(id: String): Tapped = when {
        !canTap(id) -> Tapped(this, FindOutcome.IGNORED)
        id == level.target -> Tapped(copy(found = true), FindOutcome.FOUND)
        else -> Tapped(copy(wrong = wrong + id), FindOutcome.WRONG)
    }
}
