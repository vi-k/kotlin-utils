package ru.vik.utils.parser

open class Parser(var start: Int,
                  val end: Int) {
    var pos = start

    fun reset(pos: Int = 0) {
        this.start = pos
        this.pos = pos
    }

    fun start(): Int {
        this.start = this.pos
        return this.pos
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
