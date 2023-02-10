package net.kigawa.kutil.unitapi.registrar

import net.kigawa.kutil.unitapi.UnitIdentify

interface ListRegistrar {
  fun register(identifies: List<UnitIdentify<out Any>>)
}