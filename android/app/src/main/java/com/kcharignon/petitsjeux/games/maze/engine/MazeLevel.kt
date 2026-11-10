package com.kcharignon.petitsjeux.games.maze.engine

import org.json.JSONObject

/** Une case du labyrinthe : colonne [x] (de gauche à droite) et ligne [y] (de haut en bas). */
data class Cell(val x: Int, val y: Int) {
    fun manhattan(other: Cell): Int = kotlin.math.abs(x - other.x) + kotlin.math.abs(y - other.y)
}

/**
 * Un labyrinthe : une grille de [cols] x [rows] cases, des murs entre elles, un [start] (au bord du bas, c'est l'entrée du poisson) et une [exit] (au bord du haut,
 * ou au centre dans quelques niveaux). Les niveaux viennent du fichier `res/raw/maze_levels.json`, fabriqué à l'avance par `tools/mazes/make-mazes.js`.
 *
 * Les murs sont rangés comme dans la grille en art ASCII du fichier : [vertical] `[y][x]` est le mur à gauche de la case (x, y) (`x` de 0 à [cols] : le dernier est
 * le bord droit) ; [horizontal] `[y][x]` est le mur au-dessus de la case (x, y) (`y` de 0 à [rows] : le dernier est le bord du bas).
 */
class MazeLevel internal constructor(
    val number: Int,
    /** Le monde, de 0 à 9. */
    val world: Int,
    val cols: Int,
    val rows: Int,
    val start: Cell,
    val exit: Cell,
    private val vertical: Array<BooleanArray>,
    private val horizontal: Array<BooleanArray>,
    /** La grille telle qu'elle est dans le fichier (pour l'empreinte et les tests). */
    val grid: List<String>,
) {
    /** Les cases voisines de [c] auxquelles on accède sans franchir de mur (jamais hors de la grille), dans l'ordre haut, droite, bas, gauche. */
    fun neighbors(c: Cell): List<Cell> {
        val out = ArrayList<Cell>(4)
        if (c.y > 0 && !horizontal[c.y][c.x]) out += Cell(c.x, c.y - 1)
        if (c.x + 1 < cols && !vertical[c.y][c.x + 1]) out += Cell(c.x + 1, c.y)
        if (c.y + 1 < rows && !horizontal[c.y + 1][c.x]) out += Cell(c.x, c.y + 1)
        if (c.x > 0 && !vertical[c.y][c.x]) out += Cell(c.x - 1, c.y)
        return out
    }

    fun inside(c: Cell): Boolean = c.x in 0 until cols && c.y in 0 until rows

    /** Deux cases voisines sans mur entre elles. */
    fun isOpen(a: Cell, b: Cell): Boolean = inside(a) && inside(b) && a.manhattan(b) == 1 && b in neighbors(a)

    /** Y a-t-il un mur franchissable ou non entre deux cases voisines ? (faux pour deux cases qui ne se touchent pas) */
    fun wallBetween(a: Cell, b: Cell): Boolean = inside(a) && inside(b) && a.manhattan(b) == 1 && b !in neighbors(a)

    /** Le mur au-dessus de la case (x, y) existe-t-il ? (y = lignes : le bord du bas.) Sert au dessin. */
    fun wallAbove(x: Int, y: Int): Boolean = horizontal[y][x]

    /** Le mur à gauche de la case (x, y) existe-t-il ? (x = colonnes : le bord droit.) Sert au dessin. */
    fun wallLeftOf(x: Int, y: Int): Boolean = vertical[y][x]

    /** La sortie est-elle une ouverture dans le bord du haut (sinon elle est au centre) ? */
    val exitOnBorder: Boolean get() = exit.y == 0 && !horizontal[0][exit.x]

    /** Nombre de passages entre cases : `cols * rows - 1` pour un labyrinthe parfait (sans boucle), plus pour un labyrinthe à boucles. */
    val passages: Int by lazy {
        var n = 0
        for (y in 0 until rows) for (x in 0 until cols) {
            if (x + 1 < cols && !vertical[y][x + 1]) n++
            if (y + 1 < rows && !horizontal[y + 1][x]) n++
        }
        n
    }

    /** Distance (en cases) de chaque case à la sortie, par les couloirs : `y * cols + x`. Toutes les cases sont reliées, donc jamais -1. */
    private val toExit: IntArray by lazy { distancesFrom(exit) }

    fun distanceToExit(c: Cell): Int = toExit[c.y * cols + c.x]

    /** Un plus court chemin du départ à la sortie, cases de départ et de sortie comprises (le premier dans l'ordre haut, droite, bas, gauche : toujours le même). */
    val solution: List<Cell> by lazy {
        val path = arrayListOf(start)
        while (path.last() != exit) {
            val here = path.last()
            path += neighbors(here).first { distanceToExit(it) == distanceToExit(here) - 1 }
        }
        path
    }

    /** Embranchements sur la solution : les cases de la solution (sans départ ni sortie) où l'on a au moins trois couloirs, donc de vraies décisions. */
    val decisions: Int by lazy { solution.drop(1).dropLast(1).count { neighbors(it).size >= 3 } }

    /** Les cases qui n'ont qu'une seule ouverture (hors départ et sortie) : les culs-de-sac. */
    val deadEnds: List<Cell> by lazy {
        val out = ArrayList<Cell>()
        for (y in 0 until rows) for (x in 0 until cols) {
            val c = Cell(x, y)
            if (c != start && c != exit && neighbors(c).size == 1) out += c
        }
        out
    }

    internal fun distancesFrom(from: Cell): IntArray {
        val dist = IntArray(cols * rows) { -1 }
        val queue = ArrayDeque<Cell>()
        dist[from.y * cols + from.x] = 0
        queue.add(from)
        while (queue.isNotEmpty()) {
            val c = queue.removeFirst()
            for (n in neighbors(c)) {
                val i = n.y * cols + n.x
                if (dist[i] < 0) {
                    dist[i] = dist[c.y * cols + c.x] + 1
                    queue.add(n)
                }
            }
        }
        return dist
    }
}

