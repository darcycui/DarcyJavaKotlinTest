
import exts.logD

fun main() {
    logD(message = "Hello World!")
    var selector = Selector {
        logD(message = "it=$it")
    }
    selector.doSomething()
    selector.responseUI = null
    repeat(10) {
        logD(message = "count=$it")
    }
}