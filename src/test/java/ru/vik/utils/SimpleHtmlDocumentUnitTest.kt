package ru.vik.utils

import org.junit.Test

import org.junit.Assert.*

import ru.vik.utils.document.Paragraph
import ru.vik.utils.document.Section
import ru.vik.utils.html.Tag
import ru.vik.utils.htmldocument.BaseHtmlDocument
import ru.vik.utils.htmldocument.SimpleHtmlDocument

class SimpleHtmlDocumentUnitTest {
    @Test
    fun test() {
        val parser = SimpleHtmlDocument()

        parser.addTag("img", BaseHtmlDocument.TextConfig(type = Tag.Type.VOID))

        // Общая проверка
        parser.setHtml("  <h1>  Заголовок  </h1>  \n" +
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
                     "  Текст  <b>  Жирный  <i>  Курсив  <- Набор  незакрытых  тегов   ")
        assertEquals(4, parser.root!!.paragraphs.size)
        assertEquals("Заголовок",
                (parser.root!!.paragraphs[0] as? Paragraph)!!.text.toString())
        assertEquals("\u00A0 \u00A0 \u2007 \u202F Заголовок 2 \u202F \u2007 \u00A0 \u00A0",
                (parser.root!!.paragraphs[1] as? Paragraph)!!.text.toString())
        assertEquals("Простой текст Конец тега без начала </a> " +
                     "<tag> Несуществующий тег </tag> " +
                     "Курсив Жирный курсив Тег 'b' автоматически закрывается " +
                     "и продолжается на новом месте" +
                     "\nПеревод строки\n" +
                     "Тег 'img' без  завершающего тега " +
                     "Незакрытый 'u'",
                (parser.root!!.paragraphs[2] as? Paragraph)!!.text.toString())
        assertEquals(3, (parser.root!!.paragraphs[3] as? Section)!!.paragraphs.size)
        assertEquals("Раздел",
                ((parser.root!!.paragraphs[3] as? Section)!!.paragraphs[0] as? Paragraph)!!.text.toString())
        assertEquals("Параграф в разделе",
                ((parser.root!!.paragraphs[3] as? Section)!!.paragraphs[1] as? Paragraph)!!.text.toString())
        assertEquals("Текст Жирный Курсив <- Набор незакрытых тегов",
                ((parser.root!!.paragraphs[3] as? Section)!!.paragraphs[2] as? Paragraph)!!.text.toString())

        // Проверка пересекающихся тегов
        parser.setHtml("<i>i,<b>bi,<s>bis,</i>bs,</b>s</s> <div>1<p>2</div>3</p>")
        assertEquals(3, parser.root!!.paragraphs.size)
        assertEquals("i,bi,bis,bs,s",
                (parser.root!!.paragraphs[0] as? Paragraph)!!.text.toString())
        assertEquals(2, (parser.root!!.paragraphs[1] as? Section)!!.paragraphs.size)
        assertEquals("1",
                ((parser.root!!.paragraphs[1] as? Section)!!.paragraphs[0] as? Paragraph)!!.text.toString())
        assertEquals("2",
                ((parser.root!!.paragraphs[1] as? Section)!!.paragraphs[1] as? Paragraph)!!.text.toString())
        assertEquals("3</p>",
                (parser.root!!.paragraphs[2] as? Paragraph)!!.text.toString())
    }
}