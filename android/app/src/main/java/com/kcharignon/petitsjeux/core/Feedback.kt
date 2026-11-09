package com.kcharignon.petitsjeux.core

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.compositionLocalOf
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/** Les retours sonores et tactiles des jeux. Les jeux ne connaissent que cette interface (sans son ni vibration en test et en capture). */
interface Feedback {
    /** Une voiture sort : vroum. */
    fun exit()

    /** Une faute : klaxon. */
    fun bump()

    /** Niveau réussi : petite fanfare. */
    fun win()

    /** Un bouton : petit « pop ». */
    fun tap()
}

object NoFeedback : Feedback {
    override fun exit() = Unit
    override fun bump() = Unit
    override fun win() = Unit
    override fun tap() = Unit
}

val LocalFeedback = compositionLocalOf<Feedback> { NoFeedback }

/**
 * Sons **synthétisés** (aucun fichier son dans l'appli) et vibrations légères, selon les réglages du joueur courant.
 * Les sons se calculent une fois, au premier besoin, hors du fil principal.
 */
class AndroidFeedback(context: Context, private val soundOn: () -> Boolean, private val vibrationOn: () -> Boolean) : Feedback {
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= 31) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
    private val handler = Handler(Looper.getMainLooper())

    private val exitPcm by lazy { Synth.vroum() }
    private val bumpPcm by lazy { Synth.horn() }
    private val winPcm by lazy { Synth.fanfare() }
    private val tapPcm by lazy { Synth.pop() }

    override fun exit() {
        play { exitPcm }
        buzz(18)
    }

    override fun bump() {
        play { bumpPcm }
        buzz(70)
    }

    override fun win() {
        play { winPcm }
        buzz(120)
    }

    override fun tap() = play { tapPcm }

    private fun buzz(ms: Long) {
        if (!vibrationOn()) return
        runCatching { vibrator?.takeIf { it.hasVibrator() }?.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE)) }
    }

    private fun play(pcm: () -> ShortArray) {
        if (!soundOn()) return
        Thread {
            runCatching {
                val data = pcm()
                val track = AudioTrack.Builder()
                    .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
                    .setAudioFormat(AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(Synth.RATE).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .setBufferSizeInBytes(data.size * 2)
                    .build()
                track.write(data, 0, data.size)
                track.play()
                handler.postDelayed({ runCatching { track.release() } }, data.size * 1000L / Synth.RATE + 200)
            }
        }.start()
    }
}

/** Petits sons calculés : (échantillons 16 bits, mono, 22 050 Hz). */
object Synth {
    const val RATE = 22_050

    private fun make(seconds: Float, sample: (t: Float) -> Float): ShortArray {
        val n = (seconds * RATE).toInt()
        return ShortArray(n) { i ->
            val t = i / RATE.toFloat()
            val fadeOut = ((n - i) / (0.02f * RATE)).coerceIn(0f, 1f)
            (sample(t).coerceIn(-1f, 1f) * fadeOut * 0.55f * Short.MAX_VALUE).toInt().toShort()
        }
    }

    private fun saw(phase: Double): Float = (2.0 * (phase - Math.floor(phase + 0.5))).toFloat()

    /** Vroum : un son grave qui monte en accélérant. */
    fun vroum(): ShortArray {
        var phase = 0.0
        return make(0.4f) { t ->
            val freq = 90.0 + 260.0 * t * t / 0.16
            phase += freq / RATE
            val env = (t / 0.04f).coerceAtMost(1f) * exp(-1.6f * t)
            (saw(phase) * 0.55f + sin(2 * PI * phase * 2).toFloat() * 0.25f) * env
        }
    }

    /** Klaxon : deux notes proches jouées ensemble. */
    fun horn(): ShortArray = make(0.3f) { t ->
        val env = (t / 0.01f).coerceAtMost(1f) * (1f - t / 0.3f * 0.3f)
        val a = saw(330.0 * t)
        val b = saw(415.0 * t)
        (a + b) * 0.3f * env
    }

    /** Fanfare : do, mi, sol, do aigu. */
    fun fanfare(): ShortArray {
        val notes = floatArrayOf(523.25f, 659.25f, 783.99f, 1046.5f)
        return make(0.9f) { t ->
            val k = (t / 0.16f).toInt().coerceAtMost(notes.size - 1)
            val local = t - k * 0.16f
            val env = (local / 0.01f).coerceAtMost(1f) * exp(-3f * local) + if (k == notes.size - 1) 0.35f * exp(-2f * local) else 0f
            val f = notes[k].toDouble()
            (sin(2 * PI * f * t).toFloat() * 0.6f + sin(2 * PI * f * 2 * t).toFloat() * 0.2f) * env
        }
    }

    /** Pop : un petit « bip » qui retombe. */
    fun pop(): ShortArray = make(0.08f) { t ->
        sin(2 * PI * (700.0 + 500.0 * (1 - t / 0.08)) * t).toFloat() * exp(-30f * t)
    }
}
