package com.kcharignon.petitsjeux.core.save

import org.json.JSONObject

/** Les animaux au choix d'un joueur. L'`id` est celui écrit dans la sauvegarde : ne jamais le changer. */
enum class Avatar(val id: String) {
    CAT("chat"),
    DOG("chien"),
    BEAR("ours"),
    PANDA("panda"),
    RABBIT("lapin"),
    FROG("grenouille"),
    FOX("renard"),
    LION("lion");

    companion object {
        fun fromId(id: String?): Avatar = entries.firstOrNull { it.id == id } ?: CAT
    }
}

data class Settings(val sound: Boolean = true, val vibration: Boolean = true)

/**
 * Un joueur et tout ce que l'on garde de lui. Une seule sauvegarde par emplacement (0 à 4) ; chaque jeu a **sa part** dans [games]
 * (sous son identifiant) et n'écrit jamais dans celle des autres.
 */
data class Profile(
    val slot: Int,
    val name: String,
    val avatar: Avatar,
    val created: Long,
    val lastPlayed: Long = created,
    val settings: Settings = Settings(),
    /** Part de chaque jeu, en JSON : l'identifiant du jeu -> son texte JSON. Le format de chaque part est l'affaire du jeu. */
    val games: Map<String, String> = emptyMap(),
) {
    init {
        require(slot in 0 until SLOTS) { "emplacement $slot hors de 0..${SLOTS - 1}" }
        require(cleanName(name) == name) { "prénom non normalisé : « $name »" }
    }

    /** La part JSON du jeu [gameId], ou `null` s'il n'a jamais été joué. */
    fun gameSave(gameId: String): JSONObject? = games[gameId]?.let { runCatching { JSONObject(it) }.getOrNull() }

    fun withGameSave(gameId: String, save: JSONObject): Profile = copy(games = games + (gameId to save.toString()))

    companion object {
        const val SLOTS = 5
        const val NAME_MAX = 12

        /** Prénom nettoyé (espaces en trop retirés, 12 caractères au plus), ou `null` s'il est vide. */
        fun cleanName(raw: String): String? {
            val collapsed = raw.trim().replace(Regex("\\s+"), " ")
            if (collapsed.isEmpty()) return null
            val cut = if (collapsed.codePointCount(0, collapsed.length) > NAME_MAX) {
                collapsed.substring(0, collapsed.offsetByCodePoints(0, NAME_MAX)).trimEnd()
            } else {
                collapsed
            }
            return cut.ifEmpty { null }
        }

        /** Ce que l'on tape dans le champ du prénom : sans retour à la ligne, coupé à 12 caractères (jamais au milieu d'un emoji). */
        fun limitTyping(raw: String): String {
            val single = raw.filterNot { it == '\n' || it == '\r' }
            return if (single.codePointCount(0, single.length) > NAME_MAX) single.substring(0, single.offsetByCodePoints(0, NAME_MAX)) else single
        }

        /** Un nouveau joueur, ou `null` si le prénom est vide. */
        fun create(slot: Int, rawName: String, avatar: Avatar, now: Long): Profile? =
            cleanName(rawName)?.let { Profile(slot = slot, name = it, avatar = avatar, created = now) }
    }
}

/** Les rangs : l'évolution du joueur, selon ses étoiles cumulées sur tous les jeux. */
object Ranks {
    class Rank(val name: String, val minStars: Int)

    val all = listOf(
        Rank("Petite voiture", 0),
        Rank("Premier tour de roue", 10),
        Rank("Moteur chaud", 30),
        Rank("As du créneau", 60),
        Rank("Super turbo", 120),
        Rank("Génie du parking", 200),
        Rank("Légende du parking", 300),
    )

    fun of(stars: Int): Rank = all.last { stars >= it.minStars }

    /** Le rang suivant, ou `null` au rang maximum. */
    fun next(stars: Int): Rank? = all.firstOrNull { it.minStars > stars }

    /** Avancement (0 à 1) vers le rang suivant ; 1 au rang maximum. */
    fun progress(stars: Int): Float {
        val current = of(stars)
        val next = next(stars) ?: return 1f
        return ((stars - current.minStars).toFloat() / (next.minStars - current.minStars)).coerceIn(0f, 1f)
    }
}
