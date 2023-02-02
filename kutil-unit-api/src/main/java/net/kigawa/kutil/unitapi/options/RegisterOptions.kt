package net.kigawa.kutil.unitapi.options

class RegisterOptions(vararg option: RegisterOption): Options<RegisterOption>(*option) {
  
  override fun toString(): String {
    return "RegisterOptions(options=$options)"
  }
}