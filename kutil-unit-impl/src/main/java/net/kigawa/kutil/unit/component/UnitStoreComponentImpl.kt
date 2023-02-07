package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.exception.UnitException
import net.kigawa.kutil.unit.extension.store.*
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.*
import net.kigawa.kutil.unitapi.options.RegisterOptions

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
    } ?: throw UnitException("could not register to UnitStore", identify, options)
  }
}