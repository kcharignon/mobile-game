package com.kcharignon.petitsjeux.core

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tanh

/**
 * Les sons du jeu, **synthétisés** (aucun fichier son dans l'appli), calculés une fois au démarrage hors du fil principal.
 * Échantillons 16 bits, mono, [RATE] Hz. Du pur Kotlin : testé sur la JVM (niveau, durée, silence final).
 *
 * Techniques : oscillateurs à bande limitée (pas de repliement), filtres à variable d'état qui s'ouvrent ou se déplacent (le souffle du moteur),
 * cloches à partiels décroissants, petite réverbération à peignes. Les notes sont toutes en do majeur : elles s'accordent entre elles
 * (la fanfare de victoire et les « ding » des étoiles se superposent sans fausse note).
 */
object Synth {
    const val RATE = 44_100
    private const val TWO_PI = 2.0 * PI

    /** Notes (Hz) : do5, mi5, sol5, do6, mi6, sol6. */
    private val C5 = 523.25
    private val E5 = 659.25
    private val G5 = 783.99
    private val C6 = 1046.5
    private val E6 = 1318.5
    private val G6 = 1568.0

    // ------------------------------------------------------------------ outils

    /** Bruit blanc déterministe (le même à chaque lancement). */
    private class Noise(seed: Long) {
        private var s = seed
        fun next(): Double {
            s = s * 6364136223846793005L + 1442695040888963407L
            return ((s ushr 11).toDouble() / (1L shl 53).toDouble()) * 2.0 - 1.0
        }
    }

    /** Filtre à variable d'état (Chamberlin) : sortie passe-bande, fréquence changeable à chaque échantillon. */
    private class BandPass(private val damping: Double = 0.5) {
        private var low = 0.0
        private var band = 0.0
        fun process(x: Double, cutoffHz: Double): Double {
            val f = 2.0 * sin(PI * (cutoffHz / RATE).coerceAtMost(0.16))
            low += f * band
            val high = x - low - damping * band
            band += f * high
            return band
        }
    }

    /** Filtre passe-bas à un pôle. */
    private class LowPass {
        private var y = 0.0
        fun process(x: Double, cutoffHz: Double): Double {
            val a = 1.0 - exp(-TWO_PI * cutoffHz / RATE)
            y += a * (x - y)
            return y
        }
    }

    /** Scie à bande limitée : somme des harmoniques sous la moitié de la fréquence d'échantillonnage (pas de repliement). */
    private fun saw(phase: Double, freq: Double, maxHarmonics: Int = 24): Double {
        val k = ((RATE * 0.45) / freq).toInt().coerceIn(1, maxHarmonics)
        var v = 0.0
        for (h in 1..k) v += sin(TWO_PI * h * phase) / h
        return v * (2.0 / PI)
    }

    private fun samples(seconds: Double) = DoubleArray((seconds * RATE).toInt())

    private fun DoubleArray.mix(other: DoubleArray, atSeconds: Double, gain: Double = 1.0) {
        val offset = (atSeconds * RATE).toInt()
        for (i in other.indices) {
            val j = offset + i
            if (j in indices) this[j] += other[i] * gain
        }
    }

    /** Montée douce puis chute : enveloppe d'une note jouée [attack] s, tenue jusqu'à [hold] s, relâchée en [release] s. */
    private fun envelope(t: Double, attack: Double, hold: Double, release: Double): Double = when {
        t < attack -> t / attack
        t < hold -> 1.0
        t < hold + release -> (1.0 - (t - hold) / release).let { it * it * (3 - 2 * it) }
        else -> 0.0
    }

