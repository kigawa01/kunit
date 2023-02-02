package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions

@LateInit
open class ListRegistrar(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent,
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