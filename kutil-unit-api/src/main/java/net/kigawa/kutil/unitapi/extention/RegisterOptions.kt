package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.util.Options

class RegisterOptions(vararg option: RegisterOption): Options<RegisterOption>(*option) {
  
  override fun toString(): String {
    return "RegisterOptions(options=$options)"
  }
}