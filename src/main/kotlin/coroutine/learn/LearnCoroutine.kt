package org.example.coroutine.learn

import kotlinx.coroutines.*

/**
 * 理解协程 CoroutineDispatcher
 * https://juejin.cn/post/7127492385923137549
 */
fun main() {
    val scope = CoroutineScope(Job())
//    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        printWithThread("协程开始执行")
        printWithThread("模拟耗时操作：start")
        delay(1_000)
        printWithThread("模拟耗时操作：finish")
        printWithThread("协程执行结束")
    }
    Thread.sleep(5_000)
}

private fun printWithThread(message: String) {
    println("${Thread.currentThread().name} $message")
}
