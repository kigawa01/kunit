package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.extension.registeroption.InstanceOption
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.extention.RegisterOptions
import net.kigawa.kutil.unitapi.extention.UnitRegistrar

@LateInit
class InstanceRegistrar(private val classRegistrar: ClassRegistrar): UnitRegistrar {
  fun register(instance: Any, name: String?) {
    classRegistrar.register(instance.javaClass, name, RegisterOptions(InstanceOption(instance)))
  }
  
  fun register(instance: Any) {
    register(instance, null)
  }
}