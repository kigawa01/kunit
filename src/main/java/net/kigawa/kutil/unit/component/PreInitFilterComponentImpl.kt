package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.ComponentInfoDatabase
import net.kigawa.kutil.unit.api.extention.PreInitFilter

class PreInitFilterComponentImpl(
  container: UnitContainer,
  database: ComponentInfoDatabase,
  loggerComponent: UnitLoggerComponent,
):
  PreInitFilterComponent, ComponentHolderImpl<PreInitFilter>(container, database, loggerComponent) {
  override fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    forEach {
      it.filter(identify, stack)
    }
  }
}