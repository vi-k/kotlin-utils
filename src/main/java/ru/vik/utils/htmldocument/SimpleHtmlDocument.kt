package ru.vik.utils.htmldocument

import ru.vik.utils.html.Tag
import ru.vik.utils.document.*

open class SimpleHtmlDocument : BaseHtmlDocument() {

    init {
        tag("br") {
            type = Tag.Type.BR
        }

        // Sections
        tag("div") {
            type = Tag.Type.SECTION
            borderStyle(::setBorderStyleFromAttributes)
            paragraphStyle(::setParagraphStyleFromAttributes)
            characterStyle(::setCharacterStyleFromAttributes)
        }

        // Paragraphs
        tag("p") {
            type = Tag.Type.PARAGRAPH
            borderStyle(::setBorderStyleFromAttributes)
            paragraphStyle {
                spaceBefore = Size.em(1f)
                spaceAfter = Size.em(1f)
                setParagraphStyleFromAttributes(this, it)
            }
            characterStyle(::setCharacterStyleFromAttributes)
        }

        addTag("h1", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    size = Size.em(1.6f)
                    bold = true
                    setCharacterStyleFromAttributes(this, tag)
                }))

        addTag("h2", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    size = Size.em(1.4f)
                    bold = true
                    setCharacterStyleFromAttributes(this, tag)
                }))

        addTag("h3", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    size = Size.em(1.2f)
                    bold = true
                    setCharacterStyleFromAttributes(this, tag)
                }))

        addTag("blockquote", TagConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBorderStyle = { tag ->
                    marginLeft = Size.em(2f)
                    setBorderStyleFromAttributes(this, tag)
                },
                onSetParagraphStyle = ::setParagraphStyleFromAttributes,
                onSetCharacterStyle = ::setCharacterStyleFromAttributes))

        // Spans
        addTag("b", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    bold = true
                    setCharacterStyleFromAttributes(this, tag)
                }))

        addTag("i", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    italic = true
                    setCharacterStyleFromAttributes(this, tag)
                }))

        addTag("s", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    strike = true
                    setCharacterStyleFromAttributes(this, tag)
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
                onSetCharacterStyle = { tag ->
                    size = Size.em(0.85f)
                    baselineShift = Size.em(0.25f)
                    setCharacterStyleFromAttributes(this, tag)
                }))

        addTag("sup", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    size = Size.em(0.85f)
                    baselineShift = Size.em(-0.4f)
                    setCharacterStyleFromAttributes(this, tag)
                }))

        addTag("small", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    size = Size.em(0.85f)
                    setCharacterStyleFromAttributes(this, tag)
                }))

        addTag("u", TagConfig(
                type = Tag.Type.CHARACTER,
                onSetBorderStyle = ::setBorderStyleFromAttributes,
                onSetCharacterStyle = { tag ->
                    underline = true
                    setCharacterStyleFromAttributes(this, tag)
                })
        )
    }

    private fun setBorderStyleFromAttributes(borderStyle: BorderStyle, tag: Tag) {
        tag.attributes["bgColor"]?.apply {
            toHtmlColor()?.also { borderStyle.backgroundColor = it }
        }

        tag.attributes["margin"]?.apply {
            toHtmlSize()?.also { borderStyle.setMargin(it) }
        }

        tag.attributes["marginTop"]?.apply {
            toHtmlSize()?.also { borderStyle.marginTop = it }
        }

        tag.attributes["marginRight"]?.apply {
            toHtmlSize()?.also { borderStyle.marginTop = it }
        }

        tag.attributes["marginBottom"]?.apply {
            toHtmlSize()?.also { borderStyle.marginTop = it }
        }

        tag.attributes["marginLeft"]?.apply {
            toHtmlSize()?.also { borderStyle.marginTop = it }
        }

        tag.attributes["padding"]?.apply {
            toHtmlSize()?.also { borderStyle.setPadding(it) }
        }

        tag.attributes["paddingTop"]?.apply {
            toHtmlSize()?.also { borderStyle.paddingTop = it }
        }

        tag.attributes["paddingRight"]?.apply {
            toHtmlSize()?.also { borderStyle.paddingTop = it }
        }

        tag.attributes["paddingBottom"]?.apply {
            toHtmlSize()?.also { borderStyle.paddingTop = it }
        }

        tag.attributes["paddingLeft"]?.apply {
            toHtmlSize()?.also { borderStyle.paddingTop = it }
        }

        tag.attributes["border"]?.apply {
            val list = splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.setBorder(Border(size, color))
                    }
                }
            }
        }

        tag.attributes["borderTop"]?.apply {
            val list = splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.borderTop = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderRight"]?.apply {
            val list = splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.borderRight = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderBottom"]?.apply {
            val list = splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.borderBottom = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderLeft"]?.apply {
            val list = splitBySpace()
            if (list.size >= 2) {
                list[0].toHtmlSize(false)?.also { size ->
                    list[1].toHtmlColor()?.also { color ->
                        borderStyle.borderLeft = Border(size, color)
                    }
                }
            }
        }
    }

    private fun setParagraphStyleFromAttributes(paragraphStyle: ParagraphStyle, tag: Tag) {
        tag.attributes["align"]?.apply {
            when (this) {
                "left" -> paragraphStyle.align = ParagraphStyle.Align.LEFT
                "right" -> paragraphStyle.align = ParagraphStyle.Align.RIGHT
                "center" -> paragraphStyle.align = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.align = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["firstAlign"]?.apply {
            when (this) {
                "left" -> paragraphStyle.firstAlign = ParagraphStyle.Align.LEFT
                "right" -> paragraphStyle.firstAlign = ParagraphStyle.Align.RIGHT
                "center" -> paragraphStyle.firstAlign = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.firstAlign = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["lastAlign"]?.apply {
            when (this) {
                "left" -> paragraphStyle.lastAlign = ParagraphStyle.Align.LEFT
                "right" -> paragraphStyle.lastAlign = ParagraphStyle.Align.RIGHT
                "center" -> paragraphStyle.lastAlign = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.lastAlign = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["leftIndent"]?.apply {
            toHtmlSize()?.also { paragraphStyle.leftIndent = it }
        }

        tag.attributes["rightIndent"]?.apply {
            toHtmlSize()?.also { paragraphStyle.rightIndent = it }
        }

        tag.attributes["firstLeftIndent"]?.apply {
            toHtmlSize()?.also { paragraphStyle.firstLeftIndent = it }
        }

        tag.attributes["firstRightIndent"]?.apply {
            toHtmlSize()?.also { paragraphStyle.firstRightIndent = it }
        }
    }

    private fun setCharacterStyleFromAttributes(characterStyle: CharacterStyle, tag: Tag) {
        tag.attributes["font"]?.apply {
            characterStyle.font = this
        }

        tag.attributes["lang"]?.apply {
            if (this == "csl") characterStyle.font = "ponomar"
        }

        tag.attributes["size"]?.apply {
            toHtmlSize()?.also { characterStyle.size = it }
        }
    }
}
