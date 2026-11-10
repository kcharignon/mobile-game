package com.kcharignon.petitsjeux.games.maze.engine

import kotlin.math.abs
import kotlin.math.floor

/** Ce qui se passe quand le doigt trace : l'interface en tire les sons, les bulles et les animations. */
sealed interface MazeEvent {
    /** La trace gagne la case [cell] ; [closer] : cette case est plus près de la sortie que la précédente (le son monte), sinon il descend. */
    data class Stepped(val cell: Cell, val closer: Boolean) : MazeEvent

    /** La trace recule de [removed] cases (les bulles de la partie effacée éclatent). */
    data class Backed(val removed: Int) : MazeEvent

    /** La trace vient de s'engager dans une impasse : une impasse de plus. [cell] est la première case qui s'éloigne de la sortie. */
    data class Detour(val cell: Cell) : MazeEvent

    /** La trace est au fond d'une impasse : plus aucun couloir où aller sans repasser sur elle-même. */
    data class DeadEnd(val cell: Cell) : MazeEvent

    /** Le doigt pousse contre un mur à côté de la tête de la trace : rien ne bouge (ce n'est jamais une faute). */
    data class Blocked(val from: Cell, val toward: Cell) : MazeEvent

    /** La trace est arrivée à la sortie. */
    data object Arrived : MazeEvent
}

/** Un indice : les prochaines cases à suivre, ou le chemin du retour si la trace est dans une impasse. */
data class MazeHint(val cells: List<Cell>, val back: Boolean)

/** Les étoiles d'un niveau selon ses impasses visitées : 0 = 3 étoiles, 1 ou 2 = 2 étoiles, 3 ou plus = 1 étoile. On gagne toujours au moins une étoile. */
object MazeScoring {
    fun stars(detours: Int): Int = when {
        detours <= 0 -> 3
        detours <= 2 -> 2
        else -> 1
    }
}

/**
 * Une partie : le doigt **trace** le chemin du départ à la sortie. Du pur Kotlin, sans Android ni horloge : les positions sont en **cases** (la case (x, y) occupe
 * de x à x + 1 et de y à y + 1) ; c'est l'interface qui convertit les pixels.
 *
 * La trace suit les **couloirs**, pas le doigt : si le doigt est sur une case qu'un couloir relie à la tête de la trace, la trace s'allonge le long du couloir,
 * mais **elle ne traverse jamais un mur**. C'est une suite de cases voisines, qui part du départ et ne repasse jamais deux fois par la même case (si le doigt
 * repasse sur la trace, elle se rembobine jusque-là). Lever le doigt la laisse en place ; on la reprend en reposant le doigt sur sa tête.
 *
 * Les **impasses** : chaque série de pas qui s'éloigne de la sortie (par le chemin le plus court) en compte **une**, quelle que soit sa longueur ; on gagne toujours.
 */
class MazePlay(val level: MazeLevel) {
    private val cells = ArrayList<Cell>().apply { add(level.start) }
    private val index = HashMap<Cell, Int>().apply { put(level.start, 0) }

    /** Y a-t-il un doigt posé sur la trace ? */
    var holding = false
        private set

    /** Impasses visitées. */
    var detours = 0
        private set

    var finished = false
        private set

    /** Dernière case contre laquelle le doigt a poussé (pour ne signaler un mur qu'une fois par appui). */
    private var lastBlocked: Cell? = null

    /** La trace, du départ (inclus) à sa tête. */
    val trail: List<Cell> get() = cells.toList()

    val head: Cell get() = cells.last()

    val stars: Int get() = MazeScoring.stars(detours)

    /**
     * Pose le doigt en ([x], [y]) cases. Il prend la trace s'il est à moins de [GRAB] cases de sa tête (le poisson, au début). Renvoie `true` si le doigt est pris.
     */
    fun down(x: Double, y: Double): Boolean {
        if (finished) return false
        val h = head
        val dx = x - (h.x + 0.5)
        val dy = y - (h.y + 0.5)
        holding = dx * dx + dy * dy <= GRAB * GRAB
        lastBlocked = null
        return holding
    }

    fun up() {
        holding = false
        lastBlocked = null
    }

    /**
     * Le doigt est en ([x], [y]) cases. [reach] : combien de cases la trace a le droit de gagner d'un coup (un doigt rapide en gagne plus) ; une case plus loin est ignorée.
     * Une case à droite d'un mur, atteignable seulement en faisant le tour, est ignorée aussi : la trace s'arrête devant le mur et attend.
     */
    fun move(x: Double, y: Double, reach: Int = DEFAULT_REACH): List<MazeEvent> {
        if (!holding || finished) return emptyList()
        val target = Cell(floor(x).toInt(), floor(y).toInt())
        if (!level.inside(target)) return emptyList()
        val h = head
        if (target == h) return emptyList()
        val wanted = h.manhattan(target)
        val at = index[target]
        if (at != null) {
            // le doigt repasse sur la trace : on la rembobine jusque-là, si c'est tout droit derrière la tête (un doigt qui frôle un couloir voisin du même tracé ne l'efface pas)
            val back = cells.size - 1 - at
            if (back > reach || back != wanted) return emptyList()
            return rewind(at)
        }
        if (wanted > reach) return emptyList()
        val path = monotonePath(h, target)
        if (path == null) {
            // un mur juste à côté : on le signale une fois, la trace ne bouge pas
            if (wanted == 1 && lastBlocked != target) {
                lastBlocked = target
                return listOf(MazeEvent.Blocked(h, target))
            }
            return emptyList()
        }
        lastBlocked = null
        val events = ArrayList<MazeEvent>()
        for (c in path) {
            step(c, events)
            if (finished) break
        }
        return events
    }

