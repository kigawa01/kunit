package net.kigawa.kutil.kunit.api.exception

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.InitStack

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