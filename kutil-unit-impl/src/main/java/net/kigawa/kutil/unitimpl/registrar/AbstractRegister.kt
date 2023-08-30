package net.kigawa.kutil.unitimpl.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.UnitRegistrar
import net.kigawa.kutil.unitapi.options.RegisterOptions

abstract class AbstractRegister(
  private val getterComponent: UnitStoreComponent,
  private val databaseComponent: UnitDatabaseComponent,
  private val container: UnitContainer,
): UnitRegistrar {
  protected fun <T: Any> registerTask(identify: UnitIdentify<T>, registerOptions: RegisterOptions): ()->T {
    val getter = getterComponent.findStore(identify, registerOptions)
    val info = net.kigawa.kutil.unitimpl.UnitInfoImpl(identify, getter)
    
    databaseComponent.findByIdentify(info.identify).forEach {
      container.removeUnit(it.identify)
    }
    
    databaseComponent.registerInfo(info, registerOptions)
    return {
      info.initGetter(InitStack())
      info.get()
    }
  }
}