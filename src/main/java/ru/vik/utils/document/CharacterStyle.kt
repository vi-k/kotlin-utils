package ru.vik.utils.document

class CharacterStyle(var font: String? = null,                // Название шрифта
                     var size: Size = Size.em(1f),       // Размер шрифта
                     var scaleX: Float = 1f,                  // Масштаб по-горизонтали
                     var bold: Boolean? = null,               // Полужирный
                     var italic: Boolean? = null,             // Курсив
                     var underline: Boolean? = null,          // Подчёркивание
                     var strike: Boolean? = null,             // Зачёркнутый текст
                     var color: Int? = null,                  // Цвет букв
                     var baselineShift: Size = Size.px(0f), // Смещение базовой линии
                     var letterSpacing: Float? = null,        // Расстояние между буквами
                     var allCaps: Caps? = null                // Все заглавные
) {
    enum class Caps {
        NONE, ALL_CAPS, SMALL_CAPS
    }

    fun attach(cs: CharacterStyle): CharacterStyle {
        cs.font?.also { this.font = it }

        // Смещение базовой линии рассчитываем до того, как изменим шрифт
        val size = cs.baselineShift.toPixels(this.size)
        when {
            size.isRelative()               -> this.baselineShift = cs.baselineShift
            this.baselineShift.isRelative() -> this.baselineShift = size
            else                            -> {
                this.baselineShift = Size(this.baselineShift.size + size.size, Size.Units.PX)
            }
        }

        this.size = cs.size.toPixels(this.size)
        this.scaleX *= cs.scaleX
        cs.bold?.also { this.bold = it }
        cs.italic?.also { this.italic = it }
        cs.underline?.also { this.underline = it }
        cs.strike?.also { this.strike = it }
        cs.color?.also { this.color = it }

        cs.letterSpacing?.also { this.letterSpacing = it }
        cs.allCaps?.also { this.allCaps = it }
        return this
    }

//    fun cloneAndAttach(cs: CharacterStyle) = CharacterStyle(
//            font = cs.font ?: this.font,
//            size = cs.size ?: this.size,
//            scale = this.scale * cs.scale,
//            scaleX = this.scaleX * cs.scaleX,
//            bold = cs.bold ?: this.bold,
//            italic = cs.italic ?: this.italic,
//            underline = cs.underline ?: this.underline,
//            strike = cs.strike ?: this.strike,
//            color = cs.color ?: this.color,
//            baselineShift = cs.baselineShift ?: this.baselineShift,
//            letterSpacing = cs.letterSpacing ?: this.letterSpacing,
//            allCaps = cs.allCaps ?: this.allCaps
//    )

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
                size = Size.px(16f),
                scaleX = 1f,
                bold = false,
                italic = false,
                underline = false,
                strike = false,
                color = 0x00ffffff.inv(),
                baselineShift = Size.px(0f),
                letterSpacing = 0f,
                allCaps = Caps.NONE
        )
    }
}
