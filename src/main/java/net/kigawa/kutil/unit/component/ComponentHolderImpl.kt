package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.api.component.ComponentHolder
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase

abstract class ComponentHolderImpl<T: Any>(
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
}