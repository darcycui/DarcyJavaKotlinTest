package org.example.proxy

import java.util.*
import kotlin.reflect.KProperty

fun main() {
    val testFieldProxy = TestFieldProxy()
    testFieldProxy.test()
}

/**
 * 测试 属性代理
 */
class TestFieldProxy {
    fun test() {
        val person = Person("tom")
        println("person.name=${person.name}") // TOM
        person.name = "jerry"
        println("person.name=${person.name}")// JERRY

    }
}

class Person() {
    constructor(name: String): this() {
        this.name = name
    }
    // TODO name属性委托给 UpperCaseProxy
    var name: String by UpperCaseProxy()
}

/**
 * 属性代理类
 */
class UpperCaseProxy {
    private var value: String = ""

    // 操作符重载 getValue()
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return value.uppercase(Locale.getDefault())

    }

    // 操作符重载 setValue()
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: String) {
        value = newValue.uppercase(Locale.getDefault())
    }
}