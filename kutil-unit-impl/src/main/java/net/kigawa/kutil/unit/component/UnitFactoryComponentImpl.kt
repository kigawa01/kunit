package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.extension.factory.NormalFactory
import net.kigawa.kutil.unit.util.LocaleBuilder
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.CircularReferenceException
import net.kigawa.kutil.unitapi.exception.NoFoundFactoryException
import net.kigawa.kutil.unitapi.extention.*
import java.util.logging.Level

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
  
  @Throws(CircularReferenceException::class)
  override fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T {
    val initStack = try {
      stack.addIdentify(identify)
    } catch (e: CircularReferenceException) {
      
      throw e
    }
    
    preInitFilterComponent.filter(identify, initStack)
    
    val result = lastMap {
      try {
        it.init(identify, initStack)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder("there is an exception when init unit").toString(),
            listOf(e),
            listOf(identify, stack, it)
          )
        )
        null
      }
    } ?: throw NoFoundFactoryException("valid factory is not found", identify = identify)
    
    return initializedFilter.filter(result, initStack)
  }
  
  fun addFactory(factory: UnitFactory) {
    database.registerComponent(factory)
    classes.add(factory.javaClass)
  }
}