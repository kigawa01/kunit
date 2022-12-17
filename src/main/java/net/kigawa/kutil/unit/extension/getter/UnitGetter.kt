package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOption
import java.util.concurrent.Future

interface UnitGetter {
  fun <T> get(identify: UnitIdentify<T>): T
  fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): Future<T>
  fun init(identify: UnitIdentify<out Any>, initStack: InitStack)
  fun register(identify: UnitIdentify<out Any>, options: List<RegisterOption>): Boolean
}