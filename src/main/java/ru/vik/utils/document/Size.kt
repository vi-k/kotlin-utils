package ru.vik.utils.document

data class Size(val size: Float, val units: Units) {
    enum class Units {
        PX, EM, PERCENT
    }

    fun clone() = Size(
            size = this.size,
            units = this.units
    )

    fun toPx(fontSize: Float, parentSize: Float = fontSize): Float {
        return when (this.units) {
            Units.PX      -> this.size
            Units.EM      -> this.size * fontSize
            Units.PERCENT -> this.size * parentSize
        }
    }
}
