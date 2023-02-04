package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.UnitException
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.UnitFinder
import net.kigawa.kutil.unitapi.options.FindOptions

@LateInit
class UnitFinderComponentImpl(
  container: UnitContainer,
  private val database: ComponentDatabase, loggerComponent: UnitLoggerComponent,
): UnitFinderComponent, ComponentHolderImpl<UnitFinder>(container, database, loggerComponent) {
  fun addExecutor(executor: UnitFinder) {
    database.registerComponent(executor)
    classes.add(executor.javaClass)
  }
  
  override fun <T: Any> findUnits(identify: UnitIdentify<T>, findOptions: FindOptions): List<T> {
    return lastMap {
      val units = it.findUnitAsync(identify, findOptions) ?: return@lastMap null
      if (units.isEmpty()) return@lastMap null
      units
    } ?: throw UnitException("injector is not found", identify, findOptions)
  }
}