package com.kcharignon.petitsjeux.games.mushrooms

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import com.kcharignon.petitsjeux.core.MiniGame
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.mushrooms.engine.Difficulty
import com.kcharignon.petitsjeux.games.mushrooms.ui.ShroomColors
import com.kcharignon.petitsjeux.games.mushrooms.ui.ShroomMenuScreen
import com.kcharignon.petitsjeux.games.mushrooms.ui.ShroomPlayScreen
import com.kcharignon.petitsjeux.games.mushrooms.ui.drawForest
import com.kcharignon.petitsjeux.games.mushrooms.ui.drawMushroom
import org.json.JSONObject

/** Champis Magiques : des champignons s'allument dans la forêt, il faut les toucher le plus vite possible. On ne perd jamais. */
object MushroomMiniGame : MiniGame {
    override val id = MushroomProgress.GAME_ID
    override val title = "Champis Magiques"
    override val tagline = "Touche vite les champignons qui s'allument !"

    override fun stars(save: JSONObject?): Int = MushroomProgress.fromJson(save).totalStars

    override fun progressLabel(save: JSONObject?): String {
        val p = MushroomProgress.fromJson(save)
        return if (p.played == 0) "Nouveau !" else "Record ${p.topScore}"
    }

    @Composable
    override fun Illustration(modifier: Modifier) {
        Canvas(modifier) {
            val corner = CornerRadius(26f * density)
            clipPath(Path().apply { addRoundRect(RoundRect(0f, 0f, size.width, size.height, corner)) }) {
                drawForest(0f)
                val r = size.height * 0.2f
                // une petite scène : des champignons blancs, trois allumés
                val spots = listOf(
                    Triple(0.12f, 0.66f, null), Triple(0.28f, 0.52f, 1), Triple(0.45f, 0.70f, 0), Triple(0.62f, 0.50f, null),
                    Triple(0.78f, 0.68f, 2), Triple(0.92f, 0.52f, null),
                )
                for ((k, s) in spots.withIndex()) {
                    val radius = r * (0.85f + 0.2f * ((k * 37) % 5) / 4f)
                    drawMushroom(size.width * s.first, size.height * s.second, radius, s.third?.let { ShroomColors[it] }, glow = 0.7f, variant = k)
                }
            }
        }
    }

    @Composable
    override fun Content(profile: Profile, onProfileChange: (Profile) -> Unit, onExit: () -> Unit) {
        val progress = MushroomProgress.fromJson(profile.gameSave(id))
        // null = le choix de la difficulté ; sinon l'indice de la difficulté en cours. À l'ouverture : directement la plus élevée déjà jouée.
        var playing by rememberSaveable { mutableStateOf<Int?>(progress.launchDifficulty.index) }
        val difficulty = playing?.let { Difficulty.of(it) }
        if (difficulty == null) {
            ShroomMenuScreen(progress, onPlay = { playing = it.index }, onBack = onExit)
        } else {
            ShroomPlayScreen(
                difficulty = difficulty,
                progress = progress,
                onFinished = { score, picked, streak ->
                    onProfileChange(profile.withGameSave(id, progress.withResult(difficulty, score, picked, streak).toJson()))
                },
                onNextDifficulty = { playing = it.index },
                onMenu = { playing = null },
                onExit = onExit,
            )
        }
    }
}
