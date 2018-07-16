package ru.vik.utils.color

import ru.vik.utils.math.posRoundToInt
import java.util.logging.Logger
import kotlin.math.roundToInt

/**
 * Компоненты цвета через свойства.
 * backgroundColor.a; backgroundColor.r; backgroundColor.g; backgroundColor.b
 */
val Int.a get() = this ushr 24 and 0xff
val Int.r get() = this ushr 16 and 0xff
val Int.g get() = this ushr 8 and 0xff
val Int.b get() = this and 0xff

fun Int.setA(value: Int) = (this and 0x00ffffff) or (value shl 24)
fun Int.setA(value: Float) = (this and 0x00ffffff) or ((value * 255f).posRoundToInt() shl 24)
fun Int.setA(value: Double) = (this and 0x00ffffff) or ((value * 255.0).posRoundToInt() shl 24)
fun Int.setR(value: Int) = (this and 0xff0000.inv()) or (value shl 16)
fun Int.setG(value: Int) = (this and 0xff00.inv()) or (value shl 8)
fun Int.setB(value: Int) = (this and 0xff.inv()) or value

/**
 * Все компоненты цвета через мультидекларацию:
 * val (a, r, g, b) = сolor.argb
 */
data class Argb(val a: Int, val r: Int, val g: Int, val b: Int)

val Int.argb get() = Argb(this.a, this.r, this.g, this.b)

/**
 * Компоненты r, g и b цвета через мультидекларацию:
 * val (r, g, b) = сolor.rgb
 */
data class Rgb(val r: Int, val g: Int, val b: Int)

val Int.rgb get() = Rgb(this.r, this.g, this.b)

/**
 * Наложение цвета с учётом альфа-каналов цветов. При наложении результат, как и в природе,
 * всегда менее прозрачен, чем исходные цвета. Порядок цветов имеет значение!
 *
 * @param color Накладываемый цвет.
 * @result Цвет - результат наложения.
 */
fun Int.layer(color: Int): Int {
    // Попытка перевести с Double на Int оказалась неуспешной. На ПК даблы быстрее Int.
    // Думаю, что и на Андроиде будет тоже самое
    val a2 = color.a.toDouble()
    if (a2 == 0.0) return this

    val a1 = this.a.toDouble()

    // Результирующая прозрачность вычисляется по формуле:
    // na = na1 * na2, - где na - величина, обратная прозрачности (na = 1-a).
    // Следовательно:
    // a = 1 - (1 - a1) * (1 - a2) = a1 + a2 - a1 * a2.
    val a = a1 + a2 - (a1 * a2) / 255.0

    val alpha = a2 / a
    val rAlpha = 1.0 - alpha
    val r = (this.r * rAlpha + color.r * alpha).posRoundToInt()
    val g = (this.g * rAlpha + color.g * alpha).posRoundToInt()
    val b = (this.b * rAlpha + color.b * alpha).posRoundToInt()

    return Color.argb(a.posRoundToInt(), r, g, b)
}

fun Int.layer2(color: Int): Int {
    // Попытка перевести с Double на Int оказалась неуспешной. На ПК даблы быстрее Int.
    // Думаю, что и на Андроиде будет тоже самое
    val a2 = color.a.toDouble()
    if (a2 == 0.0) return this

    val a1 = this.a.toDouble()

    // Результирующая прозрачность вычисляется по формуле:
    // na = na1 * na2, - где na - величина, обратная прозрачности (na = 1-a).
    // Следовательно:
    // a = 1 - (1 - a1) * (1 - a2) = a1 + a2 - a1 * a2.
    val a = a1 + a2 - (a1 * a2) / 255.0

    val alpha = a2 / a
    val rAlpha = 1.0 - alpha
    val r = (this.r * rAlpha + color.r * alpha).roundToInt()
    val g = (this.g * rAlpha + color.g * alpha).roundToInt()
    val b = (this.b * rAlpha + color.b * alpha).roundToInt()

    return Color.argb(a.roundToInt(), r, g, b)
}

/**
 * Разбавление цвета прозрачным цветом. Изменения происходят только в канале a.
 *
 * @param weight Вес цвета в пределах 0..1 (0 - цвет становится полностью
 * прозрачным, 1 - цвет не меняется).
 * @return "Разбавленный" цвет.
 */
fun Int.mix(weight: Float) = this.setA((this.a * weight).posRoundToInt())

fun Int.mix(weight: Double) = this.setA((this.a * weight).posRoundToInt())

