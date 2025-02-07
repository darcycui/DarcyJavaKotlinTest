package future

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.stream.Collectors

fun main() {
    testSerial()
//    testParallel()
}

fun testParallel() {
    // 创建一个固定大小的线程池
    val executorService: ExecutorService = Executors.newFixedThreadPool(3)
    // 假设我们有一个任务列表，每个任务返回一个字符串
    val tasks: List<() -> String> = listOf(
        { "Result of Task 1" },
        { "Result of Task 2" },
        { "Result of Task 3" },
        { "Result of Task 4" },
        { "Result of Task 5" }
    )
    // 并行执行任务
    val allFutures = tasks.stream()
        .map { task ->
            CompletableFuture.supplyAsync({
                task().also {
                    println("Executing task parallel: $it")
                } // 执行任务并返回结果
            }, executorService)
        }.collect(Collectors.toList())

    CompletableFuture.allOf(*allFutures.toTypedArray()).thenApply {
        for (future in allFutures) {
            println("Result: ${future.get()}")
        }
    }.thenAccept {
        println("All tasks completed.")
    }
    // 关闭线程池
    executorService.shutdown()
}

private fun testSerial() {
    // 创建一个固定大小的线程池
    val executorService: ExecutorService = Executors.newFixedThreadPool(3)

    // 假设我们有一个任务列表，每个任务返回一个字符串
    val tasks: List<() -> String> = listOf(
        { "Result of Task 1" },
        { "Result of Task 2" },
        { "Result of Task 3" },
        { "Result of Task 4" },
        { "Result of Task 5" }
    )
    // 创建一个初始的 CompletableFuture
    var finalFuture: CompletableFuture<String> = CompletableFuture.completedFuture(null)
    // 顺序执行任务
    for (task in tasks) {
        finalFuture = finalFuture.thenComposeAsync { prevResult ->
            CompletableFuture.supplyAsync({
                task().also {
                    println("prevResult=$prevResult")
                    println("Executing task serial: $it")
                } // 执行任务并返回结果
            }, executorService)
        }
    }
    // 等待所有任务完成
    finalFuture.join()
    // 关闭线程池
    executorService.shutdown()
}
