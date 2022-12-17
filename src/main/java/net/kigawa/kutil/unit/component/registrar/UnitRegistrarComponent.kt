package net.kigawa.kutil.unit.component.registrar

import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registrar.*

interface UnitRegistrarComponent {
  fun addRegistrar(registrar: Class<out UnitRegistrar>)
  fun removeRegistrar(registrar: Class<out UnitRegistrar>)
  fun registerUnits(registrarInfo: UnitRegistrarInfo<*>): MutableList<Throwable>
  
  fun unregisterUnits(registrarInfo: UnitRegistrarInfo<*>): MutableList<Throwable>
  
  fun registerUnit(unitClass: Class<*>, name: String?) {
    registerUnits(
      SingleRegistrarInfo(
        UnitIdentify(unitClass, name),
      )
    ).forEach {throw UnitException("could not register unit", it)}
  }
  
  fun unregisterUnit(unitClass: Class<*>, name: String?) {
    unregisterUnit(UnitIdentify(unitClass, name))
  }
  
  fun unregisterUnit(identify: UnitIdentify<*>) {
    unregisterUnits(
      SingleRegistrarInfo(identify)
    ).forEach {throw UnitException("could not unregister unit", it)}
  }
  
  fun registerUnit(unitClass: Class<*>) {
    return registerUnit(unitClass, null)
  }
  
  fun unregisterUnit(unitClass: Class<*>) {
    return unregisterUnit(unitClass, null)
  }
}