@file:Suppress("unused")

package net.kigawa.kutil.kunit.dummy

import net.kigawa.kutil.kunit.api.annotation.Kunit
import kotlin.test.Ignore

@Kunit
@Ignore
object InjectUnitObj: ParentFieldInjection() {
  init {
    println("aaaaa")
  }
  @net.kigawa.kutil.kunit.api.annotation.Inject
  lateinit var finalField: Unit2
  
  @set:net.kigawa.kutil.kunit.api.annotation.Inject
  lateinit var method: Unit2
  
  @net.kigawa.kutil.kunit.api.annotation.Inject
  lateinit var lateInitField: Unit2
  
  @JvmStatic
  @net.kigawa.kutil.kunit.api.annotation.Inject
  lateinit var staticField: Unit2
  
  @JvmStatic
  @set:net.kigawa.kutil.kunit.api.annotation.Inject
  lateinit var staticMethod: Unit2
}
