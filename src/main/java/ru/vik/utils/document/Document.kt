package ru.vik.utils.document

open class Document : Section()

// Invoke внутри класса позволяет работать только с самим классом - в наследниках её можно
// применять только по отношению к родительскому классу. Данная же конструкция позволяет
// и наследникам полноценно использовать invoke.
//operator fun <T : Document> T.invoke(init: T.() -> Unit): T {
//    this.init()
//    return this
//}
