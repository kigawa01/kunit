package net.kigawa.kutil.kunit.api.exception

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.options.RegisterOptions

class UnitStoreException(
  message: String,
  identify: UnitIdentify<out Any>,
  val options: RegisterOptions? = null,
  cause: Throwable? = null,
) :
  UnitException(message, cause, identify) {
}