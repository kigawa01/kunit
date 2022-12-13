package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.component.UnitContainerConponents
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

abstract class AbstractRegistrar: UnitRegistrar {
  val identifies: MutableList<UnitIdentify<*>> = mutableListOf()
  val errors: MutableList<Throwable> = mutableListOf()
  
  override fun register(conponents: UnitContainerConponents): MutableList<Throwable> {
    identifies.map {
      try {
        conponents.databaseComponent.registerUnit(it)
        return@map it
      } catch (e: Throwable) {
        errors.add(e)
        return@map null
      }
    }.forEach {
      try {
        it?.let {conponents.getterComponent.registerUnit(it)}
      } catch (e: Throwable) {
        errors.add(e)
      }
    }
    return errors
  }
}