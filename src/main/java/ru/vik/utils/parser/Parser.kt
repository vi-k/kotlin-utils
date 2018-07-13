package ru.vik.utils.parser

open class Parser(
    var start: Int,
    val end: Int
) {
    var pos = start

    fun reset(start: Int = 0) {
        this.start = start
        this.pos = start
    }

    fun start(): Int {
        this.start = this.pos
        return this.start
    }

    fun eof() = this.pos >= this.end

    fun next() {
        this.pos++
    }

    fun back() {
        this.pos--
    }

    fun parsed() = this.pos != this.start

    fun parsed(start: Int): Boolean {
        this.start = start
        return this.pos != start
    }
}
