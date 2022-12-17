package net.kigawa.kutil.unit.extension.registrarinfo

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

abstract class AbstractRegistrarInfo<T>: UnitRegistrarInfo<T> {
  override val identifies: MutableList<UnitIdentify<T>> = mutableListOf()
  override val errors: MutableList<Throwable> = mutableListOf()
}