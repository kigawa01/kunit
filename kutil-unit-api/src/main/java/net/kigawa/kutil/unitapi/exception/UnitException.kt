package net.kigawa.kutil.unitapi.exception

import net.kigawa.kutil.unitapi.util.Util

open class UnitException: RuntimeException {
  constructor(message: String, cause: Throwable?, vararg obj: Any?): super(
    Util.createMessage(message, obj.toList()),
    cause
  )
  
  constructor(message: String, vararg obj: Any?): this(message, null, *obj)
}