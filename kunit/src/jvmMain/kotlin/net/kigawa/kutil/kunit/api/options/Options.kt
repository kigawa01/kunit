package net.kigawa.kutil.kunit.api.options

import net.kigawa.kutil.kutil.reflection.KutilReflect

abstract class Options<O: Option>(vararg option: O) {
  val options = listOf(*option)
  
  fun <T: O> firstOrNull(optionClass: Class<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return options.firstOrNull {KutilReflect.instanceOf(it.javaClass, optionClass)} as T?
  }
  
  fun contain(option: O): Boolean {
    return options.contains(option)
  }
  
  override fun toString(): String {
    return "RegisterOptions(options=$options)"
  }
}