package ru.vik.utils.document

import ru.vik.utils.parser.StringParser
import ru.vik.utils.parser.parseWord

class Paragraph(text: String? = null) : ParagraphItem {
    var parent: Section? = null
    val spans = mutableListOf<Span>()
    internal val textBuilder = if (text != null) StringBuilder(text) else StringBuilder()
    override val borderStyle = BorderStyle()
    override val paragraphStyle = ParagraphStyle()
    override val characterStyle = CharacterStyle()
    override var data: Any? = null

    override var text: String
        get() = this.textBuilder.toString()
        set(value) {
            this.spans.clear()
            this.textBuilder.setLength(0)
            this.textBuilder.append(value)
        }

    /**
     * Добавление участка форматирования
     *
//     * Если span.start положительное число, то span.end тоже должен быть положительным, большим,
//     * чем span.start. Если же span.end отрицательное, то участок распространяется
//     * до конца строки. Если span.start отрицательное число, то начало участка отсчитывается
//     * с конца абзаца, в этом случае span.end тоже должен быть отрицательным числом. Если же
//     * span.end положительное или равно 0, то участок распространяется до конца строки
     */
    override fun addSpan(span: Span): Span {
        this.spans.add(span)
        return span
    }

    override fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Span {
        var correctedEnd = end
        if (end < start) correctedEnd = this.textBuilder.length

        return addSpan(Span(start, correctedEnd, characterStyle, borderStyle))
    }

    override fun addSpan(regex: Regex, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): SpanList {
        @Suppress("NAME_SHADOWING")
        var count = count

        val spans = mutableListOf<Span>()

        var result = regex.find(this.textBuilder)
        while (result != null && count != 0) {
            spans.add(addSpan(result.range.start, result.range.start + result.value.length,
                    characterStyle, borderStyle))

            result = result.next()
            count--
        }

        return spans
    }

    override fun addWordSpan(first: Int, last: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Span {
        val parser = StringParser(this.textBuilder)
        var start = parser.end
        var end = parser.end

        if (parser.parseWord(first)) {
            start = parser.start
            end = parser.pos

            if (last > first) {
                parser.parseWord(last - first)
                end = parser.pos
            } else if (last < first) {
                end = parser.end
            }
        }

        return addSpan(start, end, characterStyle, borderStyle)
    }

    override fun removeSpan(span: Span) {
        this.spans.remove(span)
    }

    override fun findWord(number: Int, start: Int): Pair<Int, Int> {
        val parser = StringParser(this.textBuilder, start)
        parser.parseWord(number)
        return Pair(parser.start, parser.pos)
    }

    override fun findString(string: String, start: Int): Pair<Int, Int> {
        val pos = this.textBuilder.indexOf(string, start)
        return if (pos >= 0) {
            Pair(pos, pos + string.length)
        } else {
            Pair(this.textBuilder.length, this.textBuilder.length)
        }
    }

    override fun findLastString(string: String): Pair<Int, Int> {
        val pos = this.textBuilder.lastIndexOf(string)
        return if (pos >= 0) {
            Pair(pos, pos + string.length)
        } else {
            Pair(this.textBuilder.length, this.textBuilder.length)
        }
    }

    override fun find(regex: Regex, start: Int): Pair<Int, Int> {
        regex.find(this.textBuilder, start)?.also {
            return Pair(it.range.start, it.range.start + it.value.length)
        }

        return Pair(this.textBuilder.length, this.textBuilder.length)
    }
}
