package net.kigawa.kutil.unit.extension.registeroption

import net.kigawa.kutil.unitapi.extention.RegisterOption

class InstanceOption(val instance: Any): RegisterOption {
  override fun match(clazz: Class<out Any>): Boolean {
    return false
  }
}