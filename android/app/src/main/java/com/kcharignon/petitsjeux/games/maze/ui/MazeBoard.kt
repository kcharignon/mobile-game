package com.kcharignon.petitsjeux.games.maze.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.kcharignon.petitsjeux.games.maze.engine.Cell
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevel
import kotlin.math.hypot

/** Tout ce qu'il faut pour dessiner un niveau, calculé une fois : le décor de son monde, ses murs, ses petits objets du fond des impasses. */
internal class MazeScene(val level: MazeLevel) {
    val theme: MazeTheme = MazeTheme.of(level.world)
    val walls: WallShapes = wallShapes(level, theme)
    val motifs: List<MotifSpot> = motifSpots(level)
}

/** Ce que le plateau montre à un instant donné. */
internal class BoardState(
    val trail: List<Cell>,
    /** Trace parfaite (aucune impasse, niveau fini) : elle est dorée. */
    val gold: Boolean = false,
    val hint: List<Cell> = emptyList(),
    val hintAlpha: Float = 0f,
    /** Où est le poisson (centre, en pixels) ; `null` = à l'entrée. */
    val fishAt: Offset? = null,
    val fishAngle: Float = 0f,
    val fishScale: Float = 0.82f,
    val fishWiggle: Float = 0f,
    val holding: Boolean = false,
    /** Les bulles qui éclatent : leur case et leur avancement (0 à 1). */
    val bursts: List<Pair<Cell, Float>> = emptyList(),
    /** Le temps, en secondes (les bulles, l'anémone). */
    val time: Float = 0f,
    /** Montre la tête de la trace (la grosse bulle sous le doigt). */
    val showHead: Boolean = true,
    /** La bouée sous l'entrée (absente de la petite carte du menu, où elle serait coupée). */
    val showBuoy: Boolean = true,
)

/** Le labyrinthe dans l'ordre où il se superpose : lumière de la sortie, sol, objets d'impasse, bouée, trace, indice, murs, anémone, poisson, tête de la trace. */
internal fun DrawScope.drawMazeBoard(scene: MazeScene, layout: MazeLayout, st: BoardState) {
    val level = scene.level
    val theme = scene.theme
    val s = layout.cell
    drawExitLight(level, layout)
    drawFloor(level, layout, theme)
    for (m in scene.motifs) {
        val c = layout.center(m.cell)
        drawMotif(theme.motif, Offset(c.x + m.dx * s, c.y + m.dy * s), s * 0.82f, m.k, alpha = 0.7f)
    }
    if (st.showBuoy) drawBuoy(Offset(layout.center(level.start).x, layout.originY + level.rows * s + s * 0.6f), s)
    val points = st.trail.map { layout.center(it) }
    if (points.size > 1) drawRibbon(points, s, theme, st.gold, st.time)
    if (st.hint.isNotEmpty() && st.hintAlpha > 0f) drawHint(st.hint.map { layout.center(it) }, s, st.hintAlpha)
    drawWalls(scene.walls, layout, theme)
    drawAnemone(layout.center(level.exit), s * 1.05f, st.time)
    val start = layout.center(level.start)
    val fishAt = st.fishAt ?: Offset(start.x, start.y - s * 0.04f)
    drawFish(fishAt, s, st.fishAngle, st.fishScale, st.fishWiggle)
    if (st.showHead && (points.size > 1 || st.holding)) drawHead(points.last(), s, theme, st.holding)
    for ((cell, p) in st.bursts) drawBurst(layout.center(cell), s, p)
}

/** Position et direction (angle en degrés, 0 = vers le haut) à la fraction [f] (de 0 à 1) du chemin brisé [pts], à vitesse constante. */
internal fun positionAlong(pts: List<Offset>, f: Float): Pair<Offset, Float> {
    if (pts.size < 2) return (pts.firstOrNull() ?: Offset.Zero) to 0f
    val lengths = (1 until pts.size).map { hypot(pts[it].x - pts[it - 1].x, pts[it].y - pts[it - 1].y) }
    var left = lengths.sum() * f.coerceIn(0f, 1f)
    for (i in lengths.indices) {
        if (left <= lengths[i] || i == lengths.lastIndex) {
            val t = if (lengths[i] > 0f) (left / lengths[i]).coerceIn(0f, 1f) else 1f
            val a = pts[i]
            val b = pts[i + 1]
            return Offset(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t) to angleBetween(a, b)
        }
        left -= lengths[i]
    }
    return pts.last() to 0f
}
