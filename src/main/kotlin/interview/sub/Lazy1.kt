package org.example.interview.sub

class Lazy1 {
    private val lazyVal = lazy {
        println("init lazy")
        "value"
    }

    fun getLazyVal() = lazyVal.value
}

fun main() {
    val bean = Lazy1()
    bean.getLazyVal().apply { println(this) }
    bean.getLazyVal().apply { println(this) }
}