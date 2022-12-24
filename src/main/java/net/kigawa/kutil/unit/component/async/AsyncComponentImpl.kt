package net.kigawa.kutil.unit.component.async

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.async.AsyncExecutor
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registrar.InstanceRegistrar

class AsyncComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): AsyncComponent {
  private val instanceRegistrar
    get() = container.getUnit(InstanceRegistrar::class.java)
  private val executors = ConcurrentList<AsyncExecutor>()
  override fun addAsyncExecutor(async: AsyncExecutor) {
    instanceRegistrar.register(async)
    executors.add(async)
  }
  
  override fun removeAsyncExecutor(async: AsyncExecutor) {
    executors.remove(async)
    container.removeUnit(async.javaClass)
  }
  
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable) {
    executors.last {
      loggerComponent.catch(false) {
        it.execute(identify, runnable)
      }
    }
  }
}