package ru.vik.utils.document

import ru.vik.utils.color.Color

class CharacterStyle(
    var font: String? = null,
    var size: Size = Size.em(1f),
    var scaleX: Float = 1f,
    var bold: Boolean? = null,
    var italic: Boolean? = null,
    var underline: Boolean? = null,
    var strike: Boolean? = null,
    var color: Int? = null,
    var baselineShift: Size = Size.dp(0f),
    var verticalAlign: VAlign? = null
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
            scaleX = characterStyle?.scaleX ?: 1f,
            bold = characterStyle?.bold,
            italic = characterStyle?.italic,
            underline = characterStyle?.underline,
            strike = characterStyle?.strike,
            color = characterStyle?.color,
            baselineShift = characterStyle?.baselineShift ?: Size.dp(0f),
            verticalAlign = characterStyle?.verticalAlign
//            letterSpacing = characterStyle?.letterSpacing,
//            allCaps = characterStyle?.allCaps
    )

    fun clone() = CharacterStyle(this)

    fun attach(characterStyle: CharacterStyle, density: Float): CharacterStyle {
        characterStyle.font?.also { this.font = it }

        // Смещение базовой линии рассчитываем до того, как изменим шрифт
        val size = characterStyle.baselineShift.tryToAbsolute(this.size)

        if (this.baselineShift.isRelative()) {
            // Если предыдущее значение не приведено к абсолютным величинам, игнорируем его
            this.baselineShift = size
        } else if (size.isAbsolute()) {
            if (size.units == Size.Units.PX || this.baselineShift.units == Size.Units.PX) {
                // Если хоть одно значение - предыдущее или заданное - указано в PX,
                // приводим оба к PX
                val newSize = if (this.baselineShift.units == Size.Units.DP) {
                    this.baselineShift.size * density
                } else {
                    this.baselineShift.size
                } + if (size.units == Size.Units.DP) {
                    size.size * density
                } else {
                    size.size
                }

                this.baselineShift = Size.px(newSize)
            } else {
                // Если оба значения в DP, оставляем в DP
                this.baselineShift = Size.dp(this.baselineShift.size + size.size)
            }
        }

        this.size = characterStyle.size.tryToAbsolute(this.size)
        this.scaleX *= characterStyle.scaleX
        characterStyle.bold?.also { this.bold = it }
        characterStyle.italic?.also { this.italic = it }
        characterStyle.underline?.also { this.underline = it }
        characterStyle.strike?.also { this.strike = it }
        characterStyle.color?.also { this.color = it }
        characterStyle.verticalAlign?.also { this.verticalAlign = it }
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
                scaleX = 1f,
                bold = false,
                italic = false,
                underline = false,
                strike = false,
                color = Color.BLACK,
                baselineShift = Size.dp(0f),
                verticalAlign = VAlign.BASELINE
//                letterSpacing = 0f,
//                allCaps = Caps.NONE
        )
    }
}
