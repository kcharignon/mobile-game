package com.kcharignon.petitsjeux.games.find.engine

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/** La place d'un objet : centre ([cx], [cy]) en pixels dans la zone de jeu, côté [size] de son carré, inclinaison [angle] en degrés. */
data class FindSpot(val id: String, val cx: Float, val cy: Float, val size: Float, val angle: Float) {
    /** Le rayon dans lequel un toucher compte pour cet objet. Deux objets ne se touchent jamais, donc deux zones ne se chevauchent jamais. */
    val hitRadius: Float get() = size * FindLayout.HIT_FACTOR

    /** Le plus grand écart au centre, sur chaque axe, du carré incliné : tout l'objet tient dans ce cadre. */
    val reach: Float get() = size / 2 * (abs(cos(Math.toRadians(angle.toDouble()))) + abs(sin(Math.toRadians(angle.toDouble())))).toFloat()
}

/**
 * La mise en page d'un niveau : **une grille décalée**. La zone de jeu est découpée en cellules presque carrées (au moins une par objet), chaque objet
 * va dans une cellule tirée au sort, un peu décalé et incliné dans sa cellule. Chacun a sa cellule et tient dedans, même incliné : **deux objets ne
 * se touchent jamais et aucun ne sort de la zone**, quelle que soit sa taille. La même graine donne la même disposition.
 */
object FindLayout {
    /** Inclinaison maximale d'un objet, en degrés. */
    const val TILT_DEGREES = 9f

    /** Un objet de côté `s`, incliné, tient dans un carré de côté `s * OVERHANG` (marge comprise). */
    const val OVERHANG = 1.2f

    const val HIT_FACTOR = 0.6f

    /** Le nombre de colonnes et de lignes qui donnent les plus grands objets pour [count] objets dans une zone de [width] x [height]. */
    fun grid(count: Int, width: Float, height: Float): Pair<Int, Int> {
        require(count >= 1 && width > 0f && height > 0f)
        var best = 1 to count
        var bestSide = -1f
        for (cols in 1..count) {
            val rows = ceil(count / cols.toFloat()).toInt()
            val side = minOf(width / cols, height / rows)
            // la plus grande taille ; à égalité, la grille qui a le moins de cellules vides
            if (side > bestSide + 1e-3f || (abs(side - bestSide) <= 1e-3f && cols * rows < best.first * best.second)) {
                best = cols to rows
                bestSide = side
            }
        }
        return best
    }

    /**
     * Place les objets [ids] dans une zone de [width] x [height] pixels. Le résultat suit l'ordre de [ids] ; [maxSize] plafonne la taille d'un objet
     * (pour ne pas avoir de géants quand il y en a peu).
     */
    fun arrange(ids: List<String>, width: Float, height: Float, seed: Int, maxSize: Float = Float.MAX_VALUE): List<FindSpot> {
        if (ids.isEmpty()) return emptyList()
        val (cols, rows) = grid(ids.size, width, height)
        val cellW = width / cols
        val cellH = height / rows
        val size = minOf(minOf(cellW, cellH) / OVERHANG, maxSize)
        val slackX = (cellW - size * OVERHANG) / 2
        val slackY = (cellH - size * OVERHANG) / 2
        val rnd = Random(seed)
        // les cellules, mélangées : les `ids.size` premières servent ; chaque objet y est décalé et incliné
        val cells = (0 until cols * rows).shuffled(rnd).take(ids.size)
        return ids.mapIndexed { i, id ->
            val cell = cells[i]
            val cx = (cell % cols + 0.5f) * cellW + (rnd.nextFloat() * 2 - 1) * slackX
            val cy = (cell / cols + 0.5f) * cellH + (rnd.nextFloat() * 2 - 1) * slackY
            FindSpot(id, cx, cy, size, (rnd.nextFloat() * 2 - 1) * TILT_DEGREES)
        }
    }
}
