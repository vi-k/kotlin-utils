package ru.vik.utils.html

class Tag(var name: String, var type: Type) {
    var parent: Tag? = null
    var text = ""
    var attributes = hashMapOf<String, String?>()
    var children = mutableListOf<Tag>()
    var closed = false

    enum class Type {
        UNKNOWN,
        CHARACTER,
        PARAGRAPH,
        SECTION,
        VOID, // Элемент без закрывающего тега
        BR;

        fun isCorrect(): Boolean {
            return this != UNKNOWN
        }

        fun isBlock(): Boolean {
            return this == PARAGRAPH || this == SECTION || this == BR
        }

        fun isInline(): Boolean {
            return this == CHARACTER || this == VOID
        }

        fun withEndTag(): Boolean {
            return this == CHARACTER || this == PARAGRAPH || this == SECTION
        }

        fun withoutEndTag(): Boolean {
            return this == VOID || this == BR
        }
    }

    fun add(tag: Tag) {
        tag.parent = this
        this.children.add(tag)
    }
}