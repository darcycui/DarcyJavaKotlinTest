package org.example.coroutine

import kotlinx.coroutines.*

fun main() {
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    println("main start: I'm working in thread ${Thread.currentThread().name}")
    scope.launch(Dispatchers.Default) {
        println("default start: I'm working in thread ${Thread.currentThread().name}")
        delay(3_000)
        println("default end: I'm working in thread ${Thread.currentThread().name}")
        scope.launch(Dispatchers.IO) {
            println("io start: I'm working in thread ${Thread.currentThread().name}")
            delay((1_000))
            println("io end: I'm working in thread ${Thread.currentThread().name}")
        }
    }
    Thread.sleep(10_000)
    println("main end: I'm working in thread ${Thread.currentThread().name}")
}

class TestCoroutine {
}