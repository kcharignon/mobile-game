package com.kcharignon.petitsjeux.games.skate

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
import com.kcharignon.petitsjeux.games.skate.engine.SkateDifficulty
import com.kcharignon.petitsjeux.games.skate.ui.SkateHelmets
import com.kcharignon.petitsjeux.games.skate.ui.SkateMenuScreen
import com.kcharignon.petitsjeux.games.skate.ui.SkatePlayScreen
import com.kcharignon.petitsjeux.games.skate.ui.SkateThemes
import com.kcharignon.petitsjeux.games.skate.ui.drawSkateCover
import org.json.JSONObject

/** Super Skate : on glisse le doigt de gauche à droite pour que le skateur évite piétons et lampadaires et prenne les rampes. On ne perd jamais. */
object SkateMiniGame : MiniGame {
    override val id = SkateProgress.GAME_ID
    override val title = "Super Skate"
    override val tagline = "Glisse ton doigt pour éviter les obstacles !"

    override fun stars(save: JSONObject?): Int = SkateProgress.fromJson(save).totalStars

    override fun progressLabel(save: JSONObject?): String {
        val p = SkateProgress.fromJson(save)
        return if (p.played == 0) "Nouveau !" else "Record ${p.topScore}"
    }

    @Composable
    override fun Illustration(modifier: Modifier) {
        Canvas(modifier) {
            val corner = CornerRadius(26f * density)
            clipPath(Path().apply { addRoundRect(RoundRect(0f, 0f, size.width, size.height, corner)) }) {
                drawSkateCover(SkateThemes.Seaside, SkateHelmets[0])
            }
        }
    }

    @Composable
    override fun Content(profile: Profile, onProfileChange: (Profile) -> Unit, onExit: () -> Unit) {
        val progress = SkateProgress.fromJson(profile.gameSave(id))
        // null = le choix de la difficulté ; sinon l'indice de la difficulté en cours. À l'ouverture : directement la plus élevée déjà jouée.
        var playing by rememberSaveable { mutableStateOf<Int?>(progress.launchDifficulty.index) }
        val difficulty = playing?.let { SkateDifficulty.of(it) }
        val helmet = SkateHelmets[profile.avatar.ordinal % SkateHelmets.size]
        if (difficulty == null) {
            SkateMenuScreen(progress, helmet, onPlay = { playing = it.index }, onBack = onExit)
        } else {
            SkatePlayScreen(
                difficulty = difficulty,
                progress = progress,
                helmet = helmet,
                onFinished = { stars, score, hits, jumps ->
                    onProfileChange(profile.withGameSave(id, progress.withResult(difficulty, stars, score, hits, jumps).toJson()))
                },
                onNextDifficulty = { playing = it.index },
                onMenu = { playing = null },
                onExit = onExit,
            )
        }
    }
}
