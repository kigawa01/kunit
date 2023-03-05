package net.kigawa.kutil.unitimpl.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.options.RegistrarInstanceOption
import net.kigawa.kutil.unitapi.registrar.InstanceListRegistrar

@LateInit
open class InstanceListRegistrarImpl(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
): AbstractRegister(getterComponent, databaseComponent, container), InstanceListRegistrar {
  override fun register(identifies: List<Any>) {
    identifies.map {
      registerTask(UnitIdentify(it.javaClass, null), RegisterOptions(RegistrarInstanceOption(it)))
    }.forEach {
      it()
    }
  }
}