package net.kigawa.kutil.unitapi.exception

class UnitConstructorException(
  override val message: String?,
  override val cause: Throwable? = null,
  @Suppress("UNUSED_PARAMETER") unitClass: Class<out Any>,
): UnitException()