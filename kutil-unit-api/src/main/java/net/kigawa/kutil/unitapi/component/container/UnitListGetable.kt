package net.kigawa.kutil.unitapi.component.container

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.FindOptions

interface UnitListGetable {
  
  fun <T: Any> getUnitList(identify: UnitIdentify<T>, findOptions: FindOptions): List<T>
  fun <T: Any> getUnitList(identify: UnitIdentify<T>): List<T> {
    return getUnitList(identify, FindOptions())
  }
  
  fun <T: Any> getUnitList(unitClass: Class<T>): List<T> {
    return getUnitList(unitClass, null)
  }
  
  fun <T: Any> getUnitList(unitClass: Class<T>, name: String?): List<T> {
    return getUnitList(UnitIdentify(unitClass, name))
  }
}