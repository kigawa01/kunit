package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.extention.PreInitFilter

interface PreInitFilterComponent: ComponentHolder<PreInitFilter> {
  fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack)
}