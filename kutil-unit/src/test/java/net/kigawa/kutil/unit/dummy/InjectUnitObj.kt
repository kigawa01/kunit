@file:Suppress("unused")

package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.annotation.Kunit

@Kunit
object InjectUnitObj: ParentFieldInjection() {
  init {
    println("aaaaa")
  }
  @Inject
  lateinit var finalField: Unit2
  
  @set:Inject
  lateinit var method: Unit2
  
  @Inject
  lateinit var lateInitField: Unit2
  
  @JvmStatic
  @Inject
  lateinit var staticField: Unit2
  
  @JvmStatic
  @set:Inject
  lateinit var staticMethod: Unit2
}
