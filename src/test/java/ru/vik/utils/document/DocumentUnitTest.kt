package ru.vik.utils.document

import org.junit.Assert.*
import org.junit.Test

class DocumentUnitTest {
    @Test
    fun test() {
        val doc = Document()

        // Общая проверка
        doc.text = "aaa\nbbb\rccc\r\nddd\u2029eee\u2028fff\u0085ggg"

        assertEquals(5, doc.items.size)
        assertEquals("aaa", doc[0].text)
        assertEquals("bbb", doc[1].text)
        assertEquals("ccc", doc[2].text)
        assertEquals("ddd", doc[3].text)
        assertEquals("eee\u2028fff\u0085ggg", doc[4].text)
        assertEquals("aaa\nbbb\nccc\nddd\neee\u2028fff\u0085ggg", doc.text)


        // Проверяем спаны
        doc {
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."

            assertEquals(1, items.size)

            // Спан на весь абзац
            span
            spanTestAndClear(paragraph(0), 1, 0, 123)

            paragraph(0) {
                span
                spanTestAndClear(this, 1, 0, 123)
            }


            // Проверям установку спанов на символы
            span on 0
            spanTestAndClear(paragraph(0), 1, 0, 1)

            span from 6
            spanTestAndClear(paragraph(0), 1, 6, 123)

            span to 11
            spanTestAndClear(paragraph(0), 1, 0, 11)

            span from 6 to 11
            spanTestAndClear(paragraph(0), 1, 6, 11)

            span from 12 count 5
            spanTestAndClear(paragraph(0), 1, 12, 17)

            span from 12 to 0 // Нештатная ситуация
            spanTestAndClear(paragraph(0), 1, 12, 0)

            span from 17 count -5 // Нештатная ситуация
            spanTestAndClear(paragraph(0), 1, 17, 12)


            // Проверям установку спанов на слова
            span on word(0) // Нештатная ситуация
            spanTestAndClear(paragraph(0), 1, 123, 123)

            span on word(1)
            spanTestAndClear(paragraph(0), 1, 0, 5)

            span on word(5)
            spanTestAndClear(paragraph(0), 1, 22, 26)

            span from word(2)
            spanTestAndClear(paragraph(0), 1, 6, 123)

            span to word(3)
            spanTestAndClear(paragraph(0), 1, 0, 17)

            span from word(2) to word(3)
            spanTestAndClear(paragraph(0), 1, 6, 17)

            span from word(3) count word(1)
            spanTestAndClear(paragraph(0), 1, 12, 17)

            span from word(3) count word(3)
            spanTestAndClear(paragraph(0), 1, 12, 26)


            // Проверям установку спанов на строки
            span on "" // Нештатная ситуация
            spanTestAndClear(paragraph(0), 1, 0, 0)

            span on "amet"
            spanTestAndClear(paragraph(0), 1, 22, 26)

            span from "sit"
            spanTestAndClear(paragraph(0), 1, 18, 123)

            span to "elit"
            spanTestAndClear(paragraph(0), 1, 0, 55)

            span from "sit" to "elit"
            spanTestAndClear(paragraph(0), 1, 18, 55)

            span from "sit" count word(5)
            spanTestAndClear(paragraph(0), 1, 18, 55)

            span from "dolor"
            spanTestAndClear(paragraph(0), 1, 12, 123)

            span to "dolor"
            spanTestAndClear(paragraph(0), 1, 0, 17)

            span from "dolor" to "dolor"
            spanTestAndClear(paragraph(0), 1, 12, 17)

            span from "dolor" to next("dolor")
            spanTestAndClear(paragraph(0), 1, 12, 108)

            span from "dolor" to last("dolor")
            spanTestAndClear(paragraph(0), 1, 12, 108)

            span from last("dolor")
            spanTestAndClear(paragraph(0), 1, 103, 123)

            span from next("Lorem")
            spanTestAndClear(paragraph(0), 1, 123, 123)

            span from "it"
            spanTestAndClear(paragraph(0), 1, 19, 123)

            span from next("it")
            spanTestAndClear(paragraph(0), 1, 19, 123)

            span from next("it", 2)
            spanTestAndClear(paragraph(0), 1, 53, 123)

            span from "it" to "it"
            spanTestAndClear(paragraph(0), 1, 19, 21)

            span from "it" to next("it")
            spanTestAndClear(paragraph(0), 1, 19, 55)

            span from "it" to next("it", 2)
            spanTestAndClear(paragraph(0), 1, 19, 123)


            // Проверяем установку спанов по регулярным выражениям
            span on Regex("tra-ta-ta") // Нештатная ситуация
            spanTestAndClear(paragraph(0), 1, 123, 123)

            span on Regex("") // Нештатная ситуация
            spanTestAndClear(paragraph(0), 1, 0, 0)

            span from Regex(",")
            spanTestAndClear(paragraph(0), 1, 26, 123)

            span to Regex(",")
            spanTestAndClear(paragraph(0), 1, 0, 27)

            span to next(Regex(","))
            spanTestAndClear(paragraph(0), 1, 0, 27)

            span to next(Regex(","), 2)
            spanTestAndClear(paragraph(0), 1, 0, 56)

            span from Regex(",") to Regex(",")
            spanTestAndClear(paragraph(0), 1, 26, 27)

            span from Regex(",") to next(Regex(","))
            spanTestAndClear(paragraph(0), 1, 26, 56)

            span from Regex("""(?<=, )""") to Regex(",")
            spanTestAndClear(paragraph(0), 1, 28, 56)

            span from Regex("""(?<=, )""") to Regex("""(?=,)""")
            spanTestAndClear(paragraph(0), 1, 28, 55)
        }
    }

    private fun spanTestAndClear(paragraph: Paragraph, size: Int, start: Int, end: Int) {
        assertEquals(size, paragraph.spans.size)
        assertEquals(start, paragraph.spans[0].start)
        assertEquals(end, paragraph.spans[0].end)
        paragraph.spans.clear()
    }
}