    /** Efface la trace et les impasses : on recommence le niveau. */
    fun reset() {
        cells.clear()
        cells.add(level.start)
        index.clear()
        index[level.start] = 0
        holding = false
        detours = 0
        finished = false
        lastBlocked = null
    }

    /**
     * L'indice : si la trace est sur le bon chemin (chaque pas l'a rapprochée de la sortie), les [count] prochaines cases à suivre ;
     * sinon le chemin du retour, de la tête jusqu'à la dernière case où elle était sur le bon chemin (pas plus de [count] cases).
     */
    fun hint(count: Int = HINT_CELLS): MazeHint {
        val validUntil = validPrefix()
        if (validUntil < cells.size - 1) {
            val way = ArrayList<Cell>()
            var i = cells.size - 2
            while (i >= validUntil && way.size < count) way += cells[i--]
            return MazeHint(way, back = true)
        }
        val way = ArrayList<Cell>()
        var here = head
        while (here != level.exit && way.size < count) {
            here = level.neighbors(here).first { level.distanceToExit(it) == level.distanceToExit(here) - 1 }
            way += here
        }
        return MazeHint(way, back = false)
    }

    /** Index de la dernière case de la trace avant le premier pas qui s'éloigne de la sortie (la trace entière si elle n'a jamais reculé du but). */
    private fun validPrefix(): Int {
        for (i in 1 until cells.size) if (level.distanceToExit(cells[i]) > level.distanceToExit(cells[i - 1])) return i - 1
        return cells.size - 1
    }

    private fun rewind(to: Int): List<MazeEvent> {
        val removed = cells.size - 1 - to
        while (cells.size - 1 > to) index.remove(cells.removeAt(cells.size - 1))
        lastBlocked = null
        return listOf(MazeEvent.Backed(removed))
    }

    private fun step(c: Cell, events: MutableList<MazeEvent>) {
        val before = head
        val closer = level.distanceToExit(c) < level.distanceToExit(before)
        // une impasse de plus à chaque fois que la trace se met à s'éloigner de la sortie
        val moving = !closer
        val wasMovingAway = cells.size >= 2 && level.distanceToExit(before) > level.distanceToExit(cells[cells.size - 2])
        index[c] = cells.size
        cells.add(c)
        events += MazeEvent.Stepped(c, closer)
        if (moving && !wasMovingAway) {
            detours++
            events += MazeEvent.Detour(c)
        }
        if (c == level.exit) {
            finished = true
            holding = false
            events += MazeEvent.Arrived
        } else if (level.neighbors(c).none { it !in index }) {
            events += MazeEvent.DeadEnd(c)
        }
    }

    /**
     * Un chemin de [from] (exclu) à [to] (inclus) par les couloirs, qui ne repasse jamais sur la trace et dont **chaque pas rapproche** de [to] : sa longueur est donc
     * exactement la distance à vol d'oiseau en cases (un doigt qui coupe un coin suit le couloir en L ; un doigt de l'autre côté d'un mur n'obtient rien).
     */
    private fun monotonePath(from: Cell, to: Cell): List<Cell>? {
        val previous = HashMap<Cell, Cell>()
        val queue = ArrayDeque<Cell>()
        queue.add(from)
        while (queue.isNotEmpty()) {
            val c = queue.removeFirst()
            if (c == to) {
                val path = ArrayList<Cell>()
                var at = to
                while (at != from) {
                    path += at
                    at = previous.getValue(at)
                }
                path.reverse()
                return path
            }
            for (n in level.neighbors(c)) {
                if (n in index || n in previous || n == from) continue
                if (n.manhattan(to) >= c.manhattan(to)) continue
                previous[n] = c
                queue.add(n)
            }
        }
        return null
    }

    companion object {
        /** À quelle distance (en cases) de la tête de la trace le doigt la prend. */
        const val GRAB = 1.3

        /** Combien de cases la trace gagne d'un coup, au plus, pour un doigt normal. */
        const val DEFAULT_REACH = 3

        /** Combien de cases montre un indice. */
        const val HINT_CELLS = 5

        /** Combien de cases d'un coup pour un doigt qui a parcouru [movedCells] cases depuis le dernier événement : de 3 (doigt lent) à 8 (doigt très rapide). */
        fun reachFor(movedCells: Double): Int = (abs(movedCells) + 2).toInt().coerceIn(DEFAULT_REACH, 8)
    }
}
