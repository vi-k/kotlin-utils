package ru.vik.utils.document

import ru.vik.utils.parser.StringParser
import ru.vik.utils.parser.parseWord

class Paragraph(text: String? = null) : ParagraphItem {
    var parent: Section? = null

    override val borderStyle = BorderStyle()
    override val paragraphStyle = ParagraphStyle()
    override val characterStyle = CharacterStyle()

    val cacheParagraphStyle = ParagraphStyle()
    val cacheCharacterStyle = CharacterStyle()
    val cacheLocalMetrics = Size.LocalMetrics()
    val cacheSegmentLocalMetrics = Size.LocalMetrics()

    internal val textBuilder = if (text != null) StringBuilder(text) else StringBuilder()
    override val text: CharSequence get() = this.textBuilder

    val spans = mutableListOf<Span>()

//    fun setText(textBuilder: String) {
//        this.textBuilder.setLength(0)
//        this.textBuilder.append(textBuilder)
//        this.spans.clear()
//    }

    /**
     * Добавление участка форматирования
     *
     * Если span.start положительное число, то span.end тоже должен быть положительным, большим,
     * чем span.start. Если же span.end отрицательное, то участок распространяется
     * до конца строки. Если span.start отрицательное число, то начало участка отсчитывается
     * с конца абзаца, в этом случае span.end тоже должен быть отрицательным числом. Если же
     * span.end положительное или равно 0, то участок распространяется до конца строки
     */
    override fun addSpan(span: Span): Paragraph {
        val length = this.textBuilder.length

        if (span.start >= 0 && span.end < 0 || span.start < 0 && span.end >= 0) {
            span.end = length
        }

        if (span.start < 0) {
            span.start = length + span.start
            if (span.end < 0) {
                span.end = length + span.end
            }
        }

        this.spans.add(span)
        return this
    }

    override fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Paragraph {
        addSpan(Span(start, end, characterStyle, borderStyle))
        return this
    }

    @Suppress("NAME_SHADOWING")
    override fun addSpan(regex: Regex, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Paragraph {
        var count = count

        var result = regex.find(this.textBuilder)
        while (result != null && count != 0) {
            val span = Span(result.range.start, result.range.start + result.value.length,
                    characterStyle, borderStyle)
            addSpan(span)

            result = result.next()
            count--
        }

        return this
    }

    override fun addSpan(regex: Regex, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Paragraph {
        return addSpan(regex, -1, characterStyle, borderStyle)
    }

    override fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Paragraph {
        return addWordSpan(numberOfWord, 1, characterStyle, borderStyle)
    }

    override fun addWordSpan(numberOfWord: Int, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Paragraph {
        if (count != 0) {
            val parser = StringParser(this.textBuilder)
            if (parser.parseWord(numberOfWord)) {
                val start = parser.start
                var end = parser.pos

                if (count > 1) {
                    parser.parseWord(count - 1)
                    end = parser.pos
                } else if (count < 0) {
                    end = parser.end
                }

                addSpan(Span(start, end, characterStyle, borderStyle))
            }
        }

        return this
    }

    override fun findWord(numberOfWord: Int): Int {
        val parser = StringParser(this.textBuilder)
        if (parser.parseWord(numberOfWord)) {
            return parser.start
        }

        return -1
    }
}
