package net.kigawa.kutil.unit.component.getter

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitGetterComponent {
  fun <T> registerUnit(identify: UnitIdentify<T>)
  fun <T> getUnit(identify: UnitIdentify<T>): T
  fun <T> getUnit(unitClass: Class<T>, name: String?): T {
    return getUnit(UnitIdentify(unitClass, name))
  }
  
  fun <T> getUnit(unitClass: Class<T>): T {
    return getUnit(UnitIdentify(unitClass, null))
  }
}