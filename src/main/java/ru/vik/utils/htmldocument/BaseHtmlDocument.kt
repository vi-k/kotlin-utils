package ru.vik.utils.htmldocument

import ru.vik.utils.document.BlockStyle
import ru.vik.utils.document.CharacterStyle
import ru.vik.utils.document.Document
import ru.vik.utils.document.Paragraph
import ru.vik.utils.document.ParagraphStyle
import ru.vik.utils.document.Section
import ru.vik.utils.html.BaseHtml
import ru.vik.utils.html.Tag

typealias SetBlockStyleHandler = (Tag, BlockStyle) -> Unit
typealias SetParagraphStyleHandler = (Tag, ParagraphStyle) -> Unit
typealias SetCharacterStyleHandler = (Tag, CharacterStyle) -> Unit

open class BaseHtmlDocument(private val html: BaseHtml = BaseHtml())
    : Document() {

    class TextConfig(type: Tag.Type,
                     val onSetBlockStyle: SetBlockStyleHandler? = null,
                     val onSetParagraphStyle: SetParagraphStyleHandler? = null,
                     val onSetCharacterStyle: SetCharacterStyleHandler? = null
    ) : BaseHtml.TagConfig(type)

    private class State(var section: Section,
                        var paragraph: Paragraph? = null,
                        val openedSpans: MutableList<Paragraph.Span> = mutableListOf())

    fun setHtml(html: String) {
        this.html.parse(html)

        this.root = Section()
        val state = State(this.root!!)

        tagToText(this.html.root!!, state, true)
    }

    fun getTextConfig(name: String): TextConfig? {
        return this.html.getTagConfig(name)?.let {
            it as? TextConfig
        }
    }

    fun addTag(name: String, config: TextConfig) {
        this.html.config[name] = config
    }

    private fun tagToText(tag: Tag, state: State, isRoot: Boolean = false) {
        val config = this.getTextConfig(tag.name)

        // Создаём элемент и заполняем его свойства
        when (tag.type) {

        // Добавляем раздел
            Tag.Type.SECTION   -> {
                val section = if (isRoot) state.section else Section()
                config?.also {
                    it.onSetBlockStyle?.invoke(tag, section.bs)
                    it.onSetParagraphStyle?.invoke(tag, section.ps)
                    it.onSetCharacterStyle?.invoke(tag, section.cs)
                }

                var parent: Section? = null

                // Рутовый тег присоединяем к уже существующей рутовой секции
                // Для не рутовых тегов создаём отдельные разделы
                if (!isRoot) {
                    parent = state.section
                    parent.addSection(section)
                    state.section = section
                }

                closeParagraph(state)

                if (tag.text.isNotEmpty()) {
                    appendParagraph(state, tag.text)
                }

                for (child in tag.children) {
                    tagToText(child, state)
                }

                if (!isRoot) {
                    state.section = parent!!
                    closeParagraph(state)
                }
            }

        // Добавляем абзац
            Tag.Type.PARAGRAPH -> {
                val paragraph = appendParagraph(state, tag.text)

                config?.also {
                    it.onSetBlockStyle?.invoke(tag, paragraph.bs)
                    it.onSetParagraphStyle?.invoke(tag, paragraph.ps)
                    it.onSetCharacterStyle?.invoke(tag, paragraph.cs)
                }

                for (child in tag.children) {
                    tagToText(child, state)
                }

                closeParagraph(state)
            }

        // Добавляем стиль символов (спан). Спан в HTML может содержать в себе абзацы
        // и пересекать границы абзацев. У нас же он, наоборот, может находиться только внутри
        // абзаца. Поэтому дробим спан, разделяя его по абзацам. Открытые спаны сохраняем
        // в state.openedSpans
            Tag.Type.CHARACTER,
            Tag.Type.VOID      -> {
                var span: Paragraph.Span? = null

                if (tag.name.isNotEmpty()) {
                    span = Paragraph.Span()
                    config?.also {
                        it.onSetBlockStyle?.invoke(tag, span.bs)
                        it.onSetCharacterStyle?.invoke(tag, span.cs)
                    }
                }

                val paragraph = state.paragraph ?: appendParagraph(state)

                span?.start = paragraph.text.length
                paragraph.text.append(tag.text)

                span?.also {
                    paragraph.addSpan(it)
                    state.openedSpans.add(it)
                }

                for (child in tag.children) {
                    tagToText(child, state)
                }

                span?.also {
                    it.end = paragraph.text.length
                    state.openedSpans.remove(it)
                }
            }

            Tag.Type.BR        -> {
                val paragraph = state.paragraph ?: appendParagraph(state)
                paragraph.text.append('\n')
            }

            Tag.Type.UNKNOWN   -> {
            }
        }
    }

    // Добавление нового абзаца
    private fun appendParagraph(state: State, text: String? = null): Paragraph {
        val paragraph = Paragraph(text)

        // Переносим в созданный абзац открытые спаны
        for (span in state.openedSpans) {
            paragraph.addSpan(Paragraph.Span(span.bs.clone(), span.cs.clone(), 0))
        }

        // Закрываем текущий абзац
        closeParagraph(state)

        state.section.addParagraph(paragraph)
        state.paragraph = paragraph

        return paragraph
    }

    // Закрытие текущего абзаца
    private fun closeParagraph(state: State) {
        // Закрываем все открытые спаны
        state.paragraph?.also {
            for (span in it.spans) {
                if (span.end == -1) {
                    span.end = it.text.length
                }
            }
        }

        state.paragraph = null
    }

    companion object {
        fun getAttrColor(attr: String): Int {
            var color = 0
            if (attr.isNotEmpty()) {
                if (attr[0] == '#') {
                    val value = attr.substring(1)
                    if (value.length == 6) {
                        value.toIntOrNull(16)?.also {
                            color = it or -16777216 // alpha = 255
                        }
                    } else if (value.length == 3) {
                        value.toIntOrNull(16)?.also {
                            val r = (it and 0x000f00)
                            val g = (it and 0x0000f0)
                            val b = (it and 0x00000f)
                            color = (r shl 12) or (r shl 8) or
                                    (g shl 8) or (g shl 4) or
                                    (b shl 4) or b or (0xff shl 24) // alpha = 255
//                                    -16777216 // alpha = 255
                        }
                    }
                }
            }

            return color
        }
    }
}