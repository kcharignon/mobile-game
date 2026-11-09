package com.kcharignon.petitsjeux.games.road.engine

import kotlin.math.floor

/** Les constantes du jeu « Poussin Traverse » (voir docs/DESIGN.md). Les longueurs sont en **cases** (la route fait [COLS] cases de large), les durées en ms. */
object Road {
    /** Largeur de la route, en cases : toujours la même, sur tous les téléphones. */
    const val COLS = 14

    /** La colonne où le poussin part. */
    const val START_COL = 6

    /** Un saut dure 200 ms ; pendant la première moitié le poussin compte comme étant encore dans sa ligne, puis dans la suivante. */
    const val HOP_MS = 200L

    /** Combien de temps le poussin reste aplati avant de revenir au dernier refuge. */
    const val SQUASH_MS = 900L

    /** Après un écrasement, le poussin clignote et ne peut plus être touché pendant ce temps. */
    const val INVULNERABLE_MS = 1200L

    /** Le poussin se dessine sur ~0,32 case de rayon ; on ne le touche que sur 0,22 : si ça se touche presque, ça passe. */
    const val CHICK_HALF = 0.22

    /** Chaque véhicule est raccourci de 0,08 case à chaque bout pour la collision. */
    const val BODY_SHRINK = 0.08

    const val POINTS_NEW_ROW = 10
    const val POINTS_GRAIN = 10
    const val POINTS_ARRIVAL = 50
    const val POINTS_HIT = 15

    /** Le moteur avance par pas de 20 ms au plus : aucun véhicule ne peut « traverser » le poussin sans le toucher. */
    const val STEP_MS = 20L
}

/** Les véhicules ; [length] en cases, [horn] = la longueur de klaxon à jouer (2 = voiture, 3 = camion, 4 = bus / train, voir `Feedback.bump`). */
enum class VehicleKind(val title: String, val length: Double, val horn: Int) {
    SCOOTER("Scooter", 1.1, 2),
    CAR("Voiture", 2.0, 2),
    TAXI("Taxi", 2.0, 2),
    SPORT("Voiture de sport", 2.1, 2),
    VAN("Camionnette", 2.5, 3),
    TRACTOR("Tracteur", 2.4, 3),
    AMBULANCE("Ambulance", 2.8, 3),
    TRUCK("Camion", 4.4, 3),
    BUS("Bus", 4.8, 4),
    TRAIN("Train", 14.0, 4),
}

/** Un véhicule tel qu'on le dessine : son bord gauche (en cases, peut sortir de la route), sa longueur, son sens. [color] = indice de la palette. */
class Body(val kind: VehicleKind, val color: Int, val left: Double, val length: Double, val direction: Int)

/** Une place dans le flux d'une voie : un véhicule, sa couleur, et l'espace libre **derrière** lui jusqu'au suivant. */
class Slot(val kind: VehicleKind, val color: Int, val gap: Double)

/** Ce qui roule sur une ligne. Tout est une **fonction pure du temps** : pas de simulation, donc tout est testable et prouvable. */
sealed interface Traffic {
    /** +1 : vers la droite ; -1 : vers la gauche. */
    val direction: Int

    /** Vitesse en cases par seconde (sert à régler les marges du robot). */
    val speed: Double

    /** Le véhicule qui touche la zone [lo, hi] (en cases) à l'instant [t] (secondes), ou `null`. La zone de collision est un peu plus courte que le dessin. */
    fun hit(t: Double, lo: Double, hi: Double): VehicleKind?

    /** Les véhicules à dessiner qui rencontrent [lo, hi] à l'instant [t]. */
    fun bodies(t: Double, lo: Double, hi: Double): List<Body>
}

/**
 * Un flux de véhicules **périodique** : la suite de [slots] se répète à l'infini sur la ligne (période = somme des longueurs et des espaces) ; à l'instant t
 * le bord gauche du véhicule i est à `décalage_i + phase + sens · vitesse · t`, plus un nombre entier de périodes. Aucune apparition brutale à l'écran.
 */
