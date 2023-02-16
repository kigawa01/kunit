package net.kigawa.kutil.unitapi.exception

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.InitStack

class CircularReferenceException(
  message: String,
  override val identify: UnitIdentify<out Any>,
  val initStack: InitStack,
  cause: Throwable?,
):
  UnitException(message, cause) {
  constructor(message: String, identify: UnitIdentify<out Any>, initStack: InitStack): this(
    message,
    identify,
    initStack,
    null
  )
}