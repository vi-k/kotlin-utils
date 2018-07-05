package ru.vik.utils.htmldocument

import ru.vik.utils.html.Tag
import ru.vik.utils.color.Color
import ru.vik.utils.document.*

open class SimpleHtmlDocument : BaseHtmlDocument() {

    init {
        addTag("br", TextConfig(type = Tag.Type.BR))

        // Sections
        addTag("div", TextConfig(
                type = Tag.Type.SECTION,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = ::setCSFromAttributes))

        // Paragraphs
        addTag("p", TextConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = ::setCSFromAttributes))

        addTag("h1", TextConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(1.6f)
                    characterStyle.bold = true
                    setCSFromAttributes(tag, characterStyle)
                }))

        addTag("h2", TextConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(1.4f)
                    characterStyle.bold = true
                    setCSFromAttributes(tag, characterStyle)
                }))

        addTag("h3", TextConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(1.2f)
                    characterStyle.bold = true
                    setCSFromAttributes(tag, characterStyle)
                }))

        // Spans
        addTag("b", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.bold = true
                    setCSFromAttributes(tag, characterStyle)
                }))

        addTag("i", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.italic = true
                    setCSFromAttributes(tag, characterStyle)
                }))

        addTag("s", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.strike = true
                    setCSFromAttributes(tag, characterStyle)
                }))

        addTag("strike", getTextConfig("s")!!)

        addTag("span", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = ::setCSFromAttributes))

        addTag("font", getTextConfig("span")!!)

        addTag("sub", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(0.85f)
                    characterStyle.baselineShift = Size.em(0.25f)
                    setCSFromAttributes(tag, characterStyle)
                }))

        addTag("sup", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(0.85f)
                    characterStyle.baselineShift = Size.em(-0.4f)
                    setCSFromAttributes(tag, characterStyle)
                }))

        addTag("small", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.size = Size.em(0.85f)
                    setCSFromAttributes(tag, characterStyle)
                }))

        addTag("u", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, characterStyle ->
                    characterStyle.underline = true
                    setCSFromAttributes(tag, characterStyle)
                })
        )
    }

    private fun setBSFromAttributes(tag: Tag, blockStyle: BlockStyle) {
        tag.attributes["bgColor"]?.also {
            getAttrColor(it)?.also { blockStyle.color = it }
        }

        tag.attributes["margin"]?.also {
            getAttrSize(it)?.also { blockStyle.setMargin(it) }
        }

        tag.attributes["marginTop"]?.also {
            getAttrSize(it)?.also { blockStyle.marginTop = it }
        }

        tag.attributes["marginRight"]?.also {
            getAttrSize(it)?.also { blockStyle.marginTop = it }
        }

        tag.attributes["marginBottom"]?.also {
            getAttrSize(it)?.also { blockStyle.marginTop = it }
        }

        tag.attributes["marginLeft"]?.also {
            getAttrSize(it)?.also { blockStyle.marginTop = it }
        }

        tag.attributes["padding"]?.also {
            getAttrSize(it)?.also { blockStyle.setPadding(it) }
        }

        tag.attributes["paddingTop"]?.also {
            getAttrSize(it)?.also { blockStyle.paddingTop = it }
        }

        tag.attributes["paddingRight"]?.also {
            getAttrSize(it)?.also { blockStyle.paddingTop = it }
        }

        tag.attributes["paddingBottom"]?.also {
            getAttrSize(it)?.also { blockStyle.paddingTop = it }
        }

        tag.attributes["paddingLeft"]?.also {
            getAttrSize(it)?.also { blockStyle.paddingTop = it }
        }

        tag.attributes["border"]?.also {
            val list = splitAttr(it)
            if (list.size >= 2) {
                getAttrSize(list[0])?.also { size ->
                    getAttrColor(list[1])?.also { color ->
                        blockStyle.setBorder(Border(size, color))
                    }
                }
            }
        }

        tag.attributes["borderTop"]?.also {
            val list = splitAttr(it)
            if (list.size >= 2) {
                getAttrSize(list[0])?.also { size ->
                    getAttrColor(list[1])?.also { color ->
                        blockStyle.borderTop = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderRight"]?.also {
            val list = splitAttr(it)
            if (list.size >= 2) {
                getAttrSize(list[0])?.also { size ->
                    getAttrColor(list[1])?.also { color ->
                        blockStyle.borderRight = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderBottom"]?.also {
            val list = splitAttr(it)
            if (list.size >= 2) {
                getAttrSize(list[0])?.also { size ->
                    getAttrColor(list[1])?.also { color ->
                        blockStyle.borderBottom = Border(size, color)
                    }
                }
            }
        }

        tag.attributes["borderLeft"]?.also {
            val list = splitAttr(it)
            if (list.size >= 2) {
                getAttrSize(list[0])?.also { size ->
                    getAttrColor(list[1])?.also { color ->
                        blockStyle.borderLeft = Border(size, color)
                    }
                }
            }
        }
    }

    private fun setPSFromAttributes(tag: Tag, paragraphStyle: ParagraphStyle) {
        tag.attributes["align"]?.also {
            when (it) {
                "left"    -> paragraphStyle.align = ParagraphStyle.Align.LEFT
                "right"   -> paragraphStyle.align = ParagraphStyle.Align.RIGHT
                "center"  -> paragraphStyle.align = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.align = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["firstAlign"]?.also {
            when (it) {
                "left"    -> paragraphStyle.firstAlign = ParagraphStyle.Align.LEFT
                "right"   -> paragraphStyle.firstAlign = ParagraphStyle.Align.RIGHT
                "center"  -> paragraphStyle.firstAlign = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.firstAlign = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["lastAlign"]?.also {
            when (it) {
                "left"    -> paragraphStyle.lastAlign = ParagraphStyle.Align.LEFT
                "right"   -> paragraphStyle.lastAlign = ParagraphStyle.Align.RIGHT
                "center"  -> paragraphStyle.lastAlign = ParagraphStyle.Align.CENTER
                "justify" -> paragraphStyle.lastAlign = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["leftIndent"]?.also {
            getAttrSize(it)?.also { paragraphStyle.leftIndent = it }
        }

        tag.attributes["rightIndent"]?.also {
            getAttrSize(it)?.also { paragraphStyle.rightIndent = it }
        }

        tag.attributes["firstLeftIndent"]?.also {
            getAttrSize(it)?.also { paragraphStyle.firstLeftIndent = it }
        }

        tag.attributes["firstRightIndent"]?.also {
            getAttrSize(it)?.also { paragraphStyle.firstRightIndent = it }
        }
    }

    private fun setCSFromAttributes(tag: Tag, characterStyle: CharacterStyle) {
        tag.attributes["lang"]?.also {
            if (it == "csl") characterStyle.font = "ponomar"
        }

        tag.attributes["size"]?.also {
            getAttrSize(it)?.also { characterStyle.size = it }
        }
    }
}
