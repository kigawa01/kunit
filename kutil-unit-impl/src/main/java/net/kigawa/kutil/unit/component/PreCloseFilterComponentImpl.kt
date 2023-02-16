package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.util.LocaleBuilder
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.*
import java.util.logging.Level

@LateInit
class PreCloseFilterComponentImpl(
  container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
  database: ComponentDatabase,
): PreCloseFilterComponent, ComponentHolderImpl<PreCloseFilter>(container, database, loggerComponent) {
  
  override fun filter(info: UnitInfo<out Any>) {
    forEach {
      try {
        it.filter(info)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder("there is an exception in pre close filter").toString(),
            listOf(e),
            listOf(info)
          )
        )
      }
    }
  }
}