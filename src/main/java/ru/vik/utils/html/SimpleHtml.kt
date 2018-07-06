package ru.vik.utils.html

open class SimpleHtml : BaseHtml() {

    init {
        addTag("br", BaseTagConfig(type = Tag.Type.BR))

        addTag("div", BaseTagConfig(type = Tag.Type.SECTION))

        addTag("p", BaseTagConfig(type = Tag.Type.PARAGRAPH))
        addTag("h1", BaseTagConfig(type = Tag.Type.PARAGRAPH))
        addTag("h2", BaseTagConfig(type = Tag.Type.PARAGRAPH))
        addTag("h3", BaseTagConfig(type = Tag.Type.PARAGRAPH))
        addTag("blockquote", BaseTagConfig(type = Tag.Type.PARAGRAPH))

        addTag("b", BaseTagConfig(type = Tag.Type.CHARACTER))
        addTag("i", BaseTagConfig(type = Tag.Type.CHARACTER))
        addTag("s", BaseTagConfig(type = Tag.Type.CHARACTER))
        addTag("strike", getBaseTagConfig("s")!!)
        addTag("span", BaseTagConfig(type = Tag.Type.CHARACTER))
        addTag("font", getBaseTagConfig("span")!!)
        addTag("sub", BaseTagConfig(type = Tag.Type.CHARACTER))
        addTag("sup", BaseTagConfig(type = Tag.Type.CHARACTER))
        addTag("small", BaseTagConfig(type = Tag.Type.CHARACTER))
        addTag("u", BaseTagConfig(type = Tag.Type.CHARACTER))
    }
}
