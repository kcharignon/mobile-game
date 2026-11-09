package com.kcharignon.petitsjeux.games.road

import com.kcharignon.petitsjeux.games.road.engine.RoadWorlds
import org.json.JSONObject

/**
 * Ce que Poussin Traverse garde d'un joueur : sa part de la sauvegarde (`games.road`).
 * Tolérant : un champ absent ou invalide vaut zéro, jamais une erreur.
 */
data class RoadProgress(
    /** Meilleure note (1 à 3 étoiles) de chaque niveau réussi. */
    val stars: Map<Int, Int> = emptyMap(),
    /** Meilleur score de chaque niveau réussi. */
    val best: Map<Int, Int> = emptyMap(),
    /** Fois où le poussin s'est fait écraser (tous niveaux, rejeux compris). */
    val hits: Int = 0,
    /** Traversées réussies (rejeux compris). */
    val crossings: Int = 0,
) {
    val totalStars: Int get() = stars.values.sum()

    val levelsDone: Int get() = stars.size

    /** Le premier niveau pas encore réussi (le dernier niveau si tout est réussi) : c'est là que le joueur reprend. */
    val currentLevel: Int
        get() = (1..RoadWorlds.LEVEL_COUNT).firstOrNull { it !in stars } ?: RoadWorlds.LEVEL_COUNT

    val allDone: Boolean get() = (1..RoadWorlds.LEVEL_COUNT).all { it in stars }

    /** Un niveau se débloque quand le précédent est réussi. */
    fun isUnlocked(level: Int): Boolean = level in 1..RoadWorlds.LEVEL_COUNT && level <= currentLevel

    fun starsOf(level: Int): Int = stars[level] ?: 0

    fun bestOf(level: Int): Int = best[level] ?: 0

    /** Après une traversée réussie : garde la meilleure note et le meilleur score, additionne les compteurs. */
    fun withResult(level: Int, earnedStars: Int, score: Int, hitsNow: Int): RoadProgress =
        copy(
            stars = stars + (level to maxOf(starsOf(level), earnedStars.coerceIn(1, 3))),
            best = best + (level to maxOf(bestOf(level), score.coerceAtLeast(0))),
            hits = hits + hitsNow.coerceAtLeast(0),
            crossings = crossings + 1,
        )

    fun toJson(): JSONObject {
        val starsJson = JSONObject()
        for ((level, s) in stars.toSortedMap()) starsJson.put(level.toString(), s)
        val bestJson = JSONObject()
        for ((level, s) in best.toSortedMap()) bestJson.put(level.toString(), s)
        return JSONObject().put("stars", starsJson).put("best", bestJson).put("hits", hits).put("crossings", crossings)
    }

    companion object {
        const val GAME_ID = "road"

        fun fromJson(json: JSONObject?): RoadProgress {
            if (json == null) return RoadProgress()
            val stars = HashMap<Int, Int>()
            json.optJSONObject("stars")?.let { s ->
                for (key in s.keys()) {
                    val level = key.toIntOrNull() ?: continue
                    val value = s.optInt(key, 0)
                    if (level in 1..RoadWorlds.LEVEL_COUNT && value in 1..3) stars[level] = value
                }
            }
            val best = HashMap<Int, Int>()
            json.optJSONObject("best")?.let { b ->
                for (key in b.keys()) {
                    val level = key.toIntOrNull() ?: continue
                    val value = b.optInt(key, -1)
                    if (level in 1..RoadWorlds.LEVEL_COUNT && value >= 0) best[level] = value
                }
            }
            return RoadProgress(
                stars = stars,
                best = best,
                hits = json.optInt("hits", 0).coerceAtLeast(0),
                crossings = json.optInt("crossings", 0).coerceAtLeast(0),
            )
        }
    }
}
