package net.kigawa.kutil.unit.component.database

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.database.UnitInfoDatabase
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions
import net.kigawa.kutil.unit.extension.registrar.InstanceRegistrar

class UnitInfoDatabaseComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): UnitInfoDatabaseComponent {
  private val instanceRegistrar
    get() = container.getUnit(InstanceRegistrar::class.java)
  private val databases = ConcurrentList<UnitInfoDatabase>()
  
  init {
    val componentDatabase = ComponentInfoDatabase()
    databases.add(componentDatabase)
  }
  
  override fun addDatabase(unitInfoDatabase: UnitInfoDatabase) {
    databases.add(unitInfoDatabase)
    instanceRegistrar.register(unitInfoDatabase)
  }
  
  override fun removeDatabase(unitInfoDatabase: UnitInfoDatabase) {
    container.removeUnit(unitInfoDatabase.javaClass)
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