package ru.vik.utils.html

open class BaseHtml {
    var root: Tag? = null
    val config = hashMapOf<String, TagConfig>()

    class InnerHtmlParser(
        val parent: BaseHtml,
        source: CharSequence
    ) : HtmlParser(source = source) {

        override fun setTagProperties(tag: Tag) {
            this.parent.setTagProperties(tag)
        }
    }

    open class TagConfig(val type: Tag.Type)

    open fun setTagProperties(tag: Tag) {
        if (!tag.type.isCorrect()) {
            tag.type = this.config[tag.name]?.type ?: Tag.Type.UNKNOWN
        }
    }

    fun parse(source: CharSequence): BaseHtml {
        this.root = InnerHtmlParser(this, source).parse()
        return this
    }

    fun getTagConfig(name: String): TagConfig? {
        return config[name]
    }

    fun addTag(name: String, config: TagConfig) {
        this.config[name] = config
    }

    fun removeTag(name: String) {
        config.remove(name)
    }

    override fun toString(): String {
        val output = StringBuilder()
        root?.let {
            HtmlParser.tagToString(output, it, false)
        }
        return output.toString()
    }
}
