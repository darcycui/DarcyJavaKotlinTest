package exts

private const val TAG = "DarcyLog"

fun logD(tag: String = TAG, message: String) {
    println("$tag $message")
}

fun logI(tag: String = TAG, message: String) {
    println("$tag $message")
}

fun logW(tag: String = TAG, message: String) {
    println("$tag $message")
}

fun logV(tag: String = TAG, message: String) {
    println("$tag $message")
}

fun logE(tag: String = TAG, message: String) {
    println("$tag $message")
}

fun logE(tag: String = TAG, message: String, exception: Exception) {
    println("$tag $message ${exception::class::simpleName} ${exception.message}")
}