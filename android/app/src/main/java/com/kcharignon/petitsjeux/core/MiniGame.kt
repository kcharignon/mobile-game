package com.kcharignon.petitsjeux.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.games.find.FindMiniGame
import com.kcharignon.petitsjeux.games.maze.MazeMiniGame
import com.kcharignon.petitsjeux.games.mushrooms.MushroomMiniGame
import com.kcharignon.petitsjeux.games.parking.ParkingMiniGame
import com.kcharignon.petitsjeux.games.road.RoadMiniGame
import org.json.JSONObject

/**
 * Le contrat d'un jeu de l'appli. Pour en ajouter un : implémenter cette interface et l'ajouter à [Games.all].
 * Le menu ne connaît rien d'autre : ni les règles, ni le format de la sauvegarde du jeu.
 */
interface MiniGame {
    /** Identifiant stable, clé de la part du jeu dans la sauvegarde (`games.<id>`). Ne jamais le changer. */
    val id: String
    val title: String
    val tagline: String

    /** Étoiles gagnées dans ce jeu, d'après sa part de la sauvegarde (`null` = jamais joué). Elles comptent pour le rang du joueur. */
    fun stars(save: JSONObject?): Int

    /** Où en est le joueur, en quelques mots (« Niveau 27 »), d'après sa part de la sauvegarde. */
    fun progressLabel(save: JSONObject?): String

    /** L'illustration de la carte du jeu dans le menu. */
    @Composable
    fun Illustration(modifier: Modifier)

    /**
     * Le jeu lui-même, plein écran. [onProfileChange] enregistre tout de suite le joueur modifié (sa part de sauvegarde à jour) ;
     * [onExit] revient au menu.
     */
    @Composable
    fun Content(profile: Profile, onProfileChange: (Profile) -> Unit, onExit: () -> Unit)
}

object Games {
    val all: List<MiniGame> = listOf(ParkingMiniGame, MushroomMiniGame, RoadMiniGame, MazeMiniGame, FindMiniGame)

    fun byId(id: String): MiniGame? = all.firstOrNull { it.id == id }

    /** Étoiles de tout le profil, tous jeux confondus. */
    fun totalStars(profile: Profile): Int = all.sumOf { it.stars(profile.gameSave(it.id)) }
}
