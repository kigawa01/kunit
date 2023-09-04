package net.kigawa.kutil.unitapi.exception

import net.kigawa.kutil.unitapi.UnitIdentify

class UnitRegisterException(
  message: String,
  identify: UnitIdentify<out Any>,
  cause: Throwable? = null,
) : UnitException(message, cause, identify)