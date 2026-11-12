package com.kcharignon.petitsjeux.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.kcharignon.petitsjeux.games.plane.PlaneMiniGame
import com.kcharignon.petitsjeux.games.plane.PlaneProgress
import com.kcharignon.petitsjeux.games.plane.engine.PathPoint
import com.kcharignon.petitsjeux.games.plane.engine.Piece
import com.kcharignon.petitsjeux.games.plane.engine.PieceKind
import com.kcharignon.petitsjeux.games.plane.engine.PlaneDifficulty
import com.kcharignon.petitsjeux.games.plane.engine.PlaneRun
import com.kcharignon.petitsjeux.games.plane.engine.PlaneWorld
import com.kcharignon.petitsjeux.games.plane.ui.PlaneColors
import com.kcharignon.petitsjeux.games.plane.ui.PlaneMenuScreen
import com.kcharignon.petitsjeux.games.plane.ui.PlanePlayScreen
import com.kcharignon.petitsjeux.games.plane.ui.drawBalloon
import com.kcharignon.petitsjeux.games.plane.ui.drawBirds
import com.kcharignon.petitsjeux.games.plane.ui.drawBoat
import com.kcharignon.petitsjeux.games.plane.ui.drawCactus
import com.kcharignon.petitsjeux.games.plane.ui.drawCloud
import com.kcharignon.petitsjeux.games.plane.ui.drawControlTower
import com.kcharignon.petitsjeux.games.plane.ui.drawGroundHouse
import com.kcharignon.petitsjeux.games.plane.ui.drawHangar
import com.kcharignon.petitsjeux.games.plane.ui.drawIsland
import com.kcharignon.petitsjeux.games.plane.ui.drawMesa
import com.kcharignon.petitsjeux.games.plane.ui.drawOncomingPlane
import com.kcharignon.petitsjeux.games.plane.ui.drawParaglider
import com.kcharignon.petitsjeux.games.plane.ui.drawParkedPlane
import com.kcharignon.petitsjeux.games.plane.ui.drawPlayerPlane
import com.kcharignon.petitsjeux.games.plane.ui.drawRing
import com.kcharignon.petitsjeux.games.plane.ui.drawTowerBlock
import com.kcharignon.petitsjeux.games.plane.ui.drawWindmill
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

