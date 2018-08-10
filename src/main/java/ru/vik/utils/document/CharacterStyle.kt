package ru.vik.utils.document

import ru.vik.utils.color.Color
import ru.vik.utils.font.Font

class CharacterStyle(
    var font: String? = null,
    var size: Size = Size.em(1f),
    var leading: Size? = null,
    var scaleX: Float = 1f,
    var weight: Int? = null,
    var style: Style? = null,
    var underline: Boolean? = null,
    var strike: Boolean? = null,
    var color: Int? = null,
    var baselineShift: Size = Size.px(0f),
    var verticalAlign: VAlign? = null
//    var letterSpacing: Float? = null,
//    var allCaps: Caps? = null
) {
    enum class Style {
        NORMAL, ITALIC, OBLIQUE
    }

//    enum class Caps {
//        NONE, ALL_CAPS, SMALL_CAPS
//    }

    enum class VAlign {
        BASELINE, TOP, BOTTOM, BASELINE_TO_BOTTOM
    }

    var baselineShiftAdd: Size = Size.px(0f)

    constructor(characterStyle: CharacterStyle?) : this(
            font = characterStyle?.font,
            size = characterStyle?.size ?: Size.em(1f),
            leading = characterStyle?.leading,
            scaleX = characterStyle?.scaleX ?: 1f,
            weight = characterStyle?.weight,
            style = characterStyle?.style,
            underline = characterStyle?.underline,
            strike = characterStyle?.strike,
            color = characterStyle?.color,
            baselineShift = characterStyle?.baselineShift ?: Size.dp(0f),
            verticalAlign = characterStyle?.verticalAlign
    )

    var italic: Boolean
        get() = style == Style.ITALIC
        set(value) {
            style = if (value) Style.ITALIC else Style.NORMAL
        }

    var oblique: Boolean
        get() = style == Style.OBLIQUE
        set(value) {
            style = if (value) Style.OBLIQUE else Style.NORMAL
        }

    var bold: Boolean
        get() = this.weight?.let { it >= Font.BOLD } ?: false
        set(value) {
            weight = if (value) Font.BOLD else Font.NORMAL
        }


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
        this.weight = characterStyle.weight
        this.style = characterStyle.style
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
        characterStyle.weight?.also { this.weight = it }
        characterStyle.style?.also { this.style = it }
        characterStyle.underline?.also { this.underline = it }
        characterStyle.strike?.also { this.strike = it }
        characterStyle.color?.also { this.color = it }
        characterStyle.verticalAlign?.also { this.verticalAlign = it }
        return this
    }

    companion object {
        fun default() = CharacterStyle(
                font = null,
                size = Size.sp(16f),
                leading = Size.auto(),
                scaleX = 1f,
                weight = Font.NORMAL,
                style = Style.NORMAL,
                underline = false,
                strike = false,
                color = Color.BLACK,
                baselineShift = Size.dp(0f),
                verticalAlign = VAlign.BASELINE
        )
    }
}
