package ru.vik.utils

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import ru.vik.utils.color.speedTest

@RunWith(AndroidJUnit4::class)
class ColorInstrumentedTest {
    @Test
    fun test() {
        speedTest(500)
        speedTest(500)
        speedTest(500)
    }
}
