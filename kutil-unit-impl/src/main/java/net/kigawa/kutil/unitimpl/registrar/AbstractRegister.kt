package net.kigawa.kutil.unitimpl.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.component.UnitDatabaseComponent
import net.kigawa.kutil.unitapi.component.UnitStoreComponent
import net.kigawa.kutil.unitapi.component.container.UnitContainer
import net.kigawa.kutil.unitapi.exception.UnitRegisterException
import net.kigawa.kutil.unitapi.extention.UnitRegistrar
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitimpl.UnitInfoImpl

abstract class AbstractRegister(
  private val getterComponent: UnitStoreComponent,
  private val databaseComponent: UnitDatabaseComponent,
  private val container: UnitContainer,
) : UnitRegistrar {
  protected fun <T : Any> registerTask(identify: UnitIdentify<T>, registerOptions: RegisterOptions): () -> T {
    val store = getterComponent.findStore(identify, registerOptions)
    val info = UnitInfoImpl(identify, store)

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