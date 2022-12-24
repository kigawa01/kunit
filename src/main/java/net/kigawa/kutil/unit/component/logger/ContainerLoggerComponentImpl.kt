package net.kigawa.kutil.unit.component.logger

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.concurrent.UnitClassList
import net.kigawa.kutil.unit.extension.logger.ContainerLogger
import java.util.logging.Level

class ContainerLoggerComponentImpl(
  private val container: UnitContainer,
): ContainerLoggerComponent {
  private val loggerClasses = UnitClassList<ContainerLogger>(container)
  override fun addLogger(logger: Class<out ContainerLogger>) {
    loggerClasses.addContainer(logger)
  }
  
  override fun removeLogger(logger: Class<out ContainerLogger>) {
    loggerClasses.removeContainer(logger)
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