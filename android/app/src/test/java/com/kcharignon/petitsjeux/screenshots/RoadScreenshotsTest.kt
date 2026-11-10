package com.kcharignon.petitsjeux.screenshots

import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.geometry.Offset
import com.github.takahirom.roborazzi.captureRoboImage
import com.kcharignon.petitsjeux.core.Feedback
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.LockLandscape
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.Screen
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.InMemoryProfileStore
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.road.RoadProgress
import com.kcharignon.petitsjeux.games.road.engine.HopDir
import com.kcharignon.petitsjeux.games.road.engine.Grain
import com.kcharignon.petitsjeux.games.road.engine.Planner
import com.kcharignon.petitsjeux.games.road.engine.Road
import com.kcharignon.petitsjeux.games.road.engine.RoadLevel
import com.kcharignon.petitsjeux.games.road.engine.RoadLevels
import com.kcharignon.petitsjeux.games.road.engine.Row
import com.kcharignon.petitsjeux.games.road.engine.RowKind
import com.kcharignon.petitsjeux.games.road.engine.Slot
import com.kcharignon.petitsjeux.games.road.engine.Stream
import com.kcharignon.petitsjeux.games.road.engine.VehicleKind
import com.kcharignon.petitsjeux.games.road.ui.RoadMenuScreen
import com.kcharignon.petitsjeux.games.road.ui.RoadPlayScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Les écrans de Poussin Traverse en PNG (docs/screenshots, toujours en paysage), et de vraies traversées jouées en touchant l'écran. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w844dp-h390dp-land-xhdpi")
class RoadScreenshotsTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    /** Le temps de jeu de l'écran, à une image (16 ms) près. */
    private var ui = 0L

    private fun show(name: String?, advanceMs: Long = 600, content: @Composable () -> Unit) {
        rule.mainClock.autoAdvance = false
        rule.setContent { content() }
        rule.mainClock.advanceTimeBy(advanceMs)
        if (name != null) shot(name)
    }

    private fun shot(name: String) = rule.onRoot().captureRoboImage("../../docs/screenshots/$name.png")

    /** Fait avancer le jeu de [ms] (image par image). */
    private fun play(ms: Long) {
        var left = ms
        while (left > 0) {
            rule.mainClock.advanceTimeByFrame()
            ui += 16
            left -= 16
        }
    }

    private fun playUntil(ms: Long) {
        if (ui < ms) play(ms - ui)
    }

    private fun start() {
        rule.onNodeWithText("C'est parti !").performClick()
        play(48)
        ui = 0
    }

    private fun tapWorld() = rule.onNodeWithTag("world").performTouchInput { click(center) }
    /** Glisse le doigt de 120 px dans le sens [dir] à partir du milieu de l'écran. */
    private fun glide(dir: HopDir, from: Offset? = null) = rule.onNodeWithTag("world").performTouchInput {
        val start = from ?: center
        val end = when (dir) {
            HopDir.FORWARD -> Offset(start.x, start.y - 120f)
            HopDir.BACK -> Offset(start.x, start.y + 120f)
            HopDir.LEFT -> Offset(start.x - 120f, start.y)
            HopDir.RIGHT -> Offset(start.x + 120f, start.y)
        }
        swipe(start, end, durationMillis = 80)
    }

    /** Le geste du joueur pour sauter dans le sens [dir] : toucher pour avancer, glisser pour le reste. */
    private fun move(dir: HopDir) = if (dir == HopDir.FORWARD) tapWorld() else glide(dir)

    private fun text(tag: String, unmerged: Boolean = false): String =
        rule.onNodeWithTag(tag, useUnmergedTree = unmerged).fetchSemanticsNode().config.getOrNull(SemanticsProperties.Text)?.joinToString("") ?: ""

    private fun chick(): String = rule.onNodeWithTag("chick").fetchSemanticsNode().config.getOrNull(SemanticsProperties.StateDescription) ?: ""

    private fun finished(): Boolean = rule.onAllNodesWithTag("final-score", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    /** Une route où rien ne passe jamais pendant les tests. */
    private fun emptyRoad() = Row(RowKind.ROAD, Stream(+1, 0.01, listOf(Slot(VehicleKind.CAR, 0, 100.0)), phase = 50.0))

    /** Une route où un camion est toujours sur le poussin : touche garantie. */
    private fun jam() = Row(RowKind.ROAD, Stream(+1, 1.0, listOf(Slot(VehicleKind.TRUCK, 3, 0.2)), phase = 0.0))

    private fun custom(vararg rows: Row, grains: List<Grain> = emptyList()) =
        RoadLevel(1, listOf(Row(RowKind.START)) + rows.toList() + Row(RowKind.FINISH), grains)

    private val empty = RoadProgress()
    private val someProgress = RoadProgress()
        .withResult(1, 3, 100, 0).withResult(2, 3, 110, 0).withResult(3, 2, 90, 2).withResult(4, 2, 100, 1).withResult(5, 1, 70, 4)
        .withResult(6, 3, 130, 0).withResult(7, 2, 120, 1).withResult(8, 1, 90, 3)

    private fun noop(level: RoadLevel, onFinished: (Int, Int, Int) -> Unit = { _, _, _ -> }, progress: RoadProgress = empty): @Composable () -> Unit =
        { RoadPlayScreen(level, progress, onFinished, {}, {}) }

    // ------------------------------------------------------------------ les écrans

    @Test
    fun theMenuForANewPlayer() = show("50-route-menu") { RoadMenuScreen(empty, onPlay = {}, onBack = {}) }

    @Test
    fun theMenuWithSomeProgress() = show("51-route-menu-progres") { RoadMenuScreen(someProgress, onPlay = {}, onBack = {}) }

    @Test
    fun theIntroExplainsTheGame() {
        show("52-route-intro", content = noop(RoadLevels.level(1)))
        assertTrue(rule.onAllNodesWithText("Niveau 1").fetchSemanticsNodes().isNotEmpty())
        rule.onNodeWithText("2 voies à traverser").assertExists()
        rule.onNodeWithTag("world-news", useUnmergedTree = true).assertExists()
    }

    @Test
    fun theRailwayWorldAnnouncesTheTrain() {
        show("53-route-intro-train", content = noop(RoadLevels.level(31)))
        rule.onNodeWithText("Nouveau : Un train passe : écoute la cloche !").assertExists()
    }

    @Test
    fun theFirstLevelIsAStreetWithTwoLanesGoingOppositeWays() {
        show(null, content = noop(RoadLevels.level(1)))
        start()
        play(900)
        shot("54-route-jeu")
        assertEquals("ligne 0 colonne ${Road.START_COL} en route", chick())
    }

    @Test
    fun aBigRoadWithTrucksBusesAndTheTrain() {
        show(null, content = noop(RoadLevels.level(47)))
        start()
        play(1500)
        shot("55-route-grande-route")
    }

    @Test
    fun theEveningWorldHasHeadlights() {
        show(null, content = noop(RoadLevels.level(38)))
        start()
        play(1200)
        shot("56-route-soir")
    }

    // ------------------------------------------------------------------ les règles, jouées à l'écran

    @Test
    fun touchingHopsForwardAndSwipingGoesTheWayTheFingerGoes() {
        show(null, content = noop(custom(Row(RowKind.REFUGE), emptyRoad(), Row(RowKind.REFUGE))))
        start()
        tapWorld()
        play(300)
        assertEquals("ligne 1 colonne ${Road.START_COL} en route", chick())
        assertEquals("10", text("score"))
        glide(HopDir.LEFT)
        play(300)
        assertEquals("ligne 1 colonne ${Road.START_COL - 1} en route", chick())
        glide(HopDir.RIGHT)
        play(300)
        glide(HopDir.RIGHT)
        play(300)
        assertEquals("ligne 1 colonne ${Road.START_COL + 1} en route", chick())
        glide(HopDir.BACK)
        play(300)
        assertEquals("ligne 0 colonne ${Road.START_COL + 1} en route", chick())
        // revenir en arrière ne rapporte rien
        assertEquals("10", text("score"))
    }

    @Test
    fun theRoadAndTheGesturesReachBothEdgesOfTheScreen() {
        show(null, content = noop(custom(Row(RowKind.REFUGE), Row(RowKind.REFUGE))))
        start()
        val root = rule.onRoot().fetchSemanticsNode().boundsInRoot
        val road = rule.onNodeWithTag("road").fetchSemanticsNode().boundsInRoot
        val world = rule.onNodeWithTag("world").fetchSemanticsNode().boundsInRoot
        assertEquals("la route va jusqu'au bord gauche et au bord droit", root.left, road.left, 0.5f)
        assertEquals(root.right, road.right, 0.5f)
        assertEquals("les gestes aussi", root.left, world.left, 0.5f)
        assertEquals(root.right, world.right, 0.5f)
        // un glissé qui part tout au bord gauche de l'écran saute à droite ; un toucher tout au bord droit avance
        rule.onNodeWithTag("world").performTouchInput { swipe(Offset(1f, centerY), Offset(121f, centerY), durationMillis = 80) }
        play(300)
        assertEquals("ligne 0 colonne ${Road.START_COL + 1} en route", chick())
        rule.onNodeWithTag("world").performTouchInput { click(Offset(width - 1f, centerY)) }
        play(300)
        assertTrue(chick(), chick().startsWith("ligne 1 "))
    }

    @Test
    fun aFingerThatBarelyMovesIsStillATapAndASwipeIsOneHopOnly() {
        show(null, content = noop(custom(Row(RowKind.REFUGE), Row(RowKind.REFUGE), Row(RowKind.REFUGE))))
        start()
        // un doigt qui tremble un peu (8 px, bien moins que le seuil) : c'est un toucher, donc on avance
        rule.onNodeWithTag("world").performTouchInput { swipe(center, center + Offset(8f, 0f), durationMillis = 60) }
        play(300)
        assertEquals("ligne 1 colonne ${Road.START_COL} en route", chick())
        // un long glissé vers la gauche : un seul saut de côté, pas dix
        rule.onNodeWithTag("world").performTouchInput { swipe(Offset(width - 10f, centerY), Offset(20f, centerY), durationMillis = 400) }
        play(800)
        assertEquals("ligne 1 colonne ${Road.START_COL - 1} en route", chick())
    }

    @Test
    fun aTouchDuringAHopIsNotLost() {
        show(null, content = noop(custom(Row(RowKind.REFUGE), Row(RowKind.REFUGE), Row(RowKind.REFUGE))))
        start()
        tapWorld()
        play(48)
        tapWorld() // pendant le saut : mis en attente, joué à l'atterrissage
        play(500)
        assertTrue(chick(), chick().startsWith("ligne 2 "))
    }

    @Test
    fun beingRunOverCostsPointsAndTheChickComesBackWithoutLosingTheGame() {
        var calls = 0
        show(null, content = noop(custom(Row(RowKind.REFUGE), jam()), onFinished = { _, _, _ -> calls++ }))
        start()
        tapWorld() // sur le refuge : 10 points
        play(300)
        assertEquals("10", text("score"))
        tapWorld() // dans l'embouteillage : écrasé
        play(300)
        assertTrue(chick(), chick().endsWith("écrasé"))
        assertEquals("0", text("score"))
        shot("57-route-ecrase")
        // quelques instants plus tard : de retour sur le refuge, la partie continue
        play(1000)
        assertEquals("ligne 1 colonne ${Road.START_COL} en route", chick())
        assertEquals(0, calls)
        rule.onNodeWithTag("world").assertExists()
    }

    @Test
    fun aGrainIsTakenAndCountedAndAPerfectCrossingGivesThreeStars() {
        var result: Triple<Int, Int, Int>? = null
        val level = custom(Row(RowKind.REFUGE), emptyRoad(), grains = listOf(Grain(0, 2, Road.START_COL)))
        show(null, content = noop(level, onFinished = { s, st, h -> result = Triple(s, st, h) }))
        start()
        assertEquals("0/1", text("grains"))
        repeat(3) {
            tapWorld()
            play(260)
        }
        assertEquals("1/1", text("grains"))
        play(1800)
        assertTrue("la fenêtre de fin", finished())
        assertEquals(Triple(10 * 3 + 10 + 50, 3, 0), result)
        assertEquals("90", text("final-score", unmerged = true))
        assertEquals("Jamais écrasé !", text("final-hits", unmerged = true))
        play(1500) // les trois étoiles s'allument une à une
        shot("58-route-fin")
    }

    @Test
    fun theRobotsPerfectPathPlayedByTouchingTheScreenFinishesLevelOne() {
        var result: Triple<Int, Int, Int>? = null
        var calls = 0
        val level = RoadLevels.level(1)
        show(null, content = noop(level, onFinished = { s, st, h -> result = Triple(s, st, h); calls++ }))
        start()
        val moves = Planner(level).route(level.grains)!!
        for (m in moves) {
            playUntil(m.tick.toLong() * Planner.TICK_MS)
            move(m.dir)
        }
        play(3000)
        assertTrue("la fenêtre de fin", finished())
        assertEquals("une seule sauvegarde", 1, calls)
        val (score, stars, hits) = result!!
        assertEquals("jamais écrasé", 0, hits)
        assertEquals(level.maxScore, score)
        assertEquals(3, stars)
    }

    // ------------------------------------------------------------------ l'appli complète et l'orientation

    private class QuietFeedback : Feedback {
        val horns = mutableListOf<Int>()
        var hops = 0
        val picks = mutableListOf<Int>()
        override fun exit() = Unit
        override fun bump(vehicleLength: Int) { horns += vehicleLength }
        override fun win() = Unit
        override fun tap() = Unit
        override fun star(index: Int) = Unit
        override fun hint() = Unit
        override fun pick(index: Int) { picks += index }
        override fun finalPick() = Unit
        override fun puff() = Unit
        override fun bonk() = Unit
        override fun hop() { hops++ }
        override fun bell() = Unit
    }

    @Test
    fun hopsGrainsAndHornsMakeTheirSounds() {
        val feedback = QuietFeedback()
        val level = custom(Row(RowKind.REFUGE), emptyRoad(), jam(), grains = listOf(Grain(0, 2, Road.START_COL)))
        show(null) { CompositionLocalProvider(LocalFeedback provides feedback) { RoadPlayScreen(level, empty, { _, _, _ -> }, {}, {}) } }
        start()
        repeat(3) {
            tapWorld()
            play(260)
        }
        assertEquals(3, feedback.hops)
        assertEquals("une graine : la première note de la mélodie", listOf(0), feedback.picks)
        assertEquals("un camion écrase le poussin : klaxon de camion", listOf(3), feedback.horns)
    }

/**
     * Fait défiler la liste des mondes jusqu'au niveau [levelTag]. L'horloge du test est rendue automatique le temps du défilement (le menu n'a aucune animation infinie :
     * l'attente d'inactivité de `performScrollToNode` ne finirait jamais avec une horloge arrêtée à la main), puis arrêtée de nouveau.
     */
    private fun scrollListTo(listTag: String, levelTag: String) {
        rule.mainClock.autoAdvance = true
        rule.onNodeWithTag(listTag).performScrollToNode(hasTestTag(levelTag))
        rule.waitForIdle()
        rule.mainClock.autoAdvance = false
    }

    @Test
    fun anyLevelCanBeOpenedFromTheStartEvenTheTrainWorld() {
        // tous les niveaux sont jouables : on saute au niveau 31 (le premier du passage à niveau) sans avoir fini les précédents
        val store = InMemoryProfileStore(listOf(Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!))
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, "road")) }
        rule.mainClock.advanceTimeBy(300)
        // le jeu s'ouvre sur le niveau 1 : la flèche de retour (même pendant que la route se prépare) ramène à la grille
        rule.runOnUiThread { rule.activity.onBackPressedDispatcher.onBackPressed() }
        rule.mainClock.advanceTimeBy(300)
        scrollListTo("worlds", "level-31")
        rule.mainClock.advanceTimeBy(100)
        rule.onNodeWithTag("level-31").performClick()
        var guard = 0
        while (rule.onAllNodesWithTag("road").fetchSemanticsNodes().isEmpty() && guard++ < 200) {
            Thread.sleep(25)
            rule.mainClock.advanceTimeByFrame()
        }
        assertTrue("la route est prête", guard < 200)
        rule.onNodeWithText("Nouveau : Un train passe : écoute la cloche !").assertExists()
        start()
        play(300)
        tapWorld()
        play(300)
        // et ce niveau-là se joue et se sauvegarde comme les autres : ici on s'arrête, la partie est vérifiée plus haut
        assertTrue(chick().startsWith("ligne "))
    }

    @Test
    fun theGameLocksLandscapeAndGivesPortraitBack() {
        // ici rien n'est animé : l'horloge automatique du test suffit
        var inGame by mutableStateOf(false)
        rule.setContent { if (inGame) LockLandscape() }
        rule.waitForIdle()
        val before = rule.activity.requestedOrientation
        rule.runOnIdle { inGame = true }
        rule.waitForIdle()
        assertEquals(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, rule.activity.requestedOrientation)
        rule.runOnIdle { inGame = false }
        rule.waitForIdle()
        assertEquals("l'orientation d'avant est remise", before, rule.activity.requestedOrientation)
    }

    @Test
    fun fromTheAppTheRoadGameIsLandscapeAndSavesTheCrossing() {
        val player = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!
        val store = InMemoryProfileStore(listOf(player))
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, "road")) }
        rule.mainClock.advanceTimeBy(300)
        assertEquals(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, rule.activity.requestedOrientation)
        // le jeu s'ouvre tout de suite sur le niveau 1 : pas de grille de niveaux à parcourir
        assertTrue(rule.onAllNodesWithTag("level-1").fetchSemanticsNodes().isEmpty())
        // la route se prépare hors du fil principal : on laisse un peu de temps
        var guard = 0
        while (rule.onAllNodesWithTag("road").fetchSemanticsNodes().isEmpty() && guard++ < 200) {
            Thread.sleep(25)
            rule.mainClock.advanceTimeByFrame()
        }
        assertTrue("la route est prête", guard < 200)
        rule.onNodeWithText("C'est parti !").assertExists()
        start()
        val level = RoadLevels.level(1)
        for (m in Planner(level).route(level.grains)!!) {
            playUntil(m.tick.toLong() * Planner.TICK_MS)
            move(m.dir)
        }
        play(3000)
        assertTrue(finished())
        val saved = RoadProgress.fromJson(store.load(0)!!.gameSave(RoadProgress.GAME_ID))
        assertEquals(3, saved.starsOf(1))
        assertEquals(level.maxScore, saved.bestOf(1))
        assertEquals(1, saved.crossings)
        assertNotNull(store.load(0))
        // « Niveau suivant » ouvre le niveau 2
        rule.onNodeWithText("Niveau suivant").performClick()
        guard = 0
        while (rule.onAllNodesWithText("Niveau 2").fetchSemanticsNodes().isEmpty() && guard++ < 200) {
            Thread.sleep(25)
            rule.mainClock.advanceTimeByFrame()
        }
        assertTrue(rule.onAllNodesWithText("Niveau 2").fetchSemanticsNodes().isNotEmpty())
    }
}
