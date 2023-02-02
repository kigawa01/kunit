package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.PreInitFilter

class PreInitFilterComponentImpl(
  container: UnitContainer,
  database: ComponentDatabase,
  loggerComponent: UnitLoggerComponent,
):
  PreInitFilterComponent, ComponentHolderImpl<PreInitFilter>(container, database, loggerComponent) {
  override fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    forEach {
      it.filter(identify, stack)
    }
  }
}