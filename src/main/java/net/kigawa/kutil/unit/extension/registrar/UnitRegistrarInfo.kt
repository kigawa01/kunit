package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitRegistrarInfo {
  val identifies: MutableList<UnitIdentify<*>>
  val errors: MutableList<Throwable>
}