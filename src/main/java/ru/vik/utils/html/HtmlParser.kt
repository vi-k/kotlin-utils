package ru.vik.utils.html

import ru.vik.utils.parser.StringParserEx

open class HtmlParser(source: CharSequence,
                      start: Int = 0,
                      end: Int = source.length) : StringParserEx(source, start, end) {

    private var tag: Tag? = null
    private var lastTagWithEndSpace: Tag? = null
    private var needForTrimStart: Boolean = true

    fun parse(): Tag {
        val root = Tag("root", Tag.Type.SECTION)
        setTagProperties(root)

        this.tag = root

        while (!eof()) {
            handleText()
            handleTag()
        }

        lastTagTrimEnd()

        return root
    }

    open fun setTagProperties(tag: Tag) {
    }

    override fun isSpace(char: Char): Boolean {
        // The space characters, for the purposes of this specification, are U+0020 SPACE,
        // U+0009 CHARACTER TABULATION (tab), U+000A LINE FEED (LF), U+000C FORM FEED (FF),
        // and U+000D CARRIAGE RETURN (CR).
        return char == ' ' || char == '\t' || char == '\r' || char == '\u000C' || char == '\n'
    }

    private fun handleText(): Boolean {
        start()

        while (!eof() && get() != '<') next()

        if (parsed()) {
            appendText(getText(start, pos))
            return true
        }

        return false
    }

    private fun handleTag(): Boolean {
        val parseStart = start()

        if (!parseChar('<')) return false

        val opened = !parseChar('/')
        val tagStart = this.pos

        if (parseTagName()) {
            if (opened) {
                // Открывающий тег
                var isOk = false
                val tagName = getString(tagStart)
                val tag = Tag(tagName, Tag.Type.UNKNOWN)

                loop@ while (!eof()) {
                    parseSpaces()

                    if (parseChar('>')) {
                        isOk = true
                        break
                    }

                    // Аттрибут
                    if (parseAttrName()) {
                        val name = getString()
                        var value: String? = null

                        parseSpaces()

                        // Значение
                        if (parseChar('=')) {
                            parseSpaces()

                            value = when {
                                parseQuotedValue() -> getValue(this.start + 1, this.pos - 1)
                                parseSimpleValue() -> getValue(this.start, this.pos)
                                else               -> ""
                            }

                        }

                        tag.attributes[name] = value
                    }
                }

                if (isOk) {
                    setTagProperties(tag)
                    isOk = tag.type.isCorrect()
                }

                if (isOk) {
                    if (tag.type.isBlock()) {
                        lastTagTrimEnd()
                        this.needForTrimStart = true
                    } else if (tag.type == Tag.Type.VOID) {
                        this.needForTrimStart = false
                        this.lastTagWithEndSpace = null
                    }

                    this.tag!!.add(tag)

                    // Теги без закрывающего тега (<img>, <br>) не могут становиться родителями
                    if (tag.type.withEndTag()) {
                        this.tag = tag
                    } else {
                        tag.closed = true
                    }

                    return true
                }
            } else {
                // Закрывающий тег

                /* End tags must have the following format:
                   1. The first character of an end tag must be a U+003C LESS-THAN SIGN character (<).
                   2. The second character of an end tag must be a U+002F SOLIDUS character (/).
                   3. The next few characters of an end tag must be the element’s tag name.
                   4. After the tag name, there may be one or more space characters.
                   5. Finally, end tags must be closed by a U+003E GREATER-THAN SIGN character (>). */

                val tagName = getString(tagStart, this.pos)

                parseSpaces()

                if (parseChar('>')) {
                    var tag: Tag? = this.tag
                    while (tag != null) {
                        if (tag.name == tagName) break
                        tag = tag.parent
                    }

                    if (tag != null && tag.type.withEndTag()) {
                        if (tag.type.isBlock()) {
                            lastTagTrimEnd()
                            this.needForTrimStart = true
                        }

                        this.tag = closeTag(tag)
                        return true
                    }
                }
            }
        }

        addParsed(parseStart)

        return false
    }

    // Закрываем тег, переносим незакрытые вложенные теги дальше
    private fun closeTag(tag: Tag, parent: Tag? = tag.parent): Tag? {
        tag.closed = true

        var lastTag: Tag? = parent

        // Перенос возможен только тогда, когда указан parent
        parent?.let {
            tag.children.lastOrNull()?.let { lastChild ->
                if (!lastChild.closed) {
                    // Перенос незакрытых тегов возможен только для типа CHARACTER,
                    // блочные типы просто закрывают друг друга
                    if (lastChild.type != Tag.Type.CHARACTER) {
                        lastChild.closed = true
                    } else {
                        // Копируем тег со всеми его аттрибутами и свойствами
                        val copy = Tag(lastChild.name, lastChild.type)
                        copy.attributes.putAll(lastChild.attributes)
                        setTagProperties(copy)
                        parent.add(copy)
                        // Закрываем все вложенные теги, подменяя parent на нужный нам
                        lastTag = closeTag(lastChild, copy)
                    }
                }
            }
        }

        return lastTag
    }

    private fun lastTagTrimEnd() {
        val tag = this.lastTagWithEndSpace

        if (tag != null) {
            tag.text = trimEnd(tag.text)

            // Удаляем пустой фиктивный тег
            if (tag.text.isEmpty() && tag.name.isEmpty()) {
                tag.parent?.children?.remove(tag)
            }

            this.lastTagWithEndSpace = null
        }
    }

    private fun setTextToTag(tag: Tag, tagText: String) {
        var text = tagText

        if (text.isNotEmpty()) {
            if ((this.needForTrimStart || this.lastTagWithEndSpace != null)
                && isSpace(text.first())) {

                text = trimStart(text)
            }

            tag.text = text

            if (text.isNotEmpty()) {
                this.needForTrimStart = false
                this.lastTagWithEndSpace = if (isSpace(text.last())) tag else null
            }
        }
    }

    // Добавление текста к последнему тегу
    private fun appendText(text: String) {
        @Suppress("NAME_SHADOWING")
        var text = text
        var tag = this.tag!!

        // Пытаемся прилепить текст к существующему тегу, не добавляя новых, если это возможно
        if (tag.children.isEmpty() || tag.children.last().name.isEmpty()) {
            if (text.isNotEmpty()) {
                if (tag.children.isNotEmpty()) tag = tag.children.last()

                // Убираем лишние пробелы между тегами
                if (isSpace(text.first()) && tag.text.lastOrNull()?.let { isSpace(it) } == true) {
                    text = "${trimEnd(tag.text)} ${trimStart(text)}"
                    this.needForTrimStart = false
                } else if (tag.text.isNotEmpty()) {
                    text = "${tag.text}$text"
                    this.lastTagWithEndSpace = null
                }

                setTextToTag(tag, text)
            }
        }
        // Если прилепить текст к существующему тегу не удаётся, добавляем фиктивный тег
        else {
            val child = Tag("", Tag.Type.CHARACTER)

            child.closed = true
            setTextToTag(child, text)

            if (child.text.isNotEmpty()) tag.add(child)
        }
    }

    private fun getText(start: Int, end: Int): String {
        val output = StringBuilder()
        val parser = HtmlParser(this.source, start, end)
        var lastCharIsSpace = false

        while (!parser.eof()) {
            var char = parser.getAndNext()

            if (char == '&') char = parser.parseAmp()

            if (isSpace(char)) {
                // Заменяем все пробелы на ' ', лишние пробелы пропускаем
                if (!lastCharIsSpace) output.append(' ')

                parser.parseSpaces()
                lastCharIsSpace = true
            } else {
                lastCharIsSpace = false
                output.append(char)
            }
        }

        return output.toString()
    }

    private fun getValue(start: Int, end: Int): String {
        val output = StringBuilder()
        val parser = HtmlParser(this.source, start, end)

        while (!parser.eof()) {
            val char = parser.getAndNext()

            output.append(if (char != '&') char else parser.parseAmp())
        }

        return output.toString()
    }

    private fun addParsed(start: Int = this.start, end: Int = this.pos) {
        appendText(getString(start, end))
    }

    private fun parseAmp(): Char {
        val parseStart = start()

        var char = '&'
        var name: String? = null

        if (parseChar('#')) {
            if (parseChar('x')) {
                val code = parseHexDigits()
                if (parsed()) char = code.toChar()
            } else {
                val code = parseDigits()
                if (parsed()) char = code.toChar()
            }
        } else {
            val nameStart = start()
            while (parseAmpName());
            if (parsed(nameStart)) name = getString()
        }

        if (!parseChar(';')) {
            pos = parseStart
            return '&'
        }

        if (name != null) {
            when (name) {
                "nbsp" -> char = '\u00A0'
                "amp"  -> char = '&'
                "lt"   -> char = '<'
                "gt"   -> char = '>'
                "quot" -> char = '"'
                "apos" -> char = '\''
                else   -> pos = parseStart
            }
        }

        return char
    }

    private fun parseTagNameFirst(): Boolean {
        start()

        if (!eof() && isTagNameFirst(get())) next()

        return parsed()
    }

    private fun parseTagNamePart(): Boolean {
        start()

        if (!eof() && isTagNamePart(get())) next()

        return parsed()
    }

    private fun parseAttrNameFirst(): Boolean {
        start()

        if (!eof() && isAttrNameFirst(get())) next()

        return parsed()
    }

    private fun parseAttrNamePart(): Boolean {
        start()

        if (!eof() && isAttrNamePart(get())) next()

        return parsed()
    }

    private fun parseAmpNameFirst(): Boolean {
        start()

        if (!eof() && isAmpNameFirst(get())) next()

        return parsed()
    }

    private fun parseAmpNamePart(): Boolean {
        start()

        if (!eof() && isAmpNamePart(get())) next()

        return parsed()
    }

    private fun parseSpace(): Boolean {
        start()

        if (!eof() && isSpace(get())) next()

        return parsed()
    }

    private fun parseNoSpace(): Boolean {
        start()

        if (!eof() && !isSpace(get())) next()

        return parsed()
    }

    // Парсинг символа в строке, заключённой в кавычки
    private fun parseStringChar(quoteChar: Char): Boolean {
        start()
        return !eof() && getAndNext() != quoteChar
    }

    // Название тега
    private fun parseTagName(): Boolean {
        val parseStart = start()

        if (parseTagNameFirst()) {
            while (parseTagNamePart()) {
            }
        }

        return parsed(parseStart)
    }

    // Название аттрибута
    private fun parseAttrName(): Boolean {
        val parseStart = start()

        if (parseAttrNameFirst()) {
            while (parseAttrNamePart()) {
            }
        }

        return parsed(parseStart)
    }

    // Название сивола (&amp;)
    private fun parseAmpName(): Boolean {
        val parseStart = start()

        if (parseAmpNameFirst()) {
            while (parseAmpNamePart()) {
            }
        }

        return parsed(parseStart)
    }

    // Парсинг значения аттрибута в теге, заключённого в кавычки
    private fun parseQuotedValue(): Boolean {
        val parseStart = start()

        if (!eof()) {
            val quoteChar = get()
            if (quoteChar == '\'' || quoteChar == '"') {
                next()
                while (parseStringChar(quoteChar)) {
                }
            }
        }

        return parsed(parseStart)
    }

    // Парсинг значения аттрибута в теге, не заключённого в кавычки
    private fun parseSimpleValue(): Boolean {
        val parseStart = start()

        while (parseNoSpace()) {
            if (get(this.pos - 1) == '>') {
                back()
                break
            }
        }

        return parsed(parseStart)
    }

    private fun parseSpaces(): Boolean {
        val parseStart = start()

        while (parseSpace()) {
        }

        return parsed(parseStart)
    }

    companion object {
        fun tagToString(output: Appendable, tag: Tag, includeThisTag: Boolean = true) {

            // Название тега и его аттрибуты
            val hasName = includeThisTag && tag.name.isNotEmpty()
            if (hasName) {
                output.append('<')
                output.append(tag.name)

                for (attr in tag.attributes) {
                    output.append(' ')
                    output.append(attr.key)

                    attr.value?.let {
                        output.append("='")
                        output.append(it
                                .replace("&", "&amp;")
                                .replace("'", "&apos;")
                                .replace("\u00A0", "&nbsp;"))
                        output.append('\'')
                    }
                }

                output.append('>')
            }

            if (tag.type.withEndTag()) {
                // Текст тега
                output.append(tag.text
                        .replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\u00A0", "&nbsp;"))

                // "Дети"
                for (child in tag.children) {
                    tagToString(output, child)
                }

                // Завершающий тег
                if (hasName) {
                    output.append("</")
                    output.append(tag.name)
                    output.append('>')
                }
            }
        }

        fun isTagNameFirst(char: Char): Boolean {
            return char in 'A'..'Z' || char in 'a'..'z'
        }

        fun isTagNamePart(char: Char): Boolean {
            return char in 'A'..'Z' || char in 'a'..'z' ||
                   char == '-' || char == '_' || char in '0'..'9'
        }

        fun isAttrNameFirst(char: Char): Boolean {
            return Character.isLetter(char)
        }

        fun isAttrNamePart(char: Char): Boolean {
            return Character.isLetterOrDigit(char) || char == '-' || char == '_'
        }

        fun isAmpNameFirst(char: Char): Boolean {
            return char in 'A'..'Z' || char in 'a'..'z'
        }

        fun isAmpNamePart(char: Char): Boolean {
            return char in 'A'..'Z' || char in 'a'..'z' || char in '0'..'9'
        }
    }
}
