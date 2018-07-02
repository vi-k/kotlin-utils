package ru.vik.utils.htmldocument

import ru.vik.utils.color.Color
import ru.vik.utils.color.setA
import ru.vik.utils.document.*
import ru.vik.utils.html.BaseHtml
import ru.vik.utils.html.Tag
import ru.vik.utils.math.simpleRoundToInt

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
                    it.onSetBlockStyle?.invoke(tag, section.blockStyle)
                    it.onSetParagraphStyle?.invoke(tag, section.paragraphStyle)
                    it.onSetCharacterStyle?.invoke(tag, section.characterStyle)
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
                    it.onSetBlockStyle?.invoke(tag, paragraph.blockStyle)
                    it.onSetParagraphStyle?.invoke(tag, paragraph.paragraphStyle)
                    it.onSetCharacterStyle?.invoke(tag, paragraph.characterStyle)
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
                        it.onSetBlockStyle?.invoke(tag, span.blockStyle)
                        it.onSetCharacterStyle?.invoke(tag, span.characterStyle)
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
            paragraph.addSpan(Paragraph.Span(span.blockStyle.clone(), span.characterStyle.clone(), 0))
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
        private val reColorNum: Regex by lazy {
            Regex("""^#([0-9a-f]{3,6})$""", RegexOption.IGNORE_CASE)
        }

        private val reColorFun: Regex by lazy {
            Regex("""^(rgba?)\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*(,\s*(\d+(\.\d*)?)\s*)?\)$""",
                    RegexOption.IGNORE_CASE)
        }

        private val reSize: Regex by lazy {
            Regex("""^(-?\d+(\.\d*)?)(|px|em|%)$""", RegexOption.IGNORE_CASE)
        }

        fun getAttrColor(value: String): Int? {
            reColorNum.find(value)?.also {
                val num = it.groupValues[1]
                if (num.length == 6) {
                    num.toIntOrNull(16)?.also { return it.setA(255) }
                } else if (num.length == 3) {
                    num.toIntOrNull(16)?.also {
                        val r = it and 0x000f00
                        val g = it and 0x0000f0
                        val b = it and 0x00000f
                        return (0xff shl 24) or
                                (r shl 12) or (r shl 8) or
                                (g shl 8) or (g shl 4) or
                                (b shl 4) or b
                    }
                }

                return null
            }

            reColorFun.find(value)?.also {
                it.groupValues[2].toIntOrNull()?.also { mr ->
                    it.groupValues[3].toIntOrNull()?.also { mg ->
                        it.groupValues[4].toIntOrNull()?.also { mb ->
                            val r = Math.min(Math.max(mr, 0), 255)
                            val g = Math.min(Math.max(mg, 0), 255)
                            val b = Math.min(Math.max(mb, 0), 255)
                            var a = 255

                            it.groupValues[6].toFloatOrNull()?.also { ma ->
                                a = Math.min(Math.max((ma * 255f).simpleRoundToInt(), 0), 255)
                            }

                            return Color.argb(a, r, g, b)
                        }
                    }
                }
            }

            return null
        }

        fun getAttrSize(value: String): Size? {
            reSize.find(value)?.also {
                it.groupValues[1].toFloatOrNull()?.also { num ->
                    return when (it.groupValues[3].toLowerCase()) {
                        "%"  -> Size.percent(num)
                        "em" -> Size.em(num)
                        else -> Size.dp(num)
                    }
                }
            }

            return null
        }
    }
}