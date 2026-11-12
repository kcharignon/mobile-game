package com.kcharignon.petitsjeux.games.skate.engine

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.min

/** Ce qui arrive pendant la course (le son, la vibration et les animations s'y accrochent ; les règles n'en dépendent pas). */
sealed interface SkateEvent {
    /** Le skateur a heurté [piece] : une étoile de moins (s'il en restait), clignotement, ralentissement. [livesLeft] = étoiles restantes. */
    class Hit(val piece: Piece, val livesLeft: Int) : SkateEvent

    /** Le skateur décolle d'une rampe ; [starBack] : cela lui rend une étoile. */
    class Jump(val ramp: Piece, val starBack: Boolean) : SkateEvent

    /** Il retombe sur ses roues. */
    data object Land : SkateEvent

    /** Il passe sous l'arche d'arrivée. */
    data object Finish : SkateEvent

    /** Il s'est arrêté après l'arche : la course est finie. */
    data object Done : SkateEvent
}

/**
 * Une course de Super Skate, du départ à l'arche d'arrivée, dans la rue [world]. Du pur Kotlin, sans Android : l'écran lui donne le temps ([update]) et le doigt ([steer]).
 *
 * Les règles : on part avec 3 étoiles ([lives]) ; un choc en enlève une (jamais en dessous de 0), une rampe prise en rend une ;
 * on finit toujours la course, avec au moins une étoile ([stars]). Après un choc, la vitesse tombe à 30 %, reste basse un instant, puis remonte ; on est protégé
 * [HURT_SECONDS] secondes (le skateur clignote) ; en l'air on ne heurte rien.
 */
class SkateRun(val world: SkateWorld) {
    /** Avancée depuis la ligne de départ (m), position de côté (-1 à 1), vitesse (m/s). */
    var dist = 0f
        private set
    var x = 0f
        private set
    var speed = 0f
        private set

    /** Où le doigt veut le skateur (le skateur s'y rend en un instant, mais pas plus vite que [LATERAL_MAX]). */
    var targetX = 0f
        private set

    var lives = SkateDifficulty.MAX_STARS
        private set
    var hits = 0
        private set
    var jumps = 0
        private set
    var score = 0
        private set

    /** Temps de course écoulé (s). */
    var time = 0f
        private set

    /** Temps depuis le dernier choc (s), grand tant qu'il n'y en a pas eu : pour l'animation du choc. */
    var sinceHit = 1000f
        private set

    /** Protection restante après un choc (s). */
    var hurtLeft = 0f
        private set

    /** Temps depuis le dernier atterrissage (s), pour l'animation de la réception. */
    var sinceLand = 1000f
        private set

    /** Temps passé en l'air (s) ; `null` si le skateur est au sol. */
    var airTime: Float? = null
        private set

    var finished = false
        private set
    var done = false
        private set

    /** Les pièces heurtées, avec le temps écoulé depuis (elles sont projetées sur le côté) : identifiant -> (âge, sens). */
    private val knocks = HashMap<Int, Knock>()
    class Knock(var age: Float, val direction: Float)

    /** Les pièces heurtées et leur âge, pour l'animation. */
    val knocked: Map<Int, Knock> get() = knocks

    private var holdLeft = 0f
    private var passIndex = 0
    private var scanIndex = 0

    /** Étoiles de la course : celles qui restent, au moins une. */
    val stars: Int get() = SkateDifficulty.stars(lives)

    val airborne: Boolean get() = airTime != null

    /** Avancement de la course, de 0 à 1. */
    val progress: Float get() = (dist / world.length).coerceIn(0f, 1f)

    /** Le doigt a glissé de [dx] unités de rue (vers la droite si positif). */
    fun steer(dx: Float) {
        targetX = (targetX + dx).coerceIn(-MAX_X, MAX_X)
    }

    /** Place directement la cible (le robot des tests). */
    fun steerTo(newX: Float) {
        targetX = newX.coerceIn(-MAX_X, MAX_X)
    }

    /**
     * Fait avancer la course de [dt] secondes (par petits pas de 1/60 s au plus : le résultat ne dépend pas de la cadence de l'écran).
     * Renvoie ce qui s'est passé, dans l'ordre.
     */
    fun update(dt: Float): List<SkateEvent> {
        if (done) return emptyList()
        val events = ArrayList<SkateEvent>(2)
        var left = dt.coerceIn(0f, 0.5f)
        while (left > 0f && !done) {
            val h = min(left, STEP)
            step(h, events)
            left -= h
        }
        return events
    }

