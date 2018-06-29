package ru.vik.utils.document

class ParagraphStyle(var align: Align? = null,
                     var leftIndent: Float? = null,
                     var rightIndent: Float? = null,
                     var firstAlign: Align? = null,
                     var firstLeftIndent: Float? = null,
                     var firstRightIndent: Float? = null,
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

    fun attach(ps: ParagraphStyle): ParagraphStyle {
        ps.align?.also { this.align = it }
        ps.leftIndent?.also { this.leftIndent = it }
        ps.rightIndent?.also { this.rightIndent = it }
        ps.firstAlign?.also { this.firstAlign = it }
        ps.firstLeftIndent?.also { this.firstLeftIndent = it }
        ps.firstRightIndent?.also { this.firstRightIndent = it }
        ps.lastAlign?.also { this.lastAlign = it }
        ps.dropCapCharacters?.also { this.dropCapCharacters = it }
        ps.dropCapLines?.also { this.dropCapLines = it }
        ps.dropCapStyle?.also { this.dropCapStyle = it }
        ps.tabStop?.also { this.tabStop = it }
        ps.baselineTop?.also { this.baselineTop = it }
        ps.baselineGrid?.also { this.baselineGrid = it }
        ps.baselineBottom?.also { this.baselineBottom = it }
        ps.hyphens?.also { this.hyphens = it }
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
                leftIndent = 0.0f,
                rightIndent = 0.0f,
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
