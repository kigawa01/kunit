package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.component.UnitIdentify
import java.util.concurrent.Future

interface UnitStore {
  fun <T: Any> get(identify: UnitIdentify<T>): T
  fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): Future<T>
  fun initGetter(identify: UnitIdentify<out Any>, initStack: InitStack)
  fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean
}