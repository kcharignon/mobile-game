package com.kcharignon.petitsjeux.games.maze

import com.kcharignon.petitsjeux.games.maze.engine.MazeWorlds
import org.json.JSONObject

/**
 * Ce que Poisson Perdu garde d'un joueur : sa part de la sauvegarde (`games.maze`).
 * Tolérant : un champ absent ou invalide vaut zéro, jamais une erreur.
 */
data class MazeProgress(
    /** Meilleure note (1 à 3 étoiles) de chaque niveau réussi. */
    val stars: Map<Int, Int> = emptyMap(),
    /** Impasses visitées (tous niveaux, rejeux compris). */
    val detours: Int = 0,
    /** Niveaux joués jusqu'au bout (réussis, rejeux compris). */
    val played: Int = 0,
) {
    val totalStars: Int get() = stars.values.sum()

    val levelsDone: Int get() = stars.size

    /** Le premier niveau pas encore réussi (le dernier niveau si tout est réussi) : c'est là que le joueur reprend. */
    val currentLevel: Int
        get() = (1..MazeWorlds.LEVEL_COUNT).firstOrNull { it !in stars } ?: MazeWorlds.LEVEL_COUNT

    val allDone: Boolean get() = (1..MazeWorlds.LEVEL_COUNT).all { it in stars }

    /** Un niveau se débloque quand le précédent est réussi. */
    fun isUnlocked(level: Int): Boolean = level in 1..MazeWorlds.LEVEL_COUNT && level <= currentLevel

    fun starsOf(level: Int): Int = stars[level] ?: 0

    /** Après un niveau réussi : garde la meilleure note et additionne les compteurs. */
    fun withResult(level: Int, earnedStars: Int, detoursNow: Int): MazeProgress =
        copy(
            stars = stars + (level to maxOf(starsOf(level), earnedStars.coerceIn(1, 3))),
            detours = detours + detoursNow.coerceAtLeast(0),
            played = played + 1,
        )

    fun toJson(): JSONObject {
        val starsJson = JSONObject()
        for ((level, s) in stars.toSortedMap()) starsJson.put(level.toString(), s)
        return JSONObject().put("stars", starsJson).put("detours", detours).put("played", played)
    }

    companion object {
        const val GAME_ID = "maze"

        fun fromJson(json: JSONObject?): MazeProgress {
            if (json == null) return MazeProgress()
            val stars = HashMap<Int, Int>()
            json.optJSONObject("stars")?.let { s ->
                for (key in s.keys()) {
                    val level = key.toIntOrNull() ?: continue
                    val value = s.optInt(key, 0)
                    if (level in 1..MazeWorlds.LEVEL_COUNT && value in 1..3) stars[level] = value
                }
            }
            return MazeProgress(
                stars = stars,
                detours = json.optInt("detours", 0).coerceAtLeast(0),
                played = json.optInt("played", 0).coerceAtLeast(0),
            )
        }
    }
}
