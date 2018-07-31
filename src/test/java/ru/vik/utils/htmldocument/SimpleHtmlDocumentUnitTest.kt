package ru.vik.utils.htmldocument

import org.junit.Test

import org.junit.Assert.*

import ru.vik.utils.document.Paragraph
import ru.vik.utils.document.Section
import ru.vik.utils.html.Tag

class SimpleHtmlDocumentUnitTest {
    @Test
    fun test() {
        val doc = SimpleHtmlDocument()

        doc.addTag("img", BaseHtmlDocument.TagConfig(type = Tag.Type.VOID))

        // Общая проверка
        doc.text = "  <h1>  Заголовок  </h1>  \n" +
                "  <h2  >  &nbsp;  \u00A0  \u2007  \u202F  Заголовок  2  \u202f  \u2007  \u00a0  &nbsp; </h2  >  \n" +
                "  Простой  текст  \n" +
                "  Конец  тега  без  начала </a>  \n" +
                "  <tag>  Несуществующий  тег </tag>  \n" +
                "  <i>  Курсив  <b>  Жирный  курсив </i>  Тег  'b'  автоматически  закрывается  " +
                "  и  продолжается на  новом  месте  </b>  \n" +
                "  <br>  Перевод  строки <br>  \n" +
                "  <b>  Тег  'img'  без  <img>  завершающего  тега  </b>  \n" +
                "  <u>  Незакрытый  'u'  \n" +
                "  <div>  Раздел  <p>  Параграф  в  разделе  </p>  \n" +
                "  Текст  <b>  Жирный  <i>  Курсив  <- Набор  незакрытых  тегов   "

        assertEquals(4, doc.items.size)

        assertTrue(doc[0] === doc.paragraph(0))
        assertEquals("Заголовок", doc[0].text)

        assertTrue(doc[1] === doc.paragraph(1))
        assertEquals("\u00A0 \u00A0 \u2007 \u202F Заголовок 2 \u202F \u2007 \u00A0 \u00A0",
                doc[1].text)

        assertTrue(doc[2] === doc.paragraph(2))
        assertEquals("Простой текст Конец тега без начала </a> " +
                "<tag> Несуществующий тег </tag> " +
                "Курсив Жирный курсив Тег 'b' автоматически закрывается " +
                "и продолжается на новом месте" +
                "\nПеревод строки\n" +
                "Тег 'img' без  завершающего тега " +
                "Незакрытый 'u'",
                doc[2].text)

        assertTrue(doc[3] === doc.section(0))
        assertEquals(3, doc.section(0).items.size)

        assertEquals("Раздел", doc.section(0)[0].text)
        assertEquals("Параграф в разделе", doc.section(0)[1].text)
        assertEquals("Текст Жирный Курсив <- Набор незакрытых тегов", doc.section(0)[2].text)


        // Проверка пересекающихся тегов
        doc.text = "<i>i,<b>bi,<s>bis,</i>bs,</b>s</s> <div>1<p>2</div>3</p>"

        assertEquals(3, doc.items.size)
        assertEquals("i,bi,bis,bs,s", doc[0].text)

        assertTrue((doc[1] as? Section) === doc.section(0))
        assertEquals(2, doc.section(0).items.size)
        assertEquals("1", doc.section(0)[0].text)
        assertEquals("2", doc.section(0)[1].text)

        assertEquals("3</p>", doc[2].text)
    }
}
