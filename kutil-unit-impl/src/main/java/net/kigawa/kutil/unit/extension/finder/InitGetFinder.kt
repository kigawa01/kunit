package net.kigawa.kutil.unit.extension.finder

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.UnitDatabaseComponent
import net.kigawa.kutil.unitapi.extention.UnitFinder
import net.kigawa.kutil.unitapi.options.FindInitGetOption
import net.kigawa.kutil.unitapi.options.FindOptions

@LateInit
class InitGetFinder(
  private val databaseComponent: UnitDatabaseComponent,
): UnitFinder {
  
  override fun <T: Any> findUnits(identify: UnitIdentify<T>, findOptions: FindOptions): List<T>? {
    val stack = findOptions.firstOrNull(FindInitGetOption::class.java)?.initStack ?: return null
    return databaseComponent.findByIdentify(identify).map {it.initOrGet(stack)}.map {it.get()}
  }
}