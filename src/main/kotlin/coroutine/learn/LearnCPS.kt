package org.example.coroutine.learn

fun main() {
    aaa()
    println("----------")
    aaaCPS()
}

// 直接调用风格
fun aaa() {
    println("我是aaa")
    val b = bbb()
    println("b=$b")
}

fun bbb(): Int {
    println("我是bbb")
    return 100
}

// CPS风格
fun aaaCPS() {
    println("我是aaaCPS")
    bbbCPS { b ->
        println("b=$b")
    }
}

fun bbbCPS(x: (Int) -> Unit) {
    println("我是bbbCPS")
    x.invoke(100)
}