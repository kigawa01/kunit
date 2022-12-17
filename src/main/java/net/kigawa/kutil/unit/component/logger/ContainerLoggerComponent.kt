package net.kigawa.kutil.unit.component.logger

import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.logger.ContainerLogger
import net.kigawa.kutil.unit.extension.registrar.UnitRegistrarInfo
import java.util.logging.Level

interface ContainerLoggerComponent {
  fun addLogger(logger: UnitRegistrarInfo<out ContainerLogger>)
  fun removeLogger(logger: UnitRegistrarInfo<out ContainerLogger>)
  fun log(level: Level, message: String, cause: Throwable? = null, identify: UnitIdentify<*>? = null)
  fun log(level: Level, message: String, cause: Throwable? = null, unitClass: Class<*>, name: String) {
    log(level, message, cause, UnitIdentify(unitClass, name))
  }
}