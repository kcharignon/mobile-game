package com.kcharignon.petitsjeux.games.find.engine

import com.kcharignon.petitsjeux.games.find.ui.FindHue
import com.kcharignon.petitsjeux.games.find.ui.FindKind
import com.kcharignon.petitsjeux.games.find.ui.FindObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Les niveaux livrés, regardés avec les couleurs et les sortes du catalogue : tous les objets existent, et les faux ressemblent au modèle comme
 * l'issue #29 (§2) le promet : plus on avance, plus ils lui ressemblent. Ce test relit le fichier, il ne fait pas confiance à l'outil.
 */
class FindLevelsCatalogTest {
    private val levels: List<FindLevel> by lazy { FindLevels.parse(File("src/main/res/raw/find_levels.json").readText()) }

    private fun obj(id: String) = FindObject.byId(id) ?: throw AssertionError("objet inconnu dans un niveau : $id")

    @Test
    fun theCatalogHasFortyObjectsOfNineColours() {
        assertEquals(40, FindObject.entries.size)
        assertEquals(40, FindObject.entries.map { it.id }.toSet().size)
        assertEquals(FindHue.entries.toSet(), FindObject.entries.map { it.hue }.toSet())
        for (hue in FindHue.entries) assertTrue("$hue a trop peu d'objets", FindObject.entries.count { it.hue == hue } >= 3)
        for (kind in FindKind.entries) assertTrue("$kind a trop peu d'objets", FindObject.entries.count { it.kind == kind } >= 2)
        for (o in FindObject.entries) assertTrue("${o.id} n'a pas de nom lisible", o.label.length >= 5)
    }

    @Test
    fun everyObjectOfEveryLevelExistsInTheCatalog() {
        for (l in levels) for (id in l.ids) assertNotNull("niveau ${l.number} : $id", FindObject.byId(id))
    }

    @Test
    fun everyObjectOfTheCatalogIsShownAndNoneIsFarMoreThanTheOthers() {
        val shown = levels.flatMap { it.ids }.groupingBy { it }.eachCount()
        assertEquals(40, shown.size)
        for ((id, n) in shown) assertTrue("$id montré $n fois", n in 15..32)
    }

    @Test
    fun theFirstTwoWorldsHaveVeryDifferentFakes() {
        for (l in levels.filter { it.world <= 1 }) {
            val t = obj(l.target)
            val fakes = l.others.map(::obj)
            assertTrue("niveau ${l.number} : un faux a la couleur du modèle", fakes.none { it.hue == t.hue })
            assertTrue("niveau ${l.number} : un faux est de la même sorte que le modèle", fakes.none { it.kind == t.kind })
            assertEquals("niveau ${l.number} : deux faux de la même couleur", fakes.size, fakes.map { it.hue }.toSet().size)
        }
    }

    @Test
    fun worldsThreeAndFourNeverShareTheModelsColourAndKeepFakesOfDifferentColours() {
        for (l in levels.filter { it.world in 2..3 }) {
            val t = obj(l.target)
            val fakes = l.others.map(::obj)
            assertTrue("niveau ${l.number}", fakes.none { it.hue == t.hue })
            assertEquals("niveau ${l.number} : deux faux de la même couleur", fakes.size, fakes.map { it.hue }.toSet().size)
        }
    }

    @Test
    fun fromWorldFiveOneFakeHasTheModelsColourFromTheFourthLevelOfTheWorld() {
        for (l in levels.filter { it.world in 4..6 }) {
            val t = obj(l.target)
            val sameHue = l.others.map(::obj).count { it.hue == t.hue }
            assertEquals("niveau ${l.number}", if (FindWorlds.indexIn(l.number) >= 3) 1 else 0, sameHue)
        }
    }

    @Test
    fun theLastThreeWorldsMixFakesOfTheSameColourAndTheSameKind() {
        for (l in levels.filter { it.world >= 7 }) {
            val t = obj(l.target)
            val fakes = l.others.map(::obj)
            val sameHue = fakes.count { it.hue == t.hue }
            val sameKind = fakes.count { it.kind == t.kind && it.hue != t.hue }
            val idx = FindWorlds.indexIn(l.number)
            val expectedHue = when (l.world) {
                7 -> if (idx >= 4) 1 else 0
                8 -> if (idx >= 3) 2 else 1
                else -> 2
            }
            assertEquals("niveau ${l.number} : faux de la couleur du modèle", expectedHue, sameHue)
            assertTrue("niveau ${l.number} : au moins un faux de la même sorte", sameKind >= 1)
            assertTrue("niveau ${l.number} : pas plus de 4 faux proches", sameHue + sameKind <= 4)
        }
    }

    @Test
    fun closeFakesNeverMakeTheModelAmbiguous() {
        // un faux n'est jamais le modèle sous un autre nom : les identifiants sont tous différents et aucun nom lisible n'est partagé
        assertEquals(FindObject.entries.size, FindObject.entries.map { it.label }.toSet().size)
        for (l in levels) assertTrue(l.target !in l.others)
    }
}
