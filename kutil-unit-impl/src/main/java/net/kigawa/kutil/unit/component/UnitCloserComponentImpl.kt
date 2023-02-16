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
): UnitCloserComponent, ComponentHolderImpl<UnitCloser>(container, database, loggerComponent) {
  override fun closeUnit(identify: UnitInfo<out Any>) {
    last {
      try {
        it.closeUnit(identify)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder(Locale.ENGLISH, "could not close unit").toString(),
            listOf(e),
            listOf(identify, it)
          )
        )
        false
      }
    }
  }
}