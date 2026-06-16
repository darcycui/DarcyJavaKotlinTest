package coroutine
import exts.logD

import kotlinx.coroutines.*

fun main() {
    val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        logD(message = "发生异常：$throwable")
    }
    // supervisorJob() 和 exception要同时使用
    // supervisorJob() 只保证scope创建的子协程相互独立 不能保证孙协程
    // 孙协程要使用 supervisorScope{} 函数包裹
    val scope: CoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
//                + exceptionHandler
    )
    runBlocking {
        val scopeJob = scope.launch {
            logD(message = "scope1 开始")
            supervisorScope {
                launch {
                    logD(message = "子协程a开始")
                    delay(1_00)
                    val a = 1 / 0 // 模拟未捕获的异常
                    delay(1_000)
                    logD(message = "子协程a结束")
                }
                launch {
                    logD(message = "子协程b开始")
                    delay(2_000)
                    logD(message = "子协程b结束")
                }
            }
            delay(1_000)
            logD(message = "scope1 结束")
        }
        scopeJob.join()

        val scopeJob2 = scope.launch {
            logD(message = "子协程c开始")
            delay(3_000)
            logD(message = "子协程c结束")
        }
        scopeJob2.join()
//        delay(1500)
//        scopeJob.cancel()
    }

}