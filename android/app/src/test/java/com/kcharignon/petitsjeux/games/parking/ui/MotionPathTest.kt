package com.kcharignon.petitsjeux.games.parking.ui

import com.kcharignon.petitsjeux.games.parking.engine.Car
import com.kcharignon.petitsjeux.games.parking.engine.Cell
import com.kcharignon.petitsjeux.games.parking.engine.Dir
import com.kcharignon.petitsjeux.games.parking.engine.Move
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotionPathTest {
    private val right = Car(0, 0, 2, 2, Dir.RIGHT, 0) // cases 0,2 et 1,2 ; centre (1,0 ; 2,5)

    @Test
    fun atRestTheVehicleIsWhereItParked() {
        val path = MotionPath(right, Move.FORWARD, right.route(6, 5, Move.FORWARD).cells, Dir.RIGHT)
        val pose = path.at(0f)
        assertEquals(1.0f, pose.col, 0.001f)
        assertEquals(2.5f, pose.row, 0.001f)
        assertEquals(90f, pose.travelDegrees, 0.001f)
    }

    @Test
    fun drivingStraightKeepsTheHeading() {
        val path = MotionPath(right, Move.FORWARD, right.route(6, 5, Move.FORWARD).cells, Dir.RIGHT)
        val pose = path.at(2f)
        assertEquals(3.0f, pose.col, 0.001f)
        assertEquals(2.5f, pose.row, 0.001f)
        assertEquals(90f, pose.travelDegrees, 0.001f)
        // bien au-delà du bord, il continue tout droit (le nez dépasse la colonne 5)
        assertTrue(path.at(8f).col > 6f)
    }

    @Test
    fun onOilTheVehicleTurnsRightAndEndsHeadingDown() {
        val route = right.route(6, 5, Move.FORWARD, listOf(Cell(3, 2)))
        val path = MotionPath(right, Move.FORWARD, route.cells, route.finalDirection)
        // quand le centre est sur la tache : à mi-virage, le cap est entre la droite (90°) et le bas (180°)
        val onOil = path.at(2.5f) // centre à 1,0 + 2,5 = 3,5 -> sur la tache (3,2)
        assertEquals(3.5f, onOil.col, 0.001f)
        assertEquals(2.5f, onOil.row, 0.001f)
        assertEquals(135f, onOil.travelDegrees, 1f)
        // plus loin, il roule vers le bas sur la colonne 3
        val after = path.at(5f)
        assertEquals(3.5f, after.col, 0.001f)
        assertTrue(after.row > 4f)
        assertEquals(180f, after.travelDegrees, 0.001f)
    }

    @Test
    fun reversingFlipsTheDrawnHeadingButFollowsTheSameKindOfPath() {
        val back = Car(0, 3, 2, 2, Dir.RIGHT, 0) // cases 3,2 et 4,2 ; recule vers la gauche
        val route = back.route(6, 5, Move.BACKWARD, listOf(Cell(1, 2)))
        val path = MotionPath(back, Move.BACKWARD, route.cells, route.finalDirection)
        val start = path.at(0f)
        assertEquals(270f, start.travelDegrees, 0.001f) // il roule vers la gauche
        assertEquals(90f, path.headingDegrees(start.travelDegrees), 0.001f) // mais son nez regarde toujours à droite
        // après le virage à gauche (vers le bas de l'écran), il roule vers le bas, le nez en haut
        val end = path.at(5f)
        assertEquals(180f, end.travelDegrees, 0.001f)
        assertEquals(0f, path.headingDegrees(end.travelDegrees), 0.001f)
    }
}
