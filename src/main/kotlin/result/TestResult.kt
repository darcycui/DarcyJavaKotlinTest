package result
import exts.logD

import dispatch

fun main() {
    val testClass = TestResult()
    var result = true
    var upload = true
    var update = false

    // 1.测试Result基本操作
//    val result: Result<String> = testClass.getResult(result)
//    result.onSuccess {
//        logD(message = "success: $it")
//    }.onFailure {
//        logD(message = "error: $it")
//    }.getOrDefault("default")
//    logD(message = "result=$result")

    // 2.测试Result串行操作--TODO 推荐使用
//    testClass.uploadHeader(upload)
//        .andThen { testClass.updateUserInfo(update) }
//        .onSuccess {
//            logD(message = "addThen success: $it")
//        }
//        .onFailure {
//            logD(message = "addThen error: $it")
//        }

    // 3.测试Result并行操作
//    zip {
//        val result1 = testClass.uploadHeader(upload)
//        val result2 = testClass.updateUserInfo(update)
//        result1 to result2
//    }.onSuccess {
//        logD(message = "zip success: $it")
//    }.onFailure {
//        logD(message = "zip error: $it")
//    }

    // 4.测试Result分发操作
    testClass.getResult(result).dispatch {
        logD(message = "dispatch: $it")
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