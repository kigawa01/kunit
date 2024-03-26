package net.kigawa.kutil.kunit.impl.extension.finder

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.extention.UnitFinder
import net.kigawa.kutil.kunit.api.options.FindInitGetOption
import net.kigawa.kutil.kunit.api.options.FindOptions

@LateInit
class InitGetFinder(
  private val databaseComponent: UnitDatabaseComponent,
): UnitFinder {
  
  override fun <T: Any> findUnits(identify: UnitIdentify<T>, findOptions: FindOptions): List<T>? {
    val stack = findOptions.firstOrNull(FindInitGetOption::class.java)?.initStack ?: return null
    return databaseComponent.findByIdentify(identify).map {it.initOrGet(stack)}.map {it}
  }
}