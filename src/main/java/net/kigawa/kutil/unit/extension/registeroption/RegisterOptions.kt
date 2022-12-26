package net.kigawa.kutil.unit.extension.registeroption

import net.kigawa.kutil.unit.util.Util

class RegisterOptions(vararg option: RegisterOption) {
  private val options = mutableListOf(*option)
  
  fun <T> firstOrNull(optionClass: Class<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return options.firstOrNull {Util.instanceOf(it.javaClass, optionClass)} as T?
  }
  
  fun contain(option: RegisterOption): Boolean {
    return options.contains(option)
  }
  
  fun match(option: RegisterOption, clazz: Class<out Any>): Boolean {
    if (option.match(clazz)) return true
    if (contain(option)) return true
    return false
  }
}