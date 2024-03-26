package net.kigawa.kutil.kunit.api.exception

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.UnitInfo

class NoSingleUnitException(
  override val message: String?,
  override val cause: Throwable? = null,
  @Suppress("UNUSED_PARAMETER") identify: UnitIdentify<out Any>,
  @Suppress("UNUSED_PARAMETER") units: List<Any>? = null,
  @Suppress("UNUSED_PARAMETER") infoList: List<UnitInfo<out Any>>? = null,
): UnitException()