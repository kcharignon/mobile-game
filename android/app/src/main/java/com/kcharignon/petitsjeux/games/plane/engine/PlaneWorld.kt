package com.kcharignon.petitsjeux.games.plane.engine

import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random

/**
 * Ce qu'on croise dans le ciel. Chaque obstacle tient **dans une voie** (largeur au plus 0,28 pour une voie de 0,4) : jamais à cheval sur deux.
 * [depth] : l'épaisseur dans le sens du vol, en mètres. Un anneau n'est pas un obstacle : on le traverse.
 */
enum class PieceKind(val width: Float, val depth: Float, val solid: Boolean) {
    /** Un banc d'oiseaux. */
    BIRDS(0.28f, 0.5f, true),

    /** Un autre avion, qui vient de face. */
    PLANE(0.28f, 0.5f, true),

    /** Une montgolfière. */
    BALLOON(0.26f, 0.6f, true),

    /** Un parapentiste (ou un parachutiste). */
    PARAGLIDER(0.26f, 0.5f, true),

    /** Un anneau lumineux : le traverser fait faire un tonneau à l'avion. */
    RING(0.0f, 0.2f, false),
}

/**
 * Une chose posée dans le ciel, à la distance [z] du départ (mètres), au centre de la voie [lane] (-2 à 2, 0 = au milieu).
 * [variant] ne change que le dessin (couleurs, nombre d'oiseaux).
 *
 * Un avion peut **traverser le ciel** : si [drift] n'est pas nul, il arrive de [drift] voies plus loin sur le côté et glisse vers sa voie pendant les [span] derniers mètres
 * (en douceur : il ne bouge plus de côté quand il passe). Il se trouve donc dans sa voie [lane] à l'instant où l'on arrive à sa hauteur, et seulement là compte :
 * le couloir libre n'est jamais dans sa voie. Son mouvement ne dépend que de la distance qui nous en sépare, jamais du temps (un ralentissement ne change rien).
 */
class Piece(
    val id: Int,
    val kind: PieceKind,
    val z: Float,
    val lane: Int,
    val variant: Int,
    val drift: Int = 0,
    val span: Float = 0f,
) {
    /** Le travers (-1 à 1) du centre de la voie : là où se trouve la chose quand l'avion passe à sa hauteur. */
    val x: Float get() = lane * PlaneWorld.LANE_WIDTH

    /** Vrai pour un avion qui traverse le ciel. */
    val crossing: Boolean get() = drift != 0 && span > 0f

    /** Le travers de la chose quand elle est à [rel] mètres devant l'avion (elle est dans sa voie dès que [rel] est nul ou négatif). */
    fun xAt(rel: Float): Float {
        if (!crossing || rel <= 0f) return x
        val t = (rel / span).coerceIn(0f, 1f)
        return x - drift * PlaneWorld.LANE_WIDTH * (t * t * (3f - 2f * t))
    }

    /** L'inclinaison de l'avion qui traverse, de -1 à 1 : il penche du côté où il va, le plus au milieu de sa traversée (0 hors traversée). */
    fun bankAt(rel: Float): Float {
        if (!crossing || rel <= 0f || rel >= span) return 0f
        val t = rel / span
        return (if (drift > 0) 1f else -1f) * 4f * t * (1f - t)
    }

    fun withId(newId: Int) = Piece(newId, kind, z, lane, variant, drift, span)
}

/** Un point du couloir libre : à la distance [z], le milieu du couloir est en [x]. */
class PathPoint(val z: Float, val x: Float)

/** Comment une série de rangées d'obstacles (une « vague », toujours d'un seul genre) est disposée. */
enum class Formation {
    /** Quelques obstacles dans des voies tirées au hasard, une à trois rangées (avant la dernière difficulté : toujours deux voies libres côte à côte). */
    SCATTER,

    /** Une rangée qui ne laisse qu'une voie libre : isolée (une seule rangée, jamais deux murs proches) avant la dernière difficulté. */
    WALL,

    /** Des rangées dont la place libre se décale d'une voie à chaque rangée : un zigzag à suivre. Deux voies libres côte à côte, sauf à la dernière difficulté (une seule). */
    SLALOM,
}

