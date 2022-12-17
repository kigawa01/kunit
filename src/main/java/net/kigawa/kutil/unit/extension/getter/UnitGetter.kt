package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOption

interface UnitGetter<T> {
  fun get(): T
  fun register(identify: UnitIdentify<out Any>, options: List<RegisterOption>): Boolean
}