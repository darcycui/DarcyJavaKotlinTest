package org.example.`object`.statement

fun main() {
    val testObjectStatement = TestObjectStatement()
    testObjectStatement.test()
    testObjectStatement.test2()
}

/**
 * 测试对象表达式
 */
class TestObjectStatement {
    // 对象表达式, 将匿名对象赋值给字段 greeting
    private val greeting = object {
        val hello = "Hello"
        val world = "World!"
    }

    fun test() {
        println("-->${greeting.hello} ${greeting.world}")
        // greeting type: class org.example.object.statement.TestObjectStatement$greeting$1
        println("greeting type: ${greeting.javaClass}")
    }

    // 匿名对象实现接口, 一般赋值给私有变量
    private fun getObject() = object : X, Y {
        val name = "Tom"
        override fun funX() {
            println("funX")
        }

        override fun funY() {
            println("funY")
        }
    }

    fun test2() {
        getObject().funX()
        getObject().funY()
        println("getObject()-->" + getObject().name)
    }
}

interface X {
    fun funX() {}
}

interface Y {
    fun funY() {}
}
