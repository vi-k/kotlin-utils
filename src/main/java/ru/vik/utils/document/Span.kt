package ru.vik.utils.document

class Span(
    var start: Int,
    var end: Int,
    val characterStyle: CharacterStyle,
    val borderStyle: BorderStyle = BorderStyle()
)
