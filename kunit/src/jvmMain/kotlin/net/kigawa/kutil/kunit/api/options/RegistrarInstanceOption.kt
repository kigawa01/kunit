package net.kigawa.kutil.kunit.api.options

class RegistrarInstanceOption(val instance: Any): RegisterOption {
  override fun match(clazz: Class<out Any>): Boolean {
    return false
  }
}