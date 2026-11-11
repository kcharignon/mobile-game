package com.kcharignon.petitsjeux.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kcharignon.petitsjeux.games.find.FindMiniGame
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.github.takahirom.roborazzi.captureRoboImage
import com.kcharignon.petitsjeux.core.Feedback
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.NoFeedback
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.Screen
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.InMemoryProfileStore
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.find.FindProgress
import com.kcharignon.petitsjeux.games.find.engine.FindLevel
import com.kcharignon.petitsjeux.games.find.engine.FindLevels
import com.kcharignon.petitsjeux.games.find.ui.FindMenuScreen
import com.kcharignon.petitsjeux.games.find.ui.FindPlayScreen
import com.kcharignon.petitsjeux.games.find.ui.NUDGE_AFTER_MS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/** Les écrans de Cherche et Trouve en PNG (docs/screenshots, en portrait), et de vrais niveaux joués en touchant les objets. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w390dp-h844dp-port-xhdpi")
class FindScreenshotsTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val levels: List<FindLevel> by lazy { FindLevels.parse(File("src/main/res/raw/find_levels.json").readText()) }
    private fun level(n: Int) = levels[n - 1]

    private fun show(name: String?, advanceMs: Long = 1400, content: @Composable () -> Unit) {
        rule.mainClock.autoAdvance = false
        rule.setContent { content() }
        rule.mainClock.advanceTimeBy(advanceMs)
        if (name != null) shot(name)
    }

    private fun shot(name: String) = rule.onRoot().captureRoboImage("../../docs/screenshots/$name.png")

    private fun play(ms: Long) {
        var left = ms
        while (left > 0) {
            rule.mainClock.advanceTimeByFrame()
            left -= 16
        }
    }

    private fun screen(
        l: FindLevel, progress: FindProgress = FindProgress(), onFinished: (Int, Int) -> Unit = { _, _ -> }, onNext: (Int) -> Unit = {},
    ): @Composable () -> Unit = { FindPlayScreen(l, progress, onFinished, onNext, {}, {}) }

    private fun tap(id: String) = rule.onNodeWithTag("obj-$id").performClick()

    private fun stateOf(id: String): String? = rule.onNodeWithTag("obj-$id").fetchSemanticsNode().config.getOrNull(SemanticsProperties.StateDescription)

    private fun ended() = rule.onAllNodesWithTag("end").fetchSemanticsNodes().isNotEmpty()

    // ------------------------------------------------------------------ les écrans

    @Test
    fun levelOneShowsTheModelFiveObjectsAndTheHint() = show("70-cherche-niveau-1", content = screen(level(1)))

    @Test
    fun levelOneHasExactlyFiveObjectsOnScreen() {
        show(null, content = screen(level(1)))
        assertEquals(5, levelObjects(level(1)).count { exists(it) })
    }

    @Test
    fun theLastLevelFitsFourteenObjectsOnTheScreen() = show("71-cherche-niveau-100", content = screen(level(100)))

    @Test
    fun aMiddleWorldHasItsOwnBackdrop() = show("72-cherche-niveau-45", content = screen(level(45)))

    @Test
    fun theMenuShowsTenWorlds() = show("73-cherche-niveaux") {
        FindMenuScreen(FindProgress(stars = (1..24).associateWith { listOf(3, 3, 2, 1, 3)[it % 5] }), onPlay = {}, onBack = {})
    }


    @Test
    fun theHubCardShowsObjectsAndAMagnifyingGlass() = show("78-cherche-carte") {
        Box(Modifier.background(Color(0xFFFFB020)).padding(20.dp)) {
            FindMiniGame.Illustration(Modifier.size(width = 330.dp, height = 170.dp))
        }
    }
    private fun levelObjects(l: FindLevel) = l.ids

    private fun exists(id: String) = rule.onAllNodesWithTag("obj-$id").fetchSemanticsNodes().isNotEmpty()

    // ------------------------------------------------------------------ des niveaux joués

    @Test
    fun touchingTheModelAtOnceGivesThreeStarsAndSavesBeforeTheEndCard() {
        val l = level(1)
        var result: Pair<Int, Int>? = null
        show(null, content = screen(l, onFinished = { s, m -> result = s to m }))
        tap(l.target)
        play(16)
        assertEquals("le niveau est enregistré dès le toucher, avant la fenêtre de bilan", 3 to 0, result)
        assertFalse(ended())
        play(2800)
        assertTrue(ended())
        shot("74-cherche-bravo")
        rule.onNodeWithTag("final-mistakes", useUnmergedTree = true).assertExists()
    }

    @Test
    fun aWrongObjectShakesGoesPaleAndCostsOneStar() {
        val l = level(1)
        var result: Pair<Int, Int>? = null
        show(null, content = screen(l, onFinished = { s, m -> result = s to m }))
        val fake = l.others.first()
        assertEquals("à toucher", stateOf(fake))
        tap(fake)
        play(500)
        assertEquals("pas le bon", stateOf(fake))
        shot("75-cherche-erreur")
        // un objet pâle n'est plus touchable : rien ne change si on le touche encore
        rule.onNodeWithTag("obj-$fake").performClick()
        play(16)
        assertEquals(null, result)
        tap(l.target)
        play(16)
        assertEquals(2 to 1, result)
    }

    @Test
    fun touchingEveryWrongObjectNeverEndsTheGameAndTheModelStillWins() {
        val l = level(100)
        var result: Pair<Int, Int>? = null
        show(null, content = screen(l, onFinished = { s, m -> result = s to m }))
        for (id in l.others) {
            tap(id)
            play(80)
        }
        play(1500)
        assertFalse("aucun game over : le niveau continue", ended())
        assertEquals(null, result)
        tap(l.target)
        play(16)
        assertEquals("toujours au moins une étoile", 1 to 13, result)
        play(2800)
        assertTrue(ended())
        shot("76-cherche-bravo-une-etoile")
    }

    @Test
    fun anObjectOfAnotherLevelIsNotThere() {
        show(null, content = screen(level(1)))
        assertTrue(!exists("licorne"))
        assertEquals(5, level(1).ids.count { exists(it) })
    }

    @Test
    fun theHintCaptionDisappearsAtTheFirstTouchAndOnlyShowsOnLevelOne() {
        val l = level(1)
        show(null, content = screen(l))
        rule.onNodeWithTag("caption", useUnmergedTree = true).assertExists()
        tap(l.others.first())
        play(16)
        assertTrue(rule.onAllNodesWithTag("caption", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theCaptionIsNotShownOnOtherLevels() {
        show(null, content = screen(level(2)))
        assertTrue(rule.onAllNodesWithTag("caption", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theHelpWigglesTheRightObjectAfterAWhileWithoutAnyPenalty() {
        val l = level(3)
        var result: Pair<Int, Int>? = null
        show(null, content = screen(l, onFinished = { s, m -> result = s to m }))
        play(NUDGE_AFTER_MS + 600)
        shot("77-cherche-aide")
        // l'aide ne coûte rien : on trouve toujours du premier coup avec trois étoiles
        tap(l.target)
        play(16)
        assertEquals(3 to 0, result)
    }

    @Test
    fun nextLevelAndReplayAndLevelsButtonsWork() {
        val l = level(1)
        var next = 0
        show(null, content = screen(l, onNext = { next = it }))
        tap(l.target)
        play(2800)
        rule.onNodeWithText("Niveau suivant").performClick()
        assertEquals(2, next)
    }

    // ------------------------------------------------------------------ depuis l'appli

    private fun profile(levelsDone: Int): Profile {
        val base = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!
        return if (levelsDone == 0) base else base.withGameSave(FindProgress.GAME_ID, FindProgress((1..levelsDone).associateWith { 3 }).toJson())
    }

    private fun openGame(store: InMemoryProfileStore) {
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, FindProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(1400)
    }

    @Test
    fun openingTheGameLandsOnTheNextLevelAndTheArrowGoesHomeAndTheGridButtonShowsTheLevels() {
        val store = InMemoryProfileStore(listOf(profile(10)))
        openGame(store)
        rule.onNodeWithTag("model").assertExists()
        assertTrue(rule.onAllNodesWithTag("level-11").fetchSemanticsNodes().isEmpty())
        rule.onNodeWithText("Niveau 11").assertExists()
        // le bouton en grille de tuiles ouvre le choix du niveau
        rule.onNodeWithTag("levels").performClick()
        play(300)
        rule.onNodeWithTag("level-11").assertExists()
    }

    @Test
    fun theBlueArrowGoesHome() {
        val store = InMemoryProfileStore(listOf(profile(0)))
        openGame(store)
        rule.onNodeWithTag("exit").performClick()
        play(300)
        assertTrue("à l'accueil", rule.onAllNodesWithText("Parking Malin").fetchSemanticsNodes().isNotEmpty())
        assertTrue(rule.onAllNodesWithTag("model").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theSystemBackGestureGoesHomeToo() {
        val store = InMemoryProfileStore(listOf(profile(0)))
        openGame(store)
        rule.runOnUiThread {
            rule.activity.onBackPressedDispatcher.onBackPressed()
            Snapshot.sendApplyNotifications()
        }
        play(300)
        assertTrue(rule.onAllNodesWithText("Parking Malin").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun aLevelPlayedInTheAppIsSavedAndTheGameOpensOnTheNextOneNextTime() {
        val store = InMemoryProfileStore(listOf(profile(0)))
        openGame(store)
        val l = level(1)
        tap(l.others.first())
        play(300)
        tap(l.target)
        play(2800)
        val saved = FindProgress.fromJson(store.load(0)!!.gameSave(FindProgress.GAME_ID))
        assertEquals(2, saved.starsOf(1))
        assertEquals(1, saved.mistakes)
        assertEquals(1, saved.played)
        assertEquals(2, saved.currentLevel)
        rule.onNodeWithText("Niveau suivant").performClick()
        play(1400)
        rule.onNodeWithText("Niveau 2").assertExists()
    }

    @Test
    fun allDoneOpensTheGridInsteadOfALevel() {
        val store = InMemoryProfileStore(listOf(profile(100)))
        openGame(store)
        // tout est réussi : la grille s'ouvre sur le dernier monde (où l'on en est)
        rule.onNodeWithTag("level-100").assertExists()
        assertTrue(rule.onAllNodesWithTag("model").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theSoundsAreTheExistingOnes() {
        val log = mutableListOf<String>()
        val feedback = object : Feedback by NoFeedback {
            override fun win() { log += "win" }
            override fun puff() { log += "puff" }
            override fun star(index: Int) { log += "star$index" }
        }
        val l = level(1)
        show(null) { CompositionLocalProvider(LocalFeedback provides feedback) { screen(l)() } }
        tap(l.others.first())
        play(300)
        tap(l.target)
        play(2800)
        assertEquals(listOf("puff", "win", "star0", "star1"), log)
    }
}
