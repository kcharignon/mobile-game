package com.kcharignon.petitsjeux.games.skate

import com.kcharignon.petitsjeux.games.skate.engine.SkateDifficulty
import org.json.JSONObject

/**
 * Ce que Super Skate garde d'un joueur : sa part de la sauvegarde (`games.skate`). Tolérant : un champ absent ou invalide vaut zéro.
 * Les étoiles de chaque difficulté sont la **meilleure** note (1 à 3) de ses courses ; la difficulté suivante se débloque à 2 étoiles sur la précédente.
 */
data class SkateProgress(
    /** Meilleure note (1 à 3 étoiles) de chaque difficulté jouée (indice de la difficulté -> étoiles). */
    val stars: Map<Int, Int> = emptyMap(),
    /** Meilleur score de chaque difficulté jouée (indice de la difficulté -> points). */
    val best: Map<Int, Int> = emptyMap(),
    /** Chocs en tout (toutes courses). */
    val hits: Int = 0,
    /** Rampes prises en tout. */
    val jumps: Int = 0,
    /** Courses finies. */
    val played: Int = 0,
) {
    fun hasPlayed(d: SkateDifficulty): Boolean = d.index in stars

    /** Étoiles de la difficulté : 0 si jamais jouée, sinon 1 à 3. */
    fun stars(d: SkateDifficulty): Int = stars[d.index] ?: 0

    fun bestScore(d: SkateDifficulty): Int = best[d.index] ?: 0

    val totalStars: Int get() = SkateDifficulty.entries.sumOf { stars(it) }

    /** La première difficulté est ouverte ; les autres demandent 2 étoiles sur la précédente. */
    fun isUnlocked(d: SkateDifficulty): Boolean = d.previous?.let { stars(it) >= SkateDifficulty.UNLOCK_STARS } ?: true

    /**
     * La difficulté à lancer tout de suite quand l'enfant ouvre le jeu, sans passer par le choix : la plus élevée **déjà jouée**
     * (pas la plus haute débloquée, qu'il n'a peut-être pas encore essayée) ; la première s'il n'a jamais joué.
     */
    val launchDifficulty: SkateDifficulty get() = SkateDifficulty.entries.lastOrNull { hasPlayed(it) } ?: SkateDifficulty.entries.first()

    /** Le meilleur score de toutes les difficultés (0 si jamais joué). */
    val topScore: Int get() = best.values.maxOrNull() ?: 0

    /** Après une course : garde la meilleure note et le meilleur score, additionne les compteurs. */
    fun withResult(d: SkateDifficulty, earnedStars: Int, score: Int, hitsNow: Int, jumpsNow: Int): SkateProgress =
        copy(
            stars = stars + (d.index to maxOf(stars(d), earnedStars.coerceIn(1, SkateDifficulty.MAX_STARS))),
            best = best + (d.index to maxOf(bestScore(d), score.coerceAtLeast(0))),
            hits = hits + hitsNow.coerceAtLeast(0),
            jumps = jumps + jumpsNow.coerceAtLeast(0),
            played = played + 1,
        )

    fun toJson(): JSONObject {
        fun map(m: Map<Int, Int>) = JSONObject().also { o -> for ((k, v) in m.toSortedMap()) o.put(k.toString(), v) }
        return JSONObject().put("stars", map(stars)).put("best", map(best)).put("hits", hits).put("jumps", jumps).put("played", played)
    }

    companion object {
        const val GAME_ID = "skate"

        fun fromJson(json: JSONObject?): SkateProgress {
            if (json == null) return SkateProgress()
            fun read(key: String, valid: (Int) -> Boolean): Map<Int, Int> {
                val out = HashMap<Int, Int>()
                json.optJSONObject(key)?.let { o ->
                    for (k in o.keys()) {
                        val index = k.toIntOrNull() ?: continue
                        val v = o.optInt(k, -1)
                        if (index in SkateDifficulty.entries.indices && valid(v)) out[index] = v
                    }
                }
                return out
            }
            return SkateProgress(
                stars = read("stars") { it in 1..SkateDifficulty.MAX_STARS },
                best = read("best") { it >= 0 },
                hits = json.optInt("hits", 0).coerceAtLeast(0),
                jumps = json.optInt("jumps", 0).coerceAtLeast(0),
                played = json.optInt("played", 0).coerceAtLeast(0),
            )
        }
    }
}
