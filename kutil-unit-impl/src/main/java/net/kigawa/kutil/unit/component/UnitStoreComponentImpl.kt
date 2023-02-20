package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.extension.store.*
import net.kigawa.kutil.unit.util.LocaleBuilder
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.UnitStoreException
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.UnitStore
import net.kigawa.kutil.unitapi.options.RegisterOptions
import java.util.*

@LateInit
class UnitStoreComponentImpl(
  container: UnitContainer,
  loggerComponent: UnitLoggerComponent,
  factoryComponent: UnitFactoryComponent,
  database: ComponentDatabase,
): UnitStoreComponent, ComponentHolderImpl<UnitStore>(container, database, loggerComponent) {
  
  init {
    val initializeGetter = InitializeStore(factoryComponent)
    classes.add(SingletonStore::class.java)
    database.registerComponent(SingletonStore::class.java, initializeGetter)
    classes.add(InstanceStore::class.java)
    database.registerComponent(InstanceStore::class.java, initializeGetter)
    classes.add(initializeGetter.javaClass)
    database.registerComponent(initializeGetter, null)
  }
  
  override fun findStore(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitStore {
    return lastMap {
      try {
        if (it.register(identify, options)) it
        else null
      } catch (e: Throwable) {
        null
      }
    } ?: throw UnitStoreException(
      LocaleBuilder(Locale.ENGLISH, "could not register to UnitStore").toString(),
      identify,
      options
    )
  }
}