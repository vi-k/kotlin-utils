package ru.vik.utils.document

open class Section : ParagraphItem {
    var parent: Section? = null
    val blockStyle = BlockStyle()
    val paragraphStyle = ParagraphStyle()
    val characterStyle = CharacterStyle()

    val paragraphs = mutableListOf<ParagraphItem>()

    operator fun get(index: Int) : ParagraphItem {
        return paragraphs[index]
    }

    fun addSection(section: Section) {
        section.parent = this
        this.paragraphs.add(section)
    }

    fun addParagraph(paragraph: Paragraph) {
        paragraph.parent = this
        this.paragraphs.add(paragraph)
    }

    fun clear() {
        this.paragraphs.clear()
    }
}
