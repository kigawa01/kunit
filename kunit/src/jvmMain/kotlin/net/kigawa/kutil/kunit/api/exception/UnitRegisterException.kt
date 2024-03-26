package net.kigawa.kutil.kunit.api.exception

import net.kigawa.kutil.kunit.api.UnitIdentify

class UnitRegisterException(
  message: String,
  identify: UnitIdentify<out Any>,
  cause: Throwable? = null,
) : UnitException(message, cause, identify)