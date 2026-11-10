package com.kcharignon.petitsjeux.games.parking.engine

/** Direction du nez d'un véhicule. `dc` / `dr` : pas en colonnes / en lignes (la ligne 0 est en haut). */
enum class Dir(val dc: Int, val dr: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    val horizontal: Boolean get() = dr == 0

    val opposite: Dir
        get() = when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }

    /** Un quart de tour vers la droite (sens des aiguilles d'une montre) : haut -> droite -> bas -> gauche. */
    val clockwise: Dir
        get() = when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }

    /** Un quart de tour vers la gauche. */
    val counterClockwise: Dir get() = clockwise.opposite

    /** Rotation à appliquer à un dessin dont le nez regarde vers le haut. */
    val degrees: Float
        get() = when (this) {
            UP -> 0f
            RIGHT -> 90f
            DOWN -> 180f
            LEFT -> 270f
        }
}

/** Comment un véhicule est arrivé sur une case de son trajet : en roulant, poussé de côté par un tapis roulant, ou sorti d'un téléporteur. */
enum class Entry { DRIVE, PUSH, JUMP }

/** Ce qui arrête un véhicule sur son trajet sans qu'il ait besoin d'un obstacle posé : [cell] est la case où il ne peut pas aller ([kind] dit pourquoi). */
data class Stop(val cell: Cell, val kind: ObstacleKind)

/**
 * Le trajet d'un véhicule jusqu'à la sortie : les [cells] qu'il va traverser (la case du nez ne compte pas), dans l'ordre, avec les virages des taches d'huile,
 * les poussées des tapis roulants et les sauts des téléporteurs.
 *
 * - [directions] : le sens où il repart de chaque case, après un éventuel virage ;
 * - [headings] : le sens de marche en arrivant sur chaque case (un tapis ne le change pas : le véhicule glisse de côté sans tourner) ;
 * - [entries] : comment il est arrivé sur chaque case ;
 * - [origins] : la case d'où il part pour arriver sur chacune (le nez du véhicule pour la première) : c'est entre les deux qu'une barrière peut se trouver ;
 * - [finalDirection] : le sens où il quitte le plateau, [finalHeading] : le sens de marche à ce moment-là ;
 * - [exit] : le bord par où il sortirait (un mur peut le fermer), ou `null` s'il est arrêté ;
 * - [stop] : ce qui l'arrête (un sens interdit, ou un tour en rond), ou `null`.
 */
class Route(
    val cells: List<Cell>,
    val directions: List<Dir>,
    val headings: List<Dir>,
    val entries: List<Entry>,
    val origins: List<Cell>,
    val finalDirection: Dir,
    val finalHeading: Dir,
    val exit: Wall?,
    val stop: Stop?,
) {
    /** Il tourne en rond (ou reviendrait sur sa propre place) : il ne sortira jamais par là. */
    val loops: Boolean get() = stop?.kind == ObstacleKind.OIL

    /** Les [n] premières cases seulement (un véhicule qui s'arrête devant un obstacle), puis il continue dans le sens où il roulait ([startDirection] s'il n'a pas avancé). */
    fun take(n: Int, startDirection: Dir): Route {
        val dir = if (n == 0) startDirection else directions[n - 1]
        return Route(cells.take(n), directions.take(n), headings.take(n), entries.take(n), origins.take(n), dir, dir, null, null)
    }
}

/** Un véhicule peut rouler dans le sens de son nez (marche avant) ou à l'opposé (marche arrière), selon le sens où on le glisse. */
enum class Move {
    FORWARD,
    BACKWARD;

    val opposite: Move get() = if (this == FORWARD) BACKWARD else FORWARD
}

data class Cell(val col: Int, val row: Int)

/**
 * Un véhicule large d'une case : voiture (2 cases), camion (3 cases), bus ou poids lourd (4 cases).
 *
 * (col, row) est la case en haut à gauche de son emprise ; [dir] est le sens du nez ; [color] un indice dans la palette de l'interface.
 * [carrier] : un camion qui transporte une caisse (un pick-up) : en partant, il la laisse tomber sur la case que sa **queue** occupait
 * (toujours la même, jamais au hasard). Un véhicule qui passe ensuite sur cette caisse la détruit et continue sa route, mais c'est une faute.
 */
