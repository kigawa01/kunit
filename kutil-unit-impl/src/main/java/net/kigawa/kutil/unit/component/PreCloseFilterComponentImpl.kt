package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.PreCloseFilter

@LateInit
class PreCloseFilterComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  database: ComponentDatabase,
): PreCloseFilterComponent, ComponentHolderImpl<PreCloseFilter>(container, database, loggerComponent) {
  init {
  
  }
  override fun filter(info: UnitInfo<out Any>) {
    forEach {
      loggerComponent.catch(false) {
        it.filter(info)
      }
    }
  }
}