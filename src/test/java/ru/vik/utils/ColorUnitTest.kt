package ru.vik.utils

import org.junit.Test

import org.junit.Assert.*
import java.util.logging.Logger

import ru.vik.utils.color.Color.Companion.argb
import ru.vik.utils.color.layer
import ru.vik.utils.color.mix

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ColorUnitTest {
    @Test
    fun test() {
//        val log = Logger.getLogger("Test")

        // Наложение цветов - layer()

        // Наложение прозрачного цвета
        var color = argb(192, 255, 128, 0)
                .layer(argb(0, 0, 0, 0))
        assertEquals(argb(192, 255, 128, 0), color)

        color = argb(192, 255, 128, 0)
                .layer(argb(0, 255, 255, 255))
        assertEquals(argb(192, 255, 128, 0), color)

        // Обычные цвета
        color = argb(192, 255, 128, 0)
                .layer(argb(64, 0, 255, 128))
        assertEquals(argb(208, 176, 167, 39), color)

        color = argb(255, 255, 0, 0)
                .layer(argb(128, 0, 0, 255))
        assertEquals(argb(255, 127, 0, 128), color)

        color = argb(255, 255, 0, 0)
                .layer(argb(127, 0, 0, 255))
        assertEquals(argb(255, 128, 0, 127), color)


        // Смешивание цветов

        // Смешивание с прозрачным цветом - должен измениться только канал a. Каналы r, g, b
        // должны остаться прежними. Какие при этом у прозрачного r, g и b значения не имеет
        color = argb(255, 255, 255, 255)
                .mix(1.0)
        assertEquals(argb(255, 255, 255, 255), color)

        color = argb(255, 255, 255, 255)
                .mix(0.5)
        assertEquals(argb(128, 255, 255, 255), color)

        color = argb(255, 255, 255, 255)
                .mix(0.25)
        assertEquals(argb(64, 255, 255, 255), color)

        color = argb(255, 255, 255, 255)
                .mix(0.0)
        assertEquals(argb(0, 255, 255, 255), color)

        color = argb(255, 255, 255, 255)
                .mix(1.0, argb(0, 0, 0, 0))
        assertEquals(argb(255, 255, 255, 255), color)

        color = argb(255, 255, 255, 255)
                .mix(0.5, argb(0, 0, 0, 0))
        assertEquals(argb(128, 255, 255, 255), color)

        color = argb(255, 255, 255, 255)
                .mix(0.25, argb(0, 255, 128, 0))
        assertEquals(argb(64, 255, 255, 255), color)

        // Один из цветов занимаёт всё пространство - он же и должен стать результатом смешивания
        color = argb(255, 255, 255, 255)
                .mix(1.0, argb(255, 0, 0, 0))
        assertEquals(argb(255, 255, 255, 255), color)

        color = argb(255, 255, 255, 255)
                .mix(0.0, argb(255, 0, 0, 0))
        assertEquals(argb(255, 0, 0, 0), color)

        color = argb(128, 255, 255, 255)
                .mix(1.0, argb(255, 0, 0, 0))
        assertEquals(argb(128, 255, 255, 255), color)

        color = argb(255, 255, 255, 255)
                .mix(0.0, argb(128, 0, 0, 0))
        assertEquals(argb(128, 0, 0, 0), color)

        // Обычные цвета
        // Одного цвета только 25%, но зато a = 75%. Второго цвета - 75%, но a = 25%.
        // Каналы r, g, b должны стать равны среднему значению
        color = argb(192, 255, 128, 0)
                .mix(0.25, argb(64, 0, 255, 128))
        assertEquals(argb(96, 128, 192, 64), color)

        color = argb(204, 255, 128, 0)
                .mix(0.2, argb(51, 0, 255, 128))
        assertEquals(argb(82, 128, 192, 64), color)

        color = argb(230, 255, 128, 0)
                .mix(0.1, argb(25, 0, 255, 128))
        assertEquals(argb(46, 129, 191, 63), color)

        // Цвета в одинаковой пропорции с одинаковой прозрачностью
        color = argb(128, 255, 128, 0)
                .mix(0.5, argb(128, 0, 255, 128))
        assertEquals(argb(128, 128, 192, 64), color)

        // Gimp смешивает как-то по-другому - у него результат ярче, что на глаз выглядит лучше.
        // Но в устаревшем режиме смешивания результат тот же
        color = argb(128, 255, 0, 0)
                .mix(0.5, argb(128, 0, 0, 255))
        assertEquals(argb(128, 128, 0, 128), color)

        // Вес цветов не составляет 1, т.е. примешивается прозрачный. Меняется только канал a.
        // Каналы r, g, b смешиваются в относительной пропорции только друг с другом, прозрачный
        // цвет никак не участвует
        color = argb(128, 255, 0, 0)
                .mix(0.25, argb(128, 0, 0, 255), 0.25)
        assertEquals(argb(64, 128, 0, 128), color)

//        val (a, r, g, b) = argb


//        // Тест скорости
//        val MAX_TIME = 10000
//
//        var count = 0L
//        var t = System.currentTimeMillis()
//
//        loop@ while(true)
//            for (c1 in 0..255) {
//                for (c2 in 0..255) {
//                    val rgb1 = (c1 shl 16) or (c1 shl 8) or c1
//                    val rgb2 = (c2 shl 16) or (c2 shl 8) or c2
//                    for (a1 in 1..255) {
//                        for (a2 in 1..255) {
//                            val argb1 = (a1 shl 24) or rgb1
//                            val argb2 = (a2 shl 24) or rgb2
//
//                            argb1.layer(argb2)
//                            count++
//                        }
//                    }
//
//                    if (System.currentTimeMillis() - t >= MAX_TIME) break@loop
//                }
//            }
//        log.warning(String.format("layer(): %.1fs count=%d (%.1fM)",
//                (System.currentTimeMillis() - t) / 1000f, count, count / 1000000f))
//
//        // Тест скорости
//        count = 0L
//        t = System.currentTimeMillis()
//
//        loop@ while(true)
//            for (c1 in 0..255) {
//                for (c2 in 0..255) {
//                    val rgb1 = (c1 shl 16) or (c1 shl 8) or c1
//                    val rgb2 = (c2 shl 16) or (c2 shl 8) or c2
//                    for (a1 in 1..255) {
//                        for (a2 in 1..255) {
//                            val argb1 = (a1 shl 24) or rgb1
//                            val argb2 = (a2 shl 24) or rgb2
//
//                            argb1.mix(0.75, argb2)
//                            count++
//                        }
//                    }
//
//                    if (System.currentTimeMillis() - t >= MAX_TIME) break@loop
//                }
//            }
//        log.warning(String.format("mix(): %.1fs count=%d (%.1fM)",
//                (System.currentTimeMillis() - t) / 1000f, count, count / 1000000f))
    }
}