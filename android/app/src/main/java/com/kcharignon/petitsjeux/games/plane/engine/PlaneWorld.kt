package com.kcharignon.petitsjeux.games.plane.engine

import kotlin.math.floor
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
 */
class Piece(val id: Int, val kind: PieceKind, val z: Float, val lane: Int, val variant: Int) {
    /** Le travers (-1 à 1) du centre de la voie. */
    val x: Float get() = lane * PlaneWorld.LANE_WIDTH
}

/** Un point du couloir libre : à la distance [z], le milieu du couloir est en [x]. */
class PathPoint(val z: Float, val x: Float)

/** Comment une série de rangées d'obstacles (une « vague », toujours d'un seul genre) est disposée. */
enum class Formation {
    /** Quelques obstacles dans des voies tirées au hasard, rangée après rangée. */
    SCATTER,

    /** Une rangée qui ne laisse qu'une voie libre. */
    WALL,

    /** Des rangées qui ne laissent qu'une voie libre, et cette voie se décale d'une voie à chaque rangée : un zigzag à suivre. */
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
                    lastRing = z
                    z += gap * 1.4f
                    continue
                }
                // une vague : un seul genre d'obstacle, une seule disposition
                val kind = pickKind(random, difficulty, p, lastKind)
                lastKind = kind
                val formation = pickFormation(random, difficulty, p)
                val rows = when (formation) {
                    Formation.SCATTER -> 1 + random.nextInt(2 + difficulty.tier / 2)
                    Formation.WALL -> 1 + random.nextInt(2)
                    Formation.SLALOM -> 3 + random.nextInt(1 + difficulty.tier)
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
                    val others = (-MAX_LANE..MAX_LANE).filter { it != lane }
                    val occupied = when (formation) {
                        Formation.SCATTER -> {
                            val mean = lerp(difficulty.perRowStart, difficulty.perRowEnd, p)
                            val n = (mean.toInt() + if (random.nextFloat() < mean - mean.toInt()) 1 else 0).coerceIn(1, others.size)
                            others.shuffled(random).take(n).sorted()
                        }
                        Formation.WALL, Formation.SLALOM -> others
                    }
                    for (l in occupied) pieces += Piece(nextId++, kind, z, l, look)
                    mark(z)
                    z += gap
                }
                // un temps pour souffler entre deux vagues
                z += gap * 0.6f
            }
            path += PathPoint(length + 60f, 0f)
            return PlaneWorld(difficulty, seed, pieces.sortedWith(compareBy({ it.z }, { it.lane })).mapIndexed { i, piece -> Piece(i, piece.kind, piece.z, piece.lane, piece.variant) }, path, theme)
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

        private fun pickFormation(random: Random, d: PlaneDifficulty, p: Float): Formation {
            val r = random.nextFloat()
            return when (d.tier) {
                0 -> Formation.SCATTER
                1 -> if (p > 0.25f && r < 0.25f) Formation.SLALOM else Formation.SCATTER
                2 -> when {
                    r < 0.45f -> Formation.SCATTER
                    r < 0.65f -> Formation.WALL
                    else -> Formation.SLALOM
                }
                else -> when {
                    r < 0.3f -> Formation.SCATTER
                    r < 0.5f -> Formation.WALL
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
