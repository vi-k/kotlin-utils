package ru.vik.utils.parser

open class StringParser(val source: CharSequence,
                        start: Int = 0,
                        end: Int = source.length)
    : Parser(start, end) {

    fun get() = this.source[this.pos]

    operator fun get(index: Int) = this.source[index]

    fun getAndNext() = this.source[this.pos++]

    fun getParsedText() = this.source.substring(this.start, this.pos)
}
