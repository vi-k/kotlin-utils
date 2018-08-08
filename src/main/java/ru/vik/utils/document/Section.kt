package ru.vik.utils.document

import ru.vik.utils.parser.StringParser

open class Section : ParagraphItem {
    var parent: Section? = null
    private val _items = mutableListOf<ParagraphItem>()
    val items: List<ParagraphItem> get() = this._items
    override var borderStyle = BorderStyle()
    override var paragraphStyle = ParagraphStyle()
    override var characterStyle = CharacterStyle()
    override var data: Any? = null
    var firstBaselineToTop: Boolean = false
    var drawEmptyParagraph = false
    var marginCollapsing = true
    var ignoreFirstMargin = false
    var ignoreLastMargin = false

    override var text: String
        get() {
            val out = StringBuilder()
            for (item in this._items) {
                if (out.isNotEmpty()) out.append('\n')
                out.append(item.text)
            }
            return out.toString()
        }
        set(value) {
            clear()

            val parser = StringParser(value)

            while (!parser.eof()) {
                parser.start()
                while (!parser.eof()) {
                    val char = parser.get()
                    if (char == '\r' || char == '\n' || char == '\u2029')
                        break

                    parser.next()
                }

                addParagraph(Paragraph(parser.getParsed()))

                if (!parser.eof()) {
                    val char = parser.get()
                    parser.next()
                    if (char == '\r' && !parser.eof() && parser.get() == '\n') parser.next()
                }
            }
        }

    val section get() = addSection()

    operator fun get(index: Int) = this._items[index]

    fun item(index: Int, init: (ParagraphItem.() -> Unit)? = null): ParagraphItem {
        val item = this._items[index]
        init?.invoke(item)
        return item
    }

    fun section(index: Int, init: (Section.() -> Unit)? = null): Section {
        var count = index

        for (item in this._items) {
            (item as? Section)?.also {
                if (count-- == 0) {
                    init?.invoke(it)
                    return it
                }
            }
        }

        throw IndexOutOfBoundsException()
    }

    fun paragraph(index: Int, init: (Paragraph.(Int) -> Unit)? = null): Paragraph {
        var count = index

        for (item in this._items) {
            (item as? Paragraph)?.also {
                if (count-- == 0) {
                    init?.invoke(it, index)
                    return it
                }
            }
        }

        throw IndexOutOfBoundsException()
    }

    fun paragraph(indexes: IntRange, init: (Paragraph.(Int) -> Unit)? = null) {
        for (index in indexes) paragraph(index, init)
    }

    fun paragraph(vararg indexes: Int, init: (Paragraph.(Int) -> Unit)? = null) {
        if (indexes.isEmpty()) {
            // Все элементы
            var index = 0
            for (item in this._items) {
                (item as? Paragraph)?.also {
                    init?.invoke(it, index++)
                }
            }
        } else {
            // Выборочные элементы
            for (index in indexes) paragraph(index, init)
        }
    }

    fun addSection(section: Section = Section()): Section {
        section.parent = this
        this._items.add(section)
        return section
    }

    fun addParagraph(paragraph: Paragraph = Paragraph()): Paragraph {
        paragraph.parent = this
        this._items.add(paragraph)
        return paragraph
    }

    fun clear() {
        this._items.clear()
    }

    override fun addSpan(span: Span) = this._items[0].addSpan(span)

    override fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ) = this._items[0].addSpan(start, end, characterStyle, borderStyle)

    override fun addSpan(regex: Regex, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ) = this._items[0].addSpan(regex, count, characterStyle, borderStyle)

    override fun addSpan(regex: Regex, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ) = this._items[0].addSpan(regex, characterStyle, borderStyle)

    override fun addWordSpan(number: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ) = this._items[0].addWordSpan(number, characterStyle, borderStyle)

    override fun addWordSpan(first: Int, last: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ) = this._items[0].addWordSpan(first, last, characterStyle, borderStyle)

    override fun removeSpan(span: Span) = this._items[0].removeSpan(span)

    override fun findWord(number: Int, start: Int) = this._items[0].findWord(number, start)
    override fun findString(string: String, start: Int) = this._items[0].findString(string, start)
    override fun findLastString(string: String) = this._items[0].findLastString(string)
    override fun find(regex: Regex, start: Int) = this._items[0].find(regex, start)
}