    /** Cloche : un son fondamental et des partiels qui s'éteignent de plus en plus vite ; [ring] = durée de résonance en secondes. */
    private fun bell(freq: Double, ring: Double, gain: Double = 1.0, brightness: Double = 1.0): DoubleArray {
        val out = samples(ring * 1.6)
        val ratios = doubleArrayOf(1.0, 2.0, 3.0, 4.2, 5.4)
        val amps = doubleArrayOf(1.0, 0.42, 0.22 * brightness, 0.11 * brightness, 0.05 * brightness)
        val rates = doubleArrayOf(3.0, 4.4, 6.5, 9.0, 13.0)
        for (i in out.indices) {
            val t = i / RATE.toDouble()
            var v = 0.0
            for (k in ratios.indices) {
                if (freq * ratios[k] > RATE * 0.45) continue
                v += amps[k] * sin(TWO_PI * freq * ratios[k] * t) * exp(-rates[k] * t / ring * 1.6)
            }
            out[i] = v * minOf(1.0, t / 0.002) * gain
        }
        return out
    }

    /** Petite réverbération : deux peignes en parallèle (écho court et diffus) mélangés au son sec. */
    private fun reverb(x: DoubleArray, mix: Double = 0.22): DoubleArray {
        val out = x.copyOf()
        for ((delay, feedback) in listOf(0.0297 to 0.42, 0.0411 to 0.36, 0.0533 to 0.3)) {
            val d = (delay * RATE).toInt()
            val comb = DoubleArray(x.size)
            for (i in x.indices) {
                comb[i] = x[i] + (if (i >= d) comb[i - d] * feedback else 0.0)
            }
            for (i in x.indices) out[i] += comb[i] * mix / 3.0
        }
        return out
    }

    /** Vers des échantillons 16 bits : écrêtage doux, niveau de crête [peak] (0 à 1), fondu final pour ne jamais faire de « clic ». */
    private fun pcm(x: DoubleArray, peak: Double): ShortArray {
        var max = 1e-9
        for (v in x) max = maxOf(max, abs(v))
        val fade = (0.012 * RATE).toInt()
        return ShortArray(x.size) { i ->
            val soft = tanh(x[i] / max * 1.25) / tanh(1.25)
            val tail = ((x.size - i) / fade.toDouble()).coerceIn(0.0, 1.0)
            (soft * peak * tail * Short.MAX_VALUE).toInt().toShort()
        }
    }

    // ------------------------------------------------------------------ les sons

    /**
     * Vroum : un moteur qui monte en régime en s'ouvrant (filtre passe-bas qui s'élève), avec ses pulsations, un grave de fond,
     * et le souffle d'air d'une voiture qui passe (bruit filtré, plus fort au milieu). [pitch] varie un peu d'une voiture à l'autre.
     */
    fun vroum(pitch: Double = 1.0): ShortArray {
        val dur = 0.62
        val out = samples(dur)
        val noise = Noise(7)
        val lowpass = LowPass()
        val air = BandPass(0.7)
        var phase = 0.0
        for (i in out.indices) {
            val t = i / RATE.toDouble()
            val p = t / dur
            val freq = (58.0 + 210.0 * (1.0 - exp(-3.6 * t))) * pitch
            phase += freq / RATE
            val engine = lowpass.process(saw(phase, freq, 20), 420.0 + 2600.0 * p.pow(1.3))
            val pulse = 0.72 + 0.28 * sin(TWO_PI * phase * 2)
            val sub = sin(TWO_PI * phase * 0.5) * 0.45
            val whoosh = air.process(noise.next(), 500.0 + 2200.0 * p) * sin(PI * p).pow(2) * 0.55
            out[i] = (engine * pulse + sub + whoosh) * envelope(t, 0.015, dur - 0.24, 0.24)
        }
        return pcm(out, 0.62)
    }

    /**
     * Pouet : le choc sourd de la tôle, puis le klaxon d'un jouet : une note nasale qui monte vite à sa hauteur
     * (scie filtrée + une quinte plus faible, un tout petit désaccord pour le « buzz »), puis retombe un peu.
     */
    fun honk(): ShortArray {
        val out = samples(0.5)
        // choc : un grave qui plonge + un claquement de bruit filtré
        val noise = Noise(11)
        val thudLow = LowPass()
        var thudPhase = 0.0
        val thud = samples(0.16)
        for (i in thud.indices) {
            val t = i / RATE.toDouble()
            val f = 52.0 + 95.0 * exp(-t / 0.035)
            thudPhase += f / RATE
            thud[i] = (sin(TWO_PI * thudPhase) * exp(-t / 0.07) + thudLow.process(noise.next(), 650.0) * exp(-t / 0.018) * 0.8)
        }
        out.mix(thud, 0.0, 1.1)
        // klaxon
        val horn = samples(0.36)
        val tone = LowPass()
        var p1 = 0.0
        var p2 = 0.0
        var p3 = 0.0
        for (i in horn.indices) {
            val t = i / RATE.toDouble()
            val bend = 1.0 - 0.17 * exp(-t / 0.022) - 0.05 * ((t - 0.2).coerceAtLeast(0.0) / 0.16)
            val f = 392.0 * bend
            p1 += f / RATE
            p2 += f * 1.0107 / RATE
            p3 += f * 1.4983 / RATE
            val raw = saw(p1, f, 14) * 0.55 + saw(p2, f * 1.0107, 14) * 0.4 + saw(p3, f * 1.4983, 10) * 0.22
            horn[i] = tone.process(raw, 2300.0) * envelope(t, 0.006, 0.23, 0.13)
        }
        out.mix(horn, 0.045, 0.85)
        return pcm(out, 0.82)
    }

    /** Fanfare de victoire : « ta-da-da-daaa » en cloches (sol, do, mi puis l'accord do-mi-sol-do), un souffle qui monte, des étincelles aiguës. */
    fun fanfare(): ShortArray {
        val out = samples(1.7)
        val intro = listOf(392.0 to 0.0, C5 to 0.09, E5 to 0.18)
        for ((f, at) in intro) out.mix(bell(f, 0.32, 0.8), at)
        for (f in listOf(C5, E5, G5, C6)) out.mix(bell(f, 0.95, 0.62, brightness = 1.2), 0.3)
        // souffle qui monte
        val noise = Noise(5)
        val air = BandPass(0.8)
        val whoosh = samples(0.34)
        for (i in whoosh.indices) {
            val t = i / RATE.toDouble()
            val p = t / 0.34
            whoosh[i] = air.process(noise.next(), 700.0 + 4500.0 * p * p) * sin(PI * p) * 0.5
        }
        out.mix(whoosh, 0.0, 0.5)
        // étincelles
        val sparkles = doubleArrayOf(2093.0, 2637.0, 3136.0, 2637.0, 4186.0, 3136.0)
        for ((k, f) in sparkles.withIndex()) out.mix(bell(f, 0.22, 0.34), 0.42 + k * 0.1)
        return pcm(reverb(out), 0.8)
    }

    /** « Ding » de l'étoile numéro [index] (0, 1, 2) : do, mi, sol aigus qui montent, comme des étoiles qui s'allument. */
    fun star(index: Int): ShortArray {
        val f = listOf(C6, E6, G6)[index.coerceIn(0, 2)]
        val out = samples(0.9)
        out.mix(bell(f, 0.55, 0.9, brightness = 1.4), 0.0)
        out.mix(bell(f * 2, 0.25, 0.22), 0.0)
        return pcm(reverb(out, 0.18), 0.7)
    }

    /** « Tin-tin » de l'indice : deux petites notes qui montent. */
    fun hint(): ShortArray {
        val out = samples(0.7)
        out.mix(bell(G5, 0.28, 0.8), 0.0)
        out.mix(bell(C6, 0.4, 0.8), 0.09)
        return pcm(out, 0.55)
    }

    /** Pop : un petit « bip » rond qui monte, pour les boutons. */
    fun pop(): ShortArray {
        val out = samples(0.09)
        val noise = Noise(3)
        var phase = 0.0
        for (i in out.indices) {
            val t = i / RATE.toDouble()
            phase += (560.0 + 520.0 * (t / 0.09)) / RATE
            out[i] = sin(TWO_PI * phase) * exp(-t / 0.022) + noise.next() * exp(-t / 0.002) * 0.25
        }
        return pcm(out, 0.4)
    }
}
