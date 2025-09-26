package org.example.thread.custom.impl

import org.example.thread.CountBean
import java.util.concurrent.CountDownLatch

/**
 * 线程顺序执行 cyclicBarrier实现
 */
class ThreadCyclicBarrierImpl(
    name: String,
    private val bean: CountBean,
    private val whoCurrent: Int,
) : Thread(name) {
    override fun run() {
        super.run()
        while (true) {
            try {
                if (bean.who == whoCurrent) {
                    bean.count += 1
                    println("Thread is $name count: ${bean.count}")
                    Thread.sleep(1000)
                    val barrierWaited = bean.cyclicBarrier
                    // 等待所有线程到达屏障点
                    barrierWaited.await()
                } else {
                    // 等待
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}