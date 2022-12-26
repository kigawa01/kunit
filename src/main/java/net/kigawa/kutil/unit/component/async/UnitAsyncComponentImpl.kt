package net.kigawa.kutil.unit.component.async

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.async.AsyncExecutor
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class UnitAsyncComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
  private val database: ComponentInfoDatabase,
): UnitAsyncComponent {
  private val executors = ConcurrentList<Class<out AsyncExecutor>>()
  override fun addAsyncExecutor(asyncClass: Class<out AsyncExecutor>) {
    database.registerComponentClass(asyncClass)
    executors.add(asyncClass)
  }
  
  override fun removeAsyncExecutor(asyncClass: Class<out AsyncExecutor>) {
    executors.remove(asyncClass)
    database.unregisterComponent(asyncClass)
  }
  
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable) {
    executors.last {
      loggerComponent.catch(false) {
        container.getUnit(it).execute(identify, runnable)
      }
    }
  }
}