data class Car(
    val id: Int,
    val col: Int,
    val row: Int,
    val len: Int,
    val dir: Dir,
    val color: Int,
    val carrier: Boolean = false,
) {
    init {
        require(len in 2..4) { "un véhicule fait 2 à 4 cases, pas $len" }
        require(!carrier || len == 3) { "seul un camion de 3 cases transporte une caisse" }
    }

    /** Cases occupées, de la plus petite colonne / ligne à la plus grande. */
    val cells: List<Cell> =
        List(len) { i -> if (dir.horizontal) Cell(col + i, row) else Cell(col, row + i) }

    /** Le sens de la route pour ce mouvement. */
    fun directionOf(move: Move): Dir = if (move == Move.FORWARD) dir else dir.opposite

    /** La case de devant dans le sens du mouvement : celle qui sort la première. */
    fun lead(move: Move): Cell = directionOf(move).let { d -> if (d.dc + d.dr > 0) cells.last() else cells.first() }

    /** La case de derrière dans le sens du mouvement : celle où tombe la caisse d'un camion qui part. */
    fun trail(move: Move): Cell = directionOf(move).let { d -> if (d.dc + d.dr > 0) cells.first() else cells.last() }

    /** La case du nez. */
    val head: Cell get() = lead(Move.FORWARD)

    /** L'endroit par où ce véhicule sortirait du parking dans ce sens : le bout de sa file. S'il y a un mur, il ne peut pas sortir par là. */
    fun exitEdge(cols: Int, rows: Int, move: Move = Move.FORWARD, floor: Floor = Floor.NONE): Wall? = route(cols, rows, move, floor).exit

    /**
     * Le trajet de ce véhicule dans ce sens. Il roule tout droit, sauf sur ces cases du [floor] :
     *
     * - **tache d'huile** : virage à 90°, **à droite s'il avance, à gauche s'il recule**, puis il repart tout droit dans ce nouveau sens ;
     * - **tapis roulant** : s'il le traverse (en travers), il est poussé d'une case de côté, sans tourner, puis continue tout droit ; dans le sens du tapis ou à
     *   contresens, il n'a aucun effet ;
     * - **téléporteur** : il entre par une case et ressort par l'autre, dans le même sens, puis continue tout droit ;
     * - **sens unique** : on ne peut le franchir que dans le sens de la flèche (sinon [Route.stop]).
     *
     * Toutes les cases du trajet doivent être libres. Un trajet qui reviendrait sur la place du véhicule ou tournerait en rond n'en est pas un ([Route.loops]).
     */
    fun route(cols: Int, rows: Int, move: Move = Move.FORWARD, floor: Floor = Floor.NONE): Route {
        var d = directionOf(move)
        var cell = lead(move)
        val cells = ArrayList<Cell>()
        val directions = ArrayList<Dir>()
        val headings = ArrayList<Dir>()
        val entries = ArrayList<Entry>()
        val origins = ArrayList<Cell>()
        val own = this.cells.toHashSet()
        val arrived = HashSet<Pair<Cell, Dir>>()
        fun inGrid(c: Cell) = c.col in 0 until cols && c.row in 0 until rows
        fun end(exit: Wall?, stop: Stop?, geometric: Dir, heading: Dir) = Route(cells, directions, headings, entries, origins, geometric, heading, exit, stop)
        fun enter(c: Cell, how: Entry) {
            cells += c
            entries += how
            origins += cell
            headings += d
            directions += d
            cell = c
        }
        while (true) {
            val next = Cell(cell.col + d.dc, cell.row + d.dr)
            if (!inGrid(next)) return end(Wall(cell, d), null, d, d)
            floor.oneWays[next]?.let { if (it != d) return end(null, Stop(next, ObstacleKind.ONEWAY), d, d) }
            // un trajet qui ramène le véhicule sur sa propre place : il se rentrerait dedans, il ne peut pas partir par là
            if (next in own) return end(null, Stop(next, ObstacleKind.OIL), d, d)
            enter(next, Entry.DRIVE)
            // ce que fait la case où il vient d'arriver (un tapis peut le pousser sur une autre case qui agit à son tour)
            effects@ while (true) {
                // revenu au même endroit dans le même sens : il tournerait en rond pour toujours
                if (!arrived.add(cell to d)) return end(null, Stop(cell, ObstacleKind.OIL), d, d)
                val push = floor.belts[cell]
                val other = floor.portals[cell]
                when {
                    cell in floor.oils -> {
                        d = if (move == Move.FORWARD) d.clockwise else d.counterClockwise
                        directions[directions.lastIndex] = d
                        break@effects
                    }
                    push != null && push != d && push != d.opposite -> {
                        val landing = Cell(cell.col + push.dc, cell.row + push.dr)
                        if (!inGrid(landing)) return end(Wall(cell, push), null, push, d)
                        floor.oneWays[landing]?.let { if (it != push) return end(null, Stop(landing, ObstacleKind.ONEWAY), d, d) }
                        if (landing in own) return end(null, Stop(landing, ObstacleKind.OIL), d, d)
                        enter(landing, Entry.PUSH)
                    }
                    other != null -> {
                        if (other in own) return end(null, Stop(other, ObstacleKind.OIL), d, d)
                        enter(other, Entry.JUMP)
                        break@effects // il ressort tout droit : pas d'effet à la sortie du téléporteur
                    }
                    else -> break@effects
                }
            }
        }
    }

    /** Pour les niveaux qui n'ont que des taches d'huile. */
    fun route(cols: Int, rows: Int, move: Move, oils: Collection<Cell>): Route = route(cols, rows, move, Floor(oils = oils.toSet()))

    /** Cases devant le véhicule dans le sens du mouvement (sans rien sur le sol : en ligne droite), de la plus proche au bord du plateau. */
    fun path(cols: Int, rows: Int, move: Move = Move.FORWARD): List<Cell> = route(cols, rows, move).cells

    /** Les cases du véhicule de sa queue à son nez, dans le sens du mouvement. */
    fun cellsTrailToLead(move: Move): List<Cell> = directionOf(move).let { d -> if (d.dc + d.dr > 0) cells else cells.reversed() }
}

