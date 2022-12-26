package net.kigawa.kutil.unit.component.getter

import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

interface UnitGetterComponent {
  fun addGetter(getterClass: Class<out UnitGetter>)
  fun removeGetter(getterClass: Class<out UnitGetter>)
  fun findGetter(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitGetter
}