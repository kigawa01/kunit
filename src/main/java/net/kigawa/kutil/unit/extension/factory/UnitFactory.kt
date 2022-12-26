package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify

interface UnitFactory {
  fun <T: Any> init(identify: UnitIdentify<T>, initStack: InitStack): T?
}
