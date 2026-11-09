package com.kcharignon.petitsjeux.games.parking.engine

/** Un ensemble de cases (jusqu'à 128) en deux entiers : de quoi tester « cette voie est-elle libre ? » en une opération. */
private class Bits(val lo: Long = 0L, val hi: Long = 0L) {
    fun with(i: Int): Bits = if (i < 64) Bits(lo or (1L shl i), hi) else Bits(lo, hi or (1L shl (i - 64)))
    operator fun plus(o: Bits) = Bits(lo or o.lo, hi or o.hi)
    fun without(o: Bits) = Bits(lo and o.lo.inv(), hi and o.hi.inv())
    fun intersects(o: Bits) = (lo and o.lo) != 0L || (hi and o.hi) != 0L
    override fun equals(other: Any?) = other is Bits && other.lo == lo && other.hi == hi
    override fun hashCode() = 31 * lo.hashCode() + hi.hashCode()
}

/** Ce que dit le solveur d'un niveau. */
data class Analysis(
    /** Peut-on tout faire sortir ? (en tenant compte des caisses que les camions laissent tomber). */
    val solvable: Boolean,
    /** Nombre de « vagues » de sorties (en ignorant les caisses) : la longueur de la plus longue suite de dépendances. */
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
)

/**
 * Le solveur. Règles : un véhicule sort s'il peut rouler **dans un sens ou dans l'autre** jusqu'au bord sans rencontrer ni véhicule, ni plot,
 * ni caisse. Faire sortir un véhicule libère toujours de la place (monotone) : sans caisse, le niveau est soluble si et seulement si on peut
 * tout vider en retirant à chaque fois un véhicule libre, dans n'importe quel ordre. Seuls les camions qui laissent tomber une caisse
 * (qui en **ajoutent** un obstacle) demandent de chercher le bon ordre : il y en a peu, la recherche est minuscule.
 */
object Solver {
    private class Piece(val car: Car, val cells: Bits, val paths: Array<Bits>, val trailIndex: IntArray, val walled: BooleanArray, val usesOil: BooleanArray)

    private class Compiled(val cols: Int, val rows: Int, val pieces: List<Piece>, val cones: Bits) {
        fun index(cell: Cell) = cell.row * cols + cell.col

        fun bits(cells: Iterable<Cell>): Bits = cells.fold(Bits()) { acc, c -> acc.with(index(c)) }
    }

    private fun compile(cols: Int, rows: Int, cones: Collection<Cell>, cars: List<Car>, walls: Collection<Wall>, oils: Collection<Cell>): Compiled {
        require(cols * rows <= 128) { "grille trop grande pour le solveur" }
        fun idx(c: Cell) = c.row * cols + c.col
        val wallSet = walls.toHashSet()
        val oilSet = oils.toHashSet()
        val pieces = cars.map { car ->
            // le trajet de chaque sens (il tourne sur les taches d'huile) : ses cases, et si la sortie est fermée (mur, ou tour en rond sans fin)
            val routes = Array(2) { m -> car.route(cols, rows, Move.entries[m], oilSet) }
            Piece(
                car,
                car.cells.fold(Bits()) { acc, c -> acc.with(idx(c)) },
                Array(2) { m -> routes[m].cells.fold(Bits()) { acc, c -> acc.with(idx(c)) } },
                IntArray(2) { m -> idx(car.trail(Move.entries[m])) },
                BooleanArray(2) { m -> routes[m].loops || routes[m].exit in wallSet },
                BooleanArray(2) { m -> routes[m].cells.any { it in oilSet } },
            )
        }
        return Compiled(cols, rows, pieces, cones.fold(Bits()) { acc, c -> acc.with(idx(c)) })
    }

    private fun occupied(pieces: List<Piece>, fixed: Bits): Bits = pieces.fold(fixed) { acc, p -> acc + p.cells }

    private fun isFree(p: Piece, move: Move, occupied: Bits) = !p.walled[move.ordinal] && !p.paths[move.ordinal].intersects(occupied)

    private fun isFreeEitherWay(p: Piece, occupied: Bits) = isFree(p, Move.FORWARD, occupied) || isFree(p, Move.BACKWARD, occupied)

    /** Retire tant qu'on peut les véhicules qui ne laissent rien tomber (retirer l'un d'eux ne gêne jamais personne). */
    private fun closure(pieces: List<Piece>, fixed: Bits): List<Piece> {
        var rest = pieces
        var occ = occupied(rest, fixed)
        while (true) {
            val free = rest.firstOrNull { !it.car.carrier && isFreeEitherWay(it, occ) } ?: return rest
            rest = rest - free
            occ = occ.without(free.cells)
        }
    }

    private fun finish(pieces: List<Piece>, crates: Bits, cones: Bits, memo: HashMap<String, Boolean>): Boolean {
        val rest = closure(pieces, cones + crates)
        if (rest.isEmpty()) return true
        val key = rest.joinToString(",") { it.car.id.toString() } + "|" + crates.lo + "," + crates.hi
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

    /** Peut-on vider [cars] (placés sur une grille [cols] x [rows], avec ces plots et ces caisses déjà tombées) ? */
    fun canFinish(
        cols: Int,
        rows: Int,
        fixedObstacles: Collection<Cell>,
        cars: List<Car>,
        walls: Collection<Wall> = emptyList(),
        oils: Collection<Cell> = emptyList(),
    ): Boolean {
        val c = compile(cols, rows, fixedObstacles, cars, walls, oils)
        return finish(c.pieces, Bits(), c.cones, HashMap())
    }

    fun canFinish(level: Level): Boolean = canFinish(level.cols, level.rows, level.cones, level.cars, level.walls, level.oils)

    /** Les mouvements possibles tout de suite pour [car] parmi [cars] et ces obstacles fixes (plots, caisses). */
    fun freeMoves(
        cols: Int,
        rows: Int,
        fixedObstacles: Collection<Cell>,
        cars: List<Car>,
        car: Car,
        walls: Collection<Wall> = emptyList(),
        oils: Collection<Cell> = emptyList(),
    ): List<Move> {
        val c = compile(cols, rows, fixedObstacles, cars, walls, oils)
        val occ = occupied(c.pieces, c.cones)
        val piece = c.pieces.first { it.car.id == car.id }
        return Move.entries.filter { isFree(piece, it, occ) }
    }

    fun analyse(level: Level): Analysis {
        val c = compile(level.cols, level.rows, level.cones, level.cars, level.walls, level.oils)
        val solvable = finish(c.pieces, Bits(), c.cones, HashMap())
        // vagues : à chaque tour, tous les véhicules libres partent ensemble (sans caisses : optimiste)
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
                if (isFree(p, m, start) && !finish(c.pieces - p, Bits().with(p.trailIndex[m.ordinal]), c.cones, HashMap())) traps++
            }
        }
        val coneBlocks = c.pieces.count { p -> Move.entries.any { p.paths[it.ordinal].intersects(c.cones) } }
        val wallBlocks = c.pieces.count { p -> p.walled[0] || p.walled[1] }
        val oilTurns = c.pieces.count { it.usesOil[0] || it.usesOil[1] }
        return Analysis(solvable, rounds, free, backwardOnly, traps, coneBlocks, wallBlocks, oilTurns)
    }
}