/** Les écrans de Super Avion en PNG (docs/screenshots, en portrait), et de vrais vols pilotés du bout du doigt. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w390dp-h844dp-port-xhdpi")
class PlaneScreenshotsTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val color = PlaneColors[0]

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

    /** Un ciel de [length] mètres avec seulement [pieces] dedans (le couloir libre passe par la voie du milieu, ou par [path]). */
    private fun sky(vararg pieces: Piece, length: Float = 240f, theme: Int = 0, difficulty: PlaneDifficulty = PlaneDifficulty.EASY, path: List<PathPoint> = listOf(PathPoint(0f, 0f))) =
        PlaneWorld(difficulty, 1, pieces.sortedBy { it.z }, path, theme, length)

    private class Capture {
        var run: PlaneRun? = null
        val finished = ArrayList<List<Int>>()
        val log = ArrayList<String>()
        val next = ArrayList<PlaneDifficulty>()
        var menu = 0
        var exit = 0
    }

    private fun screen(
        d: PlaneDifficulty = PlaneDifficulty.EASY,
        world: PlaneWorld? = null,
        progress: PlaneProgress = PlaneProgress(),
        capture: Capture = Capture(),
        startingRun: PlaneRun? = null,
        seed: Long = SEED,
    ): @Composable () -> Unit = {
        val feedback = object : Feedback by NoFeedback {
            override fun crash() { capture.log += "crash" }
            override fun roll() { capture.log += "roll" }
            override fun land() { capture.log += "land" }
            override fun win() { capture.log += "win" }
            override fun star(index: Int) { capture.log += "star$index" }
        }
        CompositionLocalProvider(LocalFeedback provides feedback) {
            PlanePlayScreen(
                d, progress, color,
                onFinished = { stars, score, hits, rolls -> capture.finished += listOf(stars, score, hits, rolls) },
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

    private fun playUntilTheEnd(maxMs: Long = 150_000) {
        var left = maxMs
        while (!ended() && left > 0) {
            play(160)
            left -= 160
        }
        assertTrue("le vol finit", ended())
    }

    /** Glisse le doigt de [dx] pixels (vers la droite si positif), à partir du bas de l'écran, comme un enfant. */
    private fun glide(dx: Float) = rule.onNodeWithTag("road").performTouchInput {
        val start = Offset(width / 2f, height * 0.8f)
        swipe(start, start + Offset(dx, 0f), durationMillis = 96)
    }

    /** Un vol avancé par le robot du couloir jusqu'à la distance [dist] (sans aucun choc), que l'écran reprend ensuite. */
    private fun advancedRun(world: PlaneWorld, dist: Float): PlaneRun {
        val run = PlaneRun(world)
        var guard = 0
        while (run.dist < dist && guard++ < 100_000) {
            run.steerTo(world.pathAt(run.dist + 0.1f * run.speed))
            run.update(1f / 60f)
        }
        assertEquals(0, run.hits)
        return run
    }

    private fun worldWithTheme(d: PlaneDifficulty, theme: Int): PlaneWorld = (0L until 200L).map { PlaneWorld.generate(d, it) }.first { it.theme == theme }

    // ------------------------------------------------------------------ les écrans

    private val someProgress = PlaneProgress()
        .withResult(PlaneDifficulty.EASY, 3, 640, 0, 2)
        .withResult(PlaneDifficulty.MEDIUM, 2, 520, 1, 1)

    @Test
    fun theSpriteSheet() = show("107-avion-planche") {
        androidx.compose.foundation.Canvas(Modifier.fillMaxSize().background(Color(0xFFBFE6FF))) {
            val rowH = size.height / 10f
            val u = 420f
            fun x(col: Int, of: Int = 3) = size.width * (col + 0.5f) / of
            fun y(row: Int) = rowH * (row + 0.5f)
            for ((i, c) in listOf(PlaneColors[0], PlaneColors[1], PlaneColors[5]).withIndex()) drawPlayerPlane(x(i), y(0), u, c, if (i == 1) 14f else 0f, 0f, 0f, 0.1f)
            for ((i, r) in listOf(0.18f, 0.5f, 0.78f).withIndex()) drawPlayerPlane(x(i), y(1), u, PlaneColors[0], 0f, r, if (i == 0) 1f else 0f, 0.1f)
            for (v in 0 until 3) drawBirds(x(v), y(2), u, v, 0.2f, 1)
            for (v in 0 until 3) drawOncomingPlane(x(v), y(3), u, v, 0.05f)
            for (v in 0 until 3) drawBalloon(x(v), y(4), u, v, 0.3f)
            for (v in 0 until 3) drawParaglider(x(v), y(5), u, v, 0.4f)
            drawRing(x(0), y(6), u, 0.4f)
            drawCloud(x(1), y(6), u * 1.1f, 1, Color.White)
            drawWindmill(x(2), y(6) + rowH * 0.4f, u, 0.5f, false)
            drawGroundHouse(x(0), y(7) + rowH * 0.35f, u, 0, false)
            drawBoat(x(1), y(7) + rowH * 0.35f, u, 1)
            drawIsland(x(2), y(7) + rowH * 0.3f, u, 0)
            drawCactus(x(0), y(8) + rowH * 0.35f, u)
            drawMesa(x(1), y(8) + rowH * 0.35f, u, 1)
            drawTowerBlock(x(2), y(8) + rowH * 0.35f, u, 2, false)
            drawControlTower(x(0), y(9) + rowH * 0.45f, u * 0.7f, 0f, false)
            drawHangar(x(1), y(9) + rowH * 0.35f, u * 0.8f, false)
            drawParkedPlane(x(2), y(9) + rowH * 0.3f, u, 1)
        }
    }

    @Test
    fun theMenuForANewPlayer() = show("90-avion-menu") {
        PlaneMenuScreen(PlaneProgress(), color, onPlay = {}, onBack = {})
    }

    @Test
    fun theMenuWithSomeProgress() = show("91-avion-menu-progres") {
        PlaneMenuScreen(someProgress, color, onPlay = {}, onBack = {})
    }

    @Test
    fun theIntroExplainsTheGame() {
        show("92-avion-intro", content = screen())
        rule.onNodeWithText("Glisse ton doigt à gauche et à droite pour piloter l'avion !").assertExists()
        rule.onNodeWithText("Évite les oiseaux, les avions, les montgolfières et les parapentes.").assertExists()
        rule.onNodeWithText("Un choc te fait perdre une étoile, mais tu ne perds jamais.").assertExists()
        rule.onNodeWithText("Passe dans un anneau lumineux : ton avion fait un tonneau, tu gagnes des points et une étoile !").assertExists()
        rule.onNodeWithText("À la fin, tu atterris à l'aéroport.").assertExists()
        rule.onNodeWithText("Petit vol").assertExists()
    }

    @Test
    fun theHubCardShowsThePlaneTheRingAndTheBalloon() = show("93-avion-carte") {
        Box(Modifier.background(Color(0xFFFFB020)).padding(20.dp)) {
            PlaneMiniGame.Illustration(Modifier.size(width = 330.dp, height = 170.dp))
        }
    }

    /** Un vol déjà bien avancé dans le monde numéro [theme] (l'écran le reprend où le robot l'a laissé). */
    private fun theWorld(name: String, theme: Int, d: PlaneDifficulty = PlaneDifficulty.HARD, dist: Float = 330f) {
        val world = worldWithTheme(d, theme)
        show(name, content = screen(d, world = world, startingRun = advancedRun(world, dist)))
        assertTrue(rule.onAllNodesWithTag("road").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun theCountryWorld() = theWorld("94-avion-campagne", 0)

    @Test
    fun theSeaWorld() = theWorld("95-avion-mer", 1)

    @Test
    fun theDesertWorld() = theWorld("96-avion-desert", 2)

    @Test
    fun theNightWorld() = theWorld("97-avion-nuit", 3, PlaneDifficulty.EXPERT, 400f)

    /** Une rangée de [kind] dans les voies [lanes], à la distance [z]. */
    private fun row(startId: Int, kind: PieceKind, z: Float, lanes: List<Int>) = lanes.mapIndexed { i, l -> Piece(startId + i, kind, z, l, (startId + i) % 3) }

    @Test
    fun everyKindOfObstacleHasItsOwnRowAndSitsInALane() {
        // une rangée par genre, chacune dans ses voies : jamais de mélange côte à côte, jamais à cheval sur deux voies
        val pieces = row(0, PieceKind.BIRDS, 90f, listOf(-2, 1)) + row(10, PieceKind.BALLOON, 104f, listOf(0, 2)) +
            row(20, PieceKind.PARAGLIDER, 118f, listOf(-1, 2)) + row(30, PieceKind.PLANE, 132f, listOf(-2, 0, 1)) + row(40, PieceKind.RING, 146f, listOf(-1))
        val path = listOf(PathPoint(0f, 0f), PathPoint(90f, 0f), PathPoint(104f, -0.4f), PathPoint(118f, 0f), PathPoint(132f, 0.8f), PathPoint(146f, -0.4f))
        val world = sky(*pieces.toTypedArray(), length = 400f, path = path)
        show("98-avion-obstacles", content = screen(world = world, startingRun = advancedRun(world, 56f)))
        assertTrue(rule.onAllNodesWithTag("road").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun aSlalomKeepsTwoFreeLanesSideBySideAndShiftsThemByOneLaneAtATime() {
        // avant la dernière difficulté : trois obstacles par rangée, la paire de voies libres se décale d'une voie à chaque rangée
        val frees = listOf(0 to 1, 1 to 2, 2 to 1, 1 to 0, 0 to -1, -1 to 0)
        val pieces = ArrayList<Piece>()
        for ((i, free) in frees.withIndex()) pieces += row(100 * i, PieceKind.BIRDS, 90f + i * 13f, (-2..2).filter { it != free.first && it != free.second })
        val path = listOf(PathPoint(0f, 0f)) + frees.mapIndexed { i, f -> PathPoint(90f + i * 13f, f.first * 0.4f) }
        val world = sky(*pieces.toTypedArray(), length = 400f, path = path, difficulty = PlaneDifficulty.HARD)
        show("99-avion-slalom", content = screen(PlaneDifficulty.HARD, world = world, startingRun = advancedRun(world, 62f)))
        assertTrue(rule.onAllNodesWithTag("road").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun theLastDifficultyHasNarrowSlaloms() {
        // à la dernière difficulté seulement : une seule voie libre, rangée après rangée
        val frees = listOf(0, 1, 2, 1, 0, -1)
        val pieces = ArrayList<Piece>()
        for ((i, free) in frees.withIndex()) pieces += row(100 * i, PieceKind.BIRDS, 90f + i * 11f, (-2..2).filter { it != free })
        val path = listOf(PathPoint(0f, 0f)) + frees.mapIndexed { i, f -> PathPoint(90f + i * 11f, f * 0.4f) }
        val world = sky(*pieces.toTypedArray(), length = 400f, path = path, difficulty = PlaneDifficulty.EXPERT)
        show("108-avion-passage-etroit", content = screen(PlaneDifficulty.EXPERT, world = world, startingRun = advancedRun(world, 62f)))
        assertTrue(rule.onAllNodesWithTag("road").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun planesCrossTheSkyAndGlideToTheirLane() {
        // deux rangées d'avions qui traversent le ciel dans le même sens : ils arrivent de plus loin sur la gauche et glissent vers leur voie en penchant
        val first = listOf(-2, -1, 2).mapIndexed { i, l -> Piece(i, PieceKind.PLANE, 92f, l, 1, drift = 3, span = 70f) }
        val second = listOf(-1, 0).mapIndexed { i, l -> Piece(10 + i, PieceKind.PLANE, 112f, l, 1, drift = 3, span = 70f) }
        val path = listOf(PathPoint(0f, 0f), PathPoint(92f, 0f), PathPoint(112f, 0.8f))
        val world = sky(*(first + second).toTypedArray(), length = 400f, path = path, difficulty = PlaneDifficulty.HARD)
        show("109-avion-traversee", content = screen(PlaneDifficulty.HARD, world = world, startingRun = advancedRun(world, 66f)))
        assertTrue(rule.onAllNodesWithTag("road").fetchSemanticsNodes().isNotEmpty())
    }

    @Test
    fun aHitShowsTheBurstTheLostStarAndAnOuch() {
        val capture = Capture()
        show(null, content = screen(world = sky(Piece(0, PieceKind.BIRDS, 40f, 0, 1), Piece(1, PieceKind.BALLOON, 80f, 2, 0)), capture = capture))
        start()
        assertEquals(3, lives())
        var guard = 0
        while (capture.run!!.hits == 0 && guard++ < 600) play(16)
        play(96)
        assertEquals(1, capture.run!!.hits)
        assertEquals(2, lives())
        assertTrue("Aïe !", hasText("Aïe !"))
        assertEquals(listOf("crash"), capture.log)
        shot("100-avion-choc")
    }

    @Test
    fun aRingMakesTheAirplaneRollAndGivesBackAStar() {
        val capture = Capture()
        // une montgolfière qui coûte une étoile, puis un anneau en plein milieu : l'avion immobile le traverse ; des oiseaux juste après : en tonneau on passe au travers
        show(null, content = screen(world = sky(Piece(0, PieceKind.BALLOON, 40f, 0, 0), Piece(1, PieceKind.RING, 90f, 0, 0), Piece(2, PieceKind.BIRDS, 95f, 0, 0), length = 260f), capture = capture))
        start()
        var guard = 0
        while (capture.run!!.rolls == 0 && guard++ < 1500) play(16)
        assertEquals(1, capture.run!!.rolls)
        assertEquals("une étoile rendue", 3, lives())
        play(260)
        assertTrue("en tonneau", capture.run!!.rolling)
        assertTrue("Tonneau", hasText("Tonneau ! +50\nUne étoile en plus !"))
        shot("101-avion-tonneau")
        play(1200)
        assertEquals("on a traversé les oiseaux", 1, capture.run!!.hits)
        assertEquals(listOf("crash", "roll"), capture.log)
    }

    @Test
    fun theLandingOnTheRunwayEndsTheFlight() {
        val capture = Capture()
        show(null, content = screen(world = sky(length = 260f), capture = capture))
        start()
        var guard = 0
        while (capture.run!!.altitude > 0.3f && guard++ < 4000) play(16)
        assertTrue(capture.run!!.autopilot)
        shot("102-avion-atterrissage")
        while (!capture.run!!.finished && guard++ < 8000) play(16)
        play(200)
        shot("103-avion-piste")
        playUntilTheEnd()
        assertEquals(1, capture.finished.size)
        assertEquals(listOf(3, capture.run!!.score, 0, 0), capture.finished.single())
        assertTrue("les roues avant la fin", capture.log.indexOf("land") in 0 until capture.log.indexOf("win"))
    }

    @Test
    fun theThreeStarsEndCardUnlocksTheNextDifficultyAndOffersIt() {
        val capture = Capture()
        show(null, content = screen(world = sky(length = 200f), capture = capture))
        start()
        playUntilTheEnd()
        play(1700)
        shot("104-avion-bravo")
        rule.onNodeWithText("Aucun choc : atterrissage parfait !").assertExists()
        assertTrue(rule.onAllNodesWithTag("unlocked", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty())
        rule.onNodeWithText("Rejouer").assertExists()
        rule.onNodeWithText("Difficultés").assertExists()
        rule.onNodeWithText("Difficulté suivante").performClick()
        assertEquals(listOf(PlaneDifficulty.MEDIUM), capture.next)
        assertEquals(listOf("land", "win", "star0", "star1", "star2"), capture.log)
    }

    @Test
    fun withOnlyOneStarTheNextDifficultyStaysClosedAndIsNotOffered() {
        val capture = Capture()
        // deux obstacles en plein milieu : deux chocs, donc une seule étoile
        show(null, content = screen(world = sky(Piece(0, PieceKind.BALLOON, 40f, 0, 0), Piece(1, PieceKind.PLANE, 70f, 0, 0), length = 220f), capture = capture))
        start()
        playUntilTheEnd()
        play(1500)
        shot("105-avion-bravo-une-etoile")
        assertEquals(2, capture.run!!.hits)
        assertEquals(1, capture.finished.single()[0])
        assertEquals(2, capture.finished.single()[2])
        assertEquals(0, capture.finished.single()[3])
        assertTrue(rule.onAllNodesWithText("Difficulté suivante").fetchSemanticsNodes().isEmpty())
        assertTrue(rule.onAllNodesWithTag("unlocked", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
        rule.onNodeWithText("Passe dans les anneaux : ils rendent des étoiles !").assertExists()
    }

    @Test
    fun theNextDifficultyIsOfferedWhenAlreadyOpenEvenAfterABadFlight() {
        val open = PlaneProgress().withResult(PlaneDifficulty.EASY, 2, 400, 1, 0)
        show(null, content = screen(world = sky(Piece(0, PieceKind.BALLOON, 40f, 0, 0), Piece(1, PieceKind.PLANE, 70f, 0, 0), length = 220f), progress = open))
        start()
        playUntilTheEnd()
        play(1500)
        rule.onNodeWithText("Difficulté suivante").assertExists()
        assertTrue("ce n'est pas une nouveauté", rule.onAllNodesWithTag("unlocked", useUnmergedTree = true).fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun theGreenButtonReplaysWithANewSkyAndNoIntro() {
        val capture = Capture()
        show(null, content = screen(world = sky(length = 200f), capture = capture))
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
        show(null, content = screen(world = sky(length = 200f), capture = capture))
        start()
        rule.onNodeWithTag("levels").performClick()
        assertEquals(1, capture.menu)
        rule.onNodeWithTag("exit").performClick()
        assertEquals(1, capture.exit)
    }

    // ------------------------------------------------------------------ le doigt

    @Test
    fun theAirplaneFliesOnItsOwnAndTheProgressBarGrows() {
        val capture = Capture()
        show(null, content = screen(world = sky(length = 500f), capture = capture))
        start()
        play(1500)
        val run = capture.run!!
        assertTrue("il vole (${run.dist} m)", run.dist > 3f)
        assertTrue(run.speed > 1f)
        rule.onNodeWithTag("progress").assertExists()
        assertEquals("0", text("score"))
        shot("106-avion-vol")
    }

    @Test
    fun theAirplaneDoesNotMoveBeforeTheStartButton() {
        val capture = Capture()
        show(null, content = screen(world = sky(length = 500f), capture = capture))
        play(800)
        assertEquals(0f, capture.run!!.dist, 0.0001f)
    }

    @Test
    fun slidingTheFingerMovesTheAirplaneOneToOneAtItsDepth() {
        val capture = Capture()
        show(null, content = screen(world = sky(length = 500f), capture = capture))
        start()
        val unit = 0.5f * roadWidthPx()
        glide(0.2f * roadWidthPx())
        play(200)
        val right = capture.run!!.targetX
        assertEquals("0,2 largeur d'écran de glissé = ${0.2f * roadWidthPx() / unit} unités de ciel", 0.2f * roadWidthPx() / unit, right, 0.04f)
        assertTrue("l'avion suit", abs(capture.run!!.x - right) < 0.08f)
        glide(-0.3f * roadWidthPx())
        play(200)
        assertEquals(right - 0.3f * roadWidthPx() / unit, capture.run!!.targetX, 0.04f)
        assertTrue(capture.run!!.x < right)
    }

    @Test
    fun theAirplaneStaysFreeBetweenLanes() {
        // le contrôle reste précis : un petit glissé qui ne tombe sur aucun milieu de voie est respecté tel quel
        val capture = Capture()
        show(null, content = screen(world = sky(length = 500f), capture = capture))
        start()
        glide(0.065f * roadWidthPx())
        play(400)
        val lane = capture.run!!.x / PlaneWorld.LANE_WIDTH
        assertNotEquals(Math.round(lane).toFloat(), lane, 0.05f)
        assertTrue(capture.run!!.x > 0.05f)
    }

    @Test
    fun theAirplaneNeverLeavesTheSkyEvenWithAHugeSlide() {
        val capture = Capture()
        show(null, content = screen(world = sky(length = 500f), capture = capture))
        start()
        glide(roadWidthPx() * 0.45f)
        glide(-roadWidthPx() * 0.45f)
        glide(-roadWidthPx() * 0.45f)
        play(300)
        assertEquals(-PlaneRun.MAX_X, capture.run!!.targetX, 0.001f)
        assertTrue(capture.run!!.x >= -PlaneRun.MAX_X - 0.001f)
    }

    @Test
    fun theFingerDoesNothingWhileTheIntroIsShown() {
        val capture = Capture()
        show(null, content = screen(world = sky(length = 500f), capture = capture))
        glide(0.2f * roadWidthPx())
        play(100)
        assertEquals(0f, capture.run!!.targetX, 0.0001f)
    }

    @Test
    fun aMotionlessAirplaneGetsHitButAlwaysLandsAndSavesAtLeastOneStar() {
        val capture = Capture()
        val pieces = Array(6) { Piece(it, PieceKind.BALLOON, 40f + it * 25f, 0, 0) }
        show(null, content = screen(world = sky(*pieces, length = 300f), capture = capture))
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
        val pieces = Array(6) { Piece(it, PieceKind.BALLOON, 40f + it * 25f, 0, 0) }
        show(null, content = screen(world = sky(*pieces, length = 300f), capture = capture))
        start()
        val seen = HashSet<Int>()
        var guard = 0
        while (!ended() && guard++ < 6000) {
            play(32)
            seen += lives()
        }
        assertEquals(setOf(3, 2, 1, 0), seen)
    }

    @Test
    fun aRealGeneratedFlightCanBeWonWithTheFingerAlone() {
        // le doigt suit le couloir libre, image par image, comme un très bon joueur : aucun choc, trois étoiles
        val world = PlaneWorld.generate(PlaneDifficulty.EASY, SEED)
        val capture = Capture()
        show(null, content = screen(world = world, capture = capture))
        start()
        val unit = 0.5f * roadWidthPx()
        var guard = 0
        while (!ended() && guard++ < 7000) {
            val run = capture.run!!
            if (run.autopilot) {
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
        assertTrue("le vol finit", ended())
        assertEquals("aucun choc", 0, capture.run!!.hits)
        assertEquals(3, lives())
        assertEquals(3, capture.finished.single()[0])
        assertTrue("des points d'obstacles évités et d'anneaux", capture.run!!.score > 200)
    }

    // ------------------------------------------------------------------ depuis l'appli

    private fun profile(progress: PlaneProgress?): Profile {
        val base = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!
        return if (progress == null) base else base.withGameSave(PlaneProgress.GAME_ID, progress.toJson())
    }

    private fun openGame(store: InMemoryProfileStore) {
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, PlaneProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(600)
    }

    @Test
    fun openingTheGameLandsOnTheFirstDifficultyForANewPlayer() {
        openGame(InMemoryProfileStore(listOf(profile(null))))
        rule.onNodeWithText("Petit vol").assertExists()
        rule.onNodeWithText("C'est parti !").assertExists()
        assertTrue(rule.onAllNodesWithTag("difficulty-0").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun openingTheGameLandsOnTheHighestDifficultyAlreadyPlayedThenTheButtonShowsTheChoice() {
        openGame(InMemoryProfileStore(listOf(profile(someProgress))))
        rule.onNodeWithText("Vol rapide").assertExists()
        // l'introduction propose de changer de difficulté ; une fois le vol lancé, c'est le bouton en grille de tuiles
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
        rule.onNodeWithText("Petit vol").assertExists()
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
    fun theSaveOfTheOldSkaterGameOpensTheAirplaneAtTheSameDifficulty() {
        // un joueur de la version 0.15.0 (Super Skate, identifiant « skate ») : ses étoiles et sa difficulté suivent dans Super Avion
        val skater = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!.withGameSave(
            "skate", org.json.JSONObject("""{"stars":{"0":3,"1":2},"best":{"0":640,"1":520},"hits":4,"jumps":7,"played":5}"""),
        )
        val store = com.kcharignon.petitsjeux.core.save.FileProfileStore(java.io.File(java.nio.file.Files.createTempDirectory("saves").toFile(), "s"))
        store.save(skater)
        openGame(InMemoryProfileStore(listOf(store.load(0)!!)))
        rule.onNodeWithText("Vol rapide").assertExists()
        assertEquals(5, com.kcharignon.petitsjeux.core.Games.totalStars(store.load(0)!!))
    }

    @Test
    fun theHubCardShowsTheStarsAndTheProgress() {
        assertEquals(5, PlaneMiniGame.stars(someProgress.toJson()))
        assertEquals("Record 640", PlaneMiniGame.progressLabel(someProgress.toJson()))
        assertEquals("Nouveau !", PlaneMiniGame.progressLabel(null))
        assertEquals("Super Avion", PlaneMiniGame.title)
        assertEquals(PlaneProgress.GAME_ID, PlaneMiniGame.id)
        assertNotNull(PlaneMiniGame.tagline)
    }

    @Test
    fun theMenuLocksTheDifficultiesNotYetEarned() {
        show(null) { PlaneMenuScreen(PlaneProgress().withResult(PlaneDifficulty.EASY, 1, 100, 3, 0), color, onPlay = {}, onBack = {}) }
        rule.onNodeWithTag("difficulty-0").assertIsEnabled()
        rule.onNodeWithTag("difficulty-1").assertIsNotEnabled()
        rule.onNodeWithTag("difficulty-3").assertIsNotEnabled()
        rule.onNodeWithText("Atterris avec 2 étoiles en « Petit vol » pour débloquer.").assertExists()
    }

    @Test
    fun theSoundsAreCrashRollAndLand() {
        val capture = Capture()
        show(null, content = screen(world = sky(Piece(0, PieceKind.BALLOON, 40f, 0, 0), Piece(1, PieceKind.RING, 90f, 0, 0), length = 220f), capture = capture))
        start()
        playUntilTheEnd()
        assertEquals(listOf("crash", "roll", "land", "win"), capture.log.filter { !it.startsWith("star") })
    }

    companion object {
        /** La graine des vols des tests (le jeu, lui, en tire une nouvelle à chaque vol). */
        const val SEED = 20260812L
    }
}
