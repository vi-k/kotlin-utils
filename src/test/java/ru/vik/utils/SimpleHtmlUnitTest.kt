package ru.vik.utils

import org.junit.Test

import org.junit.Assert.*
import ru.vik.utils.html.BaseHtml
import ru.vik.utils.html.SimpleHtml
import ru.vik.utils.html.Tag

class SimpleHtmlUnitTest {
    @Test
    fun test() {
        val parser = SimpleHtml()

        parser.addTag("img", BaseHtml.TagConfig(type = Tag.Type.VOID))

        // Общая проверка
        parser.parse("  <h1>  Заголовок  </h1>  \n" +
                     "  <h2  >  &nbsp;  \u00A0  \u2007  \u202F  Заголовок  2  \u202f  \u2007  \u00a0  &nbsp; </h2  >  \n" +
                     "  Простой  текст  \n" +
                     "  Конец  тега  без  начала </a>  \n" +
                     "  <tag>  Несуществующий  тег </tag>  \n" +
                     "  <i>  Курсив  <b>  Жирный  курсив </i>  Тег  'b'  автоматически  закрывается  " +
                     "  и  продолжается на  новом  месте  </b>  \n" +
                     "  <br>  Перевод  строки <br>  \n" +
                     "  <b>  Тег  'img'  без  <img>  завершающего  тега  </b>  \n" +
                     "  <span>  Незакрытый  'span'  \n" +
                     "  <div>  Раздел  <p>  Параграф  в  разделе  </p>  \n" +
                     "  Текст  <b>  Жирный  <i>  Курсив  <- Набор  незакрытых  тегов   ")
        assertEquals(
                "<h1>Заголовок</h1>" +
                "<h2>&nbsp; &nbsp; \u2007 \u202F Заголовок 2 \u202F \u2007 &nbsp; &nbsp;</h2>" +
                "Простой текст " +
                "Конец тега без начала &lt;/a&gt; " +
                "&lt;tag&gt; Несуществующий тег &lt;/tag&gt; " +
                "<i>Курсив <b>Жирный курсив </b></i><b>Тег 'b' автоматически закрывается " +
                "и продолжается на новом месте</b>" +
                "<br>Перевод строки<br>" +
                "<b>Тег 'img' без <img> завершающего тега </b>" +
                "<span>Незакрытый 'span'<div>Раздел<p>Параграф в разделе</p>" +
                "Текст <b>Жирный <i>Курсив &lt;- Набор незакрытых тегов</i></b></div></span>",
                parser.toString())
        assertEquals(10, parser.root!!.children.size)

        // Аттрибуты
        parser.parse(
                "  <span  a  b = abcd123;'\"_()/-=  c = ' a \"a\" &apos;b&apos; &#10; &#10 \n ' " +
                "                                        d=\"a &quot;a&quot; 'b' \n &#x0a &#x0a; \"  >   " +
                "  <div  a=''b=\"\"c d=' &#x00A0; &#x000d; &#x000A; &#xFfFf; [&#x00AD;] &#x03A9; &#x2627; &#x8000; '> ")
        assertEquals(
                "<span a b='abcd123;&apos;\"_()/-=' c=' a \"a\" &apos;b&apos; \n &amp;#10 \n ' " +
                "d='a \"a\" &apos;b&apos; \n &amp;#x0a \n '>" +
                "<div a='' b='' c d=' &nbsp; \r \n \uFFFF [\u00AD] Ω ☧ 耀 '>" +
                "</div></span>",
                parser.toString())

        // Символы
        parser.parse(
                "  &#x20;  &#32;  &amp;  &lt;  &gt;  &nbsp;  &#13;  &#10;  &#1234;  &#x1234;  " +
                "&shy;  &le;  &frac12;  &amp  &lt  &#1234  &#x1234 &gt  ")
        assertEquals(
                "&amp; &lt; &gt; &nbsp; \u04d2 \u1234 " +
                "&amp;shy; &amp;le; &amp;frac12; &amp;amp &amp;lt &amp;#1234 &amp;#x1234 &amp;gt",
                parser.toString())

        // Проверка удаления пустых фиктивных тегов
        parser.parse("<b>b</b> <i></i> <p> <i></i> </p>")
        assertEquals(3, parser.root!!.children.size)
        assertEquals(1, parser.root!!.children[2].children.size)

        // Проверка пересекающихся тегов
        parser.parse("<i>i,<b>bi,<s>bis,</i>blockStyle,</b>s</s> <div>1<p>2</div>3</p>")
        assertEquals(
                "<i>i,<b>bi,<s>bis,</s></b></i><b><s>blockStyle,</s></b><s>s</s>" +
                "<div>1<p>2</p></div>3&lt;/p&gt;",
                parser.toString())

        parser.parse("<i ii=1>i,<b bb=2>bi,<s ss=3>bis,</i>blockStyle,</b>s</s>")
        assertEquals(
                "<i ii='1'>i,<b bb='2'>bi,<s ss='3'>bis,</s></b></i>" +
                "<b bb='2'><s ss='3'>blockStyle,</s></b><s ss='3'>s</s>",
                parser.toString())
    }
}