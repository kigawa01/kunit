package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase

class InitializedFilterComponentImpl(
  database: ComponentInfoDatabase,
  private val loggerComponent: UnitLoggerComponent,
  private val container: UnitContainer,
):
  InitializedFilterComponent, ComponentHolderImpl<InitializedFilter>(container, database) {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    var result = obj
    
    classes.forEach {
      val filter = loggerComponent.catch(null) {
        container.getUnit(it)
      } ?: return@forEach
      loggerComponent.catch(null) {
        result = filter.filter(result, stack)
      }
    }
    
    return result
  }
}