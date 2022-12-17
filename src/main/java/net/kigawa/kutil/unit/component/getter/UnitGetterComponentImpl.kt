package net.kigawa.kutil.unit.component.getter

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOption

class UnitGetterComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): UnitGetterComponent {
  private val getterClasses = ConcurrentList<Class<out UnitGetter<Any>>>()
  override fun addGetter(getterClass: Class<out UnitGetter<Any>>) {
    getterClasses.add(getterClass)
  }
  
  override fun removeGetter(getterClass: Class<out UnitGetter<Any>>) {
    getterClasses.remove(getterClass)
  }
  
  override fun <T: Any> findGetter(identify: UnitIdentify<T>, options: List<RegisterOption>): UnitGetter<T> {
    for (getterClass in getterClasses.reversed()) {
      val getter = loggerComponent.catch(null, "") {
        container.getUnit(getterClass)
      } ?: continue
      
      @Suppress("UNCHECKED_CAST")
      if (loggerComponent.catch(false, "") {
          getter.register(identify,options)
        }) return getter as UnitGetter<T>
    }
    throw UnitException("getter is not found", identify, options)
  }
}