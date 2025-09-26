package org.example.interview.sub.coroutine

class Function11 {
    fun testLambda() {
        val sum = { a: Int, b: Int ->
            a + b
        }
        sum(1, 2)
        sum.invoke(10, 20)
    }
}