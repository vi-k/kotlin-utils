package ru.vik.utils.document

interface ParagraphItem {
    val borderStyle: BorderStyle
    val paragraphStyle: ParagraphStyle
    val characterStyle: CharacterStyle
    var text: String
    val span get() = addSpan()

    class Word(val number: Int)
    class NextString(val string: String, var number: Int = 1)
    class LastString(val string: String)
    class NextRegex(val regex: Regex, var number: Int = 1)

    fun word(number: Int) = Word(number)
    fun next(string: String, number: Int = 1) = NextString(string, number)
    fun last(string: String) = LastString(string)
    fun next(regex: Regex, number: Int = 1) = NextRegex(regex, number)

    /* Установка участка по символам */
    infix fun Span.on(start: Int): Span {
        this.start = start
        this.end = start + 1
        return this
    }

    infix fun Span.from(start: Int): Span {
        this.start = start
        return this
    }

    infix fun Span.to(end: Int): Span {
        this.end = end
        return this
    }

    infix fun Span.count(count: Int): Span {
        this.end = this.start + count
        return this
    }

    infix fun Span.style(characterStyle: CharacterStyle): Span {
        this.characterStyle = characterStyle
        return this
    }

    infix fun Span.style(init: CharacterStyle.() -> Unit): Span {
        this.characterStyle.init()
        return this
    }

    infix fun Span.border(borderStyle: BorderStyle): Span {
        this.borderStyle = borderStyle
        return this
    }

    infix fun Span.border(init: BorderStyle.() -> Unit): Span {
        if (this.borderStyle == null) this.borderStyle = BorderStyle()
        this.borderStyle!!.init()
        return this
    }

    /* Установка участка по словам */
    infix fun Span.on(word: Word): Span {
        val (start, end) = findWord(word.number)
        this.start = start
        this.end = end
        return this
    }

    infix fun Span.from(word: Word): Span {
        this.start = findWord(word.number).first
        return this
    }

    infix fun Span.to(word: Word): Span {
        this.end = findWord(word.number).second
        return this
    }

    infix fun Span.count(word: Word): Span {
        this.end = findWord(word.number, this.start).second
        return this
    }


    /* Установка участка по словам */
    infix fun Span.on(string: String): Span {
        val (start, end) = findString(string)
        this.start = start
        this.end = end
        return this
    }

    infix fun Span.from(string: String): Span {
        this.start = findString(string).first
        return this
    }

    infix fun Span.from(next: NextString): Span {
        var res = findString(next.string)
        if (res.first != 0) this.start = res.first
        else this.start = findString(next.string, res.second).first

        for (i in 2..next.number) {
            res = findString(next.string, res.second)
            this.start = res.first
        }

        return this
    }

    infix fun Span.from(last: LastString): Span {
        this.start = findLastString(last.string).first
        return this
    }

    infix fun Span.to(string: String): Span {
        this.end = findString(string, this.start).second
        return this
    }

    infix fun Span.to(next: NextString): Span {
        val (start, end) = findString(next.string, this.start)
        if (start != this.start) this.end = end
        else this.end = findString(next.string, end).second

        for (i in 2..next.number) {
            this.end = findString(next.string, this.end).second
        }

        return this
    }

    infix fun Span.to(last: LastString): Span {
        this.end = findLastString(last.string).second
        return this
    }


    /* Установка участка по регулярным выражениям */
    infix fun Span.on(regex: Regex): Span {
        val (start, end) = find(regex)
        this.start = start
        this.end = end
        return this
    }

    infix fun Span.from(regex: Regex): Span {
        this.start = find(regex).first
        return this
    }

    infix fun Span.from(next: NextRegex): Span {
        var res = find(next.regex)
        if (start != 0) this.start = res.first
        else this.start = find(next.regex, res.second).first

        for (i in 2..next.number) {
            res = find(next.regex, res.second)
            this.start = res.first
        }

        return this
    }

    infix fun Span.to(regex: Regex): Span {
        this.end = find(regex, this.start).second
        return this
    }

    infix fun Span.to(next: NextRegex): Span {
        val (start, end) = find(next.regex, this.start)
        if (start != this.start) this.end = end
        else this.end = find(next.regex, end).second

        for (i in 2..next.number) {
            this.end = find(next.regex, this.end).second
        }

        return this
    }

    infix fun Span.all(regex: Regex): SpanList {
        removeSpan(this)
        return addSpan(regex)
    }

    infix fun SpanList.limit(count: Int): SpanList {
        for (i in this.size - 1 downTo count) {
            removeSpan(this[i])
            this.removeAt(i)
        }
        return this
    }

    infix fun SpanList.style(characterStyle: CharacterStyle): SpanList {
        for (span in this) {
            span.characterStyle = characterStyle
        }
        return this
    }

    infix fun SpanList.style(init: CharacterStyle.() -> Unit): SpanList {
        for (span in this) {
            span.characterStyle.init()
        }
        return this
    }

    infix fun SpanList.border(borderStyle: BorderStyle?): SpanList {
        for (span in this) {
            span.borderStyle = borderStyle
        }
        return this
    }

    infix fun SpanList.border(init: BorderStyle.() -> Unit): SpanList {
        for (span in this) {
            if (span.borderStyle == null) span.borderStyle = BorderStyle()
            span.borderStyle!!.init()
        }
        return this
    }

    fun addSpan(span: Span): Span

    fun addSpan(start: Int = 0, end: Int = -1, characterStyle: CharacterStyle = CharacterStyle(),
        borderStyle: BorderStyle? = null
    ): Span

    fun addSpan(regex: Regex, characterStyle: CharacterStyle = CharacterStyle(),
        borderStyle: BorderStyle? = null
    ) = addSpan(regex, -1, characterStyle, borderStyle)

    fun addSpan(regex: Regex, count: Int, characterStyle: CharacterStyle = CharacterStyle(),
        borderStyle: BorderStyle? = null
    ): SpanList

    fun addWordSpan(number: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle? = null
    ) = addWordSpan(number, number, characterStyle, borderStyle)

    fun addWordSpan(first: Int, last: Int, characterStyle: CharacterStyle,
        borderStyle: BorderStyle? = null
    ): Span

    fun removeSpan(span: Span)

    fun findWord(number: Int, start: Int = 0): Pair<Int, Int>
    fun findString(string: String, start: Int = 0): Pair<Int, Int>
    fun findLastString(string: String): Pair<Int, Int>
    fun find(regex: Regex, start: Int = 0): Pair<Int, Int>
}
