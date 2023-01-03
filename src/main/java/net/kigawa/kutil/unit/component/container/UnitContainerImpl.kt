package net.kigawa.kutil.unit.component.container

import net.kigawa.kutil.unit.annotation.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.component.async.UnitAsyncComponentImpl
import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.closer.UnitCloserComponentImpl
import net.kigawa.kutil.unit.component.config.UnitConfigComponentImpl
import net.kigawa.kutil.unit.component.database.UnitDatabaseComponent
import net.kigawa.kutil.unit.component.database.UnitDatabaseComponentImpl
import net.kigawa.kutil.unit.component.executor.UnitReflectionComponentImpl
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponentImpl
import net.kigawa.kutil.unit.component.getter.UnitGetterComponentImpl
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponentImpl
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.async.SyncedExecutorUnit
import net.kigawa.kutil.unit.extension.closer.AutoCloseAbleCloser
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.executor.InjectionReflectionExecutor
import net.kigawa.kutil.unit.extension.factory.KotlinObjectFactory
import net.kigawa.kutil.unit.extension.factory.NormalFactory
import net.kigawa.kutil.unit.extension.registrar.*
import java.util.concurrent.Callable

@LateInit
class UnitContainerImpl(
  private val parent: UnitContainer? = null,
): UnitContainer {
  private val closerComponent: UnitCloserComponent
  private val componentClasses = ConcurrentList<Class<out Any>>()
  private val loggerComponent: ContainerLoggerComponent
  private val databaseComponent: UnitDatabaseComponent
  
  init {
    // 登録に最低限必要
    val componentDatabase = ComponentInfoDatabase()
    
    databaseComponent = initComponent(componentDatabase) {UnitDatabaseComponentImpl(componentDatabase)}
    loggerComponent = initComponent(componentDatabase) {ContainerLoggerComponentImpl(this, componentDatabase)}
    
    databaseComponent.loggerComponent = loggerComponent
    
    val factoryComponent = initComponent(componentDatabase) {
      UnitFactoryComponentImpl(this, loggerComponent, componentDatabase)
    }
    val asyncComponent = initComponent(componentDatabase) {
      UnitAsyncComponentImpl(this, loggerComponent, componentDatabase)
    }
    val getterComponent = initComponent(componentDatabase) {
      UnitGetterComponentImpl(this, loggerComponent, factoryComponent, asyncComponent, componentDatabase)
    }
    
    
    componentDatabase.getterComponent = getterComponent
    
    val reflectionComponent = initComponent(componentDatabase) {
      UnitReflectionComponentImpl(this, loggerComponent, componentDatabase)
    }
    val configComponent = initComponent(componentDatabase) {UnitConfigComponentImpl()}
    
    factoryComponent.addFactory(NormalFactory(reflectionComponent))
    reflectionComponent.addExecutor(InjectionReflectionExecutor(databaseComponent, configComponent))
    
    factoryComponent.addFactory(KotlinObjectFactory::class.java)
    asyncComponent.addAsyncExecutor(SyncedExecutorUnit::class.java)
    
    // その他
    closerComponent = initComponent(componentDatabase) {
      UnitCloserComponentImpl(this, loggerComponent, componentDatabase)
    }
    
    // 拡張機能の登録
    closerComponent.addCloser(AutoCloseAbleCloser::class.java)
    
    componentDatabase.registerComponentClass(ClassRegistrar::class.java)
    componentDatabase.registerComponentClass(ListRegistrar::class.java)
    componentDatabase.registerComponentClass(InstanceRegistrar::class.java)
    componentDatabase.registerComponentClass(FileClassRegistrar::class.java)
    componentDatabase.registerComponentClass(JarRegistrar::class.java)
    componentDatabase.registerComponentClass(ResourceRegistrar::class.java)
  }
  
  private fun <T: Any> initComponent(componentInfoDatabase: ComponentInfoDatabase, callable: Callable<T>): T {
    val result = callable.call()
    componentInfoDatabase.registerComponent(result)
    return result
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