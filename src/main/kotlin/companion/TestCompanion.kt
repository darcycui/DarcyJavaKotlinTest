package org.example.companion

fun main() {
    val testCompanion = TestCompanion.getInstance()
    testCompanion.test()
    testCompanion.log()
    println("id: ${testCompanion.id}")
    // TODO 对拓展属性赋值不起作用
    testCompanion.id = "id-200"
    println("id: ${testCompanion.id}")
    // 调用伴生对象的拓展方法
    TestCompanion.hello()

}

/**
 * 测试伴生对象
 */
class TestCompanion private constructor() {
    var address: String = ""
        get() = "$field-"
        set(value) {
            field = "-$value"
        }

    companion object {
        var name = "Tom"

        // 不生成 get/set方法
        @JvmField
        val ids = 101
        fun getInstance(): TestCompanion {
            return TestCompanion()
        }

        // java静态方法
        @JvmStatic
        fun jvmFunc() {
            println("jvmFunc")
        }
    }

    fun test() {
        println("name: $name")
    }
}
