package ru.vik.utils.document

import ru.vik.utils.color.Color

class CharacterStyle(
    var font: String? = null,
    var size: Size = Size.em(1f),
    var leading: Size? = null,
    var scaleX: Float = 1f,
    var bold: Boolean? = null,
    var italic: Boolean? = null,
    var underline: Boolean? = null,
    var strike: Boolean? = null,
    var color: Int? = null,
    var baselineShift: Size = Size.px(0f),
    var verticalAlign: VAlign? = null
//    var letterSpacing: Float? = null,
//    var allCaps: Caps? = null
) {
    var baselineShiftAdd: Size = Size.px(0f)

    enum class Caps {
        NONE, ALL_CAPS, SMALL_CAPS
    }

    enum class VAlign {
        BASELINE, TOP, BOTTOM, BASELINE_TO_BOTTOM
    }

    constructor(characterStyle: CharacterStyle?) : this(
            font = characterStyle?.font,
            size = characterStyle?.size ?: Size.em(1f),
            leading = characterStyle?.leading,
            scaleX = characterStyle?.scaleX ?: 1f,
            bold = characterStyle?.bold,
            italic = characterStyle?.italic,
            underline = characterStyle?.underline,
            strike = characterStyle?.strike,
            color = characterStyle?.color,
            baselineShift = characterStyle?.baselineShift ?: Size.dp(0f),
            verticalAlign = characterStyle?.verticalAlign
    )

    operator fun invoke(init: CharacterStyle.() -> Unit): CharacterStyle {
        this.init()
        return this
    }

    fun clone() = CharacterStyle(this)

    fun copyFrom(characterStyle: CharacterStyle): CharacterStyle {
        this.font = characterStyle.font
        this.size = characterStyle.size
        this.leading = characterStyle.leading
        this.scaleX = characterStyle.scaleX
        this.bold = characterStyle.bold
        this.italic = characterStyle.italic
        this.underline = characterStyle.underline
        this.strike = characterStyle.strike
        this.color = characterStyle.color
        this.baselineShift = characterStyle.baselineShift
        this.verticalAlign = characterStyle.verticalAlign
        return this
    }

    /**
     * Присоединение нового стиля.
     *
     * Функция служит для того, чтобы проходя по дереву стилей, накапливать изменения. Величины
     * со значением null будут наследовать родительское значение. Абсолютные значения -
     * перекрывать родительские. Относительные - складываться или умножаться.
     *
     * Важно, чтобы в начале дерева размер шрифта был указан в абсолютных единицах, иначе его
     * невозможно будет вычислить.
     *
     * @param characterStyle Присоединяемый стиль.
     * @param localMetrics Параметры, необходимые для рассчёта базовой линии и вертикального
     * выравнивания.
     * @return Функция не возвращает новый объект, а модифицирует текущий, и возвращает
     * ссылку на него.
     */
    fun attach(characterStyle: CharacterStyle, deviceMetrics: Size.DeviceMetrics,
        localMetrics: Size.LocalMetrics
    ): CharacterStyle {

        characterStyle.font?.also { this.font = it }

        // Если предыдущие значения размера шрифта и смещения базовой линии не были приведены
        // к абсолютным величинам, сделать ничего с ними не можем, т.к. неизвестны параметры
        // шрифта на предыдущем шаге.
        assert(this.size.isAbsolute()) {
            "CharacterStyle.attach(): `size` must be absolute"
        }
        assert(this.baselineShift.isAbsolute()) {
            "CharacterStyle.attach(): `baselineShift` must be absolute"
        }

        // Текущий размер шрифта и размер шрифта, переданный в localMetrics должны совпадать
        assert(this.size.toPixels(deviceMetrics, localMetrics) == localMetrics.fontSize) {
            "CharacterStyle.attach(): size != localMetrics.fontSize"
        }

        // Рассчитываем смещение базовой линии
        var baselineShift = this.baselineShift.toPixels(deviceMetrics, localMetrics,
                useParentSize = false) + characterStyle.baselineShift.toPixels(deviceMetrics,
                localMetrics, useParentSize = false)

        // На смещение влиет и вертикальное выравнивание
        this.baselineShiftAdd = Size.px(0f)
        when {
            characterStyle.verticalAlign == VAlign.TOP -> {
                baselineShift -= localMetrics.fontAscent
                this.baselineShiftAdd = Size.fa(1f)
            }
            characterStyle.verticalAlign == VAlign.BOTTOM -> {
                baselineShift += localMetrics.fontDescent
                this.baselineShiftAdd = Size.fd(-1f)
            }
            characterStyle.verticalAlign == VAlign.BASELINE_TO_BOTTOM -> baselineShift += localMetrics.fontDescent
        }

        this.baselineShift = Size.px(baselineShift)

        // Рассчитываем leading, если он указан в RATIO
        characterStyle.leading?.also {
            this.leading = if (it.units != Size.Units.RATIO) {
                it
            } else {
                Size.px((localMetrics.fontAscent + localMetrics.fontDescent) * it.size)
            }
        }

        this.size = Size.px(characterStyle.size.toPixels(deviceMetrics, localMetrics))

        this.scaleX *= characterStyle.scaleX
        characterStyle.bold?.also { this.bold = it }
        characterStyle.italic?.also { this.italic = it }
        characterStyle.underline?.also { this.underline = it }
        characterStyle.strike?.also { this.strike = it }
        characterStyle.color?.also { this.color = it }
        characterStyle.verticalAlign?.also { this.verticalAlign = it }
        return this
    }

    fun setFont(font: String?): CharacterStyle {
        this.font = font
        return this
    }

    fun setSize(size: Size): CharacterStyle {
        this.size = size
        return this
    }

    fun setLeading(leading: Size?): CharacterStyle {
        this.leading = leading
        return this
    }

    fun setScaleX(scaleX: Float): CharacterStyle {
        this.scaleX = scaleX
        return this
    }

    fun setBold(bold: Boolean?): CharacterStyle {
        this.bold = bold
        return this
    }

    fun setItalic(italic: Boolean?): CharacterStyle {
        this.italic = italic
        return this
    }

    fun setUnderline(underline: Boolean?): CharacterStyle {
        this.underline = underline
        return this
    }

    fun setStrike(strike: Boolean?): CharacterStyle {
        this.strike = strike
        return this
    }

    fun setColor(color: Int?): CharacterStyle {
        this.color = color
        return this
    }

    fun setBaselineShift(baselineShift: Size): CharacterStyle {
        this.baselineShift = baselineShift
        return this
    }

    fun setVerticalAlign(verticalAlign: VAlign?): CharacterStyle {
        this.verticalAlign = verticalAlign
        return this
    }

    companion object {
        fun default() = CharacterStyle(
                font = null,
                size = Size.sp(16f),
                leading = Size.auto(),
                scaleX = 1f,
                bold = false,
                italic = false,
                underline = false,
                strike = false,
                color = Color.BLACK,
                baselineShift = Size.dp(0f),
                verticalAlign = VAlign.BASELINE
        )
    }
}
