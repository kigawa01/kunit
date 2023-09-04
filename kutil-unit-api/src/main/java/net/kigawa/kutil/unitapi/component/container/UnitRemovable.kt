package net.kigawa.kutil.unitapi.component.container

import net.kigawa.kutil.unitapi.UnitIdentify

interface UnitRemovable {
  
  fun removeUnit(unitClass: Class<out Any>) {
    removeUnit(unitClass, null)
  }
  
  fun removeUnit(unitClass: Class<out Any>, name: String?) {
    removeUnit(UnitIdentify(unitClass, name))
  }
  
  fun removeUnit(identify: UnitIdentify<out Any>)
}