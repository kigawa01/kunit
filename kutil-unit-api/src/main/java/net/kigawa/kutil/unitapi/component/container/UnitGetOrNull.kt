package net.kigawa.kutil.unitapi.component.container

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.FindOptions

interface UnitGetOrNull {

  fun <T: Any> getUnitOrNull(unitClass: Class<T>): T? {
    return getUnitOrNull(unitClass, null)
  }

  fun <T: Any> getUnitOrNull(unitClass: Class<T>, findOptions: FindOptions): T? {
    return getUnitOrNull(unitClass, null, findOptions)
  }

  fun <T: Any> getUnitOrNull(unitClass: Class<T>, name: String?): T? {
    return getUnitOrNull(UnitIdentify(unitClass, name))
  }

  fun <T: Any> getUnitOrNull(unitClass: Class<T>, name: String?, findOptions: FindOptions): T? {
    return getUnitOrNull(UnitIdentify(unitClass, name), findOptions)
  }

  fun <T: Any> getUnitOrNull(identify: UnitIdentify<T>): T? {
    return getUnitOrNull(identify, FindOptions())
  }

  fun <T: Any> getUnitOrNull(identify: UnitIdentify<T>, findOptions: FindOptions): T?
}