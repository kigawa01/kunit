package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.ComponentDatabase
import net.kigawa.kutil.unit.api.extention.InitializedFilter

class InitializedFilterComponentImpl(
  database: ComponentDatabase,
  private val loggerComponent: UnitLoggerComponent,
  container: UnitContainer,
):
  InitializedFilterComponent, ComponentHolderImpl<InitializedFilter>(container, database, loggerComponent) {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    var result = obj
    
    forEach {
      loggerComponent.catch(null) {
        result = it.filter(result, stack)
      }
    }
    
    return result
  }
}