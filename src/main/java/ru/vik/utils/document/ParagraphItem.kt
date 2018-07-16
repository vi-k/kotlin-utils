package ru.vik.utils.document

interface ParagraphItem {
    val borderStyle: BorderStyle
    val paragraphStyle: ParagraphStyle
    val characterStyle: CharacterStyle

    fun addSpan(span: Span): ParagraphItem

    fun addSpan(start: Int, end: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle = BorderStyle()
    ): ParagraphItem

    fun addWordSpan(numberOfWord: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle = BorderStyle()
    ): ParagraphItem

    fun addWordSpan(numberOfWord: Int, count: Int = 0, characterStyle: CharacterStyle,
        borderStyle: BorderStyle = BorderStyle()
    ): ParagraphItem

    fun findWord(numberOfWord: Int): Int

    // Интерфейс для BorderStyle
//    fun setMargin(margin: Size?): ParagraphItem
//    fun setMargin(topAndBottom: Size?, leftAndRight: Size?): ParagraphItem
//    fun setMargin(top: Size?, leftAndRight: Size?, bottom: Size?): ParagraphItem
//    fun setMargin(top: Size?, right: Size?, bottom: Size?, left: Size?): ParagraphItem
//
//    fun setPadding(padding: Size?): ParagraphItem
//    fun setPadding(topAndBottom: Size?, leftAndRight: Size?): ParagraphItem
//    fun setPadding(top: Size?, leftAndRight: Size?, bottom: Size?): ParagraphItem
//    fun setPadding(top: Size?, right: Size?, bottom: Size?, left: Size?): ParagraphItem
//
//    fun setBorder(border: Border?): ParagraphItem
//    fun setBorder(topAndBottom: Border?, leftAndRight: Border?): ParagraphItem
//    fun setBorder(top: Border?, leftAndRight: Border?, bottom: Border?): ParagraphItem
//    fun setBorder(top: Border?, right: Border?, bottom: Border?, left: Border?): ParagraphItem
//
//    fun setBackgroundColor(color: Int): ParagraphItem
}
