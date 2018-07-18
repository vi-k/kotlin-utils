package ru.vik.utils.document

import ru.vik.utils.parser.StringParser

open class Document : Section() {
    init {
        this.paragraphStyle = ParagraphStyle.default()
        this.characterStyle = CharacterStyle.default()
    }

    open fun setText(text: String) {
        this.clear()

        val parser = StringParser(text)

        while (!parser.eof()) {
            parser.start()
            while (!parser.eof()) {
                val char = parser.get()
                if (char == '\r' || char == '\n' || char == '\u2029')
                    break

                parser.next()
            }

            this.addParagraph(Paragraph(parser.getParsed()))

            if (!parser.eof()) {
                val char = parser.get()
                parser.next()
                if (char == '\r' && !parser.eof() && parser.get() == '\n') parser.next()
            }
        }
    }
}
