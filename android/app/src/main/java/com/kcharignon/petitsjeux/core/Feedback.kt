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
import com.kcharignon.petitsjeux.R
import java.util.concurrent.atomic.AtomicReferenceArray

/** Les retours sonores et tactiles des jeux. Les jeux ne connaissent que cette interface (sans son ni vibration en test et en capture). */
interface Feedback {
    /** Une voiture sort : vroum. */
    fun exit()

    /** Une faute : le véhicule de [vehicleLength] cases klaxonne (petit klaxon pour une voiture, klaxon de camion, double klaxon de bus). */
    fun bump(vehicleLength: Int)

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
    override fun bump(vehicleLength: Int) = Unit
    override fun win() = Unit
    override fun tap() = Unit
    override fun star(index: Int) = Unit
    override fun hint() = Unit
}

val LocalFeedback = compositionLocalOf<Feedback> { NoFeedback }

/**
 * Les sons de l'appli et les vibrations légères, selon les réglages du joueur courant : les klaxons sont de vrais enregistrements
 * (res/raw, voir third_party/mixkit), le reste est synthétisé (Synth.kt). Tout est préparé dès le démarrage, sur un fil à part.
 */
class AndroidFeedback(private val context: Context, private val soundOn: () -> Boolean, private val vibrationOn: () -> Boolean) : Feedback {
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= 31) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
    private val handler = Handler(Looper.getMainLooper())

    private val exits = AtomicReferenceArray<Sound>(3)
    private val stars = AtomicReferenceArray<Sound>(3)
    private val horns = AtomicReferenceArray<Sound>(4) // petit, voiture, camion, bus
    @Volatile private var win: Sound? = null
    @Volatile private var tap: Sound? = null
    @Volatile private var hint: Sound? = null
    private var nextExit = 0
    private var nextSmallHorn = 0

    init {
        Thread {
            runCatching {
                tap = Sound(Synth.pop(), Synth.RATE)
                for ((k, pitch) in doubleArrayOf(1.0, 0.9, 1.13).withIndex()) exits.set(k, Sound(Synth.vroum(pitch), Synth.RATE))
                for (k in 0 until 3) stars.set(k, Sound(Synth.star(k), Synth.RATE))
                hint = Sound(Synth.hint(), Synth.RATE)
                win = Sound(Synth.fanfare(), Synth.RATE)
            }
            runCatching {
                val raws = intArrayOf(R.raw.horn_small, R.raw.horn_car, R.raw.horn_classic, R.raw.horn_double)
                for ((k, id) in raws.withIndex()) horns.set(k, Wav.parse(context.resources.openRawResource(id).use { it.readBytes() }))
            }
        }.start()
    }

    override fun exit() {
        // trois « vroum » un peu différents, à tour de rôle : pas de répétition lassante quand on enchaîne les véhicules
        val k = nextExit++ % exits.length()
        play { exits.get(k) }
        buzz(18)
    }

    override fun bump(vehicleLength: Int) {
        // une voiture : l'un des deux petits klaxons à tour de rôle ; un camion : le klaxon classique ; un bus : le double klaxon
        val k = when (vehicleLength) {
            3 -> 2
            4 -> 3
            else -> nextSmallHorn++ % 2
        }
        play { horns.get(k) }
        buzz(70)
    }

    override fun win() {
        play { win }
        buzz(120)
    }

    override fun tap() = play { tap }

    override fun star(index: Int) = play { if (index in 0 until stars.length()) stars.get(index) else null }

    override fun hint() = play { hint }

    private fun buzz(ms: Long) {
        if (!vibrationOn()) return
        runCatching { vibrator?.takeIf { it.hasVibrator() }?.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE)) }
    }

    private fun play(sound: () -> Sound?) {
        if (!soundOn()) return
        Thread {
            runCatching {
                val s = sound() ?: return@runCatching // pas encore prêt (les tout premiers instants) : on se tait
                val track = AudioTrack.Builder()
                    .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
                    .setAudioFormat(AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(s.rate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .setBufferSizeInBytes(s.pcm.size * 2)
                    .build()
                track.write(s.pcm, 0, s.pcm.size)
                track.play()
                handler.postDelayed({ runCatching { track.release() } }, (s.seconds * 1000).toLong() + 200)
            }
        }.start()
    }
}
