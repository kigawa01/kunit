package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.extention.UnitFinder
import net.kigawa.kutil.unitapi.options.FindOptions

interface UnitFinderComponent: ComponentHolder<UnitFinder> {
  fun <T: Any> findUnits(identify: UnitIdentify<T>, findOptions: FindOptions): List<T>
}