package ru.vik.utils.font

class FontList(init: (FontList.() -> Unit)? = null) {
    class FontContext(
        val fontSet: FontFamily,
        var weight: Int = 400,
        var isItalic: Boolean = false
    )

    private val families = hashMapOf<String, FontFamily>()

    init {
        init?.invoke(this)
    }

    operator fun invoke(init: FontList.() -> Unit): FontList {
        this.init()
        return this
    }

    operator fun get(name: String) = this.families[name]

    operator fun set(name: String, family: FontFamily) {
        this.families[name] = family
    }

    operator fun get(name: String, weight: Int, italic: Boolean) = get(name)?.get(weight, italic)

    operator fun set(name: String, weight: Int, italic: Boolean, font: Font) {
        getOrCreate(name)[weight, italic] = font
    }

    fun getOrCreate(name: String): FontFamily =
            get(name) ?: FontFamily().also { this[name] = it }

    fun getOrCreate(name: String, weight: Int, italic: Boolean) =
            getOrCreate(name).getOrCreate(weight, italic)

    fun getNear(name: String, weight: Int, italic: Boolean) =
            get(name)?.getNear(weight, italic)

    /*
     * DSL
     */

    fun family(name: String) = getOrCreate(name)

    fun font(name: String, weight: Int? = null, isBold: Boolean = false, isItalic: Boolean = false,
        init: Font.() -> Unit
    ) = getOrCreate(name, weight ?: if (isBold) 700 else 400, isItalic).also {
        it.init()
    }

    fun font(name: String, isBold: Boolean = false, weight: Int? = null, isItalic: Boolean = false
    ) = FontContext(getOrCreate(name), weight ?: if (isBold) 700 else 400, isItalic)

    infix fun FontContext.to(font: Font): Font {
        this.fontSet[this.weight, this.isItalic] = font
        return font
    }
}
