package org.example.delegate.classs

/**
 * 类委托/类代理
 */
class MessageProcessor(private val printer: Printer) : Printer by printer{
    override fun printMessage(message: String){
        // 可以在代理类执行前/执行后添加额外逻辑
        println("before+++")
        printer.printMessage(message)
        println("after---")
    }
}