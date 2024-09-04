package cache.impl

import cache.ICache
import java.util.*

/**
 * FIFO(First In First Out) Cache
 */
class FIFOCache<K, V> : ICache<K, V> {
    private val queue: LinkedList<V> = LinkedList()
    override fun put(key: K, value: V) {
        synchronized(FIFOCache::class) {
            queue.offer(value)
        }
    }

    override fun get(key: K): V? {
        synchronized(FIFOCache::class) {
            return queue.poll()
        }
    }

    override fun remove(key: K) {
        synchronized(FIFOCache::class) {
            queue.removeFirst()
        }
    }

    override fun clear() {
        synchronized(FIFOCache::class) {
            queue.clear()
        }
    }
}