/**
 * Un mur posé sur le bord du parking, **à la sortie** d'une file : le véhicule d'une file dont ce bout est muré ne peut pas sortir de ce côté-là.
 * [cell] est une case du bord ; [side] le côté de cette case qui donne sur l'extérieur (HAUT pour la ligne du haut, etc.).
 */
data class Wall(val cell: Cell, val side: Dir)

/** Une flèche posée sur une case : un **sens unique** (on ne la franchit que dans ce sens) ou un **tapis roulant** (il pousse de côté vers ce sens). */
data class Arrow(val cell: Cell, val dir: Dir)

/** Deux cases de téléporteur : un véhicule qui entre par l'une ressort par l'autre. */
data class Portal(val a: Cell, val b: Cell)

/**
 * Une barrière de péage, posée **entre deux cases voisines** ([a] et [b]) comme un petit mur : baissée (fermée), on ne passe pas d'une case à l'autre ;
 * levée (ouverte), on passe. **Elle se lève ou se baisse à chaque véhicule qui sort du parking** (qu'il passe par elle ou non) ; un véhicule qui klaxonne devant
 * une barrière baissée la fait aussi changer (c'est une faute, mais on n'est jamais bloqué pour de bon). [startsOpen] : levée au début ?
 */
data class Gate(val a: Cell, val b: Cell, val startsOpen: Boolean) {
    init {
        require(Math.abs(a.col - b.col) + Math.abs(a.row - b.row) == 1) { "une barrière sépare deux cases voisines : $a et $b" }
    }

    /** Levée quand le sol a changé [turns] fois (véhicules sortis, plus klaxons devant une barrière) ? */
    fun isOpen(turns: Int): Boolean = startsOpen == (turns % 2 == 0)

    /** Aller de [from] à [to] (deux cases voisines) traverse-t-il cette barrière ? */
    fun separates(from: Cell, to: Cell): Boolean = (from == a && to == b) || (from == b && to == a)

    /** La barrière est-elle posée entre deux cases côte à côte (donc dessinée à la verticale) ? */
    val vertical: Boolean get() = a.row == b.row
}

/**
 * Le sol du parking : tout ce qui change le trajet d'un véhicule ou le bloque selon l'endroit où il passe. Jamais de véhicule garé dessus.
 * [oils] : taches d'huile ; [oneWays] : sens uniques ; [belts] : tapis roulants ; [portals] : pour chaque case de téléporteur, l'autre case ; [gates] : barrières.
 */
class Floor(
    val oils: Set<Cell> = emptySet(),
    val oneWays: Map<Cell, Dir> = emptyMap(),
    val belts: Map<Cell, Dir> = emptyMap(),
    val portals: Map<Cell, Cell> = emptyMap(),
    val gates: List<Gate> = emptyList(),
) {
    /** Les cases du sol qui ne changent pas selon le nombre de sorties (tout sauf les barrières). */
    val isPlain: Boolean get() = gates.isEmpty()

    companion object {
        val NONE = Floor()
    }
}

/**
 * Un parking à vider. [index] commence à 1. [cones] : les plots de chantier (jamais déplacés, on ne passe pas dessus) ; [walls] : les sorties murées ;
 * [oils] : les taches d'huile (virage à 90°) ; [oneWays] : les sens uniques ; [belts] : les tapis roulants (ils poussent de côté) ;
 * [portals] : les téléporteurs ; [gates] : les barrières (elles changent à chaque véhicule qui sort).
 */
