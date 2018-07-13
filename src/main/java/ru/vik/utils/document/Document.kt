package ru.vik.utils.document

import ru.vik.utils.parser.StringParser

open class Document : Section() {
    open fun setText(text: String) {
        this.clear()

        val parser = StringParser(text)

        while (!parser.eof()) {
            parser.start()
            while (!parser.eof() && parser.get() != '\n') {
                parser.next()
            }

            this.addParagraph(Paragraph(parser.getParsed()))
            parser.next()
        }
    }
}
