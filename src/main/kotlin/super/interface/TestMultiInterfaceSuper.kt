package `super`.`interface`
import exts.logD

fun main() {
    val testMultiInterfaceSuper = TestMultiInterfaceSuper()
    testMultiInterfaceSuper.fun1()
}

/**
 * 测试 多继承 调用父类方法
 */
class TestMultiInterfaceSuper : A, B {
    override fun fun1() {
        // TODO 调用父类A的方法
        super<A>.fun1()
        // TODO 调用父类B的方法
        super<B>.fun1()
        logD(message = "funMulti-->fun1")
    }
}

interface A {
    fun fun1() {
        logD(message = "fun1 from A")
    }
}

interface B {
    fun fun1() {
        logD(message = "fun1 from B")
    }
}