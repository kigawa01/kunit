package net.kigawa.kutil.unitapi.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.RegisterOptions

interface ClassRegistrar {
  fun <T: Any> register(unitClass: Class<T>): T
  fun <T: Any> register(unitClass: Class<T>, name: String?): T
  fun <T: Any> register(unitClass: Class<T>, name: String?, registerOptions: RegisterOptions): T
  fun <T: Any> register(identify: UnitIdentify<T>): T
  fun <T: Any> register(identify: UnitIdentify<T>, registerOptions: RegisterOptions): T
}