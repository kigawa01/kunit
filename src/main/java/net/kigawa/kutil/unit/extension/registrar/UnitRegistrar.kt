package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.component.UnitContainerConponents

interface UnitRegistrar {
  fun register(conponents: UnitContainerConponents): MutableList<Throwable>
}