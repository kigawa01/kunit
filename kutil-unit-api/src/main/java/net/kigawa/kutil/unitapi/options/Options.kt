package net.kigawa.kutil.unitapi.options

import net.kigawa.kutil.unitapi.util.ReflectionUtil

abstract class Options<O: Option>(vararg option: O) {
  val options = listOf(*option)
  
  fun <T: O> firstOrNull(optionClass: Class<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return options.firstOrNull {ReflectionUtil.instanceOf(it.javaClass, optionClass)} as T?
  }
  
  fun contain(option: O): Boolean {
    return options.contains(option)
  }
  
  override fun toString(): String {
    return "RegisterOptions(options=$options)"
  }
}