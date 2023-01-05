package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.UnitGetter
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase
import net.kigawa.kutil.unit.extension.getter.*
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

@LateInit
class UnitGetterComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  factoryComponent: UnitFactoryComponent,
  asyncComponent: UnitAsyncComponent,
  private val database: ComponentInfoDatabase,
): UnitGetterComponent {
  private val getterClasses = ConcurrentList<Class<out UnitGetter>>()
  
  init {
    val initializeGetter = InitializeGetter(factoryComponent, asyncComponent)
    getterClasses.add(SingletonGetter::class.java)
    database.registerComponent(SingletonGetter::class.java, initializeGetter)
    getterClasses.add(InstanceGetter::class.java)
    database.registerComponent(InstanceGetter::class.java, initializeGetter)
    getterClasses.add(initializeGetter.javaClass)
    database.registerComponent(initializeGetter)
  }
  
  override fun addGetter(getterClass: Class<out UnitGetter>) {
    database.registerComponentClass(getterClass)
    getterClasses.add(getterClass)
  }
  
  override fun removeGetter(getterClass: Class<out UnitGetter>) {
    getterClasses.remove(getterClass)
    database.unregisterComponent(getterClass)
  }
  
  override fun findGetter(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitGetter {
    for (getterClass in getterClasses.reversed()) {
      val getter = loggerComponent.catch(null) {
        container.getUnit(getterClass)
      } ?: continue
      
      @Suppress("UNCHECKED_CAST")
      if (loggerComponent.catch(false) {
          getter.register(identify, options)
        }) return getter
    }
    throw UnitException("getter is not found", identify, options, getterClasses)
  }
}