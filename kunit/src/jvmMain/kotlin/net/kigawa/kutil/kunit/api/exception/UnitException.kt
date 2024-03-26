package net.kigawa.kutil.kunit.api.exception

import net.kigawa.kutil.kunit.api.UnitIdentify

open class UnitException(
  message: String? = null,
  cause: Throwable? = null,
  open val identify: UnitIdentify<out Any>? = null,
) : RuntimeException("$message, $identify", cause)