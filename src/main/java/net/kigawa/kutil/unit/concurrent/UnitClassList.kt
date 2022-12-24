package net.kigawa.kutil.unit.concurrent

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.extension.registrar.ClassRegistrar

class UnitClassList<T: Any>(
  private val container: UnitContainer,
): ConcurrentList<Class<out T>>() {
  private var registrar: ClassRegistrar? = null
  fun getRegistrar(): ClassRegistrar {
    var result = registrar
    if (result == null) {
      result = container.getUnit(ClassRegistrar::class.java)
      registrar = result
    }
    return result
  }
  
  fun addContainer(item: Class<out T>): Boolean {
    registrar.register(item)
    return super.add(item)
  }
  
  fun removeContainer(item: Class<out T>): Boolean {
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