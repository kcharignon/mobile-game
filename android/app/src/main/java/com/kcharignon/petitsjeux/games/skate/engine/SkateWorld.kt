package com.kcharignon.petitsjeux.games.skate.engine

import kotlin.math.abs
import kotlin.random.Random

/**
 * Ce qu'on croise dans la rue. Les largeurs sont en « unités de rue » : la rue va de -1 à +1 (une unité = 2,5 m), le skateur fait [SkateRun.SKATER_WIDTH].
 * [depth] : l'épaisseur dans le sens de la route, en mètres. Une rampe n'est pas un obstacle : on la prend.
 */
enum class PieceKind(val width: Float, val depth: Float, val solid: Boolean) {
    PEDESTRIAN(0.20f, 0.5f, true),
    LAMP(0.10f, 0.4f, true),
    CONE(0.13f, 0.3f, true),
    BIN(0.17f, 0.4f, true),
    BENCH(0.52f, 0.5f, true),
    BARRIER(0.64f, 0.4f, true),
    RAMP(0.56f, SkateWorld.RAMP_LENGTH, false),
}

/**
 * Une chose posée dans la rue, à la distance [z] du départ (mètres) et au travers [x] (-1 à 1). [variant] ne change que le dessin (habits, couleur).
 *
 * Un piéton qui **traverse** part de [fromX] (le trottoir) et marche vers [x] pendant que le skateur s'approche : sa position est une fonction de la **distance** qui reste
 * jusqu'à lui (il commence à marcher à [walkStart] mètres, il est arrivé à [walkEnd] mètres), pas du temps. Un ralentissement après un choc ne le change donc pas
 * là où il sera quand le skateur arrive : c'est ce qui permet de prouver que la rue est toujours faisable.
 */
class Piece(
    val id: Int,
    val kind: PieceKind,
    val z: Float,
    val x: Float,
    val variant: Int,
    val fromX: Float = x,
    val walkStart: Float = 0f,
    val walkEnd: Float = 0f,
) {
    val crosses: Boolean get() = fromX != x

    /** Où est la pièce quand elle est à [rel] mètres devant le skateur (négatif : déjà dépassée). */
    fun xAt(rel: Float): Float {
        if (!crosses) return x
        val t = ((walkStart - rel) / (walkStart - walkEnd)).coerceIn(0f, 1f)
        return lerp(fromX, x, t)
    }
}

/** Un point du couloir libre : à la distance [z], le milieu du couloir est en [x]. */
class PathPoint(val z: Float, val x: Float)

/**
 * Une rue tirée au hasard : ses [pieces] (triées par distance), le [path] (le couloir libre, que le générateur ne bouche jamais) et le [theme] du décor.
 * Elle ne dépend que de [difficulty] et de [seed] : la même graine redonne la même rue (tests stables) ; le jeu prend une graine nouvelle à chaque course.
 */
