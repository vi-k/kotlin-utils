package ru.vik.utils.document

open class BlockStyle(var margin: Boundary<Float> = Boundary(0f, 0f, 0f, 0f),
                      var padding: Boundary<Float> = Boundary(0f, 0f, 0f, 0f),
                      var border: Boundary<Border?> = Boundary(null, null, null, null),
                      var color: Int = 0
) {
    fun clone() = BlockStyle(
            margin = this.margin.clone(),
            padding = this.padding.clone(),
            border = this.border.clone(),
            color = this.color
    )

    fun setMargin(margin: Float) {
        this.margin.top = margin
        this.margin.right = margin
        this.margin.bottom = margin
        this.margin.left = margin
    }

    fun setMargin(topAndBottom: Float, leftAndRight: Float) {
        this.margin.top = topAndBottom
        this.margin.right = leftAndRight
        this.margin.bottom = topAndBottom
        this.margin.left = leftAndRight
    }

    fun setMargin(top: Float, leftAndRight: Float, bottom: Float) {
        this.margin.top = top
        this.margin.right = leftAndRight
        this.margin.bottom = bottom
        this.margin.left = leftAndRight
    }

    fun setMargin(top: Float, right: Float, bottom: Float, left: Float) {
        this.margin.top = top
        this.margin.right = right
        this.margin.bottom = bottom
        this.margin.left = left
    }

    fun setPadding(padding: Float) {
        this.padding.top = padding
        this.padding.right = padding
        this.padding.bottom = padding
        this.padding.left = padding
    }

    fun setPadding(topAndBottom: Float, leftAndRight: Float) {
        this.padding.top = topAndBottom
        this.padding.right = leftAndRight
        this.padding.bottom = topAndBottom
        this.padding.left = leftAndRight
    }

    fun setPadding(top: Float, leftAndRight: Float, bottom: Float) {
        this.padding.top = top
        this.padding.right = leftAndRight
        this.padding.bottom = bottom
        this.padding.left = leftAndRight
    }

    fun setPadding(top: Float, right: Float, bottom: Float, left: Float) {
        this.padding.top = top
        this.padding.right = right
        this.padding.bottom = bottom
        this.padding.left = left
    }

    val borderTopWidth
        get() = border.top?.width ?: 0f

    val borderRightWidth
        get() = border.right?.width ?: 0f

    val borderBottomWidth
        get() = border.bottom?.width ?: 0f

    val borderLeftWidth
        get() = border.left?.width ?: 0f

    fun setBorder(border: Border) {
        this.border.top = border
        this.border.right = border
        this.border.bottom = border
        this.border.left = border
    }

    fun setBorder(topAndBottom: Border?, leftAndRight: Border?) {
        this.border.top = topAndBottom
        this.border.right = leftAndRight
        this.border.bottom = topAndBottom
        this.border.left = leftAndRight
    }

    fun setBorder(top: Border?, leftAndRight: Border?, bottom: Border?) {
        this.border.top = top
        this.border.right = leftAndRight
        this.border.bottom = bottom
        this.border.left = leftAndRight
    }

    fun setBorder(top: Border?, right: Border?, bottom: Border?, left: Border?) {
        this.border.top = top
        this.border.right = right
        this.border.bottom = bottom
        this.border.left = left
    }

    fun needForDraw(): Boolean {
        return (this.color != 0 ||
                this.border.top?.let { it.color != 0 && it.width > 0f} == true &&
                this.border.right?.let { it.color != 0 && it.width > 0f} == true &&
                this.border.bottom?.let { it.color != 0 && it.width > 0f} == true &&
                this.border.left?.let { it.color != 0 && it.width > 0f} == true)
    }

    companion object {
        fun default() = BlockStyle()
    }
}
