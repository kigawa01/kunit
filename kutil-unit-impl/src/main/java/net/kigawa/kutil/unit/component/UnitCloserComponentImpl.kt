package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.ComponentDatabase
import net.kigawa.kutil.unitapi.extention.UnitCloser

@LateInit
class UnitCloserComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  database: ComponentDatabase,
): UnitCloserComponent, ComponentHolderImpl<UnitCloser>(container, database, loggerComponent) {
  override fun closeUnit(identify: UnitInfo<out Any>) {
    last {
      return@last loggerComponent.catch(false) {
        it.closeUnit(identify)
      }
    }
  }
}