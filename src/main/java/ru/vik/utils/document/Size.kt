package ru.vik.utils.document

/**
 * Единицы измерения:
 * PX - пиксели, абсолютные единицы измерения.
 * EM, RATIO - относительные единицы измерения, EM - относительно текущего шрифта (0..1),
 * RATIO - относительно размера родителя (0..1). В шрифтах EM соответствует RATIO. Различия между
 * EM и RATIO только там, где изначально размер относительно шрифта не подразумевается, например,
 * в indent, margin, padding и border.
 */
data class Size(val size: Float, val units: Units) {
    enum class Units {
        PX, EM, RATIO
    }

    companion object {
        fun px(size: Float) = Size(size, Units.PX)
        fun em(size: Float) = Size(size, Units.EM)
        fun ratio(size: Float) = Size(size, Units.RATIO)
        fun percent(size: Float) = Size(size / 100f, Units.RATIO)
    }

    fun clone() = Size(
            size = this.size,
            units = this.units
    )

    fun isAbsolute() = this.units == Units.PX
    fun isRelative() = this.units != Units.PX

    /**
     * Возврат значения в пикселях или значения по-умолчанию.
     *
     * @param default Значение по-умолчанию.
     * @return Размер в пикселях или значение по-умолчанию.
     */
    fun getPixelsOrDefault(default: Float): Float {
        return if (this.units == Units.PX) this.size else default
    }

    fun getPixelsOrZero(): Float {
        return if (this.units == Units.PX) this.size else 0f
    }

    /**
     * Преобразование размера в пиксели.
     *
     * @param fontSize Размер текущего шрифта для вычисления единиц, заданных в EM.
     * @param parentSize Размер родителя для вычисления единиц, заданных в RATIO.
     * @return Вычисленный размер в пикселях.
     */
    fun toPixels(fontSize: Float, parentSize: Float): Float {
        return when (this.units) {
            Units.PX    -> this.size
            Units.EM    -> this.size * fontSize
            Units.RATIO -> this.size * parentSize
        }
    }

    /**
     * Преобразование размера в пиксели, если это возможно.
     *
     * @param fontSize Размер текущего шрифта для вычисления единиц, заданных в EM.
     * @param parentSize Размер родителя для вычисления единиц, заданных в RATIO.
     * @return 1) Размер в пикселях, если преобразование возможно; 2) this, если преобразование
     * невозможно (если и текущий, и родительский размер заданы в относительных единицах).
     */
    fun tryToPixels(fontSize: Size, parentSize: Float? = null): Size {
        if (this.units == Units.RATIO && parentSize != null)
            return px(this.size * parentSize)

        if (this.isAbsolute() || fontSize.isRelative()) return this

        return px(this.size * fontSize.size)
    }
}
