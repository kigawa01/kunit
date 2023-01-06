package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.UnitFactory
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.exception.NoFoundFactoryException
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase

@LateInit
class UnitFactoryComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  private val database: ComponentInfoDatabase,
  private val initializedFilter: InitializedFilterComponent,
): UnitFactoryComponent {
  private val factoryClasses = ConcurrentList<Class<out UnitFactory>>()
  override fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T {
    val initStack = stack.addIdentify(identify)
    for (factoryClass in factoryClasses.reversed()) {
      val factory = loggerComponent.catch(null) {
        container.getUnit(factoryClass)
      } ?: continue
      
      val result = loggerComponent.catch(null, identify, initStack) {
        factory.init(identify, initStack)
      } ?: continue
      
      return initializedFilter.filter(result, stack)
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