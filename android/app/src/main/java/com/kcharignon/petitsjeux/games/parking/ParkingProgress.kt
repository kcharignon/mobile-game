package com.kcharignon.petitsjeux.games.parking

import com.kcharignon.petitsjeux.games.parking.engine.Worlds
import org.json.JSONObject

/**
 * Ce que Parking Malin garde d'un joueur : sa part de la sauvegarde (`games.parking`).
 * Tolérant : un champ absent ou invalide vaut zéro, jamais une erreur.
 */
data class ParkingProgress(
    /** Meilleure note (1 à 3 étoiles) de chaque niveau réussi. */
    val stars: Map<Int, Int> = emptyMap(),
    val carsFreed: Int = 0,
    val bumps: Int = 0,
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
        get() = ((stars.keys.maxOrNull() ?: 0) + 1).coerceAtMost(Worlds.LEVEL_COUNT)

    val allDone: Boolean get() = (1..Worlds.LEVEL_COUNT).all { it in stars }

    /** Ce niveau a-t-il déjà été réussi (au moins une étoile) ? */
    fun isDone(level: Int): Boolean = level in stars

    fun starsOf(level: Int): Int = stars[level] ?: 0

    /** Après un niveau réussi : garde la meilleure note et additionne les compteurs. */
    fun withResult(level: Int, earnedStars: Int, freed: Int, faults: Int): ParkingProgress =
        copy(
            stars = stars + (level to maxOf(starsOf(level), earnedStars.coerceIn(1, 3))),
            carsFreed = carsFreed + freed,
            bumps = bumps + faults,
            played = played + 1,
        )

    fun toJson(): JSONObject {
        val starsJson = JSONObject()
        for ((level, s) in stars.toSortedMap()) starsJson.put(level.toString(), s)
        return JSONObject().put("stars", starsJson).put("carsFreed", carsFreed).put("bumps", bumps).put("played", played)
    }

    companion object {
        const val GAME_ID = "parking"

        fun fromJson(json: JSONObject?): ParkingProgress {
            if (json == null) return ParkingProgress()
            val stars = HashMap<Int, Int>()
            json.optJSONObject("stars")?.let { s ->
                for (key in s.keys()) {
                    val level = key.toIntOrNull() ?: continue
                    val value = s.optInt(key, 0)
                    if (level >= 1 && value in 1..3) stars[level] = value
                }
            }
            return ParkingProgress(
                stars = stars,
                carsFreed = json.optInt("carsFreed", 0).coerceAtLeast(0),
                bumps = json.optInt("bumps", 0).coerceAtLeast(0),
                played = json.optInt("played", 0).coerceAtLeast(0),
            )
        }
    }
}