/** Les mondes : 10 mondes de 10 niveaux. Le décor change à chaque monde (voir `MazeTheme`) ; le fichier de niveaux porte la taille et la difficulté. */
object MazeWorlds {
    const val COUNT = 10
    const val LEVELS_PER_WORLD = 10
    const val LEVEL_COUNT = COUNT * LEVELS_PER_WORLD

    val names = listOf(
        "Le lagon", "Le récif de corail", "La forêt d'algues", "L'épave", "La grotte lumineuse",
        "La banquise", "Le volcan", "Le jardin d'anémones", "Les abysses", "Le palais du roi",
    )

    /** Ce qui est nouveau au premier niveau de chaque monde (affiché dans l'introduction du niveau). */
    val news = listOf(
        "Pose ton doigt sur le poisson et suis le couloir !",
        "Le labyrinthe grandit : regarde bien avant de tracer.",
        "Des couloirs qui se séparent : choisis le bon !",
        "Plus de culs-de-sac : si tu te trompes, recule !",
        "Le plus grand labyrinthe jusqu'ici !",
        "Des boucles : il y a plusieurs chemins !",
        "Encore plus grand, encore plus de chemins.",
        "Parfois la maison est au milieu du labyrinthe !",
        "Presque les plus grands : prends ton temps.",
        "Le dernier monde : tout est mélangé !",
    )

    /** Le monde (de 0 à 9) du niveau [level] (de 1 à 100). */
    fun of(level: Int): Int = (level - 1) / LEVELS_PER_WORLD

    fun firstLevel(world: Int): Int = world * LEVELS_PER_WORLD + 1

    /** Le rang du niveau dans son monde (0 pour le premier). */
    fun indexIn(level: Int): Int = (level - 1) % LEVELS_PER_WORLD
}

