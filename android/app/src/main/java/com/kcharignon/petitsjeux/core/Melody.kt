package com.kcharignon.petitsjeux.core

import kotlin.math.pow

/**
 * La petite mélodie de Champis Magiques : pas de musique de fond, mais **chaque champignon touché joue la note suivante** de cette mélodie.
 * Le rythme est celui des doigts de l'enfant ; l'ensemble des touchers dessine une mélodie. Elle recommence au début de chaque partie, et quand elle est finie.
 *
 * Composée (par un agent « compositeur », puis vérifiée par des tests) en do majeur pentatonique, comme la fanfare et les « ding » des étoiles :
 * aucune note ne sonne faux, même si deux se chevauchent ou si l'enfant touche très vite.
 *
 * - 4 phrases de 8 notes autour d'un motif (sol la sol mi) : question, élan jusqu'au point culminant (sol6, la 14e note), deuxième question, réponse qui se pose sur do5 ;
 * - surtout des mouvements par degrés, quelques sauts (tierce, quarte, quinte au plus) ; jamais deux fois la même note de suite ;
 * - la dernière note ramène au sol5 du début par une quinte : la boucle est naturelle.
 */
object Melody {
    /** Les notes de la gamme pentatonique de do utilisées, avec leur distance en demi-tons au la 440 Hz (la4). */
    private val SEMITONES_FROM_A4 = mapOf(
        "sol4" to -2, "la4" to 0, "do5" to 3, "re5" to 5, "mi5" to 7, "sol5" to 10, "la5" to 12, "do6" to 15, "re6" to 17, "mi6" to 19, "sol6" to 22,
    )

    val tune: List<String> = listOf(
        "sol5", "la5", "sol5", "mi5", "la5", "sol5", "mi5", "re5",
        "sol5", "la5", "do6", "la5", "mi6", "sol6", "mi6", "do6",
        "sol5", "la5", "sol5", "mi5", "sol5", "la5", "do6", "la5",
        "sol5", "la5", "sol5", "mi5", "sol5", "mi5", "re5", "do5",
    )

    val length: Int get() = tune.size

    /** Les notes différentes de la mélodie (pour préparer un son par note). */
    val distinct: List<String> = tune.distinct()

    /** La note du [index]-ième champignon touché de la partie (0 pour le premier) ; la mélodie recommence quand elle est finie. */
    fun noteAt(index: Int): String = tune[Math.floorMod(index, tune.size)]

    /** La hauteur d'une note en demi-tons à partir du la 440 Hz. */
    fun semitones(note: String): Int = requireNotNull(SEMITONES_FROM_A4[note]) { "note inconnue : $note" }

    /** La fréquence d'une note (Hz), tempérament égal, la4 = 440 Hz. */
    fun hz(note: String): Double = 440.0 * 2.0.pow(semitones(note) / 12.0)

    /** L'accord de do qui conclut une partie : do5, mi5, sol5, do6. */
    val finalChord: List<String> = listOf("do5", "mi5", "sol5", "do6")
}
