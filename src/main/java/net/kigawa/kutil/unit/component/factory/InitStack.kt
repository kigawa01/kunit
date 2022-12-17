package net.kigawa.kutil.unit.component.factory

import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.util.*

class InitStack(
  private val identifies: MutableList<UnitIdentify<out Any>>,
) {
  constructor(): this(mutableListOf())
  
  fun addIdentify(unitIdentify: UnitIdentify<out Any>) {
    if (identifies.contains(unitIdentify)) throw UnitException("unit has bean circular reference", unitIdentify)
    identifies.add(unitIdentify)
  }
  
  fun clone(): InitStack {
    return InitStack(LinkedList(identifies))
  }
}