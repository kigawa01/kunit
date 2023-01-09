package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.UnitGetter
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

interface UnitGetterComponent: ComponentHolder<UnitGetter> {
  fun findGetter(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitGetter
}