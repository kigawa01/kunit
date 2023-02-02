package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.UnitAsyncExecutor

@LateInit
class UnitAsyncComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  database: ComponentDatabase,
): UnitAsyncComponent, ComponentHolderImpl<UnitAsyncExecutor>(container, database, loggerComponent) {
  
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable) {
    last {
      loggerComponent.catch(false) {
        it.execute(identify, runnable)
      }
    }
  }
}