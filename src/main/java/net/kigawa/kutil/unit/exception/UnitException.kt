package net.kigawa.kutil.unit.exception

import net.kigawa.kutil.unit.util.Util

open class UnitException: RuntimeException {
  constructor(message: String, cause: Throwable?, vararg obj: Any?): super(
    Util.createLogMessage(message, obj.toList()),
    cause
  )
  
  constructor(message: String, vararg obj: Any?): this(message, null, *obj)
}