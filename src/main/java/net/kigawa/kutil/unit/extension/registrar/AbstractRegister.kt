package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponent
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.component.getter.UnitGetterComponent
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

abstract class AbstractRegister(
  private val getterComponent: UnitGetterComponent,
  private val databaseComponent: UnitInfoDatabaseComponent,
): UnitRegistrar {
  protected fun registerTask(identify: UnitIdentify<out Any>, registerOptions: RegisterOptions): ()->Unit {
    val getter = getterComponent.findGetter(identify, registerOptions)
    val info = UnitInfo.create(identify, getter)
    databaseComponent.registerInfo(info, registerOptions)
    return {info.getter.init(identify, InitStack())}
  }
}