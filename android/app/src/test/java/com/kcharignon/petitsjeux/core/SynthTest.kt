package com.kcharignon.petitsjeux.core

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.abs

class SynthTest {
    private val sounds: Map<String, ShortArray> by lazy {
        linkedMapOf(
            "vroum" to Synth.vroum(),
            "vroum-grave" to Synth.vroum(0.9),
            "vroum-aigu" to Synth.vroum(1.13),
            "fanfare" to Synth.fanfare(),
            "etoile-1" to Synth.star(0),
            "etoile-2" to Synth.star(1),
            "etoile-3" to Synth.star(2),
            "indice" to Synth.hint(),
            "pop" to Synth.pop(),
            "champi-note-1" to Synth.note(Melody.hz(Melody.noteAt(0))),
            "champi-note-grave" to Synth.note(Melody.hz("do5")),
            "champi-note-aigue" to Synth.note(Melody.hz("sol6")),
            "champi-accord" to Synth.chord(),
            "souffle" to Synth.puff(),
            "toc" to Synth.bonk(),
        )
    }

    private fun seconds(s: ShortArray) = s.size.toDouble() / Synth.RATE

    /** Passages par zéro par seconde dans [from, to) secondes : une mesure grossière de la hauteur. */
    private fun zeroCrossings(s: ShortArray, from: Double, to: Double): Double {
        val a = (from * Synth.RATE).toInt()
        val b = (to * Synth.RATE).toInt().coerceAtMost(s.size)
        var n = 0
        for (i in a + 1 until b) if ((s[i - 1] >= 0) != (s[i] >= 0)) n++
        return n / (to - from)
    }

    @Test
    fun everySoundIsAudibleSafeAndEndsInSilence() {
        for ((name, pcm) in sounds) {
            val peak = pcm.maxOf { abs(it.toInt()) }
            assertTrue("$name : trop faible ($peak)", peak > 0.2 * Short.MAX_VALUE)
            assertTrue("$name : trop fort ($peak)", peak < 0.95 * Short.MAX_VALUE)
            val clipped = pcm.count { abs(it.toInt()) >= 32_700 }
            assertEquals("$name : écrêté", 0, clipped)
            val tail = pcm.takeLast(20).maxOf { abs(it.toInt()) }
            assertTrue("$name : finit par un clic ($tail)", tail < 200)
            val mean = pcm.sumOf { it.toLong() }.toDouble() / pcm.size
            assertTrue("$name : décalage continu ($mean)", abs(mean) < 0.03 * peak)
        }
    }

    @Test
    fun everyMelodyNoteHasItsOwnPitchAndTheSameLevel() {
        val peaks = HashMap<String, Int>()
        val pitches = HashMap<String, Double>()
        for (name in Melody.distinct) {
            val pcm = Synth.note(Melody.hz(name))
            peaks[name] = pcm.maxOf { abs(it.toInt()) }
            pitches[name] = zeroCrossings(pcm, 0.0, 0.15) / 2
        }
        // la hauteur mesurée suit la hauteur voulue (à 12 % près : mesure grossière par passages à zéro)
        for (name in Melody.distinct) assertEquals("$name", Melody.hz(name), pitches.getValue(name), Melody.hz(name) * 0.12)
        // toutes les notes ont le même niveau : la mélodie ne « saute » pas en volume
        assertTrue("niveaux : $peaks", peaks.values.max() - peaks.values.min() < 0.05 * Short.MAX_VALUE)
    }

    @Test
    fun theFinalChordRingsLongerThanANote() {
        assertTrue(seconds(Synth.chord()) > 1.2)
        assertTrue(seconds(Synth.note(Melody.hz("do5"))) < 0.6)
    }

    @Test
    fun durationsAreAsDesigned() {
        assertTrue(seconds(sounds.getValue("vroum")) in 0.55..0.7)
        assertTrue(seconds(sounds.getValue("fanfare")) in 1.6..1.8)
        assertTrue(seconds(sounds.getValue("pop")) < 0.15)
        assertTrue(seconds(sounds.getValue("champi-note-1")) < 0.6)
        assertTrue(seconds(sounds.getValue("champi-accord")) in 1.4..1.8)
        assertTrue(seconds(sounds.getValue("souffle")) < 0.3)
        assertTrue(seconds(sounds.getValue("toc")) < 0.25)
        assertTrue(seconds(sounds.getValue("indice")) < 0.8)
    }

