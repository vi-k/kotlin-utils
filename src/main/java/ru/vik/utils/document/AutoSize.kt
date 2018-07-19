package ru.vik.utils.document

/**
 * AutoSize - класс с возможностью хранения или Size, или null.
 */
open class AutoSize(val size: Size? = null) {
    fun isAuto() = this.size == null
}
