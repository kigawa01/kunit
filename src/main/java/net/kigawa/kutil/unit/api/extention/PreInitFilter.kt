package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify

interface PreInitFilter {
  fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack)
}