    private fun step(h: Float, events: MutableList<SkateEvent>) {
        time += h
        sinceHit += h
        sinceLand += h
        if (hurtLeft > 0f) hurtLeft = (hurtLeft - h).coerceAtLeast(0f)
        for (k in knocks.values) k.age += h

        // la vitesse : on monte vers la vitesse de la rue (et on part de zéro) ; après l'arche on freine ; juste après un choc on reste lent un instant
        if (holdLeft > 0f) {
            holdLeft = (holdLeft - h).coerceAtLeast(0f)
        } else {
            val target = if (finished) 0f else world.difficulty.speedAt(dist)
            speed = if (speed < target) min(target, speed + ACCELERATION * h) else (speed - (if (finished) BRAKE else 8f) * h).coerceAtLeast(target)
        }

        // de côté : le skateur rejoint la cible en un instant, sans jamais dépasser LATERAL_MAX
        val move = (targetX - x) * (1f - exp(-FOLLOW * h))
        x += move.coerceIn(-LATERAL_MAX * h, LATERAL_MAX * h)

        val before = dist
        dist += speed * h

        airTime?.let {
            val t = it + h
            if (t >= AIR_SECONDS) {
                airTime = null
                sinceLand = 0f
                events += SkateEvent.Land
            } else {
                airTime = t
            }
        }

        // les pièces : la fenêtre de recherche suit le skateur
        while (scanIndex < world.pieces.size && world.pieces[scanIndex].z + world.pieces[scanIndex].kind.depth + 1f < before) scanIndex++
        var i = scanIndex
        while (i < world.pieces.size && world.pieces[i].z - 1f <= dist + 1f) {
            val piece = world.pieces[i++]
            if (piece.kind == PieceKind.RAMP) {
                val lip = piece.z + PieceKind.RAMP.depth
                if (before < lip && dist >= lip && !airborne && abs(x - piece.x) < PieceKind.RAMP.width / 2f) {
                    val back = lives < SkateDifficulty.MAX_STARS
                    airTime = 0f
                    jumps++
                    score += RAMP_POINTS
                    if (back) lives++
                    events += SkateEvent.Jump(piece, back)
                }
                continue
            }
            if (piece.id in knocks || airborne || hurtLeft > 0f) continue
            val half = piece.kind.depth / 2f
            if (piece.z + half < before - SKATER_DEPTH || piece.z - half > dist + SKATER_DEPTH) continue
            val px = piece.xAt(piece.z - dist)
            if (abs(x - px) < (piece.kind.width + SKATER_WIDTH) / 2f * LENIENCY) {
                hits++
                if (lives > 0) lives--
                hurtLeft = HURT_SECONDS
                sinceHit = 0f
                speed = min(speed, 0.3f * world.difficulty.speedAt(dist))
                holdLeft = HOLD_SECONDS
                knocks[piece.id] = Knock(0f, if (px >= x) 1f else -1f)
                events += SkateEvent.Hit(piece, lives)
            }
        }

        // les points : 10 par obstacle dépassé sans l'avoir heurté
        while (passIndex < world.pieces.size && world.pieces[passIndex].z + world.pieces[passIndex].kind.depth < dist) {
            val piece = world.pieces[passIndex++]
            if (piece.kind.solid && piece.id !in knocks) {
                score += PASS_POINTS
            }
        }

        if (!finished && dist >= world.length) {
            finished = true
            holdLeft = 0f
            events += SkateEvent.Finish
        }
        if (finished && speed <= 0.05f) {
            done = true
            speed = 0f
            events += SkateEvent.Done
        }
    }

    /** La hauteur du skateur au-dessus du sol (unités de rue) : sur la rampe qui monte, ou dans son saut. */
    val height: Float
        get() {
            val t = airTime
            if (t != null) {
                val tau = t / AIR_SECONDS
                return RAMP_HEIGHT * (1f - tau) + 4f * AIR_HEIGHT * tau * (1f - tau)
            }
            val ramp = rampUnder()
            return if (ramp != null) RAMP_HEIGHT * ((dist - ramp.z) / PieceKind.RAMP.depth).coerceIn(0f, 1f) else 0f
        }

    /** La rampe sous le skateur en ce moment (il roule dessus), s'il y en a une. */
    private fun rampUnder(): Piece? {
        var i = scanIndex
        while (i < world.pieces.size && world.pieces[i].z <= dist + 0.1f) {
            val p = world.pieces[i++]
            if (p.kind == PieceKind.RAMP && dist >= p.z && dist <= p.z + PieceKind.RAMP.depth && abs(x - p.x) < PieceKind.RAMP.width / 2f) return p
        }
        return null
    }

    /** Avancement du saut, de 0 à 1 (0 au sol) : la figure en l'air tourne de 0 à 360°. */
    val airProgress: Float get() = airTime?.let { it / AIR_SECONDS } ?: 0f

    /** Le skateur est-il protégé après un choc (il clignote) ? */
    val hurt: Boolean get() = hurtLeft > 0f

    companion object {
        const val STEP = 1f / 60f

        /** La largeur du skateur et la marge de bonté des chocs : il faut se toucher bien franchement (comme dans les autres jeux, on reste gentil). */
        const val SKATER_WIDTH = 0.20f
        const val LENIENCY = 0.80f
        const val SKATER_DEPTH = 0.25f

        /** Le skateur reste dans la rue : il ne va jamais jusqu'au bord. */
        const val MAX_X = 0.88f

        /** Vitesse de côté maximale (unités par seconde) : traverser toute la rue prend 0,6 s. */
        const val LATERAL_MAX = 3.2f
        private const val FOLLOW = 14f

        const val ACCELERATION = 4.5f
        const val BRAKE = 7f
        const val HURT_SECONDS = 1.6f
        const val HOLD_SECONDS = 0.35f

        const val AIR_SECONDS = 1.0f
        const val AIR_HEIGHT = 0.5f
        const val RAMP_HEIGHT = 0.3f
        const val RAMP_POINTS = 50
        const val PASS_POINTS = 10
    }
}
