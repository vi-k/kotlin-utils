package ru.vik.utils.document

/**
 * Единицы измерения:
 * Абсолютные единицы измерения (зависящие только от параметров устройства):
 * PX - реальные пиксели устройства.
 * DP - пиксели, не зависимые от устройства (device independent pixels).
 * SP - для шрифтов - DP, умноженные на коэффициент, установленный пользователем.
 * IN - дюймы (inches).
 * PT - пункты (points).
 * MM - миллиметры.
 * CM - сантиметры.
 * Относительные единицы измерения (зависящие от параметров того места, где они применяются):
 * EM - доли от кегля текущего шрифта.
 * RATIO - доли от размера родителя (или ширина родителя, или размер шрифта). Для шрифтов
 * идентично EM.
 * EH - доли от высоты шрифта (abs(ascent) + descent + leading).
 */
open class Size(val size: Float, val units: Units) {

    enum class Units {
        PX, DP, SP, IN, PT, MM, CM, EM, RATIO, EH
    }

    class DeviceMetrics(
        var density: Float,
        var scaledDensity: Float,
        var xdpi: Float,
        var ydpi: Float
    )

    class LocalMetrics(
        val deviceMetrics: DeviceMetrics,
        var fontSize: Float = 0f,
        var fontHeight: Float = 0f,
        var parentSize: Float? = null
    ) {
        val density get() = this.deviceMetrics.density
        val scaledDensity get() = this.deviceMetrics.scaledDensity
        val xdpi get() = this.deviceMetrics.xdpi
        val ydpi get() = this.deviceMetrics.ydpi
    }

    companion object {
        fun px(size: Float) = Size(size, Units.PX)
        fun dp(size: Float) = Size(size, Units.DP)
        fun sp(size: Float) = Size(size, Units.SP)
        fun mm(size: Float) = Size(size, Units.MM)
        fun cm(size: Float) = Size(size, Units.CM)
        fun inch(size: Float) = Size(size, Units.IN)
        fun pt(size: Float) = Size(size, Units.PT)
        fun em(size: Float) = Size(size, Units.EM)
        fun ratio(size: Float) = Size(size, Units.RATIO)
        fun eh(size: Float) = Size(size, Units.EH)

        fun toPixels(size: Size?, metrics: DeviceMetrics, fontSize: Float, fontHeight: Float,
            parentSize: Float? = null, isHorizontal: Boolean = false
        ) = size?.toPixels(metrics, fontSize, fontHeight, parentSize, isHorizontal) ?: 0f

        fun toPixels(size: Size?, metrics: LocalMetrics, useParentSize: Boolean = true,
            horizontal: Boolean = false
        ) = size?.toPixels(metrics, useParentSize, horizontal) ?: 0f

        fun isZero(size: Size?) = size?.isZero() ?: true
        fun isNotZero(size: Size?) = size?.isNotZero() ?: false
    }

    fun isAbsolute() = this.units >= Units.PX && this.units <= Units.CM
    fun isRelative() = this.units >= Units.EM && this.units <= Units.RATIO
    fun isZero() = this.size == 0f
    fun isNotZero() = this.size != 0f

    /**
     * Преобразование размера в абсолютные единицы, если это возможно.
     *
     * @param fontSize Размер текущего шрифта для вычисления единиц, заданных в EM.
     * @param parentSize Размер родителя для вычисления единиц, заданных в RATIO. Если null,
     * то размер вычисляется относительно fontSize. Размер должен уже включать в себя density.
     * @return Если преобразование невозможно (если и текущий размер, и fontSize заданы
     * в относительных единицах), то возвращается this. Если преобразование не требуется,
     * то также возвращается this. В любом другом случае возвращается значение в тех же единица,
     * что и fontSize.
     */
    fun tryToAbsolute(fontSize: Size, parentSize: Float? = null): Size {
        // Размер в RATIO, зависящий от parentSize
        if (this.units == Units.RATIO && parentSize != null) {
            return px(this.size * parentSize)
        }

        if (this.isAbsolute() || fontSize.isRelative()) return this

        // Размер в EM и RATIO, зависящий от размера шрифта
        return Size(this.size * fontSize.size, fontSize.units)
    }

    /**
     * Преобразование размера в конечные пиксели устройства.
     *
     * @param metrics Параметры устройства.
     * @param fontSize Размер текущего шрифта для вычисления единиц, заданных в EM (размер шрифта
     * должен быть уже приведён к плотности устройства).
     * @param parentSize Размер родителя для вычисления единиц, заданных в RATIO (размер должен
     * быть уже приведён к плотности устройства). Если null, то размер вычисляется относительно
     * размера шрифта.
     * @param horizontal По-умолчанию, величины MM, CM, IN, PT рассчитываются относительно
     * плотности по вертикали (metrics.ypdi). Чтобы использовать metrics.xdpi
     * надо установить horizontal = true.
     * @return Вычисленный размер в пикселях.
     */
    fun toPixels(metrics: DeviceMetrics, fontSize: Float, fontHeight: Float,
        parentSize: Float? = null, horizontal: Boolean = false
    ) = this.size * when (this.units) {
        Units.PX -> 1f
        Units.DP -> metrics.density
        Units.SP -> metrics.scaledDensity
        Units.IN -> if (horizontal) metrics.xdpi else metrics.ydpi
        Units.PT -> (if (horizontal) metrics.xdpi else metrics.ydpi) / 72f
        Units.MM -> (if (horizontal) metrics.xdpi else metrics.ydpi) / 25.4f
        Units.CM -> (if (horizontal) metrics.xdpi else metrics.ydpi) / 2.54f
        Units.EM -> fontSize
        Units.EH -> fontHeight
        Units.RATIO -> (parentSize ?: fontSize)
    }

    /**
     * Преобразование размера в конечные пиксели устройства.
     *
     * @param metrics Параметры перевода единиц.
     * @param useParentSize Возможно ли использовать размер родителя или вычислять относительно
     * размера шрифта.
     * @param horizontal По-умолчанию, величины MM, CM, IN, PT рассчитываются относительно
     * плотности по вертикали (deviceMetrics.ypdi). Чтобы использовать deviceMetrics.xdpi
     * надо установить horizontal = true.
     * @return Вычисленный размер в пикселях.
     */
    fun toPixels(metrics: LocalMetrics, useParentSize: Boolean = true, horizontal: Boolean = false
    ) = this.size * when (this.units) {
        Units.PX -> 1f
        Units.DP -> metrics.density
        Units.SP -> metrics.scaledDensity
        Units.IN -> if (horizontal) metrics.xdpi else metrics.ydpi
        Units.PT -> (if (horizontal) metrics.xdpi else metrics.ydpi) / 72f
        Units.MM -> (if (horizontal) metrics.xdpi else metrics.ydpi) / 25.4f
        Units.CM -> (if (horizontal) metrics.xdpi else metrics.ydpi) / 2.54f
        Units.EM -> metrics.fontSize
        Units.EH -> metrics.fontHeight
        Units.RATIO -> metrics.parentSize?.takeIf { useParentSize } ?: metrics.fontSize
    }
}
