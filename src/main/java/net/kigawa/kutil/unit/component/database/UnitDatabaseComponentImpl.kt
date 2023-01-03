package net.kigawa.kutil.unit.component.database

import net.kigawa.kutil.unit.annotation.LateInit
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.*
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

@LateInit
class UnitDatabaseComponentImpl(
  private val componentDatabase: ComponentInfoDatabase,
): UnitDatabaseComponent {
  private lateinit var loggerComponent: ContainerLoggerComponent
  private val databases = ConcurrentList<UnitInfoDatabase>(componentDatabase)
  
  init {
    addDatabase(DefaultInfoDatabase())
  }
  
  override fun getComponentDatabase(): ComponentInfoDatabase {
    return componentDatabase
  }
  
  override fun addDatabase(unitInfoDatabase: UnitInfoDatabase) {
    databases.add(unitInfoDatabase)
    componentDatabase.registerComponent(unitInfoDatabase)
  }
  
  override fun removeDatabase(unitInfoDatabase: UnitInfoDatabase) {
    componentDatabase.unregisterComponent(unitInfoDatabase.javaClass)
    databases.remove(unitInfoDatabase)
  }
  
  override fun registerInfo(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions) {
    databases.last {
      loggerComponent.catch(default = false) {
        it.register(unitInfo, registerOptions)
      }
    }
  }
  
  override fun unregisterInfo(unitInfo: UnitInfo<*>) {
    databases.forEach {
      loggerComponent.catch(null) {
        it.unregister(unitInfo)
      }
    }
  }
  
  override fun <T: Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>> {
    return databases.flatMap {
      loggerComponent.catch(listOf()) {
        it.findByIdentify(identify)
      }
    }
  }
}