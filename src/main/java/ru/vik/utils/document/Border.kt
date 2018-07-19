package ru.vik.utils.document

open class Border(
        size: Float,
        units: Units,
        val color: Int) : Size(size, units) {

    constructor(size: Size, color: Int) : this(size.size, size.units, color)

    companion object {
        fun px(size: Float, color: Int) = Border(size, Units.PX, color)
        fun dp(size: Float, color: Int) = Border(size, Units.DP, color)
        fun sp(size: Float, color: Int) = Border(size, Units.SP, color)
        fun mm(size: Float, color: Int) = Border(size, Units.MM, color)
        fun cm(size: Float, color: Int) = Border(size, Units.CM, color)
        fun inch(size: Float, color: Int) = Border(size, Units.IN, color)
        fun pt(size: Float, color: Int) = Border(size, Units.PT, color)
        fun em(size: Float, color: Int) = Border(size, Units.EM, color)
        fun ratio(size: Float, color: Int) = Border(size, Units.RATIO, color)
        fun eh(size: Float, color: Int) = Border(size, Units.EH, color)
    }
}
