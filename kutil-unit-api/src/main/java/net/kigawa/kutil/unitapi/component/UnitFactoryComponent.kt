package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.extention.UnitFactory

interface UnitFactoryComponent: ComponentHolder<UnitFactory> {
  fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T
}