package net.kigawa.kutil.kunit.impl.registrar

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.component.UnitStoreComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.exception.UnitRegisterException
import net.kigawa.kutil.kunit.api.extention.UnitRegistrar
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.impl.UnitInfoImpl

abstract class AbstractRegister(
  private val getterComponent: UnitStoreComponent,
  private val databaseComponent: UnitDatabaseComponent,
  private val container: UnitContainer,
) : UnitRegistrar {
  protected fun <T : Any> registerTask(identify: UnitIdentify<T>, registerOptions: RegisterOptions): () -> T {
    val store = getterComponent.findStore(identify, registerOptions)
    val info = net.kigawa.kutil.kunit.impl.UnitInfoImpl(identify, store)

    databaseComponent.findByIdentify(info.identify).forEach {
      container.removeUnit(it.identify)
    }

    databaseComponent.registerInfo(info, registerOptions)
    return {
      info.initGetter(InitStack())
      info.get() ?: throw UnitRegisterException("unit could not get in register", identify)
    }
  }
}