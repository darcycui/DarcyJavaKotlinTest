package org.example.interview.sub.coroutine

object CPS {
    fun addNormal(a: Int, b: Int): Int {
        logD(message = "普通方法")
        return a + b
    }

    /**
     * 继续体(可以理解为回调)
     */
    interface Continuation {
        fun resumeWith(value: Int)
    }

    fun addCPS(a: Int, b: Int, continuation: Continuation) {
        logD(message = "CPS风格方法")
        // continuation 类似回调
        continuation.resumeWith(a + b)
    }
}

fun main() {
    val result = CPS.addNormal(1, 2)
    logD(message = "result1=$result")

    CPS.addCPS(1, 2, object : CPS.Continuation {
        override fun resumeWith(value: Int) {
            logD(message = "result2=$value")
        }
    })
}