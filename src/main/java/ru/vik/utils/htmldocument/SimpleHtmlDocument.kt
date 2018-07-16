package ru.vik.utils.htmldocument

import ru.vik.utils.html.Tag
import ru.vik.utils.document.*

open class SimpleHtmlDocument : BaseHtmlDocument() {

    init {
        addTag("br", TagConfig(type = Tag.Type.BR))

        // Sections
        addTag("div", TagConfig(
                type = Tag.Type.SECTION,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = ::setCharacterStyleFromAttributes))

        // Paragraphs
        addTag("p", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = ::setCharacterStyleFromAttributes))

        addTag("h1", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(1.6f)
                    characterStyle.bold = true
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("h2", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(1.4f)
                    characterStyle.bold = true
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("h3", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(1.2f)
                    characterStyle.bold = true
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("blockquote", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = { tag, borderStyle ->
                    borderStyle.marginLeft = Size.em(2f)
                    setBorderStyleFromAttributes(tag, borderStyle)
                },
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = ::setCharacterStyleFromAttributes))

        // Spans
        addTag("b", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.bold = true
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("i", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.italic = true
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("s", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.strike = true
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("strike", getTagConfig("s")!!)

        addTag("span", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = ::setCharacterStyleFromAttributes))

        addTag("font", getTagConfig("span")!!)

        addTag("sub", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(0.85f)
                    characterStyle.baselineShift = Size.em(0.25f)
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("sup", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(0.85f)
                    characterStyle.baselineShift = Size.em(-0.4f)
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("small", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(0.85f)
                    setCharacterStyleFromAttributes(tag, characterStyle)
                }))

        addTag("u", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.underline = true
                    setCharacterStyleFromAttributes(tag, characterStyle)
                })
        )
    }

    fun setBorderStyleFromAttributes(tag: Tag, borderStyle: BorderStyle) {
        tag.attributes["bgColor"]?.also {
            it.toHtmlColor()?.also { borderStyle.backgroundColor = it }
        }

        tag.attributes["margin"]?.also {
            it.toHtmlSize()?.also { borderStyle.setMargin(it) }
        }

        tag.attributes["marginTop"]?.also {
            it.toHtmlSize()?.also { borderStyle.marginTop = it }
        }

        tag.attributes["marginRight"]?.also {
            it.toHtmlSize()?.also { borderStyle.marginTop = it }
        }

        tag.attributes["marginBottom"]?.also {
            it.toHtmlSize()?.also { borderStyle.marginTop = it }
        }

        tag.attributes["marginLeft"]?.also {
            it.toHtmlSize()?.also { borderStyle.marginTop = it }
        }

        tag.attributes["padding"]?.also {
            it.toHtmlSize()?.also { borderStyle.setPadding(it) }
        }

        tag.attributes["paddingTop"]?.also {
            it.toHtmlSize()?.also { borderStyle.paddingTop = it }
        }

        tag.attributes["paddingRight"]?.also {
            it.toHtmlSize()?.also { borderStyle.paddingTop = it }
        }

        tag.attributes["paddingBottom"]?.also {
            it.toHtmlSize()?.also { borderStyle.paddingTop = it }
        }

        tag.attributes["paddingLeft"]?.also {
            it.toHtmlSize()?.also { borderStyle.paddingTop = it }
        }

        tag.attributes["border"]?.also {
            val list = it.splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.setBorder(Border(size, color))
                    }
                }
            }
        }

        tag.attributes["borderTop"]?.also {
            val list = it.splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.borderTop = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderRight"]?.also {
            val list = it.splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.borderRight = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderBottom"]?.also {
            val list = it.splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.borderBottom = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderLeft"]?.also {
            val list = it.splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.borderLeft = Border(size, color)
                    }
                }
            }
        }
    }

    fun setParagraphStyleFromAttributes(tag: Tag, paragraphStyle: ParagraphStyle) {
        tag.attributes["align"]?.also {
            when (it) {
                "left" -> paragraphStyle.align = ParagraphStyle.Align.LEFT
                "right" -> paragraphStyle.align = ParagraphStyle.Align.RIGHT
                "center" -> paragraphStyle.align = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.align = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["firstAlign"]?.also {
            when (it) {
                "left" -> paragraphStyle.firstAlign = ParagraphStyle.Align.LEFT
                "right" -> paragraphStyle.firstAlign = ParagraphStyle.Align.RIGHT
                "center" -> paragraphStyle.firstAlign = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.firstAlign = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["lastAlign"]?.also {
            when (it) {
                "left" -> paragraphStyle.lastAlign = ParagraphStyle.Align.LEFT
                "right" -> paragraphStyle.lastAlign = ParagraphStyle.Align.RIGHT
                "center" -> paragraphStyle.lastAlign = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.lastAlign = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["leftIndent"]?.also {
            it.toHtmlSize()?.also { paragraphStyle.leftIndent = it }
        }

        tag.attributes["rightIndent"]?.also {
            it.toHtmlSize()?.also { paragraphStyle.rightIndent = it }
        }

        tag.attributes["firstLeftIndent"]?.also {
            it.toHtmlSize()?.also { paragraphStyle.firstLeftIndent = it }
        }

        tag.attributes["firstRightIndent"]?.also {
            it.toHtmlSize()?.also { paragraphStyle.firstRightIndent = it }
        }
    }

    fun setCharacterStyleFromAttributes(tag: Tag, characterStyle: CharacterStyle) {
        tag.attributes["font"]?.also {
            characterStyle.font = it
        }

        tag.attributes["lang"]?.also {
            if (it == "csl") characterStyle.font = "ponomar"
        }

        tag.attributes["size"]?.also {
            it.toHtmlSize()?.also { characterStyle.size = it }
        }
    }
}
