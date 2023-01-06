package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.api.component.UnitLoggerComponent
import net.kigawa.kutil.unit.api.extention.ComponentInfoDatabase
import net.kigawa.kutil.unit.api.extention.ContainerLogger
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.ContainerStdLogger
import java.util.logging.Level

@LateInit
class UnitLoggerComponentImpl(
  private val container: UnitContainer,
  private val database: ComponentInfoDatabase,
): UnitLoggerComponent {
  private val loggerClasses = ConcurrentList<Class<out ContainerLogger>>()
  
  init {
    database.registerComponent(ContainerStdLogger())
    loggerClasses.add(ContainerStdLogger::class.java)
  }
  
  override fun addLogger(logger: Class<out ContainerLogger>) {
    database.registerComponentClass(logger)
    loggerClasses.add(logger)
  }
  
  override fun removeLogger(logger: Class<out ContainerLogger>) {
    loggerClasses.remove(logger)
    database.unregisterComponent(logger)
  }
  
  override fun log(level: Level, message: String?, cause: Throwable?, vararg item: Any?) {
    loggerClasses.forEach {
      try {
        container.getUnit(it).log(level, message, cause, *item)
      } catch (e: Throwable) {
        removeLogger(it)
        log(level, "", e)
      }
    }
  }
}