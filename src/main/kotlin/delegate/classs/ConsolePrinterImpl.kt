package org.example.delegate.classs

class ConsolePrinterImpl : Printer {
    override fun printMessage(message: String) {
        println(message)
    }
}