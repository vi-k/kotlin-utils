package ru.vik.utils.document

class Section : ParagraphItem {
    var parent: Section? = null
    val bs: BlockStyle = BlockStyle()
    val ps: ParagraphStyle = ParagraphStyle()
    val cs: CharacterStyle = CharacterStyle()

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
}
