package net.kigawa.kutil.unit.component.registrar

import net.kigawa.kutil.unit.component.UnitContainerConponents
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.exception.UnitException
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
  
  override fun registerUnits(registrarInfo: UnitRegistrarInfo<*>) {
    registrars.last {
      val registrar = conponents.getterComponent.getUnit(it)
      conponents.logger.catch(false, "there an error in registrar", it) {
        registrar.register(registrarInfo)
      }
    } ?: throw UnitException("registrar is not found")
  }
  
  override fun unregisterUnits(registrarInfo: UnitRegistrarInfo<*>) {
    registrars.last {
      val registrar = conponents.getterComponent.getUnit(it)
      conponents.logger.catch(false, "there an error in registrar", it) {
        registrar.unregister(registrarInfo)
      }
    } ?: throw UnitException("registrar is not found")
  }
}