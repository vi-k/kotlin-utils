package ru.vik.utils.math

/**
 * Округление до целого.
 *
 * И Math.round() и Kotlin.roundToInt() какие-то уж очень-очень медленные (что на ПК,
 * что на Анроиде). Может из-за всяких проверок на NaN и Inf?
 */
fun Float.roundToInt() = (this + 0.5f).toInt()
fun Double.roundToInt() = (this + 0.5).toInt()
