package net.kigawa.kutil.unit.extension.registrarinfo

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitRegistrarInfo<T> {
  val identifies: MutableList<UnitIdentify<T>>
  val errors: MutableList<Throwable>
}