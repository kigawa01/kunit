package net.kigawa.kutil.unitapi.component.container

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.exception.NoFoundUnitException
import net.kigawa.kutil.unitapi.options.FindOptions

interface UnitGettable : UnitGetOrNull {

  fun <T : Any> getUnit(unitClass: Class<T>): T {
    return getUnit(unitClass, null)
  }

  fun <T : Any> getUnit(unitClass: Class<T>, findOptions: FindOptions): T {
    return getUnit(unitClass, null, findOptions)
  }

  fun <T : Any> getUnit(unitClass: Class<T>, name: String?): T {
    return getUnit(UnitIdentify(unitClass, name))
  }

  fun <T : Any> getUnit(unitClass: Class<T>, name: String?, findOptions: FindOptions): T {
    return getUnit(UnitIdentify(unitClass, name), findOptions)
  }

  fun <T : Any> getUnit(identify: UnitIdentify<T>): T {
    return getUnit(identify, FindOptions())
  }

  fun <T : Any> getUnit(identify: UnitIdentify<T>, findOptions: FindOptions): T {
    return getUnitOrNull(identify, findOptions) ?: throw NoFoundUnitException("unit is not found", identify = identify)
  }

}