package net.kigawa.kutil.unit.extension.logger

import java.util.logging.Level

interface ContainerLogger {
  fun log(level: Level, message: String, cause: Throwable?, vararg item: Any?)
}