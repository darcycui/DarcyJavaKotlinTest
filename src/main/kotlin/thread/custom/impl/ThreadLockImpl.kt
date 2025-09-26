package org.example.thread.custom.impl

import org.example.thread.CountBean
import java.util.concurrent.locks.ReentrantLock

/**
 * 线程按顺序执行 lock实现
 */
class ThreadLockImpl(
    name: String,
    private val bean: CountBean,
    private val whoCurrent: Int,
    private val whoNext: Int,
    private val lock: ReentrantLock
) : Thread(name) {
    override fun run() {
        super.run()
        while (true) {
            try {
                // 获取锁
                lock.lock()
                if (bean.who == whoCurrent) {
                    bean.count += 1
                    println("Thread is $name: ${bean.count}")
                    Thread.sleep(1000)
                    // 更新who
                    bean.who = whoNext
                } else {
                    // 等待
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                // 释放锁
                lock.unlock()
            }
        }
    }
}