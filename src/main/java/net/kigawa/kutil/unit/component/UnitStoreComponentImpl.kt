package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.ComponentDatabase
import net.kigawa.kutil.unit.api.extention.UnitStore
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.getter.*
import net.kigawa.kutil.unit.api.extention.RegisterOptions

@LateInit
class UnitStoreComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  factoryComponent: UnitFactoryComponent,
  asyncComponent: UnitAsyncComponent,
  database: ComponentDatabase,
): UnitStoreComponent, ComponentHolderImpl<UnitStore>(container, database, loggerComponent) {
  
  init {
    val initializeGetter = InitializeStore(factoryComponent, asyncComponent)
    classes.add(SingletonStore::class.java)
    database.registerComponent(SingletonStore::class.java, initializeGetter)
    classes.add(InstanceStore::class.java)
    database.registerComponent(InstanceStore::class.java, initializeGetter)
    classes.add(initializeGetter.javaClass)
    database.registerComponent(initializeGetter)
  }
  
  override fun findStore(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitStore {
    return lastMap {
      @Suppress("UNCHECKED_CAST")
      loggerComponent.catch(null) {
        if (it.register(identify, options)) it
        else null
      }
    } ?: throw UnitException("getter is not found", identify, options)
  }
}