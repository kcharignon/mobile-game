package com.kcharignon.petitsjeux.games.find.engine

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.random.Random

/**
 * La place d'un objet : centre ([cx], [cy]) en pixels dans la zone de jeu, côté [size] de son carré, inclinaison
 * [angle] en degrés, et [hitRadius], la demi-largeur du carré (centré sur l'objet) dans lequel un toucher compte pour cet objet.
 */
data class FindSpot(val id: String, val cx: Float, val cy: Float, val size: Float, val angle: Float, val hitRadius: Float)

/**
 * La mise en page d'une manche : **une grille décalée**. La zone de jeu est découpée en cellules presque carrées (au moins une par objet), chaque objet
 * va dans une cellule tirée au sort, un peu décalé dans sa cellule. Chacun a sa cellule : sa zone de toucher (le carré de côté `2 * hitRadius` centré sur l'objet, soit [OVERHANG] fois
 * la taille maximale) tient dedans, donc **deux zones de toucher ne se chevauchent jamais et aucune ne sort de la zone de jeu**, quelles que soient la
 * taille (variable ou non) et l'inclinaison des objets. La même graine donne la même disposition.
 *
 * - [arrange] avec `rotate` : chaque objet est incliné d'un angle quelconque (0 à 360°) ; sinon il reste droit ;
 * - avec `vary` : chaque objet a une taille entre [MIN_SCALE] et 1 fois la taille maximale ; sinon tous ont la taille maximale.
 */
object FindLayout {
    /** Un objet de taille maximale `s` (dessiné dans un carré de côté `s`, incliné ou non) occupe au plus un disque de diamètre `s * OVERHANG` : sa cellule en tient un. */
    const val OVERHANG = 1.2f

    /** La zone de toucher d'un objet est un carré de demi-côté `HIT_FACTOR * s` (la moitié de `OVERHANG * s`). */
    const val HIT_FACTOR = OVERHANG / 2

    /** La plus petite taille d'un objet quand les tailles varient, en part de la taille maximale. */
    const val MIN_SCALE = 0.68f

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

    /** La taille maximale d'un objet pour [count] objets dans une zone de [width] x [height] (plafonnée par [maxSize]). */
    fun maxObjectSize(count: Int, width: Float, height: Float, maxSize: Float = Float.MAX_VALUE): Float {
        val (cols, rows) = grid(count, width, height)
        return minOf(minOf(width / cols, height / rows) / OVERHANG, maxSize)
    }

    /**
     * Place les objets [ids] dans une zone de [width] x [height] pixels. Le résultat suit l'ordre de [ids] ; [maxSize] plafonne la taille d'un objet
     * (pour ne pas avoir de géants quand il y en a peu).
     */
    fun arrange(
        ids: List<String>, width: Float, height: Float, seed: Int,
        maxSize: Float = Float.MAX_VALUE, rotate: Boolean = false, vary: Boolean = false,
    ): List<FindSpot> {
        if (ids.isEmpty()) return emptyList()
        val (cols, rows) = grid(ids.size, width, height)
        val cellW = width / cols
        val cellH = height / rows
        val sizeMax = minOf(minOf(cellW, cellH) / OVERHANG, maxSize)
        val hit = sizeMax * HIT_FACTOR
        // le centre d'un objet reste dans la partie centrale de sa cellule : son carré de toucher (demi-côté `hit`) ne sort jamais de la cellule
        val slackX = (cellW / 2 - hit).coerceAtLeast(0f)
        val slackY = (cellH / 2 - hit).coerceAtLeast(0f)
        val rnd = Random(seed)
        // les cellules, mélangées : les `ids.size` premières servent
        val cells = (0 until cols * rows).shuffled(rnd).take(ids.size)
        return ids.mapIndexed { i, id ->
            val cell = cells[i]
            val cx = (cell % cols + 0.5f) * cellW + (rnd.nextFloat() * 2 - 1) * slackX
            val cy = (cell / cols + 0.5f) * cellH + (rnd.nextFloat() * 2 - 1) * slackY
            val size = if (vary) sizeMax * (MIN_SCALE + (1f - MIN_SCALE) * rnd.nextFloat()) else sizeMax
            val angle = if (rotate) rnd.nextFloat() * 360f else 0f
            FindSpot(id, cx, cy, size, angle, hit)
        }
    }
}
