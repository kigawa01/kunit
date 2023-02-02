package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.UnitException
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.UnitInjector
import java.util.concurrent.Future

@LateInit
class UnitInjectorComponentImpl(
  container: UnitContainer,
  private val database: ComponentDatabase, loggerComponent: UnitLoggerComponent,
): UnitInjectorComponent, ComponentHolderImpl<UnitInjector>(container, database, loggerComponent) {
  fun addExecutor(executor: UnitInjector) {
    database.registerComponent(executor)
    classes.add(executor.javaClass)
  }
  
  override fun <T: Any> findUnitAsync(identify: UnitIdentify<T>, stack: InitStack): Future<T> {
    return lastMap {
      it.findUnitAsync(identify, stack)
    } ?: throw UnitException("injector is not found", identify, stack)
  }
}