package ru.vik.utils.document

open class Section : ParagraphItem {
    var parent: Section? = null

    override val borderStyle = BorderStyle()
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

    override fun addSpan(span: Span): Section {
        this._paragraphs[0].addSpan(span)
        return this
    }

    override fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle
    ): Section {
        this._paragraphs[0].addSpan(start, end, characterStyle, borderStyle)
        return this
    }

    override fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle
    ): Section {
        this._paragraphs[0].addWordSpan(numberOfWord, characterStyle, borderStyle)
        return this
    }

    override fun addWordSpan(numberOfWord: Int, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle
    ): Section {
        this._paragraphs[0].addWordSpan(numberOfWord, count, characterStyle, borderStyle)
        return this
    }

    override fun findWord(numberOfWord: Int): Int {
        return this._paragraphs[0].findWord(numberOfWord)
    }

//    override fun setMargin(margin: Size?): Section = super.setMargin(margin) as Section
//    override fun setMargin(topAndBottom: Size?, leftAndRight: Size?): Section = super.setMargin(topAndBottom, leftAndRight) as Section
//    override fun setMargin(top: Size?, leftAndRight: Size?, bottom: Size?): Section = super.setMargin(top, leftAndRight, bottom) as Section
//    override fun setMargin(top: Size?, right: Size?, bottom: Size?, left: Size?): Section = super.setMargin(top, right, bottom, left) as Section
//
//    override fun setPadding(padding: Size?): Section = super.setPadding(padding) as Section
//    override fun setPadding(topAndBottom: Size?, leftAndRight: Size?): Section = super.setPadding(topAndBottom, leftAndRight) as Section
//    override fun setPadding(top: Size?, leftAndRight: Size?, bottom: Size?): Section = super.setPadding(top, leftAndRight, bottom) as Section
//    override fun setPadding(top: Size?, right: Size?, bottom: Size?, left: Size?): Section = super.setPadding(top, right, bottom, left) as Section
//
//    override fun setBorder(border: Border?): Section = super.setBorder(border) as Section
//    override fun setBorder(topAndBottom: Border?, leftAndRight: Border?): Section = super.setBorder(topAndBottom, leftAndRight) as Section
//    override fun setBorder(top: Border?, leftAndRight: Border?, bottom: Border?): Section = super.setBorder(top, leftAndRight, bottom) as Section
//    override fun setBorder(top: Border?, right: Border?, bottom: Border?, left: Border?): Section = super.setBorder(top, right, bottom, left) as Section
//
//    override fun setBackgroundColor(color: Int): Section = super.setBackgroundColor(color) as Section
}
