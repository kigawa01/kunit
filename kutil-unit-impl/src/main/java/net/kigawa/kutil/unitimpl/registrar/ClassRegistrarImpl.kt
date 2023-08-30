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
): AbstractRegister(getterComponent, databaseComponent, container), ClassRegistrar {
  override fun <T: Any> register(unitClass: Class<T>): T {
    return register(unitClass, null)
  }
  
  override fun <T: Any> register(unitClass: Class<T>, name: String?): T {
    return register(unitClass, name, RegisterOptions())
  }
  
  override fun <T: Any> register(unitClass: Class<T>, name: String?, registerOptions: RegisterOptions): T {
    return register(UnitIdentify(unitClass, name), registerOptions)
  }
  
  override fun <T: Any> register(identify: UnitIdentify<T>): T {
    return register(identify, RegisterOptions())
  }
  
  override fun <T: Any> register(identify: UnitIdentify<T>, registerOptions: RegisterOptions): T {
    return registerTask(identify, registerOptions).invoke()
  }
}