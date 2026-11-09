package com.kcharignon.petitsjeux.screenshots

import androidx.activity.ComponentActivity
import java.io.File
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.update.FakeInstaller
import com.kcharignon.petitsjeux.core.update.FakeUpdateApi
import com.kcharignon.petitsjeux.core.update.UpdateController
import com.kcharignon.petitsjeux.core.update.release
import com.kcharignon.petitsjeux.core.Screen
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.InMemoryProfileStore
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.screens.HubScreen
import com.kcharignon.petitsjeux.core.screens.ProfilesScreen
import com.kcharignon.petitsjeux.core.screens.SettingsCard
import com.kcharignon.petitsjeux.core.ui.Backdrop
import com.kcharignon.petitsjeux.games.parking.ParkingProgress
import com.kcharignon.petitsjeux.games.parking.engine.Car
import com.kcharignon.petitsjeux.games.parking.engine.Level
import com.kcharignon.petitsjeux.games.parking.engine.LevelGenerator
import com.kcharignon.petitsjeux.games.parking.engine.Move
import com.kcharignon.petitsjeux.games.parking.engine.ParkingGame
import com.kcharignon.petitsjeux.games.parking.engine.Solver
import com.kcharignon.petitsjeux.games.parking.ui.BoardGeometry
import com.kcharignon.petitsjeux.games.parking.ui.LevelSelectScreen
import com.kcharignon.petitsjeux.games.parking.ui.PlayScreen
import com.kcharignon.petitsjeux.games.parking.ui.WIN_DELAY_MS
import com.kcharignon.petitsjeux.games.parking.ui.WinCard
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
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

    // ---- mise à jour de l'appli (réseau et installateur simulés)

    private fun updateController(api: FakeUpdateApi = FakeUpdateApi(release(2)), installer: FakeInstaller = FakeInstaller()) =
        UpdateController(api, installer, 1, File(System.getProperty("java.io.tmpdir"), "pj-maj-${System.nanoTime()}"), CoroutineScope(Dispatchers.Unconfined))

    private fun hubWith(update: UpdateController, settings: Boolean = false): @Composable () -> Unit = {
        HubScreen(three[0], onPlay = {}, onSwitchPlayer = {}, onSettingsChange = {}, onDelete = {}, update = update, initialSettingsOpen = settings)
    }

    @Test
    fun aNewVersionShowsOneBigButtonOnTheHub() {
        val c = updateController().also { it.check(silent = true) }
        show("12-maj-disponible", content = hubWith(c))
        rule.onNodeWithText("Nouvelle version ! Mettre à jour").assertExists()
    }

    @Test
    fun noBannerWhenUpToDate() {
        val c = updateController(FakeUpdateApi(release(1))).also { it.check(silent = true) }
        show(null, content = hubWith(c))
        rule.onNodeWithText("Nouvelle version ! Mettre à jour").assertDoesNotExist()
    }

    @Test
    fun theDownloadShowsItsProgress() {
        val c = updateController(FakeUpdateApi(release(2), holdDownload = true)).also { it.check(silent = true); it.update() }
        show("13-maj-telechargement", content = hubWith(c))
        rule.onNodeWithText("Je télécharge la nouvelle version… 45 %").assertExists()
    }

    @Test
    fun oneTapDownloadsThenAsksAndroidToInstall() {
        val installer = FakeInstaller(allowed = true)
        val c = updateController(installer = installer).also { it.check(silent = true) }
        show(null, content = hubWith(c))
        rule.onNodeWithText("Nouvelle version ! Mettre à jour").performClick()
        rule.mainClock.advanceTimeBy(200)
        assertEquals(1, installer.installed.size)
        rule.onNodeWithText("Touche « Installer » dans la fenêtre d'Android.").assertExists()
        rule.onRoot().captureRoboImage("../../docs/screenshots/14-maj-installer.png")
    }

    @Test
    fun androidsConsentIsAskedOnceAndExplained() {
        val installer = FakeInstaller(allowed = false)
        val c = updateController(installer = installer).also { it.check(silent = true) }
        show(null, content = hubWith(c))
        rule.onNodeWithText("Nouvelle version ! Mettre à jour").performClick()
        rule.mainClock.advanceTimeBy(200)
        assertTrue(installer.installed.isEmpty())
        rule.onNodeWithText("Android demande ton accord, une seule fois. Active « Autoriser » puis reviens ici.").assertExists()
        rule.onRoot().captureRoboImage("../../docs/screenshots/15-maj-autoriser.png")
        rule.onNodeWithText("Autoriser").performClick()
        assertEquals(1, installer.permissionRequests)
    }

    @Test
    fun theSettingsCanLookForAnUpdateByHand() {
        val c = updateController(FakeUpdateApi(release(1)))
        show("16-reglages-maj", content = hubWith(c, settings = true))
        rule.onNodeWithText("Chercher une mise à jour").performClick()
        rule.mainClock.advanceTimeBy(100)
        rule.onNodeWithText("C'est la dernière version, bravo !").assertExists()
    }

    @Test
    fun withoutNetworkTheSettingsSayItKindly() {
        val c = updateController(FakeUpdateApi(release(2), failFetch = true))
        show(null, content = hubWith(c, settings = true))
        rule.onNodeWithText("Chercher une mise à jour").performClick()
        rule.mainClock.advanceTimeBy(100)
        rule.onNodeWithText("Pas de connexion. Réessaie plus tard.").assertExists()
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

    // ---- vrais parcours : on touche et on glisse des véhicules

    private fun geometry(level: Level): BoardGeometry {
        val size = rule.onNodeWithTag("board").fetchSemanticsNode().size
        return BoardGeometry.fit(level.cols, level.rows, size.width.toFloat(), size.height.toFloat())
    }

    private fun cellCenter(level: Level, col: Int, row: Int): Offset {
        val g = geometry(level)
        return Offset(g.origin.x + (col + 0.5f) * g.s, g.origin.y + (row + 0.5f) * g.s)
    }

    /** Un toucher (marche avant) ou un glissement le long du véhicule dans le sens du mouvement. */
    private fun perform(level: Level, car: Car, move: Move, swipeEvenForward: Boolean = false) {
        val cell = car.cells[car.cells.size / 2]
        val start = cellCenter(level, cell.col, cell.row)
        if (move == Move.FORWARD && !swipeEvenForward) {
            rule.onNodeWithTag("board").performTouchInput { click(start) }
        } else {
            val d = car.directionOf(move)
            val distance = geometry(level).s * 0.9f
            val end = Offset(start.x + d.dc * distance, start.y + d.dr * distance)
            rule.onNodeWithTag("board").performTouchInput { swipe(start, end, durationMillis = 150) }
        }
    }

    private fun tapCar(level: Level, car: Car) = perform(level, car, Move.FORWARD)

    @Test
    fun aFreeCarDrivesOut() {
        val level = LevelGenerator.level(61)
        show(null) { PlayScreen(level, hasNext = true, onFinished = {}, onNext = {}, onLevels = {}) }
        val game = ParkingGame(level)
        val free = level.cars.first { game.canMove(it, Move.FORWARD) }
        tapCar(level, free)
        rule.mainClock.advanceTimeBy(260)
        rule.onRoot().captureRoboImage("../../docs/screenshots/08-jeu-sortie.png")
    }

    @Test
    fun aBlockedCarHonks() {
        val level = LevelGenerator.level(61)
        show(null) { PlayScreen(level, hasNext = true, onFinished = {}, onNext = {}, onLevels = {}) }
        val game = ParkingGame(level)
        val blocked = level.cars.first { !game.canMove(it, Move.FORWARD) }
        tapCar(level, blocked)
        rule.mainClock.advanceTimeBy(330)
        rule.onRoot().captureRoboImage("../../docs/screenshots/09-jeu-pouet.png")
    }

    /** Joue le niveau [index] depuis le menu des niveaux (les précédents doivent être réussis). Renvoie les sens utilisés pour gagner. */
    private fun playLevelFromTheMenu(
        store: InMemoryProfileStore,
        index: Int,
        mistakes: Int = 0,
        impatient: Boolean = false,
        settleAtEnd: Boolean = true,
        swipeEvenForward: Boolean = false,
        reverseFirst: Boolean = false,
    ): List<Move> {
        rule.mainClock.autoAdvance = false
        rule.setContent { PetitsJeuxApp(store = store, initialScreen = Screen.Game(0, ParkingProgress.GAME_ID)) }
        rule.mainClock.advanceTimeBy(300)
        rule.onNodeWithTag("level-$index").performClick()
        rule.mainClock.advanceTimeBy(300)

        val level = LevelGenerator.level(index)
        val game = ParkingGame(level)
        repeat(mistakes) {
            val blocked = game.cars.first { !game.canMove(it, Move.FORWARD) }
            tapCar(level, blocked)
            game.tap(blocked.id)
            if (impatient) {
                // un deuxième toucher alors que le véhicule rebondit encore : il ne compte pas
                rule.mainClock.advanceTimeBy(60)
                tapCar(level, blocked)
            }
            rule.mainClock.advanceTimeBy(1200)
        }
        val used = mutableListOf<Move>()
        if (reverseFirst) {
            // un véhicule bloqué devant mais libre derrière : on le glisse vers l'arrière
            val car = game.cars.first { !game.canMove(it, Move.FORWARD) && game.canMove(it, Move.BACKWARD) }
            perform(level, car, Move.BACKWARD)
            game.tap(car.id, Move.BACKWARD)
            used += Move.BACKWARD
            rule.mainClock.advanceTimeBy(1500)
        }
        while (!game.isWon) {
            val hint = game.hint()!!
            perform(level, hint.car, hint.move, swipeEvenForward)
            game.tap(hint.car.id, hint.move)
            used += hint.move
            // (le geste sur le dernier véhicule est suivi d'une seule image : le véhicule roule encore)
            rule.mainClock.advanceTimeBy(if (game.isWon && !settleAtEnd) 16 else 1500)
        }
        // animation de sortie du dernier véhicule, puis la pause avant la fenêtre de victoire
        if (settleAtEnd) rule.mainClock.advanceTimeBy(1500 + WIN_DELAY_MS + 800)
        return used
    }

    private fun playFirstLevelFromTheMenu(store: InMemoryProfileStore, mistakes: Int, impatient: Boolean = false, settleAtEnd: Boolean = true) =
        playLevelFromTheMenu(store, 1, mistakes, impatient, settleAtEnd)

    @Test
    fun aVehicleCanBeSlidBackwardToReverseOut() {
        // le niveau 2 a un véhicule qui ne peut sortir qu'en reculant : on le glisse vers l'arrière, puis on finit avec les indices
        val store = InMemoryProfileStore(listOf(profile(0, "Léa", Avatar.CAT, levels = 1)))
        val used = playLevelFromTheMenu(store, 2, reverseFirst = true)
        assertEquals(Move.BACKWARD, used.first())
        val saved = ParkingProgress.fromJson(store.load(0)!!.gameSave(ParkingProgress.GAME_ID))
        assertEquals(3, saved.starsOf(2))
        assertEquals(0, saved.bumps)
    }

    @Test
    fun slidingForwardWorksToo() {
        val store = InMemoryProfileStore(listOf(profile(0, "Léa", Avatar.CAT)))
        playLevelFromTheMenu(store, 1, swipeEvenForward = true)
        val saved = ParkingProgress.fromJson(store.load(0)!!.gameSave(ParkingProgress.GAME_ID))
        assertEquals(3, saved.starsOf(1))
    }

    @Test
    fun aLevelWithConesCanBePlayedToTheEnd() {
        val store = InMemoryProfileStore(listOf(profile(0, "Léa", Avatar.CAT, levels = 61)))
        playLevelFromTheMenu(store, 62)
        val saved = ParkingProgress.fromJson(store.load(0)!!.gameSave(ParkingProgress.GAME_ID))
        assertEquals(3, saved.starsOf(62))
    }

    @Test
    fun theConesAndTheBigCarParkAreDrawn() {
        show("17-jeu-plots") { PlayScreen(LevelGenerator.level(64), hasNext = true, onFinished = {}, onNext = {}, onLevels = {}) }
    }

    @Test
    fun theBigCarPark() = show("18-jeu-grand-parking") {
        PlayScreen(LevelGenerator.level(165), hasNext = true, onFinished = {}, onNext = {}, onLevels = {})
    }

    @Test
    fun aPickupDropsItsCrateAndItShowsOnTheBoard() {
        // un niveau où un pick-up peut partir tout de suite, sans mener à une impasse
        fun safeMove(level: Level, pickup: Car) =
            Move.entries.firstOrNull { m -> ParkingGame(level).canMove(pickup, m) && !ParkingGame(level).also { it.tap(pickup.id, m) }.isStuck }
        val level = (181..240).map { LevelGenerator.level(it) }.first { l -> l.cars.any { it.carrier && safeMove(l, it) != null } }
        val pickup = level.cars.first { it.carrier && safeMove(level, it) != null }
        val move = safeMove(level, pickup)!!
        show(null) { PlayScreen(level, hasNext = true, onFinished = {}, onNext = {}, onLevels = {}) }
        perform(level, pickup, move)
        rule.mainClock.advanceTimeBy(700)
        rule.onRoot().captureRoboImage("../../docs/screenshots/19-jeu-caisse.png")
    }

    @Test
    fun walkingIntoACrateTrapOffersToStartAgain() {
        // un niveau piégé : un pick-up qui part du mauvais côté laisse sa caisse là où elle ferme le dernier passage
        val level = (181..240).map { LevelGenerator.level(it) }.first { Solver.analyse(it).traps >= 1 }
        val pickup = level.cars.first { it.carrier && Move.entries.any { m -> ParkingGame(level).canMove(it, m) && ParkingGame(level).also { g -> g.tap(it.id, m) }.isStuck } }
        val bad = Move.entries.first { m -> ParkingGame(level).canMove(pickup, m) && ParkingGame(level).also { g -> g.tap(pickup.id, m) }.isStuck }
        show(null) { PlayScreen(level, hasNext = true, onFinished = {}, onNext = {}, onLevels = {}) }
        perform(level, pickup, bad)
        rule.mainClock.advanceTimeBy(3000)
        rule.onNodeWithText("Oups, bloqué !").assertExists()
        rule.onRoot().captureRoboImage("../../docs/screenshots/21-jeu-bloque.png")
        rule.onNodeWithText("Recommencer").performClick()
        rule.mainClock.advanceTimeBy(300)
        rule.onNodeWithText("Oups, bloqué !").assertDoesNotExist()
    }

    @Test
    fun theHintShowsWhichWayToSlide() {
        val level = LevelGenerator.level(165)
        show(null) { PlayScreen(level, hasNext = true, onFinished = {}, onNext = {}, onLevels = {}) }
        rule.onNodeWithText("Indice").performClick()
        rule.mainClock.advanceTimeBy(350)
        rule.onRoot().captureRoboImage("../../docs/screenshots/20-jeu-indice.png")
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
