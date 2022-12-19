package net.kigawa.kutil.unit.extension.database

import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitInfoDatabase {
  fun register(unitInfo: UnitInfo<out Any>): Boolean
  fun unregister(unitInfo: UnitInfo<out Any>)
  
  fun identifyList(): MutableList<UnitIdentify<out Any>>
  
  /**
   * find info by class, parents class and name
   */
  fun <T: Any> findOneEquals(identify: UnitIdentify<T>): UnitInfo<T>
  
  /**
   * find info by equal class and name
   */
  fun <T: Any> findByClass(unitClass: Class<T>): MutableList<UnitInfo<T>>
}