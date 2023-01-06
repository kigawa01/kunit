package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.ComponentInfoDatabase
import net.kigawa.kutil.unit.api.extention.UnitAsyncExecutor

@LateInit
class UnitAsyncComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  database: ComponentInfoDatabase,
): UnitAsyncComponent, ComponentHolderImpl<UnitAsyncExecutor>(container, database, loggerComponent) {
  
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable) {
    last {
      loggerComponent.catch(false) {
        it.execute(identify, runnable)
      }
    }
  }
}