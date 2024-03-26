package net.kigawa.kutil.kunit.impl.registrar

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.component.UnitStoreComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.api.options.RegistrarInstanceOption
import net.kigawa.kutil.kunit.api.registrar.InstanceListRegistrar

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