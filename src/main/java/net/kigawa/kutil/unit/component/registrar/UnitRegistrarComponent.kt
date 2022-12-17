package net.kigawa.kutil.unit.component.registrar

import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registrar.*

interface UnitRegistrarComponent {
  fun addRegistrar(registrar: Class<out UnitRegistrar>)
  fun removeRegistrar(registrar: Class<out UnitRegistrar>)
  fun registerUnits(registrarInfo: UnitRegistrarInfo<*>)
  
  fun unregisterUnits(registrarInfo: UnitRegistrarInfo<*>)
  
  fun registerUnit(unitClass: Class<*>, name: String?) {
    registerUnits(
      SingleRegistrarInfo(UnitIdentify(unitClass, name))
    )
  }
  
  fun unregisterUnit(unitClass: Class<*>, name: String?) {
    unregisterUnit(UnitIdentify(unitClass, name))
  }
  
  fun unregisterUnit(identify: UnitIdentify<*>) {
    unregisterUnits(
      SingleRegistrarInfo(identify)
    )
  }
  
  fun registerUnit(unitClass: Class<*>) {
    return registerUnit(unitClass, null)
  }
  
  fun unregisterUnit(unitClass: Class<*>) {
    return unregisterUnit(unitClass, null)
  }
}