package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.options.RegistrarInstanceOption
import net.kigawa.kutil.unitapi.registrar.InstanceRegistrar

@LateInit
class InstanceRegistrarImpl(private val classRegistrar: ClassRegistrarImpl): InstanceRegistrar {
  override fun register(instance: Any, name: String?) {
    classRegistrar.register(instance.javaClass, name, RegisterOptions(RegistrarInstanceOption(instance)))
  }
  
  override fun register(instance: Any) {
    register(instance, null)
  }
}