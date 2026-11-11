package com.kcharignon.petitsjeux.games.find.engine

import com.kcharignon.petitsjeux.games.find.ui.FindObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FindRoundTest {
    private val byId = FindCatalog.all.associateBy { it.id }

    private fun rounds(d: FindDifficulty, count: Int = 200) = (0 until count).map { FindRound.generate(d, it * 7919L + 3) }

    @Test
    fun everyDifficultyDrawsTheRightNumberOfDifferentCatalogObjects() {
        for (d in FindDifficulty.entries) for (r in rounds(d)) {
            assertEquals(d.targets, r.targets.size)
            assertEquals(d.objects, r.ids.size)
            assertEquals("${d.name} : un objet en double", r.ids.size, r.ids.toSet().size)
            assertTrue(r.ids.all { it in byId })
            assertEquals(r.targets + r.others, r.ids)
        }
    }

    @Test
    fun theTargetsAlwaysHaveDifferentColours() {
        for (d in FindDifficulty.entries) for (r in rounds(d)) {
            val hues = r.targets.map { byId.getValue(it).hue }
            assertEquals("${d.name} seed ${r.seed}", hues.size, hues.toSet().size)
        }
    }

    @Test
    fun theFirstDifficultyHasFakesThatLookNothingLikeTheTarget() {
        for (r in rounds(FindDifficulty.EASY)) {
            val t = byId.getValue(r.targets.single())
            val fakes = r.others.map { byId.getValue(it) }
            assertTrue("même couleur", fakes.none { it.hue == t.hue })
            assertTrue("même sorte", fakes.none { it.kind == t.kind })
            assertEquals("deux faux de la même couleur", fakes.size, fakes.map { it.hue }.toSet().size)
        }
    }

    @Test
    fun theSecondDifficultyKeepsFakesOfOtherColoursAndKindsThanTheTargets() {
        for (r in rounds(FindDifficulty.MEDIUM)) {
            val hues = r.targets.map { byId.getValue(it).hue }.toSet()
            val kinds = r.targets.map { byId.getValue(it).kind }.toSet()
            for (f in r.others.map { byId.getValue(it) }) {
                assertTrue("${f.id} a la couleur d'une cible", f.hue !in hues)
                assertTrue("${f.id} est de la sorte d'une cible", f.kind !in kinds)
            }
        }
    }

    @Test
    fun theHardDifficultiesDrawFakesAtRandomSoSomeLookLikeTheTargets() {
        for (d in listOf(FindDifficulty.HARD, FindDifficulty.EXPERT, FindDifficulty.MASTER)) {
            val similar = rounds(d, 100).count { r ->
                val hues = r.targets.map { byId.getValue(it).hue }.toSet()
                r.others.any { byId.getValue(it).hue in hues }
            }
            assertTrue("${d.name} : $similar manches sur 100 avec un faux de la couleur d'une cible", similar >= 95)
        }
    }

    @Test
    fun theSameSeedGivesTheSameRoundAndAnotherSeedGivesAnother() {
        for (d in FindDifficulty.entries) {
            val a = FindRound.generate(d, 42L)
            assertEquals(a, FindRound.generate(d, 42L))
            assertNotEquals(a, FindRound.generate(d, 43L))
            assertEquals(a.layoutSeed, FindRound.generate(d, 42L).layoutSeed)
        }
    }

    @Test
    fun everyObjectComesUpAsATargetAndAsAFakeAcrossManyRounds() {
        val asTarget = HashMap<String, Int>()
        val asFake = HashMap<String, Int>()
        for (d in FindDifficulty.entries) for (r in rounds(d, 400)) {
            r.targets.forEach { asTarget.merge(it, 1, Int::plus) }
            r.others.forEach { asFake.merge(it, 1, Int::plus) }
        }
        for (e in FindCatalog.all) {
            assertTrue("${e.id} n'est jamais à trouver", (asTarget[e.id] ?: 0) >= 20)
            assertTrue("${e.id} n'est jamais un faux", (asFake[e.id] ?: 0) >= 20)
        }
        // pas de favori : chaque objet sert de cible entre 0,4 et 1,8 fois la moyenne (les couleurs sont différentes entre cibles, donc pas tout à fait uniforme)
        val counts = FindCatalog.all.map { (asTarget[it.id] ?: 0) }
        val mean = counts.average()
        assertTrue("répartition des cibles : ${counts.min()} à ${counts.max()} pour une moyenne de $mean", counts.all { it > mean * 0.4 && it < mean * 1.8 })
    }

    @Test
    fun aTinyCatalogStillGivesAValidRound() {
        // pas assez de couleurs ou de sortes différentes : on relâche les exigences plutôt que de planter
        val tiny = List(6) { FindEntry("x$it", hue = 0, kind = 0) }
        val r = FindRound.generate(FindDifficulty.EASY, 5L, tiny)
        assertEquals(5, r.ids.toSet().size)
        assertEquals(1, r.targets.size)
        val r2 = FindRound.generate(FindDifficulty.MEDIUM, 5L, List(12) { FindEntry("y$it", hue = it % 2, kind = it % 2) })
        assertEquals(10, r2.ids.toSet().size)
        assertEquals(2, r2.targets.size)
    }

    @Test
    fun theCatalogCanFillTheBiggestRound() {
        assertTrue(FindObject.entries.size >= FindDifficulty.MASTER.objects)
        assertEquals(80, FindRound.generate(FindDifficulty.MASTER, 1L).ids.size)
    }
}
