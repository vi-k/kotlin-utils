package ru.vik.utils.color

import ru.vik.utils.math.roundToInt

/**
 * Компоненты цвета через свойства.
 * color.a; color.r; color.g; color.b
 */
val Int.a get() = this ushr 24 and 0xff
val Int.r get() = this ushr 16 and 0xff
val Int.g get() = this ushr 8 and 0xff
val Int.b get() = this and 0xff

fun Int.a(value: Int) = (this and 0x00ffffff) or (value shl 24)
fun Int.r(value: Int) = (this and 0xff0000.inv()) or (value shl 16)
fun Int.g(value: Int) = (this and 0xff00.inv()) or (value shl 8)
fun Int.b(value: Int) = (this and 0xff.inv()) or value

/**
 * Все компоненты цвета через мультидекларацию:
 * val (a, r, g, b) = сolor.argb
 */
data class ARGB(val a: Int, val r: Int, val g: Int, val b: Int)

val Int.argb get() = ARGB(this.a, this.r, this.g, this.b)

/**
 * Компоненты r, g и b цвета через мультидекларацию:
 * val (r, g, b) = сolor.rgb
 */
data class RGB(val r: Int, val g: Int, val b: Int)

val Int.rgb get() = RGB(this.r, this.g, this.b)

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
fun Int.mix(weight: Float) = this.a((this.a * weight).roundToInt())

fun Int.mix(weight: Double) = this.a((this.a * weight).roundToInt())

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
    val r = (this.r * cWeight1 + color2.r * cWeight2).roundToInt()
    val g = (this.g * cWeight1 + color2.g * cWeight2).roundToInt()
    val b = (this.b * cWeight1 + color2.b * cWeight2).roundToInt()

    return Color.argb(a.roundToInt(), r, g, b)
}

class Color {
    companion object {
        /**
         * Функции создания цвета по компонентам.
         */
        fun argb(a: Int, r: Int, g: Int, b: Int): Int {
            return (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        fun rgb(r: Int, g: Int, b: Int): Int {
            return argb(255, r, g, b)
        }

        fun toString(color: Int): String {
            return String.format("argb(%d,%d,%d,%d)", color.a, color.r, color.g, color.b)
        }
    }
}
