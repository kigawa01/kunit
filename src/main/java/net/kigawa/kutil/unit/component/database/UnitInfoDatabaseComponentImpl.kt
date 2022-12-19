package net.kigawa.kutil.unit.component.database

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.UnitInfoDatabase
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registrar.InstanceRegistrar

class UnitInfoDatabaseComponentImpl(
  private val instanceRegistrar: InstanceRegistrar,
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): UnitInfoDatabaseComponent {
  private val databases = ConcurrentList<UnitInfoDatabase>()
  override fun addDatabase(unitInfoDatabase: UnitInfoDatabase) {
    instanceRegistrar.register(unitInfoDatabase)
    databases.add(unitInfoDatabase)
  }
  
  override fun removeDatabase(unitInfoDatabase: UnitInfoDatabase) {
    databases.remove(unitInfoDatabase)
    container.removeUnit(unitInfoDatabase.javaClass)
  }
  
  override fun registerInfo(unitInfo: UnitInfo<out Any>) {
    databases.last {
      loggerComponent.catch(default = false) {
        it.register(unitInfo)
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
  
  override fun <T: Any> findOneEquals(identify: UnitIdentify<T>): UnitInfo<T>? {
    for (database in databases.toMutableList()) {
      return loggerComponent.catch(null) {
        database.findOneEquals(identify)
      } ?: continue
    }
    return null
  }
  
  override fun <T: Any> findByClass(unitClass: Class<T>): List<UnitInfo<T>> {
    return databases.flatMap {
      loggerComponent.catch(listOf()) {
        it.findByClass(unitClass)
      }
    }
  }
}