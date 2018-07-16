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

    open fun setMargin(margin: Size?): BorderStyle {
        this.marginTop = margin
        this.marginRight = margin
        this.marginBottom = margin
        this.marginLeft = margin
        return this
    }

    open fun setMargin(topAndBottom: Size?, leftAndRight: Size?): BorderStyle {
        this.marginTop = topAndBottom
        this.marginRight = leftAndRight
        this.marginBottom = topAndBottom
        this.marginLeft = leftAndRight
        return this
    }

    open fun setMargin(top: Size?, leftAndRight: Size?, bottom: Size?): BorderStyle {
        this.marginTop = top
        this.marginRight = leftAndRight
        this.marginBottom = bottom
        this.marginLeft = leftAndRight
        return this
    }

    open fun setMargin(top: Size?, right: Size?, bottom: Size?, left: Size?): BorderStyle {
        this.marginTop = top
        this.marginRight = right
        this.marginBottom = bottom
        this.marginLeft = left
        return this
    }

    open fun setPadding(padding: Size?): BorderStyle {
        this.paddingTop = padding
        this.paddingRight = padding
        this.paddingBottom = padding
        this.paddingLeft = padding
        return this
    }

    open fun setPadding(topAndBottom: Size?, leftAndRight: Size?): BorderStyle {
        this.paddingTop = topAndBottom
        this.paddingRight = leftAndRight
        this.paddingBottom = topAndBottom
        this.paddingLeft = leftAndRight
        return this
    }

    open fun setPadding(top: Size?, leftAndRight: Size?, bottom: Size?): BorderStyle {
        this.paddingTop = top
        this.paddingRight = leftAndRight
        this.paddingBottom = bottom
        this.paddingLeft = leftAndRight
        return this
    }

    open fun setPadding(top: Size?, right: Size?, bottom: Size?, left: Size?): BorderStyle {
        this.paddingTop = top
        this.paddingRight = right
        this.paddingBottom = bottom
        this.paddingLeft = left
        return this
    }

    open fun setBorder(border: Border?): BorderStyle {
        this.borderTop = border
        this.borderRight = border
        this.borderBottom = border
        this.borderLeft = border
        return this
    }

    open fun setBorder(topAndBottom: Border?, leftAndRight: Border?): BorderStyle {
        this.borderTop = topAndBottom
        this.borderRight = leftAndRight
        this.borderBottom = topAndBottom
        this.borderLeft = leftAndRight
        return this
    }

    open fun setBorder(top: Border?, leftAndRight: Border?, bottom: Border?): BorderStyle {
        this.borderTop = top
        this.borderRight = leftAndRight
        this.borderBottom = bottom
        this.borderLeft = leftAndRight
        return this
    }

    open fun setBorder(top: Border?, right: Border?, bottom: Border?, left: Border?): BorderStyle {
        this.borderTop = top
        this.borderRight = right
        this.borderBottom = bottom
        this.borderLeft = left
        return this
    }

    fun needForDraw(): Boolean {
        return (this.backgroundColor != 0 ||
                this.borderTop?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderRight?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderBottom?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderLeft?.let { it.color != 0 && it.size != 0f } == true)
    }

    open fun setBackgroundColor(color: Int): BorderStyle {
        this.backgroundColor = color
        return this
    }

    companion object {
        fun default() = BorderStyle()
    }
}
