package org.example.proxy

fun main() {
    val proxy = TestProxy()
    proxy.test()
}

/**
 * 测试 字段委托给map初始化
 */
class TestProxy {
    fun test() {
        val user = User(mapOf("name" to "Tom", "age" to 8))
        println("name: ${user.name}, age: ${user.age}")
    }
}

class User(map: Map<String, Any>) {
    // name字段的初始化委托给map TODO 使用map内key=name的值进行初始化
    val name: String by map
    val age: Int by map
}