package com.kcharignon.petitsjeux.games.road.engine

import kotlin.math.max
import kotlin.math.min

/** Où le poussin saute : devant lui (vers l'arrivée), derrière, ou de côté. */
enum class HopDir(val dCol: Int, val dRow: Int) {
    FORWARD(0, 1), BACK(0, -1), LEFT(-1, 0), RIGHT(1, 0),
}

sealed interface CrossingEvent {
    data class Hopped(val dir: HopDir) : CrossingEvent

    /** Le poussin arrive pour la première fois sur la ligne [row]. */
    data class NewRow(val row: Int, val points: Int) : CrossingEvent

    /** Une graine ramassée ; [taken] = combien de graines depuis le début de la traversée. */
    data class GrainTaken(val grain: Grain, val points: Int, val taken: Int) : CrossingEvent

    /** Le poussin est écrasé : il perd [lost] points (jamais la partie). */
    data class Hit(val lost: Int, val by: VehicleKind, val row: Int) : CrossingEvent

    /** Le poussin revient au dernier refuge, et clignote un moment. */
    data object Respawned : CrossingEvent

    data class Arrived(val points: Int) : CrossingEvent
}

enum class ChickState { ALIVE, SQUASHED, DONE }

/** Où est le poussin à un instant : [x] = centre en cases (colonne + 0,5), [row] = ligne (décimale pendant un saut), [hop] = avancement du saut (0 à 1) ou -1 s'il ne saute pas. */
data class ChickPose(val x: Double, val row: Double, val hop: Double)

/**
 * Une traversée : le poussin, son score, la route d'un niveau. **Piloté par une horloge** (millisecondes, ne recule jamais), sans Android ni hasard :
 * [update] fait avancer le temps, [hop] fait sauter. Les véhicules sont des fonctions du temps ([Traffic]), donc rejouer les mêmes sauts aux mêmes instants
 * donne toujours le même résultat.
 *
 * - Un saut dure [Road.HOP_MS] ; un toucher pendant un saut est mis en attente (une seule place) et joué à l'atterrissage.
 * - Les points : +10 la première fois qu'on atteint une ligne, +10 par graine, +50 à l'arrivée ; −15 quand on est écrasé (jamais sous 0).
 * - Écrasé : on reste aplati un moment, puis on revient au **dernier refuge où l'on s'est posé** (ou au départ), invulnérable un moment. On ne perd jamais la partie.
 */
class Crossing(val level: RoadLevel) {
    var row = 0
        private set
    var col = Road.START_COL
        private set
    var state = ChickState.ALIVE
        private set
    var score = 0
        private set
    var hits = 0
        private set
    var furthest = 0
        private set

    /** Le dernier refuge (ou le départ) où le poussin s'est posé : c'est là qu'il revient quand il est écrasé. */
    var checkpoint = 0
        private set
    private val takenIds = LinkedHashSet<Int>()
    val taken: Set<Int> get() = takenIds

    /** L'instant (ms) où le poussin a été aplati la dernière fois, et jusqu'à quand il l'est. */
    var squashedAt = -1L
        private set
    private var squashUntil = -1L
    var invulnerableUntil = -1L
        private set

    private var hopStart = -1L
    private var fromRow = 0
    private var fromCol = 0
    private var toRow = 0
    private var toCol = 0
    private var pending: HopDir? = null
    private var clock = 0L

    val isHopping: Boolean get() = hopStart >= 0

    fun invulnerable(now: Long): Boolean = now < invulnerableUntil

    fun pose(now: Long): ChickPose {
        if (hopStart < 0) return ChickPose(col + 0.5, row.toDouble(), -1.0)
        val p = ((now - hopStart).toDouble() / Road.HOP_MS).coerceIn(0.0, 1.0)
        return ChickPose(fromCol + (toCol - fromCol) * p + 0.5, fromRow + (toRow - fromRow) * p, p)
    }

