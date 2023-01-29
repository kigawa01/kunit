package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.api.component.UnitGetterComponent
import net.kigawa.kutil.unit.api.component.UnitInfo
import net.kigawa.kutil.unit.api.extention.UnitRegistrar
import net.kigawa.kutil.unit.api.extention.RegisterOptions

abstract class AbstractRegister(
  private val getterComponent: UnitGetterComponent,
  private val databaseComponent: UnitDatabaseComponent,
  private val container: UnitContainer,
): UnitRegistrar {
  protected fun registerTask(identify: UnitIdentify<out Any>, registerOptions: RegisterOptions): ()->Unit {
    val getter = getterComponent.findGetter(identify, registerOptions)
    val info = UnitInfo.create(identify, getter)
    
    databaseComponent.findByIdentify(info.identify).forEach {
      container.removeUnit(it.identify)
    }
    
    databaseComponent.registerInfo(info, registerOptions)
    return {info.initGetter(InitStack())}
  }
}