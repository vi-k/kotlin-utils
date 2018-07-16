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
    var letterSpacing: Float? = null,
    var allCaps: Caps? = null
) {

    enum class Caps {
        NONE, ALL_CAPS, SMALL_CAPS
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
            letterSpacing = characterStyle?.letterSpacing,
            allCaps = characterStyle?.allCaps
    )

    fun clone() = CharacterStyle(this)

    fun attach(characterStyle: CharacterStyle): CharacterStyle {
        characterStyle.font?.also { this.font = it }

        // Смещение базовой линии рассчитываем до того, как изменим шрифт
        val size = characterStyle.baselineShift.tryToDp(this.size)
        when {
            size.isRelative() -> this.baselineShift = characterStyle.baselineShift
            this.baselineShift.isRelative() -> this.baselineShift = size
            else -> {
                this.baselineShift = Size(this.baselineShift.size + size.size, Size.Units.DP)
            }
        }

        this.size = characterStyle.size.tryToDp(this.size)
        this.scaleX *= characterStyle.scaleX
        characterStyle.bold?.also { this.bold = it }
        characterStyle.italic?.also { this.italic = it }
        characterStyle.underline?.also { this.underline = it }
        characterStyle.strike?.also { this.strike = it }
        characterStyle.color?.also { this.color = it }
        characterStyle.letterSpacing?.also { this.letterSpacing = it }
        characterStyle.allCaps?.also { this.allCaps = it }
        return this
    }

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
                letterSpacing = 0f,
                allCaps = Caps.NONE
        )
    }
}
