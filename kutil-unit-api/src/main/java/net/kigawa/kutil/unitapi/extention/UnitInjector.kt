package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.FindOptions

interface UnitInjector {
  fun <T: Any> findUnitAsync(identify: UnitIdentify<T>, stack: InitStack, findOptions: FindOptions): List<T>?
}