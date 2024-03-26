package net.kigawa.kutil.kunit.api.exception

import net.kigawa.kutil.kunit.api.UnitIdentify

class InjectFinalException(
  message: String? = null,
  cause: Throwable? = null,
  identify: UnitIdentify<out Any>? = null,
) : UnitException(message, cause, identify) {
}