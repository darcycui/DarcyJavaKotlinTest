package org.example.thread

import org.example.thread.custom.impl.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class CountBean {
    @Volatile
    var count = 0L

    @Volatile
    var who = 0 // 用于表示当前线程  0:A, 1:B, 2:C

    // 递减门闩 当减到0时放行等待的线程
    val latchMap: MutableMap<Int, CountDownLatch> =
        mutableMapOf(0 to CountDownLatch(1), 1 to CountDownLatch(1), 2 to CountDownLatch(1))

    // 循环屏障点 等待所有线程到达屏障点
    val cyclicBarrier: CyclicBarrier = CyclicBarrier(3) {
        // 屏障点操作：更新下一个应该执行的线程
        who = (who + 1) % 3
    }

    // lock锁
    val lock = ReentrantLock(false)
    // lock condition条件
    val conditionMap: MutableMap<Int, Condition> =
        mutableMapOf(0 to lock.newCondition(), 1 to lock.newCondition(), 2 to lock.newCondition())

    // 信号量 信号量为0 则线程等待
    val semaphoreMap: MutableMap<Int, Semaphore> =
        mutableMapOf(0 to Semaphore(1, false), 1 to Semaphore(1, false), 2 to Semaphore(1, false))
}

fun main() {
//    testSynchronized()
    testLock()
//    testCountdownLatch()
//    testCyclicBarrier()
//    testLockCondition()
//    testSemaphore()
}

fun testSemaphore() {
    val bean = CountBean()
    val threadA = ThreadSemaphoreImpl("A", bean, 0, 1)
    val threadB = ThreadSemaphoreImpl("B", bean, 1, 2)
    val threadC = ThreadSemaphoreImpl("C", bean, 2, 0)
    threadA.start()
    Thread.sleep(1_00)
    threadB.start()
    Thread.sleep(1_00)
    threadC.start()

    bean.semaphoreMap[0]?.release()
    threadA.join()
    threadB.join()
    threadC.join()
}

fun testLockCondition() {
    val bean = CountBean()
    val lock = bean.lock
    val threadA = ThreadLockConditionImpl("A", bean, 0, 1, lock)
    val threadB = ThreadLockConditionImpl("B", bean, 1, 2, lock)
    val threadC = ThreadLockConditionImpl("C", bean, 2, 0, lock)
    joinAll(threadA, threadB, threadC)

}

fun testCyclicBarrier() {
    val bean = CountBean()
    val threadA = ThreadCyclicBarrierImpl("A", bean, 0)
    val threadB = ThreadCyclicBarrierImpl("B", bean, 1)
    val threadC = ThreadCyclicBarrierImpl("C", bean, 2)
    joinAll(threadA, threadB, threadC)
}

fun testCountdownLatch() {
    val bean = CountBean()
    val threadA = ThreadCountdownLatchImpl("A", bean, 0, 1)
    val threadB = ThreadCountdownLatchImpl("B", bean, 1, 2)
    val threadC = ThreadCountdownLatchImpl("C", bean, 2, 0)
    threadA.start()
    Thread.sleep(1_00)
    threadB.start()
    Thread.sleep(1_00)
    threadC.start()
    bean.latchMap[0]!!.countDown()

    threadA.join()
    threadB.join()
    threadC.join()
}

fun testLock() {
    val bean = CountBean()
    val lock = bean.lock
    val threadA = ThreadLockImpl("A", bean, 0, 1, lock)
    val threadB = ThreadLockImpl("B", bean, 1, 2, lock)
    val threadC = ThreadLockImpl("C", bean, 2, 0, lock)
    joinAll(threadA, threadB, threadC)
}

private fun testSynchronized() {
    val lock = Object()
    val bean = CountBean()
    val threadA = ThreadSynchronizedImpl("A", bean, 0, 1, lock)
    val threadB = ThreadSynchronizedImpl("B", bean, 1, 2, lock)
    val threadC = ThreadSynchronizedImpl("C", bean, 2, 0, lock)
    joinAll(threadA, threadB, threadC)
}

private fun joinAll(
    threadA: Thread,
    threadB: Thread,
    threadC: Thread
) {
    threadA.start()
    Thread.sleep(1_00)
    threadB.start()
    Thread.sleep(1_00)
    threadC.start()

    threadA.join()
    threadB.join()
    threadC.start()
}