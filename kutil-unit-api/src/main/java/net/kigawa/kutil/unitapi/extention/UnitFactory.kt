package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.UnitIdentify

interface UnitFactory {
  fun <T: Any> init(identify: UnitIdentify<T>, initStack: InitStack): T?
}
