package ru.vik.utils.document

typealias SpanList = MutableList<Span>

class Span(
    var start: Int,
    var end: Int,
    var characterStyle: CharacterStyle,
    var borderStyle: BorderStyle? = null
)
