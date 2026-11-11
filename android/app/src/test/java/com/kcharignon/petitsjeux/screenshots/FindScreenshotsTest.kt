package com.kcharignon.petitsjeux.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.kcharignon.petitsjeux.core.Feedback
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.NoFeedback
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.Screen
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.InMemoryProfileStore
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.find.FindMiniGame
import com.kcharignon.petitsjeux.games.find.FindProgress
import com.kcharignon.petitsjeux.games.find.engine.FindDifficulty
import com.kcharignon.petitsjeux.games.find.engine.FindRound
import com.kcharignon.petitsjeux.games.find.ui.FindMenuScreen
import com.kcharignon.petitsjeux.games.find.ui.FindObject
import com.kcharignon.petitsjeux.games.find.ui.FindPlayScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Les écrans de Cherche et Trouve en PNG (docs/screenshots, en portrait), et de vraies manches jouées en touchant les objets. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w390dp-h844dp-port-xhdpi")
class FindScreenshotsTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private fun round(d: FindDifficulty, seed: Long = SEED) = FindRound.generate(d, seed)

    private fun show(name: String?, advanceMs: Long = 1600, content: @Composable () -> Unit) {
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
        d: FindDifficulty, progress: FindProgress = FindProgress(), seed: Long = SEED,
        onFinished: (Int, Int, Int, Boolean) -> Unit = { _, _, _, _ -> }, onNext: (FindDifficulty) -> Unit = {}, onMenu: () -> Unit = {},
    ): @Composable () -> Unit = { FindPlayScreen(d, progress, onFinished, onNext, onMenu, {}, seed = seed) }

    private fun tap(id: String) = rule.onNodeWithTag("obj-$id").performClick()

    private fun stateOf(id: String): String? = rule.onNodeWithTag("obj-$id").fetchSemanticsNode().config.getOrNull(SemanticsProperties.StateDescription)

    private fun exists(id: String) = rule.onAllNodesWithTag("obj-$id").fetchSemanticsNodes().isNotEmpty()

    private fun ended() = rule.onAllNodesWithTag("end").fetchSemanticsNodes().isNotEmpty()

    private fun captionExists() = rule.onAllNodesWithTag("caption", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    /** Les objets à trouver d'une manche en cours, lus dans la description du bandeau (le jeu tire sa manche au hasard : on ne la connaît pas). */
    private fun targetsOnScreen(): List<String> {
        val description = rule.onNodeWithTag("model").fetchSemanticsNode().config.getOrNull(SemanticsProperties.ContentDescription)!!.joinToString()
        val labels = description.removePrefix("À retrouver : ").split(", ").map { it.removeSuffix(" (trouvé)") }
        return labels.map { l -> FindObject.entries.first { it.label == l }.id }
    }

    private fun allObjectsOnScreen(): List<String> = FindObject.entries.map { it.id }.filter { exists(it) }

    // ------------------------------------------------------------------ les écrans

    @Test
    fun theFirstDifficultyShowsOneTargetSixObjectsAndTheHint() = show("70-cherche-coup-d-oeil", content = screen(FindDifficulty.EASY))

    @Test
    fun theSecondDifficultyTurnsTwelveObjectsAndAsksForTwo() = show("71-cherche-bon-oeil", content = screen(FindDifficulty.MEDIUM))

    @Test
    fun theThirdDifficultyHasTwentyObjectsAndThreeTargets() = show("72-cherche-oeil-vif", content = screen(FindDifficulty.HARD))

    @Test
    fun theFourthDifficultyAddsSizesWithFortyObjects() = show("73-cherche-oeil-d-aigle", content = screen(FindDifficulty.EXPERT))

    @Test
    fun theLastDifficultyFitsEightyFourObjectsOnTheScreen() = show("74-cherche-super-detective", content = screen(FindDifficulty.MASTER))

    @Test
    fun theMenuShowsTheFiveDifficultiesAndLocksTheHardOnes() = show("75-cherche-difficultes") {
        FindMenuScreen(FindProgress(best = mapOf(0 to 3, 1 to 2, 2 to 1), cleared = setOf(0, 1)), onPlay = {}, onBack = {})
    }

    @Test
    fun theHubCardShowsObjectsAndAMagnifyingGlass() = show("78-cherche-carte") {
        Box(Modifier.background(Color(0xFFFFB020)).padding(20.dp)) {
            FindMiniGame.Illustration(Modifier.size(width = 330.dp, height = 170.dp))
        }
    }

    private fun assertShowsExactlyItsObjects(d: FindDifficulty) {
        show(null, content = screen(d))
        assertEquals("${d.name} : les objets à l'écran", round(d).ids.toSet(), allObjectsOnScreen().toSet())
        assertEquals(d.objects, allObjectsOnScreen().size)
    }

    @Test
    fun theFirstDifficultyShowsExactlySixObjects() = assertShowsExactlyItsObjects(FindDifficulty.EASY)

    @Test
    fun theSecondDifficultyShowsExactlyTwelveObjects() = assertShowsExactlyItsObjects(FindDifficulty.MEDIUM)

    @Test
    fun theThirdDifficultyShowsExactlyTwentyObjects() = assertShowsExactlyItsObjects(FindDifficulty.HARD)

    @Test
    fun theFourthDifficultyShowsExactlyFortyObjects() = assertShowsExactlyItsObjects(FindDifficulty.EXPERT)

    @Test
    fun theLastDifficultyShowsExactlyEightyFourObjects() = assertShowsExactlyItsObjects(FindDifficulty.MASTER)

    @Test
    fun theTargetsAreNamedInTheBannerAndMultipleTargetsGetOneTilePerObject() {
        show(null, content = screen(FindDifficulty.EXPERT))
        val r = round(FindDifficulty.EXPERT)
        assertEquals(r.targets.toSet(), targetsOnScreen().toSet())
        for (t in r.targets) rule.onNodeWithTag("tile-$t", useUnmergedTree = true).assertExists()
    }

    // ------------------------------------------------------------------ des manches jouées

    @Test
    fun touchingTheTargetAtOnceGivesThreeStarsAndSavesBeforeTheEndCard() {
        val r = round(FindDifficulty.EASY)
        var result: Triple<Int, Int, Int>? = null
        var flawless: Boolean? = null
        show(null, content = screen(FindDifficulty.EASY, onFinished = { s, m, f, ok -> result = Triple(s, m, f); flawless = ok }))
        tap(r.targets.single())
        play(16)
        assertEquals("la manche est enregistrée dès le toucher, avant la fenêtre de bilan", Triple(3, 0, 1), result)
        assertEquals("sans erreur et sans l'aide : une manche parfaite", true, flawless)
        assertFalse(ended())
        play(2800)
        assertTrue(ended())
        shot("76-cherche-bravo")
        rule.onNodeWithTag("final-mistakes", useUnmergedTree = true).assertExists()
        rule.onNodeWithTag("unlocked", useUnmergedTree = true).assertExists()
        // le bouton vert rejoue toujours ; l'orange monte d'une difficulté, qui vient de s'ouvrir
        rule.onNodeWithText("Rejouer").assertExists()
        rule.onNodeWithText("Difficulté suivante").assertExists()
        assertTrue(rule.onAllNodesWithTag("locked-hint", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun aWrongObjectShakesGoesPaleAndCostsAStar() {
        val r = round(FindDifficulty.EASY)
        var result: Triple<Int, Int, Int>? = null
        var flawless: Boolean? = null
        show(null, content = screen(FindDifficulty.EASY, onFinished = { s, m, f, ok -> result = Triple(s, m, f); flawless = ok }))
        val fake = r.others.first()
        assertEquals("à toucher", stateOf(fake))
        tap(fake)
        play(500)
        assertEquals("pas le bon", stateOf(fake))
        shot("77-cherche-erreur")
        // un objet pâle n'est plus touchable : rien ne change si on le touche encore
        rule.onNodeWithTag("obj-$fake").performClick()
        play(16)
        assertEquals(null, result)
        tap(r.targets.single())
        play(16)
        assertEquals(Triple(2, 1, 1), result)
        assertEquals("une erreur : la manche n'est plus parfaite", false, flawless)
    }

    @Test
    fun touchingEveryWrongObjectNeverEndsTheGameAndTheTargetsStillWin() {
        val r = round(FindDifficulty.MASTER)
        var result: Triple<Int, Int, Int>? = null
        show(null, content = screen(FindDifficulty.MASTER, onFinished = { s, m, f, _ -> result = Triple(s, m, f) }))
        for (id in r.others) {
            tap(id)
            play(48)
        }
        play(1500)
        assertFalse("aucun game over : la manche continue", ended())
        assertEquals(null, result)
        for (t in r.targets) {
            tap(t)
            play(16)
        }
        assertEquals("toujours au moins une étoile", Triple(1, 79, 5), result)
        play(2800)
        assertTrue(ended())
        shot("79-cherche-bravo-une-etoile")
    }

    @Test
    fun severalTargetsAreFoundInAnyOrderAndTheRoundEndsOnTheLastOne() {
        val r = round(FindDifficulty.HARD)
        val calls = mutableListOf<Triple<Int, Int, Int>>()
        show(null, content = screen(FindDifficulty.HARD, onFinished = { s, m, f, _ -> calls += Triple(s, m, f) }))
        val (a, b, c) = r.targets.reversed()
        assertTrue(captionText("Trouve-les tous !"))
        tap(a)
        play(600)
        assertEquals("trouvé", stateOf(a))
        assertTrue(captionText("Encore 2 à trouver"))
        shot("80-cherche-un-trouve")
        // retoucher un objet trouvé ne fait rien
        rule.onNodeWithTag("obj-$a").performClick()
        play(16)
        tap(b)
        play(600)
        assertTrue(calls.isEmpty())
        assertFalse(ended())
        assertTrue(captionText("Encore 1 à trouver"))
        tap(c)
        play(16)
        assertEquals(listOf(Triple(3, 0, 3)), calls)
        play(2800)
        assertTrue(ended())
    }

    private fun captionText(text: String) = rule.onAllNodesWithText(text, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    @Test
    fun theHintCaptionShowsOnTheFirstDifficultyUntilTheFirstTouch() {
        val r = round(FindDifficulty.EASY)
        show(null, content = screen(FindDifficulty.EASY))
        assertTrue(captionExists())
        tap(r.others.first())
        play(16)
        assertFalse(captionExists())
    }

    @Test
    fun theHelpWigglesATargetAfterAWhileWithoutAnyPenalty() {
        val r = round(FindDifficulty.EASY)
        var result: Triple<Int, Int, Int>? = null
        var flawless: Boolean? = null
        show(null, content = screen(FindDifficulty.EASY, onFinished = { s, m, f, ok -> result = Triple(s, m, f); flawless = ok }))
        play(FindDifficulty.EASY.nudgeMs + 600)
        shot("81-cherche-aide")
        // l'aide ne coûte aucune étoile : on trouve toujours du premier coup avec trois étoiles... mais la manche n'est plus parfaite
        tap(r.targets.single())
        play(16)
        assertEquals(Triple(3, 0, 1), result)
        assertEquals(false, flawless)
    }

    @Test
    fun foundBeforeTheHelpStartsTheRoundStaysFlawless() {
        val r = round(FindDifficulty.EASY)
        var flawless: Boolean? = null
        show(null, content = screen(FindDifficulty.EASY, onFinished = { _, _, _, ok -> flawless = ok }))
        // (le temps de la mise en place compte déjà : 1,6 s)
        play(FindDifficulty.EASY.nudgeMs - 3000)
        tap(r.targets.single())
        play(16)
        assertEquals(true, flawless)
    }

    @Test
    fun aHelpedRoundWithThreeStarsDoesNotOpenTheNextDifficultyAndSaysWhy() {
        var next: FindDifficulty? = null
        show(null, content = screen(FindDifficulty.EASY, onNext = { next = it }))
        play(FindDifficulty.EASY.nudgeMs + 600)
        tap(round(FindDifficulty.EASY).targets.single())
        play(2800)
        assertTrue(ended())
        rule.onNodeWithTag("locked-hint", useUnmergedTree = true).assertExists()
        assertTrue(rule.onAllNodesWithTag("unlocked", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
        assertTrue(rule.onAllNodesWithText("Difficulté suivante").fetchSemanticsNodes().isEmpty())
        rule.onNodeWithText("Rejouer").assertExists()
        assertEquals(null, next)
    }

    @Test
    fun theOrangeButtonClimbsToTheNewDifficultyAndTheGreenOneReplaysTheSameOne() {
        var next: FindDifficulty? = null
        show(null, content = screen(FindDifficulty.EASY, onNext = { next = it }))
        tap(round(FindDifficulty.EASY).targets.single())
        play(2800)
        rule.onNodeWithTag("unlocked", useUnmergedTree = true).assertExists()
        shot("82-cherche-bravo-debloque")
        // le vert est « Rejouer » : même difficulté, une autre manche, on ne monte pas
        rule.onNodeWithText("Rejouer").performClick()
        play(1600)
        assertEquals(null, next)
        assertFalse(ended())
        assertEquals(FindDifficulty.EASY.objects, allObjectsOnScreen().size)
        // une fois la nouvelle manche finie, l'orange monte d'une difficulté
        tap(targetsOnScreen().single())
        play(2800)
        rule.onNodeWithText("Difficulté suivante").performClick()
        assertEquals(FindDifficulty.MEDIUM, next)
    }

    @Test
    fun theNextDifficultyIsAnOrdinaryButtonWhenItWasAlreadyOpenEvenAfterAnImperfectRound() {
        var next: FindDifficulty? = null
        val opened = FindProgress(best = mapOf(0 to 3), cleared = setOf(0))
        show(null, content = screen(FindDifficulty.EASY, progress = opened, onNext = { next = it }))
        // une erreur : la manche est imparfaite, mais la difficulté suivante est déjà ouverte
        tap(round(FindDifficulty.EASY).others.first())
        play(100)
        tap(round(FindDifficulty.EASY).targets.single())
        play(2800)
        assertTrue(rule.onAllNodesWithTag("unlocked", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
        assertTrue(rule.onAllNodesWithTag("locked-hint", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
        rule.onNodeWithText("Difficulté suivante").performClick()
        assertEquals(FindDifficulty.MEDIUM, next)
    }

    @Test
    fun noNextDifficultyAfterMistakesOnTheFirstOneAndTheRuleIsShown() {
        show(null, content = screen(FindDifficulty.EASY))
        val r = round(FindDifficulty.EASY)
        tap(r.others[0])
        play(100)
        tap(r.others[1])
        play(100)
        tap(r.targets.single())
        play(2800)
        assertTrue(rule.onAllNodesWithText("Difficulté suivante").fetchSemanticsNodes().isEmpty())
        rule.onNodeWithTag("locked-hint", useUnmergedTree = true).assertExists()
        rule.onNodeWithText("Rejouer").assertExists()
        shot("83-cherche-bravo-a-refaire")
    }

    @Test
    fun oneMistakeIsEnoughToKeepTheNextDifficultyClosedEvenWithTwoStars() {
        var result: Triple<Int, Int, Int>? = null
        show(null, content = screen(FindDifficulty.EASY, onFinished = { st, m, f, _ -> result = Triple(st, m, f) }))
        val r = round(FindDifficulty.EASY)
        tap(r.others[0])
        play(100)
        tap(r.targets.single())
        play(2800)
        assertEquals("deux étoiles, comme avant", Triple(2, 1, 1), result)
        assertTrue(rule.onAllNodesWithText("Difficulté suivante").fetchSemanticsNodes().isEmpty())
        rule.onNodeWithTag("locked-hint", useUnmergedTree = true).assertExists()
    }

    @Test
    fun theLastDifficultyHasNoClimbButtonAndNoRuleToShow() {
        show(null, content = screen(FindDifficulty.MASTER))
        for (t in round(FindDifficulty.MASTER).targets) {
            tap(t)
            play(16)
        }
        play(2800)
        assertTrue(ended())
        assertTrue(rule.onAllNodesWithText("Difficulté suivante").fetchSemanticsNodes().isEmpty())
        assertTrue(rule.onAllNodesWithTag("locked-hint", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
        rule.onNodeWithText("Rejouer").assertExists()
        rule.onNodeWithText("Difficultés").assertExists()
    }

    @Test
    fun replayDrawsANewRound() {
        show(null, content = screen(FindDifficulty.MEDIUM))
        val before = allObjectsOnScreen().toSet()
        assertEquals(round(FindDifficulty.MEDIUM).ids.toSet(), before)
        rule.onNodeWithTag("restart").performClick()
        play(1600)
        val after = allObjectsOnScreen().toSet()
        assertEquals(round(FindDifficulty.MEDIUM, SEED + 1).ids.toSet(), after)
        assertNotEquals(before, after)
    }

    @Test
    fun theMenuButtonOfTheHeaderGoesToTheDifficultyChoice() {
        var menu = 0
        show(null, content = screen(FindDifficulty.EASY, onMenu = { menu++ }))
        rule.onNodeWithTag("levels").performClick()
        assertEquals(1, menu)
    }

    @Test
    fun theMenuLocksTheDifficultiesNotYetEarnedEvenWithTwoStarsOnThePreviousOne() {
        // « Bon œil » a 2 étoiles mais jamais de manche parfaite : « Œil vif » reste fermée
        show(null) { FindMenuScreen(FindProgress(best = mapOf(0 to 3, 1 to 2), cleared = setOf(0)), onPlay = {}, onBack = {}) }
        rule.onNodeWithTag("difficulty-0").assertIsEnabled()
        rule.onNodeWithTag("difficulty-1").assertIsEnabled()
        rule.onNodeWithTag("difficulty-2").assertIsNotEnabled()
        rule.onNodeWithTag("difficulty-4").assertIsNotEnabled()
    }

    // ------------------------------------------------------------------ depuis l'appli

    private fun profile(best: Map<Int, Int>, cleared: Set<Int> = emptySet()): Profile {
        val base = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!
        return if (best.isEmpty()) base else base.withGameSave(FindProgress.GAME_ID, FindProgress(best = best, cleared = cleared).toJson())
    }

    private fun openGame(store: InMemoryProfileStore) {
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, FindProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(1600)
    }

    @Test
    fun openingTheGameLandsOnTheFirstDifficultyForANewPlayer() {
        openGame(InMemoryProfileStore(listOf(profile(emptyMap()))))
        rule.onNodeWithTag("model").assertExists()
        rule.onNodeWithText("Coup d'œil").assertExists()
        assertEquals(FindDifficulty.EASY.objects, allObjectsOnScreen().size)
        assertTrue(rule.onAllNodesWithTag("difficulty-0").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun openingTheGameLandsOnTheHighestDifficultyAlreadyPlayedThenTheButtonShowsTheChoice() {
        openGame(InMemoryProfileStore(listOf(profile(mapOf(0 to 3, 1 to 2, 2 to 1), cleared = setOf(0, 1)))))
        rule.onNodeWithText("Œil vif").assertExists()
        assertEquals(20, allObjectsOnScreen().size)
        rule.onNodeWithTag("levels").performClick()
        play(300)
        rule.onNodeWithTag("difficulty-0").assertExists()
        rule.onNodeWithTag("difficulty-3").assertIsNotEnabled()
        rule.onNodeWithTag("difficulty-1").performClick()
        play(1600)
        rule.onNodeWithText("Bon œil").assertExists()
        assertEquals(FindDifficulty.MEDIUM.objects, allObjectsOnScreen().size)
    }

    @Test
    fun theBlueArrowGoesHome() {
        openGame(InMemoryProfileStore(listOf(profile(emptyMap()))))
        rule.onNodeWithTag("exit").performClick()
        play(300)
        assertTrue("à l'accueil", rule.onAllNodesWithText("Parking Malin").fetchSemanticsNodes().isNotEmpty())
        assertTrue(rule.onAllNodesWithTag("model").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theSystemBackGestureGoesHomeToo() {
        openGame(InMemoryProfileStore(listOf(profile(emptyMap()))))
        rule.runOnUiThread {
            rule.activity.onBackPressedDispatcher.onBackPressed()
            Snapshot.sendApplyNotifications()
        }
        play(300)
        assertTrue(rule.onAllNodesWithText("Parking Malin").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun aFlawlessRoundPlayedInTheAppIsSavedAndUnlocksTheNextDifficulty() {
        val store = InMemoryProfileStore(listOf(profile(emptyMap())))
        openGame(store)
        tap(targetsOnScreen().single())
        play(2800)
        val saved = FindProgress.fromJson(store.load(0)!!.gameSave(FindProgress.GAME_ID))
        assertEquals(3, saved.stars(FindDifficulty.EASY))
        assertEquals(0, saved.mistakes)
        assertEquals(1, saved.found)
        assertEquals(1, saved.played)
        assertTrue(saved.isCleared(FindDifficulty.EASY))
        assertTrue(saved.isUnlocked(FindDifficulty.MEDIUM))
        assertEquals(3, saved.totalStars)
        rule.onNodeWithText("Difficulté suivante").performClick()
        play(1600)
        rule.onNodeWithText("Bon œil").assertExists()
        assertEquals(FindDifficulty.MEDIUM.objects, allObjectsOnScreen().size)
    }

    @Test
    fun aRoundWithAMistakePlayedInTheAppIsSavedButDoesNotUnlockTheNextDifficulty() {
        val store = InMemoryProfileStore(listOf(profile(emptyMap())))
        openGame(store)
        val target = targetsOnScreen().single()
        val fake = allObjectsOnScreen().first { it != target }
        tap(fake)
        play(300)
        tap(target)
        play(2800)
        val saved = FindProgress.fromJson(store.load(0)!!.gameSave(FindProgress.GAME_ID))
        assertEquals(2, saved.stars(FindDifficulty.EASY))
        assertEquals(1, saved.mistakes)
        assertEquals(1, saved.found)
        assertEquals(1, saved.played)
        assertFalse(saved.isCleared(FindDifficulty.EASY))
        assertFalse(saved.isUnlocked(FindDifficulty.MEDIUM))
        assertEquals(2, saved.totalStars)
        assertTrue(rule.onAllNodesWithText("Difficulté suivante").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theHubCardShowsTheStarsAndTheProgress() {
        val p = FindProgress(best = mapOf(0 to 3, 1 to 2), played = 4)
        assertEquals(5, FindMiniGame.stars(p.toJson()))
        assertEquals("Difficulté 2 sur 5", FindMiniGame.progressLabel(p.toJson()))
        assertEquals("Nouveau !", FindMiniGame.progressLabel(null))
    }

    @Test
    fun theSoundsAreTheExistingOnes() {
        val log = mutableListOf<String>()
        val feedback = object : Feedback by NoFeedback {
            override fun win() { log += "win" }
            override fun puff() { log += "puff" }
            override fun pick(index: Int) { log += "pick$index" }
            override fun star(index: Int) { log += "star$index" }
        }
        val r = round(FindDifficulty.MEDIUM)
        show(null) { CompositionLocalProvider(LocalFeedback provides feedback) { screen(FindDifficulty.MEDIUM)() } }
        tap(r.others.first())
        play(300)
        tap(r.targets[0])
        play(300)
        tap(r.targets[1])
        play(2800)
        // une erreur en « Bon œil » reste permise pour 3 étoiles : trois « ding »
        assertEquals(listOf("puff", "pick0", "win", "star0", "star1", "star2"), log)
    }

    companion object {
        /** La graine des manches des tests (le jeu, lui, en tire une nouvelle à chaque manche). */
        const val SEED = 20260811L
    }
}