class SkateWorld(
    val difficulty: SkateDifficulty,
    val seed: Long,
    val pieces: List<Piece>,
    val path: List<PathPoint>,
    val theme: Int,
    /** La longueur de la rue (m) : celle de la difficulté, sauf dans les tests qui veulent une course courte. */
    val length: Float = difficulty.length,
) {

    /** Le milieu du couloir libre à la distance [z] (interpolé entre deux points). */
    fun pathAt(z: Float): Float = pathCenter(path, z)

    companion object {
        const val RAMP_LENGTH = 3.2f
        const val THEMES = 4

        /** Rien dans les premiers mètres (le temps de démarrer) ni dans les derniers (on arrive en roue libre). */
        const val START_CLEAR = 42f
        const val END_CLEAR = 42f
        const val RAMP_END_CLEAR = 75f

        /** Demi-largeur du couloir libre ; le milieu du couloir ne sort jamais de ±[CENTER_MAX]. */
        const val CORRIDOR_HALF = 0.30f
        const val CORRIDOR_CLEAR = 0.04f
        const val CENTER_MAX = 0.62f

        /** La vitesse de côté (unités par seconde) qu'on exige du skateur au plus : le skateur sait en faire 1,6 fois plus ([SkateRun.LATERAL_MAX]). */
        const val LATERAL_SAFE = 2.0f

        /** Un décalage au plus de ±[ROW_JITTER] de l'écart entre rangées : les obstacles ne sont pas alignés au cordeau. */
        private const val ROW_JITTER = 0.3f

        /** Brasse une graine (finaliseur de SplitMix64) : deux graines voisines, comme 41 et 42, donnent des tirages sans rapport entre eux. */
        private fun mix(z: Long): Long {
            var x = z + -7046029254386353131L
            x = (x xor (x ushr 30)) * -4658895280553007687L
            x = (x xor (x ushr 27)) * -7723592293110705685L
            return x xor (x ushr 31)
        }

        fun generate(difficulty: SkateDifficulty, seed: Long): SkateWorld {
            val random = Random(mix(seed))
            val length = difficulty.length
            val theme = random.nextInt(THEMES)

            // 1. les rangées et le couloir libre, qui serpente sans jamais exiger plus que LATERAL_SAFE
            class Row(val z: Float, val gap: Float, val ramp: Boolean, val center: Float)
            val rows = ArrayList<Row>()
            val path = ArrayList<PathPoint>()
            path += PathPoint(0f, 0f)
            var z = START_CLEAR
            var previousZ = 0f
            var center = 0f
            var lastRamp = START_CLEAR - difficulty.rampEvery * 0.4f
            while (z < length - END_CLEAR) {
                val p = z / length
                val gap = lerp(difficulty.gapStart, difficulty.gapEnd, p) * (0.85f + 0.3f * random.nextFloat())
                // le couloir se décale au plus de ce que le skateur sait faire entre la rangée d'avant et celle-ci, à la vitesse (la plus grande) de celle-ci
                val maxShift = 0.5f * LATERAL_SAFE * (z - previousZ) / difficulty.speedAt(z)
                center = (center + (random.nextFloat() * 2f - 1f) * maxShift).coerceIn(-CENTER_MAX, CENTER_MAX)
                val ramp = z - lastRamp >= difficulty.rampEvery * (0.8f + 0.4f * random.nextFloat()) && z < length - RAMP_END_CLEAR
                path += PathPoint(z, center)
                rows += Row(z, gap, ramp, center)
                if (ramp) lastRamp = z
                previousZ = z
                // une rampe prend de la place : la rangée suivante vient après elle
                z += gap + if (ramp) RAMP_LENGTH + 2f else 0f
            }
            path += PathPoint(length + 60f, path.last().x)

            // 2. les pièces, toutes hors du couloir
            val pieces = ArrayList<Piece>()
            var nextId = 0
            for (row in rows) {
                val p = row.z / length
                if (row.ramp) {
                    val x = (row.center + (random.nextFloat() * 2f - 1f) * 0.08f).coerceIn(-0.7f, 0.7f)
                    pieces += Piece(nextId++, PieceKind.RAMP, row.z, x, 0)
                    continue
                }
                val mean = lerp(difficulty.perRowStart, difficulty.perRowEnd, p)
                var n = mean.toInt() + if (random.nextFloat() < mean - mean.toInt()) 1 else 0
                val placed = ArrayList<Piece>()
                var attempts = 0
                while (placed.size < n && attempts++ < n * 8) {
                    val kind = pickKind(random, difficulty, p)
                    val hw = kind.width / 2f
                    val z2 = row.z + (random.nextFloat() * 2f - 1f) * ROW_JITTER * row.gap
                    val x = -0.95f + hw + random.nextFloat() * (1.9f - kind.width)
                    val centerHere = pathCenter(path, z2)
                    if (abs(x - centerHere) < CORRIDOR_HALF + hw + CORRIDOR_CLEAR) continue
                    if (placed.any { abs(it.z - z2) < 1.2f + (it.kind.depth + kind.depth) / 2f && abs(it.x - x) < (it.kind.width + kind.width) / 2f + 0.05f }) continue
                    val crossing = kind == PieceKind.PEDESTRIAN && random.nextFloat() < crossingChance(difficulty, p)
                    placed += if (crossing) {
                        val side = if (x >= 0f) 1f else -1f
                        Piece(nextId++, kind, z2, x, random.nextInt(PEDESTRIAN_LOOKS), fromX = side * 1.32f, walkStart = 28f + random.nextFloat() * 6f, walkEnd = 5f + random.nextFloat() * 3f)
                    } else {
                        Piece(nextId++, kind, z2, x, random.nextInt(if (kind == PieceKind.PEDESTRIAN) PEDESTRIAN_LOOKS else 3))
                    }
                }
                pieces += placed
            }
            val sorted = pieces.sortedBy { it.z }
            return SkateWorld(difficulty, seed, sorted.mapIndexed { i, piece -> Piece(i, piece.kind, piece.z, piece.x, piece.variant, piece.fromX, piece.walkStart, piece.walkEnd) }, path, theme)
        }

        /** Les dessins différents de piéton (habits et cheveux). */
        const val PEDESTRIAN_LOOKS = 5

        /** La chance qu'un piéton traverse : jamais en Petite balade, de plus en plus ensuite. */
        private fun crossingChance(d: SkateDifficulty, p: Float): Float = when (d.tier) {
            0 -> 0f
            1 -> 0.15f + 0.3f * p
            2 -> 0.25f + 0.35f * p
            else -> 0.4f + 0.4f * p
        }

        /** Le genre d'obstacle tiré : les piétons dominent ; les poubelles, bancs et barrières arrivent avec la difficulté et au fil de la course. */
        private fun pickKind(random: Random, d: SkateDifficulty, p: Float): PieceKind {
            val weights = ArrayList<Pair<PieceKind, Int>>()
            weights += PieceKind.PEDESTRIAN to 6
            weights += PieceKind.LAMP to 3
            weights += PieceKind.CONE to 3
            if (p > 0.12f || d.tier >= 1) weights += PieceKind.BIN to 2
            if (d.tier >= 1 && p > 0.25f) weights += PieceKind.BENCH to 2
            if (d.tier >= 2 && p > 0.3f) weights += PieceKind.BARRIER to 2
            var pick = random.nextInt(weights.sumOf { it.second })
            for ((kind, w) in weights) {
                if (pick < w) return kind
                pick -= w
            }
            return PieceKind.PEDESTRIAN
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
