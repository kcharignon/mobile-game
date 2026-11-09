package com.kcharignon.petitsjeux.games.parking.ui

import com.kcharignon.petitsjeux.games.parking.engine.Car
import com.kcharignon.petitsjeux.games.parking.engine.Cell
import com.kcharignon.petitsjeux.games.parking.engine.Dir
import com.kcharignon.petitsjeux.games.parking.engine.Move
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

/** Où est un véhicule qui roule : son centre (en cases, `col` / `row` décimaux, le centre de la case (0,0) est (0,5 ; 0,5)) et son sens de marche (degrés, 0 = vers le haut). */
data class Pose(val col: Float, val row: Float, val travelDegrees: Float)

/**
 * Le trajet **dessiné** d'un véhicule : une ligne brisée passant par le centre des cases, de sa queue à son nez, puis par les cases de son [route]
 * (avec les virages sur les taches d'huile), puis au-delà du bord du plateau. Le véhicule est un corps rigide dont le centre suit cette ligne et qui
 * tourne en douceur aux virages. La distance parcourue ([at]) se compte en cases le long du trajet (0 = à l'arrêt, à sa place).
 */
class MotionPath(private val car: Car, private val move: Move, route: List<Cell>, finalDirection: Dir) {
    private val points: List<Pair<Float, Float>>

    init {
        val cells = car.cellsTrailToLead(move) + route
        val pts = cells.map { (it.col + 0.5f) to (it.row + 0.5f) }.toMutableList()
        // au-delà du bord : des cases fictives dans le dernier sens, assez pour que même un bus sorte tout entier
        val last = cells.last()
        for (k in 1..car.len + 3) pts += (last.col + 0.5f + finalDirection.dc * k) to (last.row + 0.5f + finalDirection.dr * k)
        points = pts
    }

    private val segments: Int get() = points.size - 1

    /** Sens de marche (degrés) du segment [k] de la ligne brisée. */
    private fun segmentDegrees(k: Int): Float {
        val i = k.coerceIn(0, segments - 1)
        val dx = points[i + 1].first - points[i].first
        val dy = points[i + 1].second - points[i].second
        return when {
            dy < -0.5f -> 0f
            dx > 0.5f -> 90f
            dy > 0.5f -> 180f
            else -> 270f
        }
    }

    private fun lerpAngle(a: Float, b: Float, t: Float): Float {
        var d = (b - a) % 360f
        if (d > 180f) d -= 360f
        if (d < -180f) d += 360f
        return a + d * t
    }

    private fun smooth(x: Float): Float = x.coerceIn(0f, 1f).let { it * it * (3 - 2 * it) }

    /** Le point de la ligne brisée à l'abscisse [q] (en cases depuis la case de la queue), et le sens de marche, adouci aux virages. */
    fun pointAt(q: Float): Pose {
        val clamped = q.coerceIn(0f, segments - 1e-3f)
        val i = floor(clamped).toInt().coerceAtMost(segments - 1)
        val f = clamped - i
        val col = points[i].first + (points[i + 1].first - points[i].first) * f
        val row = points[i].second + (points[i + 1].second - points[i].second) * f
        // virage : on tourne autour du sommet le plus proche, sur 0,8 case
        val vertex = clamped.roundToInt()
        val x = clamped - vertex
        val before = segmentDegrees(vertex - 1)
        val after = segmentDegrees(vertex)
        return Pose(col, row, lerpAngle(before, after, smooth((x + 0.4f) / 0.8f)))
    }

    /** Le centre du véhicule après avoir parcouru [traveled] cases : le centre de ses [Car.len] cases est à (len - 1) / 2 du début de la ligne brisée. */
    fun at(traveled: Float): Pose = pointAt((car.len - 1) / 2f + traveled)

    /** Le bout du nez après [traveled] cases, avancé de [ahead] case(s) : là où la bulle « Pouet ! » se pose. */
    fun noseAt(traveled: Float, ahead: Float = 0.4f): Pose = pointAt(car.len - 1 + traveled + ahead)

    /** Sens où le véhicule regarde (degrés, 0 = nez en haut) quand il roule dans le sens [travelDegrees] : à l'envers, il marche arrière. */
    fun headingDegrees(travelDegrees: Float): Float = if (move == Move.FORWARD) travelDegrees else (travelDegrees + 180f) % 360f

    companion object {
        fun degreesOf(d: Dir): Float = d.degrees

        /** Vrai si deux angles désignent le même sens (à 1° près). */
        fun sameAngle(a: Float, b: Float): Boolean = abs(((a - b) % 360f + 540f) % 360f - 180f) < 1f
    }
}
