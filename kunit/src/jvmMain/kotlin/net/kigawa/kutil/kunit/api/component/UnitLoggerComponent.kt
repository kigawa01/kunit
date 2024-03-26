package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.extention.Message
import net.kigawa.kutil.kunit.api.extention.UnitLogger
import java.util.logging.Level

interface UnitLoggerComponent: ComponentHolder<UnitLogger> {
  fun log(level: Level, message: String?, cause: Throwable?, vararg item: Any?) {
    log(Message(level, message, listOf(cause), listOf(*item)))
  }
  
  fun log(message: Message)
}