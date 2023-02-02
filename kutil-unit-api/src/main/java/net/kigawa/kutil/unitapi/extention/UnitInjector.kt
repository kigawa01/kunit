package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.component.UnitIdentify
import java.util.concurrent.Future

interface UnitInjector {
  fun <T: Any> findUnitAsync(identify: UnitIdentify<T>, stack: InitStack): Future<T>?
}