package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.PreInitFilter
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase

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