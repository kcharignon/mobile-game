package com.kcharignon.petitsjeux.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.save.ProfileStore
import com.kcharignon.petitsjeux.core.save.Settings
import com.kcharignon.petitsjeux.core.screens.HubScreen
import com.kcharignon.petitsjeux.core.screens.ProfilesScreen
import com.kcharignon.petitsjeux.core.update.UpdateController
import com.kcharignon.petitsjeux.core.update.installedVersion

/** Où est l'utilisateur dans l'appli. */
sealed interface Screen {
    /** « Qui joue ? » : l'écran d'accueil, toujours le premier. */
    data object Profiles : Screen

    /** L'accueil d'un joueur (ses jeux). */
    data class Hub(val slot: Int) : Screen

    /** Un jeu, plein écran. */
    data class Game(val slot: Int, val gameId: String) : Screen

    companion object {
        /** Pour retrouver l'écran après un changement de configuration ou un retour de l'appli en arrière-plan. */
        val Saver: Saver<Screen, String> = Saver(
            save = {
                when (it) {
                    Profiles -> "profiles"
                    is Hub -> "hub:${it.slot}"
                    is Game -> "game:${it.slot}:${it.gameId}"
                }
            },
            restore = { text ->
                val parts = text.split(":")
                when (parts[0]) {
                    "hub" -> parts.getOrNull(1)?.toIntOrNull()?.let { Hub(it) }
                    "game" -> parts.getOrNull(1)?.toIntOrNull()?.let { slot -> parts.getOrNull(2)?.let { Game(slot, it) } }
                    else -> Profiles
                }
            },
        )
    }
}

/**
 * Toute l'appli : choix du joueur, accueil, jeux. Les sauvegardes sont écrites **tout de suite** à chaque changement.
 *
 * @param createFeedback fabrique les sons et vibrations ; reçoit une fonction qui donne les réglages du joueur courant.
 */
@Composable
fun PetitsJeuxApp(
    store: ProfileStore,
    modifier: Modifier = Modifier,
    createFeedback: (settings: () -> Settings) -> Feedback = { NoFeedback },
    now: () -> Long = System::currentTimeMillis,
    initialScreen: Screen = Screen.Profiles,
    /** La mise à jour de l'appli (`null` : aucune, par exemple dans les tests). */
    update: UpdateController? = null,
) {
    var profiles by remember { mutableStateOf(store.loadAll()) }
    var screen by rememberSaveable(stateSaver = Screen.Saver) { mutableStateOf(initialScreen) }

    fun slotOf(s: Screen): Int? = when (s) {
        Screen.Profiles -> null
        is Screen.Hub -> s.slot
        is Screen.Game -> s.slot
    }

    fun save(profile: Profile) {
        // Une écriture qui échoue (disque plein…) ne doit pas faire planter l'appli : le joueur continue en mémoire, la prochaine écriture réussie ré-écrit tout.
        runCatching { store.save(profile) }
        profiles = profiles.toMutableList().also { it[profile.slot] = profile }
    }

    // Au lancement, l'appli regarde discrètement (un seul petit fichier sur GitHub) s'il existe une nouvelle version ; rien ne s'installe sans le geste du joueur.
    LaunchedEffect(update) { update?.check(silent = true) }

    val feedback = remember { createFeedback { slotOf(screen)?.let { profiles[it]?.settings } ?: Settings() } }

    // La taille de police du téléphone est suivie jusqu'à 120 % : au-delà les écrans d'enfant déborderaient.
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalFeedback provides feedback,
        LocalDensity provides Density(density.density, density.fontScale.coerceAtMost(1.2f)),
    ) {
        val current = screen
        val profile = slotOf(current)?.let { profiles[it] }
        if (current != Screen.Profiles && profile == null) {
            // Un joueur qui n'existe plus (effacé, ou sauvegarde illisible) : retour au choix du joueur.
            screen = Screen.Profiles
            return@CompositionLocalProvider
        }
        when (current) {
            Screen.Profiles -> ProfilesScreen(
                update = update,
                versionName = if (update != null) installedVersion(LocalContext.current).first else null,
                profiles = profiles,
                onPlay = { slot -> screen = Screen.Hub(slot) },
                onCreate = { slot, name, avatar ->
                    Profile.create(slot, name, avatar, now())?.let {
                        save(it)
                        screen = Screen.Hub(slot)
                    }
                },
                modifier = modifier,
            )
            is Screen.Hub -> HubScreen(
                profile = profile!!,
                onPlay = { game -> screen = Screen.Game(current.slot, game.id) },
                onSwitchPlayer = { screen = Screen.Profiles },
                onSettingsChange = { save(profile.copy(settings = it)) },
                update = update,
                onDelete = {
                    store.delete(current.slot)
                    profiles = profiles.toMutableList().also { it[current.slot] = null }
                    screen = Screen.Profiles
                },
                modifier = modifier,
            )
            is Screen.Game -> {
                val game = Games.byId(current.gameId)
                if (game == null) {
                    screen = Screen.Hub(current.slot)
                } else {
                    game.Content(
                        profile = profile!!,
                        onProfileChange = { save(it.copy(lastPlayed = now())) },
                        onExit = { screen = Screen.Hub(current.slot) },
                    )
                }
            }
        }
    }
}
