package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.unit.api.component.UnitStoreComponent
import net.kigawa.kutil.unit.api.extention.RegisterOptions

@LateInit
open class ClassRegistrar(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
):
  AbstractRegister(getterComponent, databaseComponent, container) {
  fun register(unitClass: Class<out Any>) {
    register(unitClass, null)
  }
  
  fun register(unitClass: Class<out Any>, name: String?) {
    register(unitClass, name, RegisterOptions())
  }
  
  fun register(unitClass: Class<out Any>, name: String?, registerOptions: RegisterOptions) {
    register(UnitIdentify(unitClass, name), registerOptions)
  }
  
  fun register(identify: UnitIdentify<out Any>) {
    register(identify, RegisterOptions())
  }
  
  fun register(identify: UnitIdentify<out Any>, registerOptions: RegisterOptions) {
    registerTask(identify, registerOptions).invoke()
  }
}