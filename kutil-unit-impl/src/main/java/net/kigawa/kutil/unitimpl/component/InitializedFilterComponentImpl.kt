package net.kigawa.kutil.unitimpl.component

import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.*
import java.util.logging.Level

class InitializedFilterComponentImpl(
  database: ComponentDatabase,
  private val loggerComponent: UnitLoggerComponent,
  container: UnitContainer,
):
  InitializedFilterComponent, net.kigawa.kutil.unitimpl.component.ComponentHolderImpl<InitializedFilter>(container, database, loggerComponent) {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    var result = obj
    
    forEach {
      try {
        result = it.filter(result, stack)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            "there is an exception in initialized filter",
            listOf(e),
            listOf(it)
          )
        )
      }
    }
    
    return result
  }
}