package net.kigawa.kutil.unitapi.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.RegisterOptions

interface ClassRegistrar {
  fun register(unitClass: Class<out Any>)
  fun register(unitClass: Class<out Any>, name: String?)
  fun register(unitClass: Class<out Any>, name: String?, registerOptions: RegisterOptions)
  fun register(identify: UnitIdentify<out Any>)
  fun register(identify: UnitIdentify<out Any>, registerOptions: RegisterOptions)
}