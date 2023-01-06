package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.ComponentDatabase
import net.kigawa.kutil.unit.api.extention.UnitCloser

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