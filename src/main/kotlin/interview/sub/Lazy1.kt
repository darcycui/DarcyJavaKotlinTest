package interview.sub

import exts.logD

class Lazy1 {
    private val lazyVal = lazy {
        logD(message = "init lazy")
        "value"
    }

    fun getLazyVal() = lazyVal.value
}

fun main() {
    val bean = Lazy1()
    bean.getLazyVal().apply { logD(message = this) }
    bean.getLazyVal().apply { logD(message = this) }
}