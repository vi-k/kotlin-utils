package ru.vik.utils.document

open class Section : BlockStyle(), ParagraphItem {
    var parent: Section? = null

    override val blockStyle get() = this as BlockStyle
    override val paragraphStyle = ParagraphStyle()
    override val characterStyle = CharacterStyle()

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

    override fun addSpan(span: Span): ParagraphItem {
        this._paragraphs[0].addSpan(span)
        return this
    }

    override fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        blockStyle: BlockStyle
    ): ParagraphItem {
        this._paragraphs[0].addSpan(start, end, characterStyle, blockStyle)
        return this
    }

    override fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        blockStyle: BlockStyle
    ): ParagraphItem {
        this._paragraphs[0].addWordSpan(numberOfWord, characterStyle, blockStyle)
        return this
    }

    override fun addWordSpan(numberOfWord: Int, count: Int, characterStyle: CharacterStyle,
        blockStyle: BlockStyle
    ): ParagraphItem {
        this._paragraphs[0].addWordSpan(numberOfWord, count, characterStyle, blockStyle)
        return this
    }

    override fun findWord(numberOfWord: Int): Int {
        return this._paragraphs[0].findWord(numberOfWord)
    }
}
