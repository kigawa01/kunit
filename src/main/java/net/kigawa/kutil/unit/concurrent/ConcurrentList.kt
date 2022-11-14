package net.kigawa.kutil.unit.concurrent

class ConcurrentList<T : Any> {
    private val factories = mutableSetOf<T>()

    @Synchronized
    fun last(predicate: (T) -> Boolean): T {
        return factories.last(predicate)
    }

    @Synchronized
    fun add(unitFactory: T) {
        factories.add(unitFactory)
    }

    @Synchronized
    fun remove(factoryClass: Class<out T>) {
        factories.removeIf { it.javaClass == factoryClass }
    }

    @Synchronized
    fun filter(predicate: (T) -> Boolean): List<T> {
        return factories.filter(predicate)
    }
}