/**
 * Un trajet tiré au hasard : ses [pieces] (triées par distance), le [path] (le couloir libre, que le générateur ne bouche jamais) et le [theme] du décor.
 * Il ne dépend que de [difficulty] et de [seed] : la même graine redonne le même trajet (tests stables) ; le jeu prend une graine nouvelle à chaque vol.
 *
 * Tout est **aligné** : le ciel a 5 voies ([LANES]), chaque obstacle est au centre d'une voie, les obstacles d'une même rangée sont à la même distance,
 * et une vague (une série de rangées) est toujours faite d'**un seul genre** d'obstacle (jamais d'oiseaux à côté d'un avion). L'avion, lui, se pilote librement :
 * rien n'est aimanté de son côté.
 */
class PlaneWorld(
    val difficulty: PlaneDifficulty,
    val seed: Long,
    val pieces: List<Piece>,
    val path: List<PathPoint>,
    val theme: Int,
    /** La longueur du trajet (m) : celle de la difficulté, sauf dans les tests qui veulent un vol court. */
    val length: Float = difficulty.length,
) {
    /** Le milieu du couloir libre à la distance [z] (interpolé entre deux points). */
    fun pathAt(z: Float): Float = pathCenter(path, z)

    companion object {
        const val THEMES = 4

        /** Cinq voies, de -2 à 2, larges de [LANE_WIDTH] : le ciel va de -1 à +1. */
        const val LANES = 5
        const val MAX_LANE = 2
        const val LANE_WIDTH = 0.4f

        /** Rien dans les premiers mètres (le temps de décoller) ; et à la fin, l'approche de l'aéroport (voir [PlaneRun.APPROACH_METERS]) est dégagée. */
        const val START_CLEAR = 45f
        const val END_CLEAR = 100f
        const val RING_END_CLEAR = 110f

        /** La vitesse de côté (unités par seconde) qu'on exige de l'avion au plus entre deux rangées : il sait en faire plus de deux fois plus ([PlaneRun.LATERAL_MAX]), ce qui laisse de la marge au retard du doigt. */
        const val LATERAL_SAFE = 1.4f

        /** Brasse une graine (finaliseur de SplitMix64) : deux graines voisines, comme 41 et 42, donnent des tirages sans rapport entre eux. */
        private fun mix(z: Long): Long {
            var x = z + -7046029254386353131L
            x = (x xor (x ushr 30)) * -4658895280553007687L
            x = (x xor (x ushr 27)) * -7723592293110705685L
            return x xor (x ushr 31)
        }

        /** Combien de voies l'avion sait changer entre deux rangées séparées de [meters] mètres, à la vitesse de la seconde rangée. */
        internal fun maxShift(difficulty: PlaneDifficulty, meters: Float, atZ: Float): Int =
            floor(LATERAL_SAFE * (meters / difficulty.speedAt(atZ)) / LANE_WIDTH).toInt().coerceIn(0, 2)

        fun generate(difficulty: PlaneDifficulty, seed: Long): PlaneWorld {
            val random = Random(mix(seed))
            val length = difficulty.length
            val theme = random.nextInt(THEMES)
            val pieces = ArrayList<Piece>()
            val path = ArrayList<PathPoint>()
            path += PathPoint(0f, 0f)
            var nextId = 0
            var z = START_CLEAR
            var previousZ = 0f
            var lane = 0 // la voie libre : celle du couloir
            var lastKind: PieceKind? = null
            var lastFormation: Formation? = null
            var wideSince = SOLO_SPACING // combien de rangées depuis la dernière qui ne laissait qu'une voie libre
            var lastRing = START_CLEAR - difficulty.ringEvery * 0.4f

            fun mark(at: Float) {
                path += PathPoint(at, lane * LANE_WIDTH)
                previousZ = at
            }

            while (z < length - END_CLEAR) {
                val p = z / length
                val gap = lerp(difficulty.gapStart, difficulty.gapEnd, p)
                // un anneau lumineux, seul sur sa rangée, dans la voie libre
                if (z - lastRing >= difficulty.ringEvery * (0.8f + 0.4f * random.nextFloat()) && z < length - RING_END_CLEAR) {
                    lane = (lane + randomShift(random, maxShift(difficulty, z - previousZ, z))).coerceIn(-MAX_LANE, MAX_LANE)
                    pieces += Piece(nextId++, PieceKind.RING, z, lane, 0)
                    mark(z)
                    wideSince++
                    lastRing = z
                    z += gap * 1.4f
                    continue
                }
                // une vague : un seul genre d'obstacle, une seule disposition, et le plus souvent une ou deux rangées seulement
                val kind = pickKind(random, difficulty, p, lastKind)
                lastKind = kind
                val formation = pickFormation(random, difficulty, p, lastFormation, wideSince >= SOLO_SPACING)
                lastFormation = formation
                // des avions qui traversent le ciel (les troisième et quatrième difficultés) : toute la vague traverse dans le même sens, de la même distance
                val crossing = kind == PieceKind.PLANE && formation == Formation.SCATTER && difficulty.crossingPlanes && random.nextFloat() < crossingChance(difficulty)
                val drift = if (crossing) (CROSS_MIN_LANES + random.nextInt(2)) * (if (random.nextBoolean()) 1 else -1) else 0
                val rows = when (formation) {
                    Formation.SCATTER -> if (crossing) 1 + random.nextInt(2) else scatterRows(random, difficulty)
                    Formation.WALL -> if (difficulty.narrowStreaks) 1 + random.nextInt(2) else 1
                    Formation.SLALOM -> 3 + random.nextInt(2)
                }
                // toute la vague a le même dessin (mêmes oiseaux, même couleur d'avion, même montgolfière)
                val look = random.nextInt(VARIANTS)
                var direction = if (random.nextBoolean()) 1 else -1
                for (row in 0 until rows) {
                    if (z >= length - END_CLEAR) break
                    val shift = maxShift(difficulty, z - previousZ, z)
                    lane = when (formation) {
                        Formation.SLALOM -> {
                            if (shift >= 1) {
                                if (lane + direction !in -MAX_LANE..MAX_LANE) direction = -direction
                                lane + direction
                            } else {
                                lane
                            }
                        }
                        else -> (lane + randomShift(random, shift)).coerceIn(-MAX_LANE, MAX_LANE)
                    }
                    // avant la dernière difficulté, une voie à côté de celle du couloir reste libre elle aussi : jamais d'affilée une seule voie libre
                    val buddy = when {
                        formation == Formation.WALL -> null
                        difficulty.narrowStreaks -> null
                        formation == Formation.SLALOM -> (lane - direction).takeIf { it in -MAX_LANE..MAX_LANE } ?: (lane + direction)
                        else -> listOf(lane - 1, lane + 1).filter { it in -MAX_LANE..MAX_LANE }.let { it[random.nextInt(it.size)] }
                    }
                    val candidates = (-MAX_LANE..MAX_LANE).filter { it != lane && it != buddy }
                    val occupied = when (formation) {
                        Formation.SCATTER -> {
                            val mean = lerp(difficulty.perRowStart, difficulty.perRowEnd, p)
                            val n = (mean.toInt() + if (random.nextFloat() < mean - mean.toInt()) 1 else 0).coerceIn(1, min(difficulty.maxPerRow, candidates.size))
                            candidates.shuffled(random).take(n).sorted()
                        }
                        Formation.WALL, Formation.SLALOM -> candidates
                    }
                    val span = CROSS_SECONDS * difficulty.speedAt(z)
                    for (l in occupied) pieces += Piece(nextId++, kind, z, l, look, drift, if (crossing) span else 0f)
                    if (LANES - occupied.size == 1) wideSince = 0 else wideSince++
                    mark(z)
                    // l'écart entre deux rangées d'une même vague n'est jamais plus court que l'écart du moment, et varie un peu
                    z += gap * (1f + 0.3f * random.nextFloat())
                }
                // un temps pour souffler entre deux vagues, et parfois un ciel vide
                z += gap * (0.2f + 0.7f * random.nextFloat())
                if (random.nextFloat() < 0.15f) z += gap * 0.8f
            }
            path += PathPoint(length + 60f, 0f)
            return PlaneWorld(difficulty, seed, pieces.sortedWith(compareBy({ it.z }, { it.lane })).mapIndexed { i, piece -> piece.withId(i) }, path, theme)
        }

        /** Un mur (une seule voie libre) ne peut venir qu'après au moins trois rangées qui laissent deux voies libres, anneaux compris. */
        const val SOLO_SPACING = 3

        /** Les avions qui traversent le ciel arrivent de 3 ou 4 voies plus loin, et mettent [CROSS_SECONDS] secondes (à la vitesse du moment) à rejoindre leur voie. */
        const val CROSS_MIN_LANES = 3
        const val CROSS_SECONDS = 3.0f

        private fun crossingChance(d: PlaneDifficulty): Float = if (d.tier >= 3) 0.55f else 0.4f

        /** Combien de rangées dans une vague éparpillée : le plus souvent une, parfois deux, rarement trois. */
        private fun scatterRows(random: Random, d: PlaneDifficulty): Int {
            val r = random.nextFloat()
            return if (d.tier == 0) {
                if (r < 0.6f) 1 else 2
            } else {
                when {
                    r < 0.5f -> 1
                    r < 0.85f -> 2
                    else -> 3
                }
            }
        }

        /** Les dessins différents de chaque genre (couleurs, nombre d'oiseaux). */
        const val VARIANTS = 3

        /** Le décalage de voie pour la rangée suivante : le plus souvent aucun, sinon d'une voie, et plus rarement de plus. */
        private fun randomShift(random: Random, max: Int): Int {
            if (max <= 0) return 0
            val r = random.nextFloat()
            val size = when {
                r < 0.45f -> 0
                r < 0.85f || max == 1 -> 1
                else -> 1 + random.nextInt(max)
            }
            return if (random.nextBoolean()) size else -size
        }

        /**
         * La disposition de la vague. Après un mur ou un slalom vient toujours une vague éparpillée (aux niveaux 1 à 3) : jamais deux tours de force d'affilée.
         * [soloOk] : assez de rangées larges depuis le dernier mur pour en poser un autre.
         */
        private fun pickFormation(random: Random, d: PlaneDifficulty, p: Float, last: Formation?, soloOk: Boolean): Formation {
            val r = random.nextFloat()
            val trick = last == Formation.WALL || last == Formation.SLALOM
            return when (d.tier) {
                0 -> Formation.SCATTER
                1 -> if (p > 0.3f && !trick && r < 0.2f) Formation.SLALOM else Formation.SCATTER
                2 -> when {
                    trick -> Formation.SCATTER
                    r < 0.55f -> Formation.SCATTER
                    r < 0.70f -> if (d.soloWalls && soloOk) Formation.WALL else Formation.SCATTER
                    else -> Formation.SLALOM
                }
                else -> when {
                    trick && r < 0.6f -> Formation.SCATTER
                    r < 0.4f -> Formation.SCATTER
                    r < 0.6f -> Formation.WALL
                    else -> Formation.SLALOM
                }
            }
        }

        /** Le genre d'une vague : jamais deux fois de suite le même. Les avions arrivent avec la deuxième difficulté. */
        private fun pickKind(random: Random, d: PlaneDifficulty, p: Float, last: PieceKind?): PieceKind {
            val weights = ArrayList<Pair<PieceKind, Int>>()
            weights += PieceKind.BIRDS to 3
            weights += PieceKind.BALLOON to 2
            weights += PieceKind.PARAGLIDER to 2
            if (d.tier >= 1) weights += PieceKind.PLANE to 3
            val choices = weights.filter { it.first != last }
            var pick = random.nextInt(choices.sumOf { it.second })
            for ((kind, w) in choices) {
                if (pick < w) return kind
                pick -= w
            }
            return choices.first().first
        }
    }
}

/** Le milieu du couloir à la distance [z] : interpolation entre les deux points du [path] qui l'entourent. */
internal fun pathCenter(path: List<PathPoint>, z: Float): Float {
    if (path.isEmpty()) return 0f
    if (z <= path.first().z) return path.first().x
    if (z >= path.last().z) return path.last().x
    var lo = 0
    var hi = path.lastIndex
    while (hi - lo > 1) {
        val mid = (lo + hi) / 2
        if (path[mid].z <= z) lo = mid else hi = mid
    }
    val a = path[lo]
    val b = path[hi]
    return lerp(a.x, b.x, (z - a.z) / (b.z - a.z))
}
