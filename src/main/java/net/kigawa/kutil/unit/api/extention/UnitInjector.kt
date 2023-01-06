package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify
import java.util.concurrent.Future

interface UnitInjector {
  fun <T: Any> findUnitAsync(identify: UnitIdentify<T>, stack: InitStack): Future<T>?
}