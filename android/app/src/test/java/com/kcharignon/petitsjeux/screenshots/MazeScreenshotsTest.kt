package com.kcharignon.petitsjeux.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.kcharignon.petitsjeux.core.Feedback
import com.kcharignon.petitsjeux.core.LocalFeedback
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.Screen
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.InMemoryProfileStore
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.ui.Backdrop
import com.kcharignon.petitsjeux.games.maze.MazeMiniGame
import com.kcharignon.petitsjeux.games.maze.MazeProgress
import com.kcharignon.petitsjeux.games.maze.engine.Cell
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevel
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevels
import com.kcharignon.petitsjeux.games.maze.engine.MazeWorlds
import com.kcharignon.petitsjeux.games.maze.ui.MazeLayout
import com.kcharignon.petitsjeux.games.maze.ui.MazeMenuScreen
import com.kcharignon.petitsjeux.games.maze.ui.MazePlayScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/** Les écrans de Poisson Perdu en PNG (docs/screenshots, en portrait), et de vrais niveaux tracés au doigt sur l'écran. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w390dp-h844dp-port-xhdpi")
class MazeScreenshotsTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val levels: List<MazeLevel> by lazy { MazeLevels.parse(File("src/main/res/raw/maze_levels.json").readText()) }
    private fun level(n: Int) = levels[n - 1]

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
            left -= 16
        }
    }

    private fun screen(l: MazeLevel, progress: MazeProgress = MazeProgress(), onFinished: (Int, Int) -> Unit = { _, _ -> }, onNext: (Int) -> Unit = {}): @Composable () -> Unit =
        { MazePlayScreen(l, progress, onFinished, onNext, {}, {}) }

    /** Où est le labyrinthe à l'écran : exactement la mise en page que l'écran de jeu calcule. */
    private fun layout(l: MazeLevel): MazeLayout {
        val size = rule.onNodeWithTag("maze").fetchSemanticsNode().size
        return MazeLayout(l, size.width.toFloat(), size.height.toFloat(), margin = 6f * rule.density.density)
    }

    /** Le doigt se pose sur [cells] (la première) et glisse de case en case, sans se lever. */
    private fun drag(l: MazeLevel, cells: List<Cell>, lift: Boolean = true) {
        val m = layout(l)
        rule.onNodeWithTag("maze").performTouchInput {
            down(m.center(cells.first()))
            for (c in cells.drop(1)) moveTo(m.center(c))
            if (lift) up()
        }
        play(48)
    }

    private fun trail(): String = rule.onNodeWithTag("trail").fetchSemanticsNode().config.getOrNull(SemanticsProperties.StateDescription) ?: ""

    private fun text(tag: String, unmerged: Boolean = false): String =
        rule.onNodeWithTag(tag, useUnmergedTree = unmerged).fetchSemanticsNode().config.getOrNull(SemanticsProperties.Text)?.joinToString("") ?: ""

    private fun finished(): Boolean = rule.onAllNodesWithTag("final-stars", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    /** La première case de la solution d'où part une impasse : (index sur la solution, première case de l'impasse). */
    private fun branch(l: MazeLevel): Pair<Int, Cell> {
        for (i in 0 until l.solution.size - 1) {
            val wrong = l.neighbors(l.solution[i]).firstOrNull { it != l.solution[i + 1] && (i == 0 || it != l.solution[i - 1]) }
            if (wrong != null && l.distanceToExit(wrong) > l.distanceToExit(l.solution[i])) return i to wrong
        }
        error("pas d'embranchement au niveau ${l.number}")
    }

    private val someProgress = (1..13).fold(MazeProgress()) { p, n -> p.withResult(n, listOf(3, 3, 2, 3, 1)[n % 5], if (n % 5 == 4) 5 else 0) }

    // ------------------------------------------------------------------ les écrans

    @Test
    fun theMenuForANewPlayer() = show("60-labyrinthe-menu") { MazeMenuScreen(MazeProgress(), onPlay = {}, onBack = {}) }

    @Test
    fun theMenuAfterJumpingAheadResumesFromTheFurthestLevel() {
        // 1 à 13 réussis, puis un saut au niveau 40 : on reprend au 41 ; les niveaux laissés derrière sont gris mais jouables
        val jumped = someProgress.withResult(40, 3, 0)
        assertEquals(41, jumped.currentLevel)
        show("70-labyrinthe-menu-saut") { MazeMenuScreen(jumped, onPlay = {}, onBack = {}) }
        rule.onNodeWithTag("level-41").assertExists()
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
    fun theBlueBackArrowGoesHomeNotToTheLevelGrid() {
        val store = InMemoryProfileStore(listOf(Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!))
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, MazeProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(300)
        rule.onNodeWithTag("exit").performClick()
        rule.mainClock.advanceTimeBy(300)
        assertTrue("à l'accueil", rule.onAllNodesWithText("Parking Malin").fetchSemanticsNodes().isNotEmpty())
        assertTrue("pas dans la grille", rule.onAllNodesWithTag("level-1").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun anyLevelCanBeOpenedFromTheStartEvenInTheLastWorld() {
        val player = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!
        val store = InMemoryProfileStore(listOf(player))
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, MazeProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(300)
        // le jeu s'ouvre sur le niveau 1 (avec son introduction) ; « Choisir un autre niveau » ramène au choix du niveau
        rule.onNodeWithText("Choisir un autre niveau").performClick()
        rule.mainClock.advanceTimeBy(300)
        // on saute au niveau 97 sans rien avoir fait avant (un niveau du milieu du dernier monde : pas d'introduction)
        scrollListTo("worlds", "level-97")
        rule.mainClock.advanceTimeBy(100)
        rule.onNodeWithTag("level-97").performClick()
        play(300)
        assertTrue(rule.onAllNodesWithText("Niveau 97").fetchSemanticsNodes().isNotEmpty())
        val l = level(97)
        assertEquals("1 cases, tête ${l.start.x},${l.start.y}, 0 impasses", trail())
        // on le réussit : la reprise se fait au niveau suivant, et le niveau 1 n'a jamais été fait
        drag(l, l.solution)
        play(5000)
        assertTrue(finished())
        val saved = MazeProgress.fromJson(store.load(0)!!.gameSave(MazeProgress.GAME_ID))
        assertEquals(3, saved.starsOf(97))
        assertEquals(98, saved.currentLevel)
        assertTrue(!saved.isDone(1))
    }

    @Test
    fun theMenuWithSomeProgress() {
        show("61-labyrinthe-menu-progres") { MazeMenuScreen(someProgress, onPlay = {}, onBack = {}) }
        rule.onNodeWithTag("level-14").assertExists()
    }

    @Test
    fun theIntroOfTheFirstLevelExplainsTheGame() {
        show("62-labyrinthe-intro", content = screen(level(1)))
        rule.onNodeWithText("Pose ton doigt sur le poisson, puis trace le chemin jusqu'à sa maison !").assertExists()
        rule.onNodeWithText("C'est parti !").assertExists()
    }

    @Test
    fun theFirstLevelOfAWorldAnnouncesWhatIsNew() {
        show("63-labyrinthe-intro-monde", content = screen(level(51)))
        rule.onNodeWithTag("world-news", useUnmergedTree = true).assertExists()
        rule.onNodeWithText("Nouveau : Des boucles : il y a plusieurs chemins !").assertExists()
    }

    @Test
    fun aLevelInTheMiddleOfAWorldStartsRightAway() {
        show(null, content = screen(level(14)))
        assertTrue("pas d'introduction au niveau 14", rule.onAllNodesWithText("C'est parti !").fetchSemanticsNodes().isEmpty())
        assertEquals("1 cases, tête ${level(14).start.x},${level(14).start.y}, 0 impasses", trail())
    }

    @Test
    fun theTraceFollowsTheFingerAndTheFishWaitsAtTheEntrance() {
        val l = level(14)
        show(null, content = screen(l))
        drag(l, l.solution.take(8), lift = false)
        play(400)
        shot("64-labyrinthe-jeu")
        assertEquals("8 cases, tête ${l.solution[7].x},${l.solution[7].y}, 0 impasses", trail())
        assertEquals("0", text("detours", unmerged = true))
    }

    @Test
    fun aDeadEndCostsAStarAndTheTraceCanBeRewound() {
        val l = level(5)
        val (at, wrong) = branch(l)
        show(null, content = screen(l))
        drag(l, l.solution.take(at + 1) + wrong, lift = false)
        play(300)
        shot("65-labyrinthe-impasse")
        assertEquals("1", text("detours", unmerged = true))
        // on revient en arrière : le doigt repasse sur la trace
        val m = layout(l)
        rule.onNodeWithTag("maze").performTouchInput { moveTo(m.center(l.solution[at])); up() }
        play(100)
        assertEquals("${at + 1} cases, tête ${l.solution[at].x},${l.solution[at].y}, 1 impasses", trail())
        assertEquals("l'impasse reste comptée", "1", text("detours", unmerged = true))
    }

    @Test
    fun theHintShowsTheNextCellsAndIsFree() {
        val l = level(14)
        show(null, content = screen(l))
        drag(l, l.solution.take(4))
        rule.onNodeWithTag("hint").performClick()
        play(500)
        shot("66-labyrinthe-indice")
        assertEquals("0", text("detours", unmerged = true))
    }

    @Test
    fun liftingTheFingerKeepsTheTraceAndItCanBeResumed() {
        val l = level(14)
        show(null, content = screen(l))
        drag(l, l.solution.take(5))
        assertEquals("5 cases, tête ${l.solution[4].x},${l.solution[4].y}, 0 impasses", trail())
        drag(l, l.solution.drop(4).take(4))
        assertEquals("8 cases, tête ${l.solution[7].x},${l.solution[7].y}, 0 impasses", trail())
    }

    @Test
    fun aFingerPutDownFarFromTheFishTracesNothing() {
        val l = level(14)
        show(null, content = screen(l))
        // la sortie est à l'autre bout du labyrinthe : le doigt n'y prend pas le poisson
        drag(l, listOf(l.exit, l.solution[l.solution.size - 2], l.solution[l.solution.size - 3]))
        assertEquals("1 cases, tête ${l.start.x},${l.start.y}, 0 impasses", trail())
    }

    @Test
    fun theRestartButtonClearsTheTraceAndTheDetours() {
        val l = level(5)
        val (at, wrong) = branch(l)
        show(null, content = screen(l))
        drag(l, l.solution.take(at + 1) + wrong)
        assertEquals("1", text("detours", unmerged = true))
        rule.onNodeWithTag("restart").performClick()
        play(100)
        assertEquals("1 cases, tête ${l.start.x},${l.start.y}, 0 impasses", trail())
        assertEquals("0", text("detours", unmerged = true))
    }

    @Test
    fun theAbyssesAreDrawnOnADarkFloor() {
        val l = level(83) // pas le premier du monde : pas d'introduction qui cache le plateau
        show(null, content = screen(l))
        drag(l, l.solution.take(9), lift = false)
        play(300)
        shot("68-labyrinthe-abysses")
    }

    @Test
    fun theGameCardOfTheMenu() {
        show("69-labyrinthe-carte") { Backdrop { MazeMiniGame.Illustration(Modifier.padding(16.dp).fillMaxWidth().height(170.dp)) } }
    }

    // ------------------------------------------------------------------ une partie complète au toucher

    @Test
    fun tracingTheWholeWayWithoutAMistakeGivesThreeStarsAndTheFishSwimsHome() {
        val l = level(14)
        var result: Pair<Int, Int>? = null
        show(null, content = screen(l, onFinished = { s, d -> result = s to d }))
        drag(l, l.solution)
        assertEquals(3 to 0, result)
        assertTrue(trail().endsWith("arrivé"))
        // le poisson suit le chemin tracé, puis le bilan s'affiche
        play(1400)
        assertTrue("le poisson nage encore : pas de bilan", !finished())
        play(2500)
        shot("67-labyrinthe-victoire")
        assertTrue(finished())
        assertEquals("Chemin parfait !", text("final-detours", unmerged = true))
        rule.onNodeWithText("Niveau suivant").assertExists()
        rule.onNodeWithText("Rejouer").assertExists()
    }

    @Test
    fun onTheLastLevelTheGreenButtonReplaysBecauseThereIsNoNextLevel() {
        val l = level(MazeWorlds.LEVEL_COUNT)
        show(null, content = screen(l))
        drag(l, l.solution)
        play(4000)
        assertTrue(finished())
        shot("84-labyrinthe-dernier-niveau")
        assertTrue(rule.onAllNodesWithText("Niveau suivant").fetchSemanticsNodes().isEmpty())
        // un seul « Rejouer » : le grand bouton vert (pas de doublon dans la rangée du bas)
        assertEquals(1, rule.onAllNodesWithText("Rejouer").fetchSemanticsNodes().size)
        rule.onNodeWithText("Niveaux").assertExists()
        rule.onNodeWithText("Rejouer").performClick()
        play(600)
        assertTrue("on rejoue le même niveau", !finished())
    }

    @Test
    fun aMistakeIsCountedAndTheFinalCardSaysSo() {
        val l = level(5)
        val (at, wrong) = branch(l)
        var result: Pair<Int, Int>? = null
        show(null, content = screen(l, onFinished = { s, d -> result = s to d }))
        // dans l'impasse, puis retour à l'embranchement, puis le bon chemin
        drag(l, l.solution.take(at + 1) + wrong, lift = false)
        val m = layout(l)
        rule.onNodeWithTag("maze").performTouchInput { moveTo(m.center(l.solution[at])) }
        play(32)
        rule.onNodeWithTag("maze").performTouchInput { for (c in l.solution.drop(at + 1)) moveTo(m.center(c)); up() }
        play(48)
        assertEquals(2 to 1, result)
        play(4500)
        assertTrue(finished())
        assertEquals("1 impasse", text("final-detours", unmerged = true))
    }

    @Test
    fun rePlayingStartsALevelOverWithoutTheIntro() {
        val l = level(1)
        show(null, content = screen(l))
        rule.onNodeWithText("C'est parti !").performClick()
        play(100)
        drag(l, l.solution)
        play(4500)
        assertTrue(finished())
        rule.onNodeWithText("Rejouer").performClick()
        play(200)
        assertTrue("on rejoue sans l'introduction", rule.onAllNodesWithText("C'est parti !").fetchSemanticsNodes().isEmpty())
        assertEquals("1 cases, tête ${l.start.x},${l.start.y}, 0 impasses", trail())
    }

    // ------------------------------------------------------------------ les sons

    private class RecordingFeedback : Feedback {
        val bloops = ArrayList<Int>()
        var bursts = 0
        var bonks = 0
        var hints = 0
        var wins = 0
        val stars = ArrayList<Int>()
        override fun exit() = Unit
        override fun bump(vehicleLength: Int) = Unit
        override fun win() { wins++ }
        override fun tap() = Unit
        override fun star(index: Int) { stars += index }
        override fun hint() { hints++ }
        override fun pick(index: Int) = Unit
        override fun finalPick() = Unit
        override fun puff() = Unit
        override fun bonk() { bonks++ }
        override fun hop() = Unit
        override fun bell() = Unit
        override fun bloop(pitch: Int) { bloops += pitch }
        override fun burst() { bursts++ }
    }

    @Test
    fun theBloopRisesTowardsTheExitAndFallsInADeadEnd() {
        // un niveau où l'on fait au moins trois pas sur le bon chemin avant l'embranchement
        val l = levels.drop(10).first { level -> runCatching { branch(level).first }.getOrDefault(-1) >= 3 }
        val (at, wrong) = branch(l)
        val feedback = RecordingFeedback()
        show(null) { CompositionLocalProvider(LocalFeedback provides feedback) { MazePlayScreen(l, MazeProgress(), { _, _ -> }, {}, {}, {}) } }
        // le bon chemin, une case à la fois : le « bloup » ne baisse jamais
        val m = layout(l)
        rule.onNodeWithTag("maze").performTouchInput {
            down(m.center(l.solution.first()))
            for (c in l.solution.take(at + 1).drop(1)) moveTo(m.center(c))
        }
        play(48)
        assertTrue("des bloups sont joués", feedback.bloops.isNotEmpty())
        assertEquals("de plus en plus aigus", feedback.bloops.sorted(), feedback.bloops)
        val before = feedback.bloops.size
        // dans l'impasse : le « bloup » descend, et on entend le « toc » au fond
        val visited = arrayListOf(wrong)
        rule.onNodeWithTag("maze").performTouchInput {
            var here = wrong
            moveTo(m.center(here))
            while (true) {
                val next = l.neighbors(here).firstOrNull { l.distanceToExit(it) > l.distanceToExit(here) } ?: break
                moveTo(m.center(next))
                here = next
                visited += next
            }
        }
        play(48)
        val inDeadEnd = feedback.bloops.drop(before)
        assertTrue("des bloups dans l'impasse", inDeadEnd.isNotEmpty())
        assertTrue("le bloup descend en s'éloignant de la sortie : $inDeadEnd", inDeadEnd.first() <= feedback.bloops[before - 1])
        assertEquals("un toc au fond de l'impasse", 1, feedback.bonks)
        // on recule en repassant le doigt sur la trace, case par case : les bulles éclatent
        rule.onNodeWithTag("maze").performTouchInput {
            for (c in visited.dropLast(1).reversed() + l.solution[at]) moveTo(m.center(c))
            up()
        }
        play(48)
        assertTrue("les bulles éclatent en reculant", feedback.bursts >= 1)
        rule.onNodeWithTag("hint").performClick()
        assertEquals(1, feedback.hints)
    }

    @Test
    fun theFanfareAndTheStarsPlayAtTheEnd() {
        val l = level(1)
        val feedback = RecordingFeedback()
        show(null) { CompositionLocalProvider(LocalFeedback provides feedback) { MazePlayScreen(l, MazeProgress(), { _, _ -> }, {}, {}, {}) } }
        rule.onNodeWithText("C'est parti !").performClick()
        play(100)
        drag(l, l.solution)
        play(5000)
        assertEquals(1, feedback.wins)
        assertEquals(listOf(0, 1, 2), feedback.stars)
    }

    // ------------------------------------------------------------------ depuis l'appli : le niveau est sauvegardé, « Niveau suivant » ouvre le suivant

    @Test
    fun fromTheAppTheLevelIsSavedAndTheNextOneOpens() {
        val player = Profile.create(0, "Léa", Avatar.CAT, now = 1L)!!
        val store = InMemoryProfileStore(listOf(player))
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, MazeProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(300)
        // le jeu s'ouvre tout de suite sur le niveau 1 : pas de grille de niveaux à parcourir
        assertTrue(rule.onAllNodesWithTag("level-1").fetchSemanticsNodes().isEmpty())
        assertTrue(rule.onAllNodesWithText("Niveau 1").fetchSemanticsNodes().isNotEmpty())
        rule.onNodeWithText("C'est parti !").performClick()
        play(100)
        val l = level(1)
        drag(l, l.solution)
        play(4500)
        assertTrue(finished())
        val saved = MazeProgress.fromJson(store.load(0)!!.gameSave(MazeProgress.GAME_ID))
        assertEquals(3, saved.starsOf(1))
        assertEquals(1, saved.played)
        assertEquals(0, saved.detours)
        assertNotNull(store.load(0))
        // « Niveau suivant » ouvre le niveau 2
        rule.onNodeWithText("Niveau suivant").performClick()
        play(300)
        assertTrue(rule.onAllNodesWithText("Niveau 2").fetchSemanticsNodes().isNotEmpty())
        assertEquals("1 cases, tête ${level(2).start.x},${level(2).start.y}, 0 impasses", trail())
    }
}
