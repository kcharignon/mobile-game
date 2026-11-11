package com.kcharignon.petitsjeux.games.maze

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import com.kcharignon.petitsjeux.R
import com.kcharignon.petitsjeux.core.MiniGame
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.maze.engine.Cell
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevel
import com.kcharignon.petitsjeux.games.maze.engine.MazeLevels
import com.kcharignon.petitsjeux.games.maze.ui.BoardState
import com.kcharignon.petitsjeux.games.maze.ui.MazeLayout
import com.kcharignon.petitsjeux.games.maze.ui.MazeMenuScreen
import com.kcharignon.petitsjeux.games.maze.ui.MazePlayScreen
import com.kcharignon.petitsjeux.games.maze.ui.MazeScene
import com.kcharignon.petitsjeux.games.maze.ui.MazeTheme
import com.kcharignon.petitsjeux.games.maze.ui.drawMazeBoard
import com.kcharignon.petitsjeux.games.maze.ui.drawSea
import org.json.JSONObject

/** Poisson Perdu : un poisson-clown retrouve sa maison en traçant au doigt le chemin à travers un labyrinthe de corail. On ne perd jamais : les impasses ne font perdre que des étoiles. */
object MazeMiniGame : MiniGame {
    override val id = MazeProgress.GAME_ID
    override val title = "Poisson Perdu"
    override val tagline = "Aide le poisson à retrouver sa maison !"

    override fun stars(save: JSONObject?): Int = MazeProgress.fromJson(save).totalStars

    override fun progressLabel(save: JSONObject?): String {
        val p = MazeProgress.fromJson(save)
        return if (p.levelsDone == 0) "Nouveau !" else if (p.allDone) "Tout réussi !" else "Niveau ${p.currentLevel}"
    }

    @Composable
    override fun Illustration(modifier: Modifier) {
        Canvas(modifier) {
            val corner = CornerRadius(26f * density)
            clipPath(Path().apply { addRoundRect(RoundRect(0f, 0f, size.width, size.height, corner)) }) {
                drawSea(MazeTheme.of(1), 1.2f)
                val layout = MazeLayout(MazeCard.level, size.width, size.height, margin = 14f * density)
                drawMazeBoard(MazeCard.scene, layout, BoardState(trail = MazeCard.level.solution.take(4), time = 1.2f, showHead = false, showBuoy = false))
            }
        }
    }

    @Composable
    override fun Content(profile: Profile, onProfileChange: (Profile) -> Unit, onExit: () -> Unit) {
        val levels = MazeData.levels(LocalContext.current)
        val progress = MazeProgress.fromJson(profile.gameSave(id))
        // null = le choix du niveau ; sinon le numéro du niveau en cours. À l'ouverture : directement le prochain niveau à faire.
        var playing by rememberSaveable { mutableStateOf(progress.launchLevel) }
        val n = playing
        if (n == null) {
            MazeMenuScreen(progress, onPlay = { playing = it }, onBack = onExit)
        } else {
            MazePlayScreen(
                level = levels[n - 1],
                progress = progress,
                onFinished = { stars, detours -> onProfileChange(profile.withGameSave(id, progress.withResult(n, stars, detours).toJson())) },
                onNext = { playing = it },
                onMenu = { playing = null },
                onExit = onExit,
            )
        }
    }
}

/** Les 100 niveaux, lus une fois du fichier `res/raw/maze_levels.json` (fabriqué à l'avance par `tools/mazes/make-mazes.js`). */
object MazeData {
    @Volatile private var cache: List<MazeLevel>? = null

    fun levels(context: Context): List<MazeLevel> = cache ?: synchronized(this) {
        cache ?: MazeLevels.parse(context.applicationContext.resources.openRawResource(R.raw.maze_levels).bufferedReader().use { it.readText() }).also { cache = it }
    }
}

/** Le petit morceau de labyrinthe (7 x 3) de la carte du jeu dans le menu : même dessin que la maquette `12-recif-accueil`. */
internal object MazeCard {
    val level: MazeLevel = MazeLevels.level(
        0, 1, 7, 3, Cell(1, 2), Cell(5, 0),
        listOf(
            "+--+--+--+--+--+  +--+",
            "|        |           |",
            "+--+  +--+  +--+  +--+",
            "|              |     |",
            "+  +--+  +--+--+--+  +",
            "|  |        |        |",
            "+--+  +--+--+--+--+--+",
        ),
    )
    val scene: MazeScene by lazy { MazeScene(level) }
}
