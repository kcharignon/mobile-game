package com.kcharignon.petitsjeux.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.github.takahirom.roborazzi.captureRoboImage
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.Screen
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.InMemoryProfileStore
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.mushrooms.MushroomProgress
import com.kcharignon.petitsjeux.games.mushrooms.engine.Difficulty
import com.kcharignon.petitsjeux.games.mushrooms.ui.ShroomMenuScreen
import com.kcharignon.petitsjeux.games.mushrooms.ui.ShroomPlayScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Les écrans de Champis Magiques en PNG (docs/screenshots), et de vraies parties jouées en touchant les champignons. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w390dp-h844dp-port-xhdpi")
class MushroomsScreenshotsTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private fun show(name: String?, advanceMs: Long = 600, content: @Composable () -> Unit) {
        rule.mainClock.autoAdvance = false
        rule.setContent { content() }
        rule.mainClock.advanceTimeBy(advanceMs)
        if (name != null) rule.onRoot().captureRoboImage("../../docs/screenshots/$name.png")
    }

    private fun shot(name: String) = rule.onRoot().captureRoboImage("../../docs/screenshots/$name.png")

    private fun stateOf(id: Int): String? =
        rule.onNodeWithTag("shroom-$id").fetchSemanticsNode().config.getOrNull(SemanticsProperties.StateDescription)

    private fun litIds(d: Difficulty, state: String = "allumé"): List<Int> = (0 until d.mushrooms).filter { stateOf(it) == state }

    private fun tapShroom(id: Int) = rule.onNodeWithTag("shroom-$id").performTouchInput { click(center) }

    private fun score(): Int = rule.onNodeWithTag("score").fetchSemanticsNode().config.getOrNull(SemanticsProperties.Text)?.joinToString("")?.toIntOrNull() ?: -1

    private fun finished(): Boolean = rule.onAllNodesWithTag("final-score", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    /** Joue la partie jusqu'à la fenêtre de fin : touche chaque champignon allumé [delayMs] après qu'il s'est allumé (jamais les interdits). */
    private fun playToTheEnd(d: Difficulty, delayMs: Long) {
        val seen = HashMap<Int, Long>()
        var now = 0L
        var guard = 0
        while (!finished() && guard++ < 4000) {
            rule.mainClock.advanceTimeBy(40)
            now += 40
            val lit = litIds(d)
            seen.keys.retainAll(lit.toSet())
            for (id in lit) {
                val since = now - seen.getOrPut(id) { now }
                if (since >= delayMs) {
                    tapShroom(id)
                    seen.remove(id)
                }
            }
        }
        assertTrue("la partie finit", finished())
    }

    private val empty = MushroomProgress()
    private val someProgress = MushroomProgress()
        .withResult(Difficulty.EASY, 840, 15, 6)
        .withResult(Difficulty.MEDIUM, 760, 18, 4)

    @Test
    fun theMenuForANewPlayer() = show("40-champis-menu") {
        ShroomMenuScreen(empty, onPlay = {}, onBack = {})
    }

    @Test
    fun theMenuWithSomeProgress() = show("41-champis-menu-progres") {
        ShroomMenuScreen(someProgress, onPlay = {}, onBack = {})
    }

    @Test
    fun theIntroExplainsTheGame() = show("42-champis-intro") {
        ShroomPlayScreen(Difficulty.EASY, empty, { _, _, _ -> }, {}, {}, seed = 5L)
    }

    @Test
    fun theExpertIntroWarnsAboutTheForbiddenColour() {
        show("43-champis-intro-expert") {
            ShroomPlayScreen(Difficulty.EXPERT, empty, { _, _, _ -> }, {}, {}, seed = 5L)
        }
        rule.onNodeWithText("Ne touche pas les champignons violets !").assertExists()
    }

    @Test
    fun mushroomsLightUpAndScoreWhenTouched() {
        show(null) { ShroomPlayScreen(Difficulty.EASY, empty, { _, _, _ -> }, {}, {}, seed = 5L) }
        rule.onNodeWithText("C'est parti !").performClick()
        // rien n'est allumé au tout début, puis ils s'allument
        rule.mainClock.advanceTimeBy(200)
        assertTrue(litIds(Difficulty.EASY).isEmpty())
        rule.mainClock.advanceTimeBy(2600)
        val lit = litIds(Difficulty.EASY)
        assertTrue("des champignons allumés : $lit", lit.isNotEmpty())
        shot("44-champis-jeu")
        assertEquals(0, score())
        // on touche un champignon allumé : des points, il s'éteint
        tapShroom(lit.first())
        rule.mainClock.advanceTimeBy(120)
        assertTrue("des points gagnés (${score()})", score() > 0)
        assertFalse(lit.first() in litIds(Difficulty.EASY))
        shot("45-champis-touche")
        // toucher un champignon éteint ne change rien et ne coûte rien
        val before = score()
        tapShroom((0 until Difficulty.EASY.mushrooms).first { stateOf(it) == "éteint" })
        rule.mainClock.advanceTimeBy(120)
        assertEquals(before, score())
    }

    @Test
    fun theForbiddenMushroomsOfTheExpertGameGiveNothing() {
        show(null) { ShroomPlayScreen(Difficulty.EXPERT, empty, { _, _, _ -> }, {}, {}, seed = 8L) }
        rule.onNodeWithText("C'est parti !").performClick()
        var guard = 0
        while (litIds(Difficulty.EXPERT, "interdit").isEmpty() && guard++ < 400) rule.mainClock.advanceTimeBy(40)
        val decoys = litIds(Difficulty.EXPERT, "interdit")
        assertTrue("un champignon interdit s'allume", decoys.isNotEmpty())
        shot("46-champis-interdit")
        val before = score()
        tapShroom(decoys.first())
        rule.mainClock.advanceTimeBy(150)
        assertEquals("aucun point, aucune punition", before, score())
        assertFalse(decoys.first() in litIds(Difficulty.EXPERT, "interdit"))
    }

    @Test
    fun aFullRoundIsPlayedSavedAndNeverLost() {
        var result: Triple<Int, Int, Int>? = null
        var calls = 0
        show(null) { ShroomPlayScreen(Difficulty.EASY, empty, { s, p, b -> result = Triple(s, p, b); calls++ }, {}, {}, seed = 3L) }
        rule.onNodeWithText("C'est parti !").performClick()
        playToTheEnd(Difficulty.EASY, delayMs = 120)
        rule.mainClock.advanceTimeBy(2500)
        assertEquals("sauvegardé une seule fois", 1, calls)
        val (finalScore, picked, streak) = result!!
        assertEquals(Difficulty.EASY.targets, picked)
        assertTrue("des points : $finalScore", finalScore > 500)
        assertTrue(streak >= 5)
        assertEquals("$finalScore", rule.onNodeWithTag("final-score", useUnmergedTree = true).fetchSemanticsNode().config.getOrNull(SemanticsProperties.Text)?.joinToString(""))
        assertEquals(3, Difficulty.EASY.stars(finalScore)) // très rapide : trois étoiles
        shot("47-champis-fin")
    }

    @Test
    fun aChildWhoNeverTouchesAnythingStillFinishesWithoutLosing() {
        var calls = 0
        var score = -1
        show(null) { ShroomPlayScreen(Difficulty.HARD, empty, { s, _, _ -> calls++; score = s }, {}, {}, seed = 2L) }
        rule.onNodeWithText("C'est parti !").performClick()
        var guard = 0
        while (!finished() && guard++ < 2500) rule.mainClock.advanceTimeBy(40)
        assertTrue("la partie finit toute seule", finished())
        assertEquals(1, calls)
        assertEquals(0, score)
        rule.onNodeWithText("Bravo !").assertExists() // jamais « perdu »
    }

    @Test
    fun reachingTwoStarsUnlocksTheNextDifficultyInTheApp() {
        val player = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!
        val store = InMemoryProfileStore(listOf(player))
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, "mushrooms")) }
        rule.mainClock.advanceTimeBy(300)
        // seule la première difficulté est ouverte
        rule.onNodeWithTag("difficulty-1").assertExists()
        rule.onNodeWithTag("difficulty-0").performClick()
        rule.mainClock.advanceTimeBy(300)
        rule.onNodeWithText("C'est parti !").performClick()
        playToTheEnd(Difficulty.EASY, delayMs = 150)
        rule.mainClock.advanceTimeBy(2500)
        rule.onNodeWithTag("unlocked", useUnmergedTree = true).assertExists()
        shot("48-champis-debloque")
        val saved = MushroomProgress.fromJson(store.load(0)!!.gameSave(MushroomProgress.GAME_ID))
        assertTrue(saved.isUnlocked(Difficulty.MEDIUM))
        assertEquals(1, saved.played)
        assertTrue(saved.stars(Difficulty.EASY) >= 2)
        // retour au menu : la carte suivante est maintenant ouverte
        rule.onNodeWithText("Menu").performClick()
        rule.mainClock.advanceTimeBy(300)
        rule.onNodeWithTag("difficulty-1").performClick()
        rule.mainClock.advanceTimeBy(300)
        rule.onNodeWithText("C'est parti !").assertExists()
    }

    @Test
    fun aLockedDifficultyDoesNothingWhenTouched() {
        var chosen: Difficulty? = null
        show(null) { ShroomMenuScreen(empty, onPlay = { chosen = it }, onBack = {}) }
        rule.onNodeWithTag("difficulty-2").performClick()
        assertEquals(null, chosen)
        rule.onNodeWithTag("difficulty-0").performClick()
        assertEquals(Difficulty.EASY, chosen)
    }
}
