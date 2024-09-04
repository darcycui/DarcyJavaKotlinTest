package cache

interface ICache<K, V> {
    fun put(key: K, value: V)
    fun get(key: K): V?

    fun remove(key: K)

    fun clear()
}