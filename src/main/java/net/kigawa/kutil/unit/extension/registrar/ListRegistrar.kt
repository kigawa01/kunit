package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.unit.api.component.UnitGetterComponent
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

@LateInit
open class ListRegistrar(
  getterComponent: UnitGetterComponent, databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
):
  AbstractRegister(getterComponent, databaseComponent, container) {
  fun register(identifies: List<UnitIdentify<out Any>>) {
    identifies.map {
      registerTask(it, RegisterOptions())
    }.forEach {
      it()
    }
  }
}