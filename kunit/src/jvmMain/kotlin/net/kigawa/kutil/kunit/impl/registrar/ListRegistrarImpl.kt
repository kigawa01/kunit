package net.kigawa.kutil.kunit.impl.registrar

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.component.UnitStoreComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.api.registrar.ListRegistrar

@LateInit
open class ListRegistrarImpl(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
): AbstractRegister(getterComponent, databaseComponent, container), ListRegistrar{
  override fun register(identifies: List<UnitIdentify<out Any>>) {
    identifies.map {
      registerTask(it, RegisterOptions())
    }.forEach {
      it()
    }
  }
}