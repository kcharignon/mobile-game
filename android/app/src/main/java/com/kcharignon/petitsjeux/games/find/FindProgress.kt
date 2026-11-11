package com.kcharignon.petitsjeux.games.find

import com.kcharignon.petitsjeux.games.find.engine.FindDifficulty
import org.json.JSONObject

/**
 * Ce que Cherche et Trouve garde d'un joueur : sa part de la sauvegarde (`games.find`). Tolérant : un champ absent ou invalide vaut zéro.
 * Les étoiles de chaque difficulté sont la **meilleure** note (1 à 3) de ses manches ; la difficulté suivante se débloque à 2 étoiles sur la précédente.
 * (La première version du jeu, à 100 niveaux fixes, rangeait ses étoiles sous la clé `stars` : elle est ignorée.)
 */
data class FindProgress(
    /** Meilleure note (1 à 3 étoiles) de chaque difficulté jouée (indice de la difficulté -> étoiles). */
    val best: Map<Int, Int> = emptyMap(),
    /** Objets trouvés en tout. */
    val found: Int = 0,
    /** Mauvais objets touchés (toutes manches). */
    val mistakes: Int = 0,
    /** Manches jouées jusqu'au bout. */
    val played: Int = 0,
) {
    fun hasPlayed(d: FindDifficulty): Boolean = d.index in best

    /** Étoiles de la difficulté : 0 si jamais jouée, sinon 1 à 3. */
    fun stars(d: FindDifficulty): Int = best[d.index] ?: 0

    val totalStars: Int get() = FindDifficulty.entries.sumOf { stars(it) }

    /** La première difficulté est ouverte ; les autres demandent 2 étoiles sur la précédente. */
    fun isUnlocked(d: FindDifficulty): Boolean = d.previous?.let { stars(it) >= 2 } ?: true

    /**
     * La difficulté à lancer tout de suite quand l'enfant ouvre le jeu, sans passer par le choix : la plus élevée **déjà jouée**
     * (pas la plus haute débloquée, qu'il n'a peut-être pas encore essayée) ; la première s'il n'a jamais joué.
     */
    val launchDifficulty: FindDifficulty get() = FindDifficulty.entries.lastOrNull { hasPlayed(it) } ?: FindDifficulty.entries.first()

    /** Après une manche : garde la meilleure note et additionne les compteurs. */
    fun withResult(d: FindDifficulty, earnedStars: Int, mistakesNow: Int, foundNow: Int): FindProgress =
        copy(
            best = best + (d.index to maxOf(stars(d), earnedStars.coerceIn(1, 3))),
            found = found + foundNow.coerceAtLeast(0),
            mistakes = mistakes + mistakesNow.coerceAtLeast(0),
            played = played + 1,
        )

    fun toJson(): JSONObject {
        val bestJson = JSONObject()
        for ((k, v) in best.toSortedMap()) bestJson.put(k.toString(), v)
        return JSONObject().put("best", bestJson).put("found", found).put("mistakes", mistakes).put("played", played)
    }

    companion object {
        const val GAME_ID = "find"

        fun fromJson(json: JSONObject?): FindProgress {
            if (json == null) return FindProgress()
            val best = HashMap<Int, Int>()
            json.optJSONObject("best")?.let { b ->
                for (key in b.keys()) {
                    val index = key.toIntOrNull() ?: continue
                    val value = b.optInt(key, 0)
                    if (index in FindDifficulty.entries.indices && value in 1..3) best[index] = value
                }
            }
            return FindProgress(
                best = best,
                found = json.optInt("found", 0).coerceAtLeast(0),
                mistakes = json.optInt("mistakes", 0).coerceAtLeast(0),
                played = json.optInt("played", 0).coerceAtLeast(0),
            )
        }
    }
}
