package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.component.UnitFinderComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.UnitFinder
import net.kigawa.kutil.kunit.api.options.FindOptionEnum
import net.kigawa.kutil.kunit.api.options.FindOptions

@LateInit
class UnitFinderComponentImpl(
  container: UnitContainer,
  private val componentDatabase: ComponentDatabase,
  private val databaseComponent: UnitDatabaseComponent,
) : UnitFinderComponent,
    net.kigawa.kutil.kunit.impl.component.ComponentHolderImpl<UnitFinder>(container, componentDatabase) {
  fun addFinder(executor: UnitFinder) {
    componentDatabase.registerComponent(executor, null)
    classes.add(executor.javaClass)
  }

  override fun <T : Any> findUnits(identify: UnitIdentify<T>, findOptions: FindOptions): List<T> {
    if (!findOptions.contain(FindOptionEnum.SKIP_FIND)) {
      val list = lastMap {
        val units = it.findUnits(identify, findOptions) ?: return@lastMap null
        if (units.isEmpty()) return@lastMap null
        units
      }
      if (list != null) return list
    }
    return defaultFind(identify)
  }

  fun <T : Any> defaultFind(identify: UnitIdentify<T>): MutableList<T> {
    val list = mutableListOf<T>()
    databaseComponent.findByIdentify(identify).forEach {
      it.get()?.let { it1 -> list.add(it1) }
    }
    return list
  }
}