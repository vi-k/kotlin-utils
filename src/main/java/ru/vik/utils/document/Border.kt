package ru.vik.utils.document

open class Border (
        val width: Float = 0f,
        val color: Int = 0,
        val type: Type = Type.SOLID
) {
    enum class Type {
        SOLID
    }

    // Клонирование не нужно, пока тип иммутабельный
//    fun clone(): Border {
//        return Border(this.width, this.color, this.units)
//    }
}