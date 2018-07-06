package ru.vik.utils.html

open class SimpleHtml : BaseHtml() {

    init {
        addTag("br", TagConfig(type = Tag.Type.BR))

        addTag("div", TagConfig(type = Tag.Type.SECTION))

        addTag("p", TagConfig(type = Tag.Type.PARAGRAPH))
        addTag("h1", TagConfig(type = Tag.Type.PARAGRAPH))
        addTag("h2", TagConfig(type = Tag.Type.PARAGRAPH))
        addTag("h3", TagConfig(type = Tag.Type.PARAGRAPH))
        addTag("blockquote", TagConfig(type = Tag.Type.PARAGRAPH))

        addTag("b", TagConfig(type = Tag.Type.CHARACTER))
        addTag("i", TagConfig(type = Tag.Type.CHARACTER))
        addTag("s", TagConfig(type = Tag.Type.CHARACTER))
        addTag("strike", getTagConfig("s")!!)
        addTag("span", TagConfig(type = Tag.Type.CHARACTER))
        addTag("font", getTagConfig("span")!!)
        addTag("sub", TagConfig(type = Tag.Type.CHARACTER))
        addTag("sup", TagConfig(type = Tag.Type.CHARACTER))
        addTag("small", TagConfig(type = Tag.Type.CHARACTER))
        addTag("u", TagConfig(type = Tag.Type.CHARACTER))
    }
}
