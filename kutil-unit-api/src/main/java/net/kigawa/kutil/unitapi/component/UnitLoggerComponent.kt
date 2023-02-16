package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.extention.Message
import net.kigawa.kutil.unitapi.extention.UnitLogger
import java.util.logging.Level

interface UnitLoggerComponent: ComponentHolder<UnitLogger> {
  fun log(level: Level, message: String?, cause: Throwable?, vararg item: Any?) {
    log(Message(level, message, listOf(cause), listOf(*item)))
  }
  
  fun log(message: Message)
}