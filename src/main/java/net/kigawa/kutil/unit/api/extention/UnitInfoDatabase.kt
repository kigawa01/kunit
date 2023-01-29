package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.api.component.UnitInfo
import net.kigawa.kutil.unit.component.UnitIdentify

interface UnitInfoDatabase {
  fun register(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions): Boolean
  fun unregister(unitInfo: UnitInfo<out Any>)
  fun identifyList(): List<UnitIdentify<out Any>>
  
  /**
   * find info by equal class and name
   */
  fun <T: Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>>
}