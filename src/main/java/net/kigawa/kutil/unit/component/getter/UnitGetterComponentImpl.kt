package net.kigawa.kutil.unit.component.getter

import net.kigawa.kutil.unit.component.async.AsyncComponent
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponent
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.UnitClassList
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.getter.*
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions
import net.kigawa.kutil.unit.extension.registrar.ClassRegistrar

class UnitGetterComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
  factoryComponent: UnitFactoryComponent,
  asyncComponent: AsyncComponent,
  componentInfoDatabase: ComponentInfoDatabase,
): UnitGetterComponent {
  private val getterClasses = UnitClassList<UnitGetter>(container)
  
  init {
    val initializeGetter = InitializeGetter(factoryComponent, asyncComponent)
    getterClasses.add(SingletonGetter::class.java)
    componentInfoDatabase.registerComponent(SingletonGetter::class.java, initializeGetter)
    getterClasses.add(InstanceGetter::class.java)
    componentInfoDatabase.registerComponent(InstanceGetter::class.java, initializeGetter)
    getterClasses.add(initializeGetter.javaClass)
    componentInfoDatabase.registerComponent(initializeGetter)
  }
  
  override fun addGetter(getterClass: Class<out UnitGetter>) {
    getterClasses.addContainer(getterClass)
  }
  
  override fun removeGetter(getterClass: Class<out UnitGetter>) {
    getterClasses.removeContainer(getterClass)
  }
  
  override fun findGetter(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitGetter {
    for (getterClass in getterClasses.reversed()) {
      val getter = loggerComponent.catch(null, "") {
        container.getUnit(getterClass)
      } ?: continue
      
      @Suppress("UNCHECKED_CAST")
      if (loggerComponent.catch(false, "") {
          getter.register(identify, options)
        }) return getter
    }
    throw UnitException("getter is not found", identify, options)
  }
}