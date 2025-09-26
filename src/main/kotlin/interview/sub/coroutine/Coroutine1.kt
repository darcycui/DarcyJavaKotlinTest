package org.example.interview.sub.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Coroutine1 {
    suspend fun suspendFun() {
        println("out start+++")
        withContext(Dispatchers.IO) {
            println("withContext start")
            delay(1_000)
            println("withContext end")
            "Result String: Hello World!"
        }
        println("out end---")
    }

    suspend fun checkLogin():Boolean {
        return withContext(Dispatchers.IO) {
            println("checkLogin start")
            delay(1_000)
            println("checkLogin end")
            false
        }
    }

    suspend fun login():Boolean {
        return withContext(Dispatchers.IO) {
            println("login start")
            delay(1_000)
            println("login end")
            true
        }
    }

    suspend fun getId():String {
        return withContext(Dispatchers.IO) {
            println("getId start")
            delay(1_000)
            println("getId end")
            "123456"
        }
    }


}