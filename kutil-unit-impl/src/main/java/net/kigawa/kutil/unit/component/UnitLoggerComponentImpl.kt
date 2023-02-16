package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.UnitStdLogger
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.component.UnitLoggerComponent
import net.kigawa.kutil.unitapi.extention.*
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