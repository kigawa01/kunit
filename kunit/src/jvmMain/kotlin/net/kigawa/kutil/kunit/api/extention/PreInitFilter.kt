package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.UnitIdentify

interface PreInitFilter {
  fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack)
}