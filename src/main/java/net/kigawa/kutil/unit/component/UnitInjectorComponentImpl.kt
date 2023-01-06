package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.ComponentInfoDatabase
import net.kigawa.kutil.unit.api.extention.UnitInjector
import net.kigawa.kutil.unit.exception.UnitException
import java.util.concurrent.Future

@LateInit
class UnitInjectorComponentImpl(
  container: UnitContainer,
  private val database: ComponentInfoDatabase, loggerComponent: UnitLoggerComponent,
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