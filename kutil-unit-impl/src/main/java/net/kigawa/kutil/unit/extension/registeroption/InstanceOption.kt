package net.kigawa.kutil.unit.extension.registeroption

import net.kigawa.kutil.unitapi.options.RegisterOption

class InstanceOption(val instance: Any): RegisterOption {
  override fun match(clazz: Class<out Any>): Boolean {
    return false
  }
}