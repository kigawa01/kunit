@file:Suppress("unused", "UNUSED_PARAMETER")

package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.annotation.Kunit

@Kunit
class FieldInjectUnit: ParentFieldInjection() {
  @Inject
  val unit1: Unit2? = null
  
  companion object {
    @JvmStatic
    @Inject
    val unit2: Unit2? = null
  }
}

open class ParentFieldInjection {
  @Inject
  val unit3: Unit2? = null
}