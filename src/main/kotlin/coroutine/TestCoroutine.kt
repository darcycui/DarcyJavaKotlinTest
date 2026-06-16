package coroutine
import exts.logD

import kotlinx.coroutines.*

fun main() {
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    logD(message = "main start: 线程:${getThreadName()}")
    scope.launch(Dispatchers.Default) {
        logD(message = "default start: 线程:${getThreadName()}")
        delay(3_000)
        logD(message = "default end: 线程:${getThreadName()}")
        scope.launch(Dispatchers.IO) {
            logD(message = "io start: 线程:${getThreadName()}")
            delay((1_000))
            logD(message = "io end: 线程:${getThreadName()}")
        }
    }
    Thread.sleep(10_000)
    logD(message = "main end: 线程:${getThreadName()}")
}

private fun getThreadName(): String? = Thread.currentThread().name