class Stream(override val direction: Int, override val speed: Double, val slots: List<Slot>, val phase: Double) : Traffic {
    private val offsets = DoubleArray(slots.size)
    val period: Double

    init {
        require(direction == 1 || direction == -1) { "sens : $direction" }
        require(slots.isNotEmpty() && speed > 0.0)
        var x = 0.0
        for ((i, s) in slots.withIndex()) {
            offsets[i] = x
            x += s.kind.length + s.gap
        }
        period = x
    }

    private fun left(i: Int, t: Double): Double {
        val raw = offsets[i] + phase + direction * speed * t
        return raw - floor(raw / period) * period
    }

    override fun hit(t: Double, lo: Double, hi: Double): VehicleKind? {
        for ((i, s) in slots.withIndex()) {
            val width = s.kind.length - 2 * Road.BODY_SHRINK
            val b = left(i, t) + Road.BODY_SHRINK
            val k = floor((lo - b - width) / period) + 1
            if (b + k * period < hi) return s.kind
        }
        return null
    }

    override fun bodies(t: Double, lo: Double, hi: Double): List<Body> {
        val out = ArrayList<Body>()
        for ((i, s) in slots.withIndex()) {
            val len = s.kind.length
            val l = left(i, t)
            var k = floor((lo - l - len) / period) + 1
            while (l + k * period < hi) {
                out += Body(s.kind, s.color, l + k * period, len, direction)
                k += 1
            }
        }
        return out
    }
}

/**
 * Une voie ferrée : toutes les [cycle] secondes, les feux clignotent et la cloche sonne [warning] secondes, puis un train de [length] cases passe à [speed]
 * cases par seconde et traverse toute la route. [offset] décale le cycle (en secondes).
 */
class TrainLine(
    override val direction: Int,
    val cycle: Double,
    val warning: Double,
    override val speed: Double,
    val length: Double,
    val offset: Double,
) : Traffic {
    /** Quand le train est complètement passé (dans le cycle). */
    val clearAt: Double = warning + (Road.COLS + length) / speed

    init {
        require(direction == 1 || direction == -1)
        require(clearAt < cycle) { "le train doit avoir fini de passer avant le cycle suivant ($clearAt >= $cycle)" }
    }

    private fun tau(t: Double): Double {
        val raw = t + offset
        return raw - floor(raw / cycle) * cycle
    }

    /** Les feux clignotent (et la cloche sonne) depuis le début de l'avertissement jusqu'à ce que le train soit passé. */
    fun lightsOn(t: Double): Boolean = tau(t) < clearAt

    /** Le bord gauche du train, ou `null` s'il n'est pas sur la route. */
    private fun trainLeft(t: Double): Double? {
        val tau = tau(t)
        if (tau < warning || tau > clearAt) return null
        val run = speed * (tau - warning)
        return if (direction > 0) -length + run else Road.COLS - run
    }

    override fun hit(t: Double, lo: Double, hi: Double): VehicleKind? {
        val l = trainLeft(t) ?: return null
        return if (l + Road.BODY_SHRINK < hi && l + length - Road.BODY_SHRINK > lo) VehicleKind.TRAIN else null
    }

    override fun bodies(t: Double, lo: Double, hi: Double): List<Body> {
        val l = trainLeft(t) ?: return emptyList()
        return if (l < hi && l + length > lo) listOf(Body(VehicleKind.TRAIN, 0, l, length, direction)) else emptyList()
    }
}

enum class RowKind {
    /** Le trottoir du départ. */ START,
    /** Une voie où roulent des véhicules. */ ROAD,
    /** Un terre-plein (ou un quai) : on y est en sécurité, et on y revient quand on s'est fait écraser. */ REFUGE,
    /** Une voie ferrée. */ RAIL,
    /** Le trottoir de l'arrivée. */ FINISH;

