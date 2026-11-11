package com.kcharignon.petitsjeux.games.find.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FindPlayTest {
    private val easy = FindRound(FindDifficulty.EASY, listOf("pomme"), listOf("poire", "ballon", "ours", "soleil"), seed = 7)
    private val medium = FindRound(FindDifficulty.MEDIUM, listOf("pomme", "ballon"), listOf("poire", "ours", "soleil", "lune", "chien", "pain", "cle", "oeuf"), seed = 9)

    @Test
    fun touchingTheTargetFindsItWithThreeStarsAtTheFirstTry() {
        val t = FindPlay(easy).tap("pomme")
        assertEquals(FindOutcome.FOUND, t.outcome)
        assertTrue(t.play.done)
        assertEquals(0, t.play.remaining)
        assertEquals(0, t.play.mistakes)
        assertEquals(3, t.play.stars)
    }

    @Test
    fun aWrongObjectCostsAStarAndGoesPale() {
        val t = FindPlay(easy).tap("poire")
        assertEquals(FindOutcome.WRONG, t.outcome)
        assertFalse(t.play.done)
        assertEquals(setOf("poire"), t.play.wrong)
        assertFalse(t.play.canTap("poire"))
        assertTrue(t.play.canTap("pomme"))
        assertEquals(2, t.play.tap("pomme").play.stars)
    }

    @Test
    fun youCannotLoseAndAlwaysKeepAtLeastOneStar() {
        var play = FindPlay(easy)
        for (id in listOf("poire", "ballon", "ours", "soleil")) play = play.tap(id).play
        assertEquals(4, play.mistakes)
        assertFalse(play.done)
        assertEquals(1, play.stars)
        assertEquals(FindOutcome.FOUND, play.tap("pomme").outcome)
        assertEquals(1, play.tap("pomme").play.stars)
    }

    @Test
    fun touchingTheSameWrongObjectTwiceCountsOnce() {
        val once = FindPlay(easy).tap("poire").play
        val again = once.tap("poire")
        assertEquals(FindOutcome.IGNORED, again.outcome)
        assertEquals(1, again.play.mistakes)
    }

    @Test
    fun theRoundIsOverOnlyWhenEveryTargetIsFoundInAnyOrder() {
        val first = FindPlay(medium).tap("ballon")
        assertEquals(FindOutcome.FOUND, first.outcome)
        assertFalse(first.play.done)
        assertEquals(1, first.play.remaining)
        assertEquals(listOf("pomme"), first.play.missing)
        val last = first.play.tap("pomme")
        assertEquals(FindOutcome.FOUND, last.outcome)
        assertTrue(last.play.done)
        assertEquals(emptyList<String>(), last.play.missing)
        assertEquals(setOf("ballon", "pomme"), last.play.found)
    }

    @Test
    fun aTargetAlreadyFoundCannotBeTouchedAgain() {
        val p = FindPlay(medium).tap("pomme").play
        assertFalse(p.canTap("pomme"))
        assertEquals(FindOutcome.IGNORED, p.tap("pomme").outcome)
        assertEquals(1, p.found.size)
    }

    @Test
    fun mistakesAreCountedPerWrongObjectAndCostStarsAccordingToTheDifficulty() {
        var p = FindPlay(medium)
        p = p.tap("poire").play
        assertEquals(3, p.stars) // MEDIUM : 1 erreur permise pour 3 étoiles
        p = p.tap("ours").play
        assertEquals(2, p.stars)
        p = p.tap("soleil").play.tap("lune").play
        assertEquals(1, p.stars)
    }

    @Test
    fun nothingHappensForUnknownObjectsOrAfterTheEnd() {
        val done = FindPlay(easy).tap("pomme").play
        assertEquals(FindOutcome.IGNORED, done.tap("poire").outcome)
        assertEquals(done, done.tap("poire").play)
        assertEquals(FindOutcome.IGNORED, FindPlay(easy).tap("licorne").outcome)
        assertFalse(FindPlay(easy).canTap("licorne"))
    }

    @Test
    fun aRoundWithNoMistakeAndNoHelpIsFlawless() {
        assertTrue(FindPlay(easy).flawless)
        assertTrue(FindPlay(easy).tap("pomme").play.flawless)
        val twoTargets = FindPlay(medium).tap("ballon").play.tap("pomme").play
        assertTrue(twoTargets.done)
        assertTrue(twoTargets.flawless)
    }

    @Test
    fun aMistakeMakesTheRoundImperfectEvenWithThreeStars() {
        // la difficulté « Bon œil » garde 3 étoiles avec une erreur, mais la manche n'est plus parfaite : elle n'ouvre pas la suivante
        val play = FindPlay(medium).tap("poire").play.tap("ballon").play.tap("pomme").play
        assertEquals(3, play.stars)
        assertFalse(play.flawless)
    }

    @Test
    fun theHelpMakesTheRoundImperfectButCostsNoStar() {
        val helped = FindPlay(easy).withHelp()
        assertTrue(helped.helped)
        assertFalse(helped.flawless)
        val done = helped.tap("pomme").play
        assertEquals(3, done.stars)
        assertFalse(done.flawless)
        assertEquals("l'aide ne se compte qu'une fois", helped, helped.withHelp())
    }

    @Test
    fun theHelpComingAfterTheEndChangesNothing() {
        val done = FindPlay(easy).tap("pomme").play
        assertEquals(done, done.withHelp())
        assertTrue(done.flawless)
    }
}
