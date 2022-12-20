package net.kigawa.kutil.unit.concurrent

import java.util.*

open class ConcurrentList<T: Any>(vararg item: T) {
  private var list = listOf(*item)
    @Synchronized get() {
      return LinkedList(field)
    }
  
  open fun add(item: T): Boolean {
    return synchronized(this) {
      val l = toMutableList()
      val result = l.add(item)
      list = l
      result
    }
  }
  
  open fun remove(item: T): Boolean {
    return synchronized(this) {
      val l = toMutableList()
      val result = l.remove(item)
      list = l
      result
    }
  }
  
  open fun removeLast(): T {
    return synchronized(this) {
      val l = toMutableList()
      val result = l.removeLast()
      list = l
      result
    }
  }
  
  fun last(predicate: (T)->Boolean): T? {
    for (item in reversed()) {
      if (predicate(item)) return item
    }
    return null
  }
  
  fun first(predicate: (T)->Boolean): T? {
    for (item in list) {
      if (predicate(item)) return item
    }
    return null
  }
  
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
  
  fun contain(predicate: (T)->Boolean): Boolean {
    return first(predicate) != null
  }
}