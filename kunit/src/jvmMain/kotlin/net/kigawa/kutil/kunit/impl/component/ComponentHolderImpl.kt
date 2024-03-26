package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.component.ComponentHolder
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.options.FindOptionEnum
import net.kigawa.kutil.kunit.api.options.FindOptions
import net.kigawa.kutil.kunit.impl.concurrent.ConcurrentList

abstract class ComponentHolderImpl<T : Any>(
  private val container: UnitContainer,
  private val database: ComponentDatabase,
) : ComponentHolder<T> {
  protected val classes = ConcurrentList<Class<out T>>()
  override fun add(clazz: Class<out T>) {
    database.registerComponentClass(clazz)
    classes.add(clazz)
  }

  override fun remove(clazz: Class<out T>) {
    classes.remove(clazz)
    database.unregisterComponent(clazz)
  }

  fun last(predicate: (T) -> Boolean) {
    classes.last {
      predicate(container.getUnit(it))
    }
  }

  fun <R : Any> lastMap(transform: (T) -> R?): R? {
    for (clazz in classes.reversed()) {
      val instance = container.getUnit(clazz, FindOptions(FindOptionEnum.SKIP_FIND))
      return transform(instance) ?: continue
    }
    return null
  }

  fun forEach(action: (T) -> Unit) {
    classes.forEach {
      action(container.getUnit(it))
    }
  }
}