/**
 * Разбавление цвета другим цветом.
 *
 * @param weight Вес основного цвета в пределах 0..1. Второй цвет занимает всю оставшуюся часть.
 * @param color Второй цвет.
 * @return Новый цвет - результат смешивания.
 */
fun Int.mix(weight: Double, color: Int) = this.mix(weight, color, 1.0 - weight)

/**
 * Смешивание цветов.
 *
 * Эта функция именно смешивания, а не наложения цветов. Каждому цвету указывается вес
 * в пределах 0..1. Если сумма весов weight1 + weight2 не равна 1, то подразумевается,
 * что оставшееся место занимает прозрачный цвет. Прозрачный цвет воздействует
 * на результирующий канал a, но не влияет на каналы r, g, b. (Вода не делает цвет светлее,
 * она делает его прозрачнее.) Порядок цветов не имеет значения!
 *
 * @param weight1 Вес основного цвета (0..1).
 * @param color2 Второй цвет.
 * @param weight2 Вес второго цвета (0..1).
 *
 * @return Новый цвет - результат смешивания.
 */
fun Int.mix(weight1: Double, color2: Int, weight2: Double): Int {
    val aw1 = this.a * weight1
    val aw2 = color2.a * weight2
    val a = aw1 + aw2

    if (a == 0.0) return 0

    // Если при расчёте каналов r, g, b мы будем использовать указанные веса, когда они
    // в сумме не составляют 1, то на самом деле к цвету мы незаметно будем примешивать
    // чёрный цвет (r=0, g=0, b=0). Белый цвет совершенно безосновательно начнёт сереть.
    // (В Gimp'е почему-то именно так и происходит! Сделайте изображение из двух половин -
    // белой и прозрачной, - уменьшите его до 1 пикселя и посмотрите на результат.
    // Неожиданно!) Именно поэтому пропорционально увеличиваем веса (с учётом канала a!),
    // чтобы в сумме они составили 1.

    val cWeight1 = aw1 / a
    val cWeight2 = 1.0 - cWeight1 // тоже самое, что и aw2 / a
    val r = (this.r * cWeight1 + color2.r * cWeight2).posRoundToInt()
    val g = (this.g * cWeight1 + color2.g * cWeight2).posRoundToInt()
    val b = (this.b * cWeight1 + color2.b * cWeight2).posRoundToInt()

    return Color.argb(a.posRoundToInt(), r, g, b)
}

fun Int.mix2(weight: Double, color: Int) = this.mix2(weight, color, 1.0 - weight)

fun Int.mix2(weight1: Double, color2: Int, weight2: Double): Int {
    val aw1 = this.a * weight1
    val aw2 = color2.a * weight2
    val a = aw1 + aw2

    if (a == 0.0) return 0

    // Если при расчёте каналов r, g, b мы будем использовать указанные веса, когда они
    // в сумме не составляют 1, то на самом деле к цвету мы незаметно будем примешивать
    // чёрный цвет (r=0, g=0, b=0). Белый цвет совершенно безосновательно начнёт сереть.
    // (В Gimp'е почему-то именно так и происходит! Сделайте изображение из двух половин -
    // белой и прозрачной, - уменьшите его до 1 пикселя и посмотрите на результат.
    // Неожиданно!) Именно поэтому пропорционально увеличиваем веса (с учётом канала a!),
    // чтобы в сумме они составили 1.

    val cWeight1 = aw1 / a
    val cWeight2 = 1.0 - cWeight1 // тоже самое, что и aw2 / a
    val r = (this.r * cWeight1 + color2.r * cWeight2).roundToInt()
    val g = (this.g * cWeight1 + color2.g * cWeight2).roundToInt()
    val b = (this.b * cWeight1 + color2.b * cWeight2).roundToInt()

    return Color.argb(a.roundToInt(), r, g, b)
}

