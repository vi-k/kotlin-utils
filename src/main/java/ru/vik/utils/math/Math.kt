package ru.vik.utils.math

import java.util.logging.Logger
import kotlin.math.roundToInt

/**
 * Округление **положительного** значения до целого. Math.round() и Kotlin.posRoundToInt()
 * медленные (что на ПК, что на Анроиде), видимо, из-за необходимости поддерживать NaN, Inf
 * и отрицательные числа. Сугубо для положительных чисел есть вариант проще. Хотя, надо признать,
 * я не знаю как точно измерить скорость, чтобы не влияли внешние факторы системы.
 */
fun Float.posRoundToInt() = (this + 0.5f).toInt()
fun Double.posRoundToInt() = (this + 0.5).toInt()

/**
 * Округление и положительных, и отрицательных чисел. На Андроиде быстрее "конкурентов"
 */
fun Float.simpleRoundToInt() = (this + if (this < 0f) -0.5f else 0.5f).toInt()
fun Double.simpleRoundToInt() = (this + if (this < 0.0) -0.5 else 0.5).toInt()

fun speedTest(maxTime: Int) {
    val log = Logger.getAnonymousLogger()

    var value = 0f
    var count = 0L
    var start = System.currentTimeMillis()
    loop@ while (System.currentTimeMillis() - start < maxTime) {
        count++
        for (i in 0 until 1000000) {
            value += 0.1f
            value.posRoundToInt()
        }
    }
    log.warning(String.format("posRoundToInt(): %.1fs count=%dM",
            (System.currentTimeMillis() - start) / 1000f, count))

    value = 0f
    count = 0L
    start = System.currentTimeMillis()
    loop@ while (System.currentTimeMillis() - start < maxTime) {
        count++
        for (i in 0 until 1000000) {
            value += 0.1f
            value.simpleRoundToInt()
        }
    }
    log.warning(String.format("simpleRoundToInt(): %.1fs count=%dM",
            (System.currentTimeMillis() - start) / 1000f, count))

    value = 0f
    count = 0L
    start = System.currentTimeMillis()
    loop@ while (System.currentTimeMillis() - start < maxTime) {
        count++
        for (i in 0 until 1000000) {
            value += 0.1f
            Math.round(value)
        }
    }
    log.warning(String.format("Math.round(): %.1fs count=%dM",
            (System.currentTimeMillis() - start) / 1000f, count))

    value = 0f
    count = 0L
    start = System.currentTimeMillis()
    loop@ while (System.currentTimeMillis() - start < maxTime) {
        count++
        for (i in 0 until 1000000) {
            value += 0.1f
            value.roundToInt()
        }
    }
    log.warning(String.format("Kotlin.roundToInt(): %.1fs count=%dM",
            (System.currentTimeMillis() - start) / 1000f, count))
}