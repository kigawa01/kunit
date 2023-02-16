package net.kigawa.kutil.unitapi.exception

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.RegisterOptions

class UnitStoreException(
  message: String,
  override val identify: UnitIdentify<out Any>,
  val options: RegisterOptions,
  cause: Throwable?,
):
  UnitException(message, cause) {
  constructor(message: String, identify: UnitIdentify<out Any>, options: RegisterOptions): this(
    message,
    identify,
    options,
    null
  )
}