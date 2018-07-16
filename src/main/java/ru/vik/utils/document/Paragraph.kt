package ru.vik.utils.document

import ru.vik.utils.parser.StringParser
import ru.vik.utils.parser.parseWord

class Paragraph(text: String? = null) : BlockStyle(), ParagraphItem {
    var parent: Section? = null

    override val blockStyle get() = this as BlockStyle
    override val paragraphStyle = ParagraphStyle()
    override val characterStyle = CharacterStyle()

    val text = if (text != null) StringBuilder(text) else StringBuilder()
    val spans = mutableListOf<Span>()

//    fun setText(text: String) {
//        this.text.setLength(0)
//        this.text.append(text)
//        this.spans.clear()
//    }

    override fun addSpan(span: Span): ParagraphItem {
        this.spans.add(span)
        return this
    }

    override fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        blockStyle: BlockStyle
    ): ParagraphItem {

        addSpan(Span(start, end, characterStyle, blockStyle))
        return this
    }

    override fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        blockStyle: BlockStyle
    ): ParagraphItem {

        return addWordSpan(numberOfWord, 1, characterStyle, blockStyle)
    }

    override fun addWordSpan(numberOfWord: Int, count: Int, characterStyle: CharacterStyle,
        blockStyle: BlockStyle
    ): ParagraphItem {

        if (count != 0) {
            val parser = StringParser(this.text)
            if (parser.parseWord(numberOfWord)) {
                val start = parser.start
                var end = parser.pos

                if (count > 1) {
                    parser.parseWord(count - 1)
                    end = parser.pos
                } else if (count < 0) {
                    end = parser.end
                }

                addSpan(Span(start, end, characterStyle, blockStyle))
                }
        }

        return this
    }

    override fun findWord(numberOfWord: Int): Int {
        val parser = StringParser(this.text)
        if (parser.parseWord(numberOfWord)) {
            return parser.start
        }

        return -1
    }
}
