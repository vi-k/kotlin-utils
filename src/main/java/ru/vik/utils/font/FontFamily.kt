package ru.vik.utils.font

import kotlin.math.abs

class FontFamily {
    private val fonts = mutableListOf<Font>()

    /**
     * Поиск шрифта. Если не найден, то null
     */
    operator fun get(weight: Int, isItalic: Boolean): Font? {
        for (font in this.fonts) {
            if (font.weight == weight && font.isItalic == isItalic) return font
        }

        return null
    }

    /**
     * Установка или добавление нового шрифта
     */
    operator fun set(weight: Int, isItalic: Boolean, font: Font) {
        remove(weight, isItalic)
        font.weight = weight
        font.isItalic = isItalic
        this.fonts.add(font)
    }

    /**
     * Установка или добавление нового шрифта
     */
    fun remove(weight: Int, isItalic: Boolean) {
        this[weight, isItalic]?.also { this.fonts.remove(it) }
    }

    /**
     * Поиск шрифта. Если не найден, то создаётся новый дефолтный
     */
    fun getOrCreate(weight: Int, isItalic: Boolean) =
            get(weight, isItalic) ?: Font().also {
                it.weight = weight
                it.isItalic = isItalic
                this.fonts.add(it)
            }

    /**
     * Поиск шрифта, максимально соответствующего искомому
     *
     * В первую очередь ищется шрифт, максимально близкий по weight. Но для не-italic шрифта,
     * никогда не выбираем italic-шрифт (исключение: если список состоит из единственного шрифта,
     * в этом случае вообще ничего не смотрится - берётся то, что есть).
     *
     * Из двух одинаково близких шрифтов выбирается тот, у кого тот же isItalic.
     */
    fun getNear(weight: Int, isItalic: Boolean): Font? {
        var isFirst = true

        val range = if (weight < 400) weight..400 else 400..weight

        var nearFont: Font? = null
        var nearDiff = 0
        var nearInRange = false

        for (font in this.fonts) {
            val diff = abs(font.weight - weight)

            val inRange = font.weight in range
            if (isFirst ||
                    (inRange && !nearInRange || diff < nearDiff ||
                            diff == nearDiff && font.isItalic == isItalic) &&
                    (isItalic || font.isItalic == isItalic) &&
                    (inRange || !nearInRange)) {
                nearFont = font
                nearDiff = diff
                nearInRange = inRange
                isFirst = false
//                println("${nearFont.weight}, isItalic=${nearFont.isItalic}, diff=$nearDiff, inRange=$nearInRange")
            }
        }

        return nearFont
    }

    fun clear() {
        this.fonts.clear()
    }
}
