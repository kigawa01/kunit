package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.UnitFactory
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify

interface UnitFactoryComponent: ComponentHolder<UnitFactory> {
  fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T
}