package org.example.result

import andThen
import dispatch
import zip

fun main() {
    val testClass = TestResult()
    var result = true
    var upload = true
    var update = false

    // 1.测试Result基本操作
//    val result: Result<String> = testClass.getResult(result)
//    result.onSuccess {
//        println("success: $it")
//    }.onFailure {
//        println("error: $it")
//    }.getOrDefault("default")
//    println("result=$result")

    // 2.测试Result串行操作--TODO 推荐使用
//    testClass.uploadHeader(upload)
//        .andThen { testClass.updateUserInfo(update) }
//        .onSuccess {
//            println("addThen success: $it")
//        }
//        .onFailure {
//            println("addThen error: $it")
//        }

    // 3.测试Result并行操作
//    zip {
//        val result1 = testClass.uploadHeader(upload)
//        val result2 = testClass.updateUserInfo(update)
//        result1 to result2
//    }.onSuccess {
//        println("zip success: $it")
//    }.onFailure {
//        println("zip error: $it")
//    }

    // 4.测试Result分发操作
    testClass.getResult(result).dispatch {
        println("dispatch: $it")
    }
}

class TestResult {
    // TODO 使用Result 封装正常返回值和异常
    fun getResult(success: Boolean): Result<String> {
        if (success) {
            // 泛型String可以省略
            return Result.success<String>("result success")
        }
        // 泛型String可以省略
        return Result.failure<String>(Exception("Error: test Exception"))
    }

    fun uploadHeader(upload: Boolean): Result<String> {
        if (upload) {
            return Result.success("image url")
        }
        return Result.failure(Exception("upload error"))
    }

    fun updateUserInfo(update: Boolean): Result<Boolean> {
        if (update) {
            return Result.success(true)
        }
        return Result.failure(Exception("update error"))
    }
}