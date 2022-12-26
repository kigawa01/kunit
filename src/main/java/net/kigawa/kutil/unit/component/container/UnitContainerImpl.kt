package net.kigawa.kutil.unit.component.container

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

class UnitContainerImpl(
  private val parent: UnitContainer? = null,
): UnitContainer {
  private val closerComponent = getUnit(UnitCloserComponent::class.java)
  private val componentClasses = ConcurrentList<Class<out Any>>()
  private val loggerComponent: ContainerLoggerComponent
  private val databaseComponent: UnitDatabaseComponent
  
  init {
    // 登録に最低限必要
    val componentDatabase = ComponentInfoDatabase()
    
    databaseComponent = UnitDatabaseComponentImpl(componentDatabase)
    loggerComponent = ContainerLoggerComponentImpl(this, componentDatabase)
    
    databaseComponent.loggerComponent = loggerComponent
    
    val factoryComponent = UnitFactoryComponentImpl(this, loggerComponent, componentDatabase)
    val asyncComponent = UnitAsyncComponentImpl(this, loggerComponent, componentDatabase)
    val getterComponent =
      UnitGetterComponentImpl(this, loggerComponent, factoryComponent, asyncComponent, componentDatabase)
    
    componentDatabase.getterComponent = getterComponent
    asyncComponent.addAsyncExecutor(SyncedExecutorUnit::class.java)
    factoryComponent.addFactory(NormalFactory::class.java)
    factoryComponent.addFactory(KotlinObjectFactory::class.java)
    
    // その他
    val closerComponent = UnitCloserComponentImpl(this, loggerComponent)
    val unitConfigComponent = UnitConfigComponentImpl()
    val reflectionComponent = UnitReflectionComponentImpl(this, loggerComponent)
    
    // コンポーネントの登録
    componentDatabase.registerComponent(componentDatabase)
    componentDatabase.registerComponent(databaseComponent)
    componentDatabase.registerComponent(loggerComponent)
    componentDatabase.registerComponent(factoryComponent)
    componentDatabase.registerComponent(asyncComponent)
    componentDatabase.registerComponent(getterComponent)
    componentDatabase.registerComponent(closerComponent)
    componentDatabase.registerComponent(unitConfigComponent)
    componentDatabase.registerComponent(reflectionComponent)
    
    // 拡張機能の登録
    closerComponent.addCloser(AutoCloseAbleCloser::class.java)
    reflectionComponent.addExecutor(InjectionReflectionExecutor::class.java)
    
    componentDatabase.registerComponentClass(ClassRegistrar::class.java)
    componentDatabase.registerComponentClass(ListRegistrar::class.java)
    componentDatabase.registerComponentClass(InstanceRegistrar::class.java)
    componentDatabase.registerComponentClass(FileClassRegistrar::class.java)
    componentDatabase.registerComponentClass(JarRegistrar::class.java)
    componentDatabase.registerComponentClass(ResourceRegistrar::class.java)
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