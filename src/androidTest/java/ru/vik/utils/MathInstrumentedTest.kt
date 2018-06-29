package ru.vik.utils

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import ru.vik.utils.math.speedTest

@RunWith(AndroidJUnit4::class)
class MathInstrumentedTest {
    @Test
    fun test() {
        speedTest(500)
        speedTest(500)
        speedTest(500)
    }
}
