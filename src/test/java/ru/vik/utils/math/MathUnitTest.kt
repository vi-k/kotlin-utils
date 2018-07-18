package ru.vik.utils.math

import org.junit.Test

import org.junit.Assert.*

import ru.vik.utils.math.posRoundToInt
import ru.vik.utils.math.simpleRoundToInt
import ru.vik.utils.math.speedTest

class MathUnitTest {
    @Test
    fun test() {
        assertEquals(0, 0.1f.posRoundToInt())
        assertEquals(0, 0.499999f.posRoundToInt())
        assertEquals(1, 0.5f.posRoundToInt())
        assertEquals(1, 0.999999f.posRoundToInt())
        assertEquals(1, 1.499999f.posRoundToInt())
        assertEquals(0, (-0.0f).posRoundToInt())
        assertEquals(0, (-0.1f).posRoundToInt())
        assertEquals(-1, (-0.500001f).simpleRoundToInt())
        assertEquals(-1, (-0.6f).simpleRoundToInt())


//        speedTest(1000)
    }
}