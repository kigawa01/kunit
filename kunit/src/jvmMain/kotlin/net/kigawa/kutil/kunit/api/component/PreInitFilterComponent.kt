package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.extention.PreInitFilter

interface PreInitFilterComponent: ComponentHolder<PreInitFilter> {
  fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack)
}