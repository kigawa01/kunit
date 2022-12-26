package net.kigawa.kutil.unit.component.container

import net.kigawa.kutil.unit.component.async.UnitAsyncComponentImpl
import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.closer.UnitCloserComponentImpl
import net.kigawa.kutil.unit.component.config.UnitConfigComponentImpl
import net.kigawa.kutil.unit.component.database.UnitDatabaseComponent
import net.kigawa.kutil.unit.component.database.UnitDatabaseComponentImpl
import net.kigawa.kutil.unit.component.executor.UnitExecutorComponentImpl
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponentImpl
import net.kigawa.kutil.unit.component.getter.UnitGetterComponentImpl
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponentImpl
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.factory.KotlinObjectFactory
import net.kigawa.kutil.unit.extension.factory.NormalFactory
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class UnitContainerImpl(
  private val parent: UnitContainer? = null,
): UnitContainer {
  private val closerComponent = getUnit(UnitCloserComponent::class.java)
  private val componentClasses = ConcurrentList<Class<out Any>>()
  private val loggerComponent: ContainerLoggerComponent
  private val databaseComponent: UnitDatabaseComponent
  
  init {
    val componentDatabase = ComponentInfoDatabase()
    
    databaseComponent = UnitDatabaseComponentImpl(componentDatabase)
    loggerComponent = ContainerLoggerComponentImpl(this, componentDatabase)
    
    databaseComponent.loggerComponent = loggerComponent
    
    val factoryComponent = UnitFactoryComponentImpl(this, loggerComponent, componentDatabase)
    val asyncComponent = UnitAsyncComponentImpl(this, loggerComponent, componentDatabase)
    val getterComponent =
      UnitGetterComponentImpl(this, loggerComponent, factoryComponent, asyncComponent, componentDatabase)
    
    componentDatabase.getterComponent = getterComponent
    factoryComponent.addFactory(NormalFactory::class.java)
    factoryComponent.addFactory(KotlinObjectFactory::class.java)
    
    val closerComponent = UnitCloserComponentImpl(this, loggerComponent)
    val config = UnitConfigComponentImpl()
    val executorComponent = UnitExecutorComponentImpl(this, loggerComponent)
  }
  
  override fun removeUnit(identify: UnitIdentify<out Any>) {
    databaseComponent.findByIdentify(identify).filter {info->
      if (componentClasses.contain {info.instanceOf(it)}) return@filter true
      loggerComponent.catch(null) {
        removeInfo(info)
      }
      false
    }
  }
  
  private fun removeInfo(info: UnitInfo<out Any>) {
    databaseComponent.unregisterInfo(info)
    closerComponent.closeUnit(info)
  }
  
  override fun <T: Any> getUnitList(identify: UnitIdentify<T>): List<T> {
    val list = mutableListOf<T>()
    databaseComponent.findByIdentify(identify).forEach {
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