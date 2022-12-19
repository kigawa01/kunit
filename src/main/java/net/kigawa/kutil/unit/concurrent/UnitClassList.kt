package net.kigawa.kutil.unit.concurrent

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.extension.registrar.ClassRegistrar

class UnitClassList<T: Any>(private val container: UnitContainer) {
  private val registrar = container.getUnit(ClassRegistrar::class.java)
  private val list = ConcurrentList<Class<out T>>()
  
  @Synchronized
  fun last(predicate: (Class<out T>)->Boolean): Class<out T>? {
    return list.last(predicate)
  }
  
  fun add(unitClass: Class<out T>) {
    registrar.register(unitClass)
    list.add(unitClass)
  }
  
  fun remove(unitClass: Class<out T>) {
    list.remove(unitClass)
    container.removeUnit(unitClass)
  }
  
  @Synchronized
  fun filter(predicate: (Class<out T>)->Boolean): List<Class<out T>> {
    return list.filter(predicate)
  }
}