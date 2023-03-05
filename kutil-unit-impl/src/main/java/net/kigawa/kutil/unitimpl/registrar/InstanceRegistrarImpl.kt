package net.kigawa.kutil.unitimpl.registrar

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.UnitLoggerComponent
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.options.RegistrarInstanceOption
import net.kigawa.kutil.unitapi.registrar.InstanceRegistrar
import java.util.logging.Level

@LateInit
class InstanceRegistrarImpl(
  private val classRegistrar: ClassRegistrarImpl,
  private val loggerComponent: UnitLoggerComponent,
): InstanceRegistrar {
  override fun register(instance: Any, name: String?) {
    if (instance is Class<*>)
      loggerComponent.log(Level.WARNING, "class registered with instance registrar", null, instance)
    classRegistrar.register(instance.javaClass, name, RegisterOptions(RegistrarInstanceOption(instance)))
  }
  
  override fun register(instance: Any) {
    register(instance, null)
  }
}