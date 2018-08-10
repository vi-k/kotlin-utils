package ru.vik.utils.font

class Font(
    var abstractTypeface: Any? = null,
    var hyphen: Char = '-',
    var scale: Float = 1.0f,
    var ascentRatio: Float? = null,
    var descentRatio: Float? = null
) {
    var weight = 400
    var isItalic = false

    constructor(font: Font,
        typeface: Any? = font.abstractTypeface,
        hyphen: Char = font.hyphen,
        scale: Float = font.scale,
        ascentRatio: Float? = font.ascentRatio,
        descentRatio: Float? = font.descentRatio
    ) : this(typeface, hyphen, scale, ascentRatio, descentRatio)

    operator fun invoke(init: Font.() -> Unit): Font {
        this.init()
        return this
    }

    companion object {
        const val THIN = 100
        const val EXTRA_LIGHT = 200
        const val LIGHT = 300
        const val NORMAL = 400
        const val MEDIUM = 500
        const val SEMI_BOLD = 600
        const val BOLD = 700
        const val EXTRA_BOLD = 800
        const val BLACK = 900
    }
}
