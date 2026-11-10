package com.kcharignon.petitsjeux.games.parking.engine

/** Un ensemble de cases (jusqu'à 256) en quatre entiers : de quoi tester « cette voie est-elle libre ? » en quelques opérations. */
private class Bits(val a: Long = 0L, val b: Long = 0L, val c: Long = 0L, val d: Long = 0L) {
    fun with(i: Int): Bits {
        val bit = 1L shl (i and 63)
        return when (i shr 6) {
            0 -> Bits(a or bit, b, c, d)
            1 -> Bits(a, b or bit, c, d)
            2 -> Bits(a, b, c or bit, d)
            else -> Bits(a, b, c, d or bit)
        }
    }

    operator fun plus(o: Bits) = Bits(a or o.a, b or o.b, c or o.c, d or o.d)
    fun without(o: Bits) = Bits(a and o.a.inv(), b and o.b.inv(), c and o.c.inv(), d and o.d.inv())
    fun intersects(o: Bits) = (a and o.a) != 0L || (b and o.b) != 0L || (c and o.c) != 0L || (d and o.d) != 0L
    override fun equals(other: Any?) = other is Bits && other.a == a && other.b == b && other.c == c && other.d == d
    override fun hashCode() = ((31 * a.hashCode() + b.hashCode()) * 31 + c.hashCode()) * 31 + d.hashCode()
}

/** Ce que dit le solveur d'un niveau. */
data class Analysis(
    /** Peut-on tout faire sortir ? (en tenant compte des caisses que les camions laissent tomber, et des barrières qui changent à chaque sortie). */
    val solvable: Boolean,
    /** Nombre de « vagues » de sorties (en ignorant les caisses et les barrières) : la longueur de la plus longue suite de dépendances. */
    val depth: Int,
    /** Véhicules qui peuvent sortir tout de suite, dans un sens ou dans l'autre. */
    val freeAtStart: Int,
    /** Véhicules qui ne peuvent sortir que **en marche arrière** au départ. */
    val backwardOnly: Int,
    /** Premiers coups possibles qui mènent à une impasse (un camion qui laisse tomber sa caisse au mauvais endroit). */
    val traps: Int,
    /** Véhicules dont une voie est coupée par un plot de chantier. */
    val coneBlocks: Int = 0,
    /** Véhicules dont une sortie est murée. */
    val wallBlocks: Int = 0,
    /** Véhicules dont un trajet passe sur une tache d'huile (donc tourne). */
    val oilTurns: Int = 0,
    /** Véhicules dont un sens est coupé par un sens unique. */
    val oneWayBlocks: Int = 0,
    /** Véhicules dont un trajet est poussé de côté par un tapis roulant. */
    val beltPushes: Int = 0,
    /** Véhicules dont un trajet passe par un téléporteur. */
    val portalJumps: Int = 0,
    /** Véhicules dont un trajet passe sur une barrière. */
    val gateBlocks: Int = 0,
)

/**
 * Le solveur. Règles : un véhicule sort s'il peut rouler **dans un sens ou dans l'autre** jusqu'au bord (en suivant les virages de l'huile, les poussées des tapis
 * et les sauts des téléporteurs) sans rencontrer ni véhicule, ni plot, ni caisse, ni barrière baissée, ni sens interdit, et sans mur à la sortie.
 *
 * Faire sortir un véhicule libère toujours de la place (monotone) : sans caisse ni barrière, le niveau est soluble si et seulement si on peut tout vider
 * en retirant à chaque fois un véhicule libre, dans n'importe quel ordre. Les camions qui laissent tomber une caisse **ajoutent** un obstacle : seuls eux
 * demandent de chercher le bon ordre, et ils sont peu nombreux. Les barrières changent à chaque sortie (une fois par véhicule sorti) : là, l'ordre compte aussi (voir [canFinish]).
 */
