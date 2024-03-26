package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.options.FindOptions

interface UnitFinder {
  fun <T: Any> findUnits(identify: UnitIdentify<T>, findOptions: FindOptions): List<T>?
}