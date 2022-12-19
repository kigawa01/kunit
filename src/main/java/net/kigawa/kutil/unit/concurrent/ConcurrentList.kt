package net.kigawa.kutil.unit.concurrent

import java.util.*

class ConcurrentList<T: Any> {
  private var list = listOf<T>()
    @Synchronized get() {
      return LinkedList(field)
    }
  
  fun last(predicate: (T)->Boolean): T? {
    for (item in reversed()) {
      if (predicate(item)) return item
    }
    return null
  }
  
  fun add(item: T): Boolean {
    return synchronized(this) {
      val l = toMutableList()
      val result = l.add(item)
      list = l
      result
    }
  }
  
  fun remove(item: T): Boolean {
    return synchronized(this) {
      val l = toMutableList()
      val result = l.remove(item)
      list = l
      result
    }
  }
  
  @Synchronized
  fun filter(predicate: (T)->Boolean): List<T> {
    return list.filter(predicate)
  }
  
  fun reversed(): List<T> {
    return list.reversed()
  }
  
  fun toMutableList(): MutableList<T> {
    return list.toMutableList()
  }
  
  fun forEach(action: (T)->Unit) {
    list.forEach(action)
  }
  
  fun <R> flatMap(transform: (T)->Iterable<R>): List<R> {
    return list.flatMap(transform)
  }
}