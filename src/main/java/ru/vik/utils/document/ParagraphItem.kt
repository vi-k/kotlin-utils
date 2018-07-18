package ru.vik.utils.document

interface ParagraphItem {
    val borderStyle: BorderStyle
    val paragraphStyle: ParagraphStyle
    val characterStyle: CharacterStyle
    val text: CharSequence

    fun addSpan(span: Span): ParagraphItem

    fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle? = null
    ): ParagraphItem

    fun addSpan(regex: Regex, count: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle? = null
    ): ParagraphItem

    fun addSpan(regex: Regex, characterStyle: CharacterStyle,
        borderStyle: BorderStyle? = null
    ): ParagraphItem

    fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle? = null
    ): ParagraphItem

    fun addWordSpan(numberOfWord: Int, count: Int = 0, characterStyle: CharacterStyle,
        borderStyle: BorderStyle? = null
    ): ParagraphItem

    fun findWord(numberOfWord: Int): Int
}
