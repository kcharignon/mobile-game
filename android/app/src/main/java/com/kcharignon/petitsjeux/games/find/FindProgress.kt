package com.kcharignon.petitsjeux.games.find

import com.kcharignon.petitsjeux.games.find.engine.FindDifficulty
import org.json.JSONArray
import org.json.JSONObject

/**
 * Ce que Cherche et Trouve garde d'un joueur : sa part de la sauvegarde (`games.find`). Tolérant : un champ absent ou invalide vaut zéro.
 * Les étoiles de chaque difficulté sont la **meilleure** note (1 à 3) de ses manches. La difficulté suivante, elle, ne se débloque pas avec les étoiles
 * mais avec une manche **parfaite** de la précédente (aucune erreur, tout trouvé avant que l'aide se déclenche) : voir [cleared].
 * (La première version du jeu, à 100 niveaux fixes, rangeait ses étoiles sous la clé `stars` : elle est ignorée.)
 */
data class FindProgress(
    /** Meilleure note (1 à 3 étoiles) de chaque difficulté jouée (indice de la difficulté -> étoiles). */
    val best: Map<Int, Int> = emptyMap(),
    /** Les difficultés dont une manche a été parfaite (indices) : chacune ouvre la suivante. Une difficulté ouverte le reste toujours. */
    val cleared: Set<Int> = emptySet(),
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

    /** Une manche de cette difficulté a-t-elle déjà été parfaite ? */
    fun isCleared(d: FindDifficulty): Boolean = d.index in cleared

    /** La première difficulté est ouverte ; les autres demandent une manche parfaite de la précédente. */
    fun isUnlocked(d: FindDifficulty): Boolean = d.previous?.let { isCleared(it) } ?: true

    /**
     * La difficulté à lancer tout de suite quand l'enfant ouvre le jeu, sans passer par le choix : la plus élevée **déjà jouée**
     * (pas la plus haute débloquée, qu'il n'a peut-être pas encore essayée) ; la première s'il n'a jamais joué.
     */
    val launchDifficulty: FindDifficulty get() = FindDifficulty.entries.lastOrNull { hasPlayed(it) } ?: FindDifficulty.entries.first()

    /** Après une manche : garde la meilleure note, retient une manche [flawless] (parfaite) et additionne les compteurs. */
    fun withResult(d: FindDifficulty, earnedStars: Int, mistakesNow: Int, foundNow: Int, flawless: Boolean): FindProgress =
        copy(
            best = best + (d.index to maxOf(stars(d), earnedStars.coerceIn(1, 3))),
            cleared = if (flawless) cleared + d.index else cleared,
            found = found + foundNow.coerceAtLeast(0),
            mistakes = mistakes + mistakesNow.coerceAtLeast(0),
            played = played + 1,
        )

    fun toJson(): JSONObject {
        val bestJson = JSONObject()
        for ((k, v) in best.toSortedMap()) bestJson.put(k.toString(), v)
        val clearedJson = JSONArray()
        for (k in cleared.sorted()) clearedJson.put(k)
        return JSONObject().put("best", bestJson).put("cleared", clearedJson).put("found", found).put("mistakes", mistakes).put("played", played)
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
            val cleared = json.optJSONArray("cleared")?.let { a ->
                (0 until a.length()).map { a.optInt(it, -1) }.filter { it in FindDifficulty.entries.indices }.toSet()
            }
                // une sauvegarde d'avant cette règle (0.13.0) ouvrait la suivante à 2 étoiles : ce qui était ouvert le reste
                ?: best.filterValues { it >= 2 }.keys
            return FindProgress(
                best = best,
                cleared = cleared,
                found = json.optInt("found", 0).coerceAtLeast(0),
                mistakes = json.optInt("mistakes", 0).coerceAtLeast(0),
                played = json.optInt("played", 0).coerceAtLeast(0),
            )
        }
    }
}
