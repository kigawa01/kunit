@file:Suppress("unused", "UNUSED_PARAMETER")

package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.annotation.Kunit

@Kunit
object InjectUnitObj: ParentFieldInjection() {
  @Inject
  val finalField: Unit2? = null
  var method: Unit2? = null
    @Inject set
  
  @Inject
  lateinit var lateInitField: Unit2
  
  @JvmStatic
  @Inject
  val staticField: Unit2? = null
  
  @JvmStatic
  var staticMethod: Unit2? = null
    @Inject set
}
