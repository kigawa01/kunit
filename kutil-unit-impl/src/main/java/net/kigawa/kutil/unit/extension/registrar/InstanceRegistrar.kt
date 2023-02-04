package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unitapi.options.RegistrarInstanceOption
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.extention.UnitRegistrar

@LateInit
class InstanceRegistrar(private val classRegistrar: ClassRegistrar): UnitRegistrar {
  fun register(instance: Any, name: String?) {
    classRegistrar.register(instance.javaClass, name, RegisterOptions(RegistrarInstanceOption(instance)))
  }
  
  fun register(instance: Any) {
    register(instance, null)
  }
}