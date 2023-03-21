package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.exception.CircularReferenceException
import java.util.*

class InitStack(
  val identifies: List<UnitIdentify<out Any>>,
) {
  constructor(): this(mutableListOf())
  
  @Throws(CircularReferenceException::class)
  fun addIdentify(identify: UnitIdentify<out Any>): InitStack {
    if (identifies.contains(identify)) {
      throw CircularReferenceException("unit has bean circular reference", identify, this)
    }
    val list = LinkedList(identifies)
    list.add(identify)
    return InitStack(identifies)
  }
  
  override fun toString(): String {
    return "InitStack(identifies=${identifies}"
  }
}