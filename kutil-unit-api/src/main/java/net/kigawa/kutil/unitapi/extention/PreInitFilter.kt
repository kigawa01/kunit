package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.UnitIdentify

interface PreInitFilter {
  fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack)
}