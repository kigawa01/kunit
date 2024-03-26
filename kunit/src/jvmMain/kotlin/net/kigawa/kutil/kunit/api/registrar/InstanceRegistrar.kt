package net.kigawa.kutil.kunit.api.registrar

interface InstanceRegistrar {
  fun register(instance: Any, name: String?)
  fun register(instance: Any)
}