package net.kigawa.kutil.unitimpl.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.UnitFactoryComponent
import net.kigawa.kutil.unitapi.component.UnitLoggerComponent
import net.kigawa.kutil.unitapi.component.UnitStoreComponent
import net.kigawa.kutil.unitapi.component.container.UnitContainer
import net.kigawa.kutil.unitapi.exception.UnitStoreException
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.UnitStore
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitimpl.extension.store.InitializeStore
import net.kigawa.kutil.unitimpl.extension.store.InstanceStore
import net.kigawa.kutil.unitimpl.extension.store.SingletonStore
import net.kigawa.kutil.unitimpl.util.LocaleBuilder
import java.util.*

@LateInit
class UnitStoreComponentImpl(
  container: UnitContainer,
  loggerComponent: UnitLoggerComponent,
  factoryComponent: UnitFactoryComponent,
  database: ComponentDatabase,
) : UnitStoreComponent,
  ComponentHolderImpl<UnitStore>(container, database, loggerComponent) {

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