package com.kcharignon.petitsjeux.games.find

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import com.kcharignon.petitsjeux.R
import com.kcharignon.petitsjeux.core.MiniGame
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.games.find.engine.FindLevel
import com.kcharignon.petitsjeux.games.find.engine.FindLevels
import com.kcharignon.petitsjeux.games.find.ui.FindMenuScreen
import com.kcharignon.petitsjeux.games.find.ui.FindObject
import com.kcharignon.petitsjeux.games.find.ui.FindPlayScreen
import com.kcharignon.petitsjeux.games.find.ui.FindTheme
import com.kcharignon.petitsjeux.games.find.ui.drawFindBackdrop
import com.kcharignon.petitsjeux.games.find.ui.drawFindObject
import org.json.JSONObject

/** Cherche et Trouve : un modèle en haut, des objets éparpillés dessous, il faut retrouver le même. On ne perd jamais : se tromper ne fait perdre que des étoiles. */
object FindMiniGame : MiniGame {
    override val id = FindProgress.GAME_ID
    override val title = "Cherche et Trouve"
    override val tagline = "Retrouve le bon objet parmi les autres !"

    override fun stars(save: JSONObject?): Int = FindProgress.fromJson(save).totalStars

    override fun progressLabel(save: JSONObject?): String {
        val p = FindProgress.fromJson(save)
        return if (p.levelsDone == 0) "Nouveau !" else if (p.allDone) "Tout réussi !" else "Niveau ${p.currentLevel}"
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
        val levels = FindData.levels(LocalContext.current)
        val progress = FindProgress.fromJson(profile.gameSave(id))
        // null = le choix du niveau ; sinon le numéro du niveau en cours. À l'ouverture : directement le prochain niveau à faire.
        var playing by rememberSaveable { mutableStateOf(progress.launchLevel) }
        val n = playing
        if (n == null) {
            FindMenuScreen(progress, onPlay = { playing = it }, onBack = onExit)
        } else {
            FindPlayScreen(
                level = levels[n - 1],
                progress = progress,
                onFinished = { stars, mistakes -> onProfileChange(profile.withGameSave(id, progress.withResult(n, stars, mistakes).toJson())) },
                onNext = { playing = it },
                onLevels = { playing = null },
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

/** Les 100 niveaux, lus une fois du fichier `res/raw/find_levels.json` (fabriqué à l'avance par `tools/find/make-levels.js`). */
object FindData {
    @Volatile private var cache: List<FindLevel>? = null

    fun levels(context: Context): List<FindLevel> = cache ?: synchronized(this) {
        cache ?: FindLevels.parse(context.applicationContext.resources.openRawResource(R.raw.find_levels).bufferedReader().use { it.readText() }).also { cache = it }
    }
}
