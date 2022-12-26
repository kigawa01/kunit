package net.kigawa.kutil.unit.component.executor

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.UnitClassList
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.executor.UnitReflectionExecutor
import java.lang.reflect.Constructor

class UnitReflectionComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): UnitReflectionComponent {
  private val executorClasses = UnitClassList<UnitReflectionExecutor>(container)
  override fun addExecutor(executorClass: Class<out UnitReflectionExecutor>) {
    executorClasses.addContainer(executorClass)
  }
  
  override fun removeExecutor(executorClass: Class<out UnitReflectionExecutor>) {
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