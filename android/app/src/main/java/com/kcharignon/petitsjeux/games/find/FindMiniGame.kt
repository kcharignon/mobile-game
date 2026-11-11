package com.kcharignon.petitsjeux.games.find

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import com.kcharignon.petitsjeux.core.MiniGame
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.games.find.engine.FindDifficulty
import com.kcharignon.petitsjeux.games.find.ui.FindMenuScreen
import com.kcharignon.petitsjeux.games.find.ui.FindObject
import com.kcharignon.petitsjeux.games.find.ui.FindPlayScreen
import com.kcharignon.petitsjeux.games.find.ui.FindTheme
import com.kcharignon.petitsjeux.games.find.ui.drawFindBackdrop
import com.kcharignon.petitsjeux.games.find.ui.drawFindObject
import org.json.JSONObject

/** Cherche et Trouve : des objets à retrouver en haut, des objets éparpillés dessous (de 5 à 80), des manches au hasard en cinq difficultés. On ne perd jamais : se tromper ne fait perdre que des étoiles. */
object FindMiniGame : MiniGame {
    override val id = FindProgress.GAME_ID
    override val title = "Cherche et Trouve"
    override val tagline = "Retrouve les objets cachés parmi tous les autres !"

    override fun stars(save: JSONObject?): Int = FindProgress.fromJson(save).totalStars

    override fun progressLabel(save: JSONObject?): String {
        val p = FindProgress.fromJson(save)
        return if (p.played == 0) "Nouveau !" else "Difficulté ${p.launchDifficulty.index + 1} sur ${FindDifficulty.entries.size}"
    }

    @Composable
    override fun Illustration(modifier: Modifier) {
        Canvas(modifier) {
            val corner = CornerRadius(26f * density)
            clipPath(Path().apply { addRoundRect(RoundRect(0f, 0f, size.width, size.height, corner)) }) {
                drawFindBackdrop(CARD_THEME)
                val s = size.height * 0.46f
                val cy = size.height * 0.55f
                translate(size.width * 0.2f, cy) { rotate(-9f, Offset.Zero) { drawFindObject(FindObject.POMME, s) } }
                translate(size.width * 0.5f, cy - s * 0.04f) { drawFindObject(FindObject.BALLON, s) }
                translate(size.width * 0.8f, cy) { rotate(9f, Offset.Zero) { drawFindObject(FindObject.POUSSIN, s) } }
                drawMagnifier(Offset(size.width * 0.56f, cy + s * 0.08f), s * 0.46f)
            }
        }
    }

    @Composable
    override fun Content(profile: Profile, onProfileChange: (Profile) -> Unit, onExit: () -> Unit) {
        val progress = FindProgress.fromJson(profile.gameSave(id))
        // null = le choix de la difficulté ; sinon l'indice de la difficulté en cours. À l'ouverture : directement la plus élevée déjà jouée.
        var playing by rememberSaveable { mutableStateOf<Int?>(progress.launchDifficulty.index) }
        val difficulty = playing?.let { FindDifficulty.of(it) }
        if (difficulty == null) {
            FindMenuScreen(progress, onPlay = { playing = it.index }, onBack = onExit)
        } else {
            FindPlayScreen(
                difficulty = difficulty,
                progress = progress,
                onFinished = { stars, mistakes, found, flawless ->
                    onProfileChange(profile.withGameSave(id, progress.withResult(difficulty, stars, mistakes, found, flawless).toJson()))
                },
                onNextDifficulty = { playing = it.index },
                onMenu = { playing = null },
                onExit = onExit,
            )
        }
    }

    /** Le décor de la carte du hub : le lilas du premier monde, qui tranche sur le cadre orange. */
    private val CARD_THEME = FindTheme.of(0)
}

/** Une loupe : un anneau d'encre, un verre clair, un manche. */
private fun DrawScope.drawMagnifier(center: Offset, r: Float) {
    val ink = Pj.Ink
    val handleStart = center + Offset(r * 0.72f, r * 0.72f)
    val handleEnd = center + Offset(r * 1.45f, r * 1.45f)
    drawLine(ink, handleStart, handleEnd, r * 0.34f, StrokeCap.Round)
    drawLine(Color(0xFFFF8A1F), handleStart + Offset(r * 0.1f, r * 0.1f), handleEnd, r * 0.18f, StrokeCap.Round)
    drawCircle(Color.White.copy(alpha = 0.3f), r, center)
    drawCircle(ink, r, center, style = Stroke(r * 0.2f))
    drawArc(Color.White.copy(alpha = 0.8f), 200f, 60f, false, center - Offset(r * 0.66f, r * 0.66f), androidx.compose.ui.geometry.Size(r * 1.32f, r * 1.32f), style = Stroke(r * 0.09f, cap = StrokeCap.Round))
}

