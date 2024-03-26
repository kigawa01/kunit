package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.component.UnitInfo
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.UnitInfoDatabase
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.impl.concurrent.ConcurrentList
import net.kigawa.kutil.kunit.impl.extension.database.DefaultInfoDatabase

@LateInit
class UnitDatabaseComponentImpl(
  private val componentDatabase: ComponentDatabase,
) : UnitDatabaseComponent {
  private val databases = ConcurrentList<UnitInfoDatabase>(componentDatabase)

  init {
    addDatabase(DefaultInfoDatabase())
  }


  override fun getComponentDatabase(): ComponentDatabase {
    return componentDatabase
  }

  override fun addDatabase(unitInfoDatabase: UnitInfoDatabase) {
    databases.add(unitInfoDatabase)
    componentDatabase.registerComponent(unitInfoDatabase, null)
  }

  override fun removeDatabase(unitInfoDatabase: UnitInfoDatabase) {
    componentDatabase.unregisterComponent(unitInfoDatabase.javaClass)
    databases.remove(unitInfoDatabase)
  }

  override fun registerInfo(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions) {
    databases.last {
      it.register(unitInfo, registerOptions)
    }
  }

  override fun unregisterInfo(unitInfo: UnitInfo<*>) {
    databases.forEach {
      it.unregister(unitInfo)
    }
  }

  override fun <T : Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>> {
    return databases.flatMap {
      it.findByIdentify(identify)
    }
  }
}