/** Lecture du fichier de niveaux. Un niveau incohérent est **rejeté** (le test des données le verrait aussi). */
object MazeLevels {
    /**
     * Lit le texte du fichier `maze_levels.json`. Chaque grille doit être exactement du format décrit dans `tools/mazes/make-mazes.js` : `+` aux coins,
     * `--` ou deux espaces pour un mur horizontal, `|` ou un espace pour un mur vertical, deux espaces dans chaque case.
     */
    fun parse(text: String): List<MazeLevel> {
        val root = JSONObject(text)
        require(root.optInt("format") == 1) { "format de niveaux inconnu : ${root.opt("format")}" }
        val array = root.getJSONArray("levels")
        val out = ArrayList<MazeLevel>(array.length())
        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            val n = o.getInt("n")
            require(n == i + 1) { "les niveaux se suivent : attendu ${i + 1}, trouvé $n" }
            val g = o.getJSONArray("grid")
            val grid = List(g.length()) { g.getString(it) }
            val start = o.getJSONArray("start").let { Cell(it.getInt(0), it.getInt(1)) }
            val exit = o.getJSONArray("exit").let { Cell(it.getInt(0), it.getInt(1)) }
            val level = level(n, o.getInt("world") - 1, o.getInt("cols"), o.getInt("rows"), start, exit, grid)
            require(level.world == MazeWorlds.of(n)) { "niveau $n : le monde ${level.world + 1} ne correspond pas à son numéro" }
            out += level
        }
        return out
    }

    /** Construit un niveau depuis sa grille en art ASCII ; échoue (IllegalArgumentException) si elle est mal formée ou si une case est isolée. */
    fun level(number: Int, world: Int, cols: Int, rows: Int, start: Cell, exit: Cell, grid: List<String>): MazeLevel {
        fun bad(why: String): Nothing = throw IllegalArgumentException("niveau $number : $why")
        if (cols < 2 || rows < 2) bad("grille trop petite ($cols x $rows)")
        if (grid.size != 2 * rows + 1) bad("${grid.size} lignes, attendu ${2 * rows + 1}")
        for ((i, line) in grid.withIndex()) if (line.length != 3 * cols + 1) bad("la ligne $i fait ${line.length} caractères, attendu ${3 * cols + 1}")
        val horizontal = Array(rows + 1) { BooleanArray(cols) }
        val vertical = Array(rows) { BooleanArray(cols + 1) }
        for (r in 0..rows) {
            val line = grid[2 * r]
            for (x in 0..cols) if (line[3 * x] != '+') bad("coin attendu en ligne ${2 * r}, colonne ${3 * x}")
            for (x in 0 until cols) {
                horizontal[r][x] = when (line.substring(3 * x + 1, 3 * x + 3)) {
                    "--" -> true
                    "  " -> false
                    else -> bad("mur horizontal mal formé en ligne ${2 * r}, case $x")
                }
            }
        }
        for (r in 0 until rows) {
            val line = grid[2 * r + 1]
            for (x in 0..cols) {
                vertical[r][x] = when (line[3 * x]) {
                    '|' -> true
                    ' ' -> false
                    else -> bad("mur vertical mal formé en ligne ${2 * r + 1}, colonne ${3 * x}")
                }
                if (x < cols && line.substring(3 * x + 1, 3 * x + 3) != "  ") bad("une case n'est pas vide en ligne ${2 * r + 1}, case $x")
            }
            if (!vertical[r][0] || !vertical[r][cols]) {
                // les bords gauche et droit sont toujours des murs : seules l'entrée et la sortie percent le bord, en bas et en haut
                bad("le bord gauche ou droit est ouvert en ligne ${2 * r + 1}")
            }
        }
        val level = MazeLevel(number, world, cols, rows, start, exit, vertical, horizontal, grid)
        if (!level.inside(start) || !level.inside(exit)) bad("le départ $start ou la sortie $exit est hors de la grille")
        if (start == exit) bad("le départ et la sortie sont la même case")
        if (start.y != rows - 1) bad("le départ n'est pas sur le bord du bas")
        if (horizontal[rows][start.x]) bad("l'entrée du départ n'est pas ouverte dans le bord du bas")
        for (x in 0 until cols) if (x != start.x && !horizontal[rows][x]) bad("le bord du bas est ouvert ailleurs qu'au départ (case $x)")
        val opened = (0 until cols).filter { !horizontal[0][it] }
        if (opened.isNotEmpty() && opened != listOf(exit.x)) bad("le bord du haut est ouvert ailleurs qu'à la sortie ($opened)")
        if (opened.isNotEmpty() && exit.y != 0) bad("la sortie ne peut être ouverte sur le bord du haut que si elle est sur la première ligne")
        // toutes les cases sont reliées : sinon une partie du labyrinthe est un îlot inutile
        val reached = level.distancesFrom(start).count { it >= 0 }
        if (reached != cols * rows) bad("$reached cases reliées au départ sur ${cols * rows}")
        return level
    }
}
