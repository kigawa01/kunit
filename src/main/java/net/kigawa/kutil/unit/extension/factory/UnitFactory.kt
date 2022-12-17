package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitFactory {
  fun <T: Any> init(unitIdentify: UnitIdentify<T>,initStack: InitStack): T?
}
