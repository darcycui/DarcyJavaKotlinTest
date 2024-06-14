package org.example.delegate.test

import org.example.delegate.classs.ConsolePrinterImpl
import org.example.delegate.classs.MessageProcessor
import org.example.delegate.field.LazyProperty

fun main() {
    // 测试类委托/类代理
    val consolePrinterImpl: ConsolePrinterImpl = ConsolePrinterImpl()
    val messageProcessor: MessageProcessor = MessageProcessor(consolePrinterImpl)
    messageProcessor.printMessage("This is a message")

    // 测试属性委托/属性代理
    val lazyProperty = LazyProperty {
        println("\ninit lazyProperty.lazyValue by lazy+++")
        1
    }
    repeat(2) {
        println()
        println("lazyProperty.lazyValue=${lazyProperty.lazyValue}")
        println("lazyProperty.lazyValue2=${lazyProperty.lazyValue2}")
    }
}