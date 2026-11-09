package com.kcharignon.petitsjeux.games.mushrooms

import com.kcharignon.petitsjeux.games.mushrooms.engine.Difficulty
import com.kcharignon.petitsjeux.games.mushrooms.engine.MushroomRound
import com.kcharignon.petitsjeux.games.mushrooms.engine.RoundEvent
import com.kcharignon.petitsjeux.games.mushrooms.engine.Scoring
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MushroomRoundTest {
    /** Joue une partie complète avec un « enfant » qui touche chaque champignon allumé [reactionMs] après son allumage (0 ou moins : il ne touche rien). */
    private class Played(val round: MushroomRound, val events: List<RoundEvent>, val endedAt: Long)

    private fun play(d: Difficulty, reactionMs: Long, seed: Long = 1L, tapDecoys: Boolean = false): Played {
        val round = MushroomRound(d, seed = seed)
        val events = ArrayList<RoundEvent>()
        var now = 0L
        while (!round.finished && now < 600_000L) {
            events += round.update(now)
            if (reactionMs > 0) {
                for (m in round.lit) {
                    if (m.decoy && !tapDecoys) continue
                    if (now - m.litAt >= reactionMs) events += round.tap(m.id, now)
                }
            }
            now += 10L
        }
        return Played(round, events, now)
    }

    /** Un « enfant » plus vrai : il touche **un champignon à la fois**, le plus ancien qu'il a le temps d'atteindre, et met [tapMs] pour passer de l'un à l'autre. */
    private class OneByOne(val round: MushroomRound, val maxReal: Int, val litAt: List<Long>)

    private fun playOneByOne(d: Difficulty, tapMs: Long, seed: Long = 1L): OneByOne {
        val round = MushroomRound(d, seed = seed)
        var now = 0L
        var freeAt = 0L
        var target: Int? = null
        var tapAt = 0L
        var maxReal = 0
        val litAt = ArrayList<Long>()
        while (!round.finished && now < 600_000L) {
            for (e in round.update(now)) if (e is RoundEvent.Lit && !e.decoy) litAt += now
            maxReal = maxOf(maxReal, round.lit.count { !it.decoy })
            if (target == null && now >= freeAt) {
                val reals = round.lit.filter { !it.decoy }
                val limit = d.litMs
                val reachable = if (limit == null) reals else reals.filter { it.litAt + limit - now > tapMs + 50 }
                (reachable.minByOrNull { it.litAt } ?: reals.maxByOrNull { it.litAt })?.let { target = it.id; tapAt = now + tapMs }
            }
            val id = target
            if (id != null && now >= tapAt) {
                round.tap(id, now)
                target = null
                freeAt = now
            }
            now += 10L
        }
        return OneByOne(round, maxReal, litAt)
    }

    @Test
    fun pointsDecreaseWithTheReactionTimeButNeverBelowTheMinimum() {
        assertEquals(100, Scoring.points(0))
        assertEquals(100, Scoring.points(-50))
        var previous = Int.MAX_VALUE
        for (ms in 0..8000 step 100) {
            val p = Scoring.points(ms.toLong())
            assertTrue("$ms ms : $p points", p <= previous && p >= Scoring.MIN_POINTS)
            previous = p
        }
        assertTrue(Scoring.points(1000) in 55..65)
        assertTrue(Scoring.points(2000) in 34..42)
        assertEquals(Scoring.MIN_POINTS, Scoring.points(60_000))
        assertTrue("toucher vite rapporte bien plus que toucher lentement", Scoring.points(300) > Scoring.points(2500) * 2)
    }

    @Test
    fun theFirstMushroomLightsUpAfterAShortWait() {
        val round = MushroomRound(Difficulty.EASY, seed = 3)
        assertTrue(round.update(0).isEmpty())
        assertTrue(round.update(500).isEmpty())
        val events = round.update(800)
        assertEquals(1, events.count { it is RoundEvent.Lit })
        assertEquals(1, round.lit.size)
    }

    @Test
    fun onEasyOneMushroomIsLitAtATimeAndItStaysLitUntilTouched() {
        val round = MushroomRound(Difficulty.EASY, seed = 5)
        var now = 0L
        var maxLit = 0
        while (now < 120_000L) {
            round.update(now)
            maxLit = maxOf(maxLit, round.lit.size)
            now += 50L
        }
        // un seul champignon s'allume, puis plus rien : il reste allumé (on n'a rien touché) et la partie n'est pas finie
        assertEquals(1, Difficulty.EASY.maxLit)
        assertEquals(1, maxLit)
        assertEquals(1, round.lit.size)
        assertFalse(round.finished)
        assertEquals(0, round.missed)
        assertTrue(round.lit.all { now - it.litAt > 100_000L })
    }

    @Test
    fun aRoundAlwaysEndsAndYouNeverLose() {
        for (d in Difficulty.entries) {
            // un enfant qui ne touche rien : seulement possible quand les champignons s'éteignent tout seuls
            if (d.litMs != null) {
                val idle = play(d, reactionMs = 0)
                assertTrue("${d.title} finit même sans rien toucher", idle.round.finished)
                assertEquals(d.targets, idle.round.missed)
                assertEquals(0, idle.round.score)
                assertEquals(1, idle.round.stars) // au moins une étoile
            }
            // un enfant très lent finit toujours
            val slow = play(d, reactionMs = 2500)
            assertTrue(slow.round.finished)
            assertTrue(slow.round.stars >= 1)
            assertEquals(d.targets, slow.round.picked + slow.round.missed)
            assertEquals(0, slow.round.remaining)
        }
    }

    @Test
    fun exactlyTheRightNumberOfMushroomsLightUpAndNoMoreThanMaxAtOnce() {
        for (d in Difficulty.entries) {
            val round = MushroomRound(d, seed = 9)
            var lit = 0
            var now = 0L
            var maxReal = 0
            while (!round.finished && now < 600_000L) {
                for (e in round.update(now)) if (e is RoundEvent.Lit && !e.decoy) lit++
                maxReal = maxOf(maxReal, round.lit.count { !it.decoy })
                for (m in round.lit) if (!m.decoy && now - m.litAt >= 1500) round.tap(m.id, now)
                now += 10L
            }
            assertEquals("${d.title} : allumages", d.targets, lit)
            assertEquals("${d.title} : jamais plus de ${d.maxLit} à la fois, et on les atteint", d.maxLit, maxReal)
        }
    }

    @Test
    fun theFasterTheChildTapsTheMoreItScores() {
        for (d in Difficulty.entries) {
            val fast = play(d, 250).round.score
            val medium = play(d, 900).round.score
            val slow = play(d, 2200).round.score
            assertTrue("${d.title} : $fast > $medium > $slow", fast > medium && medium > slow)
        }
    }

    @Test
    fun starsMatchHowFastTheChildIs() {
        // plus il y a de champignons allumés en même temps, plus chacun attend avant d'être touché : les seuils sont réglés pour un enfant qui touche un champignon à la fois
        for (d in Difficulty.entries) for (seed in 1L..5L) {
            assertEquals("${d.title} : très rapide (graine $seed)", 3, playOneByOne(d, 450, seed).round.stars)
            assertEquals("${d.title} : assez rapide (graine $seed)", 2, playOneByOne(d, 900, seed).round.stars)
            assertEquals("${d.title} : lent (graine $seed)", 1, playOneByOne(d, 2200, seed).round.stars)
        }
        assertEquals(1, Difficulty.EASY.stars(0))
        assertEquals(2, Difficulty.EASY.stars(Difficulty.EASY.twoStars))
        assertEquals(3, Difficulty.EASY.stars(Difficulty.EASY.threeStars))
        assertTrue(Difficulty.entries.all { it.threeStars < it.maxScore && it.twoStars < it.threeStars })
    }

    @Test
    fun difficultiesGetHarder() {
        val d = Difficulty.entries
        assertTrue(d.zipWithNext().all { (a, b) -> b.targets > a.targets && b.mushrooms >= a.mushrooms })
        // le même enfant (un champignon touché toutes les 0,75 s) gagne de moins en moins d'étoiles : il y a de plus en plus de champignons à surveiller
        val starsOfTheSameChild = d.map { playOneByOne(it, 750).round.stars }
        assertEquals(starsOfTheSameChild.sortedDescending(), starsOfTheSameChild)
        assertEquals("allumés en même temps : 1, 2, 3 puis 4", listOf(1, 2, 3, 4), d.map { it.maxLit })
        assertEquals(null, Difficulty.EASY.litMs) // le premier : ils restent allumés
        assertEquals(null, Difficulty.MEDIUM.litMs)
        assertTrue(Difficulty.HARD.litMs != null && Difficulty.EXPERT.litMs != null && Difficulty.EXPERT.litMs!! < Difficulty.HARD.litMs!!)
        assertTrue(d.filter { it.decoys } == listOf(Difficulty.EXPERT)) // la couleur interdite seulement à la fin
        assertEquals(null, Difficulty.EASY.previous)
        assertEquals(Difficulty.EASY, Difficulty.MEDIUM.previous)
    }

    @Test
    fun aForbiddenMushroomGivesNothingAndResetsTheStreakButNeverEndsTheGame() {
        val round = MushroomRound(Difficulty.EXPERT, seed = 11)
        var now = 0L
        var decoyTapped = false
        var streakBefore = 0
        while (!round.finished && now < 600_000L) {
            round.update(now)
            val real = round.lit.firstOrNull { !it.decoy && now - it.litAt >= 400 }
            val decoy = round.lit.firstOrNull { it.decoy }
            if (decoy != null && !decoyTapped && round.streak >= 2) {
                streakBefore = round.streak
                val scoreBefore = round.score
                val event = round.tap(decoy.id, now)
                assertTrue(event is RoundEvent.DecoyTapped)
                assertEquals(scoreBefore, round.score)
                assertEquals(0, round.streak)
                decoyTapped = true
            } else if (real != null) {
                round.tap(real.id, now)
            }
            now += 10L
        }
        assertTrue("un champignon interdit a été touché", decoyTapped)
        assertTrue(streakBefore >= 2)
        assertTrue(round.finished)
        assertEquals(Difficulty.EXPERT.targets, round.picked + round.missed) // les faux ne comptent pas
    }

    @Test
    fun forbiddenMushroomsOnlyAppearOnExpertAndDisappearByThemselves() {
        for (d in Difficulty.entries) {
            val p = play(d, 600, seed = 21)
            val decoys = p.events.count { it is RoundEvent.Lit && it.decoy }
            if (d.decoys) {
                assertTrue("${d.title} : des champignons interdits", decoys >= 3)
                // on n'en a touché aucun : tous se sont éteints tout seuls
                assertEquals(decoys, p.events.count { it is RoundEvent.DecoyGone })
                assertEquals(0, p.events.count { it is RoundEvent.DecoyTapped })
            } else {
                assertEquals(0, decoys)
            }
            assertTrue(p.round.lit.isEmpty())
        }
    }

    @Test
    fun tappingAnUnlitMushroomChangesNothing() {
        val round = MushroomRound(Difficulty.MEDIUM, seed = 2)
        round.update(1000)
        val unlit = (0 until Difficulty.MEDIUM.mushrooms).first { !round.isLit(it) }
        val before = round.score
        assertEquals(RoundEvent.Nothing(unlit), round.tap(unlit, 1100))
        assertEquals(before, round.score)
        assertEquals(0, round.streak)
    }

    @Test
    fun theStreakCountsConsecutiveTouchesAndAMissResetsIt() {
        val round = MushroomRound(Difficulty.HARD, seed = 4)
        var now = 0L
        var picks = 0
        while (picks < 3 && now < 60_000L) {
            round.update(now)
            round.lit.firstOrNull { now - it.litAt >= 300 }?.let { round.tap(it.id, now); picks++ }
            now += 10L
        }
        assertEquals(3, round.streak)
        // on laisse les suivants s'éteindre
        var missed = false
        while (!missed && now < 120_000L) {
            for (e in round.update(now)) if (e is RoundEvent.Missed) missed = true
            now += 10L
        }
        assertTrue(missed)
        assertEquals(0, round.streak)
        assertEquals(3, round.bestStreak)
    }

    @Test
    fun theSameSeedGivesTheSameRound() {
        fun trace(): List<RoundEvent> = play(Difficulty.EXPERT, 800, seed = 77).events
        assertEquals(trace(), trace())
        assertTrue(play(Difficulty.EXPERT, 800, seed = 78).events != trace())
    }

    @Test
    fun theyLightUpOneByOneUpToTheNumberOfSimultaneousMushroomsOfTheDifficulty() {
        for (d in Difficulty.entries) {
            // un enfant qui ne touche rien pendant les premières secondes : les champignons s'allument l'un après l'autre jusqu'au maximum
            val round = MushroomRound(d, seed = 6)
            val litTimes = ArrayList<Long>()
            var maxReal = 0
            var now = 0L
            while (now <= d.litMs.let { it ?: 6000L }) {
                for (e in round.update(now)) if (e is RoundEvent.Lit && !e.decoy) litTimes += now
                maxReal = maxOf(maxReal, round.lit.count { !it.decoy })
                now += 10L
            }
            assertEquals("${d.title} : ${d.maxLit} allumés en même temps", d.maxLit, maxReal)
            assertTrue("${d.title} : pas tous d'un coup (${litTimes})", litTimes.zipWithNext().all { (a, b) -> b - a >= d.gapMs.first })
            assertTrue("${d.title} : le premier attend un peu", litTimes.first() >= MushroomRound.START_DELAY_MS)
        }
    }

    @Test
    fun aNewMushroomLightsUpWithoutWaitingForTheOthersToGoOut() {
        for (d in listOf(Difficulty.MEDIUM, Difficulty.HARD, Difficulty.EXPERT)) {
            val round = MushroomRound(d, seed = 3)
            var now = 0L
            while (round.lit.count { !it.decoy } < d.maxLit && now < 20_000L) { round.update(now); now += 10L }
            assertEquals(d.maxLit, round.lit.count { !it.decoy })
            // on en touche un : les autres restent allumés et un nouveau s'allume vite, tant que les autres sont toujours là
            val others = round.lit.filter { !it.decoy }.drop(1).map { it.id }
            val touchedAt = now
            round.tap(round.lit.first { !it.decoy }.id, touchedAt)
            var newAt = -1L
            while (newAt < 0 && now < touchedAt + 3000L) {
                now += 10L
                if (round.update(now).any { it is RoundEvent.Lit && !it.decoy }) newAt = now
            }
            assertTrue("${d.title} : un nouveau s'allume", newAt >= 0)
            assertTrue("${d.title} : pas dans la même image (${newAt - touchedAt} ms)", newAt - touchedAt >= d.gapMs.first)
            assertTrue("${d.title} : vite (${newAt - touchedAt} ms)", newAt - touchedAt <= d.gapMs.last + 100)
            assertTrue("${d.title} : les autres sont toujours allumés", others.all { round.isLit(it) })
            assertEquals(d.maxLit, round.lit.count { !it.decoy })
        }
    }

    @Test
    fun aQuickChildAlwaysFacesTheFullNumberLitAtOnceAndEveryRoundEnds() {
        for (d in Difficulty.entries) for (tapMs in listOf(500L, 800L, 1500L)) {
            val p = playOneByOne(d, tapMs)
            assertTrue("${d.title} (${tapMs} ms) : finie", p.round.finished)
            assertEquals("${d.title} (${tapMs} ms) : tous les champignons comptés", d.targets, p.round.picked + p.round.missed)
            assertEquals("${d.title} (${tapMs} ms) : ${d.maxLit} allumés en même temps", d.maxLit, p.maxReal)
            assertTrue(p.round.stars >= 1)
        }
    }

    @Test
    fun aFinishedRoundIgnoresEverything() {
        val p = play(Difficulty.EASY, 300)
        val round = p.round
        val score = round.score
        assertTrue(round.update(p.endedAt + 1000).isEmpty())
        assertEquals(RoundEvent.Nothing(0), round.tap(0, p.endedAt + 1000))
        assertEquals(score, round.score)
    }
}
