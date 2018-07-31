Модуль `htmldocument` это развитие модуля [`document`] для перевода текста HTML в форматированный текст, предоставляемый классом `Document`.

[`document`]: https://github.com/vi-k/kotlin-utils/wiki/document

Содержание модуля:

- `BaseHtmlDocument`
- `SimpleHtmlDocument`

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val documentView: DocumentView = findViewById(R.id.docView)

	val htmlDocument = BaseHtmlDocument()
	documentView.document = htmlDocument

	htmlDocument.text = "Lorem <b>ipsum</b> <i>dolor</i> <b><i>sit</i></b> <u>amet</u>, <s>consectetur</s> adipiscing <sup>elit</sup>, sed <sub>do</sub> eiusmod ..."
```
