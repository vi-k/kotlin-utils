package ru.vik.utils.document

open class Document : Section() {
    operator fun invoke(init: Document.() -> Unit): Document {
        this.init()
        return this
    }
}
