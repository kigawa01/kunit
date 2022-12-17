package net.kigawa.kutil.unit.component.registrar

import net.kigawa.kutil.unit.component.UnitContainerConponents
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.registrar.UnitRegistrar
import net.kigawa.kutil.unit.extension.registrar.UnitRegistrarInfo

class UnitRegistrarComponentImpl(private val conponents: UnitContainerConponents): UnitRegistrarComponent {
  private val registrars = ConcurrentList<Class<out UnitRegistrar>>()
  override fun addRegistrar(registrar: Class<out UnitRegistrar>) {
    registerUnit(registrar)
    registrars.add(registrar)
  }
  
  override fun removeRegistrar(registrar: Class<out UnitRegistrar>) {
    unregisterUnit(registrar)
    registrars.remove(registrar)
  }
  
  override fun registerUnits(registrarInfo: UnitRegistrarInfo<*>): MutableList<Throwable> {
    val errors = mutableListOf<Throwable>()
    registrars.last {
      val registrar = conponents.getterComponent.getUnit(it)
      registrar.register(registrarInfo, errors)
    }
    return errors
  }
  
  override fun unregisterUnits(registrarInfo: UnitRegistrarInfo<*>): MutableList<Throwable> {
    val errors = mutableListOf<Throwable>()
    errors.addAll(registrarInfo.errors)
    registrars.last {
      val registrar = conponents.getterComponent.getUnit(it)
      registrar.unregister(registrarInfo, errors)
    }
    return errors
  }
}