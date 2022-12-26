package net.kigawa.kutil.unit.extension.database

import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

interface UnitInfoDatabase {
  fun register(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions): Boolean
  fun unregister(unitInfo: UnitInfo<out Any>)
  fun identifyList(): List<UnitIdentify<out Any>>
  
  /**
   * find info by equal class and name
   */
  fun <T: Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>>
}