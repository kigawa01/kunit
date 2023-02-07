package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.registrar.ListRegistrar

@LateInit
open class ListRegistrarImpl(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
): AbstractRegister(getterComponent, databaseComponent, container) ,ListRegistrar{
  override fun register(identifies: List<UnitIdentify<out Any>>) {
    identifies.map {
      registerTask(it, RegisterOptions())
    }.forEach {
      it()
    }
  }
}