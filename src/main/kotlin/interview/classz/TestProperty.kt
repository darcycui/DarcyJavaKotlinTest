package org.example.interview.classz

data class TestProperty(var school: String) {
    var name: String = "zhangsan"
    var age: Int = 18
    lateinit var city: String
}

fun main() {
    val testProperty = TestProperty("小学")
    println(testProperty.name)
}