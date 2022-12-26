package net.kigawa.kutil.unit.component.container

import net.kigawa.kutil.unit.component.async.AsyncComponentImpl
import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.closer.UnitCloserComponentImpl
import net.kigawa.kutil.unit.component.config.UnitContainerConfigImpl
import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponentImpl
import net.kigawa.kutil.unit.component.executor.ExecutorComponentImpl
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponentImpl
import net.kigawa.kutil.unit.component.getter.UnitGetterComponentImpl
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponentImpl
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registrar.ClassRegistrar
import net.kigawa.kutil.unit.extension.registrar.InstanceRegistrar

class UnitContainerImpl(
  private val parent: UnitContainer? = null,
): UnitContainer {
  private val closerComponent = getUnit(UnitCloserComponent::class.java)
  private val componentClasses = ConcurrentList<Class<out Any>>()
  private val loggerComponent: ContainerLoggerComponent
  private val database: UnitInfoDatabaseComponentImpl
  
  init {
    val componentInfoDatabase = ComponentInfoDatabase()
    database = UnitInfoDatabaseComponentImpl(componentInfoDatabase)
    loggerComponent = ContainerLoggerComponentImpl(this, componentInfoDatabase)
    database.loggerComponent = loggerComponent
    val factoryComponent = UnitFactoryComponentImpl(this, loggerComponent)
    val asyncComponent = AsyncComponentImpl(this, loggerComponent)
    val getterComponent =
      UnitGetterComponentImpl(this, loggerComponent, factoryComponent, asyncComponent, componentInfoDatabase)
    componentInfoDatabase.getterComponent = getterComponent
    
    val classRegistrar = ClassRegistrar(getterComponent, database)
    val closerComponent = UnitCloserComponentImpl(this, loggerComponent)
    val config = UnitContainerConfigImpl()
    val executorComponent = ExecutorComponentImpl(this, loggerComponent)
    
    val instanceRegistrar = InstanceRegistrar(classRegistrar)
  }
  
  override fun removeUnit(identify: UnitIdentify<out Any>) {
    database.findByIdentify(identify).filter {info->
      if (componentClasses.contain {info.instanceOf(it)}) return@filter true
      loggerComponent.catch(null) {
        removeInfo(info)
      }
      false
    }
  }
  
  private fun removeInfo(info: UnitInfo<out Any>) {
    database.unregisterInfo(info)
    closerComponent.closeUnit(info)
  }
  
  override fun <T: Any> getUnitList(identify: UnitIdentify<T>): List<T> {
    val list = mutableListOf<T>()
    database.findByIdentify(identify).forEach {
      loggerComponent.catch(null) {
        list.add(it.get())
      }
    }
    parent?.let {list.addAll(it.getUnitList(identify))}
    return list
  }
  
  override fun close() {
    removeUnit(Any::class.java)
    parent?.close()
  }
}