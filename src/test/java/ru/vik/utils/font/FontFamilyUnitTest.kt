package ru.vik.utils.font

import org.junit.Assert.*
import org.junit.Test

class FontFamilyUnitTest {
    @Test
    fun test() {
        val fontFamily = FontFamily()

        fontFamily[100, false] = Font()
        fontFamily[250, false] = Font()
        fontFamily[400, false] = Font()
        fontFamily[550, false] = Font()
        fontFamily[700, false] = Font()
        fontFamily[900, false] = Font()
        fontFamily[100, true] = Font()
        fontFamily[250, true] = Font()
        fontFamily[400, true] = Font()
        fontFamily[550, true] = Font()
        fontFamily[700, true] = Font()
        fontFamily[900, true] = Font()


        /* Нормальный */

        fontFamily.getNear(100, false).also {
            assertEquals(100, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(101, false).also {
            assertEquals(250, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(200, false).also {
            assertEquals(250, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(300, false).also {
            assertEquals(400, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(400, false).also {
            assertEquals(400, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(500, false).also {
            assertEquals(400, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(550, false).also {
            assertEquals(550, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(600, false).also {
            assertEquals(550, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(700, false).also {
            assertEquals(700, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(800, false).also {
            assertEquals(700, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(899, false).also {
            assertEquals(700, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(900, false).also {
            assertEquals(900, it?.weight)
            assertEquals(false, it?.isItalic)
        }


        /* Курсив */

        fontFamily.getNear(100, true).also {
            assertEquals(100, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(101, true).also {
            assertEquals(250, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(200, true).also {
            assertEquals(250, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(300, true).also {
            assertEquals(400, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(400, true).also {
            assertEquals(400, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(500, true).also {
            assertEquals(400, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(550, true).also {
            assertEquals(550, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(600, true).also {
            assertEquals(550, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(700, true).also {
            assertEquals(700, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(800, true).also {
            assertEquals(700, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(899, true).also {
            assertEquals(700, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(900, true).also {
            assertEquals(900, it?.weight)
            assertEquals(true, it?.isItalic)
        }


        /* Удаляем шрифт с курсивом, должен остановиться на шрифте без курсива */

        fontFamily.remove(400, true)

        fontFamily.getNear(300, true).also {
            assertEquals(400, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(400, true).also {
            assertEquals(400, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(500, true).also {
            assertEquals(400, it?.weight)
            assertEquals(false, it?.isItalic)
        }


        /* Удаляем нормальный шрифт, должен остановиться на ближайшем шрифте с курсивом */

        fontFamily.remove(400, false)

        fontFamily.getNear(300, false).also {
            assertEquals(250, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(400, false).also {
            assertEquals(550, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(500, false).also {
            assertEquals(550, it?.weight)
            assertEquals(false, it?.isItalic)
        }

        fontFamily.getNear(300, true).also {
            assertEquals(250, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(400, true).also {
            assertEquals(550, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(500, true).also {
            assertEquals(550, it?.weight)
            assertEquals(true, it?.isItalic)
        }


        /* Восстанавливаем шрифт с курсивом, но нормальный шрифт остаётся удалённым.
         * Шрифт с курсивом не должен подхватываться - из него нельзя сделать шрифт без курсива!
         */
        fontFamily[400, true] = Font()

        fontFamily.getNear(300, false).also {
            assertEquals(250, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(400, false).also {
            assertEquals(550, it?.weight)
            assertEquals(false, it?.isItalic)
        }
        fontFamily.getNear(500, false).also {
            assertEquals(550, it?.weight)
            assertEquals(false, it?.isItalic)
        }


        fontFamily.clear()
        fontFamily[400, true] = Font()

        fontFamily.getNear(300, false).also {
            assertEquals(400, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(400, false).also {
            assertEquals(400, it?.weight)
            assertEquals(true, it?.isItalic)
        }
        fontFamily.getNear(500, false).also {
            assertEquals(400, it?.weight)
            assertEquals(true, it?.isItalic)
        }
    }
}