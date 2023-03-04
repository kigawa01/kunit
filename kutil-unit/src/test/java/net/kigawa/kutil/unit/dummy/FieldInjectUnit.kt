@file:Suppress("unused", "UNUSED_PARAMETER")

package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.annotation.Kunit

@Kunit
class FieldInjectUnit: ParentFieldInjection() {
  @Inject
  val finalField: Unit2? = null
  @Inject
  lateinit var lateInitField: Unit2
  
  companion object {
    @JvmStatic
    @Inject
    val staticField: Unit2? = null
  }
}

open class ParentFieldInjection {
  @Inject
  val parentField: Unit2? = null
}