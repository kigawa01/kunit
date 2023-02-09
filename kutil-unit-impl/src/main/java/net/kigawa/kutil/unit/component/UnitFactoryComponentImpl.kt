package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.extension.factory.NormalFactory
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.NoFoundFactoryException
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.UnitFactory

@LateInit
class UnitFactoryComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  private val database: ComponentDatabase,
  private val initializedFilter: InitializedFilterComponent,
  private val preInitFilterComponent: PreInitFilterComponent,
): UnitFactoryComponent, ComponentHolderImpl<UnitFactory>(container, database, loggerComponent) {
  init {
    addFactory(NormalFactory(container))
  }
  
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