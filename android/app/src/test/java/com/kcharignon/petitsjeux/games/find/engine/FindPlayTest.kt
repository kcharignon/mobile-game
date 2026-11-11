package com.kcharignon.petitsjeux.games.find.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FindPlayTest {
    private val level = FindLevel(1, "pomme", listOf("poire", "ballon", "ours", "soleil"), seed = 7)

    @Test
    fun touchingTheModelFindsItWithThreeStarsAtTheFirstTry() {
        val t = FindPlay(level).tap("pomme")
        assertEquals(FindOutcome.FOUND, t.outcome)
        assertTrue(t.play.found)
        assertEquals(0, t.play.mistakes)
        assertEquals(3, t.play.stars)
    }

    @Test
    fun aWrongObjectCostsOneStarAndGoesPale() {
        val t = FindPlay(level).tap("poire")
        assertEquals(FindOutcome.WRONG, t.outcome)
        assertFalse(t.play.found)
        assertEquals(setOf("poire"), t.play.wrong)
        assertFalse(t.play.canTap("poire"))
        assertTrue(t.play.canTap("pomme"))
        assertEquals(2, t.play.tap("pomme").play.stars)
    }

    @Test
    fun touchingTheSamePaleObjectAgainCostsNothing() {
        val once = FindPlay(level).tap("poire").play
        val again = once.tap("poire")
        assertEquals(FindOutcome.IGNORED, again.outcome)
        assertEquals(1, again.play.mistakes)
    }

    @Test
    fun twoOrMoreMistakesStillGiveOneStar() {
        var p = FindPlay(level)
        for (id in listOf("poire", "ballon", "ours", "soleil")) p = p.tap(id).play
        assertEquals(4, p.mistakes)
        assertFalse("tout est faux sauf le modèle : le niveau n'est pas fini tant qu'on ne l'a pas touché", p.found)
        val end = p.tap("pomme")
        assertEquals(FindOutcome.FOUND, end.outcome)
        assertEquals(1, end.play.stars)
        assertEquals(1, FindScoring.stars(2))
        assertEquals(1, FindScoring.stars(50))
        assertEquals(3, FindScoring.stars(0))
        assertEquals(2, FindScoring.stars(1))
    }

    @Test
    fun youCanNeverLoseWhateverIsTouched() {
        // tous les ordres de touchers : on finit toujours, avec au moins une étoile
        val ids = level.ids
        fun explore(p: FindPlay, depth: Int) {
            if (p.found) {
                assertTrue(p.stars in 1..3)
                return
            }
            assertTrue("il reste toujours le modèle à toucher", p.canTap(level.target))
            for (id in ids) {
                val t = p.tap(id)
                if (t.outcome != FindOutcome.IGNORED) explore(t.play, depth + 1)
            }
        }
        explore(FindPlay(level), 0)
    }

    @Test
    fun nothingHappensAfterTheLevelIsFoundOrForAnUnknownObject() {
        val done = FindPlay(level).tap("pomme").play
        assertEquals(FindOutcome.IGNORED, done.tap("poire").outcome)
        assertEquals(FindOutcome.IGNORED, done.tap("pomme").outcome)
        assertEquals(FindOutcome.IGNORED, FindPlay(level).tap("licorne").outcome)
        assertEquals(0, done.mistakes)
    }
}