    /** Fait avancer le temps jusqu'à [now] (ms) : atterrissages, écrasements, retours. Renvoie ce qui s'est passé, dans l'ordre. */
    fun update(now: Long): List<CrossingEvent> {
        val events = ArrayList<CrossingEvent>()
        var s = clock
        while (s < now) {
            s = min(now, s + Road.STEP_MS)
            step(s, events)
        }
        clock = max(clock, now)
        return events
    }

    /** Le poussin saute dans la direction [dir] à l'instant [now] ; renvoie aussi ce qui s'est passé depuis la dernière mise à jour. */
    fun hop(dir: HopDir, now: Long): List<CrossingEvent> {
        val events = ArrayList(update(now))
        if (state != ChickState.ALIVE) return events
        if (hopStart >= 0) {
            pending = dir
        } else {
            startHop(dir, clock, events)
        }
        return events
    }

    private fun startHop(dir: HopDir, at: Long, events: MutableList<CrossingEvent>) {
        val r = row + dir.dRow
        val c = col + dir.dCol
        if (r !in level.rows.indices || c !in 0 until Road.COLS) return
        fromRow = row
        fromCol = col
        toRow = r
        toCol = c
        hopStart = at
        events += CrossingEvent.Hopped(dir)
    }

    private fun step(s: Long, events: MutableList<CrossingEvent>) {
        when (state) {
            ChickState.DONE -> return
            ChickState.SQUASHED -> {
                if (s >= squashUntil) {
                    row = checkpoint
                    state = ChickState.ALIVE
                    invulnerableUntil = s + Road.INVULNERABLE_MS
                    events += CrossingEvent.Respawned
                }
            }
            ChickState.ALIVE -> {
                while (hopStart >= 0 && s >= hopStart + Road.HOP_MS && state == ChickState.ALIVE) land(hopStart + Road.HOP_MS, events)
                if (state == ChickState.ALIVE && s >= invulnerableUntil) collide(s, events)
            }
        }
    }

    private fun land(at: Long, events: MutableList<CrossingEvent>) {
        row = toRow
        col = toCol
        hopStart = -1
        if (row > furthest) {
            val points = Road.POINTS_NEW_ROW * (row - furthest)
            furthest = row
            score += points
            events += CrossingEvent.NewRow(row, points)
        }
        level.grains.firstOrNull { it.row == row && it.col == col && it.id !in takenIds }?.let { g ->
            takenIds += g.id
            score += Road.POINTS_GRAIN
            events += CrossingEvent.GrainTaken(g, Road.POINTS_GRAIN, takenIds.size)
        }
        if (level.rows[row].kind.safe) checkpoint = row
        if (row == level.last) {
            state = ChickState.DONE
            pending = null
            score += Road.POINTS_ARRIVAL
            events += CrossingEvent.Arrived(Road.POINTS_ARRIVAL)
            return
        }
        pending?.let {
            pending = null
            startHop(it, at, events)
        }
    }

    private fun collide(s: Long, events: MutableList<CrossingEvent>) {
        val p = ((s - hopStart).toDouble() / Road.HOP_MS).coerceIn(0.0, 1.0)
        val hopping = hopStart >= 0
        val r = if (hopping && p >= 0.5) toRow else if (hopping) fromRow else row
        val x = if (hopping) fromCol + (toCol - fromCol) * p + 0.5 else col + 0.5
        val traffic = level.rows[r].traffic ?: return
        val kind = traffic.hit(s / 1000.0, x - Road.CHICK_HALF, x + Road.CHICK_HALF) ?: return
        // le poussin reste là où il a été touché
        if (hopping) {
            col = if (p >= 0.5) toCol else fromCol
            row = r
            hopStart = -1
        }
        pending = null
        val lost = min(Road.POINTS_HIT, score)
        score -= lost
        hits++
        state = ChickState.SQUASHED
        squashedAt = s
        squashUntil = s + Road.SQUASH_MS
        events += CrossingEvent.Hit(lost, kind, r)
    }
}
