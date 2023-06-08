package net.kigawa.kutil.unitimpl.component

import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitimpl.extension.AutoCloseAbleCloser
import net.kigawa.kutil.unitimpl.extension.database.ComponentDatabaseImpl
import net.kigawa.kutil.unitimpl.extension.factory.KotlinObjectFactory
import net.kigawa.kutil.unitimpl.extension.finder.InitGetFinder
import net.kigawa.kutil.unitimpl.extension.initializedfilter.FieldInjectFilter
import net.kigawa.kutil.unitimpl.extension.initializedfilter.MethodInjectFilter
import net.kigawa.kutil.unitimpl.extension.preinitfilter.AnnotationPreInitFilter
import net.kigawa.kutil.unitimpl.registrar.*

class ContainerInitializer(unitContainer: net.kigawa.kutil.unitimpl.component.UnitContainerImpl) {
  private val finderComponent: net.kigawa.kutil.unitimpl.component.UnitFinderComponentImpl
  private val storeComponent: net.kigawa.kutil.unitimpl.component.UnitStoreComponentImpl
  private val factoryComponent: net.kigawa.kutil.unitimpl.component.UnitFactoryComponentImpl
  private val preInitFilterComponent: PreInitFilterComponentImpl
  private val closerComponent: UnitCloserComponent
  private val initializedFilterComponent: InitializedFilterComponentImpl
  private val loggerComponent: net.kigawa.kutil.unitimpl.component.UnitLoggerComponentImpl
  private val databaseComponent: net.kigawa.kutil.unitimpl.component.UnitDatabaseComponentImpl
  private val container: net.kigawa.kutil.unitimpl.component.UnitContainerImpl
  private val componentDatabase: ComponentDatabase
  private val preCloseFilterComponent: PreCloseFilterComponent
  
  init {
    componentDatabase = ComponentDatabaseImpl()
    container = addUnit(unitContainer, unitContainer.name)
    databaseComponent = initDatabase(container, componentDatabase)
    loggerComponent = initLogger(container, componentDatabase, databaseComponent)
    initializedFilterComponent = addUnit(
      InitializedFilterComponentImpl(
        componentDatabase,
        loggerComponent,
        container
      )
    )
    preInitFilterComponent = addUnit(
      PreInitFilterComponentImpl(
        container,
        componentDatabase,
        loggerComponent
      )
    )
    factoryComponent =
      initFactory(container, loggerComponent, componentDatabase, initializedFilterComponent, preInitFilterComponent)
    storeComponent = initStore(container, loggerComponent, factoryComponent, componentDatabase)
    finderComponent = initFinder(container, databaseComponent, componentDatabase, loggerComponent)
    addUnit(UnitConfigComponentImpl())
    preCloseFilterComponent =
      PreCloseFilterComponentImpl(container, loggerComponent, componentDatabase)
    closerComponent = initCloser(container, loggerComponent, componentDatabase, preCloseFilterComponent)
    
    registerExtension()
  }
  
  private fun registerExtension() {
    factoryComponent.add(KotlinObjectFactory::class.java)
    
    // 拡張機能の登録
    closerComponent.add(AutoCloseAbleCloser::class.java)
    initializedFilterComponent.add(FieldInjectFilter::class.java)
    initializedFilterComponent.add(MethodInjectFilter::class.java)
    preInitFilterComponent.add(AnnotationPreInitFilter::class.java)
    
    componentDatabase.registerComponentClass(ClassRegistrarImpl::class.java)
    componentDatabase.registerComponentClass(ListRegistrarImpl::class.java)
    componentDatabase.registerComponentClass(InstanceRegistrarImpl::class.java)
    componentDatabase.registerComponentClass(ResourceRegistrarImpl::class.java)
    componentDatabase.registerComponentClass(InstanceListRegistrarImpl::class.java)
  }
  
  private fun initDatabase(
    container: net.kigawa.kutil.unitimpl.component.UnitContainerImpl,
    componentDatabase: ComponentDatabase,
  ): net.kigawa.kutil.unitimpl.component.UnitDatabaseComponentImpl {
    val result = addUnit(net.kigawa.kutil.unitimpl.component.UnitDatabaseComponentImpl(componentDatabase))
    container.databaseComponent = result
    return result
  }
  
  private fun initCloser(
    container: net.kigawa.kutil.unitimpl.component.UnitContainerImpl,
    loggerComponent: UnitLoggerComponent,
    componentDatabase: ComponentDatabase,
    preCloseFilterComponent: PreCloseFilterComponent,
  ): net.kigawa.kutil.unitimpl.component.UnitCloserComponentImpl {
    val result =
      addUnit(
        net.kigawa.kutil.unitimpl.component.UnitCloserComponentImpl(
          container,
          loggerComponent,
          componentDatabase,
          preCloseFilterComponent
        )
      )
    container.closerComponent = result
    return result
  }
  
  private fun initFinder(
    container: net.kigawa.kutil.unitimpl.component.UnitContainerImpl,
    databaseComponent: net.kigawa.kutil.unitimpl.component.UnitDatabaseComponentImpl,
    componentDatabase: ComponentDatabase,
    loggerComponent: UnitLoggerComponent,
  ): net.kigawa.kutil.unitimpl.component.UnitFinderComponentImpl {
    val result = addUnit(
      net.kigawa.kutil.unitimpl.component.UnitFinderComponentImpl(
        container,
        componentDatabase,
        loggerComponent,
        databaseComponent
      )
    )
    result.addFinder(InitGetFinder(databaseComponent))
    container.finderComponent = result
    return result
  }
  
  private fun initStore(
    container: UnitContainer,
    loggerComponent: UnitLoggerComponent,
    factoryComponent: UnitFactoryComponent,
    componentDatabase: ComponentDatabase,
  ): net.kigawa.kutil.unitimpl.component.UnitStoreComponentImpl {
    val result = addUnit(
      net.kigawa.kutil.unitimpl.component.UnitStoreComponentImpl(
        container,
        loggerComponent,
        factoryComponent,
        componentDatabase
      )
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
  ): net.kigawa.kutil.unitimpl.component.UnitFactoryComponentImpl {
    return addUnit(
      net.kigawa.kutil.unitimpl.component.UnitFactoryComponentImpl(
        container,
        loggerComponent,
        componentDatabase,
        initializedFilterComponent,
        preInitFilterComponent
      )
    )
  }
  
  private fun initLogger(
    container: net.kigawa.kutil.unitimpl.component.UnitContainerImpl,
    componentDatabase: ComponentDatabase,
    databaseComponent: net.kigawa.kutil.unitimpl.component.UnitDatabaseComponentImpl,
  ): net.kigawa.kutil.unitimpl.component.UnitLoggerComponentImpl {
    val result = addUnit(net.kigawa.kutil.unitimpl.component.UnitLoggerComponentImpl(container, componentDatabase))
    databaseComponent.setLoggerComponent(result)
    container.loggerComponent = result
    return result
  }
  
  private fun <T: Any> addUnit(unit: T, name: String? = null): T {
    componentDatabase.registerComponent(unit, name)
    return unit
  }
}