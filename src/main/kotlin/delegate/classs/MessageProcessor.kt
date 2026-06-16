package delegate.classs
import exts.logD

/**
 * 类委托/类代理
 */
class MessageProcessor(private val printer: Printer) : Printer by printer{
    override fun printMessage(message: String){
        // 可以在代理类执行前/执行后添加额外逻辑
        logD(message = "before+++")
        printer.printMessage(message)
        logD(message = "after---")
    }
}