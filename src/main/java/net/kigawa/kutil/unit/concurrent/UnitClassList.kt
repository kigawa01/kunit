package net.kigawa.kutil.unit.concurrent

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.extension.registrar.ClassRegistrar

class UnitClassList<T: Any>(private val container: UnitContainer): ConcurrentList<Class<out T>>() {
  private val registrar = container.getUnit(ClassRegistrar::class.java)
  
  override fun add(item: Class<out T>): Boolean {
    registrar.register(item)
    return super.add(item)
  }
  
  override fun remove(item: Class<out T>): Boolean {
    val result = super.remove(item)
    container.removeUnit(item)
    return result
  }
  
  override fun removeLast(): Class<out T> {
    val result = super.removeLast()
    container.removeUnit(result)
    return result
  }
}