package com.kcharignon.petitsjeux.games.road

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.LockLandscape
import com.kcharignon.petitsjeux.core.MiniGame
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.games.road.engine.RoadLevel
import com.kcharignon.petitsjeux.games.road.engine.RoadLevels
import com.kcharignon.petitsjeux.games.road.ui.RoadMenuScreen
import com.kcharignon.petitsjeux.games.road.ui.RoadPlayScreen
import com.kcharignon.petitsjeux.games.road.ui.drawRoadCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/** Poussin Traverse : un poussin traverse des routes de plus en plus larges et chargées. Toujours en paysage. On ne perd jamais, on perd seulement des points. */
object RoadMiniGame : MiniGame {
    override val id = RoadProgress.GAME_ID
    override val title = "Poussin Traverse"
    override val tagline = "Aide le poussin à traverser la route !"

    override fun stars(save: JSONObject?): Int = RoadProgress.fromJson(save).totalStars

    override fun progressLabel(save: JSONObject?): String {
        val p = RoadProgress.fromJson(save)
        return if (p.levelsDone == 0) "Nouveau !" else if (p.allDone) "Tout réussi !" else "Niveau ${p.currentLevel}"
    }

    @Composable
    override fun Illustration(modifier: Modifier) {
        Canvas(modifier) {
            val corner = CornerRadius(26f * density)
            clipPath(Path().apply { addRoundRect(RoundRect(0f, 0f, size.width, size.height, corner)) }) {
                drawRoadCard(1.2f)
            }
        }
    }

    @Composable
    override fun Content(profile: Profile, onProfileChange: (Profile) -> Unit, onExit: () -> Unit) {
        // tout le jeu, menu compris, est en paysage ; en sortant, l'appli retrouve son portrait
        LockLandscape()
        val progress = RoadProgress.fromJson(profile.gameSave(id))
        // null = le choix du niveau ; sinon le numéro du niveau en cours. À l'ouverture : directement le prochain niveau à faire.
        var playing by rememberSaveable { mutableStateOf(progress.launchLevel) }
        val n = playing
        if (n == null) {
            RoadMenuScreen(progress, onPlay = { playing = it }, onBack = onExit)
        } else {
            LevelHost(n, progress, onFinished = { level, score, stars, hits ->
                onProfileChange(profile.withGameSave(id, progress.withResult(level, stars, score, hits).toJson()))
            }, onNext = { playing = it }, onMenu = { playing = null }, onExit = onExit)
        }
    }
}

/** Prépare la route du niveau [n] (un calcul d'un instant, hors du fil principal) puis lance la traversée. */
@Composable
private fun LevelHost(
    n: Int,
    progress: RoadProgress,
    onFinished: (level: Int, score: Int, stars: Int, hits: Int) -> Unit,
    onNext: (Int) -> Unit,
    onMenu: () -> Unit,
    onExit: () -> Unit,
) {
    var level by remember(n) { mutableStateOf<RoadLevel?>(RoadLevels.cached(n)) }
    LaunchedEffect(n) {
        if (level == null) level = withContext(Dispatchers.Default) { RoadLevels.level(n) }
    }
    val ready = level
    if (ready == null) {
        // même pendant ce court instant, « retour » ramène à l'accueil (au lieu de quitter l'appli)
        BackHandler(onBack = onExit)
        Box(Modifier.fillMaxSize().background(Pj.Sun), contentAlignment = Alignment.Center) {
            PjText("Je prépare la route…", size = 26.sp, weight = FontWeight.Bold, color = Pj.Ink)
        }
    } else {
        RoadPlayScreen(
            level = ready,
            progress = progress,
            onFinished = { score, stars, hits -> onFinished(ready.number, score, stars, hits) },
            onNext = onNext,
            onMenu = onMenu,
            onExit = onExit,
        )
    }
}
