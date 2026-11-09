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

    /** Rotation à appliquer à un dessin dont le nez regarde vers le haut. */
    val degrees: Float
        get() = when (this) {
            UP -> 0f
            RIGHT -> 90f
            DOWN -> 180f
            LEFT -> 270f
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

    /** Cases devant le véhicule dans le sens du mouvement, de la plus proche au bord du plateau. Elles doivent être libres pour qu'il sorte. */
    fun path(cols: Int, rows: Int, move: Move = Move.FORWARD): List<Cell> {
        val d = directionOf(move)
        val start = lead(move)
        val out = ArrayList<Cell>()
        var c = start.col + d.dc
        var r = start.row + d.dr
        while (c in 0 until cols && r in 0 until rows) {
            out += Cell(c, r)
            c += d.dc
            r += d.dr
        }
        return out
    }
}

/** Un parking à vider. [index] commence à 1. [cones] : les plots de chantier (jamais déplacés, on ne passe pas dessus). */
data class Level(
    val index: Int,
    val cols: Int,
    val rows: Int,
    val cars: List<Car>,
    val cones: List<Cell> = emptyList(),
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
    }

    private fun inGrid(cell: Cell) = cell.col in 0 until cols && cell.row in 0 until rows
}

/** Les mondes de 20 niveaux, regroupés par 60 : à chaque groupe, une nouvelle mécanique. */
object Worlds {
    const val LEVELS_PER_WORLD = 20
    const val LEVELS_PER_GROUP = 60
    const val GROUPS = 4
    const val COUNT = GROUPS * LEVELS_PER_GROUP / LEVELS_PER_WORLD
    const val LEVEL_COUNT = GROUPS * LEVELS_PER_GROUP

    val names = listOf(
        "Le petit parking", "Le centre-ville", "Le centre commercial", // 1-60 : avancer et reculer
        "Les travaux", "Le chantier", "Le tunnel", // 61-120 : plots de chantier
        "Le grand parking", "L'aéroport", "La gare", // 121-180 : parking agrandi
        "Le port", "L'entrepôt", "Le grand garage", // 181-240 : caisses qui tombent
    )

    /** Monde (0 à 11) d'un niveau (1 à 240) ; au-delà, le dernier monde. */
    fun of(levelIndex: Int): Int = ((levelIndex - 1) / LEVELS_PER_WORLD).coerceIn(0, COUNT - 1)

    /** Groupe de 60 niveaux (0 à 3) : 0 = avancer et reculer, 1 = plots de chantier, 2 = parking agrandi, 3 = caisses qui tombent. */
    fun groupOf(levelIndex: Int): Int = ((levelIndex - 1) / LEVELS_PER_GROUP).coerceIn(0, GROUPS - 1)

    /** Premier niveau du monde. */
    fun firstLevel(world: Int): Int = world * LEVELS_PER_WORLD + 1
}
