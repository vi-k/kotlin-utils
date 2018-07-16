package ru.vik.utils.document

class ParagraphStyle(
    var topIndent: Size? = null,
    var rightIndent: Size? = null,
    var bottomIndent: Size? = null,
    var leftIndent: Size? = null,
    var align: Align? = null,
    var firstAlign: Align? = null,
    var firstLeftIndent: Size? = null,
    var firstRightIndent: Size? = null,
    var lastAlign: Align? = null
//    var dropCapCharacters: Int? = null,
//    var dropCapLines: Int? = null,
//    var dropCapStyle: CharacterStyle? = null,
//    var tabStop: Int? = null,
//    var baselineTop: Float? = null,
//    var baselineGrid: Float? = null,
//    var baselineBottom: Float? = null,
//    var hyphens: Hyphens? = null
) {
    enum class Align {
        LEFT, CENTER, RIGHT, JUSTIFY
    }

    enum class Hyphens {
        NONE, MANUAL//, AUTO
    }

    constructor(paragraphStyle: ParagraphStyle) : this(
            topIndent = paragraphStyle.topIndent,
            rightIndent = paragraphStyle.rightIndent,
            bottomIndent = paragraphStyle.bottomIndent,
            leftIndent = paragraphStyle.leftIndent,
            align = paragraphStyle.align,
            firstAlign = paragraphStyle.firstAlign,
            firstLeftIndent = paragraphStyle.firstLeftIndent,
            firstRightIndent = paragraphStyle.firstRightIndent,
            lastAlign = paragraphStyle.lastAlign)
//            dropCapCharacters = paragraphStyle.dropCapCharacters,
//            dropCapLines = paragraphStyle.dropCapLines,
//            dropCapStyle = paragraphStyle.dropCapStyle,
//            tabStop = paragraphStyle.tabStop,
//            baselineTop = paragraphStyle.baselineTop,
//            baselineGrid = paragraphStyle.baselineGrid,
//            baselineBottom = paragraphStyle.baselineBottom,
//            hyphens = paragraphStyle.hyphens)

    fun clone() = ParagraphStyle(this)

    fun attach(paragraphStyle: ParagraphStyle): ParagraphStyle {
        paragraphStyle.align?.also { this.align = it }
        paragraphStyle.topIndent?.also { this.topIndent = it }
        paragraphStyle.rightIndent?.also { this.rightIndent = it }
        paragraphStyle.bottomIndent?.also { this.bottomIndent = it }
        paragraphStyle.leftIndent?.also { this.leftIndent = it }
        paragraphStyle.firstAlign?.also { this.firstAlign = it }
        paragraphStyle.firstLeftIndent?.also { this.firstLeftIndent = it }
        paragraphStyle.firstRightIndent?.also { this.firstRightIndent = it }
        paragraphStyle.lastAlign?.also { this.lastAlign = it }
//        paragraphStyle.dropCapCharacters?.also { this.dropCapCharacters = it }
//        paragraphStyle.dropCapLines?.also { this.dropCapLines = it }
//        paragraphStyle.dropCapStyle?.also { this.dropCapStyle = it }
//        paragraphStyle.tabStop?.also { this.tabStop = it }
//        paragraphStyle.baselineTop?.also { this.baselineTop = it }
//        paragraphStyle.baselineGrid?.also { this.baselineGrid = it }
//        paragraphStyle.baselineBottom?.also { this.baselineBottom = it }
//        paragraphStyle.hyphens?.also { this.hyphens = it }
        return this
    }

    fun setTopIndent(size: Size): ParagraphStyle {
        this.topIndent = size
        return this
    }

    fun setRightIndent(size: Size): ParagraphStyle {
        this.rightIndent = size
        return this
    }

    fun setBottomIndent(size: Size): ParagraphStyle {
        this.bottomIndent = size
        return this
    }

    fun setLeftIndent(size: Size): ParagraphStyle {
        this.leftIndent = size
        return this
    }

    fun setAlign(align: Align): ParagraphStyle {
        this.align = align
        return this
    }

    fun setFirstAlign(align: Align): ParagraphStyle {
        this.firstAlign = align
        return this
    }

    fun setFirstLeftIndent(size: Size): ParagraphStyle {
        this.firstLeftIndent = size
        return this
    }

    fun setFirstRightIndent(size: Size): ParagraphStyle {
        this.firstRightIndent = size
        return this
    }

    fun setLastAlign(align: Align): ParagraphStyle {
        this.lastAlign = align
        return this
    }

//    fun setDropCapCharacters(count: Int): ParagraphStyle {
//        this.dropCapCharacters = count
//        return this
//    }
//
//    fun setDropCapLines(count: Int): ParagraphStyle {
//        this.dropCapLines = count
//        return this
//    }
//
//    fun setDropCapStyle(dropCapStyle: CharacterStyle): ParagraphStyle {
//        this.dropCapStyle = dropCapStyle
//        return this
//    }
//
//    fun setTabStop(tabStop: Int): ParagraphStyle {
//        this.tabStop = tabStop
//        return this
//    }
//
//    fun setBaselineTop(size: Float): ParagraphStyle {
//        this.baselineTop = size
//        return this
//    }
//
//    fun setBaselineGrid(size: Float): ParagraphStyle {
//        this.baselineGrid = size
//        return this
//    }
//
//    fun setBaselineBottom(size: Float): ParagraphStyle {
//        this.baselineBottom = size
//        return this
//    }
//
//    fun setHyphens(hyphens: Hyphens): ParagraphStyle {
//        this.hyphens = hyphens
//        return this
//    }

    companion object {
        fun default() = ParagraphStyle(
                align = Align.LEFT,
                topIndent = Size.dp(0f),
                rightIndent = Size.dp(0f),
                bottomIndent = Size.dp(0f),
                leftIndent = Size.dp(0f),
                firstAlign = null,
                firstLeftIndent = null,
                firstRightIndent = null,
                lastAlign = null
        )
//                dropCapCharacters = 0,
//                dropCapLines = 0,
//                dropCapStyle = null,
//                tabStop = null,
//                baselineTop = null,
//                baselineGrid = null,
//                baselineBottom = null,
//                hyphens = Hyphens.MANUAL
//        )
    }
}
