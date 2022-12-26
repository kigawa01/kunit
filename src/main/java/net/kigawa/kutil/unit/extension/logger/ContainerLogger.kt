package net.kigawa.kutil.unit.extension.logger

import java.util.logging.Level

interface ContainerLogger {
  fun log(level: Level, message: String, cause: Throwable?, vararg item: Any?) {
    log(Message(level, message, cause, listOf(*item)))
  }
  
  fun log(message: Message)
}

data class Message(val level: Level, val message: String, val cause: Throwable?, val item: List<Any?>)