    val safe: Boolean get() = this != ROAD && this != RAIL
}

class Row(val kind: RowKind, val traffic: Traffic? = null) {
    init {
        require((kind == RowKind.ROAD || kind == RowKind.RAIL) == (traffic != null)) { "une voie a de la circulation, un refuge non ($kind)" }
        require(kind != RowKind.RAIL || traffic is TrainLine)
        require(kind != RowKind.ROAD || traffic is Stream)
    }
}

/** Une graine à ramasser (ligne `row` à partir du départ, colonne `col`). */
class Grain(val id: Int, val row: Int, val col: Int)

/** Un niveau : les lignes du bas (départ) vers le haut (arrivée) et les graines. */
class RoadLevel(val number: Int, val rows: List<Row>, val grains: List<Grain>) {
    init {
        require(rows.size >= 3 && rows.first().kind == RowKind.START && rows.last().kind == RowKind.FINISH)
        for (g in grains) require(g.row in rows.indices && rows[g.row].kind == RowKind.ROAD && g.col in 0 until Road.COLS)
    }

    val last: Int get() = rows.size - 1
    val world: Int get() = RoadWorlds.of(number)
    val lanes: Int get() = rows.count { it.kind == RowKind.ROAD }

    /** Le meilleur score possible : toutes les lignes, toutes les graines, l'arrivée. */
    val maxScore: Int get() = Road.POINTS_NEW_ROW * last + Road.POINTS_GRAIN * grains.size + Road.POINTS_ARRIVAL

    /** Étoiles d'une traversée réussie : 1 dès l'arrivée (on ne perd jamais), 2 à 50 % du maximum, 3 à 80 %. */
    fun stars(score: Int): Int = when {
        score * 10 >= maxScore * 8 -> 3
        score * 2 >= maxScore -> 2
        else -> 1
    }
}

/** Les 48 niveaux : 8 mondes de 6. Voies : 2 + monde + (indice dans le monde) / 3, donc de 2 voies (double sens) à 10. */
object RoadWorlds {
    const val COUNT = 8
    const val LEVELS_PER_WORLD = 6
    const val LEVEL_COUNT = COUNT * LEVELS_PER_WORLD

    val names = listOf(
        "La petite rue", "Le quartier", "La nationale", "La grande ville",
        "L'autoroute", "Le passage à niveau", "L'heure de pointe", "La mégapole",
    )

    /** Ce que le monde apporte de nouveau (affiché à l'ouverture du niveau). */
    val news = listOf(
        "Des voitures et des scooters.",
        "Des camionnettes, et des tracteurs très lents.",
        "Des camions et des bus, très longs !",
        "Des taxis et des ambulances, très rapides !",
        "Des voitures de sport : ça file !",
        "Un train passe : écoute la cloche !",
        "Beaucoup de circulation, de toutes sortes.",
        "Deux voies ferrées et plein de véhicules !",
    )

    fun of(level: Int): Int = ((level - 1) / LEVELS_PER_WORLD).coerceIn(0, COUNT - 1)
    fun firstLevel(world: Int): Int = world * LEVELS_PER_WORLD + 1
    fun indexIn(level: Int): Int = (level - 1) % LEVELS_PER_WORLD
    fun lanes(level: Int): Int = 2 + of(level) + indexIn(level) / 3

    /** Voies ferrées du monde : 0, puis une avant l'arrivée (mondes 6 et 7), puis une au milieu en plus (monde 8). */
    fun rails(world: Int): Int = when {
        world < 5 -> 0
        world < 7 -> 1
        else -> 2
    }

    /** L'ambiance du monde (indice de thème dessiné par l'interface) : 0 jour, 1 campagne, 2 ville, 3 soir, 4 nuit. */
    fun theme(world: Int): Int = listOf(0, 0, 1, 2, 3, 4, 3, 4)[world.coerceIn(0, COUNT - 1)]
}
