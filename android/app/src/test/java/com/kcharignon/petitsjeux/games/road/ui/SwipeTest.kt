package com.kcharignon.petitsjeux.games.road.ui

import com.kcharignon.petitsjeux.games.road.engine.HopDir
import org.junit.Assert.assertEquals
import org.junit.Test

class SwipeTest {
    @Test
    fun aSwipeGoesTheWayTheFingerGoes() {
        // l'écran compte y vers le bas : un doigt qui monte (dy < 0) fait avancer le poussin, qui monte aussi à l'écran
        assertEquals(HopDir.FORWARD, swipeDirection(0f, -80f))
        assertEquals(HopDir.BACK, swipeDirection(0f, 80f))
        assertEquals(HopDir.LEFT, swipeDirection(-80f, 0f))
        assertEquals(HopDir.RIGHT, swipeDirection(80f, 0f))
    }

    @Test
    fun theDominantAxisWinsWhenTheFingerIsCrooked() {
        assertEquals(HopDir.FORWARD, swipeDirection(30f, -80f))
        assertEquals(HopDir.RIGHT, swipeDirection(80f, -30f))
        assertEquals(HopDir.LEFT, swipeDirection(-80f, 60f))
        assertEquals(HopDir.BACK, swipeDirection(-20f, 60f))
    }
}
