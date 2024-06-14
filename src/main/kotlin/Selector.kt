package org.example

class Selector(var responseUI: ((Int) -> Unit)?) {
    fun doSomething(): Unit? {
        responseUI?.invoke(100)
        return null
    }
}