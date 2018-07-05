package ru.vik.utils.document

class Boundary<T>(
        var top: T,
        var right: T,
        var bottom: T,
        var left: T) {

    fun clone() = Boundary(
            top = this.top,
            right = this.right,
            bottom = this.bottom,
            left = this.left)
}
