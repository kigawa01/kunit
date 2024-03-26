package net.kigawa.kutil.kunit.api.exception

import net.kigawa.kutil.kunit.api.UnitIdentify

class NoFoundFactoryException(
  override val message: String?,
  override val cause: Throwable? = null,
  override val identify: UnitIdentify<out Any>,
): UnitException()