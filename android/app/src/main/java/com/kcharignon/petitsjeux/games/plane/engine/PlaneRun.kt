package com.kcharignon.petitsjeux.games.plane.engine

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.min

/** Ce qui arrive pendant le vol (le son, la vibration et les animations s'y accrochent ; les règles n'en dépendent pas). */
sealed interface PlaneEvent {
    /** L'avion a heurté [piece] : une étoile de moins (s'il en restait), clignotement, ralentissement. [livesLeft] = étoiles restantes. */
    class Hit(val piece: Piece, val livesLeft: Int) : PlaneEvent

    /** L'avion traverse un anneau lumineux et fait un tonneau ; [starBack] : cela lui rend une étoile. */
    class Roll(val ring: Piece, val starBack: Boolean) : PlaneEvent

    /** Les roues touchent la piste de l'aéroport : le vol est réussi, l'avion roule jusqu'à l'arrêt. */
    data object Finish : PlaneEvent

    /** L'avion s'est arrêté sur la piste : l'écran de fin peut s'ouvrir. */
    data object Done : PlaneEvent
}

/**
 * Un vol de Super Avion, du décollage à l'atterrissage sur la piste de l'aéroport, dans le ciel [world]. Du pur Kotlin, sans Android :
 * l'écran lui donne le temps ([update]) et le doigt ([steer]).
 *
 * Les règles : on part avec 3 étoiles ([lives]) ; un choc en enlève une (jamais en dessous de 0), un anneau traversé en rend une ;
 * on finit toujours le vol, avec au moins une étoile ([stars]). Après un choc, la vitesse tombe à 30 %, reste basse un instant, puis remonte ;
 * on est protégé [HURT_SECONDS] secondes (l'avion clignote) ; pendant un tonneau on ne heurte rien. Pour finir, [APPROACH_METERS] avant la piste
 * l'avion se met en pilote automatique : il se centre sur la piste et descend jusqu'au toucher des roues.
 */
class PlaneRun(val world: PlaneWorld) {
    /** Avancée depuis le décollage (m), position de côté (-1 à 1), vitesse (m/s). */
    var dist = 0f
        private set
    var x = 0f
        private set
    var speed = 0f
        private set

    /** Où le doigt veut l'avion (l'avion s'y rend en un instant, mais pas plus vite que [LATERAL_MAX]). */
    var targetX = 0f
        private set

    var lives = PlaneDifficulty.MAX_STARS
        private set
    var hits = 0
        private set
    var rolls = 0
        private set
    var score = 0
        private set

    /** Temps de vol écoulé (s). */
    var time = 0f
        private set

    /** Temps depuis le dernier choc (s), grand tant qu'il n'y en a pas eu : pour l'animation du choc. */
    var sinceHit = 1000f
        private set

    /** Protection restante après un choc (s). */
    var hurtLeft = 0f
        private set

    /** Temps depuis le toucher des roues (s), grand avant : pour la fumée des pneus. */
    var sinceTouchdown = 1000f
        private set

    /** Temps passé dans le tonneau (s) ; `null` si l'avion vole droit. */
    var rollTime: Float? = null
        private set

    /** Le toucher des roues a eu lieu (le vol est réussi). */
    var finished = false
        private set

    /** L'avion est arrêté sur la piste. */
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

    /** Étoiles du vol : celles qui restent, au moins une. */
    val stars: Int get() = PlaneDifficulty.stars(lives)

    val rolling: Boolean get() = rollTime != null

    /** Avancement de la partie, de 0 à 1. */
    val progress: Float get() = (dist / world.length).coerceIn(0f, 1f)

    /** L'avion se pose tout seul : dans les derniers [APPROACH_METERS] il se centre sur la piste et descend, le doigt n'y change plus rien. */
    val autopilot: Boolean get() = dist >= world.length - APPROACH_METERS

    /** L'altitude, de 1 (en croisière) à 0 (les roues sur la piste) : elle ne baisse que pendant l'approche, en douceur (0 de vitesse verticale au toucher). */
    val altitude: Float
        get() {
            if (finished) return 0f
            val t = ((dist - (world.length - APPROACH_METERS)) / APPROACH_METERS).coerceIn(0f, 1f)
            return 1f - t * t * (3f - 2f * t)
        }

    /** Avancement du tonneau, de 0 à 1 (0 si l'avion vole droit). */
    val rollProgress: Float get() = rollTime?.let { it / ROLL_SECONDS } ?: 0f

    /** L'avion est-il protégé après un choc (il clignote) ? */
    val hurt: Boolean get() = hurtLeft > 0f

    /** Le doigt a glissé de [dx] unités (vers la droite si positif). Sans effet pendant l'approche finale. */
    fun steer(dx: Float) {
        if (autopilot) return
        targetX = (targetX + dx).coerceIn(-MAX_X, MAX_X)
    }

    /** Place directement la cible (le robot des tests). */
    fun steerTo(newX: Float) {
        if (autopilot) return
        targetX = newX.coerceIn(-MAX_X, MAX_X)
    }

    /**
     * Fait avancer le vol de [dt] secondes (par petits pas de 1/60 s au plus : le résultat ne dépend pas de la cadence de l'écran).
     * Renvoie ce qui s'est passé, dans l'ordre.
     */
    fun update(dt: Float): List<PlaneEvent> {
        if (done) return emptyList()
        val events = ArrayList<PlaneEvent>(2)
        var left = dt.coerceIn(0f, 0.5f)
        while (left > 0f && !done) {
            val h = min(left, STEP)
            step(h, events)
            left -= h
        }
        return events
    }

