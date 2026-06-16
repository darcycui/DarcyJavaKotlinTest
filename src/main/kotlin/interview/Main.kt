package interview

import kotlinx.coroutines.*
import org.example.interview.sub.coroutine.Coroutine1
import exts.logD

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    val coroutine = Coroutine1()
    val scope: CoroutineScope = GlobalScope
    scope.launch(Dispatchers.IO) {
        logD(message = "label 0")
        coroutine.checkLogin().apply { logD(message = "checkLogin: $this") }
        logD(message = "label 1")
        coroutine.login().apply { logD(message = "login: $this") }
        logD(message = "label 2")
        coroutine.getId().apply { logD(message = "getId: $this") }
        logD(message = "label 3")
    }
    Thread.sleep(5_000)
}