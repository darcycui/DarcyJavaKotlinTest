package org.example.interview.sub.coroutine

import exts.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Coroutine1 {
    suspend fun suspendFun() {
        logD(message = "out start+++")
        withContext(Dispatchers.IO) {
            logD(message = "withContext start")
            delay(1_000)
            logD(message = "withContext end")
            "Result String: Hello World!"
        }
        logD(message = "out end---")
    }

    suspend fun checkLogin():Boolean {
        return withContext(Dispatchers.IO) {
            logD(message = "checkLogin start")
            delay(1_000)
            logD(message = "checkLogin end")
            false
        }
    }

    suspend fun login():Boolean {
        return withContext(Dispatchers.IO) {
            logD(message = "login start")
            delay(1_000)
            logD(message = "login end")
            true
        }
    }

    suspend fun getId():String {
        return withContext(Dispatchers.IO) {
            logD(message = "getId start")
            delay(1_000)
            logD(message = "getId end")
            "123456"
        }
    }


}