package net.kigawa.kutil.kunit.api.registrar

import net.kigawa.kutil.kunit.api.UnitIdentify

interface ListRegistrar {
  fun register(identifies: List<UnitIdentify<out Any>>)
}