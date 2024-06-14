package org.example

import java.nio.file.Files

fun main() {
    println("Hello World!")
    var selector = Selector {
        println("it=$it")
    }
    selector.doSomething()
    selector.responseUI = null
    repeat(10) {
        println("count=$it")
    }
}