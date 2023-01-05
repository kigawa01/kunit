package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.extension.logger.ContainerLogger
import java.util.logging.Level

interface ContainerLoggerComponent {
  fun addLogger(logger: Class<out ContainerLogger>)
  fun removeLogger(logger: Class<out ContainerLogger>)
  fun log(level: Level, message: String?, cause: Throwable? = null, vararg item: Any?)
  
  fun <T> catch(default: T, vararg item: Any?, callable: ()->T): T {
    return catch(default, null, *item, callable = callable)
  }
  
  fun <T> catch(default: T, message: String?, vararg item: Any?, callable: ()->T): T {
    return try {
      callable.invoke()
    } catch (e: Throwable) {
      log(Level.WARNING, message, e, *item)
      default
    }
  }
}