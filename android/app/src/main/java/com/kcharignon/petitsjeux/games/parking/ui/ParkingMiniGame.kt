package com.kcharignon.petitsjeux.games.parking

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.kcharignon.petitsjeux.core.MiniGame
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.games.parking.engine.LevelGenerator
import com.kcharignon.petitsjeux.games.parking.engine.Worlds
import com.kcharignon.petitsjeux.games.parking.ui.BoardTheme
import com.kcharignon.petitsjeux.games.parking.ui.LevelSelectScreen
import com.kcharignon.petitsjeux.games.parking.ui.PlayScreen
import com.kcharignon.petitsjeux.games.parking.ui.drawCar
import com.kcharignon.petitsjeux.games.parking.ui.drawCone
import org.json.JSONObject

/** Parking Malin : des voitures garées, à faire sortir une par une dans le bon ordre. */
object ParkingMiniGame : MiniGame {
    override val id = ParkingProgress.GAME_ID
    override val title = "Parking Malin"
    override val tagline = "Fais sortir les voitures dans le bon ordre !"

    override fun stars(save: JSONObject?): Int = ParkingProgress.fromJson(save).totalStars

    override fun progressLabel(save: JSONObject?): String {
        val p = ParkingProgress.fromJson(save)
        return if (p.levelsDone == 0) "Nouveau !" else if (p.allDone) "Tout réussi !" else "Niveau ${p.currentLevel}"
    }

    @Composable
    override fun Illustration(modifier: Modifier) {
        Canvas(modifier) {
            val theme = BoardTheme.of(0)
            drawRoundRect(theme.top, Offset.Zero, size, CornerRadius(26f * density))
            val s = minOf(size.width / 8.6f, size.height / 4.4f)
            val ox = (size.width - 7 * s) / 2
            val oy = (size.height - 4 * s) / 2
            fun at(col: Int, row: Int, len: Int, vertical: Boolean) =
                Offset(ox + col * s + (if (vertical) s else len * s) / 2, oy + row * s + (if (vertical) len * s else s) / 2)
            drawCar(2, Pj.CarColors[4], s, at(0, 0, 2, false), 90f)
            drawCar(2, Pj.CarColors[2], s, at(3, 0, 2, true), 180f)
            drawCar(3, Pj.CarColors[5], s, at(5, 0, 3, true), 180f)
            drawCar(2, Pj.CarColors[0], s, at(6, 1, 2, true), 0f)
            drawCar(2, Pj.CarColors[6], s, at(1, 2, 2, false), 270f)
            drawCar(2, Pj.CarColors[3], s, at(3, 3, 2, false), 90f)
            drawCone(ox - 0.7f * s, oy + 3.7f * s, s * 0.6f)
            drawCone(ox + 7.7f * s, oy + 3.7f * s, s * 0.6f)
        }
    }

    @Composable
    override fun Content(profile: Profile, onProfileChange: (Profile) -> Unit, onExit: () -> Unit) {
        // null = le choix du niveau ; sinon le numéro du niveau en cours
        var playing by rememberSaveable { mutableStateOf<Int?>(null) }
        val progress = ParkingProgress.fromJson(profile.gameSave(id))
        val level = playing
        if (level == null) {
            LevelSelectScreen(progress, onPlay = { playing = it }, onBack = onExit)
        } else {
            PlayScreen(
                level = LevelGenerator.level(level),
                hasNext = level < Worlds.LEVEL_COUNT,
                onFinished = { bumps ->
                    val l = LevelGenerator.level(level)
                    val updated = progress.withResult(level, com.kcharignon.petitsjeux.games.parking.engine.starsForBumps(bumps), l.cars.size, bumps)
                    onProfileChange(profile.withGameSave(id, updated.toJson()))
                },
                onNext = { playing = level + 1 },
                onLevels = { playing = null },
            )
        }
    }
}
