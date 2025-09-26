package org.example.interview

import kotlinx.coroutines.*
import org.example.interview.sub.coroutine.Coroutine1

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    val coroutine = Coroutine1()
    val scope: CoroutineScope = GlobalScope
    scope.launch(Dispatchers.IO) {
        println("label 0")
        coroutine.checkLogin().apply { println("checkLogin: $this") }
        println("label 1")
        coroutine.login().apply { println("login: $this") }
        println("label 2")
        coroutine.getId().apply { println("getId: $this") }
        println("label 3")
    }
    Thread.sleep(5_000)
}