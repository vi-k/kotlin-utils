package ru.vik.utils.document

interface ParagraphItem {
    val blockStyle: BlockStyle
    val paragraphStyle: ParagraphStyle
    val characterStyle: CharacterStyle

    fun addSpan(span: Span): ParagraphItem

    fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        blockStyle: BlockStyle = BlockStyle()
    ): ParagraphItem

    fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        blockStyle: BlockStyle = BlockStyle()
    ): ParagraphItem

    fun addWordSpan(numberOfWord: Int, count: Int = 0, characterStyle: CharacterStyle,
        blockStyle: BlockStyle = BlockStyle()
    ): ParagraphItem

    fun findWord(numberOfWord: Int): Int
}
