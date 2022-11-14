package net.kigawa.kutil.unit.concurrent

class ConcurrentList<T : Any> {
    private val list = mutableSetOf<T>()

    @Synchronized
    fun last(predicate: (T) -> Boolean): T {
        return list.last(predicate)
    }

    @Synchronized
    fun add(unitFactory: T) {
        list.add(unitFactory)
    }

    @Synchronized
    fun remove(factoryClass: Class<out T>) {
        list.removeIf { it.javaClass == factoryClass }
    }

    @Synchronized
    fun filter(predicate: (T) -> Boolean): List<T> {
        return list.filter(predicate)
    }
}