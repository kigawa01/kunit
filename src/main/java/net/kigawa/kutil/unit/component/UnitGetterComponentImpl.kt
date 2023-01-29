package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.ComponentDatabase
import net.kigawa.kutil.unit.api.extention.UnitGetter
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.getter.*
import net.kigawa.kutil.unit.api.extention.RegisterOptions

@LateInit
class UnitGetterComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  factoryComponent: UnitFactoryComponent,
  asyncComponent: UnitAsyncComponent,
  database: ComponentDatabase,
): UnitGetterComponent, ComponentHolderImpl<UnitGetter>(container, database, loggerComponent) {
  
  init {
    val initializeGetter = InitializeGetter(factoryComponent, asyncComponent)
    classes.add(SingletonGetter::class.java)
    database.registerComponent(SingletonGetter::class.java, initializeGetter)
    classes.add(InstanceGetter::class.java)
    database.registerComponent(InstanceGetter::class.java, initializeGetter)
    classes.add(initializeGetter.javaClass)
    database.registerComponent(initializeGetter)
  }
  
  override fun findGetter(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitGetter {
    return lastMap {
      @Suppress("UNCHECKED_CAST")
      loggerComponent.catch(null) {
        if (it.register(identify, options)) it
        else null
      }
    } ?: throw UnitException("getter is not found", identify, options)
  }
}