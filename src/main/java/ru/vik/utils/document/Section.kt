package ru.vik.utils.document

open class Section : ParagraphItem {
    var parent: Section? = null

    final override var borderStyle = BorderStyle()
    final override var paragraphStyle = ParagraphStyle()
    final override var characterStyle = CharacterStyle()

    var setFirstBaselineToTop: Boolean = false

    val cacheParagraphStyle = ParagraphStyle()
    val cacheCharacterStyle = CharacterStyle()
    val cacheLocalMetrics = Size.LocalMetrics()

    override val text: CharSequence get() {
        val out = StringBuilder()
        for (item in this._paragraphs) {
            if (out.isNotEmpty()) out.append('\n')
            out.append(item.text)
        }
        return out
    }

    private val _paragraphs = mutableListOf<ParagraphItem>()
    val paragraphs: List<ParagraphItem> get() = this._paragraphs

    operator fun get(index: Int): ParagraphItem {
        return this._paragraphs[index]
    }

    fun addSection(section: Section) {
        section.parent = this
        this._paragraphs.add(section)
    }

    fun addParagraph(paragraph: Paragraph) {
        paragraph.parent = this
        this._paragraphs.add(paragraph)
    }

    fun getParagraph(index: Int): Paragraph? {
        return _paragraphs[index] as? Paragraph
    }

    fun getSection(index: Int): Section? {
        return _paragraphs[index] as? Section
    }

    fun clear() {
        this._paragraphs.clear()
    }

    override fun addSpan(span: Span): Section {
        this._paragraphs[0].addSpan(span)
        return this
    }

    override fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Section {
        this._paragraphs[0].addSpan(start, end, characterStyle, borderStyle)
        return this
    }

    override fun addSpan(regex: Regex, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Section {
        this._paragraphs[0].addSpan(regex, count, characterStyle, borderStyle)
        return this
    }

    override fun addSpan(regex: Regex, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Section {
        this._paragraphs[0].addSpan(regex, characterStyle, borderStyle)
        return this
    }

    override fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Section {
        this._paragraphs[0].addWordSpan(numberOfWord, characterStyle, borderStyle)
        return this
    }

    override fun addWordSpan(numberOfWord: Int, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle?
    ): Section {
        this._paragraphs[0].addWordSpan(numberOfWord, count, characterStyle, borderStyle)
        return this
    }

    override fun findWord(numberOfWord: Int): Int {
        return this._paragraphs[0].findWord(numberOfWord)
    }
}
