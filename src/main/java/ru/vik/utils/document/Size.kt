package ru.vik.utils.document

/**
 * Единицы измерения:
 * Абсолютные единицы измерения (зависящие только от параметров устройства):
 * - PX - реальные пиксели устройства.
 * - DP - пиксели, не зависимые от устройства (device independent pixels).
 * - SP - для шрифтов - DP, умноженные на коэффициент, установленный пользователем.
 * - IN - дюймы (inches).
 * - PT - пункты (points).
 * - MM - миллиметры.
 * - CM - сантиметры.
 * Относительные единицы измерения (зависящие от параметров того места, где они применяются):
 * - EM - доли от кегля текущего шрифта.
 * - RATIO - доли от размера родителя (или ширина родителя, или размер шрифта). Для шрифтов
 * идентично EM.
 * - FH - доли от высоты шрифта (fontAscent + fontDescent).
 */
open class Size(val size: Float, val units: Units) {

    enum class Units {
        AUTO, PX, DP, SP, IN, PT, MM, CM,
        EM, RATIO, FA, FD, FH
    }

    class DeviceMetrics(
        var density: Float,
        var scaledDensity: Float,
        var xdpi: Float,
        var ydpi: Float
    )

    class LocalMetrics(
        var fontSize: Float = 0f,
        var fontAscent: Float = 0f,
        var fontDescent: Float = 0f,
        var parentSize: Float? = null
    ) {
        fun copy(localMetrics: LocalMetrics): LocalMetrics {
            this.fontSize = localMetrics.fontSize
            this.fontAscent = localMetrics.fontAscent
            this.fontDescent = localMetrics.fontDescent
            this.parentSize = localMetrics.parentSize
            return this
        }
    }

    companion object {
        fun auto() = Size(0f, Units.AUTO)
        fun px(size: Float) = Size(size, Units.PX)
        fun dp(size: Float) = Size(size, Units.DP)
        fun sp(size: Float) = Size(size, Units.SP)
        fun mm(size: Float) = Size(size, Units.MM)
        fun cm(size: Float) = Size(size, Units.CM)
        fun inch(size: Float) = Size(size, Units.IN)
        fun pt(size: Float) = Size(size, Units.PT)
        fun em(size: Float) = Size(size, Units.EM)
        fun ratio(size: Float) = Size(size, Units.RATIO)
        fun fa(size: Float) = Size(size, Units.FA)
        fun fd(size: Float) = Size(size, Units.FD)
        fun fh(size: Float) = Size(size, Units.FH)

        fun toPixels(size: Size?, deviceMetrics: DeviceMetrics, fontSize: Float, fontAscent: Float,
            fontDescent: Float, parentSize: Float? = null, isHorizontal: Boolean = false
        ) = size?.toPixels(
                deviceMetrics, fontSize, fontAscent, fontDescent, parentSize, isHorizontal) ?: 0f

        fun toPixels(size: Size?, deviceMetrics: DeviceMetrics, localMetrics: LocalMetrics,
            useParentSize: Boolean = true, horizontal: Boolean = false
        ) = size?.toPixels(deviceMetrics, localMetrics, useParentSize, horizontal) ?: 0f

        fun isZero(size: Size?) = size?.isZero() ?: true
        fun isNotZero(size: Size?) = size?.isNotZero() ?: false
    }

    fun isAbsolute() = this.units >= Units.AUTO && this.units <= Units.CM
    fun isRelative() = this.units >= Units.EM && this.units <= Units.FH
    fun isAuto() = this.units == Units.AUTO
    fun isNotAuto() = this.units != Units.AUTO
    fun isZero() = this.size == 0f
    fun isNotZero() = this.size != 0f

//    /**
//     * Преобразование размера в абсолютные единицы, если это возможно.
//     *
//     * @param fontSize Размер текущего шрифта для вычисления единиц, заданных в EM.
//     * @param parentSize Размер родителя для вычисления единиц, заданных в RATIO. Если null,
//     * то размер вычисляется относительно fontSize. Размер должен уже включать в себя density.
//     * @return Если преобразование невозможно (если и текущий размер, и fontSize заданы
//     * в относительных единицах), то возвращается this. Если преобразование не требуется,
//     * то также возвращается this. В любом другом случае возвращается значение в тех же единица,
//     * что и fontSize.
//     */
//    fun tryToAbsolute(fontSize: Size, parentSize: Float? = null): Size {
//        // Размер в RATIO, зависящий от parentSize
//        if (this.units == Units.RATIO && parentSize != null) {
//            return px(this.size * parentSize)
//        }
//
//        if (this.isAbsolute() || fontSize.isRelative()) return this
//
//        // Размер в EM и RATIO, зависящий от размера шрифта
//        return Size(this.size * fontSize.size, fontSize.units)
//    }

