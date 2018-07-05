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
        var color: Int = 0) {

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
            color = this.color
    )

    fun setMargin(margin: Size?) {
        this.marginTop = margin
        this.marginRight = margin
        this.marginBottom = margin
        this.marginLeft = margin
    }

    fun setMargin(topAndBottom: Size?, leftAndRight: Size?) {
        this.marginTop = topAndBottom
        this.marginRight = leftAndRight
        this.marginBottom = topAndBottom
        this.marginLeft = leftAndRight
    }

    fun setMargin(top: Size?, leftAndRight: Size?, bottom: Size?) {
        this.marginTop = top
        this.marginRight = leftAndRight
        this.marginBottom = bottom
        this.marginLeft = leftAndRight
    }

    fun setMargin(top: Size?, right: Size?, bottom: Size?, left: Size?) {
        this.marginTop = top
        this.marginRight = right
        this.marginBottom = bottom
        this.marginLeft = left
    }

    fun setPadding(padding: Size?) {
        this.paddingTop = padding
        this.paddingRight = padding
        this.paddingBottom = padding
        this.paddingLeft = padding
    }

    fun setPadding(topAndBottom: Size?, leftAndRight: Size?) {
        this.paddingTop = topAndBottom
        this.paddingRight = leftAndRight
        this.paddingBottom = topAndBottom
        this.paddingLeft = leftAndRight
    }

    fun setPadding(top: Size?, leftAndRight: Size?, bottom: Size?) {
        this.paddingTop = top
        this.paddingRight = leftAndRight
        this.paddingBottom = bottom
        this.paddingLeft = leftAndRight
    }

    fun setPadding(top: Size?, right: Size?, bottom: Size?, left: Size?) {
        this.paddingTop = top
        this.paddingRight = right
        this.paddingBottom = bottom
        this.paddingLeft = left
    }

    fun setBorder(border: Border?) {
        this.borderTop = border
        this.borderRight = border
        this.borderBottom = border
        this.borderLeft = border
    }

    fun setBorder(topAndBottom: Border?, leftAndRight: Border?) {
        this.borderTop = topAndBottom
        this.borderRight = leftAndRight
        this.borderBottom = topAndBottom
        this.borderLeft = leftAndRight
    }

    fun setBorder(top: Border?, leftAndRight: Border?, bottom: Border?) {
        this.borderTop = top
        this.borderRight = leftAndRight
        this.borderBottom = bottom
        this.borderLeft = leftAndRight
    }

    fun setBorder(top: Border?, right: Border?, bottom: Border?, left: Border?) {
        this.borderTop = top
        this.borderRight = right
        this.borderBottom = bottom
        this.borderLeft = left
    }

    fun needForDraw(): Boolean {
        return (this.color != 0 ||
                this.borderTop?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderRight?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderBottom?.let { it.color != 0 && it.size != 0f } == true &&
                this.borderLeft?.let { it.color != 0 && it.size != 0f } == true)
    }

    companion object {
        fun default() = BlockStyle()
    }
}
