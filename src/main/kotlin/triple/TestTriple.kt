package triple
import exts.logD

fun main() {
    TestTriple.testTriple()
}

class TestTriple {
    companion object {
        fun testTriple(){
            getLengthWidthHeight()?.let {
                logD(message = "$it")
                // 获取第一个值
                logD(message = "length=${it.first} width=${it.second} height=${it.third}")
            }
        }
        /**
         * 使用Triple返回三个值
         */
        private fun getLengthWidthHeight(): Triple<Int, Int, Int> {
            return Triple<Int,Int,Int>(100,200,300)
        }
    }
}