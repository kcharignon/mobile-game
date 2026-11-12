package com.kcharignon.petitsjeux.games.plane

import com.kcharignon.petitsjeux.games.plane.engine.PlaneDifficulty
import org.json.JSONObject

/**
 * Ce que Super Avion garde d'un joueur : sa part de la sauvegarde (`games.plane`). Tolérant : un champ absent ou invalide vaut zéro.
 * Les étoiles de chaque difficulté sont la **meilleure** note (1 à 3) de ses courses ; la difficulté suivante se débloque à 2 étoiles sur la précédente.
 */
data class PlaneProgress(
    /** Meilleure note (1 à 3 étoiles) de chaque difficulté jouée (indice de la difficulté -> étoiles). */
    val stars: Map<Int, Int> = emptyMap(),
    /** Meilleur score de chaque difficulté jouée (indice de la difficulté -> points). */
    val best: Map<Int, Int> = emptyMap(),
    /** Chocs en tout (tous vols). */
    val hits: Int = 0,
    /** Tonneaux faits en tout (anneaux traversés). */
    val rolls: Int = 0,
    /** Vols finis. */
    val played: Int = 0,
) {
    fun hasPlayed(d: PlaneDifficulty): Boolean = d.index in stars

    /** Étoiles de la difficulté : 0 si jamais jouée, sinon 1 à 3. */
    fun stars(d: PlaneDifficulty): Int = stars[d.index] ?: 0

    fun bestScore(d: PlaneDifficulty): Int = best[d.index] ?: 0

    val totalStars: Int get() = PlaneDifficulty.entries.sumOf { stars(it) }

    /** La première difficulté est ouverte ; les autres demandent 2 étoiles sur la précédente. */
    fun isUnlocked(d: PlaneDifficulty): Boolean = d.previous?.let { stars(it) >= PlaneDifficulty.UNLOCK_STARS } ?: true

    /**
     * La difficulté à lancer tout de suite quand l'enfant ouvre le jeu, sans passer par le choix : la plus élevée **déjà jouée**
     * (pas la plus haute débloquée, qu'il n'a peut-être pas encore essayée) ; la première s'il n'a jamais joué.
     */
    val launchDifficulty: PlaneDifficulty get() = PlaneDifficulty.entries.lastOrNull { hasPlayed(it) } ?: PlaneDifficulty.entries.first()

    /** Le meilleur score de toutes les difficultés (0 si jamais joué). */
    val topScore: Int get() = best.values.maxOrNull() ?: 0

    /** Après une course : garde la meilleure note et le meilleur score, additionne les compteurs. */
    fun withResult(d: PlaneDifficulty, earnedStars: Int, score: Int, hitsNow: Int, rollsNow: Int): PlaneProgress =
        copy(
            stars = stars + (d.index to maxOf(stars(d), earnedStars.coerceIn(1, PlaneDifficulty.MAX_STARS))),
            best = best + (d.index to maxOf(bestScore(d), score.coerceAtLeast(0))),
            hits = hits + hitsNow.coerceAtLeast(0),
            rolls = rolls + rollsNow.coerceAtLeast(0),
            played = played + 1,
        )

    fun toJson(): JSONObject {
        fun map(m: Map<Int, Int>) = JSONObject().also { o -> for ((k, v) in m.toSortedMap()) o.put(k.toString(), v) }
        return JSONObject().put("stars", map(stars)).put("best", map(best)).put("hits", hits).put("rolls", rolls).put("played", played)
    }

    companion object {
        const val GAME_ID = "plane"

        fun fromJson(json: JSONObject?): PlaneProgress {
            if (json == null) return PlaneProgress()
            fun read(key: String, valid: (Int) -> Boolean): Map<Int, Int> {
                val out = HashMap<Int, Int>()
                json.optJSONObject(key)?.let { o ->
                    for (k in o.keys()) {
                        val index = k.toIntOrNull() ?: continue
                        val v = o.optInt(k, -1)
                        if (index in PlaneDifficulty.entries.indices && valid(v)) out[index] = v
                    }
                }
                return out
            }
            return PlaneProgress(
                stars = read("stars") { it in 1..PlaneDifficulty.MAX_STARS },
                best = read("best") { it >= 0 },
                hits = json.optInt("hits", 0).coerceAtLeast(0),
                // (la première version du jeu, un skateur, rangeait ses « rampes » sous la clé `jumps`)
                rolls = json.optInt("rolls", json.optInt("jumps", 0)).coerceAtLeast(0),
                played = json.optInt("played", 0).coerceAtLeast(0),
            )
        }
    }
}
