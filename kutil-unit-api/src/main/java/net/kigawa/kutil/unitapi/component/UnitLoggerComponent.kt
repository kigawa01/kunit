package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.extention.UnitLogger
import java.util.logging.Level

interface UnitLoggerComponent: ComponentHolder<UnitLogger> {
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