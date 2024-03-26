package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitFactoryComponent
import net.kigawa.kutil.kunit.api.component.UnitStoreComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.exception.UnitStoreException
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.UnitStore
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.impl.extension.store.InitializeStore
import net.kigawa.kutil.kunit.impl.extension.store.InstanceStore
import net.kigawa.kutil.kunit.impl.extension.store.SingletonStore
import net.kigawa.kutil.kunit.impl.util.LocaleBuilder
import java.util.*

@LateInit
class UnitStoreComponentImpl(
  container: UnitContainer,
  factoryComponent: UnitFactoryComponent,
  database: ComponentDatabase,
) : UnitStoreComponent,
    net.kigawa.kutil.kunit.impl.component.ComponentHolderImpl<UnitStore>(container, database) {

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