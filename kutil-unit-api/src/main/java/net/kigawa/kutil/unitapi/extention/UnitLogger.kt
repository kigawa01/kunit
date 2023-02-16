package net.kigawa.kutil.unitapi.extention

import java.util.logging.Level

interface UnitLogger {
  fun log(message: Message)
}

data class Message(val level: Level, val message: String?, val cause: List<Throwable?>, val items: List<Any?>)