package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.UnitInfoImpl
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.extention.UnitRegistrar

abstract class AbstractRegister(
  private val getterComponent: UnitStoreComponent,
  private val databaseComponent: UnitDatabaseComponent,
  private val container: UnitContainer,
): UnitRegistrar {
  protected fun registerTask(identify: UnitIdentify<out Any>, registerOptions: RegisterOptions): ()->Unit {
    val getter = getterComponent.findStore(identify, registerOptions)
    val info = UnitInfoImpl(identify, getter)
    
    databaseComponent.findByIdentify(info.identify).forEach {
      container.removeUnit(it.identify)
    }
    
    databaseComponent.registerInfo(info, registerOptions)
    return {info.initGetter(InitStack())}
  }
}