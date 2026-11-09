package com.kcharignon.petitsjeux.screenshots

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import com.kcharignon.petitsjeux.core.AppUpdate
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.Screen
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.InMemoryProfileStore
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.screens.HubScreen
import com.kcharignon.petitsjeux.core.screens.ProfilesScreen
import com.kcharignon.petitsjeux.core.screens.SettingsCard
import com.kcharignon.petitsjeux.core.ui.Backdrop
import com.kcharignon.petitsjeux.games.parking.ParkingProgress
import com.kcharignon.petitsjeux.games.parking.engine.LevelGenerator
import com.kcharignon.petitsjeux.games.parking.engine.ParkingGame
import com.kcharignon.petitsjeux.games.parking.ui.BoardGeometry
import com.kcharignon.petitsjeux.games.parking.ui.LevelSelectScreen
import com.kcharignon.petitsjeux.games.parking.ui.PlayScreen
import com.kcharignon.petitsjeux.games.parking.ui.WIN_DELAY_MS
import com.kcharignon.petitsjeux.games.parking.ui.WinCard
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.GraphicsMode

/**
 * Rend les écrans en PNG (docs/screenshots) avec Robolectric, sans téléphone : `make android-screenshots`.
 * Joue aussi de vrais parcours (toucher des voitures, finir un niveau) : ce sont les tests de l'interface.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w390dp-h844dp-port-xhdpi")
class ScreenshotsTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private fun progress(levels: Int) = ParkingProgress(stars = (1..levels).associateWith { listOf(3, 3, 2, 1, 3)[it % 5] })

    private fun profile(slot: Int, name: String, avatar: Avatar, levels: Int = 0): Profile {
        val base = Profile.create(slot, name, avatar, now = 1L)!!
        return if (levels == 0) base else base.withGameSave(ParkingProgress.GAME_ID, progress(levels).toJson())
    }

    private val three = listOf(profile(0, "Léa", Avatar.CAT, 26), profile(1, "Hugo", Avatar.FOX, 6), profile(2, "Emma", Avatar.RABBIT))

    /** Les animations infinies (confettis, indice) empêchent l'écran d'être « au repos » : on pilote l'horloge à la main. */
    private fun show(name: String?, advanceMs: Long = 600, content: @Composable () -> Unit) {
        rule.mainClock.autoAdvance = false
        rule.setContent { content() }
        rule.mainClock.advanceTimeBy(advanceMs)
        if (name != null) rule.onRoot().captureRoboImage("../../docs/screenshots/$name.png")
    }

    @Test
    fun chooseThePlayer() = show("01-choix-joueur") {
        ProfilesScreen(profiles = three + listOf(null, null), onPlay = {}, onCreate = { _, _, _ -> })
    }

    @Test
    fun newPlayer() = show("02-nouveau-joueur") {
        ProfilesScreen(profiles = three + listOf(null, null), onPlay = {}, onCreate = { _, _, _ -> }, initialCreating = 3)
    }

    @Test
    fun hub() = show("03-accueil") {
        HubScreen(three[0], onPlay = {}, onSwitchPlayer = {}, onSettingsChange = {}, onDelete = {})
    }

    @Test
    fun settings() = show("04-reglages") {
        HubScreen(three[0], onPlay = {}, onSwitchPlayer = {}, onSettingsChange = {}, onDelete = {}, initialSettingsOpen = true)
    }

    @Test
    fun theUpdateButtonOpensTheDownloadInTheBrowser() {
        show(null) { HubScreen(three[0], onPlay = {}, onSwitchPlayer = {}, onSettingsChange = {}, onDelete = {}, initialSettingsOpen = true) }
        rule.onNodeWithText("Mettre à jour").performClick()
        rule.mainClock.advanceTimeBy(100)
        val opened = shadowOf(rule.activity).nextStartedActivity
        assertEquals(Intent.ACTION_VIEW, opened.action)
        assertEquals(AppUpdate.DOWNLOAD_URL, opened.dataString)
        // c'est bien l'adresse fixe de la dernière version
        assertTrue(AppUpdate.DOWNLOAD_URL.endsWith("/releases/download/android-latest/petitsjeux.apk"))
    }

    @Test
    fun settingsConfirmDelete() = show("05-effacer") {
        Backdrop { }
        SettingsCard(three[0], onSettingsChange = {}, onDelete = {}, onClose = {}, initialConfirm = true)
    }

    @Test
    fun levels() = show("06-niveaux") {
        LevelSelectScreen(progress(26), onPlay = {}, onBack = {})
    }

    @Test
    fun play() = show("07-jeu") {
        PlayScreen(LevelGenerator.level(61), hasNext = true, onFinished = {}, onNext = {}, onLevels = {})
    }

    @Test
    fun playFirstLevel() = show("07b-jeu-niveau-1") {
        PlayScreen(LevelGenerator.level(1), hasNext = true, onFinished = {}, onNext = {}, onLevels = {})
    }

    @Test
    fun win() = show("10-victoire", advanceMs = 1500) {
        Backdrop(overlay = { WinCard(LevelGenerator.level(27), stars = 3, cars = 13, bumps = 0, hasNext = true, onNext = {}, onReplay = {}, onLevels = {}) }) { }
    }

    // ---- vrais parcours : on touche des voitures

    private fun cellCenter(level: com.kcharignon.petitsjeux.games.parking.engine.Level, col: Int, row: Int): Offset {
        val size = rule.onNodeWithTag("board").fetchSemanticsNode().size
        val g = BoardGeometry.fit(level.cols, level.rows, size.width.toFloat(), size.height.toFloat())
        return Offset(g.origin.x + (col + 0.5f) * g.s, g.origin.y + (row + 0.5f) * g.s)
    }

    private fun tapCar(level: com.kcharignon.petitsjeux.games.parking.engine.Level, car: com.kcharignon.petitsjeux.games.parking.engine.Car) {
        val cell = car.cells.first()
        val at = cellCenter(level, cell.col, cell.row)
        rule.onNodeWithTag("board").performTouchInput { click(at) }
    }

    @Test
    fun aFreeCarDrivesOut() {
        val level = LevelGenerator.level(61)
        show(null) { PlayScreen(level, hasNext = true, onFinished = {}, onNext = {}, onLevels = {}) }
        val game = ParkingGame(level)
        val free = level.cars.first { game.blockerOf(it) == null }
        tapCar(level, free)
        rule.mainClock.advanceTimeBy(260)
        rule.onRoot().captureRoboImage("../../docs/screenshots/08-jeu-sortie.png")
    }

    @Test
    fun aBlockedCarHonks() {
        val level = LevelGenerator.level(61)
        show(null) { PlayScreen(level, hasNext = true, onFinished = {}, onNext = {}, onLevels = {}) }
        val game = ParkingGame(level)
        val blocked = level.cars.first { game.blockerOf(it) != null }
        tapCar(level, blocked)
        rule.mainClock.advanceTimeBy(330)
        rule.onRoot().captureRoboImage("../../docs/screenshots/09-jeu-pouet.png")
    }

    private fun playFirstLevelFromTheMenu(store: InMemoryProfileStore, mistakes: Int, impatient: Boolean = false, settleAtEnd: Boolean = true) {
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, ParkingProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(300)
        rule.onNodeWithTag("level-1").performClick()
        rule.mainClock.advanceTimeBy(300)

        val level = LevelGenerator.level(1)
        val game = ParkingGame(level)
        repeat(mistakes) {
            val blocked = game.cars.first { game.blockerOf(it) != null }
            tapCar(level, blocked)
            game.tap(blocked.id)
            if (impatient) {
                // un deuxième toucher alors que la voiture rebondit encore : il ne compte pas
                rule.mainClock.advanceTimeBy(60)
                tapCar(level, blocked)
            }
            rule.mainClock.advanceTimeBy(1200)
        }
        while (!game.isWon) {
            val car = game.hint()!!
            tapCar(level, car)
            game.tap(car.id)
            // (le toucher de la dernière voiture est suivi d'une seule image : la voiture roule encore)
            rule.mainClock.advanceTimeBy(if (game.isWon && !settleAtEnd) 16 else 1500)
        }
        // animation de sortie de la dernière voiture, puis la pause avant la fenêtre de victoire
        if (settleAtEnd) rule.mainClock.advanceTimeBy(1500 + WIN_DELAY_MS + 800)
    }

    @Test
    fun theFirstLevelCanBeFinishedAndIsSaved() {
        val store = InMemoryProfileStore(listOf(profile(0, "Léa", Avatar.CAT)))
        playFirstLevelFromTheMenu(store, mistakes = 0)
        rule.onRoot().captureRoboImage("../../docs/screenshots/11-victoire-niveau-1.png")
        val saved = ParkingProgress.fromJson(store.load(0)!!.gameSave(ParkingProgress.GAME_ID))
        assertEquals(3, saved.starsOf(1))
        assertEquals(2, saved.currentLevel)
        assertEquals(LevelGenerator.level(1).cars.size, saved.carsFreed)
        assertEquals(1, saved.played)
    }

    @Test
    fun theLevelIsSavedTheMomentTheLastCarLeavesEvenIfWeQuitDuringItsAnimation() {
        val store = InMemoryProfileStore(listOf(profile(0, "Léa", Avatar.CAT)))
        playFirstLevelFromTheMenu(store, mistakes = 0, settleAtEnd = false)
        // ni la voiture n'a fini de rouler, ni la fenêtre de victoire n'est là : c'est déjà enregistré
        val saved = ParkingProgress.fromJson(store.load(0)!!.gameSave(ParkingProgress.GAME_ID))
        assertEquals(3, saved.starsOf(1))
        assertEquals(1, saved.played)
    }

    @Test
    fun theWinCardWaitsABeatAfterTheLastCarLeaves() {
        val store = InMemoryProfileStore(listOf(profile(0, "Léa", Avatar.CAT)))
        playFirstLevelFromTheMenu(store, mistakes = 0, settleAtEnd = false)
        rule.mainClock.advanceTimeBy(1000)
        // la dernière voiture a roulé (au moins 470 ms) mais la pause de 800 ms n'est pas finie : pas encore d'écran de fin
        rule.onNodeWithText("Bravo !").assertDoesNotExist()
        rule.mainClock.advanceTimeBy(1500)
        rule.onNodeWithText("Bravo !").assertExists()
    }

    @Test
    fun tappingABouncingCarAgainDoesNotCostASecondFault() {
        val store = InMemoryProfileStore(listOf(profile(0, "Léa", Avatar.CAT)))
        playFirstLevelFromTheMenu(store, mistakes = 1, impatient = true)
        val saved = ParkingProgress.fromJson(store.load(0)!!.gameSave(ParkingProgress.GAME_ID))
        assertEquals(1, saved.bumps)
        assertEquals(2, saved.starsOf(1))
    }

    @Test
    fun aMistakeCostsAStarButTheLevelStillCounts() {
        val store = InMemoryProfileStore(listOf(profile(0, "Léa", Avatar.CAT)))
        playFirstLevelFromTheMenu(store, mistakes = 1)
        val saved = ParkingProgress.fromJson(store.load(0)!!.gameSave(ParkingProgress.GAME_ID))
        assertEquals(2, saved.starsOf(1))
        assertEquals(1, saved.bumps)
        assertEquals(2, saved.currentLevel)
    }
}
