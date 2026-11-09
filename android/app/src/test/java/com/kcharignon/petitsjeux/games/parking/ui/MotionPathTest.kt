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
        val path = MotionPath(right, Move.FORWARD, right.route(6, 5, Move.FORWARD))
        val pose = path.at(0f)
        assertEquals(1.0f, pose.col, 0.001f)
        assertEquals(2.5f, pose.row, 0.001f)
        assertEquals(90f, pose.travelDegrees, 0.001f)
    }

    @Test
    fun drivingStraightKeepsTheHeading() {
        val path = MotionPath(right, Move.FORWARD, right.route(6, 5, Move.FORWARD))
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
        val path = MotionPath(right, Move.FORWARD, route)
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
        val path = MotionPath(back, Move.BACKWARD, route)
        val start = path.at(0f)
        assertEquals(270f, start.travelDegrees, 0.001f) // il roule vers la gauche
        assertEquals(90f, path.headingDegrees(start.travelDegrees), 0.001f) // mais son nez regarde toujours à droite
        // après le virage à gauche (vers le bas de l'écran), il roule vers le bas, le nez en haut
        val end = path.at(5f)
        assertEquals(180f, end.travelDegrees, 0.001f)
        assertEquals(0f, path.headingDegrees(end.travelDegrees), 0.001f)
    }

    @Test
    fun onABeltTheVehicleSlidesSidewaysWithoutTurning() {
        val c = Car(0, 0, 2, 2, Dir.RIGHT, 0)
        val route = c.route(6, 5, Move.FORWARD, com.kcharignon.petitsjeux.games.parking.engine.Floor(belts = mapOf(Cell(3, 2) to Dir.DOWN)))
        val path = MotionPath(c, Move.FORWARD, route)
        // le cap reste vers la droite (90°) pendant que le centre descend d'une case, de la ligne 2 à la ligne 3 (le tapis est en (3,2))
        val before = path.at(2.5f) // le centre est sur le tapis (3,2)
        val sliding = path.at(3f) // à mi-chemin du glissement
        val after = path.at(3.5f) // arrivé en (3,3)
        assertEquals(2.5f, before.row, 0.001f)
        assertEquals(3.0f, sliding.row, 0.001f)
        assertEquals(3.5f, after.row, 0.001f)
        for (pose in listOf(before, sliding, after)) {
            assertEquals(90f, pose.travelDegrees, 0.5f)
            assertEquals(1f, pose.alpha, 0.001f)
        }
    }

    @Test
    fun throughAPortalTheVehicleFadesOutThenInOnTheOtherSide() {
        val c = Car(0, 0, 2, 2, Dir.RIGHT, 0)
        val floor = com.kcharignon.petitsjeux.games.parking.engine.Floor(portals = mapOf(Cell(3, 2) to Cell(2, 0), Cell(2, 0) to Cell(3, 2)))
        val route = c.route(6, 5, Move.FORWARD, floor)
        val path = MotionPath(c, Move.FORWARD, route)
        // cases du trajet : corps (0,2) (1,2), puis (2,2) (3,2) -> saut -> (2,0) ; le saut est le segment 3 -> 4 (du centre de (3,2) à celui de (2,0))
        val enter = path.pointAt(3.1f)
        val exit = path.pointAt(3.9f)
        assertTrue("il s'efface à l'entrée : ${enter.alpha}", enter.alpha < 1f && enter.alpha > 0f)
        assertEquals(3.5f, enter.col, 0.001f) // il est toujours au téléporteur d'entrée
        assertEquals(2.5f, exit.col, 0.001f) // puis déjà au téléporteur de sortie
        assertEquals(0.5f, exit.row, 0.001f)
        assertTrue(path.pointAt(3.5f).alpha < 0.05f) // tout à fait invisible entre les deux
        assertEquals(1f, path.pointAt(5f).alpha, 0.001f)
    }
}
