package com.kcharignon.petitsjeux.games.parking.engine

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Ce qui bloque un véhicule : un autre véhicule (même s'il roule), un plot de chantier, une barrière fermée, un sens interdit,
 * un mur à la sortie ou un tour en rond (huile, tapis, téléporteur).
 */
sealed interface Blocker {
    data class Vehicle(val car: Car) : Blocker

    data class Obstacle(val cell: Cell, val kind: ObstacleKind) : Blocker

    /** La sortie de la file est murée : on arrive jusqu'au bord, mais on ne passe pas. */
    data class Barrier(val wall: Wall) : Blocker
}

enum class ObstacleKind { CONE, OIL, ONEWAY, GATE }

/** Une caisse tombée que le véhicule écrase en passant : sa case, et l'indice de cette case dans le trajet (0 = la première case devant lui). */
data class Smash(val cell: Cell, val index: Int)

/** Ce qui se passe quand on glisse (ou touche) un véhicule. */
sealed interface TapResult {
    /**
     * La voie est libre : le véhicule sort en suivant son [trip] (cases traversées, avec les virages, les poussées et les sauts) puis continue dans le sens
     * [finalDirection]. [dropped] : la case où un camion à caisse la laisse tomber en partant (celle que sa queue occupait), sinon `null`.
     * [smashed] : les caisses tombées qu'il écrase en passant : il continue sa route, mais chacune est une faute.
     */
    data class Exited(val car: Car, val move: Move, val trip: Route, val dropped: Cell? = null, val smashed: List<Smash> = emptyList()) : TapResult {
        /** Les cases traversées avant le bord du plateau. */
        val route: List<Cell> get() = trip.cells
        val finalDirection: Dir get() = trip.finalDirection

        /** Nombre de cases traversées avant le bord du plateau. */
        val pathLength: Int get() = trip.cells.size
    }

    /**
     * Quelque chose bloque : le véhicule avance de [freeCells] cases (son [trip] : ces cases, avec les virages) dans le sens [direction],
     * klaxonne et revient. C'est une faute.
     * [smashed] : les caisses tombées qu'il a écrasées sur ces [freeCells] cases (une faute de plus pour chacune).
     */
    data class Bumped(val car: Car, val move: Move, val blocker: Blocker, val freeCells: Int, val trip: Route, val smashed: List<Smash> = emptyList()) : TapResult {
        val route: List<Cell> get() = trip.cells
        val direction: Dir get() = trip.finalDirection
    }

    /** Le véhicule n'est plus sur le plateau (déjà sorti) ou est en plein mouvement : rien ne se passe. */
    data object Ignored : TapResult
}

/** Un coup conseillé : ce véhicule, dans ce sens. */
data class Hint(val car: Car, val move: Move)

/**
 * Où en est un véhicule qui roule, **tel qu'il est dessiné** : le chemin qu'il a parcouru depuis son départ, en cases le long de son trajet (0 au départ),
 * ou `null` si inconnu. C'est l'interface qui l'anime ; le moteur le lit pour que les véhicules en mouvement gardent leurs collisions.
 */
fun interface MotionSource {
    fun traveled(carId: Int): Float?
}

/**
 * Accélération (en cases par seconde au carré) de tous les véhicules qui sortent : après t secondes, ils ont parcouru EXIT_ACCELERATION × t² cases.
 * L'animation et le moteur s'accordent là-dessus : le moteur sait où sont dessinés les véhicules, même à venir.
 */
const val EXIT_ACCELERATION = 14f

/** Marge (en cases) de tolérance sur la collision de deux véhicules : s'ils ne se touchent pas visuellement, ou à peine, rien ne se passe. */
const val OVERLAP_MARGIN = 0.25f

/** Étoiles d'un niveau selon le nombre de fautes : 0 = 3 étoiles, 1 ou 2 = 2 étoiles, 3 ou plus = 1 étoile. */
fun starsForBumps(bumps: Int): Int = when {
    bumps <= 0 -> 3
    bumps <= 2 -> 2
    else -> 1
}

/** Une partie de Parking Malin : l'état d'un niveau en cours. Règles pures, aucune dépendance Android. */
class ParkingGame(val level: Level) {
    private val onBoard = LinkedHashMap<Int, Car>().apply { level.cars.forEach { put(it.id, it) } }
    private val fallenCrates = LinkedHashSet<Cell>()

    /**
     * Un véhicule en mouvement jusqu'à ce que [finishMotion] soit appelé : [exiting] = il sort pour de bon (il ne reviendra pas) ; sinon il fonce sur un obstacle
     * puis revient. [path] : toutes les cases qu'il parcourt, de sa queue de départ au bout de sa course (indice 0 = la case de sa queue).
     */
    private class Motion(val car: Car, val move: Move, cells: List<Cell>, val exiting: Boolean) {
        val path: List<Cell> = car.cellsTrailToLead(move) + cells
        val indices: Map<Cell, List<Int>> = path.indices.groupBy { path[it] }

        /** Le plus loin qu'il soit allé (pour savoir s'il revient en arrière). */
        var peak = 0f
    }

    private val motions = LinkedHashMap<Int, Motion>()

    /** Où sont dessinés les véhicules qui roulent (voir [MotionSource]). Sans source, un véhicule en mouvement barre tout son couloir. */
    var motionSource: MotionSource = MotionSource { null }

    /** Fautes commises (véhicules bloqués touchés). */
    var bumps: Int = 0
        private set

    /** Véhicules sortis. */
    var exited: Int = 0
        private set

    val carsLeft: Int get() = onBoard.size
    val isWon: Boolean get() = onBoard.isEmpty()
    val stars: Int get() = starsForBumps(bumps)

    /** Véhicules encore sur le plateau, dans l'ordre du niveau. */
    val cars: List<Car> get() = onBoard.values.toList()

    /** Les caisses tombées (obstacles pour de bon). */
    val crates: Set<Cell> get() = fallenCrates

    /** Y a-t-il encore des véhicules en mouvement ? */
    val isMoving: Boolean get() = motions.isNotEmpty()

    fun isOnBoard(id: Int): Boolean = id in onBoard

    /** L'interface appelle ceci quand l'animation d'un véhicule est terminée : son couloir est de nouveau libre. */
    fun finishMotion(id: Int) {
        motions.remove(id)
    }

    private fun fixedObstacles(): Set<Cell> = level.cones.toSet() + fallenCrates

    /** Cette barrière est-elle ouverte maintenant ? Elle change à chaque véhicule qui sort du parking. */
    fun isOpen(gate: Gate): Boolean = gate.isOpen(exited)

    private fun inGrid(c: Cell) = c.col in 0 until level.cols && c.row in 0 until level.rows

    /** Le trajet de [car] dans ce sens, avec les virages des taches d'huile de ce niveau. */
    fun routeOf(car: Car, move: Move): Route = car.route(level.cols, level.rows, move, level.floor)

    private fun traveled(m: Motion): Float = (motionSource.traveled(m.car.id) ?: 0f).coerceAtLeast(0f)

    /**
     * Les cases que **occupe** (à l'écran) un véhicule qui rebondit, à une marge près : tant qu'il fonce, tout ce qu'il va balayer jusqu'au point de choc ;
     * une fois qu'il revient, seulement l'endroit où il est maintenant (le couloir libéré ne gêne plus personne).
     */
    private fun bouncingCells(m: Motion): List<Cell> {
        val t = traveled(m)
        m.peak = maxOf(m.peak, t)
        val returning = m.peak > 0.5f && t < m.peak - 0.05f
        val from = floor(t + OVERLAP_MARGIN).toInt().coerceIn(0, m.path.size)
        val to = (if (returning) ceil(t + m.car.len - 1 - OVERLAP_MARGIN).toInt() else m.path.size - 1).coerceIn(from - 1, m.path.size - 1)
        return m.path.subList(from, to + 1).filter(::inGrid)
    }

    /** Temps (en secondes) pour avoir parcouru [progress] cases en partant de l'arrêt, avec l'accélération commune à tous les véhicules qui sortent. */
    private fun timeOf(progress: Float): Float = if (progress <= 0f) -1e6f else sqrt(progress / EXIT_ACCELERATION)

    /**
     * Le véhicule [asker] (qu'on vient de glisser) toucherait-il, **à l'écran**, le véhicule [m] qui sort ? [r] : l'indice de [cell] dans le trajet de [asker].
     * On compare les instants où chacun occupe la case : le véhicule qui sort a déjà parcouru ce qu'il a parcouru, [asker] part maintenant ; tous deux accélèrent
     * de la même façon. Une marge ([OVERLAP_MARGIN]) en début et en fin d'occupation : qui frôle l'autre passe. Un véhicule qui suit un autre dans le même sens
     * ne le touche donc jamais (il arrive toujours après), et un véhicule qui traverse devant un autre avant qu'il arrive passe.
     */
    private fun touchesRolling(m: Motion, cell: Cell, r: Int, asker: Car): Boolean {
        val indices = m.indices[cell] ?: return false
        val tau = timeOf(traveled(m)).coerceAtLeast(0f)
        val askerStart = timeOf(r + 1 + OVERLAP_MARGIN)
        val askerEnd = timeOf(asker.len + r + 1 - OVERLAP_MARGIN)
        for (i in indices) {
            val start = timeOf(i - m.car.len + 1 + OVERLAP_MARGIN).let { if (it < -1e5f) it else it - tau }
            val end = timeOf(i + 1 - OVERLAP_MARGIN) - tau
            if (maxOf(start, askerStart) < minOf(end, askerEnd)) return true
        }
        return false
    }

    /** Le premier obstacle sur le trajet [route] de [car] : son indice dans le trajet et ce que c'est, ou `null` si rien ne le touche. */
    private fun firstBlock(car: Car, route: Route): Pair<Int, Blocker>? {
        val fixed = HashMap<Cell, Blocker>()
        for (cone in level.cones) fixed[cone] = Blocker.Obstacle(cone, ObstacleKind.CONE)
        for (gate in level.gates) if (!isOpen(gate)) fixed[gate.cell] = Blocker.Obstacle(gate.cell, ObstacleKind.GATE)
        // un véhicule immobile occupe ses cases ; un véhicule qui rebondit, ce qu'il balaie
        for (other in onBoard.values) if (other.id !in motions) for (cell in other.cells) fixed[cell] = Blocker.Vehicle(other)
        val rolling = ArrayList<Motion>()
        for (m in motions.values) {
            if (m.car.id == car.id) continue
            if (m.exiting) rolling += m else for (cell in bouncingCells(m)) fixed[cell] = Blocker.Vehicle(m.car)
        }
        for ((r, cell) in route.cells.withIndex()) {
            fixed[cell]?.let { return r to it }
            rolling.firstOrNull { touchesRolling(it, cell, r, car) }?.let { return r to Blocker.Vehicle(it.car) }
        }
        return null
    }

    /** Ce qui est le plus proche sur le trajet de [car] dans ce sens (ou le mur à la sortie), ou `null` si la voie est libre. */
    fun blockerOf(car: Car, move: Move = Move.FORWARD): Blocker? {
        val route = routeOf(car, move)
        firstBlock(car, route)?.let { return it.second }
        route.stop?.let { return Blocker.Obstacle(it.cell, it.kind) }
        return route.exit?.takeIf { it in level.walls }?.let { Blocker.Barrier(it) }
    }

    fun canMove(car: Car, move: Move): Boolean = blockerOf(car, move) == null

    /** Un véhicule est glissé (ou touché : un simple toucher = marche avant) dans ce sens. */
    fun tap(id: Int, move: Move = Move.FORWARD): TapResult {
        if (id in motions) return TapResult.Ignored // déjà en mouvement (il rebondit encore) : un deuxième geste ne compte pas
        val car = onBoard[id] ?: return TapResult.Ignored
        val route = routeOf(car, move)
        val block = firstBlock(car, route)
        val blocker = block?.second ?: route.stop?.let { Blocker.Obstacle(it.cell, it.kind) } ?: route.exit?.takeIf { it in level.walls }?.let { Blocker.Barrier(it) }
        if (blocker != null) {
            bumps++
            val free = block?.first ?: route.cells.size
            val trip = route.take(free, car.directionOf(move))
            val smashed = smash(trip.cells)
            motions[id] = Motion(car, move, trip.cells, exiting = false)
            return TapResult.Bumped(car, move, blocker, free, trip, smashed)
        }
        onBoard.remove(id)
        exited++
        motions[id] = Motion(car, move, route.cells, exiting = true)
        val smashed = smash(route.cells)
        val dropped = if (car.carrier) car.trail(move).also { fallenCrates += it } else null
        return TapResult.Exited(car, move, route, dropped, smashed)
    }

    /**
     * Les caisses tombées que ce véhicule écraserait en suivant [cells] : une caisse **ne bloque pas** ; le véhicule la détruit et continue sa route, et chaque
     * caisse détruite est une faute. Une caisse n'est comptée qu'une fois, à la première case où on la rencontre.
     */
    private fun smashedBy(cells: List<Cell>): List<Smash> =
        cells.withIndex().filter { it.value in fallenCrates }.distinctBy { it.value }.map { Smash(it.value, it.index) }

    /** Les caisses que ce véhicule écraserait s'il partait maintenant dans ce sens (aucune si la voie est bloquée par autre chose : il ne roule pas). */
    fun smashes(car: Car, move: Move): List<Smash> = smashedBy(routeOf(car, move).cells)

    private fun smash(cells: List<Cell>): List<Smash> {
        val smashed = smashedBy(cells)
        for (s in smashed) fallenCrates -= s.cell
        bumps += smashed.size
        return smashed
    }

    /**
     * Une vraie impasse : plus aucun coup ne permet de tout faire sortir, **même en écrasant les caisses** (seules les barrières qui changent à chaque sortie
     * peuvent encore en créer une). Les caisses mal placées ne sont plus des impasses : le prochain véhicule les écrase, avec une faute.
     */
    val isStuck: Boolean
        get() = onBoard.isNotEmpty() && !Solver.canFinish(level.cols, level.rows, level.cones.toSet(), onBoard.values.map { it.copy(carrier = false) }, level.walls, level.floor, exits = exited, optimistic = true)

    /** Une caisse mal placée : on ne peut plus tout faire sortir **sans faute** (il faudra en écraser une). Les indices ne le conseillent jamais. */
    val mustSmashACrate: Boolean
        get() = onBoard.isNotEmpty() && !Solver.canFinish(level.cols, level.rows, fixedObstacles(), onBoard.values.toList(), level.walls, level.floor, exits = exited, optimistic = true)

    /**
     * Un coup qui mène à la solution : un véhicule libre **maintenant**, dans un sens où il peut sortir, qui en débloque le plus d'autres.
     * Un coup qui laisserait une caisse au mauvais endroit (il faudrait ensuite l'écraser : une faute) n'est jamais conseillé tant qu'il existe un coup sans faute ; si une caisse mal placée est déjà tombée, on conseille le coup qui en écrase le moins.
     * `null` si le plateau est vide ou si rien n'est libre pour l'instant (un véhicule qui roule encore barre peut-être le passage).
     */
    fun hint(): Hint? {
        val remaining = onBoard.values.toList()
        val fixed = fixedObstacles()
        // un coup qui écrase une caisse retire cette caisse (et coûte une faute) : on cherche d'abord un coup sans aucune faute, sinon celui qui en coûte le moins
        val faults = HashMap<Hint, Int>()
        val reachable = remaining.flatMap { car -> Move.entries.filter { canMove(car, it) }.map { Hint(car, it) } }
            .filter { h ->
                val smashed = smashes(h.car, h.move).map { it.cell }.toSet()
                faults[h] = smashed.size
                val crates = (fixed - smashed).let { if (h.car.carrier) it + h.car.trail(h.move) else it }
                Solver.canFinish(level.cols, level.rows, crates, remaining - h.car, level.walls, level.floor, exits = exited + 1, optimistic = true)
            }
        val fewest = reachable.minOfOrNull { faults.getValue(it) } ?: return null
        val candidates = reachable.filter { faults.getValue(it) == fewest }
        val dependants = candidates.associateWith { h ->
            val cells = h.car.cells.toSet()
            remaining.count { other ->
                other.id != h.car.id && Move.entries.any { m -> routeOf(other, m).cells.any { it in cells } }
            }
        }
        // d'abord ceux qui débloquent le plus ; à égalité, les non-camions (rien ne tombe), puis le plus petit numéro, puis la marche avant
        return candidates.maxWith(
            compareBy<Hint> { dependants.getValue(it) }
                .thenBy { if (it.car.carrier) 0 else 1 }
                .thenBy { -it.car.id }
                .thenBy { if (it.move == Move.FORWARD) 1 else 0 },
        )
    }
}
