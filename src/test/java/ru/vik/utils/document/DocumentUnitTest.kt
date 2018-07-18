package ru.vik.utils.document

import org.junit.Assert.*
import org.junit.Test

class DocumentUnitTest {
    @Test
    fun test() {
        val doc = Document()

        doc.setText("aaa\nbbb\rccc\r\nddd\u2029eee\u2028fff\u0085ggg")

        assertEquals(5, doc.paragraphs.size)
        assertEquals("aaa", doc[0].text)
        assertEquals("bbb", doc[1].text)
        assertEquals("ccc", doc[2].text)
        assertEquals("ddd", doc[3].text)
        assertEquals("eee\u2028fff\u0085ggg", doc[4].text)
        assertEquals("aaa\nbbb\nccc\nddd\neee\u2028fff\u0085ggg", doc.text)
    }
}