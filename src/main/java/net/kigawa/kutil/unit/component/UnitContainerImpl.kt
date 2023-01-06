package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.extension.*
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.factory.KotlinObjectFactory
import net.kigawa.kutil.unit.extension.factory.NormalFactory
import net.kigawa.kutil.unit.extension.initializedfilter.FieldInjectFilter
import net.kigawa.kutil.unit.extension.initializedfilter.MethodInjectFilter
import net.kigawa.kutil.unit.extension.preinitfilter.DependencyAnnotationFilter
import net.kigawa.kutil.unit.extension.registrar.*
import java.util.concurrent.Callable

@LateInit
class UnitContainerImpl(
  private val parent: UnitContainer? = null,
): UnitContainer {
  private val closerComponent: UnitCloserComponent
  private val loggerComponent: UnitLoggerComponent
  private val databaseComponent: UnitDatabaseComponent
  
  init {
    // 登録に最低限必要
    val componentDatabase = ComponentInfoDatabase()
    initComponent(componentDatabase) {this}
    
    databaseComponent = initComponent(componentDatabase) {UnitDatabaseComponentImpl(componentDatabase)}
    loggerComponent = initComponent(componentDatabase) {UnitLoggerComponentImpl(this, componentDatabase)}
    
    databaseComponent.setLoggerComponent(loggerComponent)
    
    
    val initializedFilterComponent = initComponent(componentDatabase) {
      InitializedFilterComponentImpl(componentDatabase, loggerComponent, this)
    }
    val preInitFilterComponent = initComponent(componentDatabase) {
      PreInitFilterComponentImpl(this, componentDatabase, loggerComponent)
    }
    val factoryComponent = initComponent(componentDatabase) {
      UnitFactoryComponentImpl(
        this,
        loggerComponent,
        componentDatabase,
        initializedFilterComponent,
        preInitFilterComponent
      )
    }
    val asyncComponent = initComponent(componentDatabase) {
      UnitAsyncComponentImpl(this, loggerComponent, componentDatabase)
    }
    val getterComponent = initComponent(componentDatabase) {
      UnitGetterComponentImpl(this, loggerComponent, factoryComponent, asyncComponent, componentDatabase)
    }
    
    
    componentDatabase.getterComponent = getterComponent
    
    val reflectionComponent = initComponent(componentDatabase) {
      UnitInjectorComponentImpl(this, componentDatabase, loggerComponent)
    }
    initComponent(componentDatabase) {UnitConfigComponentImpl()}
    
    factoryComponent.addFactory(NormalFactory(reflectionComponent))
    reflectionComponent.addExecutor(ContainerInjector(databaseComponent))
    
    factoryComponent.add(KotlinObjectFactory::class.java)
    asyncComponent.addAsyncExecutor(SyncedExecutorUnit::class.java)
    
    // その他
    closerComponent = initComponent(componentDatabase) {
      UnitCloserComponentImpl(this, loggerComponent, componentDatabase)
    }
    
    // 拡張機能の登録
    closerComponent.addCloser(AutoCloseAbleCloser::class.java)
    initializedFilterComponent.add(FieldInjectFilter::class.java)
    initializedFilterComponent.add(MethodInjectFilter::class.java)
    preInitFilterComponent.add(DependencyAnnotationFilter::class.java)
    
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
    databaseComponent.findByIdentify(identify).forEach {info->
      loggerComponent.catch(null) {
        removeInfo(info)
      }
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