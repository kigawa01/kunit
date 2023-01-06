package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.UnitFactory
import net.kigawa.kutil.unit.exception.NoFoundFactoryException
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase

@LateInit
class UnitFactoryComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  private val database: ComponentInfoDatabase,
  private val initializedFilter: InitializedFilterComponent,
  private val preInitFilterComponent: PreInitFilterComponent,
): UnitFactoryComponent, ComponentHolderImpl<UnitFactory>(container, database, loggerComponent) {
  override fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T {
    val initStack = stack.addIdentify(identify)
    
    preInitFilterComponent.filter(identify, initStack)
    
    return lastMap {
      val result = loggerComponent.catch(null, identify, initStack) {
        it.init(identify, initStack)
      } ?: return@lastMap null
      
      return@lastMap initializedFilter.filter(result, initStack)
    } ?: throw NoFoundFactoryException("factory is not found", identify)
  }
  
  fun addFactory(factory: UnitFactory) {
    database.registerComponent(factory)
    classes.add(factory.javaClass)
  }
}