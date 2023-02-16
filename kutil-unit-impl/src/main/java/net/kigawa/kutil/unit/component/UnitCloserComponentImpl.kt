package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.util.LocaleBuilder
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.*
import java.util.*
import java.util.logging.Level

@LateInit
class UnitCloserComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  database: ComponentDatabase,
  private val closeFilterComponent: PreCloseFilterComponent,
): UnitCloserComponent, ComponentHolderImpl<UnitCloser>(container, database, loggerComponent) {
  override fun closeUnit(info: UnitInfo<out Any>) {
    closeFilterComponent.filter(info)
    last {
      try {
        it.closeUnit(info)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder(Locale.ENGLISH, "could not close unit").toString(),
            listOf(e),
            listOf(info, it)
          )
        )
        false
      }
    }
  }
}