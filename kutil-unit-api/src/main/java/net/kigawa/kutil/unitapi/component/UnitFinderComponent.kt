package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitapi.extention.UnitInjector

interface UnitFinderComponent: ComponentHolder<UnitInjector> {
  fun <T: Any> findUnits(identify: UnitIdentify<T>, stack: InitStack, findOptions: FindOptions): List<T>
  fun <T: Any> findUnits(identifies: List<UnitIdentify<out T>>, stack: InitStack, findOptions: FindOptions): List<T> {
    return identifies.flatMap {
      findUnits(it, stack, findOptions)
    }
  }
}