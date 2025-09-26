package org.example.thread.custom.impl

import org.example.thread.CountBean

/**
 * 线程按顺序执行 synchronized + wait + notify实现
 */
class ThreadSynchronizedImpl(
    name: String,
    private val bean: CountBean,
    private val whoCurrent: Int,
    private val whoNext: Int,
    private val lock: Object
) : Thread(name) {
    override fun run() {
        super.run()
        while (true) {
            synchronized(lock) {
                try {
                    if (bean.who == whoCurrent) {
                        bean.count += 1
                        println("Thread is $name: ${bean.count}")
                        Thread.sleep(1000)
                        // 更新who
                        bean.who = whoNext
                        // 唤醒下一个线程
                        lock.notifyAll()// 等待
                    } else {
                        // 等待
                        lock.wait()
                    }

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
}