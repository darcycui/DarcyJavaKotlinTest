package org.example.proxy

import java.util.*
import kotlin.properties.ReadWriteProperty
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
        val person = Person(1,"tom","password1")
        println(person) // TOM
        person.name = "jerry"
        person.password = "password2"
        println(person)// JERRY

    }
}

class Person() {
    constructor(id: Int, name: String, passwordInit: String) : this() {
        this.name = name
        this.password = passwordInit
    }

    // TODO name属性委托给 UpperCaseProxy对象
    var name: String by UpperCaseProxy()

    // id属性委托给 lazy函数
    private val id: Int by lazy {
        -1
    }

    // TODO password属性委托给 password函数
    var password: String by password {
        "password default"
    }

    override fun toString(): String {
        return "Person(name='$name', id=$id, password='$password')"
    }

}

/**
 * 属性委托函数
 */
fun <T> password(initializer: () -> T): ReadWriteProperty<Any?, T> {
    // 返回ReadWriteProperty的实现类
    return object : ReadWriteProperty<Any?, T> {
        private var storedValue: T? = null
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return storedValue as T
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            storedValue = value
        }
    }
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