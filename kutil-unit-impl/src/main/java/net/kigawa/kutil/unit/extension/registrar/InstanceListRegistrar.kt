package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.extension.registeroption.InstanceOption
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions

@LateInit
open class InstanceListRegistrar(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
):
  AbstractRegister(getterComponent, databaseComponent, container) {
  fun register(identifies: List<Any>) {
    identifies.map {
      registerTask(UnitIdentify(it.javaClass, null), RegisterOptions(InstanceOption(it)))
    }.forEach {
      it()
    }
  }
}