package com.kcharignon.petitsjeux.games.find.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.security.MessageDigest

/**
 * Le fichier de niveaux tel qu'il est livré (`res/raw/find_levels.json`, fabriqué par `tools/find/make-levels.js`) : ce test ne fait **pas confiance à l'outil**,
 * il relit le fichier et vérifie ce qui compte pour le joueur (les règles qui dépendent des couleurs sont dans `FindLevelsCatalogTest`). Et il fige les niveaux publiés.
 */
class FindLevelsDataTest {
    private val levels: List<FindLevel> by lazy { FindLevels.parse(File("src/main/res/raw/find_levels.json").readText()) }

    private fun inWorld(w: Int) = levels.filter { it.world == w }

    @Test
    fun hundredLevelsInTenWorldsOfTen() {
        assertEquals(FindWorlds.LEVEL_COUNT, levels.size)
        assertEquals(100, levels.size)
        for ((i, l) in levels.withIndex()) {
            assertEquals(i + 1, l.number)
            assertEquals(FindWorlds.of(l.number), l.world)
        }
        for (w in 0 until FindWorlds.COUNT) assertEquals(FindWorlds.LEVELS_PER_WORLD, inWorld(w).size)
        assertEquals(FindWorlds.COUNT, FindWorlds.names.size)
    }

    @Test
    fun theNumberOfObjectsGrowsEveryTenLevelsFromFiveToFourteen() {
        for (l in levels) assertEquals("niveau ${l.number}", 5 + l.world, l.objectCount)
        assertEquals(5, levels.first().objectCount)
        assertEquals(14, levels.last().objectCount)
        for (w in 0 until FindWorlds.COUNT) assertEquals(FindWorlds.objectCount(w), inWorld(w).first().objectCount)
    }

    @Test
    fun noObjectAppearsTwiceInALevelAndTheTargetIsNeverAFake() {
        for (l in levels) {
            assertEquals("niveau ${l.number}", l.objectCount, l.ids.toSet().size)
            assertTrue(l.target !in l.others)
        }
    }

    @Test
    fun theTargetChangesFromOneLevelToTheNextAndEveryObjectServesAsTargetTwoToFourTimes() {
        for (i in 1 until levels.size) assertTrue("niveau ${levels[i].number}", levels[i].target != levels[i - 1].target)
        val perObject = levels.groupingBy { it.target }.eachCount()
        assertEquals(40, perObject.size)
        for ((id, n) in perObject) assertTrue("$id sert $n fois de modèle", n in 2..4)
    }

    @Test
    fun theLayoutSeedsDifferFromLevelToLevel() {
        assertEquals(levels.size, levels.map { it.seed }.toSet().size)
        assertTrue(levels.all { it.seed >= 0 })
    }

    @Test
    fun theTenWorldsStayExactlyAsPublished() {
        val expected = listOf(
            "292b52d4933fe873b0a6fd42ad915d2c9d8bbf26088e78d8dfc0fe8f1e2cc567",
            "160d1be0bcd6f18f4f25b44744a433d9719bff7c8edb1a477d3826c853914f9d",
            "e50fbe16022c6fd9a5c552ae5bd0d3eae486044beacc438043aeed980d864f9e",
            "43490414095b1db8215a2d4ca73be31a88ef3ab7d2573c09ca1c29f49422d20b",
            "112e939e1203b398c26db8005d9f4dca7a98b2d678e54678732f98e5260eb5dc",
            "f0f0d5dd42eebc6a84f2779d2a6b7e9fcb32056f860378ea4eb0b846f195d31f",
            "55b20e890896bd76237b459add9e212dcf4d5e1888a5fd8df4402f5c6736374d",
            "c8f8493361d5c659fe4453b4b8db2701422890a208f46a0528fc4b976b027c1b",
            "4f7bd790fb1a134bf3e970e9952a6e201780ada90665f29e977beaf8529169e9",
            "5b49d47dc12ba569343206367bf4da28cd2af0c3eb80186f8be3b543297a20f5",
        )
        for (w in 0 until FindWorlds.COUNT) {
            val text = inWorld(w).joinToString("\n") { "${it.number}|${it.target}|${it.others.joinToString(",")}|${it.seed}" }
            val digest = MessageDigest.getInstance("SHA-256").digest(text.toByteArray()).joinToString("") { "%02x".format(it) }
            assertEquals("le monde ${w + 1} a changé : les niveaux publiés ne se retouchent jamais (on ajoute des niveaux à la suite)", expected[w], digest)
        }
    }

    @Test
    fun aBrokenFileIsRejected() {
        val ok = File("src/main/res/raw/find_levels.json").readText()
        assertThrowsIllegal { FindLevels.parse(ok.replace("\"version\": 1", "\"version\": 2")) }
        assertThrowsIllegal { FindLevels.parse(ok.replaceFirst("\"n\":1,", "\"n\":2,")) }
        // un objet en double dans le niveau 1 : on remplace le premier faux par le modèle
        val first = levels.first()
        assertThrowsIllegal { FindLevels.parse(ok.replaceFirst("\"${first.others.first()}\"", "\"${first.target}\"")) }
    }

    private fun assertThrowsIllegal(block: () -> Unit) {
        try {
            block()
        } catch (e: IllegalArgumentException) {
            return
        }
        throw AssertionError("un fichier incohérent aurait dû être rejeté")
    }
}
