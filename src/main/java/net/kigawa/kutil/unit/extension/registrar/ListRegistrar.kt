package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.component.database.UnitDatabaseComponent
import net.kigawa.kutil.unit.component.getter.UnitGetterComponent
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

open class ListRegistrar(getterComponent: UnitGetterComponent, databaseComponent: UnitDatabaseComponent):
  AbstractRegister(getterComponent, databaseComponent) {
  fun register(identifies: List<UnitIdentify<out Any>>) {
    identifies.map {
      registerTask(it, RegisterOptions())
    }.forEach {
      it()
    }
  }
}