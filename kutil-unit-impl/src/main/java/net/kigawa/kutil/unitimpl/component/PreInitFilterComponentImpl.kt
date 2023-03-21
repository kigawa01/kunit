package net.kigawa.kutil.unitimpl.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.PreInitFilter

class PreInitFilterComponentImpl(
  container: UnitContainer,
  database: ComponentDatabase,
  loggerComponent: UnitLoggerComponent,
):
  PreInitFilterComponent, net.kigawa.kutil.unitimpl.component.ComponentHolderImpl<PreInitFilter>(container, database, loggerComponent) {
  override fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    forEach {
      it.filter(identify, stack)
    }
  }
}