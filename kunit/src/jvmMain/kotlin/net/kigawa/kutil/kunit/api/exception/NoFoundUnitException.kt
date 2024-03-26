package net.kigawa.kutil.kunit.api.exception

import net.kigawa.kutil.kunit.api.UnitIdentify

class NoFoundUnitException(
  message: String?,
  cause: Throwable? = null,
  identify: UnitIdentify<out Any>,
) : UnitException(message, cause, identify)