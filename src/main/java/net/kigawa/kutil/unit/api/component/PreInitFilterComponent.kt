package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.PreInitFilter
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify

interface PreInitFilterComponent: ComponentHolder<PreInitFilter> {
  fun <T: Any>filter(identify: UnitIdentify<T>, stack: InitStack)
}