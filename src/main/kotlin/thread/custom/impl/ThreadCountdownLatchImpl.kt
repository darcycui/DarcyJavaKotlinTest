package org.example.thread.custom.impl

import org.example.thread.CountBean
import java.util.concurrent.CountDownLatch

/**
 * 线程顺序执行  countdownLatch实现
 */
class ThreadCountdownLatchImpl(
    name: String,
    private val bean: CountBean,
    private val whoCurrent: Int,
    private val whoNext: Int,
) : Thread(name) {
    override fun run() {
        super.run()
        while (true) {
            try {
                val countdownLatchWaited = bean.latchMap[whoCurrent]
                // 等待 countdownLatchWaited 减到 0
                countdownLatchWaited!!.await()
                bean.count += 1
                println("Thread is $name count: ${bean.count}")
                Thread.sleep(1000)

                // 创建新的 countdownLatchWaited
                bean.latchMap[whoCurrent] = CountDownLatch(1)

                // countdownLatchCountDown 数值减1
                val countdownLatchCountDown = bean.latchMap[whoNext]
                countdownLatchCountDown!!.countDown()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}