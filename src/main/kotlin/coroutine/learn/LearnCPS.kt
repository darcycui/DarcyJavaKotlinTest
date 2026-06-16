package coroutine.learn
import exts.logD

fun main() {
    aaa()
    logD(message = "----------")
    aaaCPS()
}

// 直接调用风格
fun aaa() {
    logD(message = "我是aaa")
    val b = bbb()
    logD(message = "b=$b")
}

fun bbb(): Int {
    logD(message = "我是bbb")
    return 100
}

// CPS风格
fun aaaCPS() {
    logD(message = "我是aaaCPS")
    bbbCPS { b ->
        logD(message = "b=$b")
    }
}

fun bbbCPS(x: (Int) -> Unit) {
    logD(message = "我是bbbCPS")
    x.invoke(100)
}