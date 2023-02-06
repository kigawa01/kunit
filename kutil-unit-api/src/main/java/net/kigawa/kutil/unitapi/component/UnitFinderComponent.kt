package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitapi.extention.UnitFinder

interface UnitFinderComponent: ComponentHolder<UnitFinder> {
  fun <T: Any> findUnits(identify: UnitIdentify<T>,  findOptions: FindOptions): List<T>
  fun <T: Any> findUnits(identifies: List<UnitIdentify<out T>>, findOptions: FindOptions): List<T> {
    return identifies.flatMap {
      findUnits(it,  findOptions)
    }
  }
}