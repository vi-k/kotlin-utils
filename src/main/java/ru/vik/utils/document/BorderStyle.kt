package ru.vik.utils.document

open class BorderStyle(
    var marginTop: Size? = null,
    var marginRight: Size? = null,
    var marginBottom: Size? = null,
    var marginLeft: Size? = null,
    var borderTop: Border? = null,
    var borderRight: Border? = null,
    var borderBottom: Border? = null,
    var borderLeft: Border? = null,
    var paddingTop: Size? = null,
    var paddingRight: Size? = null,
    var paddingBottom: Size? = null,
    var paddingLeft: Size? = null,
    var backgroundColor: Int = 0
) {
    constructor(borderStyle: BorderStyle?) : this(
            marginTop = borderStyle?.marginTop,
            marginRight = borderStyle?.marginRight,
            marginBottom = borderStyle?.marginBottom,
            marginLeft = borderStyle?.marginLeft,
            borderTop = borderStyle?.borderTop,
            borderRight = borderStyle?.borderRight,
            borderBottom = borderStyle?.borderBottom,
            borderLeft = borderStyle?.borderLeft,
            paddingTop = borderStyle?.paddingTop,
            paddingRight = borderStyle?.paddingRight,
            paddingBottom = borderStyle?.paddingBottom,
            paddingLeft = borderStyle?.paddingLeft,
            backgroundColor = borderStyle?.backgroundColor ?: 0)

    open fun clone() = BorderStyle(this)

    fun attach(borderStyle: BorderStyle): BorderStyle {
        borderStyle.marginTop?.also { this.marginTop = it }
        borderStyle.marginRight?.also { this.marginRight = it }
        borderStyle.marginBottom?.also { this.marginBottom = it }
        borderStyle.marginLeft?.also { this.marginLeft = it }
        borderStyle.borderTop?.also { this.borderTop = it }
        borderStyle.borderRight?.also { this.borderRight = it }
        borderStyle.borderBottom?.also { this.borderBottom = it }
        borderStyle.borderLeft?.also { this.borderLeft = it }
        borderStyle.paddingTop?.also { this.paddingTop = it }
        borderStyle.paddingRight?.also { this.paddingRight = it }
        borderStyle.paddingBottom?.also { this.paddingBottom = it }
        borderStyle.paddingLeft?.also { this.paddingLeft = it }
        this.backgroundColor = borderStyle.backgroundColor
        return this
    }

    fun needForDraw(): Boolean {
        return (this.backgroundColor != 0 ||
                this.borderTop?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderRight?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderBottom?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderLeft?.let { it.color != 0 && it.size != 0f } == true)
    }

    fun setMarginTop(marginTop: Size?): BorderStyle {
        this.marginTop = marginTop
        return this
    }

    fun setMarginRight(marginRight: Size?): BorderStyle {
        this.marginRight = marginRight
        return this
    }

    fun setMarginBottom(marginBottom: Size?): BorderStyle {
        this.marginBottom = marginBottom
        return this
    }

    fun setMarginLeft(marginLeft: Size?): BorderStyle {
        this.marginLeft = marginLeft
        return this
    }

    fun setMargin(margin: Size?): BorderStyle {
        this.marginTop = margin
        this.marginRight = margin
        this.marginBottom = margin
        this.marginLeft = margin
        return this
    }

    fun setMargin(topAndBottom: Size?, leftAndRight: Size?): BorderStyle {
        this.marginTop = topAndBottom
        this.marginRight = leftAndRight
        this.marginBottom = topAndBottom
        this.marginLeft = leftAndRight
        return this
    }

    fun setBorderTop(borderTop: Border?): BorderStyle {
        this.borderTop = borderTop
        return this
    }

    fun setBorderRight(borderRight: Border?): BorderStyle {
        this.borderRight = borderRight
        return this
    }

    fun setBorderBottom(borderBottom: Border?): BorderStyle {
        this.borderBottom = borderBottom
        return this
    }

    fun setBorderLeft(borderLeft: Border?): BorderStyle {
        this.borderLeft = borderLeft
        return this
    }

    fun setBorder(border: Border?): BorderStyle {
        this.borderTop = border
        this.borderRight = border
        this.borderBottom = border
        this.borderLeft = border
        return this
    }

    fun setBorder(topAndBottom: Border?, leftAndRight: Border?): BorderStyle {
        this.borderTop = topAndBottom
        this.borderRight = leftAndRight
        this.borderBottom = topAndBottom
        this.borderLeft = leftAndRight
        return this
    }

    fun setPaddingTop(paddingTop: Size?): BorderStyle {
        this.paddingTop = paddingTop
        return this
    }

    fun setPaddingRight(paddingRight: Size?): BorderStyle {
        this.paddingRight = paddingRight
        return this
    }

    fun setPaddingBottom(paddingBottom: Size?): BorderStyle {
        this.paddingBottom = paddingBottom
        return this
    }

    fun setPaddingLeft(paddingLeft: Size?): BorderStyle {
        this.paddingLeft = paddingLeft
        return this
    }

    fun setPadding(padding: Size?): BorderStyle {
        this.paddingTop = padding
        this.paddingRight = padding
        this.paddingBottom = padding
        this.paddingLeft = padding
        return this
    }

    fun setPadding(topAndBottom: Size?, leftAndRight: Size?): BorderStyle {
        this.paddingTop = topAndBottom
        this.paddingRight = leftAndRight
        this.paddingBottom = topAndBottom
        this.paddingLeft = leftAndRight
        return this
    }

    fun setBackgroundColor(color: Int): BorderStyle {
        this.backgroundColor = color
        return this
    }

    companion object {
        fun default() = BorderStyle()
    }
}
