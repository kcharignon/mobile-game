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

/**
 * Le trajet d'un véhicule jusqu'à la sortie : les [cells] qu'il va traverser (la case du nez ne compte pas), dans l'ordre, en tournant sur les taches d'huile ;
 * [finalDirection] = le sens où il roule à la fin ; [exit] = le bord par où il sortirait (un mur peut le fermer), ou `null` si [loops] (il tourne en rond sur
 * des taches d'huile, ou s'il reviendrait sur sa propre place, et ne sortira jamais).
 */
class Route(val cells: List<Cell>, val directions: List<Dir>, val finalDirection: Dir, val exit: Wall?, val loops: Boolean)

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
 * (toujours la même, jamais au hasard) et elle la bloque pour de bon.
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
    fun exitEdge(cols: Int, rows: Int, move: Move = Move.FORWARD, oils: Collection<Cell> = emptyList()): Wall? = route(cols, rows, move, oils).exit

    /**
     * Le trajet de ce véhicule dans ce sens. Il roule tout droit ; **sur une tache d'huile il fait un virage à 90° : à droite s'il avance, à gauche s'il recule**
     * (un quart de tour de son sens de marche), puis repart tout droit dans ce nouveau sens, jusqu'au bord. Toutes les cases du trajet doivent être libres.
     */
    fun route(cols: Int, rows: Int, move: Move = Move.FORWARD, oils: Collection<Cell> = emptyList()): Route {
        var d = directionOf(move)
        var cell = lead(move)
        val cells = ArrayList<Cell>()
        val directions = ArrayList<Dir>() // le sens où il repart de chaque case (après un éventuel virage)
        val turns = HashSet<Pair<Cell, Dir>>()
        while (true) {
            val next = Cell(cell.col + d.dc, cell.row + d.dr)
            if (next.col !in 0 until cols || next.row !in 0 until rows) return Route(cells, directions, d, Wall(cell, d), false)
            // un virage qui ramène le véhicule sur sa propre place : il se rentrerait dedans, il ne peut pas partir par là
            if (next in this.cells) return Route(cells, directions, d, null, true)
            cells += next
            cell = next
            if (next in oils) {
                d = if (move == Move.FORWARD) d.clockwise else d.counterClockwise
                // revenu au même endroit dans le même sens : il tournerait en rond pour toujours
                if (!turns.add(cell to d)) {
                    directions += d
                    return Route(cells, directions, d, null, true)
                }
            }
            directions += d
        }
    }

    /** Cases devant le véhicule dans le sens du mouvement (sans tache d'huile : en ligne droite), de la plus proche au bord du plateau. */
    fun path(cols: Int, rows: Int, move: Move = Move.FORWARD): List<Cell> = route(cols, rows, move).cells

    /** Les cases du véhicule de sa queue à son nez, dans le sens du mouvement. */
    fun cellsTrailToLead(move: Move): List<Cell> = directionOf(move).let { d -> if (d.dc + d.dr > 0) cells else cells.reversed() }
}

/**
 * Un mur posé sur le bord du parking, **à la sortie** d'une file : le véhicule d'une file dont ce bout est muré ne peut pas sortir de ce côté-là.
 * [cell] est une case du bord ; [side] le côté de cette case qui donne sur l'extérieur (HAUT pour la ligne du haut, etc.).
 */
data class Wall(val cell: Cell, val side: Dir)

/**
 * Un parking à vider. [index] commence à 1. [cones] : les plots de chantier (jamais déplacés, on ne passe pas dessus) ; [walls] : les sorties murées ;
 * [oils] : les taches d'huile (on roule dessus, mais le véhicule y fait un virage à 90°).
 */
data class Level(
    val index: Int,
    val cols: Int,
    val rows: Int,
    val cars: List<Car>,
    val cones: List<Cell> = emptyList(),
    val walls: List<Wall> = emptyList(),
    val oils: List<Cell> = emptyList(),
) {
    val world: Int get() = Worlds.of(index)

    init {
        val seen = HashSet<Cell>()
        for (car in cars) for (cell in car.cells) {
            require(inGrid(cell)) { "le véhicule ${car.id} sort de la grille en $cell" }
            require(seen.add(cell)) { "deux choses sur la case $cell" }
        }
        for (cone in cones) {
            require(inGrid(cone)) { "un plot sort de la grille en $cone" }
            require(seen.add(cone)) { "deux choses sur la case $cone" }
        }
        for (oil in oils) {
            require(inGrid(oil)) { "une tache d'huile sort de la grille en $oil" }
            require(seen.add(oil)) { "deux choses sur la case $oil" }
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
    const val COUNT = 5
    const val LEVEL_COUNT = COUNT * LEVELS_PER_WORLD

    /** Un monde par mécanique : 1 avant et arrière, 2 plots de chantier, 3 murs sur le bord, 4 pick-up qui laissent tomber une caisse, 5 taches d'huile. */
    val names = listOf("Le petit parking", "Les travaux", "Le château fort", "Le port", "La station-service")

    /** Monde (0 à 4) d'un niveau (1 à 100) ; au-delà, le dernier monde. */
    fun of(levelIndex: Int): Int = ((levelIndex - 1) / LEVELS_PER_WORLD).coerceIn(0, COUNT - 1)

    /** Premier niveau du monde. */
    fun firstLevel(world: Int): Int = world * LEVELS_PER_WORLD + 1
}
