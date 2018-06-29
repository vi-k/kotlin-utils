package ru.vik.utils.document

class Paragraph(text: String? = null) : ParagraphItem {
    var parent: Section? = null
    val bs = BlockStyle()
    val ps = ParagraphStyle()
    val cs = CharacterStyle()

    val text = if (text != null) StringBuilder(text) else StringBuilder()
    val spans = mutableListOf<Span>()

    class Span(val bs: BlockStyle = BlockStyle(),
               val cs: CharacterStyle = CharacterStyle(),
               var start: Int = 0,
               var end: Int = -1
    )

//    fun setText(text: String) {
//        this.text.setLength(0)
//        this.text.append(text)
//        this.spans.clear()
//    }

    fun addSpan(span: Span) {
        this.spans.add(span)
    }
}
