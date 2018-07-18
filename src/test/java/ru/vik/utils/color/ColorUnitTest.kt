package ru.vik.utils.color

import org.junit.Test

import org.junit.Assert.*
import ru.vik.utils.color.*

import ru.vik.utils.color.Color.Companion.argb

class ColorUnitTest {
    @Test
    fun test() {
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


        //speedTest(1000)
    }
}