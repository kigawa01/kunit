package net.kigawa.kutil.unitimpl.component

import net.kigawa.kutil.unitapi.component.ComponentHolder
import net.kigawa.kutil.unitapi.component.UnitLoggerComponent
import net.kigawa.kutil.unitapi.component.container.UnitContainer
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.Message
import net.kigawa.kutil.unitapi.options.FindOptionEnum
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitimpl.concurrent.ConcurrentList
import net.kigawa.kutil.unitimpl.util.LocaleBuilder
import java.util.*
import java.util.logging.Level

abstract class ComponentHolderImpl<T : Any>(
  private val container: UnitContainer,
  private val database: ComponentDatabase,
  private val loggerComponent: UnitLoggerComponent,
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
      val instance = try {
        container.getUnit(clazz, FindOptions(FindOptionEnum.SKIP_FIND))
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder(Locale.ENGLISH, "could not get component instance").toString(),
            listOf(e),
            listOf(clazz)
          )
        )
        continue
      }
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