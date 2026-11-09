package com.kcharignon.petitsjeux.core.save

import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/** Les 5 sauvegardes locales, une par emplacement. */
interface ProfileStore {
    /** Le joueur de l'emplacement, ou `null` s'il est vide (ou illisible). */
    fun load(slot: Int): Profile?

    /** Tous les emplacements, dans l'ordre : toujours [Profile.SLOTS] éléments. */
    fun loadAll(): List<Profile?> = List(Profile.SLOTS) { load(it) }

    fun save(profile: Profile)

    fun delete(slot: Int)
}

/** Le format JSON d'une sauvegarde (voir docs/DESIGN.md). `v` = version du format, pour migrer un jour sans rien perdre. */
object ProfileJson {
    const val VERSION = 1

    fun encode(profile: Profile): String {
        val games = JSONObject()
        for ((id, text) in profile.games) games.put(id, JSONObject(text))
        return JSONObject()
            .put("v", VERSION)
            .put("slot", profile.slot)
            .put("name", profile.name)
            .put("avatar", profile.avatar.id)
            .put("created", profile.created)
            .put("lastPlayed", profile.lastPlayed)
            .put("settings", JSONObject().put("sound", profile.settings.sound).put("vibration", profile.settings.vibration))
            .put("games", games)
            .toString(2)
    }

    /** Lève une exception si le texte n'est pas une sauvegarde lisible de cet emplacement. */
    fun decode(text: String, expectedSlot: Int): Profile {
        val json = JSONObject(text)
        val slot = json.getInt("slot")
        require(slot == expectedSlot) { "la sauvegarde est celle de l'emplacement $slot, pas de $expectedSlot" }
        val name = requireNotNull(Profile.cleanName(json.getString("name"))) { "prénom vide" }
        val settings = json.optJSONObject("settings")
        val gamesJson = json.optJSONObject("games")
        val games = LinkedHashMap<String, String>()
        gamesJson?.keys()?.forEach { id -> gamesJson.optJSONObject(id)?.let { games[id] = it.toString() } }
        val created = json.optLong("created", 0L)
        return Profile(
            slot = slot,
            name = name,
            avatar = Avatar.fromId(json.optString("avatar")),
            created = created,
            lastPlayed = json.optLong("lastPlayed", created),
            settings = Settings(
                sound = settings?.optBoolean("sound", true) ?: true,
                vibration = settings?.optBoolean("vibration", true) ?: true,
            ),
            games = games,
        )
    }
}

/**
 * Sauvegardes dans le stockage PRIVÉ de l'appli : un fichier `slot-<n>.json` par emplacement.
 *
 * - Écriture **atomique** (fichier temporaire, synchronisé sur le disque, puis renommage) : une coupure en pleine écriture
 *   laisse l'ancienne sauvegarde intacte.
 * - Un fichier illisible est mis de côté (`slot-<n>.json.corrupt`) et l'emplacement apparaît vide : l'appli ne plante jamais.
 */
class FileProfileStore(private val dir: File) : ProfileStore {
    private fun file(slot: Int): File {
        require(slot in 0 until Profile.SLOTS) { "emplacement $slot hors de 0..${Profile.SLOTS - 1}" }
        return File(dir, "slot-$slot.json")
    }

    override fun load(slot: Int): Profile? {
        val file = file(slot)
        if (!file.isFile) return null
        return try {
            ProfileJson.decode(file.readText(Charsets.UTF_8), slot)
        } catch (e: Exception) {
            runCatching {
                Files.move(file.toPath(), File(dir, "slot-$slot.json.corrupt").toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            null
        }
    }

    override fun save(profile: Profile) {
        dir.mkdirs()
        val target = file(profile.slot)
        val temp = File(dir, "slot-${profile.slot}.json.tmp")
        FileOutputStream(temp).use { out ->
            out.write(ProfileJson.encode(profile).toByteArray(Charsets.UTF_8))
            out.flush()
            out.fd.sync()
        }
        Files.move(temp.toPath(), target.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
    }

    override fun delete(slot: Int) {
        file(slot).delete()
    }
}

/** Pour les tests et les captures d'écran. */
class InMemoryProfileStore(initial: List<Profile> = emptyList()) : ProfileStore {
    private val profiles = HashMap<Int, Profile>().apply { initial.forEach { put(it.slot, it) } }

    override fun load(slot: Int): Profile? = profiles[slot]

    override fun save(profile: Profile) {
        profiles[profile.slot] = profile
    }

    override fun delete(slot: Int) {
        profiles.remove(slot)
    }
}
