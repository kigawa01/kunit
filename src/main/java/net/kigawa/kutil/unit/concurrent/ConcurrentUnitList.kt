package net.kigawa.kutil.unit.concurrent

import net.kigawa.kutil.unit.componate.container.UnitContainer

class ConcurrentUnitList<T: Any>(private val container: UnitContainer) {
  private val list = mutableSetOf<T>()
  
  @Synchronized
  fun last(predicate: (T)->Boolean): T? {
    return list.lastOrNull(predicate)
  }
  
  fun add(unit: T, name: String?) {
    synchronized(this) {
      list.add(unit)
    }
    container.addUnit(unit, name)
  }
  
  fun remove(unitClass: Class<out T>, name: String?): MutableList<Throwable> {
    val unitList = container.getUnitList(unitClass, name)
    unitList.forEach {
      synchronized(this) {
        list.remove(it)
      }
    }
    return container.removeUnit(unitClass, name)
  }
  
  @Synchronized
  fun filter(predicate: (T)->Boolean): List<T> {
    return list.filter(predicate)
  }
}