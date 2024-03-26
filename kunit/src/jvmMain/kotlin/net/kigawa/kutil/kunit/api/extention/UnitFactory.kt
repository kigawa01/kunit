package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.UnitIdentify

interface UnitFactory {
  fun <T: Any> init(identify: UnitIdentify<T>, initStack: InitStack): T?
}
