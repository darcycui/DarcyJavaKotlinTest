package delegate.classs
import exts.logD

class ConsolePrinterImpl : Printer {
    override fun printMessage(message: String) {
        logD(message = message)
    }
}