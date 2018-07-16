package ru.vik.utils.document

import ru.vik.utils.parser.StringParser
import ru.vik.utils.parser.parseWord

class Paragraph(text: String? = null) : ParagraphItem {
    var parent: Section? = null

    override val borderStyle = BorderStyle()
    override val paragraphStyle = ParagraphStyle()
    override val characterStyle = CharacterStyle()

    val text = if (text != null) StringBuilder(text) else StringBuilder()
    val spans = mutableListOf<Span>()

//    fun setText(text: String) {
//        this.text.setLength(0)
//        this.text.append(text)
//        this.spans.clear()
//    }

    override fun addSpan(span: Span): Paragraph {
        this.spans.add(span)
        return this
    }

    override fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle
    ): Paragraph {
        addSpan(Span(start, end, characterStyle, borderStyle))
        return this
    }

    override fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle
    ): Paragraph {
        return addWordSpan(numberOfWord, 1, characterStyle, borderStyle)
    }

    override fun addWordSpan(numberOfWord: Int, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle
    ): Paragraph {
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

                addSpan(Span(start, end, characterStyle, borderStyle))
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

//    override fun setMargin(margin: Size?): Paragraph = super.setMargin(margin) as Paragraph
//    override fun setMargin(topAndBottom: Size?, leftAndRight: Size?): Paragraph = super.setMargin(topAndBottom, leftAndRight) as Paragraph
//    override fun setMargin(top: Size?, leftAndRight: Size?, bottom: Size?): Paragraph = super.setMargin(top, leftAndRight, bottom) as Paragraph
//    override fun setMargin(top: Size?, right: Size?, bottom: Size?, left: Size?): Paragraph = super.setMargin(top, right, bottom, left) as Paragraph
//
//    override fun setPadding(padding: Size?): Paragraph = super.setPadding(padding) as Paragraph
//    override fun setPadding(topAndBottom: Size?, leftAndRight: Size?): Paragraph = super.setPadding(topAndBottom, leftAndRight) as Paragraph
//    override fun setPadding(top: Size?, leftAndRight: Size?, bottom: Size?): Paragraph = super.setPadding(top, leftAndRight, bottom) as Paragraph
//    override fun setPadding(top: Size?, right: Size?, bottom: Size?, left: Size?): Paragraph = super.setPadding(top, right, bottom, left) as Paragraph
//
//    override fun setBorder(border: Border?): Paragraph = super.setBorder(border) as Paragraph
//    override fun setBorder(topAndBottom: Border?, leftAndRight: Border?): Paragraph = super.setBorder(topAndBottom, leftAndRight) as Paragraph
//    override fun setBorder(top: Border?, leftAndRight: Border?, bottom: Border?): Paragraph = super.setBorder(top, leftAndRight, bottom) as Paragraph
//    override fun setBorder(top: Border?, right: Border?, bottom: Border?, left: Border?): Paragraph = super.setBorder(top, right, bottom, left) as Paragraph
//
//    override fun setBackgroundColor(color: Int): Paragraph = super.setBackgroundColor(color) as Paragraph
}
