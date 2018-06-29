package ru.vik.utils.parser

open class StringParserEx(source: CharSequence,
                          start: Int = 0,
                          end: Int = source.length)
    : StringParser(source, start, end) {

    fun getString(start: Int = this.start, end: Int = this.pos): String {
        return StringBuilder()
                .append(this.source, start, end)
                .toString()
    }

    open fun isSpace(char: Char): Boolean {
        return Character.isSpaceChar(char)
    }

    fun trimStart(str: String): String {
        var i = 0
        for (char in str) {
            if (!isSpace(char)) break
            i++
        }
        return str.substring(i)
    }

    fun trimEnd(str: String): String {
        var i = str.length
        for (char in str.reversed()) {
            if (!isSpace(char)) break
            i--
        }
        return str.substring(0, i)
    }

//    open fun isDigit(char: Char): Boolean {
//        return char in '0'..'9'
//    }
//
//    open fun isHexDigit(char: Char): Boolean {
//        return char in 'A'..'F' || char in 'a'..'f' || char in '0'..'9'
//    }

    fun parseChar(char: Char): Boolean {
        start()
        if (!eof() && this.get() == char) {
            next()
        }
        return parsed()
    }

    fun parseNoChar(char: Char): Boolean {
        start()
        if (!eof() && this.get() != char) {
            next()
        }
        return parsed()
    }

    fun parseDigits(): Int {
        start()
        var result = 0

        loop@ while (!eof()) {
            val char = get()

            val code = if (char in '0'..'9') char - '0'
            else break@loop

            result = (result * 10) + code
            next()
        }

        return result
    }

    fun parseHexDigits(): Int {
        start()
        var result = 0

        loop@ while (!eof()) {
            val char = get()
            val code = when (char) {
                in '0'..'9' -> char - '0'
                in 'A'..'F' -> char - 'A' + 10
                in 'a'..'f' -> char - 'a' + 10
                else        -> break@loop
            }

            result = (result shl 4) or code
            next()
        }

        return result
    }
}
