package ru.vik.utils.document

class CharacterStyle(var font: String? = null,         // Шрифт
                     var size: Float? = null,          // Размер шрифта
                     var scale: Float = 1f,            // Общий масштаб букв
                     var scaleX: Float = 1f,           // Масштаб по-горизонтали
                     var bold: Boolean? = null,        // Полужирный
                     var italic: Boolean? = null,      // Курсив
                     var underline: Boolean? = null,   // Подчёркивание
                     var strike: Boolean? = null,      // Зачёркнутый текст
                     var color: Int? = null,           // Цвет букв
                     var baselineShift: Size? = null,  // Смещение базовой линии
                     var letterSpacing: Float? = null, // Расстояние между буквами
                     var allCaps: Caps? = null         // Все заглавные
) {
    enum class Caps {
        NONE, ALL_CAPS, SMALL_CAPS
    }

    fun attach(cs: CharacterStyle): CharacterStyle {
        cs.font?.also { this.font = it }
        cs.size?.also { this.size = it }
        this.scale *= cs.scale
        this.scaleX *= cs.scaleX
        cs.bold?.also { this.bold = it }
        cs.italic?.also { this.italic = it }
        cs.underline?.also { this.underline = it }
        cs.strike?.also { this.strike = it }
        cs.color?.also { this.color = it }
        cs.baselineShift?.also { this.baselineShift = it }
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
            scale = this.scale,
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
                size = 12.0f,
                scale = 1.0f,
                scaleX = 1.0f,
                bold = false,
                italic = false,
                underline = false,
                strike = false,
                color = -16777216,
                baselineShift = Size(0f, Size.Units.PX),
                letterSpacing = 0.0f,
                allCaps = Caps.NONE
        )
    }
}
