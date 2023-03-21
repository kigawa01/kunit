package net.kigawa.kutil.unitimpl.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.registrar.ClassRegistrar

@LateInit
open class ClassRegistrarImpl(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
): AbstractRegister(getterComponent, databaseComponent, container), ClassRegistrar{
  override fun register(unitClass: Class<out Any>) {
    register(unitClass, null)
  }
  
  override fun register(unitClass: Class<out Any>, name: String?) {
    register(unitClass, name, RegisterOptions())
  }
  
  override fun register(unitClass: Class<out Any>, name: String?, registerOptions: RegisterOptions) {
    register(UnitIdentify(unitClass, name), registerOptions)
  }
  
  override fun register(identify: UnitIdentify<out Any>) {
    register(identify, RegisterOptions())
  }
  
  override fun register(identify: UnitIdentify<out Any>, registerOptions: RegisterOptions) {
    registerTask(identify, registerOptions).invoke()
  }
}