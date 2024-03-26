package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.extention.UnitFinder
import net.kigawa.kutil.kunit.api.options.FindOptions

interface UnitFinderComponent: ComponentHolder<UnitFinder> {
  fun <T: Any> findUnits(identify: UnitIdentify<T>, findOptions: FindOptions): List<T>
}