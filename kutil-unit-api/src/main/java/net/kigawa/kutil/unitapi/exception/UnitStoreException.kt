package net.kigawa.kutil.unitapi.exception

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.RegisterOptions

class UnitStoreException(
  message: String,
  identify: UnitIdentify<out Any>,
  val options: RegisterOptions? = null,
  cause: Throwable? = null,
) :
  UnitException(message, cause, identify) {
}