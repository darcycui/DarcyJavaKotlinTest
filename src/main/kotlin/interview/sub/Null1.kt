package org.example.interview.sub

object Null1 {
    fun nullable(s: String?) {
        s?.let {
            println("s is: $s")
        }
    }
}

fun main() {
    Null1.nullable(null)
}