    /**
     * Преобразование размера в конечные пиксели устройства.
     *
     * @param deviceMetrics Параметры устройства.
     * @param fontSize Размер текущего шрифта для вычисления единиц, заданных в EM (размер шрифта
     * должен быть уже приведён к плотности устройства).
     * @param parentSize Размер родителя для вычисления единиц, заданных в RATIO (размер должен
     * быть уже приведён к плотности устройства). Если null, то размер вычисляется относительно
     * размера шрифта.
     * @param horizontal По-умолчанию, величины MM, CM, IN, PT рассчитываются относительно
     * плотности по вертикали (deviceMetrics.ypdi). Чтобы использовать deviceMetrics.xdpi
     * надо установить horizontal = true.
     * @return Вычисленный размер в пикселях.
     */
    fun toPixels(deviceMetrics: DeviceMetrics, fontSize: Float, fontAscent: Float,
        fontDescent: Float,
        parentSize: Float? = null, horizontal: Boolean = false
    ) = this.size * when (this.units) {
        Units.AUTO -> 0f
        Units.PX -> 1f
        Units.DP -> deviceMetrics.density
        Units.SP -> deviceMetrics.scaledDensity
        Units.IN -> if (horizontal) deviceMetrics.xdpi else deviceMetrics.ydpi
        Units.PT -> (if (horizontal) deviceMetrics.xdpi else deviceMetrics.ydpi) / 72f
        Units.MM -> (if (horizontal) deviceMetrics.xdpi else deviceMetrics.ydpi) / 25.4f
        Units.CM -> (if (horizontal) deviceMetrics.xdpi else deviceMetrics.ydpi) / 2.54f
        Units.EM -> fontSize
        Units.RATIO -> (parentSize ?: fontSize)
        Units.FA -> fontAscent
        Units.FD -> fontDescent
        Units.FH -> fontAscent + fontDescent
    }

    /**
     * Преобразование размера в конечные пиксели устройства.
     *
     * @param localMetrics Параметры перевода единиц.
     * @param useParentSize Возможно ли использовать размер родителя или вычислять относительно
     * размера шрифта.
     * @param horizontal По-умолчанию, величины MM, CM, IN, PT рассчитываются относительно
     * плотности по вертикали (deviceMetrics.ypdi). Чтобы использовать deviceMetrics.xdpi
     * надо установить horizontal = true.
     * @return Вычисленный размер в пикселях.
     */
    fun toPixels(deviceMetrics: DeviceMetrics, localMetrics: LocalMetrics,
        useParentSize: Boolean = true, horizontal: Boolean = false
    ) = this.size * when (this.units) {
        Units.AUTO -> 0f
        Units.PX -> 1f
        Units.DP -> deviceMetrics.density
        Units.SP -> deviceMetrics.scaledDensity
        Units.IN -> if (horizontal) deviceMetrics.xdpi else deviceMetrics.ydpi
        Units.PT -> (if (horizontal) deviceMetrics.xdpi else deviceMetrics.ydpi) / 72f
        Units.MM -> (if (horizontal) deviceMetrics.xdpi else deviceMetrics.ydpi) / 25.4f
        Units.CM -> (if (horizontal) deviceMetrics.xdpi else deviceMetrics.ydpi) / 2.54f
        Units.EM -> localMetrics.fontSize
        Units.RATIO -> localMetrics.parentSize?.takeIf { useParentSize } ?: localMetrics.fontSize
        Units.FA -> localMetrics.fontAscent
        Units.FD -> localMetrics.fontDescent
        Units.FH -> localMetrics.fontAscent + localMetrics.fontDescent
    }
}
