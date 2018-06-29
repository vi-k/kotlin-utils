package ru.vik.utils.htmldocument

import ru.vik.utils.document.BlockStyle
import ru.vik.utils.html.Tag
import ru.vik.utils.document.Border
import ru.vik.utils.document.CharacterStyle
import ru.vik.utils.document.ParagraphStyle
import ru.vik.utils.color.Color

open class SimpleHtmlDocument
    : BaseHtmlDocument() {

    private fun setBSFromAttributes(tag: Tag, bs: BlockStyle) {
        tag.attributes["bgColor"]?.also {
            bs.color = BaseHtmlDocument.getAttrColor(it)
        }
    }

    private fun setPSFromAttributes(tag: Tag, ps: ParagraphStyle) {
        tag.attributes["align"]?.also {
            when (it) {
                "left"    -> ps.align = ParagraphStyle.Align.LEFT
                "right"   -> ps.align = ParagraphStyle.Align.RIGHT
                "center"  -> ps.align = ParagraphStyle.Align.CENTER
                "justify" -> ps.align = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["firstAlign"]?.also {
            when (it) {
                "left"    -> ps.firstAlign = ParagraphStyle.Align.LEFT
                "right"   -> ps.firstAlign = ParagraphStyle.Align.RIGHT
                "center"  -> ps.firstAlign = ParagraphStyle.Align.CENTER
                "justify" -> ps.firstAlign = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["lastAlign"]?.also {
            when (it) {
                "left"    -> ps.lastAlign = ParagraphStyle.Align.LEFT
                "right"   -> ps.lastAlign = ParagraphStyle.Align.RIGHT
                "center"  -> ps.lastAlign = ParagraphStyle.Align.CENTER
                "justify" -> ps.lastAlign = ParagraphStyle.Align.JUSTIFY
            }
        }

        tag.attributes["leftIndent"]?.toFloatOrNull()?.also {
            ps.leftIndent = it
        }

        tag.attributes["rightIndent"]?.toFloatOrNull()?.also {
            ps.rightIndent = it
        }

        tag.attributes["firstLeftIndent"]?.toFloatOrNull()?.also {
            ps.firstLeftIndent = it
        }

        tag.attributes["firstRightIndent"]?.toFloatOrNull()?.also {
            ps.firstRightIndent = it
        }
    }

    private fun setCSFromAttributes(tag: Tag, cs: CharacterStyle) {
        tag.attributes["lang"]?.also {
            if (it == "csl") cs.font = "ponomar"
        }

        tag.attributes["size"]?.toFloatOrNull()?.also {
            cs.size = it
        }
    }

    init {
        addTag("br", TextConfig(type = Tag.Type.BR))

        // Sections
        addTag("div", TextConfig(
                type = Tag.Type.SECTION,
                onSetBlockStyle = { tag, bs ->
                    bs.setMargin(4f)
                    bs.setBorder(
                            Border(7f, Color.rgb(0, 255, 0)),
                            Border(3f, Color.rgb(255, 0, 0)))
//                    bs.setBorder(
//                            Border(7f, Color.argb(128, 0, 0, 255)),
//                            Border(7f, Color.argb(0, 255, 255, 255)),
//                            Border(3f, Color.argb(128, 255, 0, 0)),
//                            Border(2f, Color.argb(128, 0, 0, 255)))
                    bs.setPadding(4f, 4f)
                    setBSFromAttributes(tag, bs)
                },
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = ::setCSFromAttributes))

        // Paragraphs
        addTag("p", TextConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBlockStyle = { tag, bs ->
                    bs.setMargin(2f)
                    bs.setBorder(Border(1f, Color.rgb(160, 160, 160)))
//                    bs.setBorder(
//                            Border(7f, Color.argb(255, 0, 0, 255)),
//                            Border(7f, Color.argb(0, 255, 255, 255)),
//                            Border(3f, Color.argb(192, 255, 0, 0)),
//                            Border(2f, Color.argb(128, 0, 0, 255)))
                    bs.color = Color.rgb(192, 255, 192)
                    setBSFromAttributes(tag, bs)
                },
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = ::setCSFromAttributes))

        addTag("h1", TextConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    cs.scale = 1.6F
                    cs.bold = true
                    setCSFromAttributes(tag, cs)
                }))

        addTag("h2", TextConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    cs.scale = 1.4F
                    cs.bold = true
                    setCSFromAttributes(tag, cs)
                }))

        addTag("h3", TextConfig(
                type = Tag.Type.PARAGRAPH,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetParagraphStyle = ::setPSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    cs.scale = 1.2F
                    cs.bold = true
                    setCSFromAttributes(tag, cs)
                }))

        // Spans
        addTag("b", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    cs.bold = true
                    setCSFromAttributes(tag, cs)
                }))

//        addTag("div",
//                TextConfig(units = Tag.Units.SECTION))

        addTag("i", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    cs.italic = true
                    setCSFromAttributes(tag, cs)
                }))

        addTag("s", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    cs.strike = true
                    setCSFromAttributes(tag, cs)
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
                onSetCharacterStyle = { tag, cs ->
                    //                    cs.baselineShift = cs.size ?:  * 0.2F
                    cs.scale = 0.7F
                    setCSFromAttributes(tag, cs)
                }))

        addTag("sup", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    // cs.baselineShift = cs.size ?:  * 0.2F
                    cs.scale = 0.7F
                    setCSFromAttributes(tag, cs)
                }))

        addTag("small", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    cs.scale = 0.85F
                    setCSFromAttributes(tag, cs)
                }))

        addTag("u", TextConfig(
                type = Tag.Type.CHARACTER,
                onSetBlockStyle = ::setBSFromAttributes,
                onSetCharacterStyle = { tag, cs ->
                    cs.underline = true
                    setCSFromAttributes(tag, cs)
                })
        )
    }
}
