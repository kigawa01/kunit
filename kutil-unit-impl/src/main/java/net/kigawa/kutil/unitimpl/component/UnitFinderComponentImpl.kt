package net.kigawa.kutil.unitimpl.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.container.UnitContainer
import net.kigawa.kutil.unitapi.component.UnitDatabaseComponent
import net.kigawa.kutil.unitapi.component.UnitFinderComponent
import net.kigawa.kutil.unitapi.component.UnitLoggerComponent
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.Message
import net.kigawa.kutil.unitapi.extention.UnitFinder
import net.kigawa.kutil.unitapi.options.FindOptionEnum
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitimpl.util.LocaleBuilder
import java.util.logging.Level

@LateInit
class UnitFinderComponentImpl(
  container: UnitContainer,
  private val componentDatabase: ComponentDatabase,
  private val loggerComponent: UnitLoggerComponent,
  private val databaseComponent: UnitDatabaseComponent,
) : UnitFinderComponent,
  ComponentHolderImpl<UnitFinder>(container, componentDatabase, loggerComponent) {
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
        ?: loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder("could not get instance").toString(),
            listOf(),
            listOf(identify)
          )
        )
    }
    return list
  }
}