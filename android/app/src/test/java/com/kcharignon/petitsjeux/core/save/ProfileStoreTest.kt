package com.kcharignon.petitsjeux.core.save

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ProfileStoreTest {
    @get:Rule
    val tmp = TemporaryFolder()

    private fun store() = FileProfileStore(File(tmp.root, "saves"))

    private fun lea(slot: Int = 0) = Profile.create(slot, "Léa", Avatar.FOX, now = 1_000L)!!

    @Test
    fun emptySlotsAreNull() {
        val all = store().loadAll()
        assertEquals(Profile.SLOTS, all.size)
        assertTrue(all.all { it == null })
    }

    @Test
    fun saveThenLoadRoundTripsEverything() {
        val store = store()
        val profile = lea(2)
            .copy(
                lastPlayed = 5_000L,
                settings = Settings(sound = false, vibration = true),
            )
            .withGameSave("parking", JSONObject().put("stars", JSONObject().put("1", 3)).put("carsFreed", 12))
            .withGameSave("autre", JSONObject().put("x", 1))
        store.save(profile)

        // une autre instance lit le même dossier : c'est bien sur disque
        val loaded = FileProfileStore(File(tmp.root, "saves")).load(2)
        assertEquals(profile.copy(games = emptyMap()), loaded?.copy(games = emptyMap()))
        assertEquals(3, loaded!!.gameSave("parking")!!.getJSONObject("stars").getInt("1"))
        assertEquals(1, loaded.gameSave("autre")!!.getInt("x"))
        assertNull(loaded.gameSave("jamais-joue"))
        assertNull(store.load(1))
    }

    @Test
    fun theFivePlayersAreIndependent() {
        val store = store()
        for (slot in 0 until Profile.SLOTS) {
            store.save(Profile.create(slot, "Joueur $slot", Avatar.entries[slot], now = slot.toLong())!!)
        }
        store.delete(3)
        val all = store.loadAll()
        assertEquals(listOf("Joueur 0", "Joueur 1", "Joueur 2", null, "Joueur 4"), all.map { it?.name })
        assertEquals(Avatar.entries[4], all[4]!!.avatar)
    }

    @Test
    fun savingOverwritesAndLeavesNoTemporaryFile() {
        val store = store()
        store.save(lea())
        store.save(lea().copy(name = "Léa B"))
        assertEquals("Léa B", store.load(0)!!.name)
        val files = File(tmp.root, "saves").list()!!.toList()
        assertEquals(listOf("slot-0.json"), files)
    }

    @Test
    fun aCorruptFileIsSetAsideAndTheSlotLooksEmpty() {
        val dir = File(tmp.root, "saves").apply { mkdirs() }
        File(dir, "slot-1.json").writeText("{ pas du json")
        val store = FileProfileStore(dir)
        assertNull(store.load(1))
        assertFalse(File(dir, "slot-1.json").exists())
        assertTrue(File(dir, "slot-1.json.corrupt").exists())
        // et on peut y créer un joueur ensuite
        store.save(lea(1))
        assertNotNull(store.load(1))
    }

    @Test
    fun twoCorruptFilesInARowAreBothKept() {
        val dir = File(tmp.root, "saves").apply { mkdirs() }
        val store = FileProfileStore(dir)
        File(dir, "slot-2.json").writeText("premier")
        assertNull(store.load(2))
        File(dir, "slot-2.json").writeText("deuxième")
        assertNull(store.load(2))
        val kept = dir.list()!!.filter { it.startsWith("slot-2.json.corrupt") }
        assertEquals(2, kept.size)
    }

    @Test
    fun typingIsCutByCharactersNeverInTheMiddleOfAnEmoji() {
        val typed = Profile.limitTyping("abcdefghijk" + "😀😀")
        assertEquals(12, typed.codePointCount(0, typed.length))
        assertTrue(typed.endsWith("😀"))
        assertFalse(Character.isHighSurrogate(typed.last()))
        assertEquals("Léa", Profile.limitTyping("Léa\n"))
        assertNotNull(Profile.create(0, typed, Avatar.CAT, 0))
    }

    @Test
    fun aSaveFromAnotherSlotIsRefused() {
        val dir = File(tmp.root, "saves").apply { mkdirs() }
        File(dir, "slot-0.json").writeText(ProfileJson.encode(lea(3)))
        assertNull(FileProfileStore(dir).load(0))
    }

    @Test
    fun decodeToleratesMissingOptionalFieldsAndUnknownAvatars() {
        val text = """{"v":1,"slot":4,"name":"  Zoé  ","avatar":"dragon"}"""
        val p = ProfileJson.decode(text, 4)
        assertEquals("Zoé", p.name)
        assertEquals(Avatar.CAT, p.avatar)
        assertEquals(Settings(), p.settings)
        assertTrue(p.games.isEmpty())
    }

    @Test
    fun namesAreCleanedAndCapped() {
        assertNull(Profile.cleanName("   "))
        assertEquals("Léa Marie", Profile.cleanName("  Léa    Marie "))
        assertEquals("Abcdefghijkl", Profile.cleanName("Abcdefghijklmnop"))
        assertEquals(12, Profile.cleanName("😀".repeat(20))!!.let { it.codePointCount(0, it.length) })
        assertNull(Profile.create(0, "", Avatar.CAT, 0))
    }

    @Test
    fun slotsAreChecked() {
        assertTrue(runCatching { store().load(5) }.isFailure)
        assertTrue(runCatching { store().load(-1) }.isFailure)
        assertTrue(runCatching { Profile(5, "A", Avatar.CAT, 0) }.isFailure)
    }

    @Test
    fun avatarIdsAreStable() {
        // Ces identifiants sont écrits dans les sauvegardes : ils ne doivent jamais changer.
        assertEquals(
            listOf("chat", "chien", "ours", "panda", "lapin", "grenouille", "renard", "lion"),
            Avatar.entries.map { it.id },
        )
    }
}

class RanksTest {
    @Test
    fun ranksFollowTheStars() {
        assertEquals("Petite voiture", Ranks.of(0).name)
        assertEquals("Petite voiture", Ranks.of(9).name)
        assertEquals("Premier tour de roue", Ranks.of(10).name)
        assertEquals("As du créneau", Ranks.of(99).name)
        assertEquals("Génie du parking", Ranks.of(160).name)
        assertEquals("Légende du parking", Ranks.of(299).name)
        assertEquals("Maître du bitume", Ranks.of(300).name)
        assertEquals("Roi de la route", Ranks.of(400).name)
        assertEquals("Légende absolue", Ranks.of(540).name)
        assertEquals("Légende absolue", Ranks.of(9999).name)
    }

    @Test
    fun progressTowardsTheNextRank() {
        assertEquals(0f, Ranks.progress(0), 0.001f)
        assertEquals(0.5f, Ranks.progress(5), 0.001f)
        assertEquals(1f, Ranks.progress(700), 0.001f)
        assertEquals(100, Ranks.next(99)!!.minStars)
        assertNull(Ranks.next(540))
    }

    @Test
    fun ranksAreSortedAndStartAtZero() {
        assertEquals(0, Ranks.all.first().minStars)
        assertEquals(Ranks.all.sortedBy { it.minStars }.map { it.name }, Ranks.all.map { it.name })
    }
}
