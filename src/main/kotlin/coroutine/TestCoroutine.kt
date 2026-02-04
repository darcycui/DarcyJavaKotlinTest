package org.example.coroutine

import kotlinx.coroutines.*

fun main() {
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    println("main start: 线程:${getThreadName()}")
    scope.launch(Dispatchers.Default) {
        println("default start: 线程:${getThreadName()}")
        delay(3_000)
        println("default end: 线程:${getThreadName()}")
        scope.launch(Dispatchers.IO) {
            println("io start: 线程:${getThreadName()}")
            delay((1_000))
            println("io end: 线程:${getThreadName()}")
        }
    }
    Thread.sleep(10_000)
    println("main end: 线程:${getThreadName()}")
}

private fun getThreadName(): String? = Thread.currentThread().name