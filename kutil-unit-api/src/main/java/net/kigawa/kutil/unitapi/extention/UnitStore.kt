package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.options.RegisterOptions

interface UnitStore {
  fun <T: Any> get(identify: UnitIdentify<T>): T?
  fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): T
  fun initGetter(identify: UnitIdentify<out Any>, initStack: InitStack)
  fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean
}