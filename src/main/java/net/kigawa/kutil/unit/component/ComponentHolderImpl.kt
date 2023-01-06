package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.api.component.ComponentHolder
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase

abstract class ComponentHolderImpl<T: Any>(
  private val container: UnitContainer,
  private val database: ComponentInfoDatabase,
): ComponentHolder<T> {
  protected val classes = ConcurrentList<Class<out T>>()
  override fun add(clazz: Class<out T>) {
    database.registerComponentClass(clazz)
    classes.add(clazz)
  }
  
  override fun remove(clazz: Class<out T>) {
    classes.remove(clazz)
    database.unregisterComponent(clazz)
  }
  
  fun last(predicate: (T)->Boolean) {
    classes.last {
      predicate(container.getUnit(it))
    }
  }
  
  fun <R: Any> lastMap(transform: (T)->R?): R? {
    for (clazz in classes.reversed()) {
      return transform(container.getUnit(clazz)) ?: continue
    }
    return null
  }
  
  fun unitForEach(action: (T)->Unit) {
    classes.forEach {
      action(container.getUnit(it))
    }
  }
}