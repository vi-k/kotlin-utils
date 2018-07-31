package ru.vik.utils.document

interface ParagraphItem {
    val borderStyle: BorderStyle
    val paragraphStyle: ParagraphStyle
    val characterStyle: CharacterStyle
    var text: CharSequence
    val span get() = addSpan()

    class Word(val number: Int)

    fun word(number: Int) = Word(number)

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

    infix fun Span.border(borderStyle: BorderStyle): Span {
        this.borderStyle = borderStyle
        return this
    }

    /* Установка участка по словам */
    infix fun Span.on(word: Word): Span {
        val (start, end) = findWord(word.number, 0)
        this.start = start
        this.end = end
        return this
    }

    infix fun Span.from(word: Word): Span {
        val (start, _) = findWord(word.number, 0)
        this.start = start
        return this
    }

    infix fun Span.to(word: Word): Span {
        val (_, end) = findWord(word.number, 0)
        this.end = end
        return this
    }

    infix fun Span.count(word: Word): Span {
        val (_, end) = findWord(word.number, this.start)
        this.end = end
        return this
    }

    /* Установка участка по словам */
    infix fun Span.on(string: String): Span {
        val (start, end) = findString(string, 0)
        this.start = start
        this.end = end
        return this
    }

    infix fun Span.from(string: String): Span {
        val (start, _) = findString(string, 0)
        this.start = start
        return this
    }

    infix fun Span.to(string: String): Span {
        val (_, end) = findString(string, 0)
        this.end = end
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
        val (start, _) = find(regex, 0)
        this.start = start
        return this
    }

    infix fun Span.to(regex: Regex): Span {
        val (_, end) = find(regex, 0)
        this.end = end
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

    infix fun SpanList.border(borderStyle: BorderStyle?): SpanList {
        for (span in this) {
            span.borderStyle = borderStyle
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
    fun find(regex: Regex, start: Int = 0): Pair<Int, Int>
}
