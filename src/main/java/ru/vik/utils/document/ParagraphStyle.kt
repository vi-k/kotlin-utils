package ru.vik.utils.document

class ParagraphStyle(var align: Align? = null,
                     var leftIndent: Size? = null,
                     var rightIndent: Size? = null,
                     var firstAlign: Align? = null,
                     var firstLeftIndent: Size? = null,
                     var firstRightIndent: Size? = null,
                     var lastAlign: Align? = null,
                     var dropCapCharacters: Int? = null,
                     var dropCapLines: Int? = null,
                     var dropCapStyle: CharacterStyle? = null,
                     var tabStop: Int? = null,
                     var baselineTop: Float? = null,
                     var baselineGrid: Float? = null,
                     var baselineBottom: Float? = null,
                     var hyphens: Hyphens? = null

) {
    enum class Align {
        LEFT, CENTER, RIGHT, JUSTIFY
    }

    enum class Hyphens {
        NONE, MANUAL//, AUTO
    }

    fun attach(paragraphStyle: ParagraphStyle): ParagraphStyle {
        paragraphStyle.align?.also { this.align = it }
        paragraphStyle.leftIndent?.also { this.leftIndent = it }
        paragraphStyle.rightIndent?.also { this.rightIndent = it }
        paragraphStyle.firstAlign?.also { this.firstAlign = it }
        paragraphStyle.firstLeftIndent?.also { this.firstLeftIndent = it }
        paragraphStyle.firstRightIndent?.also { this.firstRightIndent = it }
        paragraphStyle.lastAlign?.also { this.lastAlign = it }
        paragraphStyle.dropCapCharacters?.also { this.dropCapCharacters = it }
        paragraphStyle.dropCapLines?.also { this.dropCapLines = it }
        paragraphStyle.dropCapStyle?.also { this.dropCapStyle = it }
        paragraphStyle.tabStop?.also { this.tabStop = it }
        paragraphStyle.baselineTop?.also { this.baselineTop = it }
        paragraphStyle.baselineGrid?.also { this.baselineGrid = it }
        paragraphStyle.baselineBottom?.also { this.baselineBottom = it }
        paragraphStyle.hyphens?.also { this.hyphens = it }
        return this
    }

    fun clone() = ParagraphStyle(
            align = this.align,
            leftIndent = this.leftIndent,
            rightIndent = this.rightIndent,
            firstAlign = this.firstAlign,
            firstLeftIndent = this.firstLeftIndent,
            firstRightIndent = this.firstRightIndent,
            lastAlign = this.lastAlign,
            dropCapCharacters = this.dropCapCharacters,
            dropCapLines = this.dropCapLines,
            dropCapStyle = this.dropCapStyle,
            tabStop = this.tabStop,
            baselineTop = this.baselineTop,
            baselineGrid = this.baselineGrid,
            baselineBottom = this.baselineBottom,
            hyphens = this.hyphens
    )

    companion object {
        fun default() = ParagraphStyle(
                align = Align.LEFT,
                leftIndent = Size.dp(0f),
                rightIndent = Size.dp(0f),
                firstAlign = null,
                firstLeftIndent = null,
                firstRightIndent = null,
                lastAlign = null,
                dropCapCharacters = 0,
                dropCapLines = 0,
                dropCapStyle = null,
                tabStop = null,
                baselineTop = null,
                baselineGrid = null,
                baselineBottom = null,
                hyphens = Hyphens.MANUAL
        )
    }
}