    @Test
    fun theStarsRiseInPitch() {
        val z = (1..3).map { zeroCrossings(sounds.getValue("etoile-$it"), 0.0, 0.12) }
        assertTrue("les étoiles doivent monter : $z", z[0] < z[1] && z[1] < z[2])
    }

    @Test
    fun theThreeEnginesHaveDifferentPitches() {
        val z = listOf("vroum-grave", "vroum", "vroum-aigu").map { zeroCrossings(sounds.getValue(it), 0.0, 0.4) }
        assertTrue("les trois vroum doivent différer : $z", z[0] < z[1] && z[1] < z[2])
    }

    @Test
    fun theEngineOpensUpAsItAccelerates() {
        val engine = sounds.getValue("vroum")
        assertTrue(zeroCrossings(engine, 0.3, 0.45) > zeroCrossings(engine, 0.02, 0.12))
    }

    @Test
    fun soundsAreTheSameEveryTime() {
        assertArrayEquals(Synth.vroum(), Synth.vroum())
        assertArrayEquals(Synth.fanfare(), Synth.fanfare())
    }

    private fun horn(name: String): Sound = Wav.parse(File("src/main/res/raw/$name.wav").readBytes())

    @Test
    fun theRealHornsAreReadableAndAudible() {
        for (name in listOf("horn_small", "horn_car", "horn_classic", "horn_double")) {
            val s = horn(name)
            assertEquals("$name : fréquence", 44_100, s.rate)
            assertTrue("$name : durée ${s.seconds}", s.seconds in 0.25..0.9)
            val peak = s.pcm.maxOf { abs(it.toInt()) }
            assertTrue("$name : trop faible ($peak)", peak > 0.6 * Short.MAX_VALUE)
            assertTrue("$name : écrêté ($peak)", peak < 0.95 * Short.MAX_VALUE)
            assertTrue("$name : finit par un clic", s.pcm.takeLast(20).maxOf { abs(it.toInt()) } < 300)
        }
    }

    @Test
    fun theWavReaderRefusesWhatItCannotPlay() {
        assertTrue(runCatching { Wav.parse(ByteArray(10)) }.isFailure)
        val stereo = wav(ShortArray(100)).also { it[22] = 2 }
        assertTrue(runCatching { Wav.parse(stereo) }.isFailure)
        val round = Wav.parse(wav(shortArrayOf(1, -2, 300, -300)))
        assertEquals(listOf<Short>(1, -2, 300, -300), round.pcm.toList())
    }

    /** Écrit les sons en .wav dans app/build/sounds/ pour pouvoir les écouter sur l'ordinateur (non versionné). */
    @Test
    fun writesWavFilesForListening() {
        val dir = File("build/sounds").apply { mkdirs() }
        for ((name, pcm) in sounds) File(dir, "$name.wav").writeBytes(wav(pcm))
        assertTrue(File(dir, "vroum.wav").length() > 44)
    }

    private fun wav(pcm: ShortArray): ByteArray {
        val data = pcm.size * 2
        val out = ByteArrayOutputStream()
        fun int(v: Int) = out.write(byteArrayOf(v.toByte(), (v shr 8).toByte(), (v shr 16).toByte(), (v shr 24).toByte()))
        fun short(v: Int) = out.write(byteArrayOf(v.toByte(), (v shr 8).toByte()))
        out.write("RIFF".toByteArray()); int(36 + data); out.write("WAVE".toByteArray())
        out.write("fmt ".toByteArray()); int(16); short(1); short(1); int(Synth.RATE); int(Synth.RATE * 2); short(2); short(16)
        out.write("data".toByteArray()); int(data)
        for (v in pcm) short(v.toInt())
        return out.toByteArray()
    }
}
