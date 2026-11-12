package com.kcharignon.petitsjeux.games.plane.ui

import androidx.compose.ui.graphics.Color

/** Ce qu'on voit sur le sol, très loin sous l'avion (le décor : on ne le heurte jamais). */
enum class GroundKind { TREE, HOUSE, WINDMILL, BOAT, ISLAND, WAVES, CACTUS, MESA, TOWER_BLOCK, LIGHTS }

/**
 * Un décor de vol : les couleurs du ciel, du sol (deux tons qui alternent tous les 10 m pour qu'on voie la vitesse, et des champs de tons voisins), et ce qu'on y pose.
 * La graine d'un vol en tire un au hasard ([PlaneThemes.of]) : le monde n'est jamais le même.
 */
class PlaneTheme(
    val name: String,
    val skyTop: Color,
    val skyBottom: Color,
    /** La brume de l'horizon : les choses lointaines s'y fondent au lieu de surgir. */
    val fog: Color,
    val terrain: Color,
    val terrainAlt: Color,
    /** Les champs : des tons voisins du sol, un par case de 1,25 de large. */
    val patches: List<Color>,
    val far: Color,
    val ground: List<GroundKind>,
    val night: Boolean = false,
    val sun: Color? = null,
    val cloud: Color = Color.White,
)

object PlaneThemes {
    val Country = PlaneTheme(
        "Campagne", skyTop = Color(0xFF4FB4FF), skyBottom = Color(0xFFD9F1FF), fog = Color(0xFFD9F1FF),
        terrain = Color(0xFF7CCB5B), terrainAlt = Color(0xFF72C252),
        patches = listOf(Color(0xFF8AD65E), Color(0xFF6FBF4F), Color(0xFFD9D25A), Color(0xFF9BCB62), Color(0xFF5FB04C), Color(0xFF86D06A)),
        far = Color(0xFF9ED6A0), ground = listOf(GroundKind.TREE, GroundKind.HOUSE, GroundKind.WINDMILL, GroundKind.TREE, GroundKind.TREE, GroundKind.HOUSE),
        sun = Color(0xFFFFF3B0),
    )
    val Sea = PlaneTheme(
        "Mer", skyTop = Color(0xFF58C4FF), skyBottom = Color(0xFFE6F8FF), fog = Color(0xFFE6F8FF),
        terrain = Color(0xFF2FA8E0), terrainAlt = Color(0xFF2A9FD6),
        patches = listOf(Color(0xFF35B3E8), Color(0xFF2AA0DA), Color(0xFF3FBDEE), Color(0xFF2D9BD2), Color(0xFF36B0E4)),
        far = Color(0xFFA7DDF2), ground = listOf(GroundKind.WAVES, GroundKind.WAVES, GroundKind.BOAT, GroundKind.ISLAND, GroundKind.WAVES, GroundKind.BOAT),
        sun = Color(0xFFFFF7C2),
    )
    val Desert = PlaneTheme(
        "Désert", skyTop = Color(0xFFFF8F6B), skyBottom = Color(0xFFFFE2A8), fog = Color(0xFFFFD9A0),
        terrain = Color(0xFFE8B96F), terrainAlt = Color(0xFFE0AE62),
        patches = listOf(Color(0xFFEBBE75), Color(0xFFE0AA5E), Color(0xFFF0C985), Color(0xFFD99C52), Color(0xFFE6B36A)),
        far = Color(0xFFD9825B), ground = listOf(GroundKind.CACTUS, GroundKind.MESA, GroundKind.CACTUS, GroundKind.MESA, GroundKind.HOUSE),
        sun = Color(0xFFFFF0B0),
    )
    val Night = PlaneTheme(
        "Nuit", skyTop = Color(0xFF0E1240), skyBottom = Color(0xFF5B3A8C), fog = Color(0xFF4A3478),
        terrain = Color(0xFF1D2147), terrainAlt = Color(0xFF191D40),
        patches = listOf(Color(0xFF222755), Color(0xFF1B1F45), Color(0xFF2A2A5E), Color(0xFF20244C), Color(0xFF303066)),
        far = Color(0xFF2A2B66), ground = listOf(GroundKind.TOWER_BLOCK, GroundKind.LIGHTS, GroundKind.TOWER_BLOCK, GroundKind.LIGHTS, GroundKind.HOUSE),
        night = true, sun = Color(0xFFFFF7D6), cloud = Color(0xFF8D86C9),
    )

    val all = listOf(Country, Sea, Desert, Night)

    fun of(index: Int): PlaneTheme = all[index.mod(all.size)]
}

/** Les couleurs de l'avion (une par animal du joueur : il reconnaît son avion tout de suite), dans l'ordre des animaux. */
val PlaneColors = listOf(
    Color(0xFFFF7A1F), Color(0xFF2D6FEF), Color(0xFF9A5B2E), Color(0xFF4B5068), Color(0xFFF0468A), Color(0xFF3FBF4A),
    Color(0xFFE8362F), Color(0xFFFFB400), Color(0xFFB36BE8), Color(0xFF14B8A6), Color(0xFF6C4CD6), Color(0xFF1FB6E8),
)
