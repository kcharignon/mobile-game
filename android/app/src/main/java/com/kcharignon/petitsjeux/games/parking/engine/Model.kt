package com.kcharignon.petitsjeux.games.parking.engine

/** Direction du nez d'une voiture. `dc` / `dr` : pas en colonnes / en lignes (la ligne 0 est en haut). */
enum class Dir(val dc: Int, val dr: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    val horizontal: Boolean get() = dr == 0

    /** Rotation à appliquer à un dessin dont le nez regarde vers le haut. */
    val degrees: Float
        get() = when (this) {
            UP -> 0f
            RIGHT -> 90f
            DOWN -> 180f
            LEFT -> 270f
        }
}

data class Cell(val col: Int, val row: Int)

/**
 * Un véhicule large d'une case : voiture (2 cases), camion (3 cases), bus ou poids lourd (4 cases).
 *
 * (col, row) est la case en haut à gauche de son emprise ; [dir] est le sens du nez ; [color] un indice dans la palette de l'interface.
 */
data class Car(
    val id: Int,
    val col: Int,
    val row: Int,
    val len: Int,
    val dir: Dir,
    val color: Int,
) {
    init {
        require(len in 2..4) { "un véhicule fait 2 à 4 cases, pas $len" }
    }

    /** Cases occupées, de l'arrière vers l'avant si la voiture roule vers la droite ou le bas, de l'avant vers l'arrière sinon. */
    val cells: List<Cell> =
        List(len) { i -> if (dir.horizontal) Cell(col + i, row) else Cell(col, row + i) }

    /** La case de devant : celle qui sort la première. */
    val head: Cell = if (dir.dc + dir.dr > 0) cells.last() else cells.first()

    /** Cases devant la voiture, de la plus proche au bord du plateau. Elles doivent être vides pour qu'elle sorte. */
    fun path(cols: Int, rows: Int): List<Cell> {
        val out = ArrayList<Cell>()
        var c = head.col + dir.dc
        var r = head.row + dir.dr
        while (c in 0 until cols && r in 0 until rows) {
            out += Cell(c, r)
            c += dir.dc
            r += dir.dr
        }
        return out
    }
}

/** Un parking à vider. [index] commence à 1. */
data class Level(
    val index: Int,
    val cols: Int,
    val rows: Int,
    val cars: List<Car>,
) {
    val world: Int get() = Worlds.of(index)

    init {
        val seen = HashSet<Cell>()
        for (car in cars) for (cell in car.cells) {
            require(cell.col in 0 until cols && cell.row in 0 until rows) { "la voiture ${car.id} sort de la grille en $cell" }
            require(seen.add(cell)) { "deux voitures sur la case $cell" }
        }
    }
}

/** Les 5 mondes de 20 niveaux. */
object Worlds {
    const val COUNT = 5
    const val LEVELS_PER_WORLD = 20
    const val LEVEL_COUNT = COUNT * LEVELS_PER_WORLD

    val names = listOf("Le petit parking", "Le centre-ville", "Le centre commercial", "L'aéroport", "Le grand garage")

    /** Monde (0 à 4) d'un niveau (1 à 100) ; au-delà, le dernier monde. */
    fun of(levelIndex: Int): Int = ((levelIndex - 1) / LEVELS_PER_WORLD).coerceIn(0, COUNT - 1)

    /** Premier niveau du monde. */
    fun firstLevel(world: Int): Int = world * LEVELS_PER_WORLD + 1
}
