package org.example.proxy

fun main() {
    val testClassProxy = TestClassProxy()
    testClassProxy.test()
}

/**
 * 测试 类委托
 */
class TestClassProxy {
    fun test() {
        val userA = UserA("Tom")
        val userB = UserB(userA)
        userB.info()
    }
}

interface IUser {
    fun info()
}

class UserA(val name: String) : IUser {
    override fun info() {
        println("UserA name: $name")
    }
}

// 这里UserB通过UserA委托实现
class UserB(private val userA: UserA) : IUser by userA {
    override fun info() {
        // 在A的实现之前添加逻辑
        println("UserB--before--")
        userA.info()
        // 在A的实现之后添加逻辑
        println("UserB--after--")
    }
}