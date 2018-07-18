package ru.vik.utils.htmldocument

import ru.vik.utils.color.Color
import ru.vik.utils.color.setA
import ru.vik.utils.document.*
import ru.vik.utils.html.BaseHtml
import ru.vik.utils.html.Tag
import ru.vik.utils.math.simpleRoundToInt
import ru.vik.utils.parser.StringParser

typealias SetBorderStyleHandler = (Tag, BorderStyle) -> Unit
typealias SetParagraphStyleHandler = (Tag, ParagraphStyle) -> Unit
typealias SetCharacterStyleHandler = (Tag, CharacterStyle) -> Unit

open class BaseHtmlDocument(
    private val html: BaseHtml = BaseHtml()
) : Document() {

    class TagConfig(
        type: Tag.Type,
        var onSetBorderStyle: SetBorderStyleHandler? = null,
        var onSetParagraphStyle: SetParagraphStyleHandler? = null,
        var onSetCharacterStyle: SetCharacterStyleHandler? = null
    ) : BaseHtml.BaseTagConfig(type)

    private class State(
        var section: Section,
        var paragraph: Paragraph? = null,
        val openedSpans: MutableList<Span> = mutableListOf()
    )

    override fun setText(text: String) {
        this.html.parse(text)

        this.clear()
        val state = State(this)

        tagToText(this.html.root!!, state, true)
    }

    fun getTagConfig(name: String): TagConfig? {
        return this.html.getBaseTagConfig(name) as? TagConfig
    }

    fun addTag(name: String, config: TagConfig) {
        this.html.config[name] = config
    }

    private fun tagToText(tag: Tag, state: State, isRoot: Boolean = false) {
        val config = this.getTagConfig(tag.name)

        // Создаём элемент и заполняем его свойства
        when (tag.type) {

            Tag.Type.SECTION -> {
                // Добавляем раздел

                val section = if (isRoot) state.section else Section()
                config?.also {
                    it.onSetBorderStyle?.invoke(tag, section.borderStyle)
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

            Tag.Type.PARAGRAPH -> {
                // Добавляем абзац

                val paragraph = appendParagraph(state, tag.text)

                config?.also {
                    it.onSetBorderStyle?.invoke(tag, paragraph.borderStyle)
                    it.onSetParagraphStyle?.invoke(tag, paragraph.paragraphStyle)
                    it.onSetCharacterStyle?.invoke(tag, paragraph.characterStyle)
                }

                for (child in tag.children) {
                    tagToText(child, state)
                }

                closeParagraph(state)
            }

            Tag.Type.CHARACTER,
            Tag.Type.VOID -> {
                // Добавляем стиль символов (спан). Спан в HTML может содержать в себе абзацы
                // и пересекать границы абзацев. У нас же он, наоборот, может находиться только внутри
                // абзаца. Поэтому дробим спан, разделяя его по абзацам. Открытые спаны сохраняем
                // в state.openedSpans

                var span: Span? = null

                if (tag.name.isNotEmpty()) {
                    span = Span(0, -1, CharacterStyle(), BorderStyle())
                    config?.also {
                        it.onSetBorderStyle?.invoke(tag, span.borderStyle!!)
                        it.onSetCharacterStyle?.invoke(tag, span.characterStyle)
                    }
                }

                val paragraph = state.paragraph ?: appendParagraph(state)

                span?.start = paragraph.textBuilder.length
                paragraph.textBuilder.append(tag.text)

                span?.also {
                    paragraph.addSpan(it)
                    state.openedSpans.add(it)
                }

                for (child in tag.children) {
                    tagToText(child, state)
                }

                span?.also {
                    it.end = paragraph.textBuilder.length
                    state.openedSpans.remove(it)
                }
            }

            Tag.Type.BR -> {
                val paragraph = state.paragraph ?: appendParagraph(state)
                paragraph.textBuilder.append('\n')
            }

            Tag.Type.UNKNOWN -> {
            }
        }
    }

    /*
     * Добавление нового абзаца
     */
    private fun appendParagraph(state: State, text: String? = null): Paragraph {
        val paragraph = Paragraph(text)

        // Переносим в созданный абзац открытые спаны
        for (span in state.openedSpans) {
            paragraph.addSpan(0, -1,
                    span.characterStyle.clone(), span.borderStyle!!.clone())
        }

        // Закрываем текущий абзац
        closeParagraph(state)

        state.section.addParagraph(paragraph)
        state.paragraph = paragraph

        return paragraph
    }

    /*
     * Закрытие текущего абзаца.
     */
    private fun closeParagraph(state: State) {
        // Закрываем все открытые спаны
        state.paragraph?.also {
            for (span in it.spans) {
                if (span.end == -1) {
                    span.end = it.textBuilder.length
                }
            }
        }

        state.paragraph = null
    }
}

/*
 * Вспомогательные функции
 */
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

/**
 * Преобразование строки в форматах, принятых в HTML и CSS, в цвет:
 * 1) #RrGgBb - цвет задан в шестнадцатиричном виде (Rr, Gg, Bb in 00..ff)
 * 2) #rgb - цвет задан в сокращённом шестнадцатиричном виде, раскрывается
 *    в #rrggbb (r, g, b = 0..f)
 * 3) rgb(r, g, b) - цвет задан тремя числами (r, g, b = 0..255)
 * 4) rgba(r, g, b, a) - цвет задан тремя числами (r, g, b = 0..255) и
 *    альфа-каналом (a = 0..1)
 * @return Числовое значение указанного цвета либо null, если преобразование не удалось.
 */
fun String.toHtmlColor(): Int? {
    reColorNum.find(this)?.also {
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

    reColorFun.find(this)?.also { res ->
        res.groupValues[2].toIntOrNull()?.also { mr ->
            res.groupValues[3].toIntOrNull()?.also { mg ->
                res.groupValues[4].toIntOrNull()?.also { mb ->
                    val r = Math.min(Math.max(mr, 0), 255)
                    val g = Math.min(Math.max(mg, 0), 255)
                    val b = Math.min(Math.max(mb, 0), 255)
                    var a = 255

                    if (res.groupValues[5].isEmpty()) {
                        // Если не задан параметр a
                        if (res.groupValues[1] == "rgba") return null
                    } else {
                        // Если задан параметр a
                        if (res.groupValues[1] != "rgba") return null

                        res.groupValues[6].toFloatOrNull()?.also { ma ->
                            a = Math.min(Math.max((ma * 255f).simpleRoundToInt(), 0), 255)
                        }
                    }

                    return Color.argb(a, r, g, b)
                }
            }
        }
    }

    return null
}

/**
 * Преобразование строки в форматах, принятых в HTML и CSS, в размер. Из единиц измерений
 * возможно использовать только px, em и %. Если единицы не указаны, подразумеваются px.
 *
 * @param allowPercent В некоторых случаях необходимо запретить установку размера
 * через проценты.
 * @return Объект типа Size либо null, если преобразование не удалось. Класс Size
 * не поддерживает напрямую проценты, они переводятся в доли (ratio).
 */
fun String.toHtmlSize(allowPercent: Boolean = true): Size? {
    reSize.find(this)?.also {
        it.groupValues[1].toFloatOrNull()?.also { num ->
            return when (it.groupValues[3].toLowerCase()) {
                "%" -> if (allowPercent) Size.percent(num) else null
                "em" -> Size.em(num)
                else -> Size.dp(num)
            }
        }
    }

    return null
}

/**
 * Преобразование строки параметров, разделённых пробелами, в список. Между параметрами может
 * быть любое кол-во пробелов.
 *
 * @return Список значений (MutableList<String>).
 */
fun String.splitBySpace(): List<String> {
    val list = mutableListOf<String>()
    val parser = StringParser(this)

    while (!parser.eof()) {
        // Пропускаем пробелы
        while (!parser.eof() && parser.get() == ' ') {
            parser.next()
        }

        // Сохраняем строку без пробелов
        parser.start()

        while (!parser.eof() && parser.get() != ' ') {
            parser.next()
        }

        if (parser.parsed()) list.add(parser.getParsed())

        parser.next()
    }

    return list
}
