package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.api.component.UnitLoggerComponent
import net.kigawa.kutil.unit.api.extention.ComponentDatabase
import net.kigawa.kutil.unit.api.extention.UnitLogger
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.UnitStdLogger
import java.util.logging.Level

@LateInit
class UnitLoggerComponentImpl(
  private val container: UnitContainer,
  private val database: ComponentDatabase,
): UnitLoggerComponent {
  private val loggerClasses = ConcurrentList<Class<out UnitLogger>>()
  
  init {
    database.registerComponent(UnitStdLogger())
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
  
  override fun log(level: Level, message: String?, cause: Throwable?, vararg item: Any?) {
    loggerClasses.forEach {
      try {
        container.getUnit(it).log(level, message, cause, *item)
      } catch (e: Throwable) {
        remove(it)
        log(level, "", e)
      }
    }
  }
}