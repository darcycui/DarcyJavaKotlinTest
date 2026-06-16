package delegate.test
import exts.logD

import delegate.classs.ConsolePrinterImpl
import delegate.classs.MessageProcessor
import delegate.field.LazyProperty

fun main() {
    // 测试类委托/类代理
    val consolePrinterImpl: ConsolePrinterImpl = ConsolePrinterImpl()
    val messageProcessor: MessageProcessor = MessageProcessor(consolePrinterImpl)
    messageProcessor.printMessage("This is a message")
    logD(message = "-----------------------------------")

    // 测试属性委托/属性代理
    val lazyProperty = LazyProperty {
        logD(message = "\ninit lazyProperty.lazyValue by lazy+++")
        1
    }
    repeat(2) {
        logD(message = "-----------------------------------")
        logD(message = "lazyProperty.lazyValue=${lazyProperty.lazyValue}")
        logD(message = "lazyProperty.lazyValue2=${lazyProperty.lazyValue2}")
    }
}