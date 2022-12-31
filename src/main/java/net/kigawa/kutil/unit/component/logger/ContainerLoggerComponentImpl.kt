package net.kigawa.kutil.unit.component.logger

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.logger.*
import java.util.logging.Level

class ContainerLoggerComponentImpl(
  private val container: UnitContainer,
  private val database: ComponentInfoDatabase,
): ContainerLoggerComponent {
  private val loggerClasses = ConcurrentList<Class<out ContainerLogger>>()
  
  init {
    database.registerComponent(ContainerMessageStore())
    loggerClasses.add(ContainerMessageStore::class.java)
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
  
  override fun log(level: Level, message: String, cause: Throwable?, vararg item: Any?) {
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