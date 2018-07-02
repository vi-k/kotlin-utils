package ru.vik.utils.document

class CharacterStyle(var font: String? = null,                // Название шрифта
                     var size: Size = Size.em(1f),       // Размер шрифта
                     var scaleX: Float = 1f,                  // Масштаб по-горизонтали
                     var bold: Boolean? = null,               // Полужирный
                     var italic: Boolean? = null,             // Курсив
                     var underline: Boolean? = null,          // Подчёркивание
                     var strike: Boolean? = null,             // Зачёркнутый текст
                     var color: Int? = null,                  // Цвет букв
                     var baselineShift: Size = Size.dp(0f), // Смещение базовой линии
                     var letterSpacing: Float? = null,        // Расстояние между буквами
                     var allCaps: Caps? = null                // Все заглавные
) {
    enum class Caps {
        NONE, ALL_CAPS, SMALL_CAPS
    }

    fun attach(characterStyle: CharacterStyle): CharacterStyle {
        characterStyle.font?.also { this.font = it }

        // Смещение базовой линии рассчитываем до того, как изменим шрифт
        val size = characterStyle.baselineShift.tryToDp(this.size)
        when {
            size.isRelative()               -> this.baselineShift = characterStyle.baselineShift
            this.baselineShift.isRelative() -> this.baselineShift = size
            else                            -> {
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

    fun clone() = CharacterStyle(
            font = this.font,
            size = this.size,
            scaleX = this.scaleX,
            bold = this.bold,
            italic = this.italic,
            underline = this.underline,
            strike = this.strike,
            color = this.color,
            baselineShift = this.baselineShift,
            letterSpacing = this.letterSpacing,
            allCaps = this.allCaps
    )

    companion object {
        fun default() = CharacterStyle(
                font = null,
                size = Size.dp(16f),
                scaleX = 1f,
                bold = false,
                italic = false,
                underline = false,
                strike = false,
                color = 0x00ffffff.inv(),
                baselineShift = Size.dp(0f),
                letterSpacing = 0f,
                allCaps = Caps.NONE
        )
    }
}
