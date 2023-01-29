package net.kigawa.kutil.unit.util

abstract class Options<O: Option>(vararg option: O) {
  protected val options = mutableListOf(*option)
  
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