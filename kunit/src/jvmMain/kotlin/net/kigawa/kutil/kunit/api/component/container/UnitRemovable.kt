package net.kigawa.kutil.kunit.api.component.container

import net.kigawa.kutil.kunit.api.UnitIdentify

interface UnitRemovable {
  
  fun removeUnit(unitClass: Class<out Any>) {
    removeUnit(unitClass, null)
  }
  
  fun removeUnit(unitClass: Class<out Any>, name: String?) {
    removeUnit(UnitIdentify(unitClass, name))
  }
  
  fun removeUnit(identify: UnitIdentify<out Any>)
}