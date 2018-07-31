Kotlin-модуль `color` содержит утилиты для работы с цветом в формате ARGB. Включает в себя расширения (extensions) для базового типа `Int` и класс `Color` для конструкторов и констант.

# Содержание
- [Компоненты цвета](#Компоненты-цвета)
- [Манипуляции с цветом](#Манипуляции-с-цветом)
- [Константы](#Константы)
- [Функции создания цвета](#Функции-создания-цвета)

# Компоненты цвета:

## Получение компонент:

```kotlin
var Int.a: Int     // Альфа-составляющая   (0..255)
var Int.r: Int     // Красная составляющая (0..255)
var Int.g: Int     // Зелёная составляющая (0..255)
var Int.b: Int     // Синяя составляющая   (0..255)
val Int.argb: Argb // Все компоненты через мультидекларацию
val Int.rgb: Rgb   // Все компоненты (кроме a) через мультидекларацию
```

Классы для мультидекларации:

```kotlin
data class Argb(val a: Int, val r: Int, val g: Int, val b: Int)
data class Rgb(val r: Int, val g: Int, val b: Int)`
```

## Установка компонент:

```kotlin
fun Int.setA(value: Int)    // Установка альфа-составляющей   (0..255)
fun Int.setA(value: Float)  // Установка альфа-составляющей   (0f-1f)
fun Int.setA(value: Double) // Установка альфа-составляющей   (0.0-1.0)
fun Int.setR(value: Int)    // Установка красной составляющей (0..255)
fun Int.setG(value: Int)    // Установка зелёной составляющей (0..255)
fun Int.setB(value: Int)    // Установка синей составляющей   (0..255)
```

# Манипуляции с цветом

## Наложение цвета

```kotlin
fun Int.layer(color: Int): Int
```

Наложение цвета `color` на `this` с учётом альфа-каналов цветов. При наложении результат, как и в природе, всегда менее прозрачен, чем исходные цвета.

## Смешивание цветов

Разбавление цвета прозрачным цветом (`weight1` - вес основного цвета, вес прозрачного цвета равен `1 - weight1`):

```kotlin
fun Int.mix(weight1: Float)
fun Int.mix(weight1: Double)
```

Разбавление цвета другим цветом (weight1 - вес основного цвета, вес второго цвета равен `1 - weight1`):

```kotlin
fun Int.mix(weight1: Float, color2: Int)
fun Int.mix(weight1: Double, color2: Int)
```

Смешивание цветов (если сумма весов не равна 1, то оставшееся место занимает прозрачный цвет):

```kotlin
fun Int.mix(weight1: Float, color2: Int, weight2: Float): Int
fun Int.mix(weight1: Double, color2: Int, weight2: Double): Int
```

Эта функции именно смешивания, а не наложения цветов. Представьте себе квадрат, в котором каждый из указанных цветов, включая неявный прозрачный, занимает какую-то часть (долю от `0` до `1`). Функция смешивания вычисляет общий цвет этого квадрата, как если бы его уменьшили до размера пикселя. Соответственно, если в функциях наложения порядок расположения цветов имеет значение, то при смешивании порядок значения не имеет. Прозрачный цвет, если присутствует в функциях явно или неявно, воздействует только на составляющую `a`, но не влияет на составляющие `r`, `g`, `b`. (Вода не делает цвет светлее, она делает его прозрачнее.)

Функция использует не самую лучшую, но более менее быструю формулу смешивания. В GIMP эта формула названа устаревшей, но она всё ещё присутствует в нём. Новую формулу я пока не нашёл.

# Константы

```kotlin
Color.BLACK       // 0xff000000
Color.DKGRAY      // 0xff444444
Color.GRAY        // 0xff888888
Color.LTGRAY      // 0xffcccccc
Color.WHITE       // 0xffffffff
Color.RED         // 0xffff0000
Color.GREEN       // 0xff00ff00
Color.BLUE        // 0xff0000ff
Color.YELLOW      // 0xffffff00
Color.CYAN        // 0xff00ffff
Color.MAGENTA     // 0xffff00ff
Color.TRANSPARENT // 0x00000000
```

# Функции создания цвета

Функции вида `argb(a, r, g, b)` и `rgb(r, g, b)`:

```kotlin
fun argb(a: Int, r: Int, g: Int, b: Int): Int    // a, r, g, b (0..255)
fun argb(a: Float, r: Int, g: Int, b: Int): Int  // a (0f-1f); r, g, b (0..255)
fun argb(a: Double, r: Int, g: Int, b: Int): Int // a (0.0-1.0); r, g, b (0..255)
fun rgb(r: Int, g: Int, b: Int): Int             // r, g, b (0..255)
```

Функции вида `argb(a, rgb)` и `rgb(rgb)` для удобства использования шестнадцатеричных значений из HTML и CSS:

```kotlin
fun argb(a: Int, rgb: Int): Int    // a (0..255);  rgb (0x000000..0xffffff)
fun argb(a: Float, rgb: Int): Int  // a (0f-1f);   rgb (0x000000..0xffffff)
fun argb(a: Double, rgb: Int): Int // a (0.0-1.0); rgb (0x000000..0xffffff)
fun rgb(rgb: Int): Int             // rgb (0x000000..0xffffff)
```
