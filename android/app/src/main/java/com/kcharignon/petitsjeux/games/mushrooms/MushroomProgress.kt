package com.kcharignon.petitsjeux.games.mushrooms

import com.kcharignon.petitsjeux.games.mushrooms.engine.Difficulty
import org.json.JSONObject

/**
 * Ce que Champis Magiques garde d'un joueur : sa part de la sauvegarde (`games.mushrooms`). Tolérant : un champ absent ou invalide vaut zéro.
 * Les étoiles se déduisent du meilleur score de chaque difficulté ; la difficulté suivante se débloque à 2 étoiles sur la précédente.
 */
data class MushroomProgress(
    /** Meilleur score de chaque difficulté jouée (indice de la difficulté -> score). */
    val best: Map<Int, Int> = emptyMap(),
    /** Champignons touchés en tout. */
    val picked: Int = 0,
    /** Parties jouées jusqu'au bout. */
    val played: Int = 0,
    /** La plus longue série de champignons touchés de suite. */
    val bestStreak: Int = 0,
) {
    fun bestScore(d: Difficulty): Int = best[d.index] ?: 0

    fun hasPlayed(d: Difficulty): Boolean = d.index in best

    /** Étoiles de la difficulté : 0 si jamais jouée, sinon 1 à 3 selon le meilleur score. */
    fun stars(d: Difficulty): Int = if (hasPlayed(d)) d.stars(bestScore(d)) else 0

    val totalStars: Int get() = Difficulty.entries.sumOf { stars(it) }

    /** La première difficulté est ouverte ; les autres demandent 2 étoiles sur la précédente. */
    fun isUnlocked(d: Difficulty): Boolean = d.previous?.let { stars(it) >= 2 } ?: true

    /** Le meilleur score de toutes les difficultés (0 si jamais joué). */
    val topScore: Int get() = best.values.maxOrNull() ?: 0

    /** Après une partie : garde le meilleur score et additionne les compteurs. */
    fun withResult(d: Difficulty, score: Int, pickedNow: Int, streak: Int): MushroomProgress =
        copy(
            best = best + (d.index to maxOf(bestScore(d), score.coerceAtLeast(0))),
            picked = picked + pickedNow.coerceAtLeast(0),
            played = played + 1,
            bestStreak = maxOf(bestStreak, streak),
        )

    fun toJson(): JSONObject {
        val bestJson = JSONObject()
        for ((k, v) in best.toSortedMap()) bestJson.put(k.toString(), v)
        return JSONObject().put("best", bestJson).put("picked", picked).put("played", played).put("bestStreak", bestStreak)
    }

    companion object {
        const val GAME_ID = "mushrooms"

        fun fromJson(json: JSONObject?): MushroomProgress {
            if (json == null) return MushroomProgress()
            val best = HashMap<Int, Int>()
            json.optJSONObject("best")?.let { b ->
                for (key in b.keys()) {
                    val index = key.toIntOrNull() ?: continue
                    val score = b.optInt(key, -1)
                    if (index in Difficulty.entries.indices && score >= 0) best[index] = score
                }
            }
            return MushroomProgress(
                best = best,
                picked = json.optInt("picked", 0).coerceAtLeast(0),
                played = json.optInt("played", 0).coerceAtLeast(0),
                bestStreak = json.optInt("bestStreak", 0).coerceAtLeast(0),
            )
        }
    }
}