data class Level(
    val index: Int,
    val cols: Int,
    val rows: Int,
    val cars: List<Car>,
    val cones: List<Cell> = emptyList(),
    val walls: List<Wall> = emptyList(),
    val oils: List<Cell> = emptyList(),
    val oneWays: List<Arrow> = emptyList(),
    val belts: List<Arrow> = emptyList(),
    val portals: List<Portal> = emptyList(),
    val gates: List<Gate> = emptyList(),
) {
    val world: Int get() = Worlds.of(index)

    /** Le sol de ce niveau. */
    val floor: Floor = Floor(
        oils = oils.toSet(),
        oneWays = oneWays.associate { it.cell to it.dir },
        belts = belts.associate { it.cell to it.dir },
        portals = portals.flatMap { listOf(it.a to it.b, it.b to it.a) }.toMap(),
        gates = gates,
    )

    init {
        val seen = HashSet<Cell>()
        for (car in cars) for (cell in car.cells) {
            require(inGrid(cell)) { "le véhicule ${car.id} sort de la grille en $cell" }
            require(seen.add(cell)) { "deux choses sur la case $cell" }
        }
        fun place(cell: Cell, what: String) {
            require(inGrid(cell)) { "$what sort de la grille en $cell" }
            require(seen.add(cell)) { "deux choses sur la case $cell" }
        }
        cones.forEach { place(it, "un plot") }
        oils.forEach { place(it, "une tache d'huile") }
        oneWays.forEach { place(it.cell, "un sens unique") }
        belts.forEach { place(it.cell, "un tapis roulant") }
        portals.forEach { place(it.a, "un téléporteur"); place(it.b, "un téléporteur") }
        val gateEdges = HashSet<Pair<Cell, Cell>>()
        for (gate in gates) {
            require(inGrid(gate.a) && inGrid(gate.b)) { "une barrière sort de la grille : $gate" }
            val key = if (gate.a.row < gate.b.row || (gate.a.row == gate.b.row && gate.a.col < gate.b.col)) gate.a to gate.b else gate.b to gate.a
            require(gateEdges.add(key)) { "deux barrières au même endroit : $gate" }
        }
        for (car in cars) for ((x, y) in car.cells.zipWithNext()) {
            require(gates.none { it.separates(x, y) }) { "une barrière couperait le véhicule ${car.id} en deux" }
        }
        val edges = HashSet<Wall>()
        for (wall in walls) {
            require(inGrid(wall.cell) && isOutside(wall)) { "un mur doit être sur le bord du parking : $wall" }
            require(edges.add(wall)) { "deux murs au même endroit : $wall" }
        }
    }

    /** Le côté du mur donne-t-il bien sur l'extérieur du parking ? */
    private fun isOutside(w: Wall): Boolean = when (w.side) {
        Dir.UP -> w.cell.row == 0
        Dir.DOWN -> w.cell.row == rows - 1
        Dir.LEFT -> w.cell.col == 0
        Dir.RIGHT -> w.cell.col == cols - 1
    }

    private fun inGrid(cell: Cell) = cell.col in 0 until cols && cell.row in 0 until rows
}

/**
 * Les mondes : **tous les 20 niveaux, le parking s'agrandit ET une nouvelle mécanique arrive** (les anciennes restent, elles se cumulent).
 * Pour ajouter une mécanique : ajouter un monde ici (nom), sa taille dans [LevelGenerator.sizes] et ses réglages dans [LevelGenerator.paramsFor].
 */
object Worlds {
    const val LEVELS_PER_WORLD = 20
    const val COUNT = 9
    const val LEVEL_COUNT = COUNT * LEVELS_PER_WORLD

    /**
     * Un monde par mécanique : 1 avant et arrière, 2 plots de chantier, 3 murs sur le bord, 4 pick-up qui laissent tomber une caisse, 5 taches d'huile,
     * 6 sens uniques, 7 tapis roulants, 8 téléporteurs, 9 barrières.
     */
    val names = listOf(
        "Le petit parking", "Les travaux", "Le château fort", "Le port", "La station-service",
        "Le parking à étages", "L'usine", "Le parc magique", "Le péage",
    )

    /** Monde (0 à 8) d'un niveau (1 à 180) ; au-delà, le dernier monde. */
    fun of(levelIndex: Int): Int = ((levelIndex - 1) / LEVELS_PER_WORLD).coerceIn(0, COUNT - 1)

    /** Premier niveau du monde. */
    fun firstLevel(world: Int): Int = world * LEVELS_PER_WORLD + 1
}
