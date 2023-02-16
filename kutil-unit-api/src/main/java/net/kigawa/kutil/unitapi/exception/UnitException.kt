package net.kigawa.kutil.unitapi.exception

import net.kigawa.kutil.unitapi.UnitIdentify

open class UnitException(
  override val message: String? = null,
  override val cause: Throwable? = null,
  open val identify: UnitIdentify<out Any>? = null,
): RuntimeException()