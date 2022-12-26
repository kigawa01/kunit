package net.kigawa.kutil.unit.extension.registeroption

class InstanceOption(val instance: Any): RegisterOption {
  override fun match(clazz: Class<out Any>): Boolean {
    return false
  }
}