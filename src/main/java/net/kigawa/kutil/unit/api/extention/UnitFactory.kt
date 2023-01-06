package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify

interface UnitFactory {
  fun <T: Any> init(identify: UnitIdentify<T>, initStack: InitStack): T?
}
