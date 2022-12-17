package net.kigawa.kutil.unit.component.getter

import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOption

interface UnitGetterComponent {
  fun addGetter(getterClass: Class<out UnitGetter>)
  fun removeGetter(getterClass: Class<out UnitGetter>)
  fun findGetter(identify: UnitIdentify<Any>, options: List<RegisterOption>): UnitGetter
}