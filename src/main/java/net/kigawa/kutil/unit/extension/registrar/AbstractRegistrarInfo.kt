package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

abstract class AbstractRegistrarInfo: UnitRegistrarInfo {
  override val identifies: MutableList<UnitIdentify<*>> = mutableListOf()
  override val errors: MutableList<Throwable> = mutableListOf()
}