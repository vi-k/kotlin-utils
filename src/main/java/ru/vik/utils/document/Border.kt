package ru.vik.utils.document

open class Border(
        size: Float,
        units: Units,
        val color: Int,
        val type: Type = Type.SOLID) : Size(size, units) {

    constructor(size: Size, color: Int, type: Type = Type.SOLID)
            : this(size.size, size.units, color, type)

    enum class Type { SOLID }

    // Клонирование не нужно, пока тип иммутабельный
//    fun clone(): Border {
//    }

    companion object {
        fun dp(size: Float, color: Int, type: Type = Type.SOLID) =
                Border(size, Units.DP, color, type)

        fun em(size: Float, color: Int, type: Type = Type.SOLID) =
                Border(size, Units.EM, color, type)
    }
}
