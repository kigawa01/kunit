package net.kigawa.kutil.unit.component.logger

import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.logger.ContainerLogger
import net.kigawa.kutil.unit.extension.registrar.UnitRegistrarInfo
import java.util.concurrent.Callable
import java.util.logging.Level

interface ContainerLoggerComponent {
  fun addLogger(logger: UnitRegistrarInfo<out ContainerLogger>)
  fun removeLogger(logger: UnitRegistrarInfo<out ContainerLogger>)
  fun log(level: Level, message: String, cause: Throwable? = null, identify: UnitIdentify<*>? = null) {
    log(level, message, cause, identify?.unitClass, identify?.name)
  }
  
  fun log(level: Level, message: String, cause: Throwable? = null, vararg item: Any?)
  
  fun <T> catch(
    default: T,
    message: String?,
    vararg item: Any?,
    callable: Callable<T>,
  ): T {
    return try {
      callable.call()
    } catch (e: Throwable) {
      log(Level.WARNING, message ?: "", e, *item)
      default
    }
  }
}