package ru.vik.utils.document

import ru.vik.utils.color.Color

class CharacterStyle(
    var font: String? = null,
    var size: Size = Size.em(1f),
    var leading: AutoSize? = null,
    var scaleX: Float = 1f,
    var bold: Boolean? = null,
    var italic: Boolean? = null,
    var underline: Boolean? = null,
    var strike: Boolean? = null,
    var color: Int? = null,
    var baselineShift: Size = Size.dp(0f),
    var verticalAlign: VAlign? = null,
    var nullSizeEffect: Boolean? = null
//    var letterSpacing: Float? = null,
//    var allCaps: Caps? = null
) {

    enum class Caps {
        NONE, ALL_CAPS, SMALL_CAPS
    }

    enum class VAlign {
        BASELINE, TOP, BOTTOM
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
            verticalAlign = characterStyle?.verticalAlign,
            nullSizeEffect = characterStyle?.nullSizeEffect
//            letterSpacing = characterStyle?.letterSpacing,
//            allCaps = characterStyle?.allCaps
    )

    fun clone() = CharacterStyle(this)

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
     * @param density Плотность, требуется для расчёта базовой линии.
     * @return Функция не возвращает новый объект, а модифицирует текущий, и возвращает
     * ссылку на него.
     */
    fun attach(characterStyle: CharacterStyle, density: Float): CharacterStyle {
        characterStyle.font?.also { this.font = it }

        // Расчитываем смещение базовой линии

        // Смещение рассчитываем до того, как изменим шрифт! Т.е. размеры, указанные
        // в em и ratio вычисляем относительно родительского стиля
        val baselineShift = characterStyle.baselineShift.tryToAbsolute(this.size)

        // Если предыдущее значение не приведено к абсолютным величинам, игнорируем его,
        // т.к. вычислить его мы уже не сможем. Если размер шрифта не приведён к абсолютным
        // значениям, то не можем вычислить и передаваеме в characterStyle значение смещения,
        // тогда оставляем то, что есть, без изменений. Выбора у нас нет

        if (this.baselineShift.isRelative()) {
            this.baselineShift = baselineShift
        } else if (baselineShift.isAbsolute()) {
            if (baselineShift.units == Size.Units.PX || this.baselineShift.units == Size.Units.PX) {
                // Если хоть одно значение - предыдущее или заданное - указано в PX,
                // приводим оба к PX. Для этого и нужна плотность (density).
                val newSize = if (this.baselineShift.units == Size.Units.DP) {
                    this.baselineShift.size * density
                } else {
                    this.baselineShift.size
                } + if (baselineShift.units == Size.Units.DP) baselineShift.size * density else baselineShift.size

                this.baselineShift = Size.px(newSize)
            } else {
                // Если оба значения в DP, оставляем в DP
                this.baselineShift = Size.dp(this.baselineShift.size + baselineShift.size)
            }
        }

        this.size = characterStyle.size.tryToAbsolute(this.size)

        characterStyle.leading?.also { this.leading = it }
        this.scaleX *= characterStyle.scaleX
        characterStyle.bold?.also { this.bold = it }
        characterStyle.italic?.also { this.italic = it }
        characterStyle.underline?.also { this.underline = it }
        characterStyle.strike?.also { this.strike = it }
        characterStyle.color?.also { this.color = it }
        characterStyle.verticalAlign?.also { this.verticalAlign = it }
        characterStyle.nullSizeEffect?.also { this.nullSizeEffect = it }
//        characterStyle.letterSpacing?.also { this.letterSpacing = it }
//        characterStyle.allCaps?.also { this.allCaps = it }
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

    fun setLeading(leading: AutoSize?): CharacterStyle {
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

    fun setNullSizeEffect(nullSizeEffect: Boolean?): CharacterStyle {
        this.nullSizeEffect = nullSizeEffect
        return this
    }

//    fun setLetterSpacing(letterSpacing: Float): CharacterStyle {
//        this.letterSpacing = letterSpacing
//        return this
//    }

//    fun setAllCaps(allCaps: Caps): CharacterStyle {
//        this.allCaps = allCaps
//        return this
//    }

    companion object {
        fun default() = CharacterStyle(
                font = null,
                size = Size.dp(16f),
                leading = AutoSize(),
                scaleX = 1f,
                bold = false,
                italic = false,
                underline = false,
                strike = false,
                color = Color.BLACK,
                baselineShift = Size.dp(0f),
                verticalAlign = VAlign.BASELINE,
                nullSizeEffect = false
//                letterSpacing = 0f,
//                allCaps = Caps.NONE
        )
    }
}
