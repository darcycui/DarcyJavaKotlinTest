package interview.classz
import exts.logD

data class TestProperty(var school: String) {
    var name: String = "zhangsan"
    var age: Int = 18
    lateinit var city: String
}

fun main() {
    val testProperty = TestProperty("小学")
    logD(message = testProperty.name)
}