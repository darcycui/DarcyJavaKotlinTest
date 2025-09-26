package org.example.thread.custom.impl

import org.example.thread.CountBean
import java.util.concurrent.locks.ReentrantLock

/**
 * 线程顺序执行  lock + condition实现
 */
class ThreadLockConditionImpl(
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
                    bean.who = whoNext
                    // 唤醒下一个线程
                    bean.conditionMap[whoNext]!!.signalAll()
                } else {
                    // 当前线程等待
                    bean.conditionMap[whoCurrent]!!.await()
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