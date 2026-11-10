package com.kcharignon.petitsjeux.games.maze.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.kcharignon.petitsjeux.games.maze.engine.Cell
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevel
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/** Le petit objet de décor posé au fond des impasses d'un monde (jamais sur la solution : il la révélerait). */
enum class MazeMotif { SHELL, STARFISH, SEAWEED, PLANK, CRYSTAL, ICE, ROCK, ANEMONE, JELLY, PEARL }

/**
 * Le décor d'un monde : les couleurs du sol, des coraux (murs), du fond marin, de la trace, et son petit objet de décor. Les dix palettes viennent des maquettes
 * (`docs/design/11-recif-dix-mondes.png`, dessinées par `tools/design/recif.js`) ; le contour des murs est **obligatoire** (la lisibilité d'un labyrinthe ne tient pas à la
 * couleur seule) : un test vérifie qu'il contraste au moins 3:1 avec le sol dans chaque monde.
 */
class MazeTheme(
    val name: String,
    val floor: Color,
    val sand: Color,
    /** Les trois couleurs de corail, qui se partagent les murs par plaques. */
    val walls: List<Color>,
    /** Sol sombre (les abysses) : le contour des murs est un liseré clair plutôt que foncé. */
    val darkFloor: Boolean,
    val frameTop: Color,
    val frameBottom: Color,
    /** Le contour et le cœur du ruban d'eau de la trace. */
    val trailEdge: Color,
    val trailCore: Color,
    val motif: MazeMotif,
) {
    /** Le contour d'un mur de couleur [wall] : la couleur mélangée à 50 % d'encre (sol clair), ou éclaircie de 55 % (sol sombre). */
    fun wallEdge(wall: Color): Color = if (darkFloor) lerp(wall, Color.White, 0.55f) else lerp(wall, Ink, 0.5f)

    companion object {
        val all: List<MazeTheme> = listOf(
            MazeTheme("Le lagon", c(0xC8F3F1), c(0xF4E3B1), listOf(c(0xFF8FA3), c(0xFFB27A), c(0xB58CFF)), false, c(0x35B3CB), c(0x176F8C), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.SHELL),
            MazeTheme("Le récif de corail", c(0xBFEAF2), c(0xF4E3B1), listOf(c(0xFF6F91), c(0xFF9671), c(0xD65DB1)), false, c(0x2E8FC0), c(0x17537F), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.STARFISH),
            MazeTheme("La forêt d'algues", c(0xD2F1D9), c(0xEBE3B4), listOf(c(0x2F9E5B), c(0x52B96E), c(0x1F8A55)), false, c(0x27A08A), c(0x0F5F55), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.SEAWEED),
            MazeTheme("L'épave", c(0xEBDCBF), c(0xD9C597), listOf(c(0xB07A4B), c(0x8C5E3A), c(0xC99A67)), false, c(0x3C85A3), c(0x1B4B63), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.PLANK),
            MazeTheme("La grotte lumineuse", c(0xDDD6F7), c(0xC9BDF0), listOf(c(0x8E6CF2), c(0x4FB6E8), c(0xC77DFF)), false, c(0x5A47B0), c(0x241A55), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.CRYSTAL),
            MazeTheme("La banquise", c(0xE8F7FF), c(0xCBE9F8), listOf(c(0x9BD5F5), c(0xBEE6FA), c(0x7FC0E8)), false, c(0x4B8FC4), c(0x285C8F), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.ICE),
            MazeTheme("Le volcan", c(0xFFE6D5), c(0xF3C9A8), listOf(c(0xE8503A), c(0xFF7A3D), c(0x5A4552)), false, c(0x7A3447), c(0x34182A), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.ROCK),
            MazeTheme("Le jardin d'anémones", c(0xFFE4F1), c(0xF6CDE0), listOf(c(0xFF7BAC), c(0xC77DFF), c(0xFFB347)), false, c(0x3B8FBF), c(0x1A5A8A), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.ANEMONE),
            MazeTheme("Les abysses", c(0x1C2B5E), c(0x2E4688), listOf(c(0x4FD1E6), c(0x9B8CFF), c(0xFF8AD8)), true, c(0x0F1A3D), c(0x070C22), c(0xFFF2A8), c(0xFFFFFF), MazeMotif.JELLY),
            MazeTheme("Le palais du roi", c(0xFFF3D6), c(0xF0DDAA), listOf(c(0xF2C14E), c(0xE8A33D), c(0xF7D98B)), false, c(0x7B5BC4), c(0x3F2F80), c(0x1F6FB5), c(0x5DB8F2), MazeMotif.PEARL),
        )

        /** Le décor du monde [world] (de 0 à 9). */
        fun of(world: Int): MazeTheme = all[world.coerceIn(0, all.lastIndex)]

        private fun c(rgb: Int) = Color(0xFF000000.toInt() or rgb)
    }
}

internal val Ink = Color(0xFF1B1D2E)

/** Luminance relative d'une couleur (WCAG). */
fun luminance(c: Color): Double {
    fun lin(v: Float): Double = if (v <= 0.03928f) v / 12.92 else ((v + 0.055) / 1.055).pow(2.4)
    return 0.2126 * lin(c.red) + 0.7152 * lin(c.green) + 0.0722 * lin(c.blue)
}

/** Rapport de contraste (WCAG) entre deux couleurs : de 1 (identiques) à 21. */
fun contrast(a: Color, b: Color): Double {
    val la = luminance(a)
    val lb = luminance(b)
    return (max(la, lb) + 0.05) / (min(la, lb) + 0.05)
}

/**
 * Où est dessiné le labyrinthe dans une zone de [width] x [height] pixels : des cases carrées de [cell] pixels, centrées, avec de la place sous le plateau pour la bouée
 * du départ et au-dessus pour la lumière de la sortie. Sert au dessin ET à convertir le doigt (pixels) en cases.
 */
class MazeLayout(val level: MazeLevel, val width: Float, val height: Float, margin: Float = 0f) {
    val cell: Float = min((width - 2 * margin) / level.cols, height / (level.rows + 1.1f)).coerceAtLeast(1f)
    val originX: Float = (width - level.cols * cell) / 2f
    val originY: Float = (height - (level.rows + 1.1f) * cell) / 2f + 0.3f * cell

    fun center(c: Cell): Offset = Offset(originX + (c.x + 0.5f) * cell, originY + (c.y + 0.5f) * cell)

    /** Un point en pixels vers des cases : (colonne, ligne) décimales, la case (x, y) occupant de x à x + 1. */
    fun toCells(p: Offset): Pair<Double, Double> = ((p.x - originX) / cell).toDouble() to ((p.y - originY) / cell).toDouble()

    fun cellAt(p: Offset): Cell = toCells(p).let { Cell(floor(it.first).toInt(), floor(it.second).toInt()) }
}

/** Hasard déterministe sans état (le même à chaque image : le décor ne scintille pas). Même formule que `hash` de `tools/design/recif.js`. */
internal fun hash01(a: Int, b: Int): Double {
    var x = a * 374761393 + b * 668265263
    x = (x xor (x ushr 13)) * 1274126177
    return ((x xor (x ushr 16)).toLong() and 0xFFFFFFFFL).toDouble() / 4294967296.0
}
