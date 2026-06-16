package org.example.interview.sub

object String1 {
    fun stringEqual() {
        val str = "hello world"
        val str2 = "hello world"
        val str3 = String("hello world".toCharArray())
        val str4 = StringBuilder("hello world").toString()
        // 比较字符串内容
        logD(message = "str==str2 ${str == str2}") // true
        logD(message = "str==str3 ${str == str3}") // true
        logD(message = "str==str4 ${str == str4}") // true
        // 比较引用地址
        logD(message = "str===str2 ${str === str2}") // true
        logD(message = "str===str3 ${str === str3}") // false
        logD(message = "str===str4 ${str === str4}") // false
    }
}
fun main() {
    String1.stringEqual()
}