class Color {
    companion object {
        const val BLACK = 0x000000 or (0xff shl 24)
        const val DKGRAY = 0x444444 or (0xff shl 24)
        const val GRAY = 0x888888 or (0xff shl 24)
        const val LTGRAY = 0xcccccc or (0xff shl 24)
        const val WHITE = 0xffffff or (0xff shl 24)
        const val RED = 0xff0000 or (0xff shl 24)
        const val GREEN = 0x00ff00 or (0xff shl 24)
        const val BLUE = 0x0000ff or (0xff shl 24)
        const val YELLOW = 0xffff00 or (0xff shl 24)
        const val CYAN = 0x00ffff or (0xff shl 24)
        const val MAGENTA = 0xff00ff or (0xff shl 24)
        const val TRANSPARENT = 0

        /**
         * Функции создания цвета по компонентам.
         */
        fun argb(a: Int, r: Int, g: Int, b: Int): Int {
            return (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        fun argb(a: Float, r: Int, g: Int, b: Int): Int {
            return ((a * 255f).posRoundToInt() shl 24) or (r shl 16) or (g shl 8) or b
        }

        fun argb(a: Double, r: Int, g: Int, b: Int): Int {
            return ((a * 255.0).posRoundToInt() shl 24) or (r shl 16) or (g shl 8) or b
        }

        fun argb(a: Int, rgb: Int): Int {
            return (a shl 24) or rgb
        }

        fun argb(a: Float, rgb: Int): Int {
            return ((a * 255f).posRoundToInt() shl 24) or rgb
        }

        fun argb(a: Double, rgb: Int): Int {
            return ((a * 255.0).posRoundToInt() shl 24) or rgb
        }

        fun rgb(r: Int, g: Int, b: Int): Int {
            return argb(255, r, g, b)
        }

        fun rgb(rgb: Int): Int {
            return argb(255, rgb)
        }

        fun toString(color: Int): String {
            return String.format("argb(%d,%d,%d,%d)", color.a, color.r, color.g, color.b)
        }
    }
}

fun speedTest(maxTime: Int) {
    // Тест скорости
    val log = Logger.getAnonymousLogger()

    var count = 0L
    var t = System.currentTimeMillis()
    loop@ while (true)
        for (c1 in 0..255) {
            for (c2 in 0..255) {
                val rgb1 = (c1 shl 16) or (c1 shl 8) or c1
                val rgb2 = (c2 shl 16) or (c2 shl 8) or c2
                for (a1 in 1..255) {
                    for (a2 in 1..255) {
                        val argb1 = (a1 shl 24) or rgb1
                        val argb2 = (a2 shl 24) or rgb2

                        argb1.layer(argb2)
                        count++
                    }
                }

                if (System.currentTimeMillis() - t >= maxTime) break@loop
            }
        }
    log.warning(String.format("layer(): %.1fs count=%d (%.1fM)",
            (System.currentTimeMillis() - t) / 1000f, count, count / 1000000f))

    count = 0L
    t = System.currentTimeMillis()
    loop@ while (true)
        for (c1 in 0..255) {
            for (c2 in 0..255) {
                val rgb1 = (c1 shl 16) or (c1 shl 8) or c1
                val rgb2 = (c2 shl 16) or (c2 shl 8) or c2
                for (a1 in 1..255) {
                    for (a2 in 1..255) {
                        val argb1 = (a1 shl 24) or rgb1
                        val argb2 = (a2 shl 24) or rgb2

                        argb1.layer2(argb2)
                        count++
                    }
                }

                if (System.currentTimeMillis() - t >= maxTime) break@loop
            }
        }
    log.warning(String.format("layer2(): %.1fs count=%d (%.1fM)",
            (System.currentTimeMillis() - t) / 1000f, count, count / 1000000f))

    count = 0L
    t = System.currentTimeMillis()
    loop@ while (true)
        for (c1 in 0..255) {
            for (c2 in 0..255) {
                val rgb1 = (c1 shl 16) or (c1 shl 8) or c1
                val rgb2 = (c2 shl 16) or (c2 shl 8) or c2
                for (a1 in 1..255) {
                    for (a2 in 1..255) {
                        val argb1 = (a1 shl 24) or rgb1
                        val argb2 = (a2 shl 24) or rgb2

                        argb1.mix(0.75, argb2)
                        count++
                    }
                }

                if (System.currentTimeMillis() - t >= maxTime) break@loop
            }
        }
    log.warning(String.format("mix(): %.1fs count=%d (%.1fM)",
            (System.currentTimeMillis() - t) / 1000f, count, count / 1000000f))

    count = 0L
    t = System.currentTimeMillis()
    loop@ while (true)
        for (c1 in 0..255) {
            for (c2 in 0..255) {
                val rgb1 = (c1 shl 16) or (c1 shl 8) or c1
                val rgb2 = (c2 shl 16) or (c2 shl 8) or c2
                for (a1 in 1..255) {
                    for (a2 in 1..255) {
                        val argb1 = (a1 shl 24) or rgb1
                        val argb2 = (a2 shl 24) or rgb2

                        argb1.mix2(0.75, argb2)
                        count++
                    }
                }

                if (System.currentTimeMillis() - t >= maxTime) break@loop
            }
        }
    log.warning(String.format("mix2(): %.1fs count=%d (%.1fM)",
            (System.currentTimeMillis() - t) / 1000f, count, count / 1000000f))
}