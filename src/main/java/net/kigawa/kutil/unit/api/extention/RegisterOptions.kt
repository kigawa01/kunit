package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.util.Options

class RegisterOptions(vararg option: RegisterOption): Options<RegisterOption>(*option) {
  fun match(option: RegisterOption, clazz: Class<out Any>): Boolean {
    if (option.match(clazz)) return true
    if (contain(option)) return true
    return false
  }
  
  override fun toString(): String {
    return "RegisterOptions(options=$options)"
  }
}