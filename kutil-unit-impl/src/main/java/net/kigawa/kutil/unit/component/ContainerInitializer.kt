package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.extension.AutoCloseAbleCloser
import net.kigawa.kutil.unit.extension.ContainerInjector
import net.kigawa.kutil.unit.extension.async.SyncedExecutorUnit
import net.kigawa.kutil.unit.extension.database.ComponentDatabaseImpl
import net.kigawa.kutil.unit.extension.factory.KotlinObjectFactory
import net.kigawa.kutil.unit.extension.factory.NormalFactory
import net.kigawa.kutil.unit.extension.initializedfilter.FieldInjectFilter
import net.kigawa.kutil.unit.extension.initializedfilter.MethodInjectFilter
import net.kigawa.kutil.unit.extension.preinitfilter.DependencyAnnotationFilter
import net.kigawa.kutil.unit.extension.registrar.*
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.ComponentDatabase

class ContainerInitializer(unitContainer: UnitContainerImpl) {
  private val injectorComponent: UnitInjectorComponentImpl
  private val getterComponent: UnitStoreComponentImpl
  private val asyncComponent: UnitAsyncComponentImpl
  private val factoryComponent: UnitFactoryComponentImpl
  private val preInitFilterComponent: PreInitFilterComponentImpl
  private val closerComponent: UnitCloserComponent
  private val initializedFilterComponent: InitializedFilterComponentImpl
  private val loggerComponent: UnitLoggerComponentImpl
  private val databaseComponent: UnitDatabaseComponentImpl
  private val container: UnitContainerImpl
  private val componentDatabase: ComponentDatabase
  
  init {
    componentDatabase = ComponentDatabaseImpl()
    container = addUnit(unitContainer)
    databaseComponent = initDatabase(container, componentDatabase)
    loggerComponent = initLogger(container, componentDatabase, databaseComponent)
    initializedFilterComponent = addUnit(InitializedFilterComponentImpl(componentDatabase, loggerComponent, container))
    preInitFilterComponent = addUnit(PreInitFilterComponentImpl(container, componentDatabase, loggerComponent))
    factoryComponent =
      initFactory(container, loggerComponent, componentDatabase, initializedFilterComponent, preInitFilterComponent)
    asyncComponent = addUnit(UnitAsyncComponentImpl(container, loggerComponent, componentDatabase))
    getterComponent = initGetter()
    injectorComponent = initInjector(factoryComponent, databaseComponent)
    addUnit(UnitConfigComponentImpl())
    closerComponent = initCloser(container, loggerComponent, componentDatabase)
    
    registerExtension()
  }
  
  private fun registerExtension() {
    factoryComponent.add(KotlinObjectFactory::class.java)
    asyncComponent.add(SyncedExecutorUnit::class.java)
    
    // 拡張機能の登録
    closerComponent.add(AutoCloseAbleCloser::class.java)
    initializedFilterComponent.add(FieldInjectFilter::class.java)
    initializedFilterComponent.add(MethodInjectFilter::class.java)
    preInitFilterComponent.add(DependencyAnnotationFilter::class.java)
    
    componentDatabase.registerComponentClass(ClassRegistrar::class.java)
    componentDatabase.registerComponentClass(ListRegistrar::class.java)
    componentDatabase.registerComponentClass(InstanceRegistrar::class.java)
    componentDatabase.registerComponentClass(FileClassRegistrar::class.java)
    componentDatabase.registerComponentClass(JarRegistrar::class.java)
    componentDatabase.registerComponentClass(ResourceRegistrar::class.java)
    componentDatabase.registerComponentClass(InstanceListRegistrar::class.java)
  }
  
  private fun initDatabase(
    container: UnitContainerImpl,
    componentDatabase: ComponentDatabase,
  ): UnitDatabaseComponentImpl {
    val result = addUnit(UnitDatabaseComponentImpl(componentDatabase))
    container.databaseComponent = result
    return result
  }
  
  private fun initCloser(
    container: UnitContainerImpl,
    loggerComponent: UnitLoggerComponent,
    componentDatabase: ComponentDatabase,
  ): UnitCloserComponentImpl {
    val result = addUnit(UnitCloserComponentImpl(container, loggerComponent, componentDatabase))
    container.closerComponent = result
    return result
  }
  
  private fun initInjector(
    factoryComponent: UnitFactoryComponentImpl,
    databaseComponent: UnitDatabaseComponentImpl,
  ): UnitInjectorComponentImpl {
    val result = addUnit(UnitInjectorComponentImpl(container, componentDatabase, loggerComponent))
    factoryComponent.addFactory(NormalFactory(result))
    result.addExecutor(ContainerInjector(databaseComponent))
    return result
  }
  
  private fun initGetter(): UnitStoreComponentImpl {
    val result = addUnit(
      UnitStoreComponentImpl(container, loggerComponent, factoryComponent, asyncComponent, componentDatabase)
    )
    componentDatabase.getterComponent = result
    return result
  }
  
  private fun initFactory(
    container: UnitContainer,
    loggerComponent: UnitLoggerComponent,
    componentDatabase: ComponentDatabase,
    initializedFilterComponent: InitializedFilterComponent,
    preInitFilterComponent: PreInitFilterComponent,
  ): UnitFactoryComponentImpl {
    return addUnit(
      UnitFactoryComponentImpl(
        container,
        loggerComponent,
        componentDatabase,
        initializedFilterComponent,
        preInitFilterComponent
      )
    )
  }
  
  private fun initLogger(
    container: UnitContainerImpl,
    componentDatabase: ComponentDatabase,
    databaseComponent: UnitDatabaseComponentImpl,
  ): UnitLoggerComponentImpl {
    val result = addUnit(UnitLoggerComponentImpl(container, componentDatabase))
    databaseComponent.setLoggerComponent(result)
    container.loggerComponent = result
    return result
  }
  
  private fun <T: Any> addUnit(unit: T): T {
    componentDatabase.registerComponent(unit)
    return unit
  }
}