package ru.vik.utils.html

open class SimpleHtml : BaseHtml() {

    init {
        addTag("b", TagConfig(type = Tag.Type.CHARACTER))
        addTag("br", TagConfig(type = Tag.Type.BR))
        addTag("div", TagConfig(type = Tag.Type.SECTION))
        addTag("h1", TagConfig(type = Tag.Type.PARAGRAPH))
        addTag("h2", TagConfig(type = Tag.Type.PARAGRAPH))
        addTag("h3", TagConfig(type = Tag.Type.PARAGRAPH))
        addTag("i", TagConfig(type = Tag.Type.CHARACTER))
        addTag("p", TagConfig(type = Tag.Type.PARAGRAPH))
        addTag("s", TagConfig(type = Tag.Type.CHARACTER))
        addTag("span", TagConfig(type = Tag.Type.CHARACTER))
        addTag("strike", TagConfig(type = Tag.Type.CHARACTER))
        addTag("sub", TagConfig(type = Tag.Type.CHARACTER))
        addTag("sup", TagConfig(type = Tag.Type.CHARACTER))
        addTag("small", TagConfig(type = Tag.Type.CHARACTER))
        addTag("u", TagConfig(type = Tag.Type.CHARACTER))
    }
}
