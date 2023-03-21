package net.kigawa.kutil.unitapi.exception

import net.kigawa.kutil.unitapi.UnitIdentify

class NoFoundFactoryException(
  override val message: String?,
  override val cause: Throwable? = null,
  override val identify: UnitIdentify<out Any>,
): UnitException()