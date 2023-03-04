@file:Suppress("unused", "UNUSED_PARAMETER")

package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.annotation.Kunit

@Kunit
class InjectUnit(val injectUnit: Unit2): ParentFieldInjection() {
  @Inject
  val finalField: Unit2? = null
  var method: Unit2? = null
    @Inject set
  
  @Inject
  lateinit var lateInitField: Unit2
  
  companion object {
    @JvmStatic
    @Inject
    val staticField: Unit2? = null
    @JvmStatic
    var staticMethod: Unit2? = null
      @Inject set
  }
}

open class ParentFieldInjection {
  @Inject
  val parentField: Unit2? = null
  @Inject
  var parentmethod: Unit2? = null
    @Inject set
}