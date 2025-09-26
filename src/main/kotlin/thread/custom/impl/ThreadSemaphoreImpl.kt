package org.example.thread.custom.impl

import org.example.thread.CountBean

/**
 * 线程顺序执行 semaphore 信号量实现
 */
class ThreadSemaphoreImpl(
    name: String,
    private val bean: CountBean,
    private val whoCurrent: Int,
    private val whoNext: Int,
) : Thread(name) {
    override fun run() {
        super.run()
        while (true) {
            try {
                val currentSemaphore = bean.semaphoreMap[whoCurrent]
                // 获取信号量
                currentSemaphore?.acquire()
                bean.count += 1
                println("Thread is $name: ${bean.count}")
                Thread.sleep(1000)
                bean.who = whoNext
                // 释放下一个线程的信号量
                bean.semaphoreMap[whoNext]?.release()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}