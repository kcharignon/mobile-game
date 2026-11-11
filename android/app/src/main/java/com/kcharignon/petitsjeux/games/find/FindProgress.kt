package com.kcharignon.petitsjeux.games.find

import com.kcharignon.petitsjeux.games.find.engine.FindWorlds
import org.json.JSONObject

/**
 * Ce que Cherche et Trouve garde d'un joueur : sa part de la sauvegarde (`games.find`).
 * Tolérant : un champ absent ou invalide vaut zéro, jamais une erreur.
 */
data class FindProgress(
    /** Meilleure note (1 à 3 étoiles) de chaque niveau réussi. */
    val stars: Map<Int, Int> = emptyMap(),
    /** Mauvais objets touchés (tous niveaux, rejeux compris). */
    val mistakes: Int = 0,
    /** Niveaux joués jusqu'au bout (réussis, rejeux compris). */
    val played: Int = 0,
) {
    val totalStars: Int get() = stars.values.sum()

    val levelsDone: Int get() = stars.size

    /**
     * Le niveau où le joueur reprend : le plus loin qu'il ait réussi, plus un (le niveau 1 s'il n'a rien fait, le dernier niveau s'il est au bout).
     * **Tous les niveaux sont jouables dès le début** : s'il trouve le jeu trop simple, il saute en avant et reprend à partir de là ; ceux qu'il a laissés derrière restent à faire.
     */
    val currentLevel: Int
        get() = ((stars.keys.maxOrNull() ?: 0) + 1).coerceAtMost(FindWorlds.LEVEL_COUNT)

    val allDone: Boolean get() = (1..FindWorlds.LEVEL_COUNT).all { it in stars }

    /**
     * Le niveau à lancer tout de suite quand l'enfant ouvre le jeu : le suivant à faire ([currentLevel]), sans passer par la grille des niveaux.
     * `null` quand tout est réussi : il n'y a plus de « suivant », on montre alors la grille pour qu'il rejoue ce qu'il veut.
     */
    val launchLevel: Int? get() = if (allDone) null else currentLevel

    /** Ce niveau a-t-il déjà été réussi (au moins une étoile) ? */
    fun isDone(level: Int): Boolean = level in stars

    fun starsOf(level: Int): Int = stars[level] ?: 0

    /** Après un niveau réussi : garde la meilleure note et additionne les compteurs. */
    fun withResult(level: Int, earnedStars: Int, mistakesNow: Int): FindProgress =
        copy(
            stars = stars + (level to maxOf(starsOf(level), earnedStars.coerceIn(1, 3))),
            mistakes = mistakes + mistakesNow.coerceAtLeast(0),
            played = played + 1,
        )

    fun toJson(): JSONObject {
        val starsJson = JSONObject()
        for ((level, s) in stars.toSortedMap()) starsJson.put(level.toString(), s)
        return JSONObject().put("stars", starsJson).put("mistakes", mistakes).put("played", played)
    }

    companion object {
        const val GAME_ID = "find"

        fun fromJson(json: JSONObject?): FindProgress {
            if (json == null) return FindProgress()
            val stars = HashMap<Int, Int>()
            json.optJSONObject("stars")?.let { s ->
                for (key in s.keys()) {
                    val level = key.toIntOrNull() ?: continue
                    val value = s.optInt(key, 0)
                    if (level in 1..FindWorlds.LEVEL_COUNT && value in 1..3) stars[level] = value
                }
            }
            return FindProgress(
                stars = stars,
                mistakes = json.optInt("mistakes", 0).coerceAtLeast(0),
                played = json.optInt("played", 0).coerceAtLeast(0),
            )
        }
    }
}
