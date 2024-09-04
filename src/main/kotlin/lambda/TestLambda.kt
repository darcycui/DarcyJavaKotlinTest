package org.example.lambda

fun main() {
    TestLambda().test()
}

class TestLambda {
    fun lambdaWrapper(lambda: (Int, Int) -> Int): Int {
        return lambda.invoke(1, 2)
    }

    /**
     * [receiver] 带有接收者的函数
     */
    fun receiverWrapper(receiver: Int.(a: Int, b: Int) -> Float): Double {
        // 接收者既可以放在括号里面作为第一个参数
//        return receiver.invoke(1, 2, 3).toDouble()
        // 接收者也可以放在括号前面
        return 1.receiver(2, 3).toDouble()
    }

    /**
     * [receiver] 带有接收者的函数2
     */
    fun receiverWrapper2(receiver: Int.(a: Int, b: Int, close: () -> Unit) -> Float): Double {
//        return receiver.invoke(10, 20, 30) {
//            println("close running")
//        }.toDouble()
        val num: Int = 100
        return num.receiver(200, 300) {
            println("close running2")
        }.toDouble()
    }

    fun test() {
        val result = lambdaWrapper { a, b ->
            a + b
        }
        println("result=$result")

        val result2 = receiverWrapper { a, b ->
            // 带有接收者的函数字面值 字面值内部的this就是接收者(this可以省略)
            this.plus(a + b).toFloat()
        }
        println("result2=$result2")

        val result3 = receiverWrapper2 { a, b, close ->
            // 这里调用 close
            close.invoke()
            this.plus(a + b).toFloat()
        }
        println("result3=$result3")
    }
}