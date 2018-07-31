package ru.vik.utils.parser

fun StringParser.parseWord(numberOfWord: Int): Boolean {
    start()

    var number = 0

    while (true) {
        // Пропускам не-буквы
        while (!eof()) {
            val char = get()
            if (Character.isLetterOrDigit(char) || char == '_') break
            next()
        }

        // Отмечаем начало слова
        start()

        // Ищем конец слова
        while (!eof()) {
            val char = get()
            if (!Character.isLetterOrDigit(char) && char != '-' && char != '_' && char != '\u00AD') break
            next()
        }

        // Проверяем, слово ли это (есть ли хоть одна буква или цифра)
        for (i in this.start until this.pos) {
            if (Character.isLetterOrDigit(get(i))) {
                number++
                break
            }
        }

        if (number == numberOfWord) break

        start()
    }

    return number == numberOfWord
}
