package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.options.RegisterOptions

interface UnitStore {
  fun <T: Any> get(identify: UnitIdentify<T>): T?
  fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): T
  fun initGetter(identify: UnitIdentify<out Any>, initStack: InitStack)
  fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean
}