    private fun step(h: Float, events: MutableList<PlaneEvent>) {
        time += h
        sinceHit += h
        sinceTouchdown += h
        if (hurtLeft > 0f) hurtLeft = (hurtLeft - h).coerceAtLeast(0f)
        for (k in knocks.values) k.age += h

        // la vitesse : on monte vers la vitesse du trajet (et on part de zéro) ; après le toucher des roues on freine ; juste après un choc on reste lent un instant
        if (holdLeft > 0f) {
            holdLeft = (holdLeft - h).coerceAtLeast(0f)
        } else {
            val target = if (finished) 0f else world.difficulty.speedAt(dist)
            speed = if (speed < target) min(target, speed + ACCELERATION * h) else (speed - (if (finished) BRAKE else 8f) * h).coerceAtLeast(target)
        }

        // le pilote automatique se centre sur la piste
        if (autopilot) targetX += (0f - targetX) * (1f - exp(-CENTERING * h))

        // de côté : l'avion rejoint la cible en un instant, sans jamais dépasser LATERAL_MAX
        val move = (targetX - x) * (1f - exp(-FOLLOW * h))
        x += move.coerceIn(-LATERAL_MAX * h, LATERAL_MAX * h)

        val before = dist
        dist += speed * h

        rollTime?.let {
            val t = it + h
            rollTime = if (t >= ROLL_SECONDS) null else t
        }

        // les pièces : la fenêtre de recherche suit l'avion
        while (scanIndex < world.pieces.size && world.pieces[scanIndex].z + world.pieces[scanIndex].kind.depth + 1f < before) scanIndex++
        var i = scanIndex
        while (i < world.pieces.size && world.pieces[i].z - 1f <= dist + 1f) {
            val piece = world.pieces[i++]
            if (piece.kind == PieceKind.RING) {
                if (before < piece.z && dist >= piece.z && !rolling && abs(x - piece.x) < RING_HALF) {
                    val back = lives < PlaneDifficulty.MAX_STARS
                    rollTime = 0f
                    rolls++
                    score += RING_POINTS
                    if (back) lives++
                    events += PlaneEvent.Roll(piece, back)
                }
                continue
            }
            if (piece.id in knocks || rolling || hurtLeft > 0f) continue
            val half = piece.kind.depth / 2f
            if (piece.z + half < before - PLANE_DEPTH || piece.z - half > dist + PLANE_DEPTH) continue
            if (abs(x - piece.xAt(piece.z - dist)) < (piece.kind.width + PLANE_WIDTH) / 2f) {
                hits++
                if (lives > 0) lives--
                hurtLeft = HURT_SECONDS
                sinceHit = 0f
                speed = min(speed, 0.3f * world.difficulty.speedAt(dist))
                holdLeft = HOLD_SECONDS
                knocks[piece.id] = Knock(0f, if (piece.xAt(piece.z - dist) >= x) 1f else -1f)
                events += PlaneEvent.Hit(piece, lives)
            }
        }

        // les points : 10 par obstacle dépassé sans l'avoir heurté
        while (passIndex < world.pieces.size && world.pieces[passIndex].z + world.pieces[passIndex].kind.depth < dist) {
            val piece = world.pieces[passIndex++]
            if (piece.kind.solid && piece.id !in knocks) score += PASS_POINTS
        }

        if (!finished && dist >= world.length) {
            finished = true
            holdLeft = 0f
            sinceTouchdown = 0f
            events += PlaneEvent.Finish
        }
        if (finished && speed <= 0.05f) {
            done = true
            speed = 0f
            events += PlaneEvent.Done
        }
    }

    companion object {
        const val STEP = 1f / 60f

        /**
         * La largeur de l'avion pour les chocs (son envergure dessinée est plus grande : les bouts d'ailes qui frôlent ne comptent pas) et l'épaisseur du choc.
         * Pas de marge en plus : un obstacle est dans sa voie, il faut se mettre dans sa voie pour le heurter, et on ne se glisse pas entre deux voies pleines.
         */
        const val PLANE_WIDTH = 0.18f
        const val PLANE_DEPTH = 0.25f

        /** L'avion reste dans le ciel : il ne va jamais jusqu'au bord. */
        const val MAX_X = 0.88f

        /** Vitesse de côté maximale (unités par seconde) : traverser tout le ciel prend 0,6 s. */
        const val LATERAL_MAX = 3.2f
        private const val FOLLOW = 14f
        private const val CENTERING = 2.5f

        const val ACCELERATION = 4.5f
        const val BRAKE = 6f
        const val HURT_SECONDS = 1.6f
        const val HOLD_SECONDS = 0.35f

        /** Le tonneau dure une seconde ; l'anneau se traverse à ± [RING_HALF] de son centre (il est large pour être généreux). */
        const val ROLL_SECONDS = 1.0f
        const val RING_HALF = 0.22f
        const val RING_POINTS = 50
        const val PASS_POINTS = 10

        /** La distance d'approche finale, en mètres : pilote automatique, descente, toucher des roues à la distance de la piste. */
        const val APPROACH_METERS = 90f
    }
}
