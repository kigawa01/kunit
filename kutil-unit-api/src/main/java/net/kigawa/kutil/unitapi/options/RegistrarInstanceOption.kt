package net.kigawa.kutil.unitapi.options

class RegistrarInstanceOption(val instance: Any): RegisterOption {
  override fun match(clazz: Class<out Any>): Boolean {
    return false
  }
}