object Solver {
    private class Piece(
        val index: Int,
        val car: Car,
        val cells: Bits,
        val paths: Array<Bits>,
        val trailIndex: IntArray,
        val walled: BooleanArray,
        val routes: Array<Route>,
        val touchesGate: Boolean,
        /** [move][parité] : une barrière de ce trajet est-elle baissée quand le sol a changé un nombre pair (0) ou impair (1) de fois ? */
        val gateBlocked: Array<BooleanArray>,
    )

    private class Compiled(val cols: Int, val rows: Int, val pieces: List<Piece>, val cones: Bits, val floor: Floor)

    private fun compile(cols: Int, rows: Int, cones: Collection<Cell>, cars: List<Car>, walls: Collection<Wall>, floor: Floor): Compiled {
        require(cols * rows <= 256) { "grille trop grande pour le solveur" }
        fun idx(c: Cell) = c.row * cols + c.col
        val wallSet = walls.toHashSet()
        val pieces = cars.mapIndexed { i, car ->
            // le trajet de chaque sens : ses cases, et si la sortie est fermée (mur, sens unique, tour en rond)
            val routes = Array(2) { m -> car.route(cols, rows, Move.entries[m], floor) }
            val paths = Array(2) { m -> routes[m].cells.fold(Bits()) { acc, c -> acc.with(idx(c)) } }
            // les barrières que ce trajet traverse (entre la case d'où l'on part et celle où l'on arrive ; un saut de téléporteur n'en traverse pas)
            val crossings = Array(2) { m ->
                val r = routes[m]
                r.cells.indices.filter { i -> r.entries[i] != Entry.JUMP }
                    .mapNotNull { i -> floor.gates.indexOfFirst { it.separates(r.origins[i], r.cells[i]) }.takeIf { it >= 0 } }
            }
            Piece(
                i,
                car,
                car.cells.fold(Bits()) { acc, c -> acc.with(idx(c)) },
                paths,
                IntArray(2) { m -> idx(car.trail(Move.entries[m])) },
                BooleanArray(2) { m -> routes[m].stop != null || routes[m].exit in wallSet },
                routes,
                crossings.any { it.isNotEmpty() },
                Array(2) { m -> BooleanArray(2) { parity -> crossings[m].any { !floor.gates[it].isOpen(parity) } } },
            )
        }
        return Compiled(cols, rows, pieces, cones.fold(Bits()) { acc, c -> acc.with(idx(c)) }, floor)
    }

    private fun occupied(pieces: List<Piece>, fixed: Bits): Bits = pieces.fold(fixed) { acc, p -> acc + p.cells }

    private fun isFree(p: Piece, move: Move, occupied: Bits) = !p.walled[move.ordinal] && !p.paths[move.ordinal].intersects(occupied)

    /** Comme [isFree], et en plus aucune barrière de ce trajet n'est baissée quand le sol a changé [turns] fois. */
    private fun isFreeAt(p: Piece, move: Move, occupied: Bits, turns: Int) = isFree(p, move, occupied) && !p.gateBlocked[move.ordinal][turns and 1]

    private fun isFreeEitherWay(p: Piece, occupied: Bits) = isFree(p, Move.FORWARD, occupied) || isFree(p, Move.BACKWARD, occupied)

    /** Retire tant qu'on peut les véhicules qui ne laissent rien tomber (retirer l'un d'eux ne gêne jamais personne). Les barrières comptent comme ouvertes. */
    private fun closure(pieces: List<Piece>, fixed: Bits): List<Piece> {
        var rest = pieces
        var occ = occupied(rest, fixed)
        while (true) {
            val free = rest.firstOrNull { !it.car.carrier && isFreeEitherWay(it, occ) } ?: return rest
            rest = rest - free
            occ = occ.without(free.cells)
        }
    }

