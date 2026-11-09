package com.kcharignon.petitsjeux.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

/** On ne se fie pas à la seule parole du compositeur : on vérifie la mélodie. */
class MelodyTest {
    private val tune = Melody.tune
    private val semitones = tune.map { Melody.semitones(it) }

    @Test
    fun itIsAPentatonicTuneInCMajorWithinTheComfortableRange() {
        assertEquals(32, tune.size)
        val pentatonic = setOf("do", "re", "mi", "sol", "la")
        for (n in tune) {
            assertTrue("$n n'est pas pentatonique", pentatonic.any { n.startsWith(it) && n.length == it.length + 1 })
            assertTrue("$n : trop grave pour un haut-parleur de téléphone", Melody.semitones(n) >= Melody.semitones("do5"))
            assertTrue("$n : trop aigu", Melody.semitones(n) <= Melody.semitones("sol6"))
        }
        // les notes des « ding » des étoiles (do6, mi6, sol6) en font partie : tout s'accorde
        assertTrue(listOf("do6", "mi6", "sol6").all { it in tune })
    }

    @Test
    fun itNeverRepeatsANoteTwiceInARowAndTheLoopClosesWell() {
        for (i in tune.indices) {
            val a = tune[i]
            val b = tune[(i + 1) % tune.size]
            assertTrue("deux fois $a de suite (notes ${i + 1} et ${(i + 1) % tune.size + 1})", a != b)
            // aucun saut de plus d'une quinte, la boucle comprise
            assertTrue("saut trop grand entre $a et $b", abs(Melody.semitones(a) - Melody.semitones(b)) <= 7)
        }
    }

    @Test
    fun itIsATuneNotAScale() {
        val steps = semitones.zipWithNext { a, b -> Integer.signum(b - a) }
        // jamais plus de 4 notes d'affilée dans le même sens
        var run = 1
        var longest = 1
        for (k in 1 until steps.size) {
            run = if (steps[k] == steps[k - 1]) run + 1 else 1
            longest = maxOf(longest, run)
        }
        assertTrue("plus longue montée ou descente : $longest intervalles", longest <= 4)
        // la mélodie change souvent de sens : elle chante
        val turns = (1 until steps.size).count { steps[it] != steps[it - 1] }
        assertTrue("changements de sens : $turns", turns >= 14)
        // et jamais une gamme de 5 notes ou plus dans le même sens, où qu'on s'arrête (boucle comprise) ; 4 notes qui descendent (la sol mi ré) font un geste de cadence
        for (i in tune.indices) {
            val group = (0..4).map { semitones[(i + it) % tune.size] }
            val diffs = group.zipWithNext { x, y -> y - x }
            assertFalse("gamme brute de 5 notes à partir de la note ${i + 1}", diffs.all { it > 0 } || diffs.all { it < 0 })
        }
    }

    @Test
    fun itHasAClearShape() {
        // le motif sol la sol mi ouvre les phrases 1, 3 et 4 ; le point culminant (sol6) est au milieu, une seule fois ; la fin se pose sur do5
        val motif = listOf("sol5", "la5", "sol5", "mi5")
        for (phrase in listOf(0, 2, 3)) assertEquals(motif, tune.subList(phrase * 8, phrase * 8 + 4))
        assertEquals(1, tune.count { it == "sol6" })
        assertEquals(13, tune.indexOf("sol6"))
        assertEquals("do5", tune.last())
        assertEquals("sol5", tune.first())
        // même une partie courte (15 touchers) entend l'élan et le sommet
        assertTrue("sol6" in tune.take(15))
    }

    @Test
    fun theTuneFollowsTheMushroomsTouchedAndRestartsAtTheEnd() {
        assertEquals("sol5", Melody.noteAt(0))
        assertEquals("la5", Melody.noteAt(1))
        assertEquals("sol5", Melody.noteAt(32))
        assertEquals("do5", Melody.noteAt(31))
        assertEquals("do5", Melody.noteAt(63))
        assertEquals(8, Melody.distinct.size)
    }

    @Test
    fun noteFrequenciesAreEqualTempered() {
        assertEquals(440.0, Melody.hz("la4"), 0.001)
        assertEquals(523.25, Melody.hz("do5"), 0.01)
        assertEquals(783.99, Melody.hz("sol5"), 0.01)
        assertEquals(1568.0, Melody.hz("sol6"), 0.05)
        assertEquals(2 * Melody.hz("mi5"), Melody.hz("mi6"), 0.001)
    }

    @Test
    fun theFinalChordIsACMajorChordMadeOfMelodyFriendlyNotes() {
        assertEquals(listOf("do5", "mi5", "sol5", "do6"), Melody.finalChord)
        // toutes les notes de la mélodie sont dans l'accord ou à un ton d'une note de l'accord : rien ne le contredit
        val chordClasses = Melody.finalChord.map { Math.floorMod(Melody.semitones(it), 12) }.toSet()
        for (n in tune) {
            val c = Math.floorMod(Melody.semitones(n), 12)
            assertTrue("$n heurte l'accord", chordClasses.any { abs(it - c) in setOf(0, 2, 10) })
        }
    }
}
