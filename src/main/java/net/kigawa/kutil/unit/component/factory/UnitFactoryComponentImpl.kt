package net.kigawa.kutil.unit.component.factory

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.UnitClassList
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.factory.UnitFactory
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class UnitFactoryComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): UnitFactoryComponent {
  private val factoryClasses = UnitClassList<UnitFactory>(container)
  override fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T {
    stack.addIdentify(identify)
    for (factoryClass in factoryClasses.reversed()) {
      val factory = loggerComponent.catch(null, "") {
        container.getUnit(factoryClass)
      } ?: continue
      
      return loggerComponent.catch(null, "") {
        factory.init(identify, stack)
      } ?: continue
    }
    throw UnitException("factory is not found", identify)
  }
  
  override fun addFactory(factoryClass: Class<out UnitFactory>) {
    factoryClasses.removeContainer(factoryClass)
  }
  
  override fun removeFactory(factoryClass: Class<out UnitFactory>) {
    factoryClasses.addContainer(factoryClass)
  }
}