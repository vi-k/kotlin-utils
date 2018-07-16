package ru.vik.utils.document

/**
 * Единицы измерения:
 * DP - пиксели (device independent), абсолютные единицы измерения.
 * EM, RATIO - относительные единицы измерения, EM - относительно текущего шрифта (0..1),
 * RATIO - относительно размера родителя (0..1). В шрифтах EM соответствует RATIO. Различия между
 * EM и RATIO только там, где изначально размер относительно шрифта не подразумевается, например,
 * в indent, margin, padding и border.
 */
open class Size(val size: Float, val units: Units) {

    enum class Units {
        PX, DP, EM, RATIO
    }

    companion object {
        fun px(size: Float) = Size(size, Units.PX)
        fun dp(size: Float) = Size(size, Units.DP)
        fun em(size: Float) = Size(size, Units.EM)
        fun ratio(size: Float) = Size(size, Units.RATIO)
        fun percent(size: Float) = Size(size / 100f, Units.RATIO)

        fun toPixels(size: Size?, density: Float, fontSize: Float, parentSize: Float = 0f): Float {
            return size?.toPixels(density, fontSize, parentSize) ?: 0f
        }

        fun isEmpty(size: Size?) = size?.isEmpty() ?: true
        fun isNotEmpty(size: Size?) = size?.isNotEmpty() ?: false
    }

    fun isAbsolute() = this.units == Units.PX || this.units == Units.DP
    fun isRelative() = this.units != Units.PX && this.units != Units.DP
    fun isEmpty() = this.size == 0f
    fun isNotEmpty() = this.size != 0f

    /**
     * Возврат значения в пикселях или значения по-умолчанию.
     *
     * @param default Значение по-умолчанию.
     * @return Размер в пикселях или значение по-умолчанию.
     */
    fun getDpOrDefault(default: Float): Float {
        return if (this.units == Units.DP) this.size else default
    }

    fun getPixelsOrZero(): Float {
        return if (this.units == Units.DP) this.size else 0f
    }

    /**
     * Преобразование размера в абсолютные единицы (DP или PX), если это возможно.
     *
     * @param fontSize Размер текущего шрифта для вычисления единиц, заданных в EM.
     * @param parentSize Размер родителя для вычисления единиц, заданных в RATIO.
     * @return 1) Размер в пикселях, если преобразование возможно; 2) this, если преобразование
     * невозможно (если и текущий, и родительский размер заданы в относительных единицах).
     */
    fun tryToAbsolute(fontSize: Size, parentSize: Float? = null): Size {
        // RATIO, зависящая от parentSize
        if (this.units == Units.RATIO && parentSize != null) {
            return dp(this.size * parentSize)
        }

        if (this.isAbsolute() || fontSize.isRelative()) return this

        // EM и RATIO, зависящая от размера шрифта
        return dp(this.size * fontSize.size)
    }

    /**
     * Преобразование размера в конечные пиксели устройства (device dependent).
     *
     * @param density Плотность пикселей (коэффициент преобразования из dp в px).
     * @param fontSize Размер текущего шрифта для вычисления единиц, заданных в EM (размер шрифта
     * должен быть уже приведён к плотности устройства).
     * @param parentSize Размер родителя для вычисления единиц, заданных в RATIO (размер должен
     * быть уже приведён к плотности устройства). Если null, то размер вычисляется относительно
     * размера шрифта.
     * @return Вычисленный размер в пикселях.
     */
    fun toPixels(density: Float, fontSize: Float, parentSize: Float? = null): Float {
        return when (this.units) {
            Units.PX    -> this.size
            Units.DP    -> this.size * density
            Units.EM    -> this.size * fontSize
            Units.RATIO -> this.size * (parentSize ?: fontSize)
        }
    }
}
