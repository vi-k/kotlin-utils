package ru.vik.utils.parser

open class StringParser(
    val source: CharSequence,
    start: Int = 0,
    end: Int = source.length
) : Parser(start, end) {

    fun get() = this.source[this.pos]

    operator fun get(index: Int) = this.source[index]

    fun getAndNext() = this.source[this.pos++]

    fun getParsed() = this.source.substring(this.start, this.pos)

    fun getString(start: Int = this.start, end: Int = this.pos) = this.source.substring(start, end)

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
