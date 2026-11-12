package com.kcharignon.petitsjeux.games.plane

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import com.kcharignon.petitsjeux.core.MiniGame
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.plane.engine.PlaneDifficulty
import com.kcharignon.petitsjeux.games.plane.ui.PlaneColors
import com.kcharignon.petitsjeux.games.plane.ui.PlaneMenuScreen
import com.kcharignon.petitsjeux.games.plane.ui.PlanePlayScreen
import com.kcharignon.petitsjeux.games.plane.ui.PlaneThemes
import com.kcharignon.petitsjeux.games.plane.ui.drawPlaneCover
import org.json.JSONObject

/** Super Avion : on glisse le doigt de gauche à droite pour que l'avion évite oiseaux, avions, montgolfières et parapentes, traverse les anneaux lumineux et atterrisse à l'aéroport. On ne perd jamais. */
object PlaneMiniGame : MiniGame {
    override val id = PlaneProgress.GAME_ID
    override val title = "Super Avion"
    override val tagline = "Glisse ton doigt pour piloter l'avion !"

    override fun stars(save: JSONObject?): Int = PlaneProgress.fromJson(save).totalStars

    override fun progressLabel(save: JSONObject?): String {
        val p = PlaneProgress.fromJson(save)
        return if (p.played == 0) "Nouveau !" else "Record ${p.topScore}"
    }

    @Composable
    override fun Illustration(modifier: Modifier) {
        Canvas(modifier) {
            val corner = CornerRadius(26f * density)
            clipPath(Path().apply { addRoundRect(RoundRect(0f, 0f, size.width, size.height, corner)) }) {
                drawPlaneCover(PlaneThemes.Country, PlaneColors[0])
            }
        }
    }

    @Composable
    override fun Content(profile: Profile, onProfileChange: (Profile) -> Unit, onExit: () -> Unit) {
        val progress = PlaneProgress.fromJson(profile.gameSave(id))
        // null = le choix de la difficulté ; sinon l'indice de la difficulté en cours. À l'ouverture : directement la plus élevée déjà jouée.
        var playing by rememberSaveable { mutableStateOf<Int?>(progress.launchDifficulty.index) }
        val difficulty = playing?.let { PlaneDifficulty.of(it) }
        val color = PlaneColors[profile.avatar.ordinal % PlaneColors.size]
        if (difficulty == null) {
            PlaneMenuScreen(progress, color, onPlay = { playing = it.index }, onBack = onExit)
        } else {
            PlanePlayScreen(
                difficulty = difficulty,
                progress = progress,
                color = color,
                onFinished = { stars, score, hits, rolls ->
                    onProfileChange(profile.withGameSave(id, progress.withResult(difficulty, stars, score, hits, rolls).toJson()))
                },
                onNextDifficulty = { playing = it.index },
                onMenu = { playing = null },
                onExit = onExit,
            )
        }
    }
}
