package net.kigawa.kutil.unitapi.registrar

interface InstanceRegistrar {
  fun register(instance: Any, name: String?)
  fun register(instance: Any)
}