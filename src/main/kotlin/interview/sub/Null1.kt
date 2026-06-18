package org.example.interview.sub

import exts.logD

object Null1 {
    fun nullable(s: String?) {
        s?.let {
            logD(message = "s is: $s")
        }
    }
}

fun main() {
    Null1.nullable(null)
}