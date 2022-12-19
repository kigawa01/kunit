package net.kigawa.kutil.unit.extension.registeroption

import net.kigawa.kutil.unit.util.Util

class RegisterOptions(vararg option: RegisterOption) {
  private val options = mutableListOf(*option)
  
  fun <T> find(optionClass: Class<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return options.firstOrNull {Util.instanceOf(it.javaClass, optionClass)} as T?
  }
}