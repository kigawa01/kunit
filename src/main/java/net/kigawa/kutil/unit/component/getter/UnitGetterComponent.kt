package net.kigawa.kutil.unit.component.getter

import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOption

interface UnitGetterComponent {
  fun <T> findGetter(identify: UnitIdentify<T>, options: List<RegisterOption>): UnitGetter<T>
}