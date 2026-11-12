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
import androidx.compose.ui.geometry.Offset
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
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
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
import com.kcharignon.petitsjeux.games.skate.SkateMiniGame
import com.kcharignon.petitsjeux.games.skate.SkateProgress
import com.kcharignon.petitsjeux.games.skate.engine.PathPoint
import com.kcharignon.petitsjeux.games.skate.engine.Piece
import com.kcharignon.petitsjeux.games.skate.engine.PieceKind
import com.kcharignon.petitsjeux.games.skate.engine.SkateDifficulty
import com.kcharignon.petitsjeux.games.skate.engine.SkateRun
import com.kcharignon.petitsjeux.games.skate.engine.SkateWorld
import com.kcharignon.petitsjeux.games.skate.ui.SkateHelmets
import com.kcharignon.petitsjeux.games.skate.ui.SkateMenuScreen
import com.kcharignon.petitsjeux.games.skate.ui.SkatePlayScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.math.abs

/** Les écrans de Super Skate en PNG (docs/screenshots, en portrait), et de vraies courses jouées du bout du doigt. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w390dp-h844dp-port-xhdpi")
class SkateScreenshotsTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val helmet = SkateHelmets[0]

    private fun show(name: String?, advanceMs: Long = 600, content: @Composable () -> Unit) {
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

    /** Une rue courte, avec seulement [pieces] dedans. */
    private fun street(vararg pieces: Piece, length: Float = 80f, theme: Int = 0, difficulty: SkateDifficulty = SkateDifficulty.EASY) =
        SkateWorld(difficulty, 1, pieces.sortedBy { it.z }, listOf(PathPoint(0f, 0f)), theme, length)

    private class Capture {
        var run: SkateRun? = null
        val finished = ArrayList<List<Int>>()
        val log = ArrayList<String>()
        val next = ArrayList<SkateDifficulty>()
        var menu = 0
        var exit = 0
    }

    private fun screen(
        d: SkateDifficulty = SkateDifficulty.EASY,
        world: SkateWorld? = null,
        progress: SkateProgress = SkateProgress(),
        capture: Capture = Capture(),
        startingRun: SkateRun? = null,
        seed: Long = SEED,
    ): @Composable () -> Unit = {
        val feedback = object : Feedback by NoFeedback {
            override fun crash() { capture.log += "crash" }
            override fun jump() { capture.log += "jump" }
            override fun land() { capture.log += "land" }
            override fun win() { capture.log += "win" }
            override fun star(index: Int) { capture.log += "star$index" }
        }
        CompositionLocalProvider(LocalFeedback provides feedback) {
            SkatePlayScreen(
                d, progress, helmet,
                onFinished = { stars, score, hits, jumps -> capture.finished += listOf(stars, score, hits, jumps) },
                onNextDifficulty = { capture.next += it },
                onMenu = { capture.menu++ },
                onExit = { capture.exit++ },
                seed = seed, world = world, onRun = { capture.run = it }, startingRun = startingRun,
            )
        }
    }

    private fun start() {
        rule.onNodeWithText("C'est parti !").performClick()
        play(32)
    }

    private fun text(tag: String, unmerged: Boolean = false): String =
        rule.onNodeWithTag(tag, useUnmergedTree = unmerged).fetchSemanticsNode().config.getOrNull(SemanticsProperties.Text)?.joinToString("") ?: ""

    /** Un texte contourné est dessiné deux fois (le liseré puis les lettres) : on cherche s'il y en a au moins un. */
    private fun hasText(text: String) = rule.onAllNodesWithText(text, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    private fun lives(): Int = rule.onNodeWithTag("lives").fetchSemanticsNode().config.getOrNull(SemanticsProperties.StateDescription)?.toInt() ?: -1

    private fun ended(): Boolean = rule.onAllNodesWithTag("final-stars", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    private fun roadWidthPx(): Float = rule.onNodeWithTag("road").fetchSemanticsNode().size.width.toFloat()

    private fun playUntilTheEnd(maxMs: Long = 120_000) {
        var left = maxMs
        while (!ended() && left > 0) {
            play(160)
            left -= 160
        }
        assertTrue("la course finit", ended())
    }

    /** Glisse le doigt de [dx] pixels (vers la droite si positif), à partir du bas de l'écran, comme un enfant. */
    private fun glide(dx: Float) = rule.onNodeWithTag("road").performTouchInput {
        val start = Offset(width / 2f, height * 0.8f)
        swipe(start, start + Offset(dx, 0f), durationMillis = 96)
    }

    /** Une course avancée par le robot du couloir jusqu'à la distance [dist] (sans aucun choc), que l'écran reprend ensuite. */
    private fun advancedRun(world: SkateWorld, dist: Float): SkateRun {
        val run = SkateRun(world)
        var guard = 0
        while (run.dist < dist && guard++ < 100_000) {
            run.steerTo(world.pathAt(run.dist + 0.1f * run.speed))
            run.update(1f / 60f)
        }
        assertEquals(0, run.hits)
        return run
    }

    private fun worldWithTheme(d: SkateDifficulty, theme: Int): SkateWorld = (0L until 200L).map { SkateWorld.generate(d, it) }.first { it.theme == theme }

    // ------------------------------------------------------------------ les écrans

    private val someProgress = SkateProgress()
        .withResult(SkateDifficulty.EASY, 3, 640, 0, 2)
        .withResult(SkateDifficulty.MEDIUM, 2, 520, 1, 1)

    @Test
    fun theMenuForANewPlayer() = show("90-skate-menu") {
        SkateMenuScreen(SkateProgress(), helmet, onPlay = {}, onBack = {})
    }

    @Test
    fun theMenuWithSomeProgress() = show("91-skate-menu-progres") {
        SkateMenuScreen(someProgress, helmet, onPlay = {}, onBack = {})
    }

    @Test
    fun theIntroExplainsTheGame() {
        show("92-skate-intro", content = screen())
        rule.onNodeWithText("Glisse ton doigt à gauche et à droite pour éviter les obstacles !").assertExists()
        rule.onNodeWithText("Un choc te fait perdre une étoile, mais tu ne perds jamais.").assertExists()
        rule.onNodeWithText("Prends une rampe pour sauter : tu gagnes des points et une étoile !").assertExists()
        assertTrue("pas de piétons qui traversent en Petite balade", rule.onAllNodesWithText("Attention : des piétons traversent la rue.").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theHarderIntroWarnsAboutCrossingPedestrians() {
        show(null, content = screen(SkateDifficulty.MEDIUM))
        rule.onNodeWithText("Attention : des piétons traversent la rue.").assertExists()
        rule.onNodeWithText("Bon rythme").assertExists()
    }

    @Test
    fun theHubCardShowsTheStreetARampAndAPedestrian() = show("93-skate-carte") {
        Box(Modifier.background(Color(0xFFFFB020)).padding(20.dp)) {
            SkateMiniGame.Illustration(Modifier.size(width = 330.dp, height = 170.dp))
        }
    }

    /** Une course déjà bien avancée dans le monde numéro [theme] (l'écran la reprend où le robot l'a laissée). */
    private fun theWorld(name: String, theme: Int, d: SkateDifficulty = SkateDifficulty.HARD, dist: Float = 330f) {
        val world = worldWithTheme(d, theme)
        show(name, content = screen(d, world = world, startingRun = advancedRun(world, dist)))
        assertTrue(rule.onAllNodesWithTag("road").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun theCityWorld() = theWorld("94-skate-ville", 0)

    @Test
    fun theParkWorld() = theWorld("95-skate-parc", 1)

    @Test
    fun theSeasideWorld() = theWorld("96-skate-bord-de-mer", 2)

    @Test
    fun theNightWorld() = theWorld("97-skate-nuit", 3, SkateDifficulty.EXPERT, 400f)

    @Test
    fun aHitShowsTheBurstTheLostStarAndAnOuch() {
        val capture = Capture()
        show(null, content = screen(world = street(Piece(0, PieceKind.PEDESTRIAN, 28f, 0f, 1), Piece(1, PieceKind.LAMP, 60f, 0.6f, 0)), capture = capture))
        start()
        assertEquals(3, lives())
        var guard = 0
        while (capture.run!!.hits == 0 && guard++ < 600) play(16)
        play(96)
        assertEquals(1, capture.run!!.hits)
        assertEquals(2, lives())
        assertTrue("Aïe !", hasText("Aïe !"))
        assertEquals(listOf("crash"), capture.log)
        shot("98-skate-choc")
    }

    @Test
    fun aRampMakesTheSkaterFlyAndGivesBackAStar() {
        val capture = Capture()
        // un plot qui coûte une étoile, puis une rampe en plein milieu : le skateur immobile la prend
        show(null, content = screen(world = street(Piece(0, PieceKind.CONE, 26f, 0f, 0), Piece(1, PieceKind.RAMP, 56f, 0f, 0), Piece(2, PieceKind.PEDESTRIAN, 62.5f, 0f, 2), length = 130f), capture = capture))
        start()
        var guard = 0
        while (capture.run!!.jumps == 0 && guard++ < 1500) play(16)
        assertEquals(1, capture.run!!.jumps)
        assertEquals("une étoile rendue", 3, lives())
        play(260)
        assertTrue("en l'air", capture.run!!.airborne)
        assertTrue("Super saut", hasText("Super saut ! +50\nUne étoile en plus !"))
        shot("99-skate-saut")
        play(1200)
        assertEquals("on a survolé le piéton", 1, capture.run!!.hits)
        assertEquals(listOf("crash", "jump", "land"), capture.log)
    }

    @Test
    fun theFinishArchEndsTheRaceAndTheSkaterRollsToAStop() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 70f), capture = capture))
        start()
        var guard = 0
        while (capture.run!!.world.length - capture.run!!.dist > 14f && guard++ < 2000) play(16)
        shot("100-skate-arrivee")
        playUntilTheEnd()
        assertEquals(1, capture.finished.size)
        assertEquals(listOf(3, capture.run!!.score, 0, 0), capture.finished.single())
    }

    @Test
    fun theThreeStarsEndCardUnlocksTheNextDifficultyAndOffersIt() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 60f), capture = capture))
        start()
        playUntilTheEnd()
        play(1700)
        shot("101-skate-bravo")
        rule.onNodeWithText("Aucun choc : parfait !").assertExists()
        assertTrue(rule.onAllNodesWithTag("unlocked", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty())
        rule.onNodeWithText("Rejouer").assertExists()
        rule.onNodeWithText("Difficultés").assertExists()
        rule.onNodeWithText("Difficulté suivante").performClick()
        assertEquals(listOf(SkateDifficulty.MEDIUM), capture.next)
        assertEquals(listOf("win", "star0", "star1", "star2"), capture.log)
    }

    @Test
    fun withOnlyOneStarTheNextDifficultyStaysClosedAndIsNotOffered() {
        val capture = Capture()
        // deux plots en plein milieu : deux chocs, donc une seule étoile
        show(null, content = screen(world = street(Piece(0, PieceKind.CONE, 26f, 0f, 0), Piece(1, PieceKind.BIN, 50f, 0f, 0), length = 80f), capture = capture))
        start()
        playUntilTheEnd()
        play(1500)
        shot("102-skate-bravo-une-etoile")
        assertEquals(2, capture.run!!.hits)
        assertEquals(1, capture.finished.single()[0])
        assertEquals(2, capture.finished.single()[2])
        assertEquals(0, capture.finished.single()[3])
        assertTrue(rule.onAllNodesWithText("Difficulté suivante").fetchSemanticsNodes().isEmpty())
        assertTrue(rule.onAllNodesWithTag("unlocked", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
        rule.onNodeWithText("Prends les rampes : elles rendent des étoiles !").assertExists()
    }

    @Test
    fun theNextDifficultyIsOfferedWhenAlreadyOpenEvenAfterABadRun() {
        val open = SkateProgress().withResult(SkateDifficulty.EASY, 2, 400, 1, 0)
        show(null, content = screen(world = street(Piece(0, PieceKind.CONE, 26f, 0f, 0), Piece(1, PieceKind.BIN, 50f, 0f, 0), length = 80f), progress = open))
        start()
        playUntilTheEnd()
        play(1500)
        rule.onNodeWithText("Difficulté suivante").assertExists()
        assertTrue("ce n'est pas une nouveauté", rule.onAllNodesWithTag("unlocked", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theGreenButtonReplaysWithANewStreetAndNoIntro() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 60f), capture = capture))
        start()
        playUntilTheEnd()
        play(1500)
        val first = capture.run
        rule.onNodeWithText("Rejouer").performClick()
        play(64)
        assertNotEquals(first, capture.run)
        assertTrue("on repart tout de suite, sans l'introduction", rule.onAllNodesWithText("C'est parti !").fetchSemanticsNodes().isEmpty())
        assertEquals(3, lives())
        assertTrue(capture.run!!.dist >= 0f)
    }

    @Test
    fun theLevelsButtonAndTheArrowCallTheirHandlers() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 60f), capture = capture))
        start()
        rule.onNodeWithTag("levels").performClick()
        assertEquals(1, capture.menu)
        rule.onNodeWithTag("exit").performClick()
        assertEquals(1, capture.exit)
    }

    // ------------------------------------------------------------------ le doigt

    @Test
    fun theSkaterRollsOnItsOwnAndTheProgressBarGrows() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 400f), capture = capture))
        start()
        play(1500)
        val run = capture.run!!
        assertTrue("il roule (${run.dist} m)", run.dist > 3f)
        assertTrue(run.speed > 1f)
        rule.onNodeWithTag("progress").assertExists()
        assertEquals("0", text("score"))
        shot("103-skate-course")
    }

    @Test
    fun theSkaterIntroDoesNotRollBeforeTheStartButton() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 400f), capture = capture))
        play(800)
        assertEquals(0f, capture.run!!.dist, 0.0001f)
    }

    @Test
    fun slidingTheFingerMovesTheSkaterOneToOneAtItsDepth() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 400f), capture = capture))
        start()
        val unit = 0.46f * roadWidthPx()
        glide(0.2f * roadWidthPx())
        play(200)
        val right = capture.run!!.targetX
        assertEquals("0,2 largeur d'écran de glissé = ${0.2f * roadWidthPx() / unit} unités de rue", 0.2f * roadWidthPx() / unit, right, 0.04f)
        assertTrue("le skateur suit", abs(capture.run!!.x - right) < 0.08f)
        glide(-0.3f * roadWidthPx())
        play(200)
        assertEquals(right - 0.3f * roadWidthPx() / unit, capture.run!!.targetX, 0.04f)
        assertTrue(capture.run!!.x < right)
    }

    @Test
    fun theSkaterNeverLeavesTheStreetEvenWithAHugeSlide() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 400f), capture = capture))
        start()
        glide(roadWidthPx() * 0.45f)
        glide(-roadWidthPx() * 0.45f)
        glide(-roadWidthPx() * 0.45f)
        play(300)
        assertEquals(-SkateRun.MAX_X, capture.run!!.targetX, 0.001f)
        assertTrue(capture.run!!.x >= -SkateRun.MAX_X - 0.001f)
    }

    @Test
    fun theFingerDoesNothingWhileTheIntroIsShown() {
        val capture = Capture()
        show(null, content = screen(world = street(length = 400f), capture = capture))
        glide(0.2f * roadWidthPx())
        play(100)
        assertEquals(0f, capture.run!!.targetX, 0.0001f)
    }

    @Test
    fun aMotionlessSkaterGetsHitButAlwaysFinishesAndSavesAtLeastOneStar() {
        val capture = Capture()
        val pieces = Array(6) { Piece(it, PieceKind.CONE, 26f + it * 20f, 0f, 0) }
        show(null, content = screen(world = street(*pieces, length = 170f), capture = capture))
        start()
        playUntilTheEnd()
        assertTrue(capture.run!!.hits >= 3)
        assertEquals(1, capture.finished.size)
        assertEquals(1, capture.finished.single()[0])
        assertEquals(capture.run!!.hits, capture.finished.single()[2])
        play(1700)
        rule.onNodeWithText("Rejouer").assertExists()
        assertEquals(1, capture.log.count { it == "star0" })
        assertEquals(0, capture.log.count { it == "star1" })
    }

    @Test
    fun theStarsInTheTopBarGoDownWithEachHitAndStopAtZero() {
        val capture = Capture()
        val pieces = Array(6) { Piece(it, PieceKind.CONE, 26f + it * 20f, 0f, 0) }
        show(null, content = screen(world = street(*pieces, length = 170f), capture = capture))
        start()
        val seen = HashSet<Int>()
        var guard = 0
        while (!ended() && guard++ < 4000) {
            play(32)
            seen += lives()
        }
        assertEquals(setOf(3, 2, 1, 0), seen)
    }

    @Test
    fun aRealGeneratedRunCanBeWonWithTheFingerAlone() {
        // le doigt suit le couloir libre, image par image, comme un très bon joueur : aucun choc, trois étoiles
        val world = SkateWorld.generate(SkateDifficulty.EASY, SEED)
        val capture = Capture()
        show(null, content = screen(world = world, capture = capture))
        start()
        val unit = 0.46f * roadWidthPx()
        var guard = 0
        while (!ended() && guard++ < 6000) {
            val run = capture.run!!
            if (run.finished) {
                play(160)
                continue
            }
            val error = world.pathAt(run.dist + 0.1f * run.speed) - run.targetX
            if (abs(error) > 0.02f) {
                rule.onNodeWithTag("road").performTouchInput {
                    val start = Offset(width / 2f, height * 0.8f)
                    down(start)
                    moveBy(Offset(error * unit, 0f))
                    up()
                }
            }
            play(16)
        }
        assertTrue("la course finit", ended())
        assertEquals("aucun choc", 0, capture.run!!.hits)
        assertEquals(3, lives())
        assertEquals(3, capture.finished.single()[0])
        assertTrue("des points d'obstacles évités et de rampes", capture.run!!.score > 200)
    }

    // ------------------------------------------------------------------ depuis l'appli

    private fun profile(progress: SkateProgress?): Profile {
        val base = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!
        return if (progress == null) base else base.withGameSave(SkateProgress.GAME_ID, progress.toJson())
    }

    private fun openGame(store: InMemoryProfileStore) {
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, SkateProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(600)
    }

    @Test
    fun openingTheGameLandsOnTheFirstDifficultyForANewPlayer() {
        openGame(InMemoryProfileStore(listOf(profile(null))))
        rule.onNodeWithText("Petite balade").assertExists()
        rule.onNodeWithText("C'est parti !").assertExists()
        assertTrue(rule.onAllNodesWithTag("difficulty-0").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun openingTheGameLandsOnTheHighestDifficultyAlreadyPlayedThenTheButtonShowsTheChoice() {
        openGame(InMemoryProfileStore(listOf(profile(someProgress))))
        rule.onNodeWithText("Bon rythme").assertExists()
        // l'introduction propose de changer de difficulté ; une fois la course lancée, c'est le bouton en grille de tuiles
        rule.onNodeWithText("C'est parti !").performClick()
        play(100)
        rule.onNodeWithTag("levels").performClick()
        play(300)
        rule.onNodeWithTag("difficulty-0").assertIsEnabled()
        rule.onNodeWithTag("difficulty-1").assertIsEnabled()
        rule.onNodeWithTag("difficulty-2").assertIsEnabled()
        rule.onNodeWithTag("difficulty-3").assertIsNotEnabled()
        rule.onNodeWithTag("difficulty-0").performClick()
        play(300)
        rule.onNodeWithText("Petite balade").assertExists()
    }

    @Test
    fun theBlueArrowGoesHomeAndTheHomeShowsTheGame() {
        openGame(InMemoryProfileStore(listOf(profile(null))))
        rule.onNodeWithTag("exit").performClick()
        play(300)
        assertTrue("à l'accueil", rule.onAllNodesWithText("Parking Malin").fetchSemanticsNodes().isNotEmpty())
        assertTrue(rule.onAllNodesWithTag("road").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theSystemBackGestureGoesHomeToo() {
        openGame(InMemoryProfileStore(listOf(profile(null))))
        rule.runOnUiThread {
            rule.activity.onBackPressedDispatcher.onBackPressed()
            Snapshot.sendApplyNotifications()
        }
        play(300)
        assertTrue(rule.onAllNodesWithText("Parking Malin").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun theHubCardShowsTheStarsAndTheProgress() {
        assertEquals(5, SkateMiniGame.stars(someProgress.toJson()))
        assertEquals("Record 640", SkateMiniGame.progressLabel(someProgress.toJson()))
        assertEquals("Nouveau !", SkateMiniGame.progressLabel(null))
        assertEquals("Super Skate", SkateMiniGame.title)
        assertEquals(SkateProgress.GAME_ID, SkateMiniGame.id)
        assertNotNull(SkateMiniGame.tagline)
    }

    @Test
    fun theMenuLocksTheDifficultiesNotYetEarned() {
        show(null) { SkateMenuScreen(SkateProgress().withResult(SkateDifficulty.EASY, 1, 100, 3, 0), helmet, onPlay = {}, onBack = {}) }
        rule.onNodeWithTag("difficulty-0").assertIsEnabled()
        rule.onNodeWithTag("difficulty-1").assertIsNotEnabled()
        rule.onNodeWithTag("difficulty-3").assertIsNotEnabled()
        rule.onNodeWithText("Finis « Petite balade » avec 2 étoiles pour débloquer.").assertExists()
    }

    @Test
    fun theSoundsAreCrashJumpAndLand() {
        val capture = Capture()
        show(null, content = screen(world = street(Piece(0, PieceKind.CONE, 26f, 0f, 0), Piece(1, PieceKind.RAMP, 60f, 0f, 0), length = 120f), capture = capture))
        start()
        playUntilTheEnd()
        assertEquals(listOf("crash", "jump", "land", "win"), capture.log.filter { !it.startsWith("star") })
    }

    companion object {
        /** La graine des courses des tests (le jeu, lui, en tire une nouvelle à chaque course). */
        const val SEED = 20260812L
    }
}
