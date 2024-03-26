package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.extention.UnitFactory

interface UnitFactoryComponent: ComponentHolder<UnitFactory> {
  fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T
}