    /** Sans tenir compte des barrières (elles comptent comme ouvertes) : peut-on tout vider, avec les caisses que les camions laissent tomber ? */
    private fun finish(pieces: List<Piece>, crates: Bits, cones: Bits, memo: HashMap<String, Boolean>): Boolean {
        val rest = closure(pieces, cones + crates)
        if (rest.isEmpty()) return true
        val key = rest.joinToString(",") { it.index.toString() } + "|" + crates.a + "," + crates.b + "," + crates.c + "," + crates.d
        memo[key]?.let { return it }
        val occ = occupied(rest, cones + crates)
        val ok = rest.any { p ->
            p.car.carrier && Move.entries.any { m ->
                isFree(p, m, occ) && finish(rest - p, crates.with(p.trailIndex[m.ordinal]), cones, memo)
            }
        }
        memo[key] = ok
        return ok
    }

    private class Budget(var left: Int)

    private data class TimedKey(val remaining: Bits, val crates: Bits, val parity: Int)

    /**
     * Avec des barrières : l'état est (véhicules restants, caisses, parité du nombre de sorties). Recherche en profondeur, mémorisée, qui essaie d'abord les
     * véhicules que les barrières concernent. `null` si le budget de la recherche est épuisé (on ne sait pas).
     */
    private fun solveTimed(
        c: Compiled,
        remaining: List<Piece>,
        crates: Bits,
        exits: Int,
        memo: HashMap<TimedKey, Boolean>,
        staticMemo: HashMap<String, Boolean>,
        budget: Budget,
    ): Boolean? {
        if (remaining.isEmpty()) return true
        val key = TimedKey(remaining.fold(Bits()) { acc, p -> acc.with(p.index) }, crates, exits and 1)
        memo[key]?.let { return it }
        // condition nécessaire : même avec toutes les barrières ouvertes, il faut que ce soit faisable
        if (!finish(remaining, crates, c.cones, staticMemo)) {
            memo[key] = false
            return false
        }
        if (--budget.left < 0) return null
        val occ = occupied(remaining, c.cones + crates)
        var unknown = false
        for (p in remaining.sortedBy { if (it.touchesGate) 0 else 1 }) {
            for (m in Move.entries) {
                if (!isFreeAt(p, m, occ, exits)) continue
                val nextCrates = if (p.car.carrier) crates.with(p.trailIndex[m.ordinal]) else crates
                when (solveTimed(c, remaining - p, nextCrates, exits + 1, memo, staticMemo, budget)) {
                    true -> {
                        memo[key] = true
                        return true
                    }
                    null -> unknown = true
                    false -> Unit
                }
            }
        }
        if (unknown) return null
        memo[key] = false
        return false
    }

    private const val TIMED_BUDGET = 4000

    /** Verdict sur l'état [c] : `true`, `false`, ou `null` si la recherche avec barrières a dépassé son budget. */
    private fun verdict(c: Compiled, pieces: List<Piece>, crates: Bits, exits: Int): Boolean? {
        val staticMemo = HashMap<String, Boolean>()
        if (c.floor.gates.isEmpty()) return finish(pieces, crates, c.cones, staticMemo)
        return solveTimed(c, pieces, crates, exits, HashMap(), staticMemo, Budget(TIMED_BUDGET))
    }

    /**
     * Peut-on vider [cars] (placés sur une grille [cols] x [rows], avec ces plots et ces caisses déjà tombées) quand [exits] véhicules sont déjà sortis ?
     * [optimistic] : que répondre si la recherche avec barrières est trop longue pour conclure ? (le jeu dit `true` : jamais de « bloqué » à tort ;
     * le générateur dit `false` : il ne garde que des niveaux dont il est sûr).
     */
    fun canFinish(
        cols: Int,
        rows: Int,
        fixedObstacles: Collection<Cell>,
        cars: List<Car>,
        walls: Collection<Wall> = emptyList(),
        floor: Floor = Floor.NONE,
        exits: Int = 0,
        optimistic: Boolean = false,
    ): Boolean {
        val c = compile(cols, rows, fixedObstacles, cars, walls, floor)
        return verdict(c, c.pieces, Bits(), exits) ?: optimistic
    }

    /** Idem pour des niveaux qui n'ont que des taches d'huile. */
    fun canFinish(cols: Int, rows: Int, fixedObstacles: Collection<Cell>, cars: List<Car>, walls: Collection<Wall>, oils: Collection<Cell>): Boolean =
        canFinish(cols, rows, fixedObstacles, cars, walls, Floor(oils = oils.toSet()))

    fun canFinish(level: Level): Boolean = canFinish(level.cols, level.rows, level.cones, level.cars, level.walls, level.floor)

    /** Les mouvements possibles tout de suite pour [car] parmi [cars] et ces obstacles fixes (plots, caisses), quand [exits] véhicules sont déjà sortis. */
    fun freeMoves(
        cols: Int,
        rows: Int,
        fixedObstacles: Collection<Cell>,
        cars: List<Car>,
        car: Car,
        walls: Collection<Wall> = emptyList(),
        floor: Floor = Floor.NONE,
        exits: Int = 0,
    ): List<Move> {
        val c = compile(cols, rows, fixedObstacles, cars, walls, floor)
        val occ = occupied(c.pieces, c.cones)
        val piece = c.pieces.first { it.car.id == car.id }
        return Move.entries.filter { isFreeAt(piece, it, occ, exits) }
    }

    /** Idem pour des niveaux qui n'ont que des taches d'huile. */
    fun freeMoves(cols: Int, rows: Int, fixedObstacles: Collection<Cell>, cars: List<Car>, car: Car, walls: Collection<Wall>, oils: Collection<Cell>): List<Move> =
        freeMoves(cols, rows, fixedObstacles, cars, car, walls, Floor(oils = oils.toSet()))

    fun analyse(level: Level): Analysis {
        val c = compile(level.cols, level.rows, level.cones, level.cars, level.walls, level.floor)
        val solvable = verdict(c, c.pieces, Bits(), 0) ?: false
        val staticMemo = HashMap<String, Boolean>()
        // vagues : à chaque tour, tous les véhicules libres partent ensemble (sans caisses ni barrières : optimiste)
        var rest = c.pieces
        var occ = occupied(rest, c.cones)
        var rounds = 0
        while (rest.isNotEmpty()) {
            val out = rest.filter { isFreeEitherWay(it, occ) }
            if (out.isEmpty()) break
            rest = rest - out.toSet()
            occ = out.fold(occ) { acc, p -> acc.without(p.cells) }
            rounds++
        }
        val start = occupied(c.pieces, c.cones)
        val free = c.pieces.count { isFreeEitherWay(it, start) }
        val backwardOnly = c.pieces.count { !isFree(it, Move.FORWARD, start) && isFree(it, Move.BACKWARD, start) }
        var traps = 0
        for (p in c.pieces) {
            if (!p.car.carrier) continue
            for (m in Move.entries) {
                if (isFree(p, m, start) && !finish(c.pieces - p, Bits().with(p.trailIndex[m.ordinal]), c.cones, staticMemo)) traps++
            }
        }
        val coneBlocks = c.pieces.count { p -> Move.entries.any { p.paths[it.ordinal].intersects(c.cones) } }
        val wallBlocks = c.pieces.count { p -> p.walled[0] || p.walled[1] }
        fun uses(pick: (Route) -> Boolean) = c.pieces.count { p -> p.routes.any(pick) }
        return Analysis(
            solvable, rounds, free, backwardOnly, traps, coneBlocks, wallBlocks,
            oilTurns = uses { r -> r.cells.any { it in level.floor.oils } },
            oneWayBlocks = uses { r -> r.stop?.kind == ObstacleKind.ONEWAY },
            beltPushes = uses { r -> Entry.PUSH in r.entries },
            portalJumps = uses { r -> Entry.JUMP in r.entries },
            gateBlocks = c.pieces.count { it.touchesGate },
        )
    }
}
