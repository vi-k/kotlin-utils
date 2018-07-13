package ru.vik.utils.parser

import org.junit.Assert.*
import org.junit.Test

class StringParserExUnitTest {
    @Test
    fun test() {
        val parser = StringParserEx("")
        val string = "   test   test   "
        assertEquals("test   test   ", parser.trimStart(string))
        assertEquals("   test   test", parser.trimEnd(string))
        assertEquals("test   test", parser.trim(string))
    }
}