package com.kcharignon.petitsjeux.games.parking.ui

import com.kcharignon.petitsjeux.games.parking.engine.Car
import com.kcharignon.petitsjeux.games.parking.engine.Dir
import com.kcharignon.petitsjeux.games.parking.engine.Entry
import com.kcharignon.petitsjeux.games.parking.engine.Move
import com.kcharignon.petitsjeux.games.parking.engine.Route
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Où est un véhicule qui roule : son centre (en cases, `col` / `row` décimaux, le centre de la case (0,0) est (0,5 ; 0,5)), son sens de marche (degrés, 0 = vers le haut)
 * et son opacité (un véhicule qui passe par un téléporteur disparaît d'un côté et réapparaît de l'autre).
 */
data class Pose(val col: Float, val row: Float, val travelDegrees: Float, val alpha: Float = 1f)

/**
 * Le trajet **dessiné** d'un véhicule : une ligne brisée passant par le centre des cases, de sa queue à son nez, puis par les cases de son [Route]
 * (virages de l'huile, glissements des tapis, sauts des téléporteurs), puis au-delà du bord du plateau. Le véhicule est un corps rigide dont le centre suit cette
 * ligne et qui tourne en douceur aux virages. La distance parcourue ([at]) se compte en cases le long du trajet (0 = à l'arrêt, à sa place).
 *
 * - **huile** : le cap tourne avec le virage ;
 * - **tapis roulant** : le véhicule glisse d'une case de côté **sans tourner** ;
 * - **téléporteur** : le véhicule s'efface d'un côté et réapparaît de l'autre.
 */
class MotionPath(private val car: Car, private val move: Move, route: Route) {
    private val points: List<Pair<Float, Float>>

    /** Le sens de marche (pour le cap) et la façon d'arriver de chaque segment de la ligne brisée. */
    private val headings: List<Dir>
    private val entries: List<Entry>

    init {
        val body = car.cellsTrailToLead(move)
        val start = car.directionOf(move)
        val cells = body + route.cells
        val pts = cells.map { (it.col + 0.5f) to (it.row + 0.5f) }.toMutableList()
        val hs = ArrayList<Dir>()
        val es = ArrayList<Entry>()
        repeat(body.size - 1) {
            hs += start
            es += Entry.DRIVE
        }
        for (j in route.cells.indices) {
            hs += route.headings[j]
            es += route.entries[j]
        }
        // au-delà du bord : des cases fictives dans le dernier sens, assez pour que même un bus sorte tout entier
        val last = cells.last()
        for (k in 1..car.len + 3) {
            pts += (last.col + 0.5f + route.finalDirection.dc * k) to (last.row + 0.5f + route.finalDirection.dr * k)
            hs += route.finalHeading
            es += Entry.DRIVE
        }
        points = pts
        headings = hs
        entries = es
    }

    private val segments: Int get() = points.size - 1

    private fun headingOf(k: Int): Dir = headings[k.coerceIn(0, segments - 1)]

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
        if (entries[i] == Entry.JUMP) {
            // téléporteur : il s'efface là où il est entré (première moitié), puis réapparaît à la sortie (seconde moitié)
            val at = if (f < 0.5f) points[i] else points[i + 1]
            val alpha = abs(1f - 2f * f)
            return Pose(at.first, at.second, headingOf(i).degrees, alpha)
        }
        val col = points[i].first + (points[i + 1].first - points[i].first) * f
        val row = points[i].second + (points[i + 1].second - points[i].second) * f
        // virage : on tourne autour du sommet le plus proche, sur 0,8 case
        val vertex = clamped.roundToInt()
        val x = clamped - vertex
        val before = headingOf(vertex - 1).degrees
        val after = headingOf(vertex).degrees
        return Pose(col, row, lerpAngle(before, after, smooth((x + 0.4f) / 0.8f)))
    }

    /** Le centre du véhicule après avoir parcouru [traveled] cases : le centre de ses [Car.len] cases est à (len - 1) / 2 du début de la ligne brisée. */
    fun at(traveled: Float): Pose = pointAt((car.len - 1) / 2f + traveled)

    /** Le bout du nez après [traveled] cases, avancé de [ahead] case(s) : là où la bulle « Pouet ! » se pose. */
    fun noseAt(traveled: Float, ahead: Float = 0.4f): Pose = pointAt(car.len - 1 + traveled + ahead)

    /** Sens où le véhicule regarde (degrés, 0 = nez en haut) quand il roule dans le sens [travelDegrees] : à l'envers, il marche arrière. */
    fun headingDegrees(travelDegrees: Float): Float = if (move == Move.FORWARD) travelDegrees else (travelDegrees + 180f) % 360f
}
