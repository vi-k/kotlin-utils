package ru.vik.utils.parser

open class StringParserEx(source: CharSequence,
    start: Int = 0,
    end: Int = source.length
) : StringParser(source, start, end) {

    open fun isSpace(char: Char): Boolean {
        return Character.isSpaceChar(char)
    }

    fun trimStart(string: CharSequence): String {
        var start = 0
        for (char in string) {
            if (!isSpace(char)) break
            start++
        }
        return string.substring(start)
    }

    fun trimEnd(string: CharSequence): String {
        var end = string.length
        while (end > 0 && isSpace(string[--end])) {
        }
        return string.substring(0, end)
    }

    fun trim(string: CharSequence): String {
        var start = 0
        for (char in string) {
            if (!isSpace(char)) break
            start++
        }

        var end = string.length
        while (end > start && isSpace(string[--end])) {
        }
        return string.substring(start, end)
    }

    fun parseSpace(): Boolean {
        start()
        if (!eof() && isSpace(get())) next()
        return parsed()
    }

    fun parseNoSpace(): Boolean {
        start()
        if (!eof() && !isSpace(get())) next()
        return parsed()
    }

    fun parseSpaces(): Boolean {
        val parseStart = start()
        while (parseSpace()) {
        }
        return parsed(parseStart)
    }

    fun parseNoSpaces(): Boolean {
        val parseStart = start()
        while (parseNoSpace()) {
        }
        return parsed(parseStart)
    }
}
