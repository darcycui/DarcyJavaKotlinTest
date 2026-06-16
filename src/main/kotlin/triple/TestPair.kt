package triple
import exts.logD

fun main() {
    TestPair.testPair()
}

class TestPair {
    companion object{
        fun testPair(){
            getWidthHeight()?.let {
                logD(message = "it=$it")
                // 获取Pair的第一个值、第二个值
                logD(message = "width = ${it.first}, height = ${it.second}")
            }
        }

        /**
         * 使用Pair返回两个值
         */
        private fun getWidthHeight(): Pair<Int, Int> {
            return Pair<Int,Int>(10,20)
        }
    }
}