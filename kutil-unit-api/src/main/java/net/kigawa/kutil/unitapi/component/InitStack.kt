package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.exception.UnitException
import java.util.*

class InitStack(
  private val identifies: List<UnitIdentify<out Any>>,
) {
  constructor(): this(mutableListOf())
  
  fun addIdentify(identify: UnitIdentify<out Any>): InitStack {
    if (identifies.contains(identify)) throw UnitException("unit has bean circular reference", identify)
    val list = LinkedList(identifies)
    list.add(identify)
    return InitStack(identifies)
  }
  
  fun clone(): InitStack {
    return InitStack(LinkedList(identifies))
  }
  
  override fun toString(): String {
    return "InitStack(identifies=${identifies.joinToString {"\n\t \t$it"}}"
  }
}