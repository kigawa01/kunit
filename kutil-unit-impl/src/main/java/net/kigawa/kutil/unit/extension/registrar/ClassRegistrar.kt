package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions

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