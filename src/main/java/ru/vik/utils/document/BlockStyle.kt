package ru.vik.utils.document

open class BlockStyle(
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
    fun clone() = BlockStyle(
            marginTop = this.marginTop,
            marginRight = this.marginRight,
            marginBottom = this.marginBottom,
            marginLeft = this.marginLeft,
            borderTop = this.borderTop,
            borderRight = this.borderRight,
            borderBottom = this.borderBottom,
            borderLeft = this.borderLeft,
            paddingTop = this.paddingTop,
            paddingRight = this.paddingRight,
            paddingBottom = this.paddingBottom,
            paddingLeft = this.paddingLeft,
            backgroundColor = this.backgroundColor
    )

    fun setMargin(margin: Size?): BlockStyle {
        this.marginTop = margin
        this.marginRight = margin
        this.marginBottom = margin
        this.marginLeft = margin
        return this
    }

    fun setMargin(topAndBottom: Size?, leftAndRight: Size?): BlockStyle {
        this.marginTop = topAndBottom
        this.marginRight = leftAndRight
        this.marginBottom = topAndBottom
        this.marginLeft = leftAndRight
        return this
    }

    fun setMargin(top: Size?, leftAndRight: Size?, bottom: Size?): BlockStyle {
        this.marginTop = top
        this.marginRight = leftAndRight
        this.marginBottom = bottom
        this.marginLeft = leftAndRight
        return this
    }

    fun setMargin(top: Size?, right: Size?, bottom: Size?, left: Size?): BlockStyle {
        this.marginTop = top
        this.marginRight = right
        this.marginBottom = bottom
        this.marginLeft = left
        return this
    }

    fun setPadding(padding: Size?): BlockStyle {
        this.paddingTop = padding
        this.paddingRight = padding
        this.paddingBottom = padding
        this.paddingLeft = padding
        return this
    }

    fun setPadding(topAndBottom: Size?, leftAndRight: Size?): BlockStyle {
        this.paddingTop = topAndBottom
        this.paddingRight = leftAndRight
        this.paddingBottom = topAndBottom
        this.paddingLeft = leftAndRight
        return this
    }

    fun setPadding(top: Size?, leftAndRight: Size?, bottom: Size?): BlockStyle {
        this.paddingTop = top
        this.paddingRight = leftAndRight
        this.paddingBottom = bottom
        this.paddingLeft = leftAndRight
        return this
    }

    fun setPadding(top: Size?, right: Size?, bottom: Size?, left: Size?): BlockStyle {
        this.paddingTop = top
        this.paddingRight = right
        this.paddingBottom = bottom
        this.paddingLeft = left
        return this
    }

    fun setBorder(border: Border?): BlockStyle {
        this.borderTop = border
        this.borderRight = border
        this.borderBottom = border
        this.borderLeft = border
        return this
    }

    fun setBorder(topAndBottom: Border?, leftAndRight: Border?): BlockStyle {
        this.borderTop = topAndBottom
        this.borderRight = leftAndRight
        this.borderBottom = topAndBottom
        this.borderLeft = leftAndRight
        return this
    }

    fun setBorder(top: Border?, leftAndRight: Border?, bottom: Border?): BlockStyle {
        this.borderTop = top
        this.borderRight = leftAndRight
        this.borderBottom = bottom
        this.borderLeft = leftAndRight
        return this
    }

    fun setBorder(top: Border?, right: Border?, bottom: Border?, left: Border?): BlockStyle {
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

    fun setBackgroundColor(color: Int): BlockStyle {
        this.backgroundColor = color
        return this
    }

    companion object {
        fun default() = BlockStyle()
    }
}
