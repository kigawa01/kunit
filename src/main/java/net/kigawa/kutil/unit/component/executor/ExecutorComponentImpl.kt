package net.kigawa.kutil.unit.component.executor

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.UnitClassList
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.executor.UnitExecutor
import java.lang.reflect.Constructor

class ExecutorComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): ExecutorComponent {
  private val executorClasses = UnitClassList<UnitExecutor>(container)
  override fun addExecutor(executorClass: Class<out UnitExecutor>) {
    executorClasses.addContainer(executorClass)
  }
  
  override fun removeExecutor(executorClass: Class<out UnitExecutor>) {
    executorClasses.removeContainer(executorClass)
  }
  
  override fun <T>callConstructor(constructor: Constructor<T>, initStack: InitStack): T {
    for (executorClass in executorClasses.reversed()) {
      val executor = loggerComponent.catch(null) {
        container.getUnit(executorClass)
      } ?: continue
      return loggerComponent.catch(null) {
        executor.callConstructor(constructor, initStack)
      } ?: continue
    }
    throw UnitException("could not execute executable")
  }
}