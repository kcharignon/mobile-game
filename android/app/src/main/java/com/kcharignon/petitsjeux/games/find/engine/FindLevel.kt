package com.kcharignon.petitsjeux.games.find.engine

import org.json.JSONObject

/**
 * Un niveau de Cherche et Trouve : le modèle à retrouver, les faux, et la graine de la mise en page. Rien d'autre : le décor vient du monde,
 * le nombre d'objets est [objectCount]. Les identifiants sont ceux du catalogue (`tools/find/catalog.js`, `FindObject.id`).
 */
data class FindLevel(val number: Int, val target: String, val others: List<String>, val seed: Int) {
    /** Le monde (de 0 à 9) du niveau. */
    val world: Int get() = FindWorlds.of(number)

    /** Les objets du niveau, le modèle en premier (l'ordre d'affichage est celui de la mise en page, pas celui-ci). */
    val ids: List<String> get() = listOf(target) + others

    val objectCount: Int get() = others.size + 1
}

/** Les mondes : 10 mondes de 10 niveaux ; le monde `w` (de 0 à 9) montre `5 + w` objets. Le décor change à chaque monde (voir `FindTheme`). */
object FindWorlds {
    const val COUNT = 10
    const val LEVELS_PER_WORLD = 10
    const val LEVEL_COUNT = COUNT * LEVELS_PER_WORLD

    val names = listOf(
        "La chambre", "Le jardin", "La plage", "La cuisine", "La ferme",
        "La forêt", "La ville", "Le ciel", "Le grenier", "La fête",
    )

    /** Le monde (de 0 à 9) du niveau [level] (de 1 à 100). */
    fun of(level: Int): Int = (level - 1) / LEVELS_PER_WORLD

    fun firstLevel(world: Int): Int = world * LEVELS_PER_WORLD + 1

    /** Le rang du niveau dans son monde (0 pour le premier). */
    fun indexIn(level: Int): Int = (level - 1) % LEVELS_PER_WORLD

    /** Combien d'objets à l'écran (le modèle compris) dans le monde [world] (de 0 à 9) : 5 dans le premier, 14 dans le dernier. */
    fun objectCount(world: Int): Int = 5 + world
}

/** Lecture du fichier de niveaux. Un niveau incohérent est **rejeté** (le test des données le verrait aussi). */
object FindLevels {
    /** Lit le texte du fichier `find_levels.json` (fabriqué par `tools/find/make-levels.js`). */
    fun parse(text: String): List<FindLevel> {
        val root = JSONObject(text)
        require(root.optInt("version") == 1) { "version de niveaux inconnue : ${root.opt("version")}" }
        val array = root.getJSONArray("levels")
        require(array.length() == FindWorlds.LEVEL_COUNT) { "${FindWorlds.LEVEL_COUNT} niveaux attendus, ${array.length()} trouvés" }
        return List(array.length()) { i ->
            val o = array.getJSONObject(i)
            val number = o.getInt("n")
            require(number == i + 1) { "niveau ${i + 1} attendu, trouvé $number" }
            val target = o.getString("target")
            val others = o.getJSONArray("others").let { a -> List(a.length()) { a.getString(it) } }
            val level = FindLevel(number, target, others, o.getInt("seed"))
            require(target.isNotBlank() && level.ids.none { it.isBlank() }) { "niveau $number : identifiant vide" }
            require(level.ids.toSet().size == level.ids.size) { "niveau $number : un objet est en double" }
            require(level.objectCount == FindWorlds.objectCount(level.world)) { "niveau $number : ${level.objectCount} objets au lieu de ${FindWorlds.objectCount(level.world)}" }
            level
        }
    }
}
