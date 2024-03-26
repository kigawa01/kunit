package net.kigawa.kutil.kunit.impl.registrar

import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitLoggerComponent
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.api.options.RegistrarInstanceOption
import net.kigawa.kutil.kunit.api.registrar.InstanceRegistrar
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