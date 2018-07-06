package ru.vik.utils.htmldocument

import org.junit.Test

import org.junit.Assert.*

class BaseHtmlDocumentUnitTest {

    fun Int.a(a: Int): Int {
        return this or (a shl 24)
    }

    @Test
    fun test() {
//        val doc = BaseHtmlDocument()

        // toHtmlColor()
        assertEquals(0x112233.a(0xff), "#112233".toHtmlColor())
        assertEquals(0xabcdef.a(0xff), "#abcdef".toHtmlColor())
        assertEquals(0xABCDEF.a(0xff), "#ABCDEF".toHtmlColor())
        assertEquals(0xaabbcc.a(0xff), "#abc".toHtmlColor())
        assertEquals(0x112233.a(0xff), "#123".toHtmlColor())
        assertEquals(0x0080ff.a(0xff), "rgb(0, 128, 255)".toHtmlColor())
        assertEquals(0x204060.a(0xff), "rgb( 32,  64,  96 )".toHtmlColor())
        assertEquals(0x102030.a(0xff), "rgb(16,32,48)".toHtmlColor())
        assertEquals(0x102030.a(128), "rgba(16,32,48,0.5)".toHtmlColor())
        assertEquals(0x102030.a(64), "rgba(16,32,48,0.25)".toHtmlColor())
        assertEquals(0x102030.a(191), "rgba(16,32,48,0.75)".toHtmlColor())
        assertEquals(0x102030.a(255), "rgba(16,32,48,1.0)".toHtmlColor())

        assertEquals(null, "#abcdef0".toHtmlColor())
        assertEquals(null, "#abcdeg".toHtmlColor())
        assertEquals(null, "#abcdeg ".toHtmlColor())
        assertEquals(null, " #abcdeg".toHtmlColor())
        assertEquals(null, "#abcde".toHtmlColor())
        assertEquals(null, "#abcd".toHtmlColor())
        assertEquals(null, "rgba(16,32,48)".toHtmlColor())
        assertEquals(null, "rgb(16,32,48,0.5)".toHtmlColor())

        // toHtmlSize

        // splitBySpace
        assertEquals(listOf("1", "2", "3"), "1 2 3".splitBySpace())
        assertEquals(listOf("1", "2", "3", "4"), "1   2   3   4".splitBySpace())
        assertEquals(listOf("AAA", "BBB", "CCC", "DDD", "EEE"),
                "  AAA  BBB  CCC  DDD  EEE".splitBySpace())
    }
}