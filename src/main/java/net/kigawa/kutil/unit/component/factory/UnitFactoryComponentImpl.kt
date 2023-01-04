package net.kigawa.kutil.unit.component.factory

import net.kigawa.kutil.unit.annotation.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.exception.NoFoundFactoryException
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.factory.UnitFactory

@LateInit
class UnitFactoryComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
  private val database: ComponentInfoDatabase,
): UnitFactoryComponent {
  private val factoryClasses = ConcurrentList<Class<out UnitFactory>>()
  override fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T {
    stack.addIdentify(identify)
    for (factoryClass in factoryClasses.reversed()) {
      val factory = loggerComponent.catch(null) {
        container.getUnit(factoryClass)
      } ?: continue
      
      return loggerComponent.catch(null, identify, stack) {
        factory.init(identify, stack)
      } ?: continue
    }
    throw NoFoundFactoryException("factory is not found", identify)
  }
  
  override fun addFactory(factoryClass: Class<out UnitFactory>) {
    database.registerComponentClass(factoryClass)
    factoryClasses.add(factoryClass)
  }
  
  fun addFactory(factory: UnitFactory) {
    database.registerComponent(factory)
    factoryClasses.add(factory.javaClass)
  }
  
  override fun removeFactory(factoryClass: Class<out UnitFactory>) {
    factoryClasses.remove(factoryClass)
    database.unregisterComponent(factoryClass)
  }
}