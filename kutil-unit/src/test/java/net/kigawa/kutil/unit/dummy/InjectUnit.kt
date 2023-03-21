@file:Suppress("unused", "UNUSED_PARAMETER")

package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.annotation.Kunit

@Kunit
class InjectUnit(val injectUnit: Unit2): ParentFieldInjection() {
  @Inject
  lateinit var finalField: Unit2
  
  @set:Inject
  lateinit var method: Unit2
  
  @Inject
  lateinit var lateInitField: Unit2
  
  companion object {
    @JvmStatic
    @Inject
    lateinit var staticField: Unit2
    
    @JvmStatic
    @set:Inject
    lateinit var staticMethod: Unit2
  }
}

open class ParentFieldInjection {
  @Inject
  lateinit var parentField: Unit2
  
  @set:Inject
  lateinit var parentMethod: Unit2
}