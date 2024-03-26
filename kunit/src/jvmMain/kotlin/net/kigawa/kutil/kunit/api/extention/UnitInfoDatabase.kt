package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.UnitInfo
import net.kigawa.kutil.kunit.api.options.RegisterOptions

interface UnitInfoDatabase {
  fun register(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions): Boolean
  fun unregister(unitInfo: UnitInfo<out Any>)
  fun identifyList(): List<UnitIdentify<out Any>>
  
  /**
   * find info by equal class and name
   */
  fun <T: Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>>
}