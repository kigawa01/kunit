package net.kigawa.kutil.unitapi.exception

import net.kigawa.kutil.unitapi.UnitIdentify

class UnitFactoryException(
  message: String? = null,
  cause: Throwable? = null,
  identify: UnitIdentify<out Any>? = null,
) : UnitException(message, cause, identify) {
}