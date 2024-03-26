package net.kigawa.kutil.kunit.api.registrar

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.options.RegisterOptions

interface ClassRegistrar {
  fun <T: Any> register(unitClass: Class<T>): T
  fun <T: Any> register(unitClass: Class<T>, name: String?): T
  fun <T: Any> register(unitClass: Class<T>, name: String?, registerOptions: RegisterOptions): T
  fun <T: Any> register(identify: UnitIdentify<T>): T
  fun <T: Any> register(identify: UnitIdentify<T>, registerOptions: RegisterOptions): T
}