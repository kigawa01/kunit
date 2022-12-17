package net.kigawa.kutil.unit.component.getter

import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOption

interface UnitGetterComponent {
  fun addGetter(getterClass: Class<out UnitGetter<Any>>)
  fun removeGetter(getterClass: Class<out UnitGetter<Any>>)
  fun <T: Any> findGetter(identify: UnitIdentify<T>, options: List<RegisterOption>): UnitGetter<T>
}