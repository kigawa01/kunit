package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitLoggerComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.Message
import net.kigawa.kutil.kunit.api.extention.UnitLogger
import net.kigawa.kutil.kunit.impl.concurrent.ConcurrentList
import net.kigawa.kutil.kunit.impl.extension.UnitStdLogger
import java.util.logging.Level

@LateInit
class UnitLoggerComponentImpl(
  private val container: UnitContainer,
  private val database: ComponentDatabase,
): UnitLoggerComponent {
  private val loggerClasses = ConcurrentList<Class<out UnitLogger>>()
  
  init {
    database.registerComponent(UnitStdLogger(), null)
    loggerClasses.add(UnitStdLogger::class.java)
  }
  
  override fun add(clazz: Class<out UnitLogger>) {
    database.registerComponentClass(clazz)
    loggerClasses.add(clazz)
  }
  
  override fun remove(clazz: Class<out UnitLogger>) {
    loggerClasses.remove(clazz)
    database.unregisterComponent(clazz)
  }
  
  override fun log(message: Message) {
    loggerClasses.forEach {
      try {
        container.getUnit(it).log(message)
      } catch (e: Throwable) {
        remove(it)
        log(Level.WARNING, "logger thrown exception", e)
      }
    }
  }
}