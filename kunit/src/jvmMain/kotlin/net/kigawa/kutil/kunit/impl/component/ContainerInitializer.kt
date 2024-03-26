package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.component.*
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.impl.extension.AutoCloseAbleCloser
import net.kigawa.kutil.kunit.impl.extension.database.ComponentDatabaseImpl
import net.kigawa.kutil.kunit.impl.extension.factory.KotlinObjectFactory
import net.kigawa.kutil.kunit.impl.extension.finder.InitGetFinder
import net.kigawa.kutil.kunit.impl.extension.initializedfilter.FieldInjectFilter
import net.kigawa.kutil.kunit.impl.extension.initializedfilter.MethodInjectFilter
import net.kigawa.kutil.kunit.impl.extension.preinitfilter.AnnotationPreInitFilter
import net.kigawa.kutil.kunit.impl.registrar.*

class ContainerInitializer(unitContainer: UnitContainerImpl) {
  private val finderComponent: net.kigawa.kutil.kunit.impl.component.UnitFinderComponentImpl
  private val storeComponent: net.kigawa.kutil.kunit.impl.component.UnitStoreComponentImpl
  private val factoryComponent: net.kigawa.kutil.kunit.impl.component.UnitFactoryComponentImpl
  private val preInitFilterComponent: net.kigawa.kutil.kunit.impl.component.PreInitFilterComponentImpl
  private val closerComponent: UnitCloserComponent
  private val initializedFilterComponent: net.kigawa.kutil.kunit.impl.component.InitializedFilterComponentImpl
  private val loggerComponent: net.kigawa.kutil.kunit.impl.component.UnitLoggerComponentImpl
  private val databaseComponent: net.kigawa.kutil.kunit.impl.component.UnitDatabaseComponentImpl
  private val container: UnitContainerImpl
  private val componentDatabase: ComponentDatabase
  private val preCloseFilterComponent: PreCloseFilterComponent

  init {
    componentDatabase = ComponentDatabaseImpl()
    container = addUnit(unitContainer, unitContainer.name)
    databaseComponent = initDatabase(container, componentDatabase)
    loggerComponent = initLogger(container, componentDatabase)
    initializedFilterComponent = addUnit(
      net.kigawa.kutil.kunit.impl.component.InitializedFilterComponentImpl(
        componentDatabase,
        container
      )
    )
    preInitFilterComponent = addUnit(
      net.kigawa.kutil.kunit.impl.component.PreInitFilterComponentImpl(
        container,
        componentDatabase
      )
    )
    factoryComponent =
      initFactory(container, loggerComponent, componentDatabase, initializedFilterComponent, preInitFilterComponent)
    storeComponent = initStore(container, loggerComponent, factoryComponent, componentDatabase)
    finderComponent = initFinder(container, databaseComponent, componentDatabase, loggerComponent)
    addUnit(net.kigawa.kutil.kunit.impl.component.UnitConfigComponentImpl())
    preCloseFilterComponent =
      net.kigawa.kutil.kunit.impl.component.PreCloseFilterComponentImpl(container, componentDatabase)
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
    componentDatabase.registerComponentClass(SelectionRegistrarImpl::class.java)
    componentDatabase.registerComponentClass(ResourceRegistrarImpl::class.java)
    componentDatabase.registerComponentClass(InstanceListRegistrarImpl::class.java)
  }

  private fun initDatabase(
    container: UnitContainerImpl,
    componentDatabase: ComponentDatabase,
  ): net.kigawa.kutil.kunit.impl.component.UnitDatabaseComponentImpl {
    val result = addUnit(net.kigawa.kutil.kunit.impl.component.UnitDatabaseComponentImpl(componentDatabase))
    container.databaseComponent = result
    return result
  }

  private fun initCloser(
    container: UnitContainerImpl,
    loggerComponent: UnitLoggerComponent,
    componentDatabase: ComponentDatabase,
    preCloseFilterComponent: PreCloseFilterComponent,
  ): net.kigawa.kutil.kunit.impl.component.UnitCloserComponentImpl {
    val result =
      addUnit(
        net.kigawa.kutil.kunit.impl.component.UnitCloserComponentImpl(
          container,
          componentDatabase,
          preCloseFilterComponent
        )
      )
    container.closerComponent = result
    return result
  }

  private fun initFinder(
    container: UnitContainerImpl,
    databaseComponent: net.kigawa.kutil.kunit.impl.component.UnitDatabaseComponentImpl,
    componentDatabase: ComponentDatabase,
    loggerComponent: UnitLoggerComponent,
  ): net.kigawa.kutil.kunit.impl.component.UnitFinderComponentImpl {
    val result = addUnit(
      net.kigawa.kutil.kunit.impl.component.UnitFinderComponentImpl(
        container,
        componentDatabase,
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
  ): net.kigawa.kutil.kunit.impl.component.UnitStoreComponentImpl {
    val result = addUnit(
      net.kigawa.kutil.kunit.impl.component.UnitStoreComponentImpl(
        container,
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
  ): net.kigawa.kutil.kunit.impl.component.UnitFactoryComponentImpl {
    return addUnit(
      net.kigawa.kutil.kunit.impl.component.UnitFactoryComponentImpl(
        container,
        componentDatabase,
        initializedFilterComponent,
        preInitFilterComponent
      )
    )
  }

  private fun initLogger(
    container: UnitContainerImpl,
    componentDatabase: ComponentDatabase,
  ): net.kigawa.kutil.kunit.impl.component.UnitLoggerComponentImpl {
    val result = addUnit(net.kigawa.kutil.kunit.impl.component.UnitLoggerComponentImpl(container, componentDatabase))
    return result
  }

  private fun <T : Any> addUnit(unit: T, name: String? = null): T {
    componentDatabase.registerComponent(unit, name)
    return unit
  }
}