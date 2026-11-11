package com.kcharignon.petitsjeux.games.find.engine

import com.kcharignon.petitsjeux.games.find.ui.FindHue
import com.kcharignon.petitsjeux.games.find.ui.FindKind
import com.kcharignon.petitsjeux.games.find.ui.FindObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Le catalogue : 120 objets différents, de neuf familles de couleurs, assez variés pour tirer 80 objets d'un coup. */
class FindCatalogTest {
    @Test
    fun theCatalogHasOneHundredTwentyDifferentObjectsOfNineColours() {
        assertEquals(120, FindObject.entries.size)
        assertEquals(120, FindObject.entries.map { it.id }.toSet().size)
        assertEquals(120, FindObject.entries.map { it.label }.toSet().size)
        assertEquals(9, FindHue.entries.size)
        assertEquals(FindHue.entries.toSet(), FindObject.entries.map { it.hue }.toSet())
        for (o in FindObject.entries) assertTrue("${o.id} n'a pas de nom lisible", o.label.length >= 5)
    }

    @Test
    fun everyColourAndEveryKindHasEnoughObjects() {
        for (hue in FindHue.entries) assertTrue("$hue a trop peu d'objets", FindObject.entries.count { it.hue == hue } >= 10)
        for (kind in FindKind.entries) assertTrue("$kind a trop peu d'objets", FindObject.entries.count { it.kind == kind } >= 3)
    }

    @Test
    fun theGenerationCatalogMirrorsTheArt() {
        assertEquals(FindObject.entries.map { it.id }, FindCatalog.all.map { it.id })
        for (e in FindCatalog.all) {
            val o = FindObject.byId(e.id)!!
            assertEquals(o.hue.ordinal, e.hue)
            assertEquals(o.kind.ordinal, e.kind)
        }
    }

    @Test
    fun theFirstFortyIdentifiersNeverChange() {
        // les 40 objets de la première version : leurs identifiants sont écrits dans des sauvegardes ou des tests, on n'y touche pas
        val first = listOf(
            "pomme", "fraise", "coeur", "voiture", "champignon", "cerises", "carotte", "orange", "citrouille", "poisson", "renard", "banane", "etoile", "soleil",
            "poussin", "fromage", "grenouille", "sapin", "poire", "tortue", "cactus", "baleine", "parapluie", "goutte", "bateau", "ballon", "raisin", "aubergine",
            "papillon", "cochon", "glace", "cupcake", "ours", "cookie", "maison", "chocolat", "nuage", "fantome", "bonhomme", "elephant",
        )
        assertEquals(first, FindObject.entries.take(40).map { it.id })
    }
}
