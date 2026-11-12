package com.kcharignon.petitsjeux.games.skate.ui

import androidx.compose.ui.graphics.Color

/** Ce qu'on dessine sur les côtés de la rue (le décor : on ne le heurte jamais). */
enum class DecorKind { BUILDING, TREE, PALM, UMBRELLA, BUSH, LAMP }

/**
 * Un décor de rue : les couleurs du ciel, de la chaussée (deux tons qui alternent tous les 4 m pour qu'on voie la vitesse), des bas-côtés, et ce qu'on y plante.
 * La graine d'une course en tire un au hasard ([SkateThemes.of]) : le monde n'est jamais le même.
 */
class SkateTheme(
    val name: String,
    val skyTop: Color,
    val skyBottom: Color,
    /** La brume de l'horizon : les choses lointaines s'y fondent au lieu de surgir. */
    val fog: Color,
    val road: Color,
    val roadAlt: Color,
    val line: Color,
    val side: Color,
    val sideAlt: Color,
    val curb: Color,
    /** La silhouette lointaine (immeubles, collines, mer). */
    val far: Color,
    val decor: List<DecorKind>,
    val night: Boolean = false,
    val sun: Color? = null,
    val sea: Color? = null,
)

object SkateThemes {
    val City = SkateTheme(
        "Ville", skyTop = Color(0xFF6EC6FF), skyBottom = Color(0xFFCDEBFF), fog = Color(0xFFCDEBFF),
        road = Color(0xFF6F7690), roadAlt = Color(0xFF666D86), line = Color(0xFFF4F1E4),
        side = Color(0xFFD9D2C2), sideAlt = Color(0xFFCFC8B7), curb = Color(0xFFF3EEDF),
        far = Color(0xFFA9C6E2), decor = listOf(DecorKind.BUILDING, DecorKind.BUILDING, DecorKind.TREE, DecorKind.LAMP, DecorKind.BUILDING, DecorKind.TREE),
        sun = Color(0xFFFFF3B0),
    )
    val Park = SkateTheme(
        "Parc", skyTop = Color(0xFF7FD3F7), skyBottom = Color(0xFFDDF6E4), fog = Color(0xFFDDF6E4),
        road = Color(0xFFE4BF8C), roadAlt = Color(0xFFDBB47D), line = Color(0xFFFFF6DD),
        side = Color(0xFF7ACB5B), sideAlt = Color(0xFF6FC251), curb = Color(0xFFF1DDB0),
        far = Color(0xFF8CCB8A), decor = listOf(DecorKind.TREE, DecorKind.TREE, DecorKind.BUSH, DecorKind.TREE, DecorKind.LAMP, DecorKind.BUSH),
        sun = Color(0xFFFFF3B0),
    )
    val Seaside = SkateTheme(
        "Bord de mer", skyTop = Color(0xFFFF9E80), skyBottom = Color(0xFFFFE9A8), fog = Color(0xFFFFE9A8),
        road = Color(0xFFC98B54), roadAlt = Color(0xFFBD7F49), line = Color(0xFFFFEFD2),
        side = Color(0xFFF1D79A), sideAlt = Color(0xFFEBCF8E), curb = Color(0xFFFFF1D0),
        far = Color(0xFFFFB38A), decor = listOf(DecorKind.PALM, DecorKind.UMBRELLA, DecorKind.PALM, DecorKind.BUSH, DecorKind.UMBRELLA, DecorKind.LAMP),
        sun = Color(0xFFFFF7C2), sea = Color(0xFF39B6D8),
    )
    val Night = SkateTheme(
        "Nuit", skyTop = Color(0xFF14183F), skyBottom = Color(0xFF5B3A8C), fog = Color(0xFF4A3478),
        road = Color(0xFF3D4366), roadAlt = Color(0xFF353B5C), line = Color(0xFFC8CCEB),
        side = Color(0xFF2E3454), sideAlt = Color(0xFF282D4A), curb = Color(0xFF7C84B3),
        far = Color(0xFF2A2B66), decor = listOf(DecorKind.BUILDING, DecorKind.LAMP, DecorKind.BUILDING, DecorKind.TREE, DecorKind.LAMP, DecorKind.BUILDING),
        night = true, sun = Color(0xFFFFF7D6),
    )

    val all = listOf(City, Park, Seaside, Night)

    fun of(index: Int): SkateTheme = all[index.mod(all.size)]
}

/** Les couleurs des casques (une par animal du joueur : le skateur le reconnaît tout de suite), dans l'ordre des animaux. */
val SkateHelmets = listOf(
    Color(0xFFFF8A1F), Color(0xFF2D5FE0), Color(0xFF9A5B2E), Color(0xFF3B3F51), Color(0xFFF0468A), Color(0xFF3FBF4A),
    Color(0xFFE8562F), Color(0xFFFFC107), Color(0xFFB36BE8), Color(0xFF14B8A6), Color(0xFF7B5BD6), Color(0xFF2BC4EE),
)
