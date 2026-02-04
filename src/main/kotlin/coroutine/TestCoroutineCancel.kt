package org.example.coroutine

import kotlinx.coroutines.*

fun main() {
    val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("发生异常：$throwable")
    }
    val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)
    runBlocking {
        val scopeJob1 = scope.launch {
            launch {
                kotlin.runCatching {
                    println("子协程a开始")
                    delay(1_000) // delay()函数内部可以检测协程取消状态 抛出JobCancellationException
//                    ensureActive()
                    println("子协程a结束")
                }.onFailure {
                    println("协程a异常：${it::class}")
                }
            }
            launch {
                kotlin.runCatching {
                    println("子协程b开始")
                    while (isActive) { // 使用 isActive 检测协程取消状态 默认不抛出异常

                    }
                    println("子协程b结束")
                }.onFailure {
                    println("协程b异常：${it::class}")
                }
            }
            launch {
                kotlin.runCatching {
                    println("子协程x开始")
                    while (true) {
                        ensureActive() // 使用 ensureActive()函数 检测携程取消状态 抛出JobCancellationException
                    }
                    println("子协程x结束")

                }.onFailure {
                    println("协程x异常：${it::class}")
                }
            }
        }

        val scopeJob2 = scope.launch {
            launch {
                kotlin.runCatching {
                    println("子协程c开始")
                    delay(3_000)
                    ensureActive()
                    println("子协程c结束")
                }.onFailure {
                    println("协程c异常：${it::class}") //
                }
            }
        }
        delay(1_00)
        println("取消scopeJob1")
        scopeJob1.cancel()
//        scopeJob1.join()
        scopeJob2.join()
    }
}