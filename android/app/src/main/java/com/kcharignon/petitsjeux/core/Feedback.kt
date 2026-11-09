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
import java.util.concurrent.atomic.AtomicReferenceArray

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

    /** L'étoile numéro [index] (0, 1, 2) s'allume sur l'écran de victoire : « ding » de plus en plus aigu. */
    fun star(index: Int)

    /** Un indice est donné : « tin-tin ». */
    fun hint()
}

object NoFeedback : Feedback {
    override fun exit() = Unit
    override fun bump() = Unit
    override fun win() = Unit
    override fun tap() = Unit
    override fun star(index: Int) = Unit
    override fun hint() = Unit
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

    // Tous les sons sont calculés dès le démarrage, sur un fil à part : le premier « vroum » n'attend donc rien.
    private val exits = AtomicReferenceArray<ShortArray>(3)
    private val starPcm = AtomicReferenceArray<ShortArray>(3)
    @Volatile private var bumpPcm: ShortArray? = null
    @Volatile private var winPcm: ShortArray? = null
    @Volatile private var tapPcm: ShortArray? = null
    @Volatile private var hintPcm: ShortArray? = null
    private var nextExit = 0

    init {
        Thread {
            runCatching {
                tapPcm = Synth.pop()
                for ((k, pitch) in doubleArrayOf(1.0, 0.9, 1.13).withIndex()) exits.set(k, Synth.vroum(pitch))
                bumpPcm = Synth.honk()
                for (k in 0 until 3) starPcm.set(k, Synth.star(k))
                hintPcm = Synth.hint()
                winPcm = Synth.fanfare()
            }
        }.start()
    }

    override fun exit() {
        // trois « vroum » un peu différents, à tour de rôle : pas de répétition lassante quand on enchaîne les voitures
        val k = nextExit++ % exits.length()
        play { exits.get(k) }
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

    override fun star(index: Int) = play { if (index in 0 until starPcm.length()) starPcm.get(index) else null }

    override fun hint() = play { hintPcm }

    private fun buzz(ms: Long) {
        if (!vibrationOn()) return
        runCatching { vibrator?.takeIf { it.hasVibrator() }?.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE)) }
    }

    private fun play(pcm: () -> ShortArray?) {
        if (!soundOn()) return
        Thread {
            runCatching {
                val data = pcm() ?: return@runCatching // pas encore calculé (les tout premiers instants) : on se tait
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
