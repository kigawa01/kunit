@file:Suppress("unused", "UNUSED_PARAMETER")

package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.annotation.Kunit

@Kunit
class MethodInjectUnit: ParentMethodInjectUnit() {
  var unit1: Unit2? = null
    @Inject set
  
  companion object {
    @JvmStatic
    var unit2: Unit2? = null
      @Inject set
  }
}

open class ParentMethodInjectUnit {
  @Inject
  var unit3: Unit2? = null
    @Inject set
}