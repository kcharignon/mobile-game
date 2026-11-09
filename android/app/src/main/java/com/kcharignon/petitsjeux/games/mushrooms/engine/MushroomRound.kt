package com.kcharignon.petitsjeux.games.mushrooms.engine

import kotlin.random.Random

/** Ce qui se passe pendant une partie ; l'interface s'en sert pour les animations et les sons. */
sealed interface RoundEvent {
    /** Un champignon s'allume ([decoy] : de la couleur interdite). */
    data class Lit(val id: Int, val color: Int, val decoy: Boolean) : RoundEvent

    /** Un champignon allumé est touché : [points] gagnés, [streak] touchés de suite sans en rater un. */
    data class Picked(val id: Int, val points: Int, val reactionMs: Long, val streak: Int) : RoundEvent

    /** Un champignon allumé s'est éteint avant d'être touché (aucune punition). */
    data class Missed(val id: Int) : RoundEvent

    /** Un champignon interdit s'éteint tout seul : bravo, on ne l'a pas touché. */
    data class DecoyGone(val id: Int) : RoundEvent

    /** On a touché un champignon interdit : aucun point, la série repart de zéro. */
    data class DecoyTapped(val id: Int) : RoundEvent

    /** On a touché un champignon éteint : rien ne se passe. */
    data class Nothing(val id: Int) : RoundEvent

    /** Tous les champignons ont été touchés ou éteints : la partie est finie. */
    data object Finished : RoundEvent
}

/**
 * Une partie de Champis Magiques : des champignons s'allument, on les touche vite. Règles pures, sans Android, pilotées par un temps [now] en millisecondes
 * que l'interface donne à [update] et [tap] (donc testables avec une horloge simulée). On ne perd jamais : la partie finit quand tous les champignons ont été
 * touchés ou éteints ([Difficulty.targets] allumages), et il reste toujours au moins une étoile.
 *
 * Les couleurs des champignons allumés : [FIRST_COLOR] à [FIRST_COLOR] + [COLORS] − 1 (jaune, orange, rose, cyan, vert) ; la couleur interdite est [FORBIDDEN_COLOR] (violet).
 */
class MushroomRound(val difficulty: Difficulty, private val mushrooms: Int = difficulty.mushrooms, seed: Long = 0L) {
    /** Un champignon allumé. */
    class LitMushroom(val id: Int, val litAt: Long, val color: Int, val decoy: Boolean)

    private val random = Random(seed)
    private val litMap = LinkedHashMap<Int, LitMushroom>()
    private val recentlyOut = HashMap<Int, Long>()
    private var nextSpawnAt = START_DELAY_MS
    private var nextDecoyAt = START_DELAY_MS + 2000L

    /** Les champignons allumés en ce moment, dans l'ordre d'allumage. */
    val lit: List<LitMushroom> get() = litMap.values.toList()

    var score = 0
        private set

    /** Champignons à allumer en tout, déjà allumés, touchés, éteints sans avoir été touchés. */
    val targets: Int = difficulty.targets
    var spawned = 0
        private set
    var picked = 0
        private set
    var missed = 0
        private set
    var streak = 0
        private set
    var bestStreak = 0
        private set

    var finished = false
        private set

    /** Combien de champignons restent à toucher (allumés ou pas encore allumés). */
    val remaining: Int get() = targets - picked - missed

    val stars: Int get() = difficulty.stars(score)

    fun isLit(id: Int): Boolean = id in litMap

    fun litMushroom(id: Int): LitMushroom? = litMap[id]

    private fun realLit(): Int = litMap.values.count { !it.decoy }

    private fun decoysLit(): Int = litMap.values.count { it.decoy }

    /** Fait avancer la partie jusqu'à [now] : les champignons trop lents s'éteignent, d'autres s'allument. */
    fun update(now: Long): List<RoundEvent> {
        if (finished) return emptyList()
        val events = ArrayList<RoundEvent>()
        // les champignons qui s'éteignent tout seuls
        val timeLimit = difficulty.litMs
        for (m in litMap.values.toList()) {
            if (m.decoy) {
                if (now - m.litAt >= DECOY_MS) {
                    litMap.remove(m.id)
                    recentlyOut[m.id] = now
                    events += RoundEvent.DecoyGone(m.id)
                }
            } else if (timeLimit != null && now - m.litAt >= timeLimit) {
                litMap.remove(m.id)
                recentlyOut[m.id] = now
                missed++
                streak = 0
                refillSoon(now)
                events += RoundEvent.Missed(m.id)
            }
        }
        // jamais de temps mort : s'il n'y a rien à toucher, le prochain allumage ne tarde pas
        if (spawned in 1 until targets && realLit() == 0) nextSpawnAt = minOf(nextSpawnAt, now + IDLE_DELAY_MS)
        // un nouveau champignon à toucher
        if (spawned < targets && realLit() < difficulty.maxLit && now >= nextSpawnAt) {
            pickFree(now)?.let { id ->
                val color = FIRST_COLOR + random.nextInt(COLORS)
                litMap[id] = LitMushroom(id, now, color, decoy = false)
                spawned++
                events += RoundEvent.Lit(id, color, decoy = false)
            }
            nextSpawnAt = now + random.nextLong(difficulty.gapMs.first, difficulty.gapMs.last + 1)
        }
        // un champignon interdit
        if (difficulty.decoys && spawned < targets && decoysLit() < MAX_DECOYS && now >= nextDecoyAt) {
            pickFree(now)?.let { id ->
                litMap[id] = LitMushroom(id, now, FORBIDDEN_COLOR, decoy = true)
                events += RoundEvent.Lit(id, FORBIDDEN_COLOR, decoy = true)
            }
            nextDecoyAt = now + random.nextLong(1500L, 3001L)
        }
        // fini : tout est touché ou éteint
        if (spawned >= targets && realLit() == 0) {
            for (m in litMap.values.toList()) events += RoundEvent.DecoyGone(m.id)
            litMap.clear()
            finished = true
            events += RoundEvent.Finished
        }
        return events
    }

    /** Un champignon à toucher vient de partir (touché ou éteint) : le suivant s'allume vite, sans attendre que les autres partent, mais pas à la même image. */
    private fun refillSoon(now: Long) {
        nextSpawnAt = maxOf(nextSpawnAt, now + difficulty.gapMs.first)
    }

    /** Un champignon éteint, qui ne vient pas de s'éteindre (pour ne pas rallumer tout de suite le même). */
    private fun pickFree(now: Long): Int? {
        recentlyOut.entries.removeAll { now - it.value > RECENT_MS }
        val free = (0 until mushrooms).filter { it !in litMap && it !in recentlyOut }
        val pool = free.ifEmpty { (0 until mushrooms).filter { it !in litMap } }
        return pool.takeIf { it.isNotEmpty() }?.let { it[random.nextInt(it.size)] }
    }

    /** On touche le champignon [id] à l'instant [now]. */
    fun tap(id: Int, now: Long): RoundEvent {
        if (finished) return RoundEvent.Nothing(id)
        val m = litMap[id] ?: return RoundEvent.Nothing(id)
        litMap.remove(id)
        recentlyOut[id] = now
        if (m.decoy) {
            streak = 0
            return RoundEvent.DecoyTapped(id)
        }
        val reaction = (now - m.litAt).coerceAtLeast(0L)
        refillSoon(now)
        val points = Scoring.points(reaction)
        score += points
        picked++
        streak++
        bestStreak = maxOf(bestStreak, streak)
        return RoundEvent.Picked(id, points, reaction, streak)
    }

    companion object {
        /** Les couleurs des champignons à toucher : 5 teintes vives. */
        const val FIRST_COLOR = 0
        const val COLORS = 5

        /** La couleur interdite. */
        const val FORBIDDEN_COLOR = 5

        const val START_DELAY_MS = 700L
        const val IDLE_DELAY_MS = 350L
        private const val DECOY_MS = 2500L
        private const val MAX_DECOYS = 2
        private const val RECENT